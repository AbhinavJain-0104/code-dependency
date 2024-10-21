package com.example.developer.controller;

import com.example.developer.dto.*;
import com.example.developer.model.Project;
import com.example.developer.model.ClassEntity;
import com.example.developer.model.ProjectStatus;
import com.example.developer.service.AnalysisService;
import com.example.developer.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
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

    @PostMapping("/analyze")
    public ResponseEntity<ProjectDTO> analyzeGitHubProject(@RequestParam("gitRepoUrl") String gitRepoUrl) {
        try {
            Project analyzedProject = projectService.analyzeGitHubProject(gitRepoUrl);

            // Extract repository name from the URL
            String[] urlParts = gitRepoUrl.split("/");
            String repoName = urlParts[urlParts.length - 1];
            if (repoName.endsWith(".git")) {
                repoName = repoName.substring(0, repoName.length() - 4);
            }
            analyzedProject.setName(repoName);

            ProjectDTO projectDTO = convertToDTO(analyzedProject);
            return ResponseEntity.ok(projectDTO);
        } catch (Exception e) {
            logger.error("Error analyzing GitHub project", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProjectDTO(null, ProjectStatus.ANALYSIS_FAILED, e.getMessage()));
        }
    }

    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setPath(project.getPath());
        dto.setStatus(project.getStatus());
        dto.setErrorMessage(project.getErrorMessage());

        dto.setModules(project.getModules().stream().map(module -> {
            ProjectModuleDTO moduleDTO = new ProjectModuleDTO();
            // Set a meaningful name for the module
            moduleDTO.setName(project.getName() + "-main");
            moduleDTO.setPath(module.getPath());
            moduleDTO.setStatus(module.getStatus());

            moduleDTO.setPackages(module.getClasses().stream()
                    .collect(Collectors.groupingBy(ClassEntity::getPackageName))
                    .entrySet().stream()
                    .map(entry -> {
                        PackageDTO packageDTO = new PackageDTO();
                        packageDTO.setName(entry.getKey());

                        packageDTO.setClasses(entry.getValue().stream().map(cls -> {
                            ClassDTO classDTO = new ClassDTO();
                            classDTO.setName(cls.getName());
                            classDTO.setPackageName(cls.getPackageName());
                            classDTO.setInnerClasses(cls.getInnerClasses());
                            classDTO.setAiDescription(cls.getAiDescription());
                            classDTO.setFields(cls.getFields());
                            classDTO.setInterfaces(cls.getInterfaces());
                            classDTO.setSuperclass(cls.getSuperclass());
                            classDTO.setModifiers(cls.getModifiers());
                            classDTO.setUsedClasses(cls.getUsedClasses().stream()
                                    .map(this::extractClassName)
                                    .collect(Collectors.toSet()));
                            return classDTO;
                        }).collect(Collectors.toList()));

                        return packageDTO;
                    }).collect(Collectors.toList()));

            return moduleDTO;
        }).collect(Collectors.toList()));

        // ... rest of the method remains the same

        dto.setDependencies(project.getDependencies().stream().map(dep -> {
            DependencyDTO dependencyDTO = new DependencyDTO();
            dependencyDTO.setSource(dep.getSource());
            dependencyDTO.setTarget(dep.getTarget());
            dependencyDTO.setType(dep.getDependencyType());
            dependencyDTO.setCircular(dep.isCircular());
            return dependencyDTO;
        }).collect(Collectors.toList()));

        dto.setExternalDependencies(project.getExternalDependencies().stream().map(extDep -> {
            ExternalDependencyDTO extDepDTO = new ExternalDependencyDTO();
            extDepDTO.setName(extDep.getName());
            extDepDTO.setVersion(extDep.getVersion());
            extDepDTO.setPackageManager(extDep.getPackageManager());
            extDepDTO.setType(extDep.getType());
            return extDepDTO;
        }).collect(Collectors.toList()));

        return dto;
    }

    private String extractClassName(String fullClassName) {
        String[] parts = fullClassName.split("\\.");
        return parts[parts.length - 1];
    }

}