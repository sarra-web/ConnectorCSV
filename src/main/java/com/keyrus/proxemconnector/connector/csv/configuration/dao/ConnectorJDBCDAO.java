package com.keyrus.proxemconnector.connector.csv.configuration.dao;

import com.keyrus.proxemconnector.connector.csv.configuration.enumerations.QueryMode;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Project;
import com.keyrus.proxemconnector.connector.csv.configuration.service.jdbc.ConnectorJDBCService;
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
@DiscriminatorValue("connectorJDBC")
public class ConnectorJDBCDAO extends ConnectorDAO {
    //  @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "jdbc_url", nullable = false, unique = false, insertable = true, updatable = true)
    private String jdbcUrl;
    @Column(name = "username", nullable = false, unique = false, insertable = true, updatable = true)
    private String username;
    @Column(name = "password ", nullable = false, unique = false, insertable = true, updatable = true)
    private String password ;
    @Column(name = "class_name", nullable = false, unique = false, insertable = true, updatable = true)
    private String className;
    @Column(name = "table_name", nullable = false, unique = false, insertable = true, updatable = true)
    private String tableName;
    @Column(name = "initial_query", nullable = false, unique = false, insertable = true, updatable = true)
    private String initialQuery;
    @Column(name = "checkpoint_column",  unique = false, insertable = true, updatable = true)
    private String checkpointColumn;
    @Column(name = "incremental_variable", unique = false, insertable = true, updatable = true)
    private String incrementalVariable;
    @Column(name = "incremental_query", unique = false, insertable = true, updatable = true)
    private String incrementalQuery;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode", unique = false, insertable = true, updatable = true)
    private QueryMode mode;
    public ConnectorJDBCDAO() {

    }

    public ConnectorJDBCDAO(
            String id,
            String name,
            String jdbcUrl,
            String username,
            String password,
            String className,
            String tableName,
            String initialQuery,
            String checkpointColumn,
            String incrementalVariable,
            String incrementalQuery,
            QueryMode mode,
            Collection<FieldDAO> fields
             //,String projectName
            ,ProjectDAO  project
            //, UserDAO user
    ) {
        this.id=id;
        this.name=name;

        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.className = className;
        this.tableName = tableName;
        this.initialQuery=initialQuery;
        this.checkpointColumn=checkpointColumn;
        this.incrementalVariable=incrementalVariable;
        this.incrementalQuery=incrementalQuery;
        this.mode=mode;
        this.fields=fields;
       // this.projectName=projectName;
        this.project=project;
       // this.user=user;
    }

    public ConnectorJDBCDAO(
            final ConnectorJDBC connectorJDBC
    ) {
        this(
                connectorJDBC.id(),
                connectorJDBC.name(),
                connectorJDBC.jdbcUrl(),
                connectorJDBC.username(),
                connectorJDBC.password(),
                connectorJDBC.className(),
                connectorJDBC.tableName(),
                connectorJDBC.initialQuery(),
                connectorJDBC.checkpointColumn(),
                connectorJDBC.incrementalVariable(),
                connectorJDBC.incrementalQuery(),
                connectorJDBC.mode(),
                com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorJDBCDAO.headersToHeaderDAOs(
                        connectorJDBC.id(),
                        connectorJDBC.fields())
                , ConnectorJDBCDAO.projectToProjectDAO(ConnectorJDBCService.getProjectByName(connectorJDBC.projectName()).toProject().get())
                //,ConnectorCSVDAO.userToUserDAO(UserServiceConnector.getUserById(connectorJDBC.userId()).get().toUser())

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

    public String jdbcUrl() {
        return this.jdbcUrl;
    }

    public String username() {
        return this.username;
    }

    public String password() {
        return this.password;
    }

    public String className() {
        return this.className;
    }

    public String tableName() {
        return this.tableName;
    }
    public String  initialQuery() {
        return this.initialQuery;
    }

    public String  checkpointColumn() {
        return this.checkpointColumn;
    }

    public String  incrementalVariable() {
        return this.incrementalVariable;
    }

    public String  incrementalQuery() {
        return this.incrementalQuery;
    }
    public QueryMode  mode() {
        return this.mode;
    }

    public Collection<FieldDAO> fields() {
        return this.fields;
    }
    public ProjectDAO project(){return this.project;}
    /*public UserDAO user(){return this.user;}*/

    public final Either<Collection<ConnectorJDBC.Error>, ConnectorJDBC> toConfiguration() {
        return
                ConnectorJDBC.Builder
                        .builder()
                        .withId(this.id)
                        .withName(this.name)
                        .withjdbcUrl(this.jdbcUrl)
                        .withusername(this.username)
                        .withpassword(this.password)
                        .withclassName(this.className)
                        .withtableName(this.tableName)
                        .withinitialQuery(this.initialQuery)
                        .withcheckpointColumn(this.checkpointColumn)
                        .withincrementalVariable(this.incrementalVariable)
                        .withincrementalQuery(this.incrementalQuery)
                        .withmode(this.mode)
                        .withHeaders(com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorJDBCDAO.headerDAOsToHeaderBuilders(this.fields))
                        .withProjectName(this.project.getName())
                     //   .withUserId(this.user.getId())
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

        ConnectorJDBCDAO that = (ConnectorJDBCDAO) o;

        if (!jdbcUrl.equals(that.jdbcUrl)) return false;
        if (!username.equals(that.username)) return false;
        if (!password.equals(that.password)) return false;
        if (!className.equals(that.className)) return false;
        if (!tableName.equals(that.tableName)) return false;
        if (!initialQuery.equals(that.initialQuery)) return false;
        if (!checkpointColumn.equals(that.checkpointColumn)) return false;
        if (!incrementalVariable.equals(that.incrementalVariable)) return false;
        if (!incrementalQuery.equals(that.incrementalQuery)) return false;
        return mode == that.mode;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + jdbcUrl.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + className.hashCode();
        result = 31 * result + tableName.hashCode();
        result = 31 * result + initialQuery.hashCode();
        result = 31 * result + checkpointColumn.hashCode();
        result = 31 * result + incrementalVariable.hashCode();
        result = 31 * result + incrementalQuery.hashCode();
        result = 31 * result + mode.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ConnectorJDBCDAO{" +
                "jdbcUrl='" + jdbcUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", className='" + className + '\'' +
                ", tableName='" + tableName + '\'' +
                ", initialQuery='" + initialQuery + '\'' +
                ", checkpointColumn='" + checkpointColumn + '\'' +
                ", incrementalVariable='" + incrementalVariable + '\'' +
                ", incrementalQuery='" + incrementalQuery + '\'' +
                ", mode=" + mode +
                '}';
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
























/*package com.keyrus.proxemconnector.connector.csv.configuration.dao;

import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Project;
import io.vavr.control.Either;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@Entity
@DiscriminatorValue("connecteurJDBC")
public class ConnectorJDBCDAO extends ConnectorDAO {
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



        public String getSeparator() {
            return separator;
        }

        public void setSeparator(String separator) {
            this.separator = separator;
        }

        public String getEncoding() {
            return encoding;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getQuotingCaracter() {
            return quotingCaracter;
        }

        public void setQuotingCaracter(String quotingCaracter) {
            this.quotingCaracter = quotingCaracter;
        }

        public String getEscapingCaracter() {
            return escapingCaracter;
        }

        public void setEscapingCaracter(String escapingCaracter) {
            this.escapingCaracter = escapingCaracter;
        }

        public boolean isContainsHeaders() {
            return containsHeaders;
        }

        public void setContainsHeaders(boolean containsHeaders) {
            this.containsHeaders = containsHeaders;
        }


        public ConnectorJDBCDAO() {

        }

        public ConnectorJDBCDAO(
                String id,
                String name,
                String separator,
                String encoding,
                String path,
                String quotingCaracter,
                String escapingCaracter,
                boolean containsHeaders,
                Collection<FieldDAO> fields
                // ,ProjectDAO projectDAO
        ) {


            this.separator = separator;
            this.encoding = encoding;
            this.path = path;
            this.quotingCaracter = quotingCaracter;
            this.escapingCaracter = escapingCaracter;
            this.containsHeaders = containsHeaders;

            //  this.projectDAO=projectDAO;
        }

        public ConnectorJDBCDAO(
                final ConnectorJDBC connectorJDBC
        ) {
            this(
                    connectorJDBC.id(),
                    connectorJDBC.name(),
                    connectorJDBC.separator(),
                    connectorJDBC.encoding(),
                    connectorJDBC.path(),
                    connectorJDBC.quotingCaracter(),
                    connectorJDBC.escapingCaracter(),
                    connectorJDBC.containsHeaders(),
                    com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorJDBCDAO.headersToHeaderDAOs(
                            connectorJDBC.id(),
                            connectorJDBC.fields())
                    //,ConnectorJDBCDAO.projectToProjectDAO(connectorJDBC.project())
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

        public final Either<Collection<ConnectorJDBC.Error>, ConnectorJDBC> toConfiguration() {
            return
                    ConnectorJDBC.Builder
                            .builder()
                            .withId(this.id)
                            .withName(this.name)
                            .withSeparator(this.separator)
                            .withEncoding(this.encoding)
                            .withpath(this.path)
                            .withquotingCaracter(this.quotingCaracter)
                            .withescapingCaracter(this.escapingCaracter)
                            .withContainsHeaders(this.containsHeaders)
                            .withHeaders(com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorJDBCDAO.headerDAOsToHeaderBuilders(this.fields))
                            //.
                            //withProject(ConnectorJDBCDAO.projectDAOToProject(this.projectDAO))
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


       /* @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorJDBCDAO that = (com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorJDBCDAO) o;

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
*/