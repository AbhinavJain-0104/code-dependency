package com.example.developer.packagemanager;

import com.example.developer.model.ExternalDependency;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PipAnalyzer implements PackageManagerAnalyzer {

    private static final Pattern DEPENDENCY_PATTERN = Pattern.compile("([\\w-]+)([=<>]+)(.+)");

    @Override
    public List<ExternalDependency> analyzePackageFile(String filePath) {
        List<ExternalDependency> dependencies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = DEPENDENCY_PATTERN.matcher(line.trim());
                if (matcher.matches()) {
                    String name = matcher.group(1);
                    String version = matcher.group(3);
                    dependencies.add(new ExternalDependency(name, version, getPackageManager(), "runtime"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dependencies;
    }

    @Override
    public String getPackageManager() {
        return "pip";
    }
}