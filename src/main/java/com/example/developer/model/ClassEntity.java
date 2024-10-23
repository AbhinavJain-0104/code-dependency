package com.example.developer.model;

import com.example.developer.dto.PerformanceInsight;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.*;


@RedisHash("class")
    public class ClassEntity implements Serializable {
        @Id
        private Long id;

        @Indexed
        private String name;

        @Indexed
        private String packageName;

        @Indexed
        private String moduleName;



        private static final long serialVersionUID = 1L;




    @JsonBackReference
    private ProjectModule module;




    private Set<String> imports = new HashSet<>();

    private Set<String> methodCalls = new HashSet<>();
    private String language;

    // Add getters and setters
    private String fullyQualifiedName;
    private String aiDescription;

    private String filePath;
    private Set<String> methods = new HashSet<>();
    private String framework;
    private Set<String> properties = new HashSet<>();
    private boolean isApiRoute;
    private boolean hasGetServerSideProps;
    private boolean hasGetStaticProps;
    private List<ClassEntity> innerClasses;
    private Set<String> fields = new HashSet<>();
    private Set<String> interfaces = new HashSet<>();
    private String superclass;
    private Set<String> modifiers = new HashSet<>();
    private Set<String> usedClasses = new HashSet<>();
    private Set<ClassEntity>innerFunctions= new HashSet<>();
    private ClassEntity classes;
    private Map<String, Double> metrics;
    private List<PerformanceInsight> performanceInsights;

    private String content;

    // ... existing methods ...

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public List<PerformanceInsight> getPerformanceInsights() {
        return performanceInsights;
    }

    public void setPerformanceInsights(List<PerformanceInsight> performanceInsights) {
        this.performanceInsights = performanceInsights != null ? performanceInsights : new ArrayList<>();
    }

    public void addPerformanceInsight(PerformanceInsight insight) {
        if (insight != null) {
            this.performanceInsights.add(insight);
        }
    }
    public Map<String, Double> getMetrics() {
        return metrics;
    }

    public void setMetrics(Map<String, Double> metrics) {
        this.metrics = metrics;
    }

    public ClassEntity getClasses() {
        return classes;
    }

    public void setClasses(ClassEntity classes) {
        this.classes = classes;
    }

    public Set<ClassEntity> getInnerFunctions() {
        return innerFunctions;
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

    public void setIsApiRoute(boolean apiRoute) {
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


    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public Set<String> getMethods() {
        return methods;
    }
    public void setMethods(Set<String> methods) {
        this.methods = methods;
    }



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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public void setApiRoute(boolean apiRoute) {
        isApiRoute = apiRoute;
    }

    public List<ClassEntity> getInnerClasses() {
        return innerClasses;
    }

    public void setInnerClasses(List<ClassEntity> innerClasses) {
        this.innerClasses = innerClasses;
    }

    // Update equals and hashCode methods to include imports


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassEntity that = (ClassEntity) o;
        return isApiRoute == that.isApiRoute && hasGetServerSideProps == that.hasGetServerSideProps && hasGetStaticProps == that.hasGetStaticProps && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(packageName, that.packageName) && Objects.equals(module, that.module) && Objects.equals(imports, that.imports) && Objects.equals(methodCalls, that.methodCalls) && Objects.equals(language, that.language) && Objects.equals(fullyQualifiedName, that.fullyQualifiedName) && Objects.equals(aiDescription, that.aiDescription) && Objects.equals(filePath, that.filePath) && Objects.equals(methods, that.methods) && Objects.equals(framework, that.framework) && Objects.equals(properties, that.properties) && Objects.equals(innerClasses, that.innerClasses) && Objects.equals(fields, that.fields) && Objects.equals(interfaces, that.interfaces) && Objects.equals(superclass, that.superclass) && Objects.equals(modifiers, that.modifiers) && Objects.equals(usedClasses, that.usedClasses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, packageName, module, imports, methodCalls, language, fullyQualifiedName, aiDescription, filePath, methods, framework, properties, isApiRoute, hasGetServerSideProps, hasGetStaticProps, innerClasses, fields, interfaces, superclass, modifiers, usedClasses);
    }
}