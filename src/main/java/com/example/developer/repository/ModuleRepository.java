//package com.example.developer.repository;
//
//import com.example.developer.model.ProjectModule;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface ModuleRepository  extends JpaRepository<ProjectModule, Long> {
//}


package com.example.developer.repository;

import com.example.developer.model.ProjectModule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends CrudRepository<ProjectModule, String> {
    // You can add custom query methods here if needed
}