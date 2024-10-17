package com.example.developer.repository;

import com.example.developer.model.Dependency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DependencyRepository extends JpaRepository<Dependency, Long> {
}
