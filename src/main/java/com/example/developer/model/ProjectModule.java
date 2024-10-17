package com.example.developer.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;


import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "project_module")
public class ProjectModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String path;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private Project project;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<ClassEntity> classes = new HashSet<>();


    @OneToMany(cascade = CascadeType.ALL)
    private List<PackageEntity> packages;

    // Getters and Setters
    // ...

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<ClassEntity> getClasses() {
        return classes;
    }

    public void setClasses(Set<ClassEntity> classes) {
        this.classes = classes;
    }

    public List<PackageEntity> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageEntity> packages) {
        this.packages = packages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectModule that = (ProjectModule) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(path, that.path) && Objects.equals(project, that.project) && Objects.equals(classes, that.classes) && Objects.equals(packages, that.packages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, path, project, classes, packages);
    }
}