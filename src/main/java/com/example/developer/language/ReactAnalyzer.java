package com.example.developer.language;

import com.example.developer.model.ClassEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ReactAnalyzer extends JavaScriptLanguageSpecificAnalyzer {

    private static final Pattern COMPONENT_PATTERN = Pattern.compile("(?:class|function)\\s+(\\w+)");
    private static final Pattern HOOK_PATTERN = Pattern.compile("use[A-Z]\\w+");
    private static final Pattern PROP_PATTERN = Pattern.compile("\\{([^}]+)\\}\\s*=\\s*props");

    @Override
    public String getLanguage() {
        return "React";
    }

    @Override
    public ClassEntity extractClassInfo(String content, String filePath) {
        ClassEntity componentEntity = super.extractClassInfo(content, filePath);
        componentEntity.setFramework("React");

        String componentName = extractComponentName(content);
        componentEntity.setName(componentName);

        Set<String> hooks = extractHooks(content);
        componentEntity.getMethods().addAll(hooks);

        Set<String> props = extractProps(content);
        componentEntity.setProperties(props);

        return componentEntity;
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
}