package com.keyrus.proxemconnector.connector.csv.configuration.dao;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @Column(name = "connector_type", insertable=false, updatable=false)
    protected String typeConnector;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnore
    protected ProjectDAO project;

  /*  @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnore
    protected UserDAO user;*/



 /*   @Column(name = "user_id", insertable=false, updatable=false)
    protected Integer userId;*/


    public String getId() {
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

    public String getTypeConnector() {
        return typeConnector;
    }

    public void setTypeConnector(String typeConnector) {
        this.typeConnector = typeConnector;
    }

    public ProjectDAO getProject() {
        return project;
    }

    public void setProject(ProjectDAO projectDAO) {
        this.project = projectDAO;
    }
/*  public UserDAO getUser() {
        return user;
    }

    public void setUser(UserDAO user) {
        this.user = user;
    }
*/
    /*public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }*/
}