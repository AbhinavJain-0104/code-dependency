package com.example.developer.repository;

import com.example.developer.model.ProjectModule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository  extends JpaRepository<ProjectModule, Long> {
}
