package com.example.developer.language;

import com.example.developer.model.ClassEntity;
import com.example.developer.model.Dependency;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public interface LanguageSpecificAnalyzer {
    ClassEntity extractClassInfo(String content, String filePath);
    List<Dependency> extractDependencies(ClassEntity classEntity, Set<ClassEntity> allClasses);
    String getLanguage();
}