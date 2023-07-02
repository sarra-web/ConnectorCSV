package com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorCSVDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CSVConnectorJDBCDatabaseRepository extends JpaRepository<ConnectorCSVDAO, String> {
    boolean existsByName(String name);

    ConnectorCSVDAO findOneById(String id);
    ConnectorCSVDAO findByName(String name);
     Page<ConnectorCSVDAO> findByNameContaining(String name, Pageable pageable);

   // @Query(value = "SELECT * FROM connector", nativeQuery = true)
    List<ConnectorCSVDAO> findAll();
}
