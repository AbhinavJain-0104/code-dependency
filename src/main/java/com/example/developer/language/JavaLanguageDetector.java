package com.example.developer.language;

import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class JavaLanguageDetector implements LanguageDetector {

    private static final String JAVA_EXTENSION = ".java";

    @Override
    public boolean isLanguage(Path filePath) {
        return filePath.toString().toLowerCase().endsWith(JAVA_EXTENSION);
    }

    @Override
    public String getLanguageName() {
        return "Java";
    }
}