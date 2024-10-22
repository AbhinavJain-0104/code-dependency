package com.example.developer.analyzer;

import com.example.developer.dependency.NpmDependencyAnalyzer;
import com.example.developer.framework.FrameworkDetector;
import com.example.developer.model.*;
import com.example.developer.language.JavaScriptLanguageSpecificAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JavaScriptProjectAnalyzer {

    private final List<JavaScriptLanguageSpecificAnalyzer> jsAnalyzers;
    private final NpmDependencyAnalyzer npmDependencyAnalyzer;
    private final FrameworkDetector frameworkDetector;

    @Autowired
    public JavaScriptProjectAnalyzer(List<JavaScriptLanguageSpecificAnalyzer> jsAnalyzers,
                                     NpmDependencyAnalyzer npmDependencyAnalyzer,
                                     FrameworkDetector frameworkDetector) {
        this.jsAnalyzers = jsAnalyzers;
        this.npmDependencyAnalyzer = npmDependencyAnalyzer;
        this.frameworkDetector = frameworkDetector;
    }

    public Project analyzeProject(String projectPath) throws IOException {
        Project project = new Project();
        project.setPath(projectPath);
        project.setName(Paths.get(projectPath).getFileName().toString());

        List<ProjectModule> modules = detectModules(projectPath);
        project.setModules(modules);

        Set<ClassEntity> classes = new HashSet<>();
        List<Dependency> dependencies = new ArrayList<>();

        for (ProjectModule module : modules) {
            Set<ClassEntity> moduleClasses = analyzeModule(module);
            classes.addAll(moduleClasses);
            dependencies.addAll(extractDependencies(moduleClasses));
        }

        project.setClasses(new ArrayList<>(classes));
        project.setDependencies(dependencies);
        project.setExternalDependencies(npmDependencyAnalyzer.analyzeDependencies(projectPath));
        project.setDetectedFrameworks(frameworkDetector.detectFrameworks(projectPath));
        project.setPrimaryLanguage(determinePrimaryLanguage(classes));

        return project;
    }

    private List<ProjectModule> detectModules(String projectPath) throws IOException {
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
        if (modules.isEmpty()) {
            ProjectModule mainModule = new ProjectModule();
            mainModule.setName("main");
            mainModule.setPath(projectPath);
            modules.add(mainModule);
        }
        return modules;
    }

    private boolean isModuleDirectory(Path dir) {
        return Files.exists(dir.resolve("package.json"));
    }

    private Set<ClassEntity> analyzeModule(ProjectModule module) throws IOException {
        Set<ClassEntity> classes = new HashSet<>();
        Files.walkFileTree(Paths.get(module.getPath()), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                for (JavaScriptLanguageSpecificAnalyzer analyzer : jsAnalyzers) {
                    if (analyzer.canHandle(file.toString())) {
                        String content = new String(Files.readAllBytes(file));
                        classes.add(analyzer.extractClassInfo(content, file.toString()));
                        break;
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return classes;
    }

    private List<Dependency> extractDependencies(Set<ClassEntity> classes) {
        List<Dependency> dependencies = new ArrayList<>();
        for (ClassEntity classEntity : classes) {
            for (JavaScriptLanguageSpecificAnalyzer analyzer : jsAnalyzers) {
                dependencies.addAll(analyzer.extractDependencies(classEntity, classes));
            }
        }
        return dependencies;
    }

    private String determinePrimaryLanguage(Set<ClassEntity> classes) {
        Map<String, Long> languageCounts = classes.stream()
                .collect(Collectors.groupingBy(this::getFileExtension, Collectors.counting()));
        return languageCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("JavaScript");
    }

    private String getFileExtension(ClassEntity classEntity) {
        String fileName = Paths.get(classEntity.getFilePath()).getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}