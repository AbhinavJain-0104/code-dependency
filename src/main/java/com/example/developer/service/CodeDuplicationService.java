package com.example.developer.service;

import com.example.developer.model.ClassEntity;
import com.example.developer.model.DuplicationInfo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CodeDuplicationService {

    public List<DuplicationInfo> detectDuplication(List<ClassEntity> classes) {
        List<DuplicationInfo> duplications = new ArrayList<>();
        Map<String, List<String>> codeBlockMap = new HashMap<>();

        for (ClassEntity classEntity : classes) {
            String[] lines = classEntity.getContent().split("\n");
            for (int i = 0; i < lines.length - 5; i++) {
                String block = String.join("\n", Arrays.copyOfRange(lines, i, i + 6));
                codeBlockMap.computeIfAbsent(block, k -> new ArrayList<>())
                        .add(classEntity.getName() + ":" + (i + 1));
            }
        }

        codeBlockMap.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .forEach(entry -> duplications.add(new DuplicationInfo(entry.getKey(), entry.getValue())));

        return duplications;
    }
}