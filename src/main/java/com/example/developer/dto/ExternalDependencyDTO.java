package com.example.developer.dto;

import java.io.Serializable;

public class ExternalDependencyDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String version;
    private String packageManager;
    private String type;

    // Constructors
    public ExternalDependencyDTO() {}

    public ExternalDependencyDTO(String name, String version, String packageManager, String type) {
        this.name = name;
        this.version = version;
        this.packageManager = packageManager;
        this.type = type;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackageManager() {
        return packageManager;
    }

    public void setPackageManager(String packageManager) {
        this.packageManager = packageManager;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}