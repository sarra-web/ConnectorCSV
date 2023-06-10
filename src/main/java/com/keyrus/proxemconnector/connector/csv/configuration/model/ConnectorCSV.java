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

public final class ConnectorCSV {
    private final String id;
    private final String name;
    private final String separator;
    private final String encoding;
    private final String path;
    //private final String quotingCaracter;
    //private final String escapingCaracter;
    private final String quotingCaracter;
    private final String escapingCaracter;


    private final boolean containsHeaders;
    private final Collection<Field> fields;
  //  private final Project project;

    private ConnectorCSV(
            final String id,
            final String name,
            final String separator,
            final String encoding,
            final String path,
            final String quotingCaracter,
            final String escapingCaracter,
            final boolean containsHeaders,
            final Collection<Field> fields,
            Project project) {
        this.id = id;
        this.name = name;
        this.separator = separator;
        this.encoding = encoding;
        this.path = path;
        this.quotingCaracter = quotingCaracter;
        this.escapingCaracter = escapingCaracter;
        this.containsHeaders = containsHeaders;
        this.fields = fields;
       // this.project = project;
    }

    private ConnectorCSV(
            final String id,
            final String name,
            final String path,
            final String quotingCaracter,
            final String escapingCaracter,
            final boolean containsHeaders,
            final Collection<Field> fields,
            final Project project) {
        this(
                id,
                name,
                ",",
                StandardCharsets.UTF_8.name(),
                path,
                quotingCaracter,
                escapingCaracter,
                containsHeaders,
                fields,
                project);
    }

    private ConnectorCSV(
            final String id,
            final String name,
            final String separator,
            final String path,
            final String quotingCaracter,
            final String escapingCaracter,
            final boolean containsHeaders,
            final Collection<Field> fields,
            final Project project
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
                fields,
                project);
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
    //public Project project() {
        //return this.project;
  //  }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Connector connector = (Connector) o;

        if (containsHeaders != connector.containsHeaders) return false;
        if (!id.equals(connector.id)) return false;
        if (!name.equals(connector.name)) return false;
        if (!separator.equals(connector.separator)) return false;
        if (!encoding.equals(connector.encoding)) return false;
        if (!path.equals(connector.path)) return false;
        if (!quotingCaracter.equals(connector.quotingCaracter)) return false;
        if (!escapingCaracter.equals(connector.escapingCaracter)) return false;
        if (!fields.equals(connector.fields)) return false;
        return project.equals(connector.project);
    }
*/
    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + separator.hashCode();
        result = 31 * result + encoding.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + quotingCaracter.hashCode();
        result = 31 * result + escapingCaracter.hashCode();
        result = 31 * result + (containsHeaders ? 1 : 0);
        result = 31 * result + fields.hashCode();
   //     result = 31 * result + project.hashCode();
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
                              //  this.project
                        );
    }

    private static Either<Collection<Error>, ConnectorCSV> of(
            final String id,
            final String name,
            final String separator,
            final String encoding,
            final String path,
            final String quotingCaracter,
            final String escapingCaracter,
            final boolean containsHeaders,
            final Collection<Field> fields,
            final Project project
    ) {
        return
                ConnectorCSV.checkThenInstantiate(
                        ConnectorCSV.instance(
                                id,
                                name,
                                separator,
                                encoding,
                                path,
                                quotingCaracter,
                                escapingCaracter,
                                containsHeaders,
                                fields,project
                        ),
                        ConnectorCSV.checkId(
                                id
                        ), ConnectorCSV.checkName(
                                name
                        ),
                        ConnectorCSV.checkSeparator(
                                separator
                        ),
                        ConnectorCSV.checkEncoding(
                                encoding
                        ),
                        ConnectorCSV.checkpath(
                                path
                        ),
                        ConnectorCSV.checkquotingCaracter(
                                quotingCaracter
                        ),
                        ConnectorCSV.checkescapingCaracter(
                                escapingCaracter
                        ),
                        ConnectorCSV.checkHeaders(
                                fields
                        )
                );
    }

    private static Supplier<ConnectorCSV> instance(
            final String id,
            final String name,
            final String separator,
            final String encoding,
            final String path,
            final String quotingCaracter,
            final String escapingCaracter,
            final boolean containsHeaders,
            final Collection<Field> fields,
            final Project project
    ) {
        if (
                Objects.isNull(separator) &&
                Objects.isNull(encoding)
        )
            return
                    () ->
                            new ConnectorCSV(
                                    id,
                                    name,
                                    path,
                                    quotingCaracter,
                                    escapingCaracter,
                                    containsHeaders,
                                    fields,
                                    project);
        if (Objects.isNull(encoding))
            return
                    () ->
                            new ConnectorCSV(
                                    id,
                                    name,
                                    separator,
                                    path,
                                    quotingCaracter,
                                    escapingCaracter,
                                    containsHeaders,
                                    fields,project
                            );
        return

                () ->
                        new ConnectorCSV(
                                id,
                                name,
                                separator,
                                encoding,
                                path,
                                quotingCaracter,
                                escapingCaracter,
                                containsHeaders,
                                fields,
                                project);
    }

    private static Supplier<Optional<Error>> checkId(
            final String id
    ) {
        return
                () ->
                        ConnectorCSV.checkNonNullableNonBlankStringField(
                                id,
                                Error.IdMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkName(
            final String name
    ) {
        return
                () ->
                        ConnectorCSV.checkNonNullableNonBlankStringField(
                                name,
                                Error.NameMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkSeparator(
            final String separator
    ) {
        return
                () ->
                        ConnectorCSV.checkNullableNonBlankStringField(
                                separator,
                                Error.SeparatorMalformed::new,
                                ConnectorCSV::checkSeparatorIsNotComposedOfLettersOrDigits
                        );
    }

    private static Supplier<Optional<Error>> checkEncoding(
            final String encoding
    ) {
        return
                () ->
                        ConnectorCSV.checkNullableNonBlankStringField(
                                encoding,
                                Error.EncodingMalformed::new,
                                Charset::isSupported
                        );
    }

    private static Supplier<Optional<Error>> checkpath(
            final String path
    ) {
        return
                () ->
                        ConnectorCSV.checkNonNullableNonBlankStringField(
                                path,
                                Error.pathMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkquotingCaracter(
            final String quotingCaracter
    ) {
        return
                () ->
                        ConnectorCSV.checkNonNullableNonBlankStringField(
                                quotingCaracter,
                                Error.quotingCaracterMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkescapingCaracter(
            final String escapingCaracter
    ) {
        return
                () ->
                        ConnectorCSV.checkNonNullableNonBlankStringField(
                                escapingCaracter,
                                Error.escapingCaracterMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkHeaders(
            final Collection<Field> fields
    ) {
        return
                () ->
                        ConnectorCSV.checkHeaderFormat(fields)
                                .or(() ->
                                        ConnectorCSV.checkHeadersIds(fields)
                                )
                                .or(() ->
                                        ConnectorCSV.checkHeadersPositions(fields)
                                );
    }

    private static Optional<Error> checkHeadersPositions(
            final Collection<Field> fields
    ) {
        return
                ConnectorCSV.checkField(
                        fields,
                        Error.HeadersSequenceMalformed::new,
                        ConnectorCSV::checkHeadersPositionSequence
                );
    }

    private static Optional<Error> checkHeadersIds(
            final Collection<Field> fields
    ) {
        return
                ConnectorCSV.checkField(
                        fields,
                        Error.IdHeaderMissing::new,
                        ConnectorCSV::checkHeadersContainsIds
                );
    }

    private static Optional<Error> checkHeaderFormat(
            final Collection<Field> fields
    ) {
        return
                ConnectorCSV.checkNonNullableField(
                        fields,
                        Error.HeaderMalformed::new
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
    private static Optional<Error> checkNonNullableNonBlankStringField(
            final String field,
            final Supplier<Error> errorIfInvalid,
            final Predicate<String>... extraChecks
    ) {
        return
                ConnectorCSV.checkNonNullableField(
                        field,
                        errorIfInvalid,
                        ConnectorCSV.mergePredicates(
                                Predicate.not(String::isBlank),
                                extraChecks
                        )
                );
    }

    @SafeVarargs
    private static Optional<Error> checkNullableNonBlankStringField(
            final String field,
            final Supplier<Error> errorIfInvalid,
            final Predicate<String>... extraChecks
    ) {
        return
                Objects.nonNull(field)
                        ?
                        ConnectorCSV.checkNonNullableField(
                                field,
                                errorIfInvalid,
                                ConnectorCSV.mergePredicates(
                                        Predicate.not(String::isBlank),
                                        extraChecks
                                )
                        )
                        :
                        Optional.empty();
    }

    @SafeVarargs
    private static <FIELD> Optional<Error> checkNonNullableField(
            final FIELD field,
            final Supplier<Error> errorIfInvalid,
            final Predicate<FIELD>... extraChecks
    ) {
        return
                ConnectorCSV.checkField(
                        field,
                        errorIfInvalid,
                        ConnectorCSV.mergePredicates(
                                Objects::nonNull,
                                extraChecks
                        )
                );
    }

    @SafeVarargs
    private static <FIELD> Optional<Error> checkField(
            final FIELD field,
            final Supplier<Error> errorIfInvalid,
            final Predicate<FIELD>... validityChecks
    ) {
        return
                Arrays.stream(validityChecks)
                        .anyMatch(Predicate.not(it -> it.test(field)))
                        ? Optional.ofNullable(errorIfInvalid.get())
                        : Optional.empty();
    }

    @SafeVarargs
    private static Either<Collection<Error>, ConnectorCSV> checkThenInstantiate(
            final Supplier<ConnectorCSV> instance,
            final Supplier<Optional<Error>>... checks
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
            this.headerBuilders = headerBuilders;
        }

        public Builder withId(
                final String id
        ) {
            return
                    new Builder(
                            Builder.nonNullOrDefault(
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
                            this.fields,
                            this.project,
                            this.headerBuilders
                    );
        }

        public Builder withName(
                final String name
        ) {
            return
                    new Builder(
                            this.id,
                            Builder.nonNullOrDefault(
                                    name,
                                    this.name
                            ),
                            this.separator,
                            this.encoding,
                            this.path,
                            this.quotingCaracter,
                            this.escapingCaracter,
                            this.containsHeaders,
                            this.fields,
                            this.project,
                            this.headerBuilders
                    );
        }

        public Builder withSeparator(
                final String separator
        ) {
            return
                    new Builder(
                            this.id,
                            this.name,
                            Builder.nonNullOrDefault(
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
                            this.headerBuilders
                    );
        }

        public Builder withEncoding(
                final String encoding
        ) {
            return
                    new Builder(
                            this.id,
                            this.name,
                            this.separator,
                            Builder.nonNullOrDefault(
                                    encoding,
                                    this.encoding
                            ),
                            this.path,
                            this.quotingCaracter,
                            this.escapingCaracter,
                            this.containsHeaders,
                            this.fields,
                            this.project,
                            this.headerBuilders
                    );
        }

        public Builder withpath(
                final String path
        ) {
            return
                    new Builder(
                            this.id,
                            this.name,
                            this.separator,
                            this.encoding,
                            Builder.nonNullOrDefault(
                                    path,
                                    this.path
                            ),
                            this.quotingCaracter,
                            this.escapingCaracter,
                            this.containsHeaders,
                            this.fields,
                            this.project,
                            this.headerBuilders
                    );
        }

        public Builder withquotingCaracter(
                final String quotingCaracter
        ) {
            return
                    new Builder(
                            this.id,
                            this.name,
                            this.separator,
                            this.encoding,
                            this.path,
                            Builder.nonNullOrDefault(
                                    quotingCaracter,
                                    this.quotingCaracter
                            ),
                            this.escapingCaracter,
                            this.containsHeaders,
                            this.fields,
                            this.project,
                            this.headerBuilders
                    );
        }

        public Builder withescapingCaracter(
                final String escapingCaracter
        ) {
            return
                    new Builder(
                            this.id,
                            this.name,
                            this.separator,
                            this.encoding,
                            this.path,
                            this.quotingCaracter,
                            Builder.nonNullOrDefault(
                                    escapingCaracter,
                                    this.escapingCaracter
                            ),
                            this.containsHeaders,
                            this.fields,
                            this.project,
                            this.headerBuilders
                    );
        }

        public Builder withContainsHeaders(
                final boolean containsHeaders
        ) {
            return
                    new Builder(
                            this.id,
                            this.name,
                            this.separator,
                            this.encoding,
                            this.path,
                            this.quotingCaracter,
                            this.escapingCaracter,
                            containsHeaders,
                            this.fields,
                            this.project,
                            this.headerBuilders
                    );
        }

        public Builder withHeaders(
                final Collection<Field> fields
        ) {
            return
                    new Builder(
                            this.id,
                            this.name,
                            this.separator,
                            this.encoding,
                            this.path,
                            this.quotingCaracter,
                            this.escapingCaracter,
                            this.containsHeaders,
                            Builder.addToCollection(
                                    this.fields,
                                    fields
                            ),
                            this.project,
                            this.headerBuilders
                    );
        }

        @SafeVarargs
        public final Builder withHeaders(
                final Supplier<Either<Collection<Field.Error>, Field>>... headerBuilders
        ) {
            return
                    new Builder(
                            this.id,
                            this.name,
                            this.separator,
                            this.encoding,
                            this.path,
                            this.quotingCaracter,
                            this.escapingCaracter,
                            this.containsHeaders,
                            this.fields,
                            this.project,
                            Builder.addToCollection(
                                    this.headerBuilders,
                                    headerBuilders
                            )
                    );
        }

        public Either<Collection<Error>, ConnectorCSV> build() {
            return
                    Builder.buildHeaders(
                                    this.fields,
                                    this.headerBuilders
                            )
                            .mapLeft(
                                    Builder.configurationInstanceForErrors(
                                            this.id,
                                            this.name,
                                            this.separator,
                                            this.encoding,
                                            this.path,
                                            this.quotingCaracter,
                                            this.escapingCaracter,
                                            this.containsHeaders,
                                            this.project
                                    )
                            )
                            .flatMap(
                                    Builder.configurationInstance(
                                            this.id,
                                            this.name,
                                            this.separator,
                                            this.encoding,
                                            this.path,
                                            this.quotingCaracter,
                                            this.escapingCaracter,
                                            this.containsHeaders,
                                            this.project
                                    )
                            );
        }

        public static Builder builder() {
            return
                    new Builder(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            true,
                            null,
                           null,
                            null
                    );
        }

        private static Function<Collection<Error>, Collection<Error>> configurationInstanceForErrors(
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
                    headersErrors ->
                            Builder.addToCollection(
                                    headersErrors,
                                    ConnectorCSV.of(
                                                    id,
                                                    name,
                                                    separator,
                                                    encoding,
                                                    path,
                                                    quotingCaracter,
                                                    escapingCaracter,
                                                    containsHeaders,
                                                    null,project
                                            )
                                            .fold(
                                                    Function.identity(),
                                                    __ -> Collections.emptySet()
                                            )
                            );
        }

        private static Function<Collection<Field>, Either<Collection<Error>, ConnectorCSV>> configurationInstance(
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
                            ConnectorCSV.of(
                                    id,
                                    name,
                                    separator,
                                    encoding,
                                    path,
                                    quotingCaracter,
                                    escapingCaracter,
                                    containsHeaders,
                                    fields,project
                            );
        }

        private static Either<Collection<Error>, Collection<Field>> buildHeaders(
                final Collection<Field> fields,
                final Collection<Supplier<Either<Collection<Field.Error>, Field>>> headerBuilders
        ) {
            final var errors = Builder.buildHeadersForErrors(headerBuilders);
            final Supplier<Collection<Field>> validHeaders =
                    () ->
                            Builder.mergeHeaders(
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
                    Builder.addToCollection(
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

        private static Collection<Error> buildHeadersForErrors(
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
                                    .map(Builder::errorToConfigurationError)
                                    .collect(Collectors.toUnmodifiableSet())
                            :
                            Collections.emptySet();
        }

        private static Error errorToConfigurationError(
                final Field.Error headerError
        ) {
            if (headerError instanceof Field.Error.IdMalformed)
                return new Error.Header.IdMalformed();
            if (headerError instanceof Field.Error.ReferenceConnectorMalformed)
                return new Error.Header.ReferenceConnectorMalformed();
            if (headerError instanceof Field.Error.NameMalformed)
                return new Error.Header.NameMalformed();
            if (headerError instanceof Field.Error.PositionMalformed)
                return new Error.Header.PositionMalformed();
            if (headerError instanceof Field.Error.MetaMalformed)
                return new Error.Header.MetaMalformed();
            if (headerError instanceof Field.Error.NullOrEmptyRestrictionCombination)
                return new Error.Header.NullOrEmptyRestrictionCombination();
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

        final class IdMalformed implements Error {
            private IdMalformed() {
            }
        }

        final class NameMalformed implements Error {
            private NameMalformed() {
            }
        }

        final class SeparatorMalformed implements Error {
            private SeparatorMalformed() {
            }
        }

        final class EncodingMalformed implements Error {
            private EncodingMalformed() {
            }
        }

        final class pathMalformed implements Error {
            private pathMalformed() {
            }
        }

        final class quotingCaracterMalformed implements Error {
            private quotingCaracterMalformed() {
            }
        }

        final class escapingCaracterMalformed implements Error {
            private escapingCaracterMalformed() {
            }
        }

        final class HeaderMalformed implements Error {
            private HeaderMalformed() {
            }
        }

        final class IdHeaderMissing implements Error {
            private IdHeaderMissing() {
            }
        }

        final class HeadersSequenceMalformed implements Error {
            private HeadersSequenceMalformed() {
            }
        }

        sealed interface Header extends Error {

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
