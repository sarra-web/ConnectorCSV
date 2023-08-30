package com.keyrus.proxemconnector.connector.csv.configuration.dao;

import com.keyrus.proxemconnector.connector.csv.configuration.model.*;
import com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ConnectorCSVService;
import io.vavr.control.Either;
import jakarta.persistence.Column;
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
public class ConnectorXMLDAO extends ConnectorDAO{


    //  @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "path_xml", nullable = false, unique = false, insertable = true, updatable = true)
    private String path;
    @Column(name = "tag_name", nullable = false, unique = false, insertable = true, updatable = true)
    private String tagName;

    public ConnectorXMLDAO() {

    }

    public ConnectorXMLDAO(
            String id,
            String name,
            String path,
            String tagName,
            Collection<FieldDAO> fields
            ,ProjectDAO project
            ,String userName
    ) {
        this.id=id;
        this.name=name;
        this.path = path;
        this.tagName = tagName;
        this.fields=fields;
        this.project=project;
        this.userName=userName;
    }

    public ConnectorXMLDAO(
            final ConnectorXML connectorXML
    ) {
        this(
                connectorXML.id(),
                connectorXML.name(),
                connectorXML.path(),
                connectorXML.tagName(),

                ConnectorXMLDAO.headersToHeaderDAOs(
                        connectorXML.id(),
                        connectorXML.fields())
                ,ConnectorXMLDAO.projectToProjectDAO(ConnectorCSVService.getProjectByName(connectorXML.projectName()).toProject().get())
                ,connectorXML.userName()
        );
    }

    private static ProjectDAO projectToProjectDAO(Project project) {
        return new ProjectDAO(project.id(),project.name(),project.proxemToken());
    }
    public static UserDAO userToUserDAO(User user) {
        return new UserDAO(user.getId(),user.getUsername(),user.getEmail(),user.getPassword(),rolesToRoleDAOs(user.getRoles()));
    }

    private static Collection<RoleDAO> rolesToRoleDAOs(final Collection<Role> roles) {
        return
                roles.stream()
                        .map(role -> new RoleDAO(role.getId(),role.getName())).toList();
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

    public String tagName() {
        return this.tagName;
    }
    public Collection<FieldDAO> fields() {
        return this.fields;
    }
    public  ProjectDAO project() {return this.project;}
    public  String userName() {return this.userName;}

    public final Either<Collection<Connector.Error>, ConnectorXML> toConfiguration() {
        return
                ConnectorXML.Builder
                        .builder()
                        .withId(this.id)
                        .withName(this.name)
                        .withpath(this.path)
                        .withtagName(this.tagName)
                        .withHeaders(ConnectorXMLDAO.headerDAOsToHeaderBuilders(this.fields))
                        .withProjectName(this.project.getName())

                        .withUserName(this.userName)
              .build();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ConnectorXMLDAO that = (ConnectorXMLDAO) o;

        if (!path.equals(that.path)) return false;
        return tagName.equals(that.tagName);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + tagName.hashCode();
        return result;
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

    @Override
    public String toString() {
        return "ConnectorXMLDAO{" +
                "path='" + path + '\'' +
                ", tagName='" + tagName + '\'' +
                '}';
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