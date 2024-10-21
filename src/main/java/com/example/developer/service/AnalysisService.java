package com.example.developer.service;

import com.example.developer.config.StorageConfig;
import com.example.developer.model.Project;
import com.example.developer.model.ProjectStatus;
import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@Service
public class AnalysisService {
    private static final Logger logger = LoggerFactory.getLogger(AnalysisService.class);

    private final StorageConfig storageConfig;
    private final ProjectAnalyzer projectAnalyzer;

    @Autowired
    public AnalysisService(StorageConfig storageConfig, ProjectAnalyzer projectAnalyzer) {
        this.storageConfig = storageConfig;
        this.projectAnalyzer = projectAnalyzer;
    }

    @Async("taskExecutor")
    public CompletableFuture<Project> processProjectUploadAsync(String gitRepoUrl) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String projectPath = cloneRepository(gitRepoUrl);
                Project project = projectAnalyzer.analyzeProject(projectPath);
                project.setName(extractProjectName(gitRepoUrl));
                return project;
            } catch (Exception e) {
                logger.error("Error processing project upload", e);
                Project failedProject = new Project();
                failedProject.setStatus(ProjectStatus.FAILED);
                failedProject.setErrorMessage(e.getMessage());
                return failedProject;
            }
        });
    }

    private String cloneRepository(String gitRepoUrl) throws Exception {
        Path tempDir = Files.createTempDirectory(storageConfig.getStoragePath(), "repo-");
        Git.cloneRepository()
                .setURI(gitRepoUrl)
                .setDirectory(tempDir.toFile())
                .call();
        return tempDir.toString();
    }

    private String extractProjectName(String gitRepoUrl) {
        String[] parts = gitRepoUrl.split("/");
        return parts[parts.length - 1].replace(".git", "");
    }

}