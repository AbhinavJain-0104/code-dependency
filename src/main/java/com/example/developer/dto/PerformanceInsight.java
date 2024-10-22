package com.example.developer.dto;

import java.io.Serializable;
import java.util.Objects;

public class PerformanceInsight implements Serializable {
    private static final long serialVersionUID = 1L;
    private String className;
    private String issue;
    private int lineNumber;
    private String suggestion;


    public PerformanceInsight(String className, String issue, int lineNumber, String suggestion) {
        this.className = className;
        this.issue = issue;
        this.lineNumber = lineNumber;
        this.suggestion = suggestion;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerformanceInsight that = (PerformanceInsight) o;
        return lineNumber == that.lineNumber && Objects.equals(className, that.className) && Objects.equals(issue, that.issue) && Objects.equals(suggestion, that.suggestion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, issue, lineNumber, suggestion);
    }
}
