package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.keyrus.proxemconnector.connector.csv.configuration.model.*;
import io.vavr.control.Either;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;




public record ConnectorXMLDTO (
        @JsonProperty
        String id,
        @JsonProperty
        String name,
        @JsonProperty
        String path,
        @JsonProperty
        String tagName,
        @JsonProperty
        Collection<FieldDTO> fields,

        @JsonProperty
        String projectName,
        @JsonProperty
        String userName
) {

    public ConnectorXMLDTO(
            final ConnectorXML connector
    ) {
        this(
                connector.id(),
                connector.name(),
                connector.path(),
                connector.tagName(),
                ConnectorXMLDTO.headersToHeaderDTOs(connector.fields())
                ,connector.projectName()
                ,connector.userName()
        );
    }

    private static ProjectDTO projectToProjectDTO(Project project) {
        return new ProjectDTO(
                project.id(),
                project.name(),
                project.proxemToken()
        );
    }

    public Either<Collection<Connector.Error>, ConnectorXML> toConfiguration() {


        return

                ConnectorXML.Builder
                        .builder()
                        .withId(ConnectorXMLDTO.idNonNullOrRandomId(this.id))
                        .withName(this.name)
                        .withpath(this.path)
                        .withtagName(this.tagName)
                        .withHeaders(
                                ConnectorXMLDTO.headerDTOsToHeaderBuilders(
                                        this.id,
                                        this.fields
                                )
                        )
                        .withProjectName(this.projectName)
                        .withUserName(this.userName)
                        .build();


    }

    private static Collection<FieldDTO> headersToHeaderDTOs(
            final Collection<Field> fields
    ) {
        return
                fields.stream()
                        .map(FieldDTO::new)
                        .toList();
    }

    private static Supplier<Either<Collection<Field.Error>, Field>>[] headerDTOsToHeaderBuilders(
            final String connectorId,
            final Collection<FieldDTO> headers
    ) {
        final Function<FieldDTO, Supplier<Either<Collection<Field.Error>, Field>>> headerBuilder =
                fieldDTO ->
                        () ->
                                fieldDTO.toHeader(
                                        connectorId
                                );
        return
                Objects.nonNull(headers)
                        ?
                        headers.stream()
                                .filter(Objects::nonNull)
                                .map(headerBuilder)
                                .toArray(Supplier[]::new)
                        :
                        Collections.emptySet()
                                .toArray(Supplier[]::new);
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