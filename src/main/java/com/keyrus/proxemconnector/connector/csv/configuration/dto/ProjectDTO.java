package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import com.keyrus.proxemconnector.connector.csv.configuration.model.Project;
import io.vavr.control.Either;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public record ProjectDTO (

     String id,
     String name,
     String proxemToken){

    public ProjectDTO(Project project)
        {
            this(
                    project.id(),
                    project.name(),
                    project.proxemToken()
            );
        }

    public ProjectDTO(String id, String name, String proxemToken) {
        this.id = id;
        this.name = name;
        this.proxemToken = proxemToken;
    }

    public Either<Collection<Project.Error>, Project> toProject() {
        return
                Project.Builder
                        .builder()
                        .withId(ProjectDTO.idNonNullOrRandomId(this.id))
                        .withName(this.name)
                        .withToken(this.proxemToken)
                        .build();
    }

    private static String idNonNullOrRandomId(
            final String id
    ) {
        return
                Objects.nonNull(id)
                        ? id
                        : UUID.randomUUID().toString();
    }
}
