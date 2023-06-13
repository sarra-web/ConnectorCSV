package com.keyrus.proxemconnector.connector.csv.configuration.repository;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.SquedulerDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SquedulerJDBC extends JpaRepository<SquedulerDAO,Long> {
 //   List<SquedulerDAO> findByConnectorId(String id);
   //  @Transactional
  //  void deleteByConnectorId(String connectorId);


}
