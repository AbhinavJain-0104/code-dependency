package com.example.developer.service;

import com.example.developer.model.Project;
import com.example.developer.service.ProjectAnalyzer;
import org.eclipse.jgit.api.Git;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import com.example.developer.dto.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProjectService {
    private static final Map<String, String> repositoryCache = new ConcurrentHashMap<>();
    private static final String CACHE_DIR = System.getProperty("java.io.tmpdir") + File.separator + "repo_cache";

    // ... rest of the class
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);
    private final ProjectAnalyzer projectAnalyzer;
    private final String tempDir = System.getProperty("java.io.tmpdir");

    @Autowired
    public ProjectService(ProjectAnalyzer projectAnalyzer) {
        this.projectAnalyzer = projectAnalyzer;
    }

    public Project analyzeGitHubProject(String gitRepoUrl) throws IOException, GitAPIException {
        String projectDir = getOrCloneRepository(gitRepoUrl);
        Project projectDTO = projectAnalyzer.analyzeProject(projectDir);
        projectDTO.setPath(projectDir);
        return projectDTO;
    }

    private String getOrCloneRepository(String gitRepoUrl) throws IOException, GitAPIException {
        String cacheKey = generateCacheKey(gitRepoUrl);
        return repositoryCache.computeIfAbsent(cacheKey, key -> {
            try {
                return cloneRepository(gitRepoUrl, key);
            } catch (Exception e) {
                throw new RuntimeException("Failed to clone repository", e);
            }
        });
    }

    private String generateCacheKey(String gitRepoUrl) {
        return DigestUtils.md5Hex(gitRepoUrl);
    }
    private String cloneRepository(String gitRepoUrl, String cacheKey) throws GitAPIException, IOException {
        File cacheDir = new File(CACHE_DIR);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        String projectDir = CACHE_DIR + File.separator + cacheKey;
        Git.cloneRepository()
                .setURI(gitRepoUrl)
                .setDirectory(new File(projectDir))
                .call();

        return projectDir;
    }

    public void updateCachedRepository(String gitRepoUrl) throws GitAPIException, IOException {
        String cacheKey = generateCacheKey(gitRepoUrl);
        String projectDir = repositoryCache.get(cacheKey);
        if (projectDir != null) {
            Git git = Git.open(new File(projectDir));
            git.pull().call();
            git.close();
        }
    }


}