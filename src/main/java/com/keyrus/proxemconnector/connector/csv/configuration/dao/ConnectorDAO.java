package com.keyrus.proxemconnector.connector.csv.configuration.dao;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
//@MappedSuperclass()
@Table(name = "connector")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "connector_type", discriminatorType = DiscriminatorType.STRING)
public abstract class ConnectorDAO implements Serializable {

    @Id
    //  @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true, insertable = true, updatable = true)
    protected String id;
    @Column(name = "name", nullable = false, unique = true, insertable = true, updatable = true)
    protected String name;
    @OneToMany(mappedBy = "referenceConnector", fetch = FetchType.EAGER, targetEntity = FieldDAO.class, cascade = CascadeType.ALL, orphanRemoval = true)
    protected Collection<FieldDAO> fields;


}
