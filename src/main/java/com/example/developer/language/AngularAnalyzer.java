package com.example.developer.language;

import com.example.developer.model.ClassEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AngularAnalyzer extends JavaScriptLanguageSpecificAnalyzer {
    private static final Pattern COMPONENT_PATTERN = Pattern.compile("@Component\\s*\\(\\{[^}]*\\}\\)\\s*export\\s+class\\s+(\\w+)");
    private static final Pattern INPUT_PATTERN = Pattern.compile("@Input\\(\\)\\s+(\\w+)");
    private static final Pattern OUTPUT_PATTERN = Pattern.compile("@Output\\(\\)\\s+(\\w+)");

    @Override
    public String getLanguage() {
        return "Angular";
    }

    @Override
    public ClassEntity extractClassInfo(String content, String filePath) {
        ClassEntity componentEntity = super.extractClassInfo(content, filePath);
        componentEntity.setFramework("Angular");

        String componentName = extractComponentName(content);
        componentEntity.setName(componentName);

        Set<String> inputs = extractInputs(content);
        Set<String> outputs = extractOutputs(content);
        componentEntity.setProperties(inputs);
        componentEntity.getProperties().addAll(outputs);

        return componentEntity;
    }

    private String extractComponentName(String content) {
        Matcher matcher = COMPONENT_PATTERN.matcher(content);
        return matcher.find() ? matcher.group(1) : "UnknownComponent";
    }

    private Set<String> extractInputs(String content) {
        Set<String> inputs = new HashSet<>();
        Matcher matcher = INPUT_PATTERN.matcher(content);
        while (matcher.find()) {
            inputs.add(matcher.group(1));
        }
        return inputs;
    }

    private Set<String> extractOutputs(String content) {
        Set<String> outputs = new HashSet<>();
        Matcher matcher = OUTPUT_PATTERN.matcher(content);
        while (matcher.find()) {
            outputs.add(matcher.group(1));
        }
        return outputs;
    }
}