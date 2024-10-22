package com.example.developer.service;

import com.example.developer.model.ExternalDependency;
import com.example.developer.model.VersionIssue;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SemanticVersionAnalysisService {

    public List<VersionIssue> analyzeVersions(List<ExternalDependency> dependencies) {
        List<VersionIssue> issues = new ArrayList<>();
        Pattern semVerPattern = Pattern.compile("^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$");

        for (ExternalDependency dependency : dependencies) {
            if (!semVerPattern.matcher(dependency.getVersion()).matches()) {
                issues.add(new VersionIssue(dependency.getName(), dependency.getVersion(), "Version does not follow semantic versioning"));
            }
        }

        return issues;
    }
}