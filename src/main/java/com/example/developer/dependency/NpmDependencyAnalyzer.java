package com.example.developer.dependency;

import com.example.developer.model.ExternalDependency;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class NpmDependencyAnalyzer {

    public List<ExternalDependency> analyzeDependencies(String projectRoot) {
        List<ExternalDependency> dependencies = new ArrayList<>();
        String packageJsonPath = Paths.get(projectRoot, "package.json").toString();

        try {
            String content = new String(Files.readAllBytes(Paths.get(packageJsonPath)));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(content);

            addDependencies(dependencies, rootNode.get("dependencies"), "runtime");
            addDependencies(dependencies, rootNode.get("devDependencies"), "dev");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dependencies;
    }

    private void addDependencies(List<ExternalDependency> dependencies, JsonNode dependencyNode, String type) {
        if (dependencyNode != null && dependencyNode.isObject()) {
            dependencyNode.fields().forEachRemaining(entry -> {
                ExternalDependency dep = new ExternalDependency(entry.getKey(), entry.getValue().asText(), "npm", type);
                dependencies.add(dep);
            });
        }
    }
}