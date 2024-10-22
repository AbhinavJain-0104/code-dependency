package com.example.developer.dto;

import java.io.Serializable;
import java.util.Date;

public class MetricTrendDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String commitId;
    private Date timestamp;
    private int value;

    // Getters and setters


    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}