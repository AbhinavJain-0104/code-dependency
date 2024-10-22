package com.example.developer.dto;

import java.io.Serializable;

public class StyleInconsistencyDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String location;
    private String description;

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