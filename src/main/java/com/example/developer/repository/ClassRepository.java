//package com.example.developer.repository;
//
//import com.example.developer.model.ClassEntity;
//import com.example.developer.model.ProjectModule;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
//    /**
//     * Finds a ClassEntity by its name, package name, and associated module.
//     *
//     * @param name        The name of the class.
//     * @param packageName The package name of the class.
//     * @param module      The ProjectModule to which the class belongs.
//     * @return An Optional containing the found ClassEntity, or empty if not found.
//     */
//    Optional<ClassEntity> findByNameAndPackageNameAndModule(String name, String packageName, ProjectModule module);
//}



   package com.example.developer.repository;

   import com.example.developer.model.ClassEntity;
   import org.springframework.data.repository.CrudRepository;
   import org.springframework.stereotype.Repository;

   import java.util.Optional;

@Repository
public interface ClassRepository extends CrudRepository<ClassEntity, String> {
    Optional<ClassEntity> findByNameAndPackageNameAndModuleName(String name, String packageName, String moduleName);
}