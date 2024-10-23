package com.example.developer.controller;

import com.example.developer.dto.*;
import com.example.developer.model.*;
import com.example.developer.service.AnalysisService;
import com.example.developer.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final AnalysisService analysisService;
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    @Autowired
    public ProjectController(AnalysisService analysisService, ProjectService projectService) {
        this.analysisService = analysisService;
        this.projectService = projectService;
    }

//    @PostMapping("/analyze")
//    public ResponseEntity<ProjectDTO> analyzeGitHubProject(@RequestParam("gitRepoUrl") String gitRepoUrl) {
//        try {
//            Project analyzedProject = projectService.analyzeGitHubProject(gitRepoUrl);
//            ProjectDTO projectDTO = convertToDTO(analyzedProject);
//            return ResponseEntity.ok(projectDTO);
//        } catch (Exception e) {
//            logger.error("Error analyzing GitHub project", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ProjectDTO(null, ProjectStatus.ANALYSIS_FAILED, e.getMessage()));
//        }
//    }


    @GetMapping("/analyze")
    public ResponseEntity<ProjectDTO> analyzeGitHubProject(@RequestParam("gitRepoUrl") String gitRepoUrl) {
        try {
            logger.info("Received request to analyze project: {}", gitRepoUrl);
            Project analyzedProject = projectService.analyzeGitHubProject(gitRepoUrl);
            ProjectDTO projectDTO = convertToDTO(analyzedProject);
            logger.info("Analysis completed successfully for: {}", gitRepoUrl);
            return ResponseEntity.ok(projectDTO);
        } catch (Exception e) {
            logger.error("Error analyzing GitHub project: {}", gitRepoUrl, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProjectDTO(null, ProjectStatus.ANALYSIS_FAILED, e.getMessage()));
        }
    }
    private ProjectDTO convertToDTO(Project project) {
        if (project == null) {
            return null;
        }

        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setPath(project.getPath());
        dto.setStatus(project.getStatus());
        dto.setErrorMessage(project.getErrorMessage());

        if (project.getApiUsagePatterns() != null) {
            dto.setApiUsagePatterns(project.getApiUsagePatterns().stream()
                    .filter(Objects::nonNull)
                    .map(this::convertToAPIUsagePatternDTO)
                    .collect(Collectors.toList()));
        }

        if (project.getDuplicationInfo() != null) {
            dto.setDuplicationInfo(project.getDuplicationInfo().stream()
                    .filter(Objects::nonNull)
                    .map(this::convertToDuplicationInfoDTO)
                    .collect(Collectors.toList()));
        }

        if (project.getTrends() != null) {
            dto.setTrends(project.getTrends().entrySet().stream()
                    .filter(e -> e.getKey() != null && e.getValue() != null)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().stream()
                                    .filter(Objects::nonNull)
                                    .map(this::convertToMetricTrendDTO)
                                    .collect(Collectors.toList())
                    )));
        }

        if (project.getStyleInconsistencies() != null) {
            dto.setStyleInconsistencies(project.getStyleInconsistencies().stream()
                    .filter(Objects::nonNull)
                    .map(this::convertToStyleInconsistencyDTO)
                    .collect(Collectors.toList()));
        }

        if (project.getVersionIssues() != null) {
            dto.setVersionIssues(project.getVersionIssues().stream()
                    .filter(Objects::nonNull)
                    .map(this::convertToVersionIssueDTO)
                    .collect(Collectors.toList()));
        }

        if (project.getModules() != null) {
            dto.setModules(project.getModules().stream()
                    .filter(Objects::nonNull)
                    .map(module -> {
                        ProjectModuleDTO moduleDTO = new ProjectModuleDTO();
                        moduleDTO.setName(project.getName());
                        moduleDTO.setPath(module.getPath());
                        moduleDTO.setStatus(module.getStatus());

                        if (module.getClasses() != null) {
                            moduleDTO.setPackages(module.getClasses().stream()
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.groupingBy(ClassEntity::getPackageName))
                                    .entrySet().stream()
                                    .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                                    .map(entry -> {
                                        PackageDTO packageDTO = new PackageDTO();
                                        packageDTO.setName(entry.getKey());

                                        packageDTO.setClasses(entry.getValue().stream()
                                                .filter(Objects::nonNull)
                                                .map(this::convertToClassDTO)
                                                .filter(Objects::nonNull)
                                                .collect(Collectors.toList()));

                                        return packageDTO;
                                    }).collect(Collectors.toList()));
                        }

                        return moduleDTO;
                    }).collect(Collectors.toList()));
        }

        if (project.getDependencies() != null) {
            dto.setDependencies(project.getDependencies().stream()
                    .filter(Objects::nonNull)
                    .map(dep -> {
                        DependencyDTO dependencyDTO = new DependencyDTO();
                        dependencyDTO.setSource(dep.getSource());
                        dependencyDTO.setTarget(dep.getTarget());
                        dependencyDTO.setType(dep.getDependencyType());
                        dependencyDTO.setCircular(dep.isCircular());
                        return dependencyDTO;
                    }).collect(Collectors.toList()));
        }

        if (project.getExternalDependencies() != null) {
            dto.setExternalDependencies(project.getExternalDependencies().stream()
                    .filter(Objects::nonNull)
                    .map(extDep -> {
                        ExternalDependencyDTO extDepDTO = new ExternalDependencyDTO();
                        extDepDTO.setName(extDep.getName());
                        extDepDTO.setVersion(extDep.getVersion());
                        extDepDTO.setPackageManager(extDep.getPackageManager());
                        extDepDTO.setType(extDep.getType());
                        return extDepDTO;
                    }).collect(Collectors.toList()));
        }

        dto.setPrimaryLanguage(project.getPrimaryLanguage());
        dto.setDetectedFrameworks(project.getDetectedFrameworks());

        return dto;
    }

    // ... other methods (convertToClassDTO, convertToAPIUsagePatternDTO, etc.) remain unchanged


    private List<ProjectModuleDTO> convertToModules(List<ProjectModule> modules) {
        if (modules == null) {
            return null;
        }
        return modules.stream()
                .filter(Objects::nonNull)
                .map(module -> {
                    ProjectModuleDTO moduleDTO = new ProjectModuleDTO();
                    moduleDTO.setName(module.getName());
                    moduleDTO.setPath(module.getPath());
                    moduleDTO.setStatus(module.getStatus());

                    if (module.getClasses() != null) {
                        moduleDTO.setPackages(convertToPackages(new ArrayList<>(module.getClasses())));
                    }

                    return moduleDTO;
                }).collect(Collectors.toList());
    }

    private List<PackageDTO> convertToPackages(List<ClassEntity> classes) {
        if (classes == null) {
            return null;
        }
        Map<String, List<ClassEntity>> packageMap = classes.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(ClassEntity::getPackageName));

        return packageMap.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .map(entry -> {
                    PackageDTO packageDTO = new PackageDTO();
                    packageDTO.setName(entry.getKey());
                    packageDTO.setClasses(convertToClassDTOs(entry.getValue()));
                    return packageDTO;
                })
                .collect(Collectors.toList());
    }

    private List<ClassDTO> convertToClassDTOs(List<ClassEntity> classes) {
        if (classes == null) {
            return null;
        }
        return classes.stream()
                .filter(Objects::nonNull)
                .map(this::convertToClassDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private ClassDTO convertToClassDTO(ClassEntity classEntity) {
        if (classEntity == null) {
            return null;
        }

        ClassDTO classDTO = new ClassDTO();
        classDTO.setName(classEntity.getName());
        classDTO.setPackageName(classEntity.getPackageName());
        classDTO.setFilePath(classEntity.getFilePath());
        classDTO.setLanguage(classEntity.getLanguage());
        classDTO.setAiDescription(classEntity.getAiDescription());
        classDTO.setFields(classEntity.getFields());
        classDTO.setMethods(classEntity.getMethods());
        classDTO.setUsedClasses(classEntity.getUsedClasses());
        classDTO.setMetrics(classEntity.getMetrics());

        if (classEntity.getPerformanceInsights() != null && !classEntity.getPerformanceInsights().isEmpty()) {
            logger.info("Performance insights found for class: " + classEntity.getName());
            classDTO.setPerformanceInsights(classEntity.getPerformanceInsights().stream()
                    .map(this::convertToPerformanceInsightDTO)
                    .collect(Collectors.toList()));
        } else {
            logger.info("No performance insights found for class: " + classEntity.getName());
        }
        if ("Java".equals(classEntity.getLanguage())) {
            classDTO.setInnerClasses(classEntity.getInnerClasses());
            classDTO.setInterfaces(classEntity.getInterfaces());
            classDTO.setSuperclass(classEntity.getSuperclass());
            classDTO.setModifiers(classEntity.getModifiers());
        } else if ("JavaScript".equals(classEntity.getLanguage()) || "TypeScript".equals(classEntity.getLanguage())) {
            classDTO.setInnerFunctions(classEntity.getInnerFunctions());
            classDTO.setProperties(classEntity.getProperties());
            classDTO.setFramework(classEntity.getFramework());
            classDTO.setApiRoute(classEntity.isApiRoute());
            classDTO.setHasGetServerSideProps(classEntity.isHasGetServerSideProps());
            classDTO.setHasGetStaticProps(classEntity.isHasGetStaticProps());
        }

        return classDTO;
    }

    private String extractClassName(String fullClassName) {
        if (fullClassName == null) {
            return null;
        }
        String[] parts = fullClassName.split("\\.");
        return parts[parts.length - 1];
    }

    private PerformanceInsightDTO convertToPerformanceInsightDTO(PerformanceInsight insight) {
        if (insight == null) {
            return null;
        }
        PerformanceInsightDTO dto = new PerformanceInsightDTO();
        dto.setClassName(insight.getClassName());
        dto.setIssue(insight.getIssue());
        dto.setLineNumber(insight.getLineNumber());
        dto.setSuggestion(insight.getSuggestion());
        return dto;
    }

    private APIUsagePatternDTO convertToAPIUsagePatternDTO(APIUsagePattern pattern) {
        if (pattern == null) {
            return null;
        }
        APIUsagePatternDTO dto = new APIUsagePatternDTO();
        dto.setApiCall(pattern.getApiCall());
        dto.setUsageCount(pattern.getUsageCount());
        dto.setUsageLocations(pattern.getUsageLocations());
        return dto;
    }

    private MetricTrendDTO convertToMetricTrendDTO(MetricTrend trend) {
        if (trend == null) {
            return null;
        }
        MetricTrendDTO dto = new MetricTrendDTO();
        dto.setCommitId(trend.getCommitId());
        dto.setTimestamp(trend.getTimestamp());
        dto.setValue(trend.getValue());
        return dto;
    }

    private StyleInconsistencyDTO convertToStyleInconsistencyDTO(StyleInconsistency inconsistency) {
        if (inconsistency == null) {
            return null;
        }
        StyleInconsistencyDTO dto = new StyleInconsistencyDTO();
        dto.setLocation(inconsistency.getLocation());
        dto.setDescription(inconsistency.getDescription());
        return dto;
    }

    private VersionIssueDTO convertToVersionIssueDTO(VersionIssue issue) {
        if (issue == null) {
            return null;
        }
        VersionIssueDTO dto = new VersionIssueDTO();
        dto.setDependencyName(issue.getDependencyName());
        dto.setVersion(issue.getVersion());
        dto.setIssue(issue.getIssue());
        return dto;
    }

    private DuplicationInfoDTO convertToDuplicationInfoDTO(DuplicationInfo info) {
        if (info == null) {
            return null;
        }
        DuplicationInfoDTO dto = new DuplicationInfoDTO();
        dto.setDuplicatedCode(info.getDuplicatedCode());
        dto.setLocations(info.getLocations());
        return dto;
    }
}