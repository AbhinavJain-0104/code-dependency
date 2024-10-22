package com.example.developer.service;

import com.example.developer.dto.PerformanceInsight;
import com.example.developer.model.ClassEntity;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.expr.MethodCallExpr;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PerformanceAnalysisService {

    public void analyzePerformance(ClassEntity classEntity, CompilationUnit cu) {
        List<PerformanceInsight> insights = new ArrayList<>();

        // Analyze loops
        analyzeLoops(classEntity, cu, insights);

        // Analyze method complexity
        analyzeMethodComplexity(classEntity, cu, insights);

        // Analyze method calls
        analyzeMethodCalls(classEntity, cu, insights);

        // Set the insights on the ClassEntity
        classEntity.setPerformanceInsights(insights);
    }

    private void analyzeLoops(ClassEntity classEntity, CompilationUnit cu, List<PerformanceInsight> insights) {
        cu.findAll(ForStmt.class).forEach(forStmt -> {
            if (forStmt.getBody().findAll(MethodCallExpr.class).size() > 5) {
                insights.add(new PerformanceInsight(
                        classEntity.getName(),
                        "High number of method calls inside a for loop",
                        forStmt.getBegin().get().line,
                        "Consider optimizing or moving method calls outside the loop if possible."
                ));
            }
        });

        cu.findAll(WhileStmt.class).forEach(whileStmt -> {
            if (whileStmt.getBody().findAll(MethodCallExpr.class).size() > 5) {
                insights.add(new PerformanceInsight(
                        classEntity.getName(),
                        "High number of method calls inside a while loop",
                        whileStmt.getBegin().get().line,
                        "Consider optimizing or moving method calls outside the loop if possible."
                ));
            }
        });
    }

    private void analyzeMethodComplexity(ClassEntity classEntity, CompilationUnit cu, List<PerformanceInsight> insights) {
        cu.findAll(MethodDeclaration.class).forEach(method -> {
            if (method.getBody().isPresent() && method.getBody().get().getStatements().size() > 50) {
                insights.add(new PerformanceInsight(
                        classEntity.getName(),
                        "High method complexity",
                        method.getBegin().get().line,
                        "Consider breaking down method '" + method.getNameAsString() + "' into smaller, more manageable pieces."
                ));
            }
        });
    }

    private void analyzeMethodCalls(ClassEntity classEntity, CompilationUnit cu, List<PerformanceInsight> insights) {
        cu.findAll(MethodDeclaration.class).forEach(method -> {
            int methodCallCount = method.findAll(MethodCallExpr.class).size();
            if (methodCallCount > 20) {
                insights.add(new PerformanceInsight(
                        classEntity.getName(),
                        "High number of method calls",
                        method.getBegin().get().line,
                        "Method '" + method.getNameAsString() + "' has " + methodCallCount +
                                " method calls. Consider refactoring for better performance."
                ));
            }
        });
    }
}