package com.keyrus.proxemconnector.connector.csv.configuration.repository.jdbcConnector;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorJDBCDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JDBCConnectorJDBCDatabaseRepository extends JpaRepository<ConnectorJDBCDAO, String> {
    boolean existsByName(String name);
    Page<ConnectorJDBCDAO> findAll(Pageable pageable);
    ConnectorJDBCDAO findOneById(String id);
    ConnectorJDBCDAO findByName(String name);
     Page<ConnectorJDBCDAO> findByNameContaining(String name, Pageable pageable);

   // @Query(value = "SELECT * FROM connector where connector_type=connecteurJDBC", nativeQuery = true)
    List<ConnectorJDBCDAO> findAll();
}
