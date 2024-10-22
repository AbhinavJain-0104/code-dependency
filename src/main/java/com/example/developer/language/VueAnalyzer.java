package com.example.developer.language;

import com.example.developer.model.ClassEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class VueAnalyzer extends JavaScriptLanguageSpecificAnalyzer {

    private static final Pattern COMPONENT_PATTERN = Pattern.compile("export\\s+default\\s+\\{\\s*name:\\s*['\"]([^'\"]+)['\"]");
    private static final Pattern PROP_PATTERN = Pattern.compile("props:\\s*\\{([^}]+)\\}");
    private static final Pattern METHOD_PATTERN = Pattern.compile("methods:\\s*\\{([^}]+)\\}");

    @Override
    public String getLanguage() {
        return "Vue.js";
    }

    @Override
    public ClassEntity extractClassInfo(String content, String filePath) {
        ClassEntity componentEntity = super.extractClassInfo(content, filePath);
        componentEntity.setFramework("Vue.js");

        String componentName = extractComponentName(content);
        componentEntity.setName(componentName);

        Set<String> props = extractProps(content);
        componentEntity.setProperties(props);

        Set<String> methods = extractMethods(content);
        componentEntity.getMethods().addAll(methods);

        return componentEntity;
    }

    private String extractComponentName(String content) {
        Matcher matcher = COMPONENT_PATTERN.matcher(content);
        return matcher.find() ? matcher.group(1) : "UnknownComponent";
    }

    private Set<String> extractProps(String content) {
        Set<String> props = new HashSet<>();
        Matcher matcher = PROP_PATTERN.matcher(content);
        if (matcher.find()) {
            String propsContent = matcher.group(1);
            Pattern propNamePattern = Pattern.compile("(\\w+):");
            Matcher propNameMatcher = propNamePattern.matcher(propsContent);
            while (propNameMatcher.find()) {
                props.add(propNameMatcher.group(1));
            }
        }
        return props;
    }

    private Set<String> extractMethods(String content) {
        Set<String> methods = new HashSet<>();
        Matcher matcher = METHOD_PATTERN.matcher(content);
        if (matcher.find()) {
            String methodsContent = matcher.group(1);
            Pattern methodNamePattern = Pattern.compile("(\\w+)\\s*\\(");
            Matcher methodNameMatcher = methodNamePattern.matcher(methodsContent);
            while (methodNameMatcher.find()) {
                methods.add(methodNameMatcher.group(1));
            }
        }
        return methods;
    }
}