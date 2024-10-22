package com.example.developer.model;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Project implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String path;
    private List<ProjectModule> modules = new ArrayList<>();
    private List<Dependency> dependencies = new ArrayList<>();
    private ProjectStatus status;
    private String errorMessage;
    private List<ExternalDependency> externalDependencies = new ArrayList<>();
   private  List<ClassEntity> classes=new ArrayList<>();
    private List<String> detectedFrameworks = new ArrayList<>();
    private String primaryLanguage;
    private List<APIUsagePattern> apiUsagePatterns;
    private List<DuplicationInfo> duplicationInfo;
    private Map<String, List<MetricTrend>> trends;
    private List<StyleInconsistency> styleInconsistencies;
    private List<VersionIssue> versionIssues;


    public List<DuplicationInfo> getDuplicationInfo() {
        return duplicationInfo;
    }

    public void setDuplicationInfo(List<DuplicationInfo> duplicationInfo) {
        this.duplicationInfo = duplicationInfo;
    }

    public List<StyleInconsistency> getStyleInconsistencies() {
        return styleInconsistencies;
    }

    public void setStyleInconsistencies(List<StyleInconsistency> styleInconsistencies) {
        this.styleInconsistencies = styleInconsistencies;
    }

    public Map<String, List<MetricTrend>> getTrends() {
        return trends;
    }

    public void setTrends(Map<String, List<MetricTrend>> trends) {
        this.trends = trends;
    }

    public List<VersionIssue> getVersionIssues() {
        return versionIssues;
    }

    public void setVersionIssues(List<VersionIssue> versionIssues) {
        this.versionIssues = versionIssues;
    }

    public List<APIUsagePattern> getApiUsagePatterns() {
        return apiUsagePatterns;
    }

    public void setApiUsagePatterns(List<APIUsagePattern> apiUsagePatterns) {
        this.apiUsagePatterns = apiUsagePatterns;
    }

    public List<String> getDetectedFrameworks() {
        return detectedFrameworks;
    }

    public void setDetectedFrameworks(List<String> detectedFrameworks) {
        this.detectedFrameworks = detectedFrameworks;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    public List<ClassEntity> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassEntity> classes) {
        this.classes = classes;
    }

    public List<ClassEntity> getAllClasses() {
        return this.getModules().stream()
                .flatMap(module -> module.getClasses().stream())
                .collect(Collectors.toList());
    }

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