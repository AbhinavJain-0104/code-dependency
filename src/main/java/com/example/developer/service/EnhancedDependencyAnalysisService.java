package com.example.developer.service;

import com.example.developer.model.ClassEntity;
import com.example.developer.model.Dependency;
import com.example.developer.model.Project;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EnhancedDependencyAnalysisService {

    public List<Dependency> analyzeDependencies(Project project, List<ClassEntity> classes) {
        List<Dependency> dependencies = new ArrayList<>();
        Map<String, ClassEntity> classMap = classes.stream()
                .collect(Collectors.toMap(ClassEntity::getFullyQualifiedName, c -> c));

        for (ClassEntity classEntity : classes) {
            Set<String> usedClasses = classEntity.getUsedClasses();
            for (String usedClass : usedClasses) {
                if (classMap.containsKey(usedClass)) {
                    Dependency dependency = new Dependency();
                    dependency.setSource(classEntity.getFullyQualifiedName());
                    dependency.setTarget(usedClass);
                    dependency.setDependencyType(determineDependencyType(classEntity, classMap.get(usedClass)));
                    dependency.setProject(project);
                    dependencies.add(dependency);
                }
            }
        }

        detectCircularDependencies(dependencies);

        return dependencies;
    }

    private String determineDependencyType(ClassEntity source, ClassEntity target) {
        if (source.getInterfaces().contains(target.getName())) {
            return "Implements";
        } else if (source.getSuperclass() != null && source.getSuperclass().equals(target.getName())) {
            return "Extends";
        } else if (source.getFields().stream().anyMatch(field -> field.contains(target.getName()))) {
            return "Composition";
        } else {
            return "Uses";
        }
    }

    private void detectCircularDependencies(List<Dependency> dependencies) {
        Map<String, Set<String>> dependencyGraph = new HashMap<>();
        for (Dependency dep : dependencies) {
            dependencyGraph.computeIfAbsent(dep.getSource(), k -> new HashSet<>()).add(dep.getTarget());
        }

        for (Dependency dep : dependencies) {
            if (isCircular(dep.getSource(), dep.getTarget(), new HashSet<>(), dependencyGraph)) {
                dep.setCircular(true);
            }
        }
    }

    private boolean isCircular(String start, String current, Set<String> visited, Map<String, Set<String>> graph) {
        if (start.equals(current)) return true;
        if (visited.contains(current)) return false;

        visited.add(current);
        Set<String> dependencies = graph.get(current);
        if (dependencies != null) {
            for (String next : dependencies) {
                if (isCircular(start, next, visited, graph)) {
                    return true;
                }
            }
        }
        visited.remove(current);
        return false;
    }
}