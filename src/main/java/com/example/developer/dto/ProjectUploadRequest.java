package com.example.developer.dto;

import javax.validation.constraints.NotBlank;

public class ProjectUploadRequest {

    @NotBlank(message = "Git repository URL must not be blank")
    private String gitRepoUrl;

    public ProjectUploadRequest() {
    }

    public ProjectUploadRequest(String gitRepoUrl) {
        this.gitRepoUrl = gitRepoUrl;
    }

    public String getGitRepoUrl() {
        return gitRepoUrl;
    }

    public void setGitRepoUrl(String gitRepoUrl) {
        this.gitRepoUrl = gitRepoUrl;
    }
}