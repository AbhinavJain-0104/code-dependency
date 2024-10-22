package com.example.developer.service;

import com.example.developer.model.ClassEntity;
import com.example.developer.model.APIUsagePattern;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class APIUsageAnalysisService {

    public List<APIUsagePattern> analyzeAPIUsage(List<ClassEntity> classes, List<CompilationUnit> compilationUnits) {
        Map<String, Integer> apiUsageCounts = new HashMap<>();
        Map<String, Set<String>> apiUsageLocations = new HashMap<>();

        for (int i = 0; i < classes.size(); i++) {
            ClassEntity classEntity = classes.get(i);
            CompilationUnit cu = compilationUnits.get(i);

            cu.findAll(MethodCallExpr.class).forEach(methodCall -> {
                String apiCall = getFullyQualifiedMethodCall(methodCall);
                apiUsageCounts.merge(apiCall, 1, Integer::sum);
                apiUsageLocations.computeIfAbsent(apiCall, k -> new HashSet<>())
                        .add(classEntity.getName() + ":" + methodCall.getBegin().get().line);
            });
        }

        return apiUsageCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1) // Only consider APIs used more than once
                .map(entry -> new APIUsagePattern(
                        entry.getKey(),
                        entry.getValue(),
                        new ArrayList<>(apiUsageLocations.get(entry.getKey()))
                ))
                .sorted(Comparator.comparingInt(APIUsagePattern::getUsageCount).reversed())
                .collect(Collectors.toList());
    }

    private String getFullyQualifiedMethodCall(MethodCallExpr methodCall) {
        if (methodCall.getScope().isPresent() && methodCall.getScope().get() instanceof NameExpr) {
            return ((NameExpr) methodCall.getScope().get()).getNameAsString() + "." + methodCall.getNameAsString();
        }
        return methodCall.getNameAsString();
    }
}