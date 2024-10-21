package com.example.developer.packagemanager;

import com.example.developer.model.ExternalDependency;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class NPMAnalyzer implements PackageManagerAnalyzer {

    @Override
    public List<ExternalDependency> analyzePackageFile(String filePath) {
        List<ExternalDependency> dependencies = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode rootNode = mapper.readTree(new File(filePath));
            JsonNode dependenciesNode = rootNode.path("dependencies");
            JsonNode devDependenciesNode = rootNode.path("devDependencies");

            addDependencies(dependencies, dependenciesNode, "runtime");
            addDependencies(dependencies, devDependenciesNode, "dev");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dependencies;
    }

    private void addDependencies(List<ExternalDependency> dependencies, JsonNode dependenciesNode, String type) {
        dependenciesNode.fields().forEachRemaining(entry -> {
            String name = entry.getKey();
            String version = entry.getValue().asText();
            dependencies.add(new ExternalDependency(name, version, getPackageManager(), type));
        });
    }

    @Override
    public String getPackageManager() {
        return "npm";
    }
}