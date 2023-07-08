package com.keyrus.proxemconnector.connector.csv.configuration.repository;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.SquedulerDAO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SquedulerJDBC extends JpaRepository<SquedulerDAO,Long> {
    List<SquedulerDAO> findByConnectorDAOId(String id);
     @Transactional
  void deleteByConnectorDAOId(String connectorId);
    Page<SquedulerDAO> findByNameContaining(String name, Pageable pageable);


}
