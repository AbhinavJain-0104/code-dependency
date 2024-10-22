//package com.example.developer.service;
//
//import com.example.developer.analyzer.JavaScriptProjectAnalyzer;
//import com.example.developer.model.Project;
//import com.example.developer.service.ProjectAnalyzer;
//import org.springframework.cache.annotation.Cacheable;
//import org.eclipse.jgit.api.Git;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.io.FileUtils;
//import org.eclipse.jgit.api.errors.GitAPIException;
//import org.slf4j.Logger;
//import com.example.developer.dto.*;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.cache.annotation.CacheEvict;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.*;
//import java.nio.file.attribute.BasicFileAttributes;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Service
//public class ProjectService {
//    private static final Map<String, String> repositoryCache = new ConcurrentHashMap<>();
//    private static final String CACHE_DIR = System.getProperty("java.io.tmpdir") + File.separator + "repo_cache";
//
//    // ... rest of the class
//    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);
//    private final ProjectAnalyzer projectAnalyzer;
//    private JavaScriptProjectAnalyzer jsProjectAnalyzer;
//
//    private final String tempDir = System.getProperty("java.io.tmpdir");
//
//
//    @Autowired
//    public ProjectService(ProjectAnalyzer projectAnalyzer,JavaScriptProjectAnalyzer jsProjectAnalyzer) {
//        this.projectAnalyzer = projectAnalyzer;
//        this.jsProjectAnalyzer=jsProjectAnalyzer;
//    }
//
////    public Project analyzeGitHubProject(String gitRepoUrl) throws IOException, GitAPIException {
////        String projectDir = getOrCloneRepository(gitRepoUrl);
////        Project projectDTO = projectAnalyzer.analyzeProject(projectDir);
////        projectDTO.setPath(projectDir);
////        return projectDTO;
////    }
//
//    private String detectProjectType(String projectPath) {
//        Map<String, Integer> languageScores = new HashMap<>();
//        languageScores.put("Java", 0);
//        languageScores.put("JavaScript", 0);
//        // Add more languages as needed
//
//        try {
//            Files.walkFileTree(Paths.get(projectPath), new SimpleFileVisitor<Path>() {
//                @Override
//                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//                    String fileName = file.getFileName().toString().toLowerCase();
//                    if (fileName.endsWith(".java")) {
//                        languageScores.put("Java", languageScores.get("Java") + 1);
//                    } else if (fileName.endsWith(".js") || fileName.endsWith(".jsx") || fileName.endsWith(".ts") || fileName.endsWith(".tsx")) {
//                        languageScores.put("JavaScript", languageScores.get("JavaScript") + 1);
//                    }
//                    // Add more conditions for other languages
//                    return FileVisitResult.CONTINUE;
//                }
//            });
//        } catch (IOException e) {
//            logger.error("Error walking through project directory", e);
//        }
//
//        // Check for specific project files
//        if (Files.exists(Paths.get(projectPath, "pom.xml")) || Files.exists(Paths.get(projectPath, "build.gradle"))) {
//            languageScores.put("Java", languageScores.get("Java") + 10);
//        }
//        if (Files.exists(Paths.get(projectPath, "package.json"))) {
//            languageScores.put("JavaScript", languageScores.get("JavaScript") + 10);
//        }
//
//        // Determine the language with the highest score
//        return languageScores.entrySet().stream()
//                .max(Map.Entry.comparingByValue())
//                .map(Map.Entry::getKey)
//                .orElse("Unknown");
//    }
//
//    @Cacheable(value = "projects", key = "#gitRepoUrl")
//    public Project analyzeGitHubProject(String gitRepoUrl) throws IOException, GitAPIException {
//        String projectDir = getOrCloneRepository(gitRepoUrl);
//        String projectType = detectProjectType(projectDir);
//
//        Project project;
//        if ("Java".equals(projectType)) {
//            project = projectAnalyzer.analyzeProject(projectDir);
//        } else if ("JavaScript".equals(projectType)) {
//            project = jsProjectAnalyzer.analyzeProject(projectDir);
//        } else {
//            throw new UnsupportedOperationException("Unsupported project type: " + projectType);
//        }
//
//        project.setPath(projectDir);
//        return project;
//    }
//    @CacheEvict(value = "projects", key = "#gitRepoUrl")
//    public void clearProjectCache(String gitRepoUrl) {
//        // This method doesn't need to do anything, the annotation takes care of clearing the cache
//    }
//
//    @CacheEvict(value = "projects", allEntries = true)
//    public void clearAllProjectCaches() {
//        // This method clears all entries in the "projects" cache
//    }
//
//    private String getOrCloneRepository(String gitRepoUrl) throws IOException, GitAPIException {
//        String cacheKey = generateCacheKey(gitRepoUrl);
//        return repositoryCache.computeIfAbsent(cacheKey, key -> {
//            try {
//                return cloneRepository(gitRepoUrl, key);
//            } catch (Exception e) {
//                throw new RuntimeException("Failed to clone repository", e);
//            }
//        });
//    }
//
//    private String generateCacheKey(String gitRepoUrl) {
//        return DigestUtils.md5Hex(gitRepoUrl);
//    }
//    private String cloneRepository(String gitRepoUrl, String cacheKey) throws GitAPIException, IOException {
//        File cacheDir = new File(CACHE_DIR);
//        if (!cacheDir.exists()) {
//            cacheDir.mkdirs();
//        }
//
//        String projectDir = CACHE_DIR + File.separator + cacheKey;
//        Git.cloneRepository()
//                .setURI(gitRepoUrl)
//                .setDirectory(new File(projectDir))
//                .call();
//
//        return projectDir;
//    }
//
//    public void updateCachedRepository(String gitRepoUrl) throws GitAPIException, IOException {
//        String cacheKey = generateCacheKey(gitRepoUrl);
//        String projectDir = repositoryCache.get(cacheKey);
//        if (projectDir != null) {
//            Git git = Git.open(new File(projectDir));
//            git.pull().call();
//            git.close();
//        }
//    }
//
//
//}


package com.example.developer.service;

import com.example.developer.analyzer.JavaScriptProjectAnalyzer;
import com.example.developer.model.Project;
import org.springframework.cache.annotation.Cacheable;
import org.eclipse.jgit.api.Git;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProjectService {
    private static final Map<String, String> repositoryCache = new ConcurrentHashMap<>();
    private static final String CACHE_DIR = System.getProperty("java.io.tmpdir") + File.separator + "repo_cache";
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);
    private final ProjectAnalyzer projectAnalyzer;
    private JavaScriptProjectAnalyzer jsProjectAnalyzer;

    @Autowired
    public ProjectService(ProjectAnalyzer projectAnalyzer, JavaScriptProjectAnalyzer jsProjectAnalyzer) {
        this.projectAnalyzer = projectAnalyzer;
        this.jsProjectAnalyzer = jsProjectAnalyzer;
    }

    @Cacheable(value = "projects", key = "#gitRepoUrl")
    public Project analyzeGitHubProject(String gitRepoUrl) throws IOException, GitAPIException {
        String projectDir = getOrCloneRepository(gitRepoUrl);
        String projectType = detectProjectType(projectDir);

        // Extract the project name from the GitHub URL
        String projectName = extractProjectNameFromUrl(gitRepoUrl);

        Project project;
        if ("Java".equals(projectType)) {
            project = projectAnalyzer.analyzeProject(projectDir);
        } else if ("JavaScript".equals(projectType)) {
            project = jsProjectAnalyzer.analyzeProject(projectDir);
        } else {
            throw new UnsupportedOperationException("Unsupported project type: " + projectType);
        }

        // Set the correct project name
        project.setName(projectName);
        project.setPath(projectDir);

        return project;
    }

    private String extractProjectNameFromUrl(String gitRepoUrl) {
        String[] urlParts = gitRepoUrl.split("/");
        String repoName = urlParts[urlParts.length - 1];
        if (repoName.endsWith(".git")) {
            repoName = repoName.substring(0, repoName.length() - 4);
        }
        logger.info("Extracted project name from URL: {}", repoName);
        return repoName;
    }


    @CacheEvict(value = "projects", key = "#gitRepoUrl")
    public void clearProjectCache(String gitRepoUrl) {
        // This method doesn't need to do anything, the annotation takes care of clearing the cache
    }

    @CacheEvict(value = "projects", allEntries = true)
    public void clearAllProjectCaches() {
        // This method clears all entries in the "projects" cache
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

    private String detectProjectType(String projectPath) {
        Map<String, Integer> languageScores = new HashMap<>();
        languageScores.put("Java", 0);
        languageScores.put("JavaScript", 0);

        try {
            Files.walkFileTree(Paths.get(projectPath), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String fileName = file.getFileName().toString().toLowerCase();
                    if (fileName.endsWith(".java")) {
                        languageScores.put("Java", languageScores.get("Java") + 1);
                    } else if (fileName.endsWith(".js") || fileName.endsWith(".jsx") || fileName.endsWith(".ts") || fileName.endsWith(".tsx")) {
                        languageScores.put("JavaScript", languageScores.get("JavaScript") + 1);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            logger.error("Error walking through project directory", e);
        }

        if (Files.exists(Paths.get(projectPath, "pom.xml")) || Files.exists(Paths.get(projectPath, "build.gradle"))) {
            languageScores.put("Java", languageScores.get("Java") + 10);
        }
        if (Files.exists(Paths.get(projectPath, "package.json"))) {
            languageScores.put("JavaScript", languageScores.get("JavaScript") + 10);
        }

        return languageScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");
    }
}