package com.example.developer.packagemanager;

import com.example.developer.model.ExternalDependency;

import java.util.List;

public interface PackageManagerAnalyzer {
    List<ExternalDependency> analyzePackageFile(String filePath);
    String getPackageManager();
}