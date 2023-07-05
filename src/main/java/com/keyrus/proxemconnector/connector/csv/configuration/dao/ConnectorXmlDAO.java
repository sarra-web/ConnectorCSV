package com.keyrus.proxemconnector.connector.csv.configuration.dao;

import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import io.vavr.control.Either;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;


@Data
@Entity
@DiscriminatorValue("connectorXML")
public class ConnectorXmlDAO extends ConnectorDAO{
    //  @GeneratedValue(strategy = GenerationType.AUTO)
     /*   @Column(name = "path", nullable = false, unique = false, insertable = true, updatable = true)
        private String path;



        public ConnectorXmlDAO() {
        }

        public ConnectorXmlDAO(
                String id,
                String name,
                String path,
                boolean containsHeaders,
                Collection<FieldDAO> fields
                // ,ProjectDAO projectDAO
        ) {
            this.id=id;
            this.name=name;
            this.path = path;
            this.fields=fields;
            //  this.projectDAO=projectDAO;
        }

        public ConnectorXmlDAO(
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
                    com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorXmlDAO.headersToHeaderDAOs(
                            connectorCSV.id(),
                            connectorCSV.fields())
                    //,ConnectorCSVDAO.projectToProjectDAO(connectorCSV.project())
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

        public String path() {
            return this.path;
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
                            .withpath(this.path)
                            .withHeaders(com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorCSVDAO.headerDAOsToHeaderBuilders(this.fields))
                            //.
                            //withProject(ConnectorCSVDAO.projectDAOToProject(this.projectDAO))
                            .build();
        }
    /*private static Supplier<Either<Collection<Project.Error>, Project>> projectDAOToProject(
            final Project project
    ) {
        final Function<ProjectDAO, Supplier<Either<Collection<Project.Error>, Project>>> projectBuilder =
                projectDAO ->
                        projectDAO::toProject;
        return
                Objects.nonNull(project)
                        ?
                        project.
                                .toArray(Supplier[]::new)
                        :
                        Collections.emptySet()
                                .toArray(Supplier[]::new);
    }*/



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


