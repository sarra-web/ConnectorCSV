package com.keyrus.proxemconnector.connector.csv.configuration.repository;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ProjectDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectJDBCDatabaseRepository extends JpaRepository<ProjectDAO, String> {
    boolean existsByName(String name);

    ProjectDAO findOneById(String id);
    @Query(value = "SELECT * FROM project", nativeQuery = true)
    List<ProjectDAO> findAll();


}
