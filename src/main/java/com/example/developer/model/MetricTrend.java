package com.example.developer.model;

import java.io.Serializable;
import java.util.Date;

public class MetricTrend implements Serializable {
    private static final long serialVersionUID = 1L;
    private String commitId;
    private Date timestamp;
    private int value;

    public MetricTrend(String commitId, Date timestamp, int value) {
        this.commitId = commitId;
        this.timestamp = timestamp;
        this.value = value;
    }

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