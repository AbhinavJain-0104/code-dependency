package com.example.developer.service;

import com.example.developer.dto.PerformanceInsight;
import com.example.developer.language.JavaLanguageSpecificAnalyzer;
import com.example.developer.model.*;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalysisService {

    @Autowired
    private JavaLanguageSpecificAnalyzer javaAnalyzer;

    @Autowired
    private EnhancedDependencyAnalysisService dependencyAnalysisService;

    @Autowired
    private PerformanceAnalysisService performanceAnalysisService;

    @Autowired
    private APIUsageAnalysisService apiUsageAnalysisService;
    @Autowired private CodeDuplicationService codeDuplicationService;
    @Autowired private TrendAnalysisService trendAnalysisService;
    @Autowired private CodeStyleAnalysisService codeStyleAnalysisService;
    @Autowired private SemanticVersionAnalysisService semanticVersionAnalysisService;

    public Project analyzeProject(String projectDir) {
        Project project = new Project();
        project.setPath(projectDir);
        project.setName(new File(projectDir).getName());
        project.setStatus(ProjectStatus.PROCESSING);

        try {
            List<ProjectModule> modules = analyzeModules(projectDir);
            project.setModules(modules);

            List<ClassEntity> allClasses = project.getModules().stream()
                    .flatMap(module -> module.getClasses().stream())
                    .collect(Collectors.toList());

            List<CompilationUnit> allCompilationUnits = new ArrayList<>();
            for (ClassEntity classEntity : allClasses) {
                try {
                    CompilationUnit cu = StaticJavaParser.parse(new File(classEntity.getFilePath()));
                    allCompilationUnits.add(cu);
                } catch (IOException e) {
                    System.err.println("Error parsing file: " + classEntity.getFilePath());
                    e.printStackTrace();
                }
            }

            project.setDependencies(dependencyAnalysisService.analyzeDependencies(project, allClasses));
            project.setExternalDependencies(analyzeExternalDependencies(projectDir));
            project.setPrimaryLanguage(detectPrimaryLanguage(projectDir));
            project.setApiUsagePatterns(apiUsageAnalysisService.analyzeAPIUsage(allClasses, allCompilationUnits));
            project.setDuplicationInfo(codeDuplicationService.detectDuplication(allClasses));
            project.setTrends(trendAnalysisService.analyzeTrends(projectDir));
            project.setStyleInconsistencies(codeStyleAnalysisService.analyzeCodeStyle(allClasses));
            project.setVersionIssues(semanticVersionAnalysisService.analyzeVersions(project.getExternalDependencies()));

            project.setStatus(ProjectStatus.ANALYSIS_COMPLETED);
        } catch (Exception e) {
            project.setStatus(ProjectStatus.ANALYSIS_FAILED);
            project.setErrorMessage(e.getMessage());
        }

        return project;
    }
    private List<ProjectModule> analyzeModules(String projectDir) throws IOException {
        List<ProjectModule> modules = new ArrayList<>();
        ProjectModule mainModule = new ProjectModule();
        mainModule.setName("main");
        mainModule.setPath(projectDir);
        mainModule.setClasses(new HashSet<>());

        Files.walkFileTree(Paths.get(projectDir), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".java")) {
                    String content = new String(Files.readAllBytes(file));
                    ClassEntity classEntity = javaAnalyzer.extractClassInfo(content, file.toString());

                    // Perform performance analysis
                    CompilationUnit cu = StaticJavaParser.parse(content);
                    performanceAnalysisService.analyzePerformance(classEntity, cu);

                    mainModule.getClasses().add(classEntity);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        modules.add(mainModule);
        return modules;
    }
    private List<ExternalDependency> analyzeExternalDependencies(String projectDir) {
        List<ExternalDependency> externalDependencies = new ArrayList<>();
        File pomFile = new File(projectDir, "pom.xml");

        if (pomFile.exists()) {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(pomFile);
                doc.getDocumentElement().normalize();

                NodeList dependencyList = doc.getElementsByTagName("dependency");
                for (int i = 0; i < dependencyList.getLength(); i++) {
                    Element dependency = (Element) dependencyList.item(i);
                    String groupId = getTagValue("groupId", dependency);
                    String artifactId = getTagValue("artifactId", dependency);
                    String version = getTagValue("version", dependency);
                    String scope = getTagValue("scope", dependency);

                    ExternalDependency externalDependency = new ExternalDependency();
                    externalDependency.setName(groupId + ":" + artifactId);
                    externalDependency.setVersion(version);
                    externalDependency.setPackageManager("maven");
                    externalDependency.setType(scope != null ? scope : "compile");

                    externalDependencies.add(externalDependency);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return externalDependencies;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getNodeValue();
        }
        return null;
    }

    private String detectPrimaryLanguage(String projectDir) throws IOException {
        Map<String, Integer> languageCounts = new HashMap<>();

        Files.walkFileTree(Paths.get(projectDir), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String fileName = file.getFileName().toString().toLowerCase();
                if (fileName.endsWith(".java")) {
                    languageCounts.merge("Java", 1, Integer::sum);
                } else if (fileName.endsWith(".js") || fileName.endsWith(".ts")) {
                    languageCounts.merge("JavaScript", 1, Integer::sum);
                }
                // Add more language detections as needed
                return FileVisitResult.CONTINUE;
            }
        });

        return languageCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");
    }
}