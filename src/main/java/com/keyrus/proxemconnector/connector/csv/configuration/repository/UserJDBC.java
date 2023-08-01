package com.keyrus.proxemconnector.connector.csv.configuration.repository;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJDBC extends JpaRepository<UserDAO,Long> {
}
