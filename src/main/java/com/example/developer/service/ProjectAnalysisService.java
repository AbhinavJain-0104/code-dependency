package com.example.developer.service;

import com.example.developer.analyzer.JavaScriptProjectAnalyzer;
import com.example.developer.dto.ClassDTO;
import com.example.developer.dto.PackageDTO;
import com.example.developer.dto.ProjectDTO;
import com.example.developer.dto.ProjectModuleDTO;
import com.example.developer.model.ClassEntity;
import com.example.developer.model.Project;
import com.example.developer.model.ProjectModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectAnalysisService {

    @Autowired
    private JavaScriptProjectAnalyzer javaScriptProjectAnalyzer;

    public ProjectDTO analyzeProject(String projectPath) {
        try {
            Project project = javaScriptProjectAnalyzer.analyzeProject(projectPath);
            return convertToProjectDTO(project);
        } catch (IOException e) {
            e.printStackTrace();
            return new ProjectDTO();
        }
    }

    private ProjectDTO convertToProjectDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setPath(project.getPath());
        projectDTO.setName(project.getName());
        projectDTO.setDetectedFrameworks(project.getDetectedFrameworks());
        projectDTO.setDependency(project.getExternalDependencies());
        projectDTO.setPrimaryLanguage(project.getPrimaryLanguage());
        projectDTO.setModules(convertToModules(project.getModules(), project.getName()));
        return projectDTO;
    }

    private List<ProjectModuleDTO> convertToModules(List<ProjectModule> modules, String projectName) {
        return modules.stream()
                .map(module -> convertToModuleDTO(module, projectName))
                .collect(Collectors.toList());
    }

    private ProjectModuleDTO convertToModuleDTO(ProjectModule module, String projectName) {
        ProjectModuleDTO moduleDTO = new ProjectModuleDTO();
        moduleDTO.setName(projectName);
        moduleDTO.setPath(module.getPath());
        moduleDTO.setPackages(convertToPackages(new ArrayList<>(module.getClasses())));
        return moduleDTO;
    }

    private List<PackageDTO> convertToPackages(List<ClassEntity> classes) {
        Map<String, List<ClassEntity>> packageMap = classes.stream()
                .collect(Collectors.groupingBy(ClassEntity::getPackageName));

        return packageMap.entrySet().stream()
                .map(entry -> {
                    PackageDTO packageDTO = new PackageDTO();
                    packageDTO.setName(entry.getKey());
                    packageDTO.setClasses(convertToClassDTOs(entry.getValue()));
                    return packageDTO;
                })
                .collect(Collectors.toList());
    }

    private List<ClassDTO> convertToClassDTOs(List<ClassEntity> classes) {
        return classes.stream()
                .map(this::convertToClassDTO)
                .collect(Collectors.toList());
    }

    private ClassDTO convertToClassDTO(ClassEntity classEntity) {
        ClassDTO classDTO = new ClassDTO();
        classDTO.setName(classEntity.getName());
        classDTO.setPackageName(classEntity.getPackageName());
        classDTO.setInnerClasses(classEntity.getInnerClasses());
        classDTO.setInnerFunctions(classEntity.getInnerFunctions());
        classDTO.setFilePath(classEntity.getFilePath());
        return classDTO;
    }
}