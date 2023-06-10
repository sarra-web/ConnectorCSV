package com.keyrus.proxemconnector.connector.csv.configuration.dao;

import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorCSV;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Project;
import io.vavr.control.Either;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;


@Data
@Entity
@DiscriminatorValue("connecteurCSV")
public class ConnectorCSVDAO extends ConnectorDAO{


  //  @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "separator", nullable = false, unique = false, insertable = true, updatable = true)
    private String separator;
    @Column(name = "encoding", nullable = false, unique = false, insertable = true, updatable = true)
    private String encoding;
    @Column(name = "path", nullable = false, unique = false, insertable = true, updatable = true)
    private String path;
    @Column(name = "quoting_caracter", nullable = false, unique = false, insertable = true, updatable = true)
    private String quotingCaracter;
    @Column(name = "escaping_caracter", nullable = false, unique = false, insertable = true, updatable = true)
    private String escapingCaracter;
    @Column(name = "contains_headers", nullable = false, unique = false, insertable = true, updatable = true)
    private boolean containsHeaders;


  /*  @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private ProjectDAO projectDAO;*/
    public ConnectorCSVDAO() {
    }

    public ConnectorCSVDAO(
            String id,
            String name,
            String separator,
            String encoding,
            String path,
            String quotingCaracter,
            String escapingCaracter,
            boolean containsHeaders,
            Collection<FieldDAO> fields
          //  ProjectDAO projectDAO
    ) {
        this.id=id;
        this.name=name;
        this.separator = separator;
        this.encoding = encoding;
        this.path = path;
        this.quotingCaracter = quotingCaracter;
        this.escapingCaracter = escapingCaracter;
        this.containsHeaders = containsHeaders;
        this.fields=fields;
       // this.projectDAO=projectDAO;
    }

    public ConnectorCSVDAO(
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
                ConnectorCSVDAO.headersToHeaderDAOs(
                        connectorCSV.id(),
                        connectorCSV.fields())
                //,ConnectorDAO.projectToProjectDAO(connector.project())
        );
    }

    private static ProjectDAO projectToProjectDAO(Project project) {
        return new ProjectDAO(project.id(),project.name(),project.proxemToken());
    }

    public String id() {
        return this.id;
    }

    public String name() {
        return this.name;
    }

    public String separator() {
        return this.separator;
    }

    public String encoding() {
        return this.encoding;
    }

    public String path() {
        return this.path;
    }

    public String quotingCaracter() {
        return this.quotingCaracter;
    }

    public String escapingCaracter() {
        return this.escapingCaracter;
    }

    public boolean containsHeaders() {
        return this.containsHeaders;
    }

    public Collection<FieldDAO> fields() {
        return this.fields;
    }

    public final Either<Collection<ConnectorCSV.Error>, ConnectorCSV> toConfiguration() {
        return
                ConnectorCSV.Builder
                        .builder()
                        .withId(this.id)
                        .withName(this.name)
                        .withSeparator(this.separator)
                        .withEncoding(this.encoding)
                        .withpath(this.path)
                        .withquotingCaracter(this.quotingCaracter)
                        .withescapingCaracter(this.escapingCaracter)
                        .withContainsHeaders(this.containsHeaders)
                        .withHeaders(ConnectorCSVDAO.headerDAOsToHeaderBuilders(this.fields))
                        .build();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ConnectorCSVDAO) obj;
        return
                Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.separator, that.separator) &&
                Objects.equals(this.encoding, that.encoding) &&
                Objects.equals(this.path, that.path) &&
                Objects.equals(this.quotingCaracter, that.quotingCaracter) &&
                Objects.equals(this.escapingCaracter, that.escapingCaracter) &&
                this.containsHeaders == that.containsHeaders &&
                (
                        Objects.nonNull(this.fields) &&
                        Objects.nonNull(that.fields)
                                ? this.fields.containsAll(that.fields)
                                : Objects.isNull(that.fields)
                );
    }

    @Override
    public int hashCode() {
        return
                Objects.hash(
                        this.id,
                        this.name,
                        this.separator,
                        this.encoding,
                        this.path,
                        this.quotingCaracter,
                        this.escapingCaracter,
                        this.containsHeaders,
                        this.fields
                );
    }

    @Override
    public String toString() {
        return
                """
                        ConfigurationDAO[
                            id=%s,
                            name=%s,
                            separator=%s,
                            encoding=%s,
                            path=%s,
                            quotingCaracter=%s,
                            escapingCaracter=%s,
                            containsHeaders=%s,
                            fields=%s
                        ]
                        """
                        .formatted(
                                this.id,
                                this.name,
                                this.separator,
                                this.encoding,
                                this.path,
                                this.quotingCaracter,
                                this.escapingCaracter,
                                this.containsHeaders,
                                this.fields
                        );
    }

    private static Collection<FieldDAO> headersToHeaderDAOs(
            final String configurationId,
            final Collection<Field> fields
    ) {
        return
                fields.stream()
                        .map(FieldDAO::new)
                        .toList();
    }

    private static Supplier<Either<Collection<Field.Error>, Field>>[] headerDAOsToHeaderBuilders(
            final Collection<FieldDAO> fields
    ) {
        final Function<FieldDAO, Supplier<Either<Collection<Field.Error>, Field>>> headerBuilder =
                fieldDAO ->
                        fieldDAO::toHeader;
        return
                Objects.nonNull(fields)
                        ?
                        fields.stream()
                                .filter(Objects::nonNull)
                                .map(headerBuilder)
                                .toArray(Supplier[]::new)
                        :
                        Collections.emptySet()
                                .toArray(Supplier[]::new);
    }
}
