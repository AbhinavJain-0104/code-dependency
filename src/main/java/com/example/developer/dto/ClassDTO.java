package com.example.developer.dto;

import java.util.List;
import java.util.Objects;

public class ClassDTO {
    private String name;
    private String packageName;
    private List<String> innerClasses;
    private String aiDescription;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getInnerClasses() {
        return innerClasses;
    }

    public void setInnerClasses(List<String> innerClasses) {
        this.innerClasses = innerClasses;
    }

    public String getAiDescription() {
        return aiDescription;
    }

    public void setAiDescription(String aiDescription) {
        this.aiDescription = aiDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassDTO classDTO = (ClassDTO) o;
        return Objects.equals(name, classDTO.name) && Objects.equals(packageName, classDTO.packageName) && Objects.equals(innerClasses, classDTO.innerClasses) && Objects.equals(aiDescription, classDTO.aiDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, packageName, innerClasses, aiDescription);
    }
}