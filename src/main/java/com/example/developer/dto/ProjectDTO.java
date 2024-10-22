package com.example.developer.dto;

import com.example.developer.model.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProjectDTO  implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private ProjectStatus status;
    private String name;
    private String path;
    private List<ProjectModuleDTO> modules;
    private List<DependencyDTO> dependencies;
    private List<ExternalDependencyDTO> externalDependencies;
    private String errorMessage;
    private String primaryLanguage;
    private List<String> detectedFrameworks;
    private List<ExternalDependency> dependency;
    private List<APIUsagePatternDTO> apiUsagePatterns;
    private List<DuplicationInfoDTO> duplicationInfo;
    private Map<String, List<MetricTrendDTO>> trends;
    private List<StyleInconsistencyDTO> styleInconsistencies;
    private List<VersionIssueDTO> versionIssues;

    public List<DuplicationInfoDTO> getDuplicationInfo() {
        return duplicationInfo;
    }

    public void setDuplicationInfo(List<DuplicationInfoDTO> duplicationInfo) {
        this.duplicationInfo = duplicationInfo;
    }

    public Map<String, List<MetricTrendDTO>> getTrends() {
        return trends;
    }

    public void setTrends(Map<String, List<MetricTrendDTO>> trends) {
        this.trends = trends;
    }

    public List<StyleInconsistencyDTO> getStyleInconsistencies() {
        return styleInconsistencies;
    }

    public void setStyleInconsistencies(List<StyleInconsistencyDTO> styleInconsistencies) {
        this.styleInconsistencies = styleInconsistencies;
    }

    public List<VersionIssueDTO> getVersionIssues() {
        return versionIssues;
    }

    public void setVersionIssues(List<VersionIssueDTO> versionIssues) {
        this.versionIssues = versionIssues;
    }

    public List<APIUsagePatternDTO> getApiUsagePatterns() {
        return apiUsagePatterns;
    }

    public void setApiUsagePatterns(List<APIUsagePatternDTO> apiUsagePatterns) {
        this.apiUsagePatterns = apiUsagePatterns;
    }

    public List<ExternalDependency> getDependency() {
        return dependency;
    }

    public void setDependency(List<ExternalDependency> dependency) {
        this.dependency = dependency;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    public List<String> getDetectedFrameworks() {
        return detectedFrameworks;
    }

    public void setDetectedFrameworks(List<String> detectedFrameworks) {
        this.detectedFrameworks = detectedFrameworks;
    }

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