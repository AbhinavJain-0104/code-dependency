package com.example.developer.model;

import java.io.Serializable;

public class StyleInconsistency implements Serializable {
    private static final long serialVersionUID = 1L;
    private String location;
    private String description;

    public StyleInconsistency(String location, String description) {
        this.location = location;
        this.description = description;
    }

    // Getters and setters
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}