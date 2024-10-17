package com.example.developer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Table(name = "class_entity")
@Entity
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String packageName;

    @ManyToOne
    @JoinColumn(name = "module_id")
    @JsonBackReference
    private ProjectModule module;

    @ElementCollection
    private List<String> innerClasses = new ArrayList<>();

    @ElementCollection
    private Set<String> imports = new HashSet<>();

    private Set<String> methodCalls = new HashSet<>();

    // Add getters and setters
    private String fullyQualifiedName;
    private String aiDescription;

    // Add getters and setters for all fields, including:

    public String getAiDescription() {
        return aiDescription;
    }

    public void setAiDescription(String aiDescription) {
        this.aiDescription = aiDescription;
    }


    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public void setFullyQualifiedName(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }

    public Set<String> getMethodCalls() {
        return methodCalls;
    }

    public void setMethodCalls(Set<String> methodCalls) {
        this.methodCalls = methodCalls;
    }

    // Existing getters and setters...

    public Set<String> getImports() {
        return imports;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
    }


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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public ProjectModule getModule() {
        return module;
    }

    public void setModule(ProjectModule module) {
        this.module = module;
    }

    public List<String> getInnerClasses() {
        return innerClasses;
    }

    public void setInnerClasses(List<String> innerClasses) {
        this.innerClasses = innerClasses;
    }

    // Update equals and hashCode methods to include imports
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassEntity that = (ClassEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(packageName, that.packageName) &&
                Objects.equals(fullyQualifiedName, that.fullyQualifiedName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, packageName, fullyQualifiedName);
    }
}