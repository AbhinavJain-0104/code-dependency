package com.example.developer.model;

import java.io.Serializable;

public class VersionIssue implements Serializable {
    private static final long serialVersionUID = 1L;
    private String dependencyName;
    private String version;
    private String issue;

    public VersionIssue(String dependencyName, String version, String issue) {
        this.dependencyName = dependencyName;
        this.version = version;
        this.issue = issue;
    }

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