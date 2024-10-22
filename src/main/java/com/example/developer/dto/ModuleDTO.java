package com.example.developer.dto;

import java.util.List;
import java.util.Objects;

public class ModuleDTO {
    private String name;
    private String path;
    private List<PackageDTO> packages;
    private String language;
    private String framework;

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

    public List<PackageDTO> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageDTO> packages) {
        this.packages = packages;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFramework() {
        return framework;
    }

    public void setFramework(String framework) {
        this.framework = framework;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleDTO moduleDTO = (ModuleDTO) o;
        return Objects.equals(name, moduleDTO.name) && Objects.equals(path, moduleDTO.path) && Objects.equals(packages, moduleDTO.packages) && Objects.equals(language, moduleDTO.language) && Objects.equals(framework, moduleDTO.framework);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, packages, language, framework);
    }
}
