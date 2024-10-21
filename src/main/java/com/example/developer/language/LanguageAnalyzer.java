package com.example.developer.language;

import com.example.developer.model.ClassEntity;
import com.example.developer.model.Dependency;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface LanguageAnalyzer {
    Set<ClassEntity> analyzeClasses(Path modulePath) throws IOException;
    List<Dependency> analyzeDependencies(Set<ClassEntity> classes);
}