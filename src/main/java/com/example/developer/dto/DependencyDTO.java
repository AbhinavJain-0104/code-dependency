package com.example.developer.dto;

import java.util.Objects;

public class DependencyDTO {
    private String source;
    private String target;
    private String type;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DependencyDTO that = (DependencyDTO) o;
        return Objects.equals(source, that.source) && Objects.equals(target, that.target) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, type);
    }
}