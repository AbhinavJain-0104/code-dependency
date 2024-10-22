package com.example.developer.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PackageDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private List<ClassDTO> classes;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ClassDTO> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassDTO> classes) {
        this.classes = classes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackageDTO that = (PackageDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(classes, that.classes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, classes);
    }
}