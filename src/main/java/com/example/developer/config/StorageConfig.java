package com.example.developer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Component
public class StorageConfig {

    @Value("${project.storage.path}")
    private String projectStoragePath;

    private Path storagePath;

    @PostConstruct
    public void init() throws IOException {
        storagePath = Paths.get(projectStoragePath).toAbsolutePath().normalize();
        Files.createDirectories(storagePath);
    }

    public Path getStoragePath() {
        return storagePath;
    }
}