package com.example.developer.util;

import com.example.developer.model.ClassEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericCodeParser {

    private static final Pattern CLASS_PATTERN = Pattern.compile("(?:class|interface|enum)\\s+(\\w+)");
    private static final Pattern FUNCTION_PATTERN = Pattern.compile("(?:function|def|public|private|protected)\\s+(\\w+)\\s*\\([^)]*\\)\\s*\\{?");
    private static final Pattern IMPORT_PATTERN = Pattern.compile("(?:import|from|require)\\s+(?:['\"]([^'\"]+)['\"]|([\\w.]+))");
    private static final Pattern NESTED_PATTERN = Pattern.compile("\\{([^{}]*)\\}");

    public static ClassEntity parseCode(String content, String filePath, String language) {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setFilePath(filePath);
        classEntity.setLanguage(language);

        String className = extractClassName(content);
        classEntity.setName(className);

        Set<String> methods = extractMethods(content);
        classEntity.setMethods(methods);

        Set<String> imports = extractImports(content);
        classEntity.setImports(imports);

        return classEntity;
    }

    private static String extractClassName(String content) {
        Matcher matcher = CLASS_PATTERN.matcher(content);
        return matcher.find() ? matcher.group(1) : "UnknownClass";
    }

    private static Set<String> extractMethods(String content) {
        Set<String> methods = new HashSet<>();
        Matcher matcher = FUNCTION_PATTERN.matcher(content);
        while (matcher.find()) {
            methods.add(matcher.group(1));
            String nestedContent = extractNestedContent(content.substring(matcher.end()));
            if (nestedContent != null) {
                methods.addAll(extractMethods(nestedContent));
            }
        }
        return methods;
    }

    private static Set<String> extractImports(String content) {
        Set<String> imports = new HashSet<>();
        Matcher matcher = IMPORT_PATTERN.matcher(content);
        while (matcher.find()) {
            imports.add(matcher.group(1) != null ? matcher.group(1) : matcher.group(2));
        }
        return imports;
    }

    private static String extractNestedContent(String content) {
        Matcher matcher = NESTED_PATTERN.matcher(content);
        return matcher.find() ? matcher.group(1) : null;
    }
}