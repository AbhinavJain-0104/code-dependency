package com.example.developer.model;

import java.util.*;

public class Project {

    private String id;
    private String name;
    private String path;
    private List<ProjectModule> modules = new ArrayList<>();
    private List<Dependency> dependencies = new ArrayList<>();
    private ProjectStatus status;
    private String errorMessage;
    private List<ExternalDependency> externalDependencies = new ArrayList<>();


    // Getters and setters

    public List<ExternalDependency> getExternalDependencies() {
        return externalDependencies;
    }

    public void setExternalDependencies(List<ExternalDependency> externalDependencies) {
        this.externalDependencies = externalDependencies;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<ProjectModule> getModules() {
        return modules;
    }

    public void setModules(List<ProjectModule> modules) {
        this.modules = modules;
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id) && Objects.equals(name, project.name) && Objects.equals(path, project.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, path);
    }
}