package com.keyrus.proxemconnector.connector.csv.configuration.repository.xmlConnector;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorXMLDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XMLConnectorJDBCDatabaseRepository extends JpaRepository<ConnectorXMLDAO, String> {
    boolean existsByName(String name);

    ConnectorXMLDAO findOneById(String id);
    ConnectorXMLDAO findByName(String name);
     Page<ConnectorXMLDAO> findByNameContaining(String name, Pageable pageable);

   // @Query(value = "SELECT * FROM connector", nativeQuery = true)
    List<ConnectorXMLDAO> findAll();
}
