package com.example.developer.dto;

import java.io.Serializable;
import java.util.Objects;

public class DependencyDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String source;
    private String target;
    private String type;
    private boolean isCircular;

    private String name;
    private String version;
    private String packageManager;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageManager() {
        return packageManager;
    }

    public void setPackageManager(String packageManager) {
        this.packageManager = packageManager;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isCircular() {
        return isCircular;
    }

    public void setCircular(boolean circular) {
        isCircular = circular;
    }

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
        return isCircular == that.isCircular && Objects.equals(source, that.source) && Objects.equals(target, that.target) && Objects.equals(type, that.type) && Objects.equals(name, that.name) && Objects.equals(version, that.version) && Objects.equals(packageManager, that.packageManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, type, isCircular, name, version, packageManager);
    }
}