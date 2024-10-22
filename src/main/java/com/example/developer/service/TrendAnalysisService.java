package com.example.developer.service;

import com.example.developer.model.MetricTrend;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class TrendAnalysisService {

    public Map<String, List<MetricTrend>> analyzeTrends(String repoPath) {
        Map<String, List<MetricTrend>> trends = new HashMap<>();
        try (Repository repository = Git.open(new File(repoPath)).getRepository()) {
            Iterable<RevCommit> commits = new Git(repository).log().call();
            for (RevCommit commit : commits) {
                // This is a simplified version. In a real scenario, you'd checkout each commit
                // and run your analysis on it.
                MetricTrend trend = new MetricTrend(
                        commit.getName(),
                        commit.getAuthorIdent().getWhen(),
                        calculateComplexity(commit)
                );
                trends.computeIfAbsent("complexity", k -> new ArrayList<>()).add(trend);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trends;
    }

    private int calculateComplexity(RevCommit commit) {
        // This is a placeholder. In a real scenario, you'd analyze the code at this commit.
        return commit.getFullMessage().length(); // Just an example metric
    }
}