//package com.example.developer.repository;
//
//import com.example.developer.model.Dependency;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface DependencyRepository extends JpaRepository<Dependency, Long> {
//}
package com.example.developer.repository;

import com.example.developer.model.Dependency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DependencyRepository extends CrudRepository<Dependency, String> {
    // You can add custom query methods here if needed
}