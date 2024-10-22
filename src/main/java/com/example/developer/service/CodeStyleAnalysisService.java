package com.example.developer.service;

import com.example.developer.model.ClassEntity;
import com.example.developer.model.StyleInconsistency;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class CodeStyleAnalysisService {

    public List<StyleInconsistency> analyzeCodeStyle(List<ClassEntity> classes) {
        List<StyleInconsistency> inconsistencies = new ArrayList<>();
        Pattern camelCasePattern = Pattern.compile("^[a-z]+([A-Z][a-z0-9]+)*$");

        for (ClassEntity classEntity : classes) {
            // Check class name
            if (!Character.isUpperCase(classEntity.getName().charAt(0))) {
                inconsistencies.add(new StyleInconsistency(classEntity.getName(), "Class name should start with an uppercase letter"));
            }

            // Check method names
            for (String method : classEntity.getMethods()) {
                if (!camelCasePattern.matcher(method).matches()) {
                    inconsistencies.add(new StyleInconsistency(classEntity.getName(), "Method '" + method + "' should be in camelCase"));
                }
            }

            // Add more style checks as needed
        }

        return inconsistencies;
    }
}