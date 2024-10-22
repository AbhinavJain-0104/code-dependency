package com.example.developer.dto;

import com.example.developer.model.ClassEntity;

import java.io.Serializable;
import java.util.*;

public class ClassDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String filePath;
    private String packageName;
    private List<ClassEntity> innerClasses;
    private String aiDescription;
    private Set<String> fields;
    private Set<String> interfaces;
    private String superclass;
    private Set<String> modifiers;
    private Set<String> usedClasses = new HashSet<>();
    private Set<ClassEntity>innerFunctions= new HashSet<>();
    private String language;
    private Set<String> methods = new HashSet<>();
    private Set<String> properties = new HashSet<>();
    private boolean isApiRoute;
    private boolean hasGetServerSideProps;
    private boolean hasGetStaticProps;
    private String framework;
    private Map<String, Double> metrics;
    private List<PerformanceInsightDTO> performanceInsights;

    public List<PerformanceInsightDTO> getPerformanceInsights() {
        return performanceInsights;
    }

    public void setPerformanceInsights(List<PerformanceInsightDTO> performanceInsights) {
        this.performanceInsights = performanceInsights;
    }


    // ... existing methods ...

    public Map<String, Double> getMetrics() {
        return metrics;
    }

    public void setMetrics(Map<String, Double> metrics) {
        this.metrics = metrics;
    }

    public String getFramework() {
        return framework;
    }

    public void setFramework(String framework) {
        this.framework = framework;
    }

    public Set<String> getProperties() {
        return properties;
    }

    public void setProperties(Set<String> properties) {
        this.properties = properties;
    }

    public boolean isApiRoute() {
        return isApiRoute;
    }

    public void setApiRoute(boolean apiRoute) {
        isApiRoute = apiRoute;
    }

    public boolean isHasGetServerSideProps() {
        return hasGetServerSideProps;
    }

    public void setHasGetServerSideProps(boolean hasGetServerSideProps) {
        this.hasGetServerSideProps = hasGetServerSideProps;
    }

    public boolean isHasGetStaticProps() {
        return hasGetStaticProps;
    }

    public void setHasGetStaticProps(boolean hasGetStaticProps) {
        this.hasGetStaticProps = hasGetStaticProps;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Set<String> getMethods() {
        return methods;
    }

    public void setMethods(Set<String> methods) {
        this.methods = methods;
    }

    public Set<ClassEntity> getInnerFunctions() {
        return innerFunctions;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setInnerFunctions(Set<ClassEntity> innerFunctions) {
        this.innerFunctions = innerFunctions;
    }

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