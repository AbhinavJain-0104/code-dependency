package com.example.developer.dto;

import com.example.developer.model.ProjectStatus;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

public class ProjectDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private String name;
    private String path;
    private List<ProjectModuleDTO> modules;
    private List<DependencyDTO> dependencies;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        return Objects.equals(name, that.name) && Objects.equals(path, that.path) && Objects.equals(modules, that.modules) && Objects.equals(dependencies, that.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, modules, dependencies);
    }
}