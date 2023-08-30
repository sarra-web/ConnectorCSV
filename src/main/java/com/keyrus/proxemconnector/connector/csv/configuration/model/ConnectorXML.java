package com.keyrus.proxemconnector.connector.csv.configuration.model;

import io.vavr.control.Either;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConnectorXML extends Connector{


        private final String path;
        private final String tagName;



        private ConnectorXML(
                final String id,
                final String name,
                final String path,
                final String tagName,
                final Collection<Field> fields,
                final String projectName
                , final String userName
        ) {
            super(id, name, fields
                    , projectName
                    , userName
            );

            this.path = path;
            this.tagName = tagName;


        }


        public String name() {
            return this.name;
        }

        public String id() {
            return this.id;
        }
        public String path() {
            return this.path;
        }
        public String tagName() {
        return this.tagName;
    }

        public Collection<Field> fields() {
            return this.fields;
        }

        public String projectName() {
            return this.projectName;
        }

        public String userName() {
            return this.userName;
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ConnectorXML that = (ConnectorXML) o;

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

    @Override
    public String toString() {
        return "ConnectorXML{" +
                "path='" + path + '\'' +
                ", tagName='" + tagName + '\'' +
                '}';
    }

    private static Either<Collection<Connector.Error>, com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML> of(
                final String id,
                final String name,
                final String path,
                final String tagName,
                final Collection<Field> fields
                , final String projectName
                , final String userName
        ) {
            return
                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkThenInstantiate(
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.instance(
                                    id,
                                    name,
                                    path,
                                    tagName,

                                    fields
                                    , projectName
                                    , userName
                            ),
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkId(
                                    id
                            ), com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkName(
                                    name
                            ),

                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkpath(
                                    path
                            ),
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checktagName(
                                    tagName
                            )

                    );
        }

        private static Supplier<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML> instance(
                final String id,
                final String name,
                final String path,
                final String tagName,

                final Collection<Field> fields
                , final String projectName
                , final String userName
        ) {

            return

                    () ->
                            new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML(
                                    id,
                                    name,
                                    path,
                                    tagName,

                                    fields
                                    , projectName
                                    , userName
                            );
        }

        private static Supplier<Optional<Connector.Error>> checkId(
                final String id
        ) {
            return
                    () ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkNonNullableNonBlankStringField(
                                    id,
                                    Connector.Error.IdMalformed::new
                            );
        }

        private static Supplier<Optional<Connector.Error>> checkName(
                final String name
        ) {
            return
                    () ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkNonNullableNonBlankStringField(
                                    name,
                                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error.NameMalformed::new
                            );
        }


        private static Supplier<Optional<Connector.Error>> checkpath(
                final String path
        ) {
            return
                    () ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkNonNullableNonBlankStringField(
                                    path,
                                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error.pathMalformed::new
                            );
        }

        private static Supplier<Optional<Connector.Error>> checktagName(
                final String tagName
        ) {
            return
                    () ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkNonNullableNonBlankStringField(
                                    tagName,
                                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error.quotingCaracterMalformed::new
                            );
        }

        private static Supplier<Optional<Connector.Error>> checkescapingCaracter(
                final String escapingCaracter
        ) {
            return
                    () ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkNonNullableNonBlankStringField(
                                    escapingCaracter,
                                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error.escapingCaracterMalformed::new
                            );
        }

        private static Supplier<Optional<Connector.Error>> checkHeaders(
                final Collection<Field> fields
        ) {
            return
                    () ->
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkHeaderFormat(fields)
                                    .or(() ->
                                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkHeadersIds(fields)
                                    )
                                    .or(() ->
                                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkHeadersPositions(fields)
                                    );
        }

        private static Optional<Connector.Error> checkHeadersPositions(
                final Collection<Field> fields
        ) {
            return
                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkField(
                            fields,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error.HeadersSequenceMalformed::new,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML::checkHeadersPositionSequence
                    );
        }

        private static Optional<Connector.Error> checkHeadersIds(
                final Collection<Field> fields
        ) {
            return
                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkField(
                            fields,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error.IdHeaderMissing::new,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML::checkHeadersContainsIds
                    );
        }

        private static Optional<Connector.Error> checkHeaderFormat(
                final Collection<Field> fields
        ) {
            return
                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkNonNullableField(
                            fields,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error.HeaderMalformed::new
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
        private static Optional<Connector.Error> checkNonNullableNonBlankStringField(
                final String field,
                final Supplier<Connector.Error> errorIfInvalid,
                final Predicate<String>... extraChecks
        ) {
            return
                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkNonNullableField(
                            field,
                            errorIfInvalid,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.mergePredicates(
                                    Predicate.not(String::isBlank),
                                    extraChecks
                            )
                    );
        }

        @SafeVarargs
        private static Optional<Connector.Error> checkNullableNonBlankStringField(
                final String field,
                final Supplier<Connector.Error> errorIfInvalid,
                final Predicate<String>... extraChecks
        ) {
            return
                    Objects.nonNull(field)
                            ?
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkNonNullableField(
                                    field,
                                    errorIfInvalid,
                                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.mergePredicates(
                                            Predicate.not(String::isBlank),
                                            extraChecks
                                    )
                            )
                            :
                            Optional.empty();
        }

        @SafeVarargs
        private static <FIELD> Optional<Connector.Error> checkNonNullableField(
                final FIELD field,
                final Supplier<Connector.Error> errorIfInvalid,
                final Predicate<FIELD>... extraChecks
        ) {
            return
                    com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.checkField(
                            field,
                            errorIfInvalid,
                            com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.mergePredicates(
                                    Objects::nonNull,
                                    extraChecks
                            )
                    );
        }

        @SafeVarargs
        private static <FIELD> Optional<Connector.Error> checkField(
                final FIELD field,
                final Supplier<Connector.Error> errorIfInvalid,
                final Predicate<FIELD>... validityChecks
        ) {
            return
                    Arrays.stream(validityChecks)
                            .anyMatch(Predicate.not(it -> it.test(field)))
                            ? Optional.ofNullable(errorIfInvalid.get())
                            : Optional.empty();
        }

        @SafeVarargs
        private static Either<Collection<Connector.Error>, com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML> checkThenInstantiate(
                final Supplier<com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML> instance,
                final Supplier<Optional<Connector.Error>>... checks
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
            private final String path;
            private final String tagName;
            private final Collection<Field> fields;
            private final String projectName;
            private final String userName;
            private final Collection<Supplier<Either<Collection<Field.Error>, Field>>> headerBuilders;

            private Builder(
                    final String id,
                    final String name,

                    final String path,
                    final String tagName,
                    final Collection<Field> fields,
                    final String projectName,
                    final String userName,
                    final Collection<Supplier<Either<Collection<Field.Error>, Field>>> headerBuilders
            ) {
                this.id = id;
                this.name = name;
                this.path = path;
                this.tagName = tagName;
                this.fields = fields;
                this.projectName = projectName;
                this.userName = userName;
                this.headerBuilders = headerBuilders;
            }

            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder withId(
                    final String id
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder(
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder.nonNullOrDefault(
                                        id,
                                        this.id
                                ),
                                this.name,
                                this.path,
                                this.tagName,
                                this.fields
                                , this.projectName
                                , this.userName

                                , this.headerBuilders
                        );
            }

            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder withProjectName(
                    final String projectName
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder(this.id,
                                this.name,
                                this.path,
                                this.tagName,
                                this.fields,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder.nonNullOrDefault(projectName, this.projectName)
                                , this.userName
                                , this.headerBuilders

                        );
            }

            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder withName(
                    final String name
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder(
                                this.id,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder.nonNullOrDefault(
                                        name,
                                        this.name
                                ),
                                this.path,
                                this.tagName,
                                this.fields
                                , this.projectName
                                , this.userName
                                , this.headerBuilders
                        );
            }



            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder withpath(
                    final String path
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder(
                                this.id,
                                this.name,

                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder.nonNullOrDefault(
                                        path,
                                        this.path
                                ),
                                this.tagName,

                                this.fields,
                                this.projectName
                                , this.userName,
                                this.headerBuilders
                        );
            }

            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder withtagName(
                    final String tagName
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder(
                                this.id,
                                this.name,

                                this.path,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder.nonNullOrDefault(
                                        tagName,
                                        this.tagName
                                ),
                                this.fields
                                , this.projectName
                                , this.userName
                                , this.headerBuilders
                        );
            }

            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder withUserName(
                    final String userName
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder(
                                this.id,
                                this.name,
                                this.path,
                                this.tagName,
                                this.fields
                                , this.projectName, com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder.nonNullOrDefault(
                                userName
                                , this.userName
                        )
                                , this.headerBuilders
                        );
            }

            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder withContainsHeaders(
                    final boolean containsHeaders
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder(
                                this.id,
                                this.name,

                                this.path,
                                this.tagName,
                                this.fields
                                , this.projectName
                                , this.userName
                                , this.headerBuilders
                        );
            }

            public com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder withHeaders(
                    final Collection<Field> fields
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder(
                                this.id,
                                this.name,

                                this.path,
                                this.tagName,
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder.addToCollection(
                                        this.fields,
                                        fields
                                )
                                , this.projectName
                                , this.userName
                                , this.headerBuilders
                        );
            }

            @SafeVarargs
            public final com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder withHeaders(
                    final Supplier<Either<Collection<Field.Error>, Field>>... headerBuilders
            ) {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder(
                                this.id,
                                this.name,

                                this.path,
                                this.tagName,

                                this.fields

                                , this.projectName
                                , this.userName
                                , com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder.addToCollection(
                                this.headerBuilders,
                                headerBuilders
                        )
                        );
            }

            public Either<Collection<Connector.Error>, com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML> build() {
                return
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder.buildHeaders(
                                        this.fields,
                                        this.headerBuilders
                                )
                                .mapLeft(
                                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder.configurationInstanceForErrors(
                                                this.id,
                                                this.name,
                                                this.path,
                                                this.tagName

                                                , this.projectName
                                                , this.userName
                                        )
                                )
                                .flatMap(
                                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder.configurationInstance(
                                                this.id,
                                                this.name,
                                                this.path,
                                                this.tagName

                                                , this.projectName
                                                , this.userName
                                        )
                                );
            }

            public static com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder builder() {
                return
                        new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null

                        );
            }

            private static Function<Collection<Connector.Error>, Collection<Connector.Error>> configurationInstanceForErrors(
                    final String id,
                    final String name,
                    final String path,
                    final String tagName,
                    final String projectName
                    , final String userName
            ) {
                return
                        headersErrors ->
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder.addToCollection(
                                        headersErrors,
                                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.of(
                                                        id,
                                                        name,
                                                        path,
                                                        tagName,
                                                        null,
                                                        projectName
                                                        , userName
                                                )
                                                .fold(
                                                        Function.identity(),
                                                        __ -> Collections.emptySet()
                                                )
                                );

            }

            private static Function<Collection<Field>, Either<Collection<Connector.Error>, com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML>> configurationInstance(
                    final String id,
                    final String name,
                    final String path,
                    final String tagName,
                    final String projectName
                    , final String userName

            ) {
                return
                        fields ->
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.of(
                                        id,
                                        name,

                                        path,
                                        tagName
                                        , fields
                                        , projectName
                                        , userName


                                );
            }

            private static Either<Collection<Connector.Error>, Collection<Field>> buildHeaders(
                    final Collection<Field> fields,
                    final Collection<Supplier<Either<Collection<Field.Error>, Field>>> headerBuilders
            ) {
                final var errors = com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder.buildHeadersForErrors(headerBuilders);
                final Supplier<Collection<Field>> validHeaders =
                        () ->
                                com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder.mergeHeaders(
                                        fields,
                                        headerBuilders
                                );
                return
                        errors.isEmpty()
                                ? Either.right(validHeaders.get())
                                : Either.left(errors);
            }


            private static Collection<Field> mergeHeaders(
                    final Collection<Field> fields,
                    final Collection<Supplier<Either<Collection<Field.Error>, Field>>> headerBuilders
            ) {
                return
                        com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder.addToCollection(
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

            private static Collection<Connector.Error> buildHeadersForErrors(
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
                                        .map(com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Builder::errorToConfigurationError)
                                        .collect(Collectors.toUnmodifiableSet())
                                :
                                Collections.emptySet();
            }

            private static Connector.Error errorToConfigurationError(
                    final Field.Error headerError
            ) {
                if (headerError instanceof Field.Error.IdMalformed)
                    return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error.Header.IdMalformed();
                if (headerError instanceof Field.Error.ReferenceConnectorMalformed)
                    return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error.Header.ReferenceConnectorMalformed();
                if (headerError instanceof Field.Error.NameMalformed)
                    return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error.Header.NameMalformed();
                if (headerError instanceof Field.Error.PositionMalformed)
                    return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error.Header.PositionMalformed();
                if (headerError instanceof Field.Error.MetaMalformed)
                    return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error.Header.MetaMalformed();
                if (headerError instanceof Field.Error.NullOrEmptyRestrictionCombination)
                    return new com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error.Header.NullOrEmptyRestrictionCombination();
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

        public sealed interface Error extends Connector.Error {

            default String message() {
                return this.getClass().getCanonicalName();
            }


            final class SeparatorMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error {
                private SeparatorMalformed() {
                }
            }

            final class EncodingMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error {
                private EncodingMalformed() {
                }
            }

            final class pathMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error {
                private pathMalformed() {
                }
            }

            final class quotingCaracterMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error {
                private quotingCaracterMalformed() {
                }
            }

            final class escapingCaracterMalformed implements com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML.Error {
                private escapingCaracterMalformed() {
                }
            }


        }
    }

