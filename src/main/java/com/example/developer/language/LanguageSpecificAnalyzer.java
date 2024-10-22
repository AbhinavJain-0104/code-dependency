package com.example.developer.language;

import com.example.developer.model.ClassEntity;
import com.example.developer.model.Dependency;

import java.util.List;
import java.util.Set;

public interface LanguageSpecificAnalyzer {
    String getLanguage();
    boolean canHandle(String filePath);
    ClassEntity extractClassInfo(String content, String filePath);
    List<Dependency> extractDependencies(ClassEntity classEntity, Set<ClassEntity> allClasses);
    String detectFramework(String projectRoot);
}