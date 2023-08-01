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
@DiscriminatorValue("connectorCSV")
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

    /*@Autowired
    private UserJDBC userJDBC;*/

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
            ,ProjectDAO project
           //,UserDAO user
           // ,Integer userId
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
         this.project=project;
         //this.user=user;
      //  this.userId=userId;
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
                ,ConnectorCSVDAO.projectToProjectDAO(ConnectorCSVService.getProjectByName(connectorCSV.projectName()).toProject().get())
           // ,ConnectorCSVDAO.userToUserDAO(UserServiceConnector.getUserById(connectorCSV.userId()).get().toUser())
   // ,connectorCSV.userId()
    );
        //UserDAO userDAO=userJDBC.save(ConnectorCSVDAO.userToUserDAO(UserServiceConnector.getUserById(connectorCSV.userId()).get().toUser()));
      //  System.out.println("userDaoooo"+userDAO);
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
    public  ProjectDAO project() {return this.project;}
    /*public  UserDAO user() {return this.user;}*/

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
                        .withProjectName(this.project.getName())

                       // .withUserId(this.userId)
                        //.withUserId(this.user.getId())
                        //.withProject(ConnectorCSVDAO.projectDAOToProject(this.projectDAO))
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ConnectorCSVDAO that = (ConnectorCSVDAO) o;

        if (containsHeaders != that.containsHeaders) return false;
        if (!separator.equals(that.separator)) return false;
        if (!encoding.equals(that.encoding)) return false;
        if (!path.equals(that.path)) return false;
        if (!quotingCaracter.equals(that.quotingCaracter)) return false;
        return escapingCaracter.equals(that.escapingCaracter);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + separator.hashCode();
        result = 31 * result + encoding.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + quotingCaracter.hashCode();
        result = 31 * result + escapingCaracter.hashCode();
        result = 31 * result + (containsHeaders ? 1 : 0);
        return result;
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