package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Project;
import io.vavr.control.Either;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public record ConnectorJDBCDTO (
    String id,
    String name,
    String jdbcUrl ,
    String username ,
    String password ,
    String className,
    String tableName,
    @JsonProperty
    Collection<FieldDTO> fields

    //ProjectDTO projectDTO
) {

    public ConnectorJDBCDTO(
        final ConnectorJDBC connectorJDBC
    ) {
            this(
                    connectorJDBC.id(),
                    connectorJDBC.name(),
                    connectorJDBC.jdbcUrl (),
                    connectorJDBC.username (),
                    connectorJDBC.password (),
                    connectorJDBC.className(),
                    connectorJDBC.tableName(),
                    ConnectorJDBCDTO.headersToHeaderDTOs(connectorJDBC.fields())
                    //,connectorJDBC.id()

                    //, ConnectorCJDBCTO.projectToProjectDTO(connectorJDBC.project())
            );
        }

        private static ProjectDTO projectToProjectDTO(Project project) {
            return new ProjectDTO(
                    project.id(),
                    project.name(),
                    project.proxemToken()
            );
        }

        public Either<Collection<ConnectorJDBC.Error>, ConnectorJDBC> toConfiguration() {
            return
                    ConnectorJDBC.Builder
                            .builder()
                            .withId(ConnectorJDBCDTO.idNonNullOrRandomId(this.id))
                            .withName(this.name)
                            .withjdbcUrl (this.jdbcUrl )
                            .withusername (this.username )
                            .withpassword (this.password )
                            .withclassName(this.className)
                            .withtableName(this.tableName)
                            .withHeaders(
                                    ConnectorJDBCDTO.headerDTOsToHeaderBuilders(
                                            this.id,
                                            this.fields
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
