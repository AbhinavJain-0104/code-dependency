package com.example.developer.service;

import com.example.developer.dependency.DependencyResolver;
import com.example.developer.dependency.DependencyResolverFactory;
import com.example.developer.dto.ProjectDTO;
import com.example.developer.language.FrameworkSpecificAnalyzer;
import com.example.developer.language.LanguageSpecificAnalyzer;
import com.example.developer.model.*;
import com.example.developer.packagemanager.PackageManagerAnalyzer;
import com.example.developer.util.FileTypeRegistry;
import com.example.developer.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProjectAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(ProjectAnalyzer.class);

    private final Map<String, LanguageSpecificAnalyzer> languageAnalyzers;
    private final Map<String, PackageManagerAnalyzer> packageManagerAnalyzers;
    private final Map<String, FrameworkSpecificAnalyzer> frameworkAnalyzers;
    private final DependencyResolverFactory dependencyResolverFactory;
    private final FileTypeRegistry fileTypeRegistry;

    @Autowired
    public ProjectAnalyzer(List<LanguageSpecificAnalyzer> languageAnalyzers,
                           List<PackageManagerAnalyzer> packageManagerAnalyzers,
                           List<FrameworkSpecificAnalyzer> frameworkAnalyzers,
                           DependencyResolverFactory dependencyResolverFactory,
                           FileTypeRegistry fileTypeRegistry) {
        this.languageAnalyzers = languageAnalyzers.stream()
                .collect(Collectors.toMap(LanguageSpecificAnalyzer::getLanguage, Function.identity()));
        this.packageManagerAnalyzers = packageManagerAnalyzers.stream()
                .collect(Collectors.toMap(PackageManagerAnalyzer::getPackageManager, Function.identity()));
        this.frameworkAnalyzers = frameworkAnalyzers.stream()
                .collect(Collectors.toMap(FrameworkSpecificAnalyzer::getFramework, Function.identity()));
        this.dependencyResolverFactory = dependencyResolverFactory;
        this.fileTypeRegistry = fileTypeRegistry;

        logger.debug("Initialized analyzers: Languages: {}, Package Managers: {}, Frameworks: {}",
                this.languageAnalyzers.keySet(), this.packageManagerAnalyzers.keySet(), this.frameworkAnalyzers.keySet());
    }


    public Project analyzeProject(String projectPath) {
        logger.info("Starting analysis of project: {}", projectPath);
        Project project = new Project();
        project.setPath(projectPath);


        try {
            List<ProjectModule> modules = analyzeProjectStructure(projectPath);
            project.setModules(modules);

            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (ProjectModule module : modules) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> analyzeModule(module));
                futures.add(future);
            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            if (modules.stream().anyMatch(module -> module.getStatus() == ProjectStatus.ANALYSIS_FAILED)) {
                project.setStatus(ProjectStatus.ANALYSIS_FAILED);
            } else {
                project.setStatus(ProjectStatus.ANALYSIS_COMPLETED);
            }

            List<ExternalDependency> externalDependencies = analyzePackageManagerFiles(projectPath);
            project.setExternalDependencies(externalDependencies);

            List<Dependency> allDependencies = modules.stream()
                    .flatMap(module -> module.getDependencies().stream())
                    .collect(Collectors.toList());
            handleCircularDependencies(allDependencies);

        } catch (Exception e) {
            logger.error("Error analyzing project", e);
            project.setStatus(ProjectStatus.ANALYSIS_FAILED);
            project.setErrorMessage(e.getMessage());
        }

        logger.info("Completed analysis of project: {}", projectPath);
        return project;
    }

    private List<ProjectModule> analyzeProjectStructure(String projectPath) throws IOException {
        List<ProjectModule> modules = new ArrayList<>();
        Files.walkFileTree(Paths.get(projectPath), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (isModuleDirectory(dir)) {
                    ProjectModule module = new ProjectModule();
                    module.setName(dir.getFileName().toString());
                    module.setPath(dir.toString());
                    modules.add(module);
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }
        });
        logger.debug("Detected {} modules in project", modules.size());
        return modules;
    }

//    private List<ProjectModule> analyzeProjectStructure(String projectPath) throws IOException {
//        List<ProjectModule> modules = new ArrayList<>();
//        Files.walkFileTree(Paths.get(projectPath), new SimpleFileVisitor<Path>() {
//            @Override
//            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//                if (isModuleDirectory(dir)) {
//                    ProjectModule module = new ProjectModule();
//                    module.setName(dir.getFileName().toString());
//                    module.setPath(dir.toString());
//                    modules.add(module);
//                    return FileVisitResult.SKIP_SUBTREE;
//                }
//                return FileVisitResult.CONTINUE;
//            }
//        });
//        logger.debug("Detected {} modules in project", modules.size());
//        return modules;
//    }

    private boolean isModuleDirectory(Path dir) {
        return Files.exists(dir.resolve("pom.xml")) ||
                Files.exists(dir.resolve("build.gradle")) ||
                Files.exists(dir.resolve("package.json")) ||
                Files.exists(dir.resolve("requirements.txt")) ||
                Files.exists(dir.resolve("Cargo.toml")) ||
                Files.exists(dir.resolve("go.mod")) ||
                (Files.exists(dir.resolve("src")) && Files.isDirectory(dir.resolve("src")));
    }

    private void analyzeModule(ProjectModule module) {
        logger.info("Analyzing module: {}", module.getName());
        try {
            Set<ClassEntity> classes = analyzeClasses(module.getPath());
            module.setClasses(classes);
            List<Dependency> dependencies = analyzeDependencies(classes);
            module.setDependencies(dependencies);
            module.setStatus(ProjectStatus.ANALYSIS_COMPLETED);
        } catch (IOException e) {
            logger.error("Error analyzing module: " + module.getName(), e);
            module.setStatus(ProjectStatus.ANALYSIS_FAILED);
            module.setErrorMessage(e.getMessage());
        }
    }

    private Set<ClassEntity> analyzeClasses(String modulePath) throws IOException {
        Set<ClassEntity> classEntities = new HashSet<>();
        Files.walkFileTree(Paths.get(modulePath), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String filePath = file.toString();
                String fileExtension = FileUtils.getFileExtension(filePath);
                String language = fileTypeRegistry.getLanguageForExtension(fileExtension);
                String framework = fileTypeRegistry.getFrameworkForExtension(fileExtension);

                logger.debug("Analyzing file: {}", filePath);
                logger.debug("Detected language: {}, framework: {}", language, framework);

                if (language != null) {
                    String content = FileUtils.readFileContent(filePath);
                    ClassEntity classEntity = null;

                    if (framework != null && frameworkAnalyzers.containsKey(framework)) {
                        logger.debug("Using framework analyzer for: {}", framework);
                        FrameworkSpecificAnalyzer frameworkAnalyzer = frameworkAnalyzers.get(framework);
                        classEntity = frameworkAnalyzer.extractComponentInfo(content, filePath);
                    } else if (languageAnalyzers.containsKey(language)) {
                        logger.debug("Using language analyzer for: {}", language);
                        LanguageSpecificAnalyzer analyzer = languageAnalyzers.get(language);
                        classEntity = analyzer.extractClassInfo(content, filePath);
                    } else {
                        logger.warn("No analyzer found for language: {}", language);
                    }

                    if (classEntity != null) {
                        classEntities.add(classEntity);
                        logger.debug("Added class entity: {}", classEntity.getName());
                    } else {
                        logger.warn("Failed to create class entity for file: {}", filePath);
                    }
                } else {
                    logger.debug("Skipping file (no language detected): {}", filePath);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return classEntities;
    }

    private List<Dependency> analyzeDependencies(Set<ClassEntity> classes) {
        List<Dependency> dependencies = new ArrayList<>();
        for (ClassEntity classEntity : classes) {
            DependencyResolver resolver = dependencyResolverFactory.getResolver(classEntity.getLanguage());
            if (resolver == null) {
                logger.warn("No dependency resolver found for language: {}", classEntity.getLanguage());
                continue;
            }
            dependencies.addAll(resolver.resolveDependencies(classEntity, classes));
        }
        return dependencies;
    }

    private List<ExternalDependency> analyzePackageManagerFiles(String projectPath) throws IOException {
        List<ExternalDependency> externalDependencies = new ArrayList<>();
        Files.walkFileTree(Paths.get(projectPath), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String filePath = file.toString();
                String fileName = file.getFileName().toString();

                PackageManagerAnalyzer analyzer = null;
                if (fileName.equals("package.json")) {
                    analyzer = packageManagerAnalyzers.get("npm");
                } else if (fileName.equals("pom.xml")) {
                    analyzer = packageManagerAnalyzers.get("maven");
                } else if (fileName.equals("requirements.txt")) {
                    analyzer = packageManagerAnalyzers.get("pip");
                }

                if (analyzer != null) {
                    externalDependencies.addAll(analyzer.analyzePackageFile(filePath));
                }

                return FileVisitResult.CONTINUE;
            }
        });
        return externalDependencies;
    }

    private void handleCircularDependencies(List<Dependency> dependencies) {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (Dependency dependency : dependencies) {
            if (!visited.contains(dependency.getSource())) {
                detectCircularDependency(dependency.getSource(), dependencies, visited, recursionStack);
            }
        }
    }

    private void detectCircularDependency(String current, List<Dependency> dependencies, Set<String> visited, Set<String> recursionStack) {
        visited.add(current);
        recursionStack.add(current);

        for (Dependency dependency : dependencies) {
            if (dependency.getSource().equals(current)) {
                String target = dependency.getTarget();
                if (recursionStack.contains(target)) {
                    logger.warn("Circular dependency detected: {} <-> {}", current, target);
                    dependency.setCircular(true);
                } else if (!visited.contains(target)) {
                    detectCircularDependency(target, dependencies, visited, recursionStack);
                }
            }
        }

        recursionStack.remove(current);
    }
}