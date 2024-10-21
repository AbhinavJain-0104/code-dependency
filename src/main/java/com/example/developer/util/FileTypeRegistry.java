package com.example.developer.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FileTypeRegistry {
    private final Map<String, String> extensionToLanguage = new HashMap<>();
    private final Map<String, String> extensionToFramework = new HashMap<>();

    public FileTypeRegistry() {
        // Initialize with known file types
        extensionToLanguage.put("java", "java");
        extensionToLanguage.put("py", "Python");
        extensionToLanguage.put("js", "JavaScript");
        extensionToLanguage.put("jsx", "JavaScript");
        extensionToLanguage.put("ts", "TypeScript");
        extensionToLanguage.put("tsx", "TypeScript");

        // Initialize with known frameworks
        extensionToFramework.put("jsx", "React");
        extensionToFramework.put("tsx", "React");
    }

    public String getLanguageForExtension(String extension) {
        return extensionToLanguage.get(extension.toLowerCase());
    }

    public String getFrameworkForExtension(String extension) {
        return extensionToFramework.get(extension.toLowerCase());
    }

    public void registerFileType(String extension, String language) {
        extensionToLanguage.put(extension.toLowerCase(), language);
    }

    public void registerFramework(String extension, String framework) {
        extensionToFramework.put(extension.toLowerCase(), framework);
    }
}