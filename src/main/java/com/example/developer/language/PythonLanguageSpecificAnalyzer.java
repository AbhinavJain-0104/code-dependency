package com.example.developer.language;

import com.example.developer.model.ClassEntity;
import com.example.developer.model.Dependency;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PythonLanguageSpecificAnalyzer implements LanguageSpecificAnalyzer {

    private static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+(\\w+)");
    private static final Pattern FUNCTION_PATTERN = Pattern.compile("def\\s+(\\w+)\\s*\\(");
    private static final Pattern IMPORT_PATTERN = Pattern.compile("(?:from\\s+(\\S+)\\s+import|import\\s+(\\S+))");

    @Override
    public String getLanguage() {
        return "Python";
    }

    @Override
    public ClassEntity extractClassInfo(String content, String filePath) {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setFilePath(filePath);
        classEntity.setLanguage(getLanguage());

        String className = extractClassName(content);
        classEntity.setName(className);

        Set<String> methods = extractMethods(content);
        classEntity.setMethods(methods);

        Set<String> imports = extractImports(content);
        classEntity.setImports(imports);

        return classEntity;
    }

    @Override
    public List<Dependency> extractDependencies(ClassEntity classEntity, Set<ClassEntity> allClasses) {
        List<Dependency> dependencies = new ArrayList<>();
        for (String importStatement : classEntity.getImports()) {
            for (ClassEntity targetClass : allClasses) {
                if (targetClass.getFilePath().endsWith(importStatement.replace(".", "/") + ".py")) {
                    dependencies.add(new Dependency(classEntity.getFullyQualifiedName(), targetClass.getFullyQualifiedName(), "import"));
                }
            }
        }
        return dependencies;
    }

    private String extractClassName(String content) {
        Matcher matcher = CLASS_PATTERN.matcher(content);
        return matcher.find() ? matcher.group(1) : "UnknownClass";
    }

    private Set<String> extractMethods(String content) {
        Set<String> methods = new HashSet<>();
        Matcher matcher = FUNCTION_PATTERN.matcher(content);
        while (matcher.find()) {
            methods.add(matcher.group(1));
        }
        return methods;
    }

    private Set<String> extractImports(String content) {
        Set<String> imports = new HashSet<>();
        Matcher matcher = IMPORT_PATTERN.matcher(content);
        while (matcher.find()) {
            imports.add(matcher.group(1) != null ? matcher.group(1) : matcher.group(2));
        }
        return imports;
    }
}