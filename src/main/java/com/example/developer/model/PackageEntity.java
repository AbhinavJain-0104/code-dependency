package com.example.developer.model;

import org.springframework.data.redis.core.RedisHash;

import java.util.List;
import java.util.Objects;
import org.springframework.data.annotation.Id;


@RedisHash("packageEntity")
public class PackageEntity {
    @Id
    private Long id;

    private String name;

    private List<ClassEntity> classes;

    // Getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ClassEntity> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassEntity> classes) {
        this.classes = classes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackageEntity that = (PackageEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(classes, that.classes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, classes);
    }
}