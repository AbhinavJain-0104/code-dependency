package com.example.developer.language;

import com.example.developer.model.ClassEntity;
import com.example.developer.model.Dependency;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JavaScriptLanguageSpecificAnalyzer implements LanguageSpecificAnalyzer {

    private static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+(\\w+)");
    private static final Pattern FUNCTION_PATTERN = Pattern.compile("(?:function\\s+(\\w+)|const\\s+(\\w+)\\s*=\\s*(?:function|\\([^)]*\\)\\s*=>))");
    private static final Pattern IMPORT_PATTERN = Pattern.compile("import\\s+(?:\\{[^}]*\\}|\\*\\s+as\\s+\\w+|\\w+)\\s+from\\s+['\"]([^'\"]+)['\"]");
    private static final Pattern REQUIRE_PATTERN = Pattern.compile("(?:const|let|var)\\s+(\\w+)\\s*=\\s*require\\(['\"]([^'\"]+)['\"]\\)");



    @Override
    public String getLanguage() {
        return "JavaScript";
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
                if (targetClass.getFilePath().endsWith(importStatement + ".js") || 
                    targetClass.getFilePath().endsWith(importStatement + ".jsx")) {
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
            methods.add(matcher.group(1) != null ? matcher.group(1) : matcher.group(2));
        }
        return methods;
    }

    private Set<String> extractImports(String content) {
        Set<String> imports = new HashSet<>();
        Matcher importMatcher = IMPORT_PATTERN.matcher(content);
        while (importMatcher.find()) {
            imports.add(importMatcher.group(1));
        }
        Matcher requireMatcher = REQUIRE_PATTERN.matcher(content);
        while (requireMatcher.find()) {
            imports.add(requireMatcher.group(2));
        }
        return imports;
    }
}