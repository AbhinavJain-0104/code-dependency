package com.example.developer.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class APIUsagePatternDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String apiCall;
    private int usageCount;
    private List<String> usageLocations;

    public APIUsagePatternDTO() {

    }

    public String getApiCall() {
        return apiCall;
    }

    public void setApiCall(String apiCall) {
        this.apiCall = apiCall;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public List<String> getUsageLocations() {
        return usageLocations;
    }

    public void setUsageLocations(List<String> usageLocations) {
        this.usageLocations = usageLocations;
    }

    public APIUsagePatternDTO(String apiCall, int usageCount, List<String> usageLocations) {
        this.apiCall = apiCall;
        this.usageCount = usageCount;
        this.usageLocations = usageLocations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        APIUsagePatternDTO that = (APIUsagePatternDTO) o;
        return usageCount == that.usageCount && Objects.equals(apiCall, that.apiCall) && Objects.equals(usageLocations, that.usageLocations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiCall, usageCount, usageLocations);
    }
}