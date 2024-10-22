package com.example.developer.framework;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class FrameworkDetector {

    public List<String> detectFrameworks(String projectPath) {
        List<String> detectedFrameworks = new ArrayList<>();

        // Check for React
        if (Files.exists(Paths.get(projectPath, "src", "App.js")) || Files.exists(Paths.get(projectPath, "src", "App.jsx"))) {
            detectedFrameworks.add("React");
        }

        // Check for Angular
        if (Files.exists(Paths.get(projectPath, "angular.json"))) {
            detectedFrameworks.add("Angular");
        }

        // Check for Vue.js
        if (Files.exists(Paths.get(projectPath, "src", "main.js")) && containsVueImport(Paths.get(projectPath, "src", "main.js"))) {
            detectedFrameworks.add("Vue.js");
        }

        // Add more framework detection logic as needed

        return detectedFrameworks;
    }

    private boolean containsVueImport(Path filePath) {
        try {
            List<String> lines = Files.readAllLines(filePath);
            return lines.stream().anyMatch(line -> line.contains("import Vue from 'vue'"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String detectFramework(String filePath) {
        List<String> detectedFrameworks = detectFrameworks(Paths.get(filePath).getParent().toString());
        return detectedFrameworks.isEmpty() ? null : detectedFrameworks.get(0);
    }
}