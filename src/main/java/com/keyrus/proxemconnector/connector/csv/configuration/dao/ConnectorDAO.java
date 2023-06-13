package com.keyrus.proxemconnector.connector.csv.configuration.dao;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;


@Entity
@Data
//@MappedSuperclass()
@Table(name = "connector")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "connector_type", discriminatorType = DiscriminatorType.STRING)
public abstract class ConnectorDAO implements Serializable {

    @Id
    @Column(name = "id", nullable = false, unique = true, insertable = true, updatable = true)
    protected String id;
    @Column(name = "name", nullable = false, unique = true, insertable = true, updatable = true)
    protected String name;
    @OneToMany(mappedBy = "referenceConnector", fetch = FetchType.EAGER, targetEntity = FieldDAO.class, cascade = CascadeType.ALL, orphanRemoval = true)
    protected Collection<FieldDAO> fields;

   /* @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "projet_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    protected ProjectDAO projectDAO;*/

    private String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<FieldDAO> getFields() {
        return fields;
    }

    public void setFields(Collection<FieldDAO> fields) {
        this.fields = fields;
    }
}
