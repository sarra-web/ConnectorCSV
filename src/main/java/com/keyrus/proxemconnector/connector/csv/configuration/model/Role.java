package com.keyrus.proxemconnector.connector.csv.configuration.model;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ERole;
import lombok.Data;

@Data
public class Role {
    private Integer id;

    private ERole name;

    public Role(Integer id, ERole name) {
        this.id = id;
        this.name = name;
    }
}
