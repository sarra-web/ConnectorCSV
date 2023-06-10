package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorCSV;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Project;
import io.vavr.control.Either;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;


public record ConnectorCSVDTO(
        String id,
        String name,
        String separator,
        String encoding,
        String path,
        String quotingCaracter,
        String escapingCaracter,
        boolean containsHeaders,
        Collection<FieldDTO> headers
     // ProjectDTO projectDTO
) {

    public ConnectorCSVDTO(
            final ConnectorCSV connectorCSV
    ) {
        this(
                connectorCSV.id(),
                connectorCSV.name(),
                connectorCSV.separator(),
                connectorCSV.encoding(),
                connectorCSV.path(),
                connectorCSV.quotingCaracter(),
                connectorCSV.escapingCaracter(),
                connectorCSV.containsHeaders(),
                ConnectorCSVDTO.headersToHeaderDTOs(connectorCSV.fields())
              //  ConnectorDTO.projectToProjectDTO(connector.project())
        );
    }

    private static ProjectDTO projectToProjectDTO(Project project) {
        return new ProjectDTO(
                project.id(),
                project.name(),
                project.proxemToken()
        );
    }

    public Either<Collection<ConnectorCSV.Error>, ConnectorCSV> toConfiguration() {
        return
                ConnectorCSV.Builder
                        .builder()
                        .withId(ConnectorCSVDTO.idNonNullOrRandomId(this.id))
                        .withName(this.name)
                        .withSeparator(this.separator)
                        .withEncoding(this.encoding)
                        .withpath(this.path)
                        .withquotingCaracter(this.quotingCaracter)
                        .withescapingCaracter(this.escapingCaracter)
                        .withContainsHeaders(this.containsHeaders)
                        .withHeaders(
                                ConnectorCSVDTO.headerDTOsToHeaderBuilders(
                                        this.id,
                                        this.headers
                                )
                        )
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
