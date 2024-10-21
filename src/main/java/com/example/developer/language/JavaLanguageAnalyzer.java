package com.example.developer.language;

import com.example.developer.model.ClassEntity;
import com.example.developer.model.Dependency;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JavaLanguageAnalyzer implements LanguageAnalyzer {

    private static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+(\\w+)");
    private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+([\\w.]+);");
    private static final Pattern IMPORT_PATTERN = Pattern.compile("import\\s+([\\w.]+);");

    @Override
    public Set<ClassEntity> analyzeClasses(Path modulePath) throws IOException {
        Set<ClassEntity> classes = new HashSet<>();
        Files.walk(modulePath)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        String content = Files.readString(path);
                        String packageName = extractPackageName(content);
                        String className = extractClassName(content);
                        if (className != null) {
                            ClassEntity classEntity = new ClassEntity();
                            classEntity.setName(className);
                            classEntity.setPackageName(packageName);
                            classEntity.setFullyQualifiedName(packageName + "." + className);
                            classEntity.setMethodCalls(extractMethodCalls(content));
                            classes.add(classEntity);
                        }
                    } catch (IOException e) {
                        // Log the error and continue with the next file
                        e.printStackTrace();
                    }
                });
        return classes;
    }

    @Override
    public List<Dependency> analyzeDependencies(Set<ClassEntity> classes) {
        List<Dependency> dependencies = new ArrayList<>();
        for (ClassEntity sourceClass : classes) {
            for (String methodCall : sourceClass.getMethodCalls()) {
                String[] parts = methodCall.split("\\.");
                if (parts.length > 1) {
                    String targetClassName = parts[0];
                    dependencies.add(new Dependency(sourceClass.getFullyQualifiedName(), targetClassName, "method"));
                }
            }
        }
        return dependencies;
    }

    private String extractPackageName(String content) {
        Matcher matcher = PACKAGE_PATTERN.matcher(content);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String extractClassName(String content) {
        Matcher matcher = CLASS_PATTERN.matcher(content);
        return matcher.find() ? matcher.group(1) : null;
    }

    private Set<String> extractMethodCalls(String content) {
        Set<String> methodCalls = new HashSet<>();
        Pattern pattern = Pattern.compile("(\\w+)\\s*\\.\\s*(\\w+)\\s*\\(");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            methodCalls.add(matcher.group(1) + "." + matcher.group(2));
        }
        return methodCalls;
    }
}