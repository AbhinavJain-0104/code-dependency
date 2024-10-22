package com.example.developer.model;

import java.io.Serializable;
import java.util.List;

public class DuplicationInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String duplicatedCode;
    private List<String> locations;

    public DuplicationInfo(String duplicatedCode, List<String> locations) {
        this.duplicatedCode = duplicatedCode;
        this.locations = locations;
    }

    // Getters and setters
    public String getDuplicatedCode() {
        return duplicatedCode;
    }

    public void setDuplicatedCode(String duplicatedCode) {
        this.duplicatedCode = duplicatedCode;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}