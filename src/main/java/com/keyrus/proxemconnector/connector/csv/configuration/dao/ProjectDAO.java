package com.keyrus.proxemconnector.connector.csv.configuration.dao;

import com.keyrus.proxemconnector.connector.csv.configuration.model.Project;
import io.vavr.control.Either;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "project")
public class ProjectDAO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true, insertable = true, updatable = true)
    private String id;
    @Column(name = "name", nullable = false, unique = true, insertable = true, updatable = true)
    private String name;
    @Column(name = "proxem_token", nullable = false, unique = false, insertable = true, updatable = true)
    private String proxemToken;

    public ProjectDAO() {

    }

    public ProjectDAO(final Project project) {

            this(
                    project.id(),
                    project.name(),
                    project.proxemToken()
            );

    }

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

    public String getProxemToken() {
        return proxemToken;
    }

    public void setProxemToken(String proxemToken) {
        this.proxemToken = proxemToken;
    }

    public ProjectDAO(String id, String name, String proxemToken) {
        this.id = id;
        this.name = name;
        this.proxemToken = proxemToken;
    }



    public final Either<Collection<Project.Error>, Project>  toProject() {

            return
                    Project.Builder
                            .builder()
                            .withId(this.id)
                            .withName(this.name).withToken(this.proxemToken)
                            .build();
        }

}
