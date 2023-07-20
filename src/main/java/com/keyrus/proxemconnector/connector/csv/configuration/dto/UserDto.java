package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Role;
import com.keyrus.proxemconnector.connector.csv.configuration.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;
    @JsonProperty
    private Set<RoleDto> roles;




    public User toUser() {
        return
                new User(this.id,this.username,this.email,this.password,UserDto.rolesDtoToRoes(this.roles));
    }
    public static Set<Role> rolesDtoToRoes(Set<RoleDto> roleDtos){
        return roleDtos.stream().map(roleDto -> new Role(roleDto.getId(),roleDto.getName())).collect(Collectors.toSet());

    }
}
