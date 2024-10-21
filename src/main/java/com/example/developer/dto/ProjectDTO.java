package com.example.developer.dto;

import com.example.developer.model.ProjectStatus;
import java.util.List;
import java.util.Objects;

public class ProjectDTO {
    private String id;
    private ProjectStatus status;
    private String name;
    private String path;
    private List<ProjectModuleDTO> modules;
    private List<DependencyDTO> dependencies;
    private List<ExternalDependencyDTO> externalDependencies;
    private String errorMessage;

    public ProjectDTO(String id, ProjectStatus status, String name) {
        this.id = id;
        this.status = status;
        this.name = name;
    }

    public ProjectDTO() {

    }

    public List<ExternalDependencyDTO> getExternalDependencies() {
        return externalDependencies;
    }

    public void setExternalDependencies(List<ExternalDependencyDTO> externalDependencies) {
        this.externalDependencies = externalDependencies;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<ProjectModuleDTO> getModules() {
        return modules;
    }

    public void setModules(List<ProjectModuleDTO> modules) {
        this.modules = modules;
    }

    public List<DependencyDTO> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<DependencyDTO> dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectDTO that = (ProjectDTO) o;
        return Objects.equals(id, that.id) && status == that.status && Objects.equals(name, that.name) && Objects.equals(path, that.path) && Objects.equals(modules, that.modules) && Objects.equals(dependencies, that.dependencies) && Objects.equals(externalDependencies, that.externalDependencies) && Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, name, path, modules, dependencies, externalDependencies, errorMessage);
    }
}