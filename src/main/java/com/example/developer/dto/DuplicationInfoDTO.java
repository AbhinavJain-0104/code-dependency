package com.example.developer.dto;

import java.io.Serializable;
import java.util.List;

public class DuplicationInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String duplicatedCode;
    private List<String> locations;

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
