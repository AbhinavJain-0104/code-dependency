package com.example.developer.service;

import com.example.developer.config.StorageConfig;
import com.example.developer.exception.CustomException;
import com.example.developer.model.*;
import com.example.developer.repository.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AnalysisService {
    private static final Logger logger = LoggerFactory.getLogger(AnalysisService.class);

    private final StorageConfig storageConfig;
    private final ProjectRepository projectRepository;
    private final ModuleRepository moduleRepository;
    private final ClassRepository classRepository;
    private final DependencyRepository dependencyRepository;
    private final AIService aiService;

    @Autowired
    public AnalysisService(StorageConfig storageConfig,
                           ProjectRepository projectRepository,
                           ModuleRepository moduleRepository,
                           ClassRepository classRepository,
                           DependencyRepository dependencyRepository,
                           AIService aiService) {
        this.storageConfig = storageConfig;
        this.projectRepository = projectRepository;
        this.moduleRepository = moduleRepository;
        this.classRepository = classRepository;
        this.dependencyRepository = dependencyRepository;
        this.aiService = aiService;
    }

    @Async("taskExecutor")
    public CompletableFuture<Project> processProjectUploadAsync(String gitRepoUrl) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Project project = new Project();
                project.setStatus(ProjectStatus.PROCESSING);
                project.setId(projectRepository.save(project).getId()); // Add this line

                String projectPath = cloneRepository(gitRepoUrl);
                project.setPath(projectPath);
                project.setName(extractProjectName(gitRepoUrl));

                List<ProjectModule> modules = analyzeProjectStructure(projectPath, project);
                project.setModules(modules);

                for (ProjectModule module : modules) {
                    module.setProject(project);
                    Set<ClassEntity> classes = analyzeClasses(module);
                    module.setClasses(classes);
                    for (ClassEntity classEntity : classes) {
                        classEntity.setModule(module);
                    }
                }

                List<Dependency> dependencies = analyzeDependencies(project);
                project.setDependencies(dependencies);

                project.setStatus(ProjectStatus.COMPLETED);
                return project;
            } catch (Exception e) {
                logger.error("Error processing project upload", e);
                Project failedProject = new Project();
                failedProject.setStatus(ProjectStatus.FAILED);
                failedProject.setErrorMessage(e.getMessage());
                return failedProject;
            }
        });
    }

    private String cloneRepository(String gitRepoUrl) throws IOException, GitAPIException {
        String repoName = extractRepoName(gitRepoUrl);
        Path repoPath = storageConfig.getStoragePath().resolve(repoName);

        if (Files.exists(repoPath)) {
            // Repository already exists, pull latest changes
            try (Git git = Git.open(repoPath.toFile())) {
                git.pull().call();
            }
        } else {
            // Clone new repository
            Git.cloneRepository()
                    .setURI(gitRepoUrl)
                    .setDirectory(repoPath.toFile())
                    .call();
        }

        return repoPath.toString();

    }

    private String extractRepoName(String gitRepoUrl) {
        String[] parts = gitRepoUrl.split("/");
        return parts[parts.length - 1].replace(".git", "");
    }

    private String extractProjectName(String gitRepoUrl) {
        return extractRepoName(gitRepoUrl);
    }

    private List<ProjectModule> analyzeProjectStructure(String projectPath, Project project) {
        List<ProjectModule> modules = new ArrayList<>();
        File rootDir = new File(projectPath);

        if (rootDir.isDirectory()) {
            ProjectModule rootModule = new ProjectModule();
            rootModule.setName(project.getName());
            rootModule.setPath(projectPath);
            rootModule.setProject(project);
            List<PackageEntity> packages = analyzePackages(rootDir);
            rootModule.setPackages(packages);

            Map<String, List<PackageEntity>> modulePackages = packages.stream()
                    .collect(Collectors.groupingBy(pkg -> {
                        String[] parts = pkg.getName().split("\\.");
                        return parts.length > 2 ? parts[2] : "default"; // Ensure safe access
                    }));

            for (Map.Entry<String, List<PackageEntity>> entry : modulePackages.entrySet()) {
                ProjectModule module = new ProjectModule();
                String moduleName = entry.getKey();
                module.setName(project.getName());
                module.setPath(project.getPath());
                module.setProject(project);
                module.setPackages(entry.getValue());
                modules.add(module);
            }
        }

        return modules;
    }

    private List<PackageEntity> analyzePackages(File moduleDir) {
        Map<String, PackageEntity> packageMap = new HashMap<>();

        try {
            Files.walkFileTree(moduleDir.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".java")) {
                        ClassEntity classEntity = analyzeJavaFile(file.toFile(), null);
                        if (classEntity != null) {
                            String packageName = classEntity.getPackageName();
                            PackageEntity packageEntity = packageMap.computeIfAbsent(packageName, k -> {
                                PackageEntity p = new PackageEntity();
                                p.setName(packageName);
                                p.setClasses(new ArrayList<>());
                                return p;
                            });
                            packageEntity.getClasses().add(classEntity);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new CustomException("Error analyzing packages", e);
        }

        return new ArrayList<>(packageMap.values());
    }

    private Set<ClassEntity> analyzeClasses(ProjectModule module) {
        Set<ClassEntity> classes = new HashSet<>();
        Path modulePath = Paths.get(module.getPath());

        logger.info("Analyzing classes in module: {}", module.getName());
        logger.info("Module path: {}", modulePath.toAbsolutePath());

        if (!Files.exists(modulePath)) {
            logger.error("Module path does not exist: {}", modulePath.toAbsolutePath());
            return classes; // Return early if the path does not exist
        }

        try {
            Files.walkFileTree(modulePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".java")) {
                        logger.debug("Processing Java file: {}", file);
                        try {
                            ClassEntity classEntity = analyzeJavaFile(file.toFile(), module);
                            if (classEntity != null) {
                                classes.add(classEntity);
                            }
                        } catch (Exception e) {
                            logger.warn("Error processing file: {}. Error: {}", file, e.getMessage());
                            // Continue processing other files
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    logger.warn("Failed to visit file: {}. Error: {}", file, exc.getMessage());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            logger.error("Error walking file tree for module: {}. Error: {}", module.getName(), e.getMessage());
            throw new CustomException("Error analyzing classes in module: " + module.getName(), e);
        }

        logger.info("Finished analyzing classes in module: {}. Found {} classes.", module.getName(), classes.size());
        return classes;
    }

    private ClassEntity analyzeJavaFile(File file, ProjectModule module) throws IOException {
        String content = new String(Files.readAllBytes(file.toPath()));
        String packageName = extractPackage(content);
        List<String> classNames = extractClassNames(content);
        Set<String> imports = extractImports(content);
        Set<String> methodCalls = extractMethodCalls(content);

        if (!classNames.isEmpty()) {
            ClassEntity classEntity = new ClassEntity();
            classEntity.setName(classNames.get(0));
            classEntity.setPackageName(packageName);
            classEntity.setModule(module);
            classEntity.setImports(imports);
            classEntity.setMethodCalls(methodCalls);
            classEntity.setFullyQualifiedName(packageName + "." + classNames.get(0));

            if (classNames.size() > 1) {
                Set<String> uniqueInnerClasses = new LinkedHashSet<>(classNames.subList(1, classNames.size()));
                classEntity.setInnerClasses(new ArrayList<>(uniqueInnerClasses));
            }

            String aiDescription = generateCustomAIDescription(classEntity, imports, methodCalls);
            classEntity.setAiDescription(aiDescription);

            return classEntity;
        }
        return null;
    }

    private String generateCustomAIDescription(ClassEntity classEntity, Set<String> imports, Set<String> methodCalls) {
        StringBuilder description = new StringBuilder();
        description.append("This class contains: ");
        description.append(imports.size()).append(" imports, ");
        description.append(methodCalls.size()).append(" method calls");
        if (!classEntity.getInnerClasses().isEmpty()) {
            description.append(", ").append(classEntity.getInnerClasses().size())
                    .append(" inner classes (")
                    .append(String.join(", ", classEntity.getInnerClasses()))
                    .append(")");
        }
        description.append(".\n");

        Set<String> dependencies = new HashSet<>();
        for (String methodCall : methodCalls) {
            String[] parts = methodCall.split("\\.");
            if (parts.length > 1) {
                dependencies.add(parts[0]);
            }
        }
        if (!dependencies.isEmpty()) {
            description.append("It uses the following classes: ");
            description.append(String.join(", ", dependencies));
        }

        return description.toString();
    }

    private String extractPackage(String content) {
        Pattern pattern = Pattern.compile("package\\s+(\\S+);");
        Matcher matcher = pattern.matcher(content);
        return matcher.find() ? matcher.group(1) : "";
    }

    private List<String> extractClassNames(String content) {
        List<String> classNames = new ArrayList<>();
        Pattern pattern = Pattern.compile("class\\s+(\\w+)");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            classNames.add(matcher.group(1));
        }
        return classNames;
    }

    private Set<String> extractImports(String content) {
        Set<String> imports = new HashSet<>();
        Pattern pattern = Pattern.compile("import\\s+(\\S+);");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            imports.add(matcher.group(1));
        }
        return imports;
    }

    private Set<String> extractMethodCalls(String content) {
        Set<String> methodCalls = new HashSet<>();
        Pattern pattern = Pattern.compile("\\b(\\w+)\\s*\\(");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            methodCalls.add(matcher.group(1));
        }
        return methodCalls;
    }

    private List<Dependency> analyzeDependencies(Project project) {
        List<Dependency> dependencies = new ArrayList<>();
        for (ProjectModule module : project.getModules()) {
            for (ClassEntity sourceClass : module.getClasses()) {
                for (String methodCall : sourceClass.getMethodCalls()) {
                    String[] parts = methodCall.split("\\.");
                    if (parts.length > 1) {
                        String targetClassName = parts[0];
                        dependencies.add(new Dependency(sourceClass.getFullyQualifiedName(), targetClassName, "method"));
                    }
                }
            }
        }
        return dependencies;
    }

    @Transactional(readOnly = true)
    public Project getProjectWithDetails(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new CustomException("Project not found"));
    }
}