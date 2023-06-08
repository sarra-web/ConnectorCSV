package com.keyrus.proxemconnector.connector.csv.configuration.repository;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConnectorJDBCDatabaseRepository extends JpaRepository<ConnectorDAO, String> {
    boolean existsByName(String name);
    Page<ConnectorDAO> findAll(Pageable pageable);
    ConnectorDAO findOneById(String id);
    ConnectorDAO findByName(String name);
     Page<ConnectorDAO> findByNameContaining(String name, Pageable pageable);

    @Query(value = "SELECT * FROM connector", nativeQuery = true)
    List<ConnectorDAO> findAll();
}
