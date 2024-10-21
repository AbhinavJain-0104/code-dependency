package com.example.developer.dependency;

import com.example.developer.model.ClassEntity;
import com.example.developer.model.Dependency;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class JavaScriptDependencyResolver implements DependencyResolver {

    @Override
    public List<Dependency> resolveDependencies(ClassEntity classEntity, Set<ClassEntity> allClasses) {
        List<Dependency> dependencies = new ArrayList<>();
        for (String importStatement : classEntity.getImports()) {
            for (ClassEntity targetClass : allClasses) {
                if (targetClass.getFilePath().endsWith(importStatement + ".js") ||
                        targetClass.getFilePath().endsWith(importStatement + ".jsx")) {
                    dependencies.add(new Dependency(classEntity.getFullyQualifiedName(), targetClass.getFullyQualifiedName(), "import"));
                }
            }
        }
        return dependencies;
    }

    @Override
    public String getLanguage() {
        return "JavaScript";
    }
}