package com.example.developer.language;

import com.example.developer.model.ClassEntity;
import com.example.developer.model.Dependency;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class JavaScriptLanguageSpecificAnalyzer implements LanguageSpecificAnalyzer {

    private static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+(\\w+)(?:\\s+extends\\s+(\\w+))?");
    private static final Pattern FUNCTION_PATTERN = Pattern.compile("(?:function\\s+(\\w+)|const\\s+(\\w+)\\s*=\\s*(?:function|\\([^)]*\\)\\s*=>))");
    private static final Pattern IMPORT_PATTERN = Pattern.compile("import\\s+(?:\\{([^}]+)\\}|\\*\\s+as\\s+(\\w+)|(\\w+))\\s+from\\s+['\"]([^'\"]+)['\"]");
    private static final Pattern REQUIRE_PATTERN = Pattern.compile("(?:const|let|var)\\s+(\\w+)\\s*=\\s*require\\(['\"]([^'\"]+)['\"]\\)");
    private static final Pattern EXPORT_PATTERN = Pattern.compile("export\\s+(?:default\\s+)?(class|function|const|let|var)\\s+(\\w+)");

    @Override
    public String getLanguage() {
        return "JavaScript";
    }
    @Override
    public String detectFramework(String filePath) {
        Path path = Paths.get(filePath);
        String fileName = path.getFileName().toString().toLowerCase();
        String content = readFileContent(filePath);

        // React detection
        if (fileName.endsWith(".jsx") || fileName.endsWith(".tsx") ||
                content.contains("import React") || content.contains("from 'react'") ||
                content.contains("React.Component") || content.contains("useState(") ||
                content.contains("useEffect(")) {
            return "React";
        }

        // Angular detection
        if (fileName.endsWith(".ts") && content.contains("@Component") ||
                content.contains("@NgModule") || content.contains("@Injectable") ||
                content.contains("import { Component }") || content.contains("from '@angular/core'")) {
            return "Angular";
        }

        // Vue.js detection
        if (fileName.endsWith(".vue") || content.contains("new Vue(") ||
                content.contains("Vue.component(") || content.contains("@Component") && content.contains("vue-property-decorator")) {
            return "Vue.js";
        }

        // Next.js detection
        if (content.contains("import { NextPage }") || content.contains("from 'next'") ||
                content.contains("getStaticProps") || content.contains("getServerSideProps")) {
            return "Next.js";
        }

        // Express.js detection
        if (content.contains("express()") || content.contains("require('express')") ||
                content.contains("import express from 'express'")) {
            return "Express.js";
        }

        // Svelte detection
        if (fileName.endsWith(".svelte") || content.contains("<script>") && content.contains("<style>") && content.contains("<svelte:")) {
            return "Svelte";
        }

        // Ember.js detection
        if (content.contains("import EmberRouter") || content.contains("Ember.Component.extend") ||
                content.contains("import { inject as service }")) {
            return "Ember.js";
        }

        // Meteor detection
        if (content.contains("import { Meteor }") || content.contains("Meteor.isClient") ||
                content.contains("Meteor.isServer") || content.contains("Meteor.startup(")) {
            return "Meteor";
        }

        return "Unknown";
    }
    @Override
    public boolean canHandle(String filePath) {
        String lowercasePath = filePath.toLowerCase();
        return lowercasePath.endsWith(".js") || lowercasePath.endsWith(".jsx") ||
                lowercasePath.endsWith(".ts") || lowercasePath.endsWith(".tsx");
    }

    @Override
    public ClassEntity extractClassInfo(String content, String filePath) {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setFilePath(filePath);
        classEntity.setPackageName(extractPackageName(filePath));

        Set<String> classes = new HashSet<>(extractPatternMatches(content, CLASS_PATTERN));
        Set<String> functions = new HashSet<>(extractPatternMatches(content, FUNCTION_PATTERN));
        Set<String> exports = new HashSet<>(extractPatternMatches(content, EXPORT_PATTERN));

        classEntity.setName(classes.isEmpty() ? Paths.get(filePath).getFileName().toString() : classes.iterator().next());
        classes.remove(classEntity.getName());

        // Convert Set<String> to List<ClassEntity> for inner classes
        List<ClassEntity> innerClassEntities = classes.stream()
                .map(className -> {
                    ClassEntity innerClass = new ClassEntity();
                    innerClass.setName(className);
                    return innerClass;
                })
                .collect(Collectors.toList());
        classEntity.setInnerClasses(innerClassEntities);

        // Convert Set<String> to Set<ClassEntity> for inner functions
        Set<ClassEntity> innerFunctionEntities = functions.stream()
                .map(functionName -> {
                    ClassEntity functionEntity = new ClassEntity();
                    functionEntity.setName(functionName);
                    return functionEntity;
                })
                .collect(Collectors.toSet());
        classEntity.setInnerFunctions(innerFunctionEntities);

        // Store exports in a suitable field, e.g., methods
        classEntity.setMethods(exports);

        return classEntity;
    }

    @Override
    public List<Dependency> extractDependencies(ClassEntity classEntity, Set<ClassEntity> allClasses) {
        List<Dependency> dependencies = new ArrayList<>();
        String content = readFileContent(classEntity.getFilePath());

        extractImportDependencies(content, classEntity, allClasses, dependencies);
        extractRequireDependencies(content, classEntity, allClasses, dependencies);

        return dependencies;
    }

    private void extractImportDependencies(String content, ClassEntity classEntity, Set<ClassEntity> allClasses, List<Dependency> dependencies) {
        Matcher matcher = IMPORT_PATTERN.matcher(content);
        while (matcher.find()) {
            String importedItems = matcher.group(1) != null ? matcher.group(1) :
                    matcher.group(2) != null ? matcher.group(2) : matcher.group(3);
            String source = matcher.group(4);

            if (importedItems != null) {
                for (String item : importedItems.split(",")) {
                    item = item.trim();
                    addDependency(classEntity, item, source, allClasses, dependencies);
                }
            } else {
                addDependency(classEntity, source, source, allClasses, dependencies);
            }
        }
    }

    private void extractRequireDependencies(String content, ClassEntity classEntity, Set<ClassEntity> allClasses, List<Dependency> dependencies) {
        Matcher matcher = REQUIRE_PATTERN.matcher(content);
        while (matcher.find()) {
            String importedItem = matcher.group(1);
            String source = matcher.group(2);
            addDependency(classEntity, importedItem, source, allClasses, dependencies);
        }
    }

    private void addDependency(ClassEntity classEntity, String importedItem, String source, Set<ClassEntity> allClasses, List<Dependency> dependencies) {
        ClassEntity targetClass = findClassByName(importedItem, allClasses);
        if (targetClass != null) {
            Dependency dependency = new Dependency();
            dependency.setSource(classEntity.getFilePath());
            dependency.setTarget(targetClass.getFilePath());
            dependency.setType("import");
            dependencies.add(dependency);
        }
    }

    private ClassEntity findClassByName(String name, Set<ClassEntity> allClasses) {
        return allClasses.stream()
                .filter(c -> c.getName().equals(name) || c.getInnerClasses().contains(name) || c.getInnerFunctions().contains(name))
                .findFirst()
                .orElse(null);
    }

    private String extractPackageName(String filePath) {
        Path path = Paths.get(filePath);
        return path.getParent().toString();
    }

    private List<String> extractPatternMatches(String content, Pattern pattern) {
        List<String> matches = new ArrayList<>();
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            matches.add(matcher.group(1));
        }
        return matches;
    }

    private String readFileContent(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}