package com.keyrus.proxemconnector.connector.csv.configuration.dao;


import com.keyrus.proxemconnector.connector.csv.configuration.model.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Table(name = "roles")
@NoArgsConstructor
public final class RoleDAO implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ERole name;



  public RoleDAO(Long id, ERole name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ERole getName() {
    return name;
  }

  public void setName(ERole name) {
    this.name = name;
  }

  public RoleDAO(
          final Role role
          ) {
    this( role.getId(),role.getName()
    );
  }








}