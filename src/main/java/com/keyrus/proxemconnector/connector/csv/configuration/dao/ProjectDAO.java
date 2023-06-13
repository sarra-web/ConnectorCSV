package com.keyrus.proxemconnector.connector.csv.configuration.dao;

import com.keyrus.proxemconnector.connector.csv.configuration.model.Project;
import io.vavr.control.Either;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

@Entity
@NoArgsConstructor
@Data
@Table(name = "project")
public class ProjectDAO implements Serializable {
    @Id
    @Column(name = "id", nullable = false, unique = true, insertable = true, updatable = true)
    private String id;
    @Column(name = "name", nullable = false, unique = true, insertable = true, updatable = true)
    private String name;
    @Column(name = "proxem_token", nullable = false, unique = false, insertable = true, updatable = true)
    private String proxemToken;



    public ProjectDAO(final Project project) {

            this(
                    project.id(),
                    project.name(),
                    project.proxemToken()
            );

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
