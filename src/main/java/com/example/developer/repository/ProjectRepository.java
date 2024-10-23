//package com.example.developer.repository;
//
//import com.example.developer.model.Project;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface ProjectRepository extends JpaRepository<Project, Long> {
//}


package com.example.developer.repository;

import com.example.developer.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, String> {
    // You can add custom query methods here if needed
}