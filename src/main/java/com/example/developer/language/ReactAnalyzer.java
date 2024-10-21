package com.example.developer.language;

import com.example.developer.model.ClassEntity;
import com.example.developer.model.Dependency;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ReactAnalyzer implements FrameworkSpecificAnalyzer {

    private static final Pattern COMPONENT_PATTERN = Pattern.compile("(?:class|function)\\s+(\\w+)");
    private static final Pattern HOOK_PATTERN = Pattern.compile("use[A-Z]\\w+");
    private static final Pattern PROP_PATTERN = Pattern.compile("\\{([^}]+)\\}\\s*=\\s*props");
    private static final Pattern IMPORT_PATTERN = Pattern.compile("import\\s+(?:\\{[^}]*\\}|\\*\\s+as\\s+\\w+|\\w+)\\s+from\\s+['\"]([^'\"]+)['\"]");

    @Override
    public String getLanguage() {
        return "React";
    }

    @Override
    public String getFramework() {
        return "React";
    }

    @Override
    public ClassEntity extractClassInfo(String content, String filePath) {
        return extractComponentInfo(content, filePath);
    }

    @Override
    public ClassEntity extractComponentInfo(String content, String filePath) {
        ClassEntity componentEntity = new ClassEntity();
        componentEntity.setFilePath(filePath);
        componentEntity.setLanguage(getLanguage());
        componentEntity.setFramework(getFramework());

        String componentName = extractComponentName(content);
        componentEntity.setName(componentName);

        Set<String> hooks = extractHooks(content);
        componentEntity.setMethods(hooks);

        Set<String> props = extractProps(content);
        componentEntity.setProperties(props);

        Set<String> imports = extractImports(content);
        componentEntity.setImports(imports);

        return componentEntity;
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

    private String extractComponentName(String content) {
        Matcher matcher = COMPONENT_PATTERN.matcher(content);
        return matcher.find() ? matcher.group(1) : "UnknownComponent";
    }

    private Set<String> extractHooks(String content) {
        Set<String> hooks = new HashSet<>();
        Matcher matcher = HOOK_PATTERN.matcher(content);
        while (matcher.find()) {
            hooks.add(matcher.group());
        }
        return hooks;
    }

    private Set<String> extractProps(String content) {
        Set<String> props = new HashSet<>();
        Matcher matcher = PROP_PATTERN.matcher(content);
        while (matcher.find()) {
            String[] propNames = matcher.group(1).split(",");
            for (String prop : propNames) {
                props.add(prop.trim());
            }
        }
        return props;
    }

    private Set<String> extractImports(String content) {
        Set<String> imports = new HashSet<>();
        Matcher matcher = IMPORT_PATTERN.matcher(content);
        while (matcher.find()) {
            imports.add(matcher.group(1));
        }
        return imports;
    }
}