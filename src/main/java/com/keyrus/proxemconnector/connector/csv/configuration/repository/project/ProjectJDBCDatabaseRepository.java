package com.keyrus.proxemconnector.connector.csv.configuration.repository.project;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ProjectDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectJDBCDatabaseRepository extends JpaRepository<ProjectDAO, String> {
    boolean existsByName(String name);
    ProjectDAO findOneById(String id);
    ProjectDAO findByName(String name);
    Page<ProjectDAO> findByNameContaining(String name, Pageable pageable);

    //Page<ConnectorDAO> findByPublished(boolean published, Pageable pageable);
   // Page<ConnectorDAO> findByNameContaining(String name, Pageable pageable);
    //List<ConnectorDAO> findByNameContaining(String title, Sort sort);
    @Query(value = "SELECT * FROM project", nativeQuery = true)
    List<ProjectDAO> findAll();


}
