package com.example.developer.controller;

import com.example.developer.dto.*;
import com.example.developer.model.Project;
import com.example.developer.model.ProjectModule;
import com.example.developer.model.ClassEntity;
import com.example.developer.model.Dependency;
import com.example.developer.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final AnalysisService analysisService;

    @Autowired
    public ProjectController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProject(@RequestParam String gitRepoUrl) {
        try {
            CompletableFuture<Project> futureProject = analysisService.processProjectUploadAsync(gitRepoUrl);
            Project project = futureProject.join(); // Wait for the async process to complete

            // Convert the project to a DTO
            ProjectDTO projectDTO = convertToDTO(project);

            return ResponseEntity.ok(projectDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error starting project analysis: " + e.getMessage()));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id) {
        try {
            Project project = analysisService.getProjectWithDetails(id);
            ProjectDTO projectDTO = convertToDTO(project);
            return ResponseEntity.ok(projectDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setPath(project.getPath());
        dto.setStatus(project.getStatus());

        List<ProjectModuleDTO> moduleDTOs = project.getModules().stream().map(module -> {
            ProjectModuleDTO moduleDTO = new ProjectModuleDTO();
            moduleDTO.setId(module.getId());
            moduleDTO.setName(module.getName());
            moduleDTO.setPath(module.getPath());

            List<PackageDTO> packageDTOs = module.getPackages().stream()
                    .map(pkg -> {
                        PackageDTO packageDTO = new PackageDTO();
                        packageDTO.setName(pkg.getName());

                        List<ClassDTO> classDTOs = pkg.getClasses().stream().map(cls -> {
                            ClassDTO classDTO = new ClassDTO();
                            classDTO.setName(cls.getName());
                            classDTO.setPackageName(cls.getPackageName());
                            classDTO.setInnerClasses(cls.getInnerClasses());
                            classDTO.setAiDescription(cls.getAiDescription());
                            return classDTO;
                        }).collect(Collectors.toList());

                        packageDTO.setClasses(classDTOs);
                        return packageDTO;
                    }).collect(Collectors.toList());

            moduleDTO.setPackages(packageDTOs);
            return moduleDTO;
        }).collect(Collectors.toList());

        dto.setModules(moduleDTOs);

        List<DependencyDTO> dependencyDTOs = project.getDependencies().stream().map(dep -> {
            DependencyDTO dependencyDTO = new DependencyDTO();
            dependencyDTO.setSource(dep.getSource());
            dependencyDTO.setTarget(dep.getTarget());
            dependencyDTO.setType(dep.getDependencyType());
            return dependencyDTO;
        }).collect(Collectors.toList());

        dto.setDependencies(dependencyDTOs);

        return dto;
    }
}