package com.keyrus.proxemconnector.connector.csv.configuration.model;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ERole;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Role {
    private Long id;

    private ERole name;

    public Role(Long id, ERole name) {
        this.id = id;
        this.name = name;
    }
}
