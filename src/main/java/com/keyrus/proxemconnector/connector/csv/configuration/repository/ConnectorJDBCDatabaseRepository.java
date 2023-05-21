package com.keyrus.proxemconnector.connector.csv.configuration.repository;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectorJDBCDatabaseRepository extends JpaRepository<ConnectorDAO, String> {
    boolean existsByName(String name);

    ConnectorDAO findOneById(String id);
}
