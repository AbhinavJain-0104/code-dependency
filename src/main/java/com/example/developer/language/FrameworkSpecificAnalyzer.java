package com.example.developer.language;

import com.example.developer.model.ClassEntity;

public interface FrameworkSpecificAnalyzer extends LanguageSpecificAnalyzer {
    String getFramework();
    ClassEntity extractComponentInfo(String content, String filePath);
}