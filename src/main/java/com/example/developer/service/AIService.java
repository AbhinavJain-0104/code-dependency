package com.example.developer.service;

import com.example.developer.model.ClassEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AIService {

    private String generateClassDescription(ClassEntity classEntity) {
        StringBuilder description = new StringBuilder();
        description.append("This class contains: ")
                .append(classEntity.getImports().size()).append(" imports, ")
                .append(classEntity.getMethodCalls().size()).append(" method calls");

        if (!classEntity.getInnerClasses().isEmpty()) {
            description.append(", ").append(classEntity.getInnerClasses().size())
                    .append(" inner classes (")
                    .append(")");
        }

        description.append(". ");

        description.append("This class is likely responsible for ")
                .append(inferPurpose(classEntity.getName()))
                .append(".");

        return description.toString();
    }

    private String inferPurpose(String className) {
        String lowerClassName = className.toLowerCase();
        if (lowerClassName.contains("controller")) {
            return "handling HTTP requests and defining API endpoints";
        } else if (lowerClassName.contains("service")) {
            return "implementing business logic and coordinating between different components";
        } else if (lowerClassName.contains("repository")) {
            return "interacting with the database and performing data access operations";
        } else if (lowerClassName.contains("entity") || lowerClassName.contains("model")) {
            return "representing a data model or database entity";
        } else if (lowerClassName.contains("dto")) {
            return "transferring data between different layers of the application";
        } else if (lowerClassName.contains("config")) {
            return "configuring application settings or beans";
        } else {
            return "performing specific operations related to its name";
        }
    }
}