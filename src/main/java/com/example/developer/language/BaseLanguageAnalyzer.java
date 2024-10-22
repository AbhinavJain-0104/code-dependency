package com.example.developer.language;

import com.example.developer.model.ClassEntity;
import com.example.developer.model.Dependency;

import java.util.List;
import java.util.Set;

public abstract class BaseLanguageAnalyzer implements LanguageSpecificAnalyzer {
    @Override
    public boolean canHandle(String filePath) {
        String[] supportedExtensions = getSupportedFileExtensions();
        for (String ext : supportedExtensions) {
            if (filePath.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    protected abstract String[] getSupportedFileExtensions();

    @Override
    public String detectFramework(String projectRoot) {
        // Default implementation returns null, subclasses can override
        return null;
    }
}