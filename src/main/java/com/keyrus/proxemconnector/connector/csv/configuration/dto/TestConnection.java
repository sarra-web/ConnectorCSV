package com.keyrus.proxemconnector.connector.csv.configuration.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestConnection {
    private String username;
    private String password;
    private String jdbcURL;
    private String className;
}
