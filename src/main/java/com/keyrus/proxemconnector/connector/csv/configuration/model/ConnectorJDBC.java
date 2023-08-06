package com.keyrus.proxemconnector.connector.csv.configuration.model;

import com.keyrus.proxemconnector.connector.csv.configuration.enumerations.QueryMode;
import io.vavr.control.Either;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConnectorJDBC extends Connector{


    private final String jdbcUrl ;
    private final String username;
    private final String password ;
    private final String className;
    private final String  tableName;
    private final String  initialQuery ;
    private final String checkpointColumn ;
    private final String incrementalVariable;
    private final String incrementalQuery;
    private final QueryMode mode;

    private ConnectorJDBC(
            final String id,
            final String name,
            final String jdbcUrl ,
            final String username,
            final String password ,
            final String className,
            final String  tableName,
            final String  initialQuery ,
            final String checkpointColumn ,
            final String incrementalVariable,
            final String incrementalQuery,
            final QueryMode mode,
            final Collection<Field> fields,
            final String projectName
            , final String userName
    ) {
        super(id,name,fields ,projectName
                ,userName
        );
        this.jdbcUrl  = jdbcUrl ;
        this.username = username;
        this.password  = password ;
        this.className = className;
        this. tableName =  tableName;
        this.initialQuery=initialQuery;
        this.checkpointColumn=checkpointColumn;
        this.incrementalVariable=incrementalVariable;
        this.incrementalQuery=incrementalQuery;
        this.mode=mode;

    }
    public String name() {
        return this.name;
    }

    public String id() {
        return this.id;
    }

    public String jdbcUrl () {
        return this.jdbcUrl ;
    }

    public String username() {
        return this.username;
    }

    public String password () {
        return this.password ;
    }

    public String className() {
        return this.className;
    }

    public String  tableName() {
        return this. tableName;
    }
    public String  initialQuery() {
        return this. initialQuery;
    }

    public String  checkpointColumn() {
        return this. checkpointColumn;
    }

    public String  incrementalVariable() {
        return this. incrementalVariable;
    }

    public String  incrementalQuery() {
        return this. incrementalQuery;
    }
    public QueryMode  mode() {
        return this.mode;
    }
    public Collection<Field> fields() {
        return this.fields;
    }
    public String  projectName() {
            return this.projectName;
        }
   public String  userName() {
        return this.userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ConnectorJDBC that = (ConnectorJDBC) o;

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
        return "ConnectorJDBC{" +
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

    private static Either<Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>, com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC> of(
            final String id,
            final String name,
            final String jdbcUrl ,
            final String username,
            final String password ,
            final String className,
            final String  tableName,
            final String  initialQuery ,
            final String checkpointColumn ,
            final String incrementalVariable,
            final String incrementalQuery,
            final QueryMode mode,
            final Collection<Field> fields
            ,final String projectName
            , final String userName
    ) {
        return
                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkThenInstantiate(
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.instance(
                                id,
                                name,
                                jdbcUrl ,
                                username,
                                password ,
                                className,
                                tableName,
                                initialQuery,checkpointColumn,incrementalVariable,incrementalQuery,mode,
                                fields
                                ,projectName
                                ,userName
                        ),
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkId(
                                id
                        ), com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkName(
                                name
                        ),
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkHeaders(
                                fields
                        )
                );
    }

    private static Supplier<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC> instance(
            final String id,
            final String name,
            final String jdbcUrl ,
            final String username,
            final String password ,
            final String className,
            final String  tableName,
            final String  initialQuery ,
            final String checkpointColumn ,
            final String incrementalVariable,
            final String incrementalQuery,
            final QueryMode mode,
            final Collection<Field> fields,
            final String projectName
            , final String userName
    ) {

        return

                () ->
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC(
                                id,
                                name,
                                jdbcUrl ,
                                username,
                                password ,
                                className,
                                tableName,
                                initialQuery,
                                checkpointColumn,
                                incrementalVariable,
                                incrementalQuery,
                                mode,
                                fields
                                ,projectName
                                ,userName
                        );
    }

    private static Supplier<Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>> checkId(
            final String id
    ) {
        return
                () ->
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNonNullableNonBlankStringField(
                                id,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.IdMalformed::new
                        );
    }

    private static Supplier<Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>> checkName(
            final String name
    ) {
        return
                () ->
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNonNullableNonBlankStringField(
                                name,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.NameMalformed::new
                        );
    }

    private static Supplier<Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>> checkjdbcUrl (
            final String jdbcUrl
    ) {
        return
                () ->
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNullableNonBlankStringField(
                                jdbcUrl ,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.jdbcUrlMalformed::new,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC::checkjdbcUrlIsNotComposedOfLettersOrDigits
                        );
    }


    private static Supplier<Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>> checkpassword (
            final String password
    ) {
        return
                () ->
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNonNullableNonBlankStringField(
                                password ,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.passwordMalformed::new
                        );
    }

    private static Supplier<Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>> checkclassName(
            final String className
    ) {
        return
                () ->
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNonNullableNonBlankStringField(
                                className,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.classNameMalformed::new
                        );
    }

    private static Supplier<Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>> checktableName(
            final String  tableName
    ) {
        return
                () ->
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNonNullableNonBlankStringField(
                                tableName,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error. tableNameMalformed::new
                        );
    }

    private static Supplier<Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>> checkHeaders(
            final Collection<Field> fields
    ) {
        return
                () ->
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkHeaderFormat(fields)
                                .or(() ->
                                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkHeadersIds(fields)
                                )
                                .or( () ->
                                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkHeadersPositions(fields)
                                );
    }

    private static Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> checkHeadersPositions(
            final Collection<Field> fields
    ) {
        return
                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkField(
                        fields,
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.HeadersSequenceMalformed::new,
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC::checkHeadersPositionSequence
                );
    }

    private static Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> checkHeadersIds(
            final Collection<Field> fields
    ) {
        return
                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkField(
                        fields,
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.IdHeaderMissing::new,
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC::checkHeadersContainsIds
                );
    }

    private static Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> checkHeaderFormat(
            final Collection<Field> fields
    ) {
        return
                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNonNullableField(
                        fields,
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.HeaderMalformed::new
                );
    }

    private static boolean checkHeadersContainsIds(
            final Collection<Field> fields
    ) {
        return
                Objects.nonNull(fields) &&
                        fields.stream()
                                .anyMatch(Field::partOfDocumentIdentity);
    }

    private static boolean checkHeadersPositionSequence(
            final Collection<Field> fields
    ) {
        return
                Objects.nonNull(fields) &&
                        fields.stream()
                                .map(Field::position)
                                .max(Comparator.naturalOrder())
                                .map(max -> fields.size() == max)
                                .orElse(false);
    }

    private static boolean checkjdbcUrlIsNotComposedOfLettersOrDigits(
            final String jdbcUrl
    ) {
        return
                jdbcUrl .chars()
                        .noneMatch(
                                ((IntPredicate) Character::isDigit)
                                        .or(Character::isAlphabetic)
                        );
    }

    @SafeVarargs
    private static Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> checkNonNullableNonBlankStringField(
            final String field,
            final Supplier<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> errorIfInvalid,
            final Predicate<String>... extraChecks
    ) {
        return
                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNonNullableField(
                        field,
                        errorIfInvalid,
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.mergePredicates(
                                Predicate.not(String::isBlank),
                                extraChecks
                        )
                );
    }

    @SafeVarargs
    private static Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> checkNullableNonBlankStringField(
            final String field,
            final Supplier<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> errorIfInvalid,
            final Predicate<String>... extraChecks
    ) {
        return
                Objects.nonNull(field)
                        ?
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNonNullableField(
                                field,
                                errorIfInvalid,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.mergePredicates(
                                        Predicate.not(String::isBlank),
                                        extraChecks
                                )
                        )
                        :
                        Optional.empty();
    }

    @SafeVarargs
    private static <FIELD> Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> checkNonNullableField(
            final FIELD field,
            final Supplier<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> errorIfInvalid,
            final Predicate<FIELD>... extraChecks
    ) {
        return
                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkField(
                        field,
                        errorIfInvalid,
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.mergePredicates(
                                Objects::nonNull,
                                extraChecks
                        )
                );
    }

    @SafeVarargs
    private static <FIELD> Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> checkField(
            final FIELD field,
            final Supplier<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> errorIfInvalid,
            final Predicate<FIELD>... validityChecks
    ) {
        return
                Arrays.stream(validityChecks)
                        .anyMatch(Predicate.not(it -> it.test(field)))
                        ? Optional.ofNullable(errorIfInvalid.get())
                        : Optional.empty();
    }

    @SafeVarargs
    private static Either<Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>, com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC> checkThenInstantiate(
            final Supplier<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC> instance,
            final Supplier<Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>>... checks
    ) {
        final var errors =
                Arrays.stream(checks)
                        .map(Supplier::get)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toUnmodifiableSet());
        return
                errors.isEmpty()
                        ? Either.right(instance.get())
                        : Either.left(errors);
    }

    @SafeVarargs
    private static <TYPE> Predicate<TYPE> mergePredicates(
            final Predicate<TYPE> predicate,
            final Predicate<TYPE>... predicates
    ) {
        return
                Stream.of(
                                Stream.of(predicate),
                                Arrays.stream(predicates)
                        )
                        .flatMap(Function.identity())
                        .filter(Objects::nonNull)
                        .reduce(
                                it -> true,
                                Predicate::and
                        );
    }

    public static final class Builder {
        private final String id;
        private final String name;
        private final String jdbcUrl ;
        private final String username;
        private final String password ;
        private final String className;
        private final String  tableName;
        private  final String  initialQuery ;
        private final String checkpointColumn ;
        private final String incrementalVariable;
        private final String incrementalQuery;
        private final QueryMode mode;
        private final Collection<Field> fields;
        private final String projectName;
        private final String userName;
        private final Collection<Supplier<Either<Collection<Field.Error>, Field>>> headerBuilders;

        private Builder(
                final String id,
                final String name,
                final String jdbcUrl ,
                final String username,
                final String password ,
                final String className,
                final String  tableName,
                final String  initialQuery ,
                final String checkpointColumn ,
                final String incrementalVariable,
                final String incrementalQuery,
                final QueryMode mode,
                final Collection<Field> fields,
                final String projectName,
               final String userName,
                final Collection<Supplier<Either<Collection<Field.Error>, Field>>> headerBuilders
        ) {
            this.id = id;
            this.name = name;
            this.jdbcUrl  = jdbcUrl ;
            this.username = username;
            this.password  = password ;
            this.className = className;
            this.tableName =  tableName;
            this.initialQuery=initialQuery;
            this.checkpointColumn=checkpointColumn;
            this.incrementalVariable=incrementalVariable;
            this.incrementalQuery=incrementalQuery;
            this.mode=mode;
            this.fields = fields;
            this.projectName = projectName;
            this.userName = userName;
            this.headerBuilders = headerBuilders;
        }

        public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withId(
                final String id
        ) {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                    id,
                                    this.id
                            ),
                            this.name,
                            this.jdbcUrl ,
                            this.username,
                            this.password ,
                            this.className,
                            this. tableName,
                            this.initialQuery,
                            this.checkpointColumn,
                            this.incrementalVariable,
                            this.incrementalQuery,
                            this.mode,
                            this.fields
                            , this.projectName
                            , this.userName
                            , this.headerBuilders
                    );
        }
        public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withProjectName(
                final String projectName
        ) {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(this.id,
                            this.name,
                            this.jdbcUrl ,
                            this.username,
                            this.password ,
                            this.className,
                            this. tableName,
                            this.initialQuery,
                            this.checkpointColumn,
                            this.incrementalVariable,
                            this.incrementalQuery,
                            this.mode,
                            this.fields,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(projectName,this.projectName),
                            this.userName,
                            this.headerBuilders

                    );
        }

        public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withName(
                final String name
        ) {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                            this.id,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                    name,
                                    this.name
                            ),
                            this.jdbcUrl ,
                            this.username,
                            this.password ,
                            this.className,
                            this. tableName,
                            this.initialQuery,
                            this.checkpointColumn,
                            this.incrementalVariable,
                            this.incrementalQuery,
                            this.mode,
                            this.fields
                            ,this.projectName
                            , this.userName
                            , this.headerBuilders
                    );
        }
      public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withUserName(
                final String userName
        ) {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                            this.id,
                            this.name,
                            this.jdbcUrl ,
                            this.username,
                            this.password ,
                            this.className,
                            this. tableName,
                            this.initialQuery,
                            this.checkpointColumn,
                            this.incrementalVariable,
                            this.incrementalQuery,
                            this.mode,
                            this.fields
                            ,this.projectName
                            ,com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                            userName,
                            this.userName
                    ), this.headerBuilders
                    );
        }

        public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withjdbcUrl (
                final String jdbcUrl
        ) {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                            this.id,
                            this.name,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                    jdbcUrl ,
                                    this.jdbcUrl
                            ),
                            this.username,
                            this.password ,
                            this.className,
                            this. tableName,
                            this.initialQuery,
                            this.checkpointColumn,
                            this.incrementalVariable,
                            this.incrementalQuery,
                            this.mode,
                            this.fields,
                            this.projectName,
                            this.userName,
                            this.headerBuilders
                    );
        }

        public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withusername(
                final String username
        ) {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                            this.id,
                            this.name,
                            this.jdbcUrl ,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                    username,
                                    this.username
                            ),
                            this.password ,
                            this.className,
                            this. tableName,
                            this.initialQuery,
                            this.checkpointColumn,
                            this.incrementalVariable,
                            this.incrementalQuery,
                            this.mode,
                            this.fields,
                            this.projectName,
                            this.userName,
                            this.headerBuilders
                    );
        }

        public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withpassword (
                final String password
        ) {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                            this.id,
                            this.name,
                            this.jdbcUrl ,
                            this.username,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                    password ,
                                    this.password
                            ),
                            this.className,
                            this. tableName,
                            this.initialQuery,
                            this.checkpointColumn,
                            this.incrementalVariable,
                            this.incrementalQuery,
                            this.mode,
                            this.fields,
                            this.projectName,
                            this.userName,
                            this.headerBuilders
                    );
        }

        public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withclassName(
                final String className
        ) {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                            this.id,
                            this.name,
                            this.jdbcUrl ,
                            this.username,
                            this.password ,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                    className,
                                    this.className
                            ),
                            this. tableName,
                            this.initialQuery,
                            this.checkpointColumn,
                            this.incrementalVariable,
                            this.incrementalQuery,
                            this.mode,
                            this.fields
                            ,this.projectName
                            ,
                            this.userName,
                            this.headerBuilders
                    );
        }

        public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withtableName(
                final String  tableName
        ) {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                            this.id,
                            this.name,
                            this.jdbcUrl ,
                            this.username,
                            this.password ,
                            this.className,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                    tableName,
                                    this. tableName
                            ),
                            this.initialQuery,
                            this.checkpointColumn,
                            this.incrementalVariable,
                            this.incrementalQuery,
                            this.mode,
                            this.fields
                            , this.projectName
                            , this.userName
                            , this.headerBuilders
                    );
        }

        public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withinitialQuery(
                final String  initialQuery
        ) {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                            this.id,
                            this.name,
                            this.jdbcUrl ,
                            this.username,
                            this.password ,
                            this.className,
                            this.tableName,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                    initialQuery,
                                    this.initialQuery
                            ),


                            this.checkpointColumn,
                            this.incrementalVariable,
                            this.incrementalQuery,
                            this.mode,
                            this.fields
                            , this.projectName
                            , this.userName
                            , this.headerBuilders
                    );
        }
        public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withcheckpointColumn(
                final String  checkpointColumn
        ) {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                            this.id,
                            this.name,
                            this.jdbcUrl ,
                            this.username,
                            this.password ,
                            this.className,
                            this.tableName,
                            this.initialQuery,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                    checkpointColumn,
                                    this. checkpointColumn
                            ),

                            this.incrementalVariable,
                            this.incrementalQuery,
                            this.mode,
                            this.fields,
                            this.projectName,
                            this.userName,
                            this.headerBuilders
                    );
        }
        public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withincrementalVariable(
                final String  incrementalVariable
        ) {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                            this.id,
                            this.name,
                            this.jdbcUrl ,
                            this.username,
                            this.password ,
                            this.className,
                            this.tableName,
                            this.initialQuery,
                            this.checkpointColumn,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                    incrementalVariable,
                                    this. incrementalVariable
                            ),

                            this.incrementalQuery,
                            this.mode,
                            this.fields
                            , this.projectName
                            , this.userName
                            , this.headerBuilders
                    );
        }
        public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withincrementalQuery(
                final String  incrementalQuery
        ) {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                            this.id,
                            this.name,
                            this.jdbcUrl ,
                            this.username,
                            this.password ,
                            this.className,
                            this.tableName,

                            this.initialQuery,
                            this.checkpointColumn,
                            this.incrementalVariable,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                    incrementalQuery,
                                    this. incrementalQuery
                            ),this.mode,

                            this.fields
                            , this.projectName
                            , this.userName
                            , this.headerBuilders
                    );
        }

        public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withmode(
                final QueryMode  mode
        ) {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                            this.id,
                            this.name,
                            this.jdbcUrl ,
                            this.username,
                            this.password ,
                            this.className,
                            this.tableName,

                            this.initialQuery,
                            this.checkpointColumn,
                            this.incrementalVariable,
                            this. incrementalQuery,
                            Builder.nonNullOrDefault(
                                    mode,
                                    this.mode
                            ),

                            this.fields
                            , this.projectName
                            , this.userName
                            , this.headerBuilders
                    );
        }





        public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withContainsHeaders(
                final boolean containsHeaders
        ) {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                            this.id,
                            this.name,
                            this.jdbcUrl ,
                            this.username,
                            this.password ,
                            this.className,
                            this. tableName,
                            this.initialQuery,
                            this.checkpointColumn,
                            this.incrementalVariable,
                            this.incrementalQuery,
                            this.mode,
                            this.fields
                            ,this.projectName
                            , this.userName
                            , this.headerBuilders
                    );
        }

        public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withHeaders(
                final Collection<Field> fields
        ) {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                            this.id,
                            this.name,
                            this.jdbcUrl ,
                            this.username,
                            this.password ,
                            this.className,
                            this. tableName,
                            this.initialQuery,
                            this.checkpointColumn,
                            this.incrementalVariable,
                            this.incrementalQuery,
                            this.mode,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.addToCollection(
                                    this.fields,
                                    fields
                            )
                            ,this.projectName
                            , this.userName
                            , this.headerBuilders
                    );
        }

        @SafeVarargs
        public final com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withHeaders(
                final Supplier<Either<Collection<Field.Error>, Field>>... headerBuilders
        ) {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                            this.id,
                            this.name,
                            this.jdbcUrl ,
                            this.username,
                            this.password ,
                            this.className,
                            this. tableName,
                            this.initialQuery,
                            this.checkpointColumn,
                            this.incrementalVariable,
                            this.incrementalQuery,
                            this.mode,
                            this.fields
                            ,this.projectName
                            , this.userName
                            , com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.addToCollection(
                            this.headerBuilders,
                            headerBuilders
                    )
                    );
        }

        public Either<Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>, com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC> build() {
            return
                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.buildHeaders(
                                    this.fields,
                                    this.headerBuilders
                            )
                            .mapLeft(
                                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.configurationInstanceForErrors(
                                            this.id,
                                            this.name,
                                            this.jdbcUrl ,
                                            this.username,
                                            this.password ,
                                            this.className,
                                            this. tableName,
                                            this.initialQuery,
                                            this.checkpointColumn,
                                            this.incrementalVariable,
                                            this.incrementalQuery,
                                            this.mode
                                             ,this.projectName
                                            ,this.userName
                                    )
                            )
                            .flatMap(
                                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.configurationInstance(
                                            this.id,
                                            this.name,
                                            this.jdbcUrl ,
                                            this.username,
                                            this.password ,
                                            this.className,
                                            this. tableName,
                                            this.initialQuery,
                                            this.checkpointColumn,
                                            this.incrementalVariable,
                                            this.incrementalQuery,
                                            this.mode
                                            , this.projectName
                                            ,this.userName
                                    )
                            );
        }

        public static com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder builder() {
            return
                    new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,null,null,null,null,null,
                            null,
                            null, null
                            ,null
                    );
        }

        private static Function<Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>, Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>> configurationInstanceForErrors(
                final String id,
                final String name,
                final String jdbcUrl ,
                final String username,
                final String password ,
                final String className,
                final String  tableName,
                final String initialQuery,
                final String checkpointColumn,
                final String incrementalVariable,
                final String incrementalQuery,
                final QueryMode mode,
                final String projectName
                , final String userName
        ) {
            return
                    headersErrors ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.addToCollection(
                                    headersErrors,
                                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.of(
                                                    id,
                                                    name,
                                                    jdbcUrl,
                                                    username,
                                                    password ,
                                                    className,
                                                    tableName,
                                                    initialQuery,
                                                    checkpointColumn,
                                                    incrementalVariable,
                                                    incrementalQuery,
                                                    mode,
                                                    null,
                                                    projectName
                                                    ,userName
                                            )
                                            .fold(
                                                    Function.identity(),
                                                    __ -> Collections.emptySet()
                                            )
                            );
        }

        private static Function<Collection<Field>, Either<Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>, com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC>> configurationInstance(
                final String id,
                final String name,
                final String jdbcUrl ,
                final String username,
                final String password ,
                final String className,
                final String  tableName,
                final String initialQuery,
                final String checkpointColumn,
                final String incrementalVariable,
                final String incrementalQuery,
                final QueryMode mode,
                final String projectName
                ,final String userName

        ) {
            return
                    fields ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.of(
                                    id,
                                    name,
                                    jdbcUrl ,
                                    username,
                                    password ,
                                    className,
                                    tableName,initialQuery,checkpointColumn,incrementalVariable,incrementalQuery,
                                    mode
                                    ,fields
                                     ,projectName
                                            ,userName


                            );
        }

        private static Either<Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>, Collection<Field>> buildHeaders(
                final Collection<Field> fields,
                final Collection<Supplier<Either<Collection<Field.Error>, Field>>> headerBuilders
        ) {
            final var errors = com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.buildHeadersForErrors(headerBuilders);
            final Supplier<Collection<Field>> validHeaders =
                    () ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.mergeHeaders(
                                    fields,
                                    headerBuilders
                            );
            return
                    errors.isEmpty()
                            ? Either.right(validHeaders.get())
                            : Either.left(errors);
        }
        private static Either<Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>, Project> buildProject(
                final Project project,
                final Supplier<Either<Collection<Project.Error>, Project>> projectBuilder
        ) {
            final var errors = com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.buildProjectForErrors(projectBuilder);
            final Supplier<Project> validProject =
                    () ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.mergeProject(
                                    project,
                                    projectBuilder
                            );
            return
                    errors.isEmpty()
                            ? Either.right(validProject.get())
                            : Either.left(errors);
        }
        private static Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> buildProjectForErrors(
                final Supplier<Either<Collection<Project.Error>, Project>> projectBuilder
        ) {
            return
                    Objects.nonNull(List.of(projectBuilder))
                            ?
                            List.of(projectBuilder).stream()
                                    .map(Supplier::get)
                                    .filter(Either::isLeft)
                                    .map(Either::getLeft)
                                    .flatMap(Collection::stream)
                                    .map(com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder::projectErrorToConfigurationError)
                                    .collect(Collectors.toUnmodifiableSet())
                            :
                            Collections.emptySet();

        }

        private static Collection<Field> mergeHeaders(
                final Collection<Field> fields,
                final Collection<Supplier<Either<Collection<Field.Error>, Field>>> headerBuilders
        ) {
            return
                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.addToCollection(
                            fields,
                            Objects.nonNull(headerBuilders)
                                    ?
                                    headerBuilders.stream()
                                            .map(Supplier::get)
                                            .filter(Either::isRight)
                                            .map(Either::get)
                                            .collect(Collectors.toUnmodifiableSet())
                                    :
                                    Collections.emptySet()
                    );
        }
        private static Project mergeProject(
                final Project project,
                final Supplier <Either <Collection<Project.Error>,Project>> projectBuilder
        )
        {   //return projectBuilder.get().getOrElse(new Project("default","default","defaukt" ));

            return

                    Objects.nonNull(projectBuilder)
                            ?
                            projectBuilder.get().get()

                            :
                            null;



        }

        private static Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> buildHeadersForErrors(
                final Collection<Supplier<Either<Collection<Field.Error>, Field>>> headerBuilders
        ) {
            return
                    Objects.nonNull(headerBuilders)
                            ?
                            headerBuilders.stream()
                                    .map(Supplier::get)
                                    .filter(Either::isLeft)
                                    .map(Either::getLeft)
                                    .flatMap(Collection::stream)
                                    .map(com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder::errorToConfigurationError)
                                    .collect(Collectors.toUnmodifiableSet())
                            :
                            Collections.emptySet();
        }

        private static com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error errorToConfigurationError(
                final Field.Error headerError
        ) {
            if (headerError instanceof Field.Error.IdMalformed)
                return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.Header.IdMalformed();
            if (headerError instanceof Field.Error.ReferenceConnectorMalformed)
                return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.Header.ReferenceConnectorMalformed();
            if (headerError instanceof Field.Error.NameMalformed)
                return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.Header.NameMalformed();
            if (headerError instanceof Field.Error.PositionMalformed)
                return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.Header.PositionMalformed();
            if (headerError instanceof Field.Error.MetaMalformed)
                return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.Header.MetaMalformed();
            if (headerError instanceof Field.Error.NullOrEmptyRestrictionCombination)
                return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.Header.NullOrEmptyRestrictionCombination();
            throw new IllegalStateException("header error not mapped to connector error");
        }
        private static com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error projectErrorToConfigurationError(
                final Project.Error projectError
        ) {
            if (projectError instanceof Project.Error.IdMalformed)
                return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.Header.IdMalformed();

            if (projectError instanceof Project.Error.NameMalformed)
                return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.Header.NameMalformed();
            throw new IllegalStateException("header error not mapped to connector error");
        }

        private static <FIELD> FIELD nonNullOrDefault(
                final FIELD field,
                final FIELD defaultValue
        ) {
            return
                    Objects.nonNull(field)
                            ? field
                            : defaultValue;
        }

        private static <TYPE> Collection<TYPE> addToCollection(
                final Collection<TYPE> initialCollection,
                final Collection<TYPE> elements
        ) {
            return
                    Stream.of(
                                    initialCollection,
                                    elements
                            )
                            .filter(Objects::nonNull)
                            .flatMap(Collection::stream)
                            .toList();
        }

        @SafeVarargs
        private static <TYPE> Collection<TYPE> addToCollection(
                final Collection<TYPE> initialCollection,
                final TYPE... elements
        ) {
            return
                    Objects.nonNull(initialCollection)
                            ?
                            Stream.of(
                                            initialCollection.stream(),
                                            Arrays.stream(elements)
                                    )
                                    .flatMap(Function.identity())
                                    .toList()
                            :
                            Arrays.stream(elements)
                                    .toList();
        }
    }

    public sealed interface Error {

        default String message() {
            return this.getClass().getCanonicalName();
        }

        final class IdMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
            private IdMalformed() {
            }
        }

        final class NameMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
            private NameMalformed() {
            }
        }

        final class jdbcUrlMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
            private jdbcUrlMalformed() {
            }
        }

        final class usernameMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
            private usernameMalformed() {
            }
        }

        final class passwordMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
            private passwordMalformed() {
            }
        }

        final class classNameMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
            private classNameMalformed() {
            }
        }

        final class  tableNameMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
            private  tableNameMalformed() {
            }
        }

        final class HeaderMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
            private HeaderMalformed() {
            }
        }

        final class IdHeaderMissing implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
            private IdHeaderMissing() {
            }
        }

        final class HeadersSequenceMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
            private HeadersSequenceMalformed() {
            }
        }

        sealed interface Header extends com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {

            final class IdMalformed implements Header {
                private IdMalformed() {
                }
            }

            final class ReferenceConnectorMalformed implements Header {
                private ReferenceConnectorMalformed() {
                }
            }

            final class NameMalformed implements Header {
                private NameMalformed() {
                }
            }

            final class PositionMalformed implements Header {
                private PositionMalformed() {
                }
            }

            final class MetaMalformed implements Header {
                private MetaMalformed() {
                }
            }

            final class NullOrEmptyRestrictionCombination implements Header {
                private NullOrEmptyRestrictionCombination() {
                }
            }
        }
    }
}







/*
package com.keyrus.proxemconnector.connector.csv.configuration.model;

import io.vavr.control.Either;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConnectorJDBC extends Connector{


        private final String separator;
        private final String encoding;
        private final String path;
        private final String quotingCaracter;
        private final String escapingCaracter;
        private final boolean containsHeaders;



        private ConnectorJDBC(
                final String id,
                final String name,
                final String separator,
                final String encoding,
                final String path,
                final String quotingCaracter,
                final String escapingCaracter,
                final boolean containsHeaders,
                final Collection<Field> fields
                , final Project project
        ) {
            super(id,name,fields
                    , project
            );
            this.separator = separator;
            this.encoding = encoding;
            this.path = path;
            this.quotingCaracter = quotingCaracter;
            this.escapingCaracter = escapingCaracter;
            this.containsHeaders = containsHeaders;

        }

        private ConnectorJDBC(
                final String id,
                final String name,
                final String path,
                final String quotingCaracter,
                final String escapingCaracter,
                final boolean containsHeaders,
                final Collection<Field> fields
                , final Project project
        ) {
            this(
                    id,
                    name,
                    ",",
                    StandardCharsets.UTF_8.name(),
                    path,
                    quotingCaracter,
                    escapingCaracter,
                    containsHeaders,fields
                    , project
            );
        }

        private ConnectorJDBC(
                final String id,
                final String name,
                final String separator,
                final String path,
                final String quotingCaracter,
                final String escapingCaracter,
                final boolean containsHeaders,
                final Collection<Field> fields
                , final Project project
        ) {
            this(
                    id,
                    name,
                    separator,
                    StandardCharsets.UTF_8.name(),
                    path,
                    quotingCaracter,
                    escapingCaracter,
                    containsHeaders,
                    fields
                    , project
            );
        }



    public String name() {
            return this.name;
        }

        public String id() {
            return this.id;
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

        public Collection<Field> fields() {
            return this.fields;
        }
        public Project project() {
            return this.project;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC that = (com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC) o;

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
                            Configuration[
                                id=%s,
                                name=%s,
                                separator=%s,
                                encoding=%s,
                                path=%s,
                                quotingCaracter=%s,
                                escapingCaracter=%s,
                                containsHeaders=%s,
                                fields=%s,
                                project=%s
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
                                    ,this.project
                            );
        }

        private static Either<Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>, com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC> of(
                final String id,
                final String name,
                final String separator,
                final String encoding,
                final String path,
                final String quotingCaracter,
                final String escapingCaracter,
                final boolean containsHeaders,
                final Collection<Field> fields
                ,final Project project
        ) {
            return
                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkThenInstantiate(
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.instance(
                                    id,
                                    name,
                                    separator,
                                    encoding,
                                    path,
                                    quotingCaracter,
                                    escapingCaracter,
                                    containsHeaders,
                                    fields
                                    ,project
                            ),
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkId(
                                    id
                            ), com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkName(
                                    name
                            ),
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkSeparator(
                                    separator
                            ),
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkEncoding(
                                    encoding
                            ),
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkpath(
                                    path
                            ),
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkquotingCaracter(
                                    quotingCaracter
                            ),
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkescapingCaracter(
                                    escapingCaracter
                            ),
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkHeaders(
                                    fields
                            )
                    );
        }

        private static Supplier<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC> instance(
                final String id,
                final String name,
                final String separator,
                final String encoding,
                final String path,
                final String quotingCaracter,
                final String escapingCaracter,
                final boolean containsHeaders,
                final Collection<Field> fields
                , final Project project
        ) {
            if (
                    Objects.isNull(separator) &&
                            Objects.isNull(encoding)
            )
                return
                        () ->
                                new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC(
                                        id,
                                        name,
                                        path,
                                        quotingCaracter,
                                        escapingCaracter,
                                        containsHeaders,
                                        fields
                                        , project
                                );
            if (Objects.isNull(encoding))
                return
                        () ->
                                new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC(
                                        id,
                                        name,
                                        separator,
                                        path,
                                        quotingCaracter,
                                        escapingCaracter,
                                        containsHeaders,
                                        fields
                                        ,project
                                );
            return

                    () ->
                            new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC(
                                    id,
                                    name,
                                    separator,
                                    encoding,
                                    path,
                                    quotingCaracter,
                                    escapingCaracter,
                                    containsHeaders,
                                    fields
                                    ,project
                            );
        }

        private static Supplier<Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>> checkId(
                final String id
        ) {
            return
                    () ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNonNullableNonBlankStringField(
                                    id,
                                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.IdMalformed::new
                            );
        }

        private static Supplier<Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>> checkName(
                final String name
        ) {
            return
                    () ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNonNullableNonBlankStringField(
                                    name,
                                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.NameMalformed::new
                            );
        }

        private static Supplier<Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>> checkSeparator(
                final String separator
        ) {
            return
                    () ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNullableNonBlankStringField(
                                    separator,
                                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.SeparatorMalformed::new,
                                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC::checkSeparatorIsNotComposedOfLettersOrDigits
                            );
        }

        private static Supplier<Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>> checkEncoding(
                final String encoding
        ) {
            return
                    () ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNullableNonBlankStringField(
                                    encoding,
                                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.EncodingMalformed::new,
                                    Charset::isSupported
                            );
        }

        private static Supplier<Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>> checkpath(
                final String path
        ) {
            return
                    () ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNonNullableNonBlankStringField(
                                    path,
                                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.pathMalformed::new
                            );
        }

        private static Supplier<Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>> checkquotingCaracter(
                final String quotingCaracter
        ) {
            return
                    () ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNonNullableNonBlankStringField(
                                    quotingCaracter,
                                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.quotingCaracterMalformed::new
                            );
        }

        private static Supplier<Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>> checkescapingCaracter(
                final String escapingCaracter
        ) {
            return
                    () ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNonNullableNonBlankStringField(
                                    escapingCaracter,
                                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.escapingCaracterMalformed::new
                            );
        }

        private static Supplier<Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>> checkHeaders(
                final Collection<Field> fields
        ) {
            return
                    () ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkHeaderFormat(fields)
                                    .or(() ->
                                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkHeadersIds(fields)
                                    )
                                    .or( () ->
                                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkHeadersPositions(fields)
                                    );
        }

        private static Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> checkHeadersPositions(
                final Collection<Field> fields
        ) {
            return
                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkField(
                            fields,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.HeadersSequenceMalformed::new,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC::checkHeadersPositionSequence
                    );
        }

        private static Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> checkHeadersIds(
                final Collection<Field> fields
        ) {
            return
                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkField(
                            fields,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.IdHeaderMissing::new,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC::checkHeadersContainsIds
                    );
        }

        private static Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> checkHeaderFormat(
                final Collection<Field> fields
        ) {
            return
                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNonNullableField(
                            fields,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.HeaderMalformed::new
                    );
        }

        private static boolean checkHeadersContainsIds(
                final Collection<Field> fields
        ) {
            return
                    Objects.nonNull(fields) &&
                            fields.stream()
                                    .anyMatch(Field::partOfDocumentIdentity);
        }

        private static boolean checkHeadersPositionSequence(
                final Collection<Field> fields
        ) {
            return
                    Objects.nonNull(fields) &&
                            fields.stream()
                                    .map(Field::position)
                                    .max(Comparator.naturalOrder())
                                    .map(max -> fields.size() == max)
                                    .orElse(false);
        }

        private static boolean checkSeparatorIsNotComposedOfLettersOrDigits(
                final String separator
        ) {
            return
                    separator.chars()
                            .noneMatch(
                                    ((IntPredicate) Character::isDigit)
                                            .or(Character::isAlphabetic)
                            );
        }

        @SafeVarargs
        private static Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> checkNonNullableNonBlankStringField(
                final String field,
                final Supplier<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> errorIfInvalid,
                final Predicate<String>... extraChecks
        ) {
            return
                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNonNullableField(
                            field,
                            errorIfInvalid,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.mergePredicates(
                                    Predicate.not(String::isBlank),
                                    extraChecks
                            )
                    );
        }

        @SafeVarargs
        private static Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> checkNullableNonBlankStringField(
                final String field,
                final Supplier<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> errorIfInvalid,
                final Predicate<String>... extraChecks
        ) {
            return
                    Objects.nonNull(field)
                            ?
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkNonNullableField(
                                    field,
                                    errorIfInvalid,
                                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.mergePredicates(
                                            Predicate.not(String::isBlank),
                                            extraChecks
                                    )
                            )
                            :
                            Optional.empty();
        }

        @SafeVarargs
        private static <FIELD> Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> checkNonNullableField(
                final FIELD field,
                final Supplier<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> errorIfInvalid,
                final Predicate<FIELD>... extraChecks
        ) {
            return
                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.checkField(
                            field,
                            errorIfInvalid,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.mergePredicates(
                                    Objects::nonNull,
                                    extraChecks
                            )
                    );
        }

        @SafeVarargs
        private static <FIELD> Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> checkField(
                final FIELD field,
                final Supplier<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> errorIfInvalid,
                final Predicate<FIELD>... validityChecks
        ) {
            return
                    Arrays.stream(validityChecks)
                            .anyMatch(Predicate.not(it -> it.test(field)))
                            ? Optional.ofNullable(errorIfInvalid.get())
                            : Optional.empty();
        }

        @SafeVarargs
        private static Either<Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>, com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC> checkThenInstantiate(
                final Supplier<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC> instance,
                final Supplier<Optional<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>>... checks
        ) {
            final var errors =
                    Arrays.stream(checks)
                            .map(Supplier::get)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toUnmodifiableSet());
            return
                    errors.isEmpty()
                            ? Either.right(instance.get())
                            : Either.left(errors);
        }

        @SafeVarargs
        private static <TYPE> Predicate<TYPE> mergePredicates(
                final Predicate<TYPE> predicate,
                final Predicate<TYPE>... predicates
        ) {
            return
                    Stream.of(
                                    Stream.of(predicate),
                                    Arrays.stream(predicates)
                            )
                            .flatMap(Function.identity())
                            .filter(Objects::nonNull)
                            .reduce(
                                    it -> true,
                                    Predicate::and
                            );
        }

        public static final class Builder {
            private final String id;
            private final String name;
            private final String separator;
            private final String encoding;
            private final String path;
            private final String quotingCaracter;
            private final String escapingCaracter;
            private final boolean containsHeaders;
            private final Collection<Field> fields;
            private final Project project;
            private final Supplier<Either<Collection<Project.Error>, Project>> projectBuilder;
            private final Collection<Supplier<Either<Collection<Field.Error>, Field>>> headerBuilders;

            private Builder(
                    final String id,
                    final String name,
                    final String separator,
                    final String encoding,
                    final String path,
                    final String quotingCaracter,
                    final String escapingCaracter,
                    final boolean containsHeaders,
                    final Collection<Field> fields,
                    final Project project,
                    Supplier<Either<Collection<Project.Error>, Project>> projectBuilder,
                    final Collection<Supplier<Either<Collection<Field.Error>, Field>>> headerBuilders
            ) {
                this.id = id;
                this.name = name;
                this.separator = separator;
                this.encoding = encoding;
                this.path = path;
                this.quotingCaracter = quotingCaracter;
                this.escapingCaracter = escapingCaracter;
                this.containsHeaders = containsHeaders;
                this.fields = fields;
                this.project = project;
                this.projectBuilder = projectBuilder;
                this.headerBuilders = headerBuilders;
            }

            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withId(
                    final String id
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                        id,
                                        this.id
                                ),
                                this.name,
                                this.separator,
                                this.encoding,
                                this.path,
                                this.quotingCaracter,
                                this.escapingCaracter,
                                this.containsHeaders,
                                this.fields
                                , this.project,
                                projectBuilder
                                , this.headerBuilders
                        );
            }
            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withProject(
                    final Project project
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(this.id,
                                this.name,
                                this.separator,
                                this.encoding,
                                this.path,
                                this.quotingCaracter,
                                this.escapingCaracter,
                                this.containsHeaders,
                                this.fields,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(project,this.project),
                                projectBuilder, this.headerBuilders

                        );
            }

            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withName(
                    final String name
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                                this.id,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                        name,
                                        this.name
                                ),
                                this.separator,
                                this.encoding,
                                this.path,
                                this.quotingCaracter,
                                this.escapingCaracter,
                                this.containsHeaders,
                                this.fields
                                ,this.project
                                ,projectBuilder
                                , this.headerBuilders
                        );
            }

            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withSeparator(
                    final String separator
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                                this.id,
                                this.name,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                        separator,
                                        this.separator
                                ),
                                this.encoding,
                                this.path,
                                this.quotingCaracter,
                                this.escapingCaracter,
                                this.containsHeaders,
                                this.fields,
                                this.project,
                                projectBuilder,
                                this.headerBuilders
                        );
            }

            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withEncoding(
                    final String encoding
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                                this.id,
                                this.name,
                                this.separator,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                        encoding,
                                        this.encoding
                                ),
                                this.path,
                                this.quotingCaracter,
                                this.escapingCaracter,
                                this.containsHeaders,
                                this.fields
                                , this.project
                                , projectBuilder
                                , this.headerBuilders
                        );
            }

            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withpath(
                    final String path
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                                this.id,
                                this.name,
                                this.separator,
                                this.encoding,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                        path,
                                        this.path
                                ),
                                this.quotingCaracter,
                                this.escapingCaracter,
                                this.containsHeaders,
                                this.fields,
                                this.project,
                                projectBuilder,
                                this.headerBuilders
                        );
            }

            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withquotingCaracter(
                    final String quotingCaracter
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                                this.id,
                                this.name,
                                this.separator,
                                this.encoding,
                                this.path,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                        quotingCaracter,
                                        this.quotingCaracter
                                ),
                                this.escapingCaracter,
                                this.containsHeaders,
                                this.fields
                                ,this.project
                                ,  projectBuilder
                                , this.headerBuilders
                        );
            }

            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withescapingCaracter(
                    final String escapingCaracter
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                                this.id,
                                this.name,
                                this.separator,
                                this.encoding,
                                this.path,
                                this.quotingCaracter,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.nonNullOrDefault(
                                        escapingCaracter,
                                        this.escapingCaracter
                                ),
                                this.containsHeaders,
                                this.fields
                                , this.project
                                ,projectBuilder
                                , this.headerBuilders
                        );
            }

            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withContainsHeaders(
                    final boolean containsHeaders
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                                this.id,
                                this.name,
                                this.separator,
                                this.encoding,
                                this.path,
                                this.quotingCaracter,
                                this.escapingCaracter,
                                containsHeaders,
                                this.fields
                                ,this.project
                                , projectBuilder
                                , this.headerBuilders
                        );
            }

            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withHeaders(
                    final Collection<Field> fields
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                                this.id,
                                this.name,
                                this.separator,
                                this.encoding,
                                this.path,
                                this.quotingCaracter,
                                this.escapingCaracter,
                                this.containsHeaders,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.addToCollection(
                                        this.fields,
                                        fields
                                )
                                ,this.project
                                ,projectBuilder
                                , this.headerBuilders
                        );
            }

            @SafeVarargs
            public final com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder withHeaders(
                    final Supplier<Either<Collection<Field.Error>, Field>>... headerBuilders
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                                this.id,
                                this.name,
                                this.separator,
                                this.encoding,
                                this.path,
                                this.quotingCaracter,
                                this.escapingCaracter,
                                this.containsHeaders,
                                this.fields

                                ,this.project
                                ,projectBuilder
                                , com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.addToCollection(
                                this.headerBuilders,
                                headerBuilders
                        )
                        );
            }

            public Either<Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>, com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC> build() {
                return
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.buildHeaders(
                                        this.fields,
                                        this.headerBuilders
                                )
                                .mapLeft(
                                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.configurationInstanceForErrors(
                                                this.id,
                                                this.name,
                                                this.separator,
                                                this.encoding,
                                                this.path,
                                                this.quotingCaracter,
                                                this.escapingCaracter,
                                                this.containsHeaders
                                                // ,this.project
                                        )
                                )
                                .flatMap(
                                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.configurationInstance(
                                                this.id,
                                                this.name,
                                                this.separator,
                                                this.encoding,
                                                this.path,
                                                this.quotingCaracter,
                                                this.escapingCaracter,
                                                this.containsHeaders
                                                , this.project
                                        )
                                );
            }

            public static com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder builder() {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                true,
                                null,
                                null,null,null
                        );
            }

            private static Function<Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>, Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>> configurationInstanceForErrors(
                    final String id,
                    final String name,
                    final String separator,
                    final String encoding,
                    final String path,
                    final String quotingCaracter,
                    final String escapingCaracter,
                    final boolean containsHeaders
                    // final Project project
            ) {
                return
                        headersErrors ->
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.addToCollection(
                                        headersErrors,
                                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.of(
                                                        id,
                                                        name,
                                                        separator,
                                                        encoding,
                                                        path,
                                                        quotingCaracter,
                                                        escapingCaracter,
                                                        containsHeaders,
                                                        null,
                                                        null
                                                )
                                                .fold(
                                                        Function.identity(),
                                                        __ -> Collections.emptySet()
                                                )
                                );
            }

            private static Function<Collection<Field>, Either<Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>, com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC>> configurationInstance(
                    final String id,
                    final String name,
                    final String separator,
                    final String encoding,
                    final String path,
                    final String quotingCaracter,
                    final String escapingCaracter,
                    final boolean containsHeaders,
                    final Project project

            ) {
                return
                        fields ->
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.of(
                                        id,
                                        name,
                                        separator,
                                        encoding,
                                        path,
                                        quotingCaracter,
                                        escapingCaracter,
                                        containsHeaders
                                        ,fields
                                        ,project


                                );
            }

            private static Either<Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>, Collection<Field>> buildHeaders(
                    final Collection<Field> fields,
                    final Collection<Supplier<Either<Collection<Field.Error>, Field>>> headerBuilders
            ) {
                final var errors = com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.buildHeadersForErrors(headerBuilders);
                final Supplier<Collection<Field>> validHeaders =
                        () ->
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.mergeHeaders(
                                        fields,
                                        headerBuilders
                                );
                return
                        errors.isEmpty()
                                ? Either.right(validHeaders.get())
                                : Either.left(errors);
            }
            private static Either<Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error>, Project> buildProject(
                    final Project project,
                    final Supplier<Either<Collection<Project.Error>, Project>> projectBuilder
            ) {
                final var errors = com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.buildProjectForErrors(projectBuilder);
                final Supplier<Project> validProject =
                        () ->
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.mergeProject(
                                        project,
                                        projectBuilder
                                );
                return
                        errors.isEmpty()
                                ? Either.right(validProject.get())
                                : Either.left(errors);
            }
            private static Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> buildProjectForErrors(
                    final Supplier<Either<Collection<Project.Error>, Project>> projectBuilder
            ) {
                return
                        Objects.nonNull(List.of(projectBuilder))
                                ?
                                List.of(projectBuilder).stream()
                                        .map(Supplier::get)
                                        .filter(Either::isLeft)
                                        .map(Either::getLeft)
                                        .flatMap(Collection::stream)
                                        .map(com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder::projectErrorToConfigurationError)
                                        .collect(Collectors.toUnmodifiableSet())
                                :
                                Collections.emptySet();

            }

            private static Collection<Field> mergeHeaders(
                    final Collection<Field> fields,
                    final Collection<Supplier<Either<Collection<Field.Error>, Field>>> headerBuilders
            ) {
                return
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder.addToCollection(
                                fields,
                                Objects.nonNull(headerBuilders)
                                        ?
                                        headerBuilders.stream()
                                                .map(Supplier::get)
                                                .filter(Either::isRight)
                                                .map(Either::get)
                                                .collect(Collectors.toUnmodifiableSet())
                                        :
                                        Collections.emptySet()
                        );
            }
            private static Project mergeProject(
                    final Project project,
                    final Supplier <Either <Collection<Project.Error>,Project>> projectBuilder
            )
            {   //return projectBuilder.get().getOrElse(new Project("default","default","defaukt" ));

                return

                        Objects.nonNull(projectBuilder)
                                ?
                                projectBuilder.get().get()

                                :
                                null;



            }

            private static Collection<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error> buildHeadersForErrors(
                    final Collection<Supplier<Either<Collection<Field.Error>, Field>>> headerBuilders
            ) {
                return
                        Objects.nonNull(headerBuilders)
                                ?
                                headerBuilders.stream()
                                        .map(Supplier::get)
                                        .filter(Either::isLeft)
                                        .map(Either::getLeft)
                                        .flatMap(Collection::stream)
                                        .map(com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Builder::errorToConfigurationError)
                                        .collect(Collectors.toUnmodifiableSet())
                                :
                                Collections.emptySet();
            }

            private static com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error errorToConfigurationError(
                    final Field.Error headerError
            ) {
                if (headerError instanceof Field.Error.IdMalformed)
                    return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.Header.IdMalformed();
                if (headerError instanceof Field.Error.ReferenceConnectorMalformed)
                    return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.Header.ReferenceConnectorMalformed();
                if (headerError instanceof Field.Error.NameMalformed)
                    return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.Header.NameMalformed();
                if (headerError instanceof Field.Error.PositionMalformed)
                    return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.Header.PositionMalformed();
                if (headerError instanceof Field.Error.MetaMalformed)
                    return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.Header.MetaMalformed();
                if (headerError instanceof Field.Error.NullOrEmptyRestrictionCombination)
                    return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.Header.NullOrEmptyRestrictionCombination();
                throw new IllegalStateException("header error not mapped to connector error");
            }
            private static com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error projectErrorToConfigurationError(
                    final Project.Error projectError
            ) {
                if (projectError instanceof Project.Error.IdMalformed)
                    return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.Header.IdMalformed();

                if (projectError instanceof Project.Error.NameMalformed)
                    return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error.Header.NameMalformed();
                throw new IllegalStateException("header error not mapped to connector error");
            }

            private static <FIELD> FIELD nonNullOrDefault(
                    final FIELD field,
                    final FIELD defaultValue
            ) {
                return
                        Objects.nonNull(field)
                                ? field
                                : defaultValue;
            }

            private static <TYPE> Collection<TYPE> addToCollection(
                    final Collection<TYPE> initialCollection,
                    final Collection<TYPE> elements
            ) {
                return
                        Stream.of(
                                        initialCollection,
                                        elements
                                )
                                .filter(Objects::nonNull)
                                .flatMap(Collection::stream)
                                .toList();
            }

            @SafeVarargs
            private static <TYPE> Collection<TYPE> addToCollection(
                    final Collection<TYPE> initialCollection,
                    final TYPE... elements
            ) {
                return
                        Objects.nonNull(initialCollection)
                                ?
                                Stream.of(
                                                initialCollection.stream(),
                                                Arrays.stream(elements)
                                        )
                                        .flatMap(Function.identity())
                                        .toList()
                                :
                                Arrays.stream(elements)
                                        .toList();
            }
        }

        public sealed interface Error {

            default String message() {
                return this.getClass().getCanonicalName();
            }

            final class IdMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
                private IdMalformed() {
                }
            }

            final class NameMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
                private NameMalformed() {
                }
            }

            final class SeparatorMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
                private SeparatorMalformed() {
                }
            }

            final class EncodingMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
                private EncodingMalformed() {
                }
            }

            final class pathMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
                private pathMalformed() {
                }
            }

            final class quotingCaracterMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
                private quotingCaracterMalformed() {
                }
            }

            final class escapingCaracterMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
                private escapingCaracterMalformed() {
                }
            }

            final class HeaderMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
                private HeaderMalformed() {
                }
            }

            final class IdHeaderMissing implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
                private IdHeaderMissing() {
                }
            }

            final class HeadersSequenceMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {
                private HeadersSequenceMalformed() {
                }
            }

            sealed interface Header extends com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC.Error {

                final class IdMalformed implements Header {
                    private IdMalformed() {
                    }
                }

                final class ReferenceConnectorMalformed implements Header {
                    private ReferenceConnectorMalformed() {
                    }
                }

                final class NameMalformed implements Header {
                    private NameMalformed() {
                    }
                }

                final class PositionMalformed implements Header {
                    private PositionMalformed() {
                    }
                }

                final class MetaMalformed implements Header {
                    private MetaMalformed() {
                    }
                }

                final class NullOrEmptyRestrictionCombination implements Header {
                    private NullOrEmptyRestrictionCombination() {
                    }
                }
            }
        }
    }


*/