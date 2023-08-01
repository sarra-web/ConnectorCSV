package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ERole;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private Long id;

    private ERole name;

public RoleDto(Role role){
     this(role.getId(),role.getName());
}
}
