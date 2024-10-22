package com.example.developer.dto;

import com.example.developer.model.ProjectStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ProjectModuleDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String path;
    private List<PackageDTO> packages;
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<PackageDTO> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageDTO> packages) {
        this.packages = packages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectModuleDTO that = (ProjectModuleDTO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(path, that.path) &&
                Objects.equals(packages, that.packages) &&
                status == that.status &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, packages, status, id);
    }
}