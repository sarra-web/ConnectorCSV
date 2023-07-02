package com.keyrus.proxemconnector.connector.csv.configuration.repository;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepCommune extends JpaRepository<ConnectorDAO, String> {
    Page<ConnectorDAO> findAll(Pageable pageable);
    Page<ConnectorDAO> findByNameContaining(String name, Pageable pageable);
}
