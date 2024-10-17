package com.example.developer.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.*;

@Table(name = "project")
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String path;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProjectModule> modules = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Dependency> dependencies = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private String errorMessage;


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

    public List<ProjectModule> getModules() {
        return modules;
    }

    // Optionally, you can create a method to return a simplified view of the project
    public List<Map<String, Object>> getModulesAsMap() {
        List<Map<String, Object>> moduleList = new ArrayList<>();
        for (ProjectModule module : modules) {
            Map<String, Object> moduleMap = new HashMap<>();
            moduleMap.put("name", module.getName());
            moduleMap.put("path", module.getPath());
            moduleMap.put("project", this); // Reference to the project
            moduleMap.put("classes", module.getClasses()); // Directly include classes
            moduleList.add(moduleMap);
        }
        return moduleList;
    }

    // ... existing code ...
public List<Map<String, Object>> getModulesAndClassesAsMap() {
    List<Map<String, Object>> moduleList = new ArrayList<>();
    for (ProjectModule module : modules) {
        Map<String, Object> moduleMap = new HashMap<>();
        moduleMap.put("name", module.getName());
        moduleMap.put("path", module.getPath());
        
        // Add packages and classes to the module map
        Map<String, Object> packageMap = new HashMap<>();
        for (ClassEntity classEntity : module.getClasses()) {
            String packageName = classEntity.getPackageName();
            String[] packageParts = packageName.split("\\."); // Split package name into parts
            
            // Build nested structure for packages
            Map<String, Object> currentLevel = packageMap;
            for (String part : packageParts) {
                currentLevel = (Map<String, Object>) currentLevel.computeIfAbsent(part, k -> new HashMap<>());
            }
            // Add class to the final package level
            List<String> classes = (List<String>) currentLevel.computeIfAbsent("classes", k -> new ArrayList<>());
            classes.add(classEntity.getName());
        }
        
        moduleMap.put("packages", packageMap);
        moduleList.add(moduleMap);
    }
    return moduleList;
}
// ... existing code ...


    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setModules(List<ProjectModule> modules) {
        this.modules = modules;
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Dependency> dependencies) {
        this.dependencies = dependencies;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id) && Objects.equals(name, project.name) && Objects.equals(path, project.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, path);
    }
}