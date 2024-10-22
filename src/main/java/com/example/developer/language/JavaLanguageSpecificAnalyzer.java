package com.example.developer.language;

import com.example.developer.model.ClassEntity;
import com.example.developer.model.Dependency;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class JavaLanguageSpecificAnalyzer implements LanguageSpecificAnalyzer {

    @Override
    public String getLanguage() {
        return "java";
    }

    @Override
    public boolean canHandle(String language) {
        return "java".equalsIgnoreCase(language);
    }

    @Override
    public String detectFramework(String content) {
        // This is a simple implementation. You may want to enhance this method
        // to detect more frameworks and make it more robust.
        if (content.contains("org.springframework")) {
            return "Spring";
        } else if (content.contains("javax.servlet")) {
            return "JavaEE";
        } else if (content.contains("play.")) {
            return "Play";
        } else if (content.contains("spark.")) {
            return "Spark";
        } else {
            return "Unknown";
        }
    }

    @Override
    public ClassEntity extractClassInfo(String content, String filePath) {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setFilePath(filePath);
        classEntity.setLanguage(getLanguage());
        classEntity.setFramework(detectFramework(content));

        JavaParser javaParser = new JavaParser();
        ParseResult<CompilationUnit> parseResult = javaParser.parse(content);
        if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
            CompilationUnit cu = parseResult.getResult().get();

            classEntity.setPackageName(extractPackageName(cu));
            classEntity.setName(extractClassName(cu));
            classEntity.setMethods(new HashSet<>(extractMethods(cu)));
            classEntity.setFields(new HashSet<>(extractFields(cu)));
            classEntity.setUsedClasses(extractUsedClasses(cu));
            classEntity.setInnerClasses(extractInnerClasses(cu));
            classEntity.setInterfaces(new HashSet<>(extractInterfaces(cu)));
            classEntity.setSuperclass(extractSuperclass(cu));
            classEntity.setModifiers(new HashSet<>(extractModifiers(cu)));
            classEntity.setImports(extractImports(cu));

            // Generate and set the AI description
            classEntity.setAiDescription(generateClassDescription(classEntity));
            classEntity.setMetrics(calculateCodeQualityMetrics(cu, classEntity));

        }

        return classEntity;
    }


    private Map<String, Double> calculateCodeQualityMetrics(CompilationUnit cu, ClassEntity classEntity) {
        Map<String, Double> metrics = new HashMap<>();

        // Number of methods
        metrics.put("methodCount", (double) classEntity.getMethods().size());

        // Number of fields
        metrics.put("fieldCount", (double) classEntity.getFields().size());

        // Lines of Code (LOC)
        int loc = cu.toString().split("\n").length;
        metrics.put("linesOfCode", (double) loc);

        // Cyclomatic Complexity (simplified version)
        int cyclomaticComplexity = calculateCyclomaticComplexity(cu);
        metrics.put("cyclomaticComplexity", (double) cyclomaticComplexity);

        // Depth of Inheritance Tree (DIT)
        int dit = calculateDepthOfInheritance(classEntity);
        metrics.put("depthOfInheritance", (double) dit);

        return metrics;
    }

    private int calculateCyclomaticComplexity(CompilationUnit cu) {
        AtomicInteger complexity = new AtomicInteger(1); // Start from 1 for the method itself
        cu.walk(MethodDeclaration.class, md -> {
            complexity.addAndGet(md.getBody()
                    .map(body -> body.findAll(IfStmt.class).size() +
                            body.findAll(WhileStmt.class).size() +
                            body.findAll(ForStmt.class).size() +
                            body.findAll(ForEachStmt.class).size() +
                            body.findAll(SwitchStmt.class).size() +
                            body.findAll(CatchClause.class).size())
                    .orElse(0));
        });
        return complexity.get();
    }

    private int calculateDepthOfInheritance(ClassEntity classEntity) {
        int depth = 0;
        String superclass = classEntity.getSuperclass();
        while (superclass != null && !superclass.equals("Object")) {
            depth++;
            // Here you would need to look up the superclass in your parsed classes
            // For simplicity, we'll just break after the first level
            break;
        }
        return depth;
    }

    private String generateClassDescription(ClassEntity classEntity) {
        StringBuilder description = new StringBuilder();
        description.append("This Java class '").append(classEntity.getName()).append("' ");
        description.append("is located in the package '").append(classEntity.getPackageName()).append("'. ");

        if (!classEntity.getMethods().isEmpty()) {
            description.append("It contains ").append(classEntity.getMethods().size()).append(" method(s). ");
        }

        if (!classEntity.getFields().isEmpty()) {
            description.append("It has ").append(classEntity.getFields().size()).append(" field(s). ");
        }

        if (!classEntity.getInterfaces().isEmpty()) {
            description.append("It implements the following interface(s): ")
                    .append(String.join(", ", classEntity.getInterfaces())).append(". ");
        }

        if (classEntity.getSuperclass() != null && !classEntity.getSuperclass().isEmpty()) {
            description.append("It extends the class '").append(classEntity.getSuperclass()).append("'. ");
        }

        if (!classEntity.getModifiers().isEmpty()) {
            description.append("The class modifiers are: ")
                    .append(String.join(", ", classEntity.getModifiers())).append(". ");
        }

        return description.toString();
    }
    private String extractPackageName(CompilationUnit cu) {
        return cu.getPackageDeclaration().map(pd -> pd.getNameAsString()).orElse("");
    }

    private String extractClassName(CompilationUnit cu) {
        return cu.findFirst(ClassOrInterfaceDeclaration.class)
                .map(ClassOrInterfaceDeclaration::getNameAsString)
                .orElse("");
    }

    private List<String> extractMethods(CompilationUnit cu) {
        List<String> methods = new ArrayList<>();
        cu.findAll(MethodDeclaration.class).forEach(md -> {
            String methodSignature = md.getDeclarationAsString(false, false, false);
            methods.add(methodSignature);
        });
        return methods;
    }

    private List<String> extractFields(CompilationUnit cu) {
        List<String> fields = new ArrayList<>();
        cu.findAll(FieldDeclaration.class).forEach(fd -> {
            fd.getVariables().forEach(v -> {
                fields.add(v.getNameAsString());
            });
        });
        return fields;
    }

    private Set<String> extractUsedClasses(CompilationUnit cu) {
        Set<String> usedClasses = new HashSet<>();
        cu.findAll(ClassOrInterfaceType.class).forEach(cit -> {
            String className = cit.getNameAsString();
            className = className.replaceAll("<.*>", "").replaceAll("\\[\\]", "");
            if (!isPrimitiveOrCommonType(className)) {
                usedClasses.add(className);
            }
        });
        return usedClasses;
    }

    private boolean isPrimitiveOrCommonType(String typeName) {
        Set<String> primitiveAndCommonTypes = new HashSet<>(Arrays.asList(
                "byte", "short", "int", "long", "float", "double", "boolean", "char",
                "String", "Integer", "Long", "Float", "Double", "Boolean", "Character",
                "Void", "Number", "Object", "Class"
        ));
        return primitiveAndCommonTypes.contains(typeName) || typeName.startsWith("java.lang.");
    }

    private List<ClassEntity> extractInnerClasses(CompilationUnit cu) {
        List<ClassEntity> innerClasses = new ArrayList<>();
        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cid -> {
            if (cid.isNestedType()) {
                ClassEntity innerClass = new ClassEntity();
                innerClass.setName(cid.getNameAsString());
                // You might want to recursively extract information for inner classes
                innerClasses.add(innerClass);
            }
        });
        return innerClasses;
    }

    private List<String> extractInterfaces(CompilationUnit cu) {
        List<String> interfaces = new ArrayList<>();
        cu.findFirst(ClassOrInterfaceDeclaration.class).ifPresent(cid -> {
            cid.getImplementedTypes().forEach(it -> {
                interfaces.add(it.getNameAsString());
            });
        });
        return interfaces;
    }

    private String extractSuperclass(CompilationUnit cu) {
        return cu.findFirst(ClassOrInterfaceDeclaration.class)
                .flatMap(cid -> cid.getExtendedTypes().stream().findFirst())
                .map(ClassOrInterfaceType::getNameAsString)
                .orElse(null);
    }

    private List<String> extractModifiers(CompilationUnit cu) {
        return cu.findFirst(ClassOrInterfaceDeclaration.class)
                .map(cid -> cid.getModifiers().stream()
                        .map(m -> m.getKeyword().asString())
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private Set<String> extractImports(CompilationUnit cu) {
        return cu.getImports().stream()
                .map(importDecl -> importDecl.getNameAsString())
                .collect(Collectors.toSet());
    }

    @Override
    public List<Dependency> extractDependencies(ClassEntity classEntity, Set<ClassEntity> allClasses) {
        List<Dependency> dependencies = new ArrayList<>();
        Set<String> imports = classEntity.getImports();
        Set<String> usedClasses = classEntity.getUsedClasses();

        for (ClassEntity targetClass : allClasses) {
            if (!targetClass.equals(classEntity)) {
                if (imports.contains(targetClass.getFullyQualifiedName()) ||
                        usedClasses.contains(targetClass.getName())) {
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
        public void visit(MethodDeclaration n, Set<String> collector) {
            super.visit(n, collector);
            collector.add(n.getNameAsString());
        }
    }
}