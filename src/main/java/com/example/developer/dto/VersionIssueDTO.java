package com.example.developer.dto;

import java.io.Serializable;

public class VersionIssueDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String dependencyName;
    private String version;
    private String issue;

    // Getters and setters


    public String getDependencyName() {
        return dependencyName;
    }

    public void setDependencyName(String dependencyName) {
        this.dependencyName = dependencyName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }
}