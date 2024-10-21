package com.example.developer.dto;

import com.example.developer.model.ClassEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ClassDTO {
    private String name;
    private String packageName;
    private List<ClassEntity> innerClasses;
    private String aiDescription;
    private Set<String> fields;
    private Set<String> interfaces;
    private String superclass;
    private Set<String> modifiers;
    private Set<String> usedClasses = new HashSet<>();

    public Set<String> getUsedClasses() {
        return usedClasses;
    }

    public void setUsedClasses(Set<String> usedClasses) {
        this.usedClasses = usedClasses;
    }

    public Set<String> getFields() {
        return fields;
    }

    public void setFields(Set<String> fields) {
        this.fields = fields;
    }

    public Set<String> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(Set<String> interfaces) {
        this.interfaces = interfaces;
    }

    public String getSuperclass() {
        return superclass;
    }

    public void setSuperclass(String superclass) {
        this.superclass = superclass;
    }

    public Set<String> getModifiers() {
        return modifiers;
    }

    public void setModifiers(Set<String> modifiers) {
        this.modifiers = modifiers;
    }

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

    public List<ClassEntity> getInnerClasses() {
        return innerClasses;
    }

    public void setInnerClasses(List<ClassEntity> innerClasses) {
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
        return Objects.equals(name, classDTO.name) && Objects.equals(packageName, classDTO.packageName) && Objects.equals(innerClasses, classDTO.innerClasses) && Objects.equals(aiDescription, classDTO.aiDescription) && Objects.equals(fields, classDTO.fields) && Objects.equals(interfaces, classDTO.interfaces) && Objects.equals(superclass, classDTO.superclass) && Objects.equals(modifiers, classDTO.modifiers) && Objects.equals(usedClasses, classDTO.usedClasses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, packageName, innerClasses, aiDescription, fields, interfaces, superclass, modifiers, usedClasses);
    }
}