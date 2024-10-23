package com.example.developer.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;



    @RedisHash("dependency")
    public class Dependency implements Serializable{
        @Id
        private Long id;

        @Indexed
        private String groupId;

        @Indexed
        private String artifactId;

    private static final long serialVersionUID = 1L;



    private String source;
    private String target;
    private String dependencyType;
    private String type;
    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private Project project;
    private boolean isCircular;
    private ClassEntity classes;



    public ClassEntity getClasses() {
        return classes;
    }

    public void setClasses(ClassEntity classes) {
        this.classes = classes;
    }

    public Dependency() {

    }

    public Dependency(String fullyQualifiedName, String fullyQualifiedName1, String anImport) {
        this.source = fullyQualifiedName;
        this.target = fullyQualifiedName1;
        this.type = anImport;
    }

    public boolean isCircular() {
        return isCircular;
    }

    public void setCircular(boolean circular) {
        isCircular = circular;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDependencyType() {
        return dependencyType;
    }

    public void setDependencyType(String dependencyType) {
        this.dependencyType = dependencyType;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Dependency(String source, String target, String dependencyType, boolean isCircular) {
        this.source = source;
        this.target = target;
        this.dependencyType = dependencyType;
        this.isCircular = isCircular;
    }


}
