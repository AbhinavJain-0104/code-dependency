package com.example.developer.model;


import java.io.Serializable;

public class ExternalDependency implements Serializable {
    private static final long serialVersionUID = 1L;


    private Long id;



    private String name;



    private String version;
    private String packageManager;
    private String type;

    public ExternalDependency() {}

    public ExternalDependency(String name, String version, String packageManager, String type) {
        this.name = name;
        this.version = version;
        this.packageManager = packageManager;
        this.type = type;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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