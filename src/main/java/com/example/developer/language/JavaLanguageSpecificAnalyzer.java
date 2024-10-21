package com.example.developer.language;

import com.example.developer.model.ClassEntity;
import com.example.developer.model.Dependency;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JavaLanguageSpecificAnalyzer implements LanguageSpecificAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(JavaLanguageSpecificAnalyzer.class);

    private final JavaParser parser;
    private final JavaSymbolSolver symbolSolver;

    public JavaLanguageSpecificAnalyzer() {
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        typeSolver.add(new JavaParserTypeSolver(new File("src/main/java")));
        this.symbolSolver = new JavaSymbolSolver(typeSolver);
        this.parser = new JavaParser();
        this.parser.getParserConfiguration().setSymbolResolver(symbolSolver);
    }

    @Override
    public String getLanguage() {
        return "java";
    }

    @Override
    public ClassEntity extractClassInfo(String content, String filePath) {
        logger.info("Analyzing Java file: {}", filePath);
        try {
            ParseResult<CompilationUnit> parseResult = parser.parse(content);
            if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
                CompilationUnit cu = parseResult.getResult().get();
                List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);

                if (!classes.isEmpty()) {
                    ClassOrInterfaceDeclaration mainClass = classes.get(0);
                    ClassEntity classEntity = createClassEntity(mainClass, cu.getPackageDeclaration().map(pd -> pd.getNameAsString()).orElse(""), filePath);
                    correlateImportsWithUsedClasses(classEntity);
                    logger.info("Successfully parsed: {}", classEntity.getFullyQualifiedName());
                    return classEntity;
                } else {
                    logger.warn("No classes found in file: {}", filePath);
                }
            } else {
                logger.error("Failed to parse file: {}", filePath);
                parseResult.getProblems().forEach(problem -> logger.error("Parsing problem: {}", problem.getMessage()));
            }
        } catch (Exception e) {
            logger.error("Error parsing file: {}", filePath, e);
        }
        return null;
    }

    private ClassEntity createClassEntity(ClassOrInterfaceDeclaration classDeclaration, String packageName, String filePath) {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setName(classDeclaration.getNameAsString());
        classEntity.setPackageName(packageName);
        classEntity.setFullyQualifiedName(packageName + "." + classEntity.getName());
        classEntity.setFilePath(filePath);
        classEntity.setImports(extractImports(classDeclaration));
        classEntity.setMethods(extractMethods(classDeclaration));
        classEntity.setMethodCalls(extractMethodCalls(classDeclaration));
        classEntity.setFields(extractFields(classDeclaration));
        classEntity.setInnerClasses(extractInnerClasses(classDeclaration, classEntity));
        classEntity.setInterfaces(extractInterfaces(classDeclaration));
        classEntity.setSuperclass(extractSuperclass(classDeclaration));
        classEntity.setModifiers(extractModifiers(classDeclaration));
        classEntity.setUsedClasses(extractUsedClasses(classDeclaration));
        return classEntity;
    }

    private Set<String> extractImports(ClassOrInterfaceDeclaration classDeclaration) {
        return classDeclaration.findCompilationUnit()
                .map(cu -> cu.getImports().stream()
                        .map(importDecl -> importDecl.getNameAsString())
                        .collect(Collectors.toSet()))
                .orElse(new HashSet<>());
    }

    private Set<String> extractMethods(ClassOrInterfaceDeclaration classDeclaration) {
        return classDeclaration.getMethods().stream()
                .map(MethodDeclaration::getNameAsString)
                .collect(Collectors.toSet());
    }

    private Set<String> extractMethodCalls(ClassOrInterfaceDeclaration classDeclaration) {
        Set<String> methodCalls = new HashSet<>();
        classDeclaration.accept(new MethodCallVisitor(), methodCalls);
        return methodCalls;
    }

    private Set<String> extractFields(ClassOrInterfaceDeclaration classDeclaration) {
        return classDeclaration.getFields().stream()
                .flatMap(field -> field.getVariables().stream())
                .map(var -> var.getTypeAsString() + " " + var.getNameAsString())
                .collect(Collectors.toSet());
    }

    private List<ClassEntity> extractInnerClasses(ClassOrInterfaceDeclaration classDeclaration, ClassEntity outerClass) {
        List<ClassEntity> innerClasses = new ArrayList<>();
        for (ClassOrInterfaceDeclaration innerClass : classDeclaration.findAll(ClassOrInterfaceDeclaration.class)) {
            if (innerClass != classDeclaration) {
                ClassEntity innerClassEntity = createInnerClassEntity(innerClass, outerClass);
                innerClasses.add(innerClassEntity);
            }
        }
        return innerClasses;
    }

    private ClassEntity createInnerClassEntity(ClassOrInterfaceDeclaration innerClassDeclaration, ClassEntity outerClass) {
        ClassEntity innerClass = new ClassEntity();
        innerClass.setName(innerClassDeclaration.getNameAsString());
        innerClass.setPackageName(outerClass.getPackageName());
        innerClass.setFullyQualifiedName(outerClass.getFullyQualifiedName() + "$" + innerClass.getName());
        innerClass.setFilePath(outerClass.getFilePath());
        innerClass.setImports(outerClass.getImports());
        innerClass.setMethods(extractMethods(innerClassDeclaration));
        innerClass.setMethodCalls(extractMethodCalls(innerClassDeclaration));
        innerClass.setFields(extractFields(innerClassDeclaration));
        innerClass.setInnerClasses(extractInnerClasses(innerClassDeclaration, innerClass));
        innerClass.setInterfaces(extractInterfaces(innerClassDeclaration));
        innerClass.setSuperclass(extractSuperclass(innerClassDeclaration));
        innerClass.setModifiers(extractModifiers(innerClassDeclaration));
        innerClass.setUsedClasses(extractUsedClasses(innerClassDeclaration));
        return innerClass;
    }

    private Set<String> extractInterfaces(ClassOrInterfaceDeclaration classDeclaration) {
        return classDeclaration.getImplementedTypes().stream()
                .map(t -> {
                    try {
                        return t.resolve().describe();
                    } catch (Exception e) {
                        logger.warn("Failed to resolve interface for {}", classDeclaration.getNameAsString(), e);
                        return t.getNameAsString();
                    }
                })
                .collect(Collectors.toSet());
    }

    private String extractSuperclass(ClassOrInterfaceDeclaration classDeclaration) {
        return classDeclaration.getExtendedTypes().stream()
                .findFirst()
                .map(t -> {
                    try {
                        return t.resolve().describe();
                    } catch (Exception e) {
                        logger.warn("Failed to resolve superclass for {}", classDeclaration.getNameAsString(), e);
                        return t.getNameAsString();
                    }
                })
                .orElse(null);
    }

    private Set<String> extractModifiers(ClassOrInterfaceDeclaration classDeclaration) {
        return classDeclaration.getModifiers().stream()
                .map(mod -> mod.getKeyword().asString())
                .collect(Collectors.toSet());
    }

    private Set<String> extractUsedClasses(ClassOrInterfaceDeclaration classDeclaration) {
        Set<String> usedClasses = new HashSet<>();
        classDeclaration.getFields().forEach(field -> {
            field.getVariables().forEach(var -> {
                String typeName = var.getType().asString();
                if (!isPrimitiveOrCommonType(typeName)) {
                    // Extract just the class name, not the full path
                    String[] parts = typeName.split("\\.");
                    usedClasses.add(parts[parts.length - 1]);
                }
            });
        });
        return usedClasses;
    }

    private boolean isPrimitiveOrCommonType(String typeName) {
        Set<String> primitiveAndCommonTypes = new HashSet<>(Arrays.asList(
                "byte", "short", "int", "long", "float", "double", "boolean", "char",
                "String", "Integer", "Long", "Float", "Double", "Boolean", "Character",
                "Void", "Number"
        ));
        return primitiveAndCommonTypes.contains(typeName) || typeName.startsWith("java.lang.") || typeName.contains("[]");
    }

    private void correlateImportsWithUsedClasses(ClassEntity classEntity) {
        Set<String> fullQualifiedUsedClasses = new HashSet<>();
        for (String usedClass : classEntity.getUsedClasses()) {
            String fullQualifiedName = classEntity.getImports().stream()
                    .filter(imp -> imp.endsWith("." + usedClass))
                    .findFirst()
                    .orElse(classEntity.getPackageName() + "." + usedClass);
            fullQualifiedUsedClasses.add(fullQualifiedName);
        }
        classEntity.setUsedClasses(fullQualifiedUsedClasses);
    }

    @Override
    public List<Dependency> extractDependencies(ClassEntity classEntity, Set<ClassEntity> allClasses) {
        List<Dependency> dependencies = new ArrayList<>();
        Set<String> imports = classEntity.getImports();
        Set<String> methodCalls = classEntity.getMethodCalls();
        Set<String> usedClasses = classEntity.getUsedClasses();

        for (ClassEntity targetClass : allClasses) {
            if (!targetClass.equals(classEntity)) {
                if (imports.contains(targetClass.getFullyQualifiedName()) ||
                        methodCalls.stream().anyMatch(call -> targetClass.getMethods().contains(call)) ||
                        usedClasses.contains(targetClass.getFullyQualifiedName())) {
                    Dependency dependency = new Dependency();
                    dependency.setSource(classEntity.getFullyQualifiedName());
                    dependency.setTarget(targetClass.getFullyQualifiedName());
                    dependencies.add(dependency);
                }
            }
        }

        return dependencies;
    }

    private static class MethodCallVisitor extends VoidVisitorAdapter<Set<String>> {
        @Override
        public void visit(MethodCallExpr n, Set<String> collector) {
            super.visit(n, collector);
            collector.add(n.getNameAsString());
        }
    }
}