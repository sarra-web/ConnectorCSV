package com.keyrus.proxemconnector.connector.csv.configuration.dao;

import com.keyrus.proxemconnector.connector.csv.configuration.model.Connector;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import io.vavr.control.Either;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;


@Data
@Entity
@Table(name = "connector")
public class ConnectorDAO implements Serializable {

    @Id
  //  @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true, insertable = true, updatable = true)
    private String id;
    @Column(name = "name", nullable = false, unique = true, insertable = true, updatable = true)
    private String name;
    @Column(name = "separator", nullable = false, unique = false, insertable = true, updatable = true)
    private String separator;
    @Column(name = "encoding", nullable = false, unique = false, insertable = true, updatable = true)
    private String encoding;
    @Column(name = "folder_to_scan", nullable = false, unique = false, insertable = true, updatable = true)
    private String folderToScan;
    @Column(name = "archive_folder", nullable = false, unique = false, insertable = true, updatable = true)
    private String archiveFolder;
    @Column(name = "failed_records_folder", nullable = false, unique = false, insertable = true, updatable = true)
    private String failedRecordsFolder;
    @Column(name = "contains_headers", nullable = false, unique = false, insertable = true, updatable = true)
    private boolean containsHeaders;
    @OneToMany(mappedBy = "referenceConnector", fetch = FetchType.EAGER, targetEntity = FieldDAO.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<FieldDAO> fields;

    public ConnectorDAO() {
    }

    public ConnectorDAO(
            String id,
            String name,
            String separator,
            String encoding,
            String folderToScan,
            String archiveFolder,
            String failedRecordsFolder,
            boolean containsHeaders,
            Collection<FieldDAO> fields
    ) {
        this.id = id;
        this.name = name;
        this.separator = separator;
        this.encoding = encoding;
        this.folderToScan = folderToScan;
        this.archiveFolder = archiveFolder;
        this.failedRecordsFolder = failedRecordsFolder;
        this.containsHeaders = containsHeaders;
        this.fields = fields;
    }

    public ConnectorDAO(
            final Connector connector
    ) {
        this(
                connector.id(),
                connector.name(),
                connector.separator(),
                connector.encoding(),
                connector.folderToScan(),
                connector.archiveFolder(),
                connector.failedRecordsFolder(),
                connector.containsHeaders(),
                ConnectorDAO.headersToHeaderDAOs(
                        connector.id(),
                        connector.fields()
                )
        );
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

    public String folderToScan() {
        return this.folderToScan;
    }

    public String archiveFolder() {
        return this.archiveFolder;
    }

    public String failedRecordsFolder() {
        return this.failedRecordsFolder;
    }

    public boolean containsHeaders() {
        return this.containsHeaders;
    }

    public Collection<FieldDAO> fields() {
        return this.fields;
    }

    public final Either<Collection<Connector.Error>, Connector> toConfiguration() {
        return
                Connector.Builder
                        .builder()
                        .withId(this.id)
                        .withName(this.name)
                        .withSeparator(this.separator)
                        .withEncoding(this.encoding)
                        .withFolderToScan(this.folderToScan)
                        .withArchiveFolder(this.archiveFolder)
                        .withFailedRecordsFolder(this.failedRecordsFolder)
                        .withContainsHeaders(this.containsHeaders)
                        .withHeaders(ConnectorDAO.headerDAOsToHeaderBuilders(this.fields))
                        .build();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ConnectorDAO) obj;
        return
                Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.separator, that.separator) &&
                Objects.equals(this.encoding, that.encoding) &&
                Objects.equals(this.folderToScan, that.folderToScan) &&
                Objects.equals(this.archiveFolder, that.archiveFolder) &&
                Objects.equals(this.failedRecordsFolder, that.failedRecordsFolder) &&
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
                        this.folderToScan,
                        this.archiveFolder,
                        this.failedRecordsFolder,
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
                            folderToScan=%s,
                            archiveFolder=%s,
                            failedRecordsFolder=%s,
                            containsHeaders=%s,
                            fields=%s
                        ]
                        """
                        .formatted(
                                this.id,
                                this.name,
                                this.separator,
                                this.encoding,
                                this.folderToScan,
                                this.archiveFolder,
                                this.failedRecordsFolder,
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
