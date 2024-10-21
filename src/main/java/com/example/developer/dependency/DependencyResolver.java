package com.example.developer.dependency;

import com.example.developer.model.Dependency;
import com.example.developer.model.ClassEntity;

import java.util.List;
import java.util.Set;

public interface DependencyResolver {
    List<Dependency> resolveDependencies(ClassEntity classEntity, Set<ClassEntity> allClasses);
    String getLanguage();
}