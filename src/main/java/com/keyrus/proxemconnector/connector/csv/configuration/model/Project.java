package com.keyrus.proxemconnector.connector.csv.configuration.model;

import io.vavr.control.Either;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Project {

    private String id;
    private  String name;
    private String proxemToken;

    public Project(String id, String name, String proxemToken) {
        this.id = id;
        this.name = name;
        this.proxemToken = proxemToken;
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String proxemToken() {
        return proxemToken;
    }

    public void setProxemToken(String proxemToken) {
        this.proxemToken = proxemToken;
    }
// private Collection<User> users;

   // Collection<Connector> connectors;

    public static final class Builder {
        private final String id;
        private final String name;
        private final String tokenProxem;


        private Builder(
                final String id,
                final String name,
                String tokenProxem) {
            this.id = id;
            this.name = name;
            this.tokenProxem = tokenProxem;

        }

        public Project.Builder withId(
                final String id
        ) {
            return
                    new Project.Builder(
                            Project.Builder.nonNullOrDefault(
                                    id,
                                    this.id
                            ),
                            this.name,
                            this.tokenProxem

                    );
        }

        public Project.Builder withName(
                final String name
        ) {
            return
                    new Project.Builder(
                            this.id,
                            Project.Builder.nonNullOrDefault(
                                    name,
                                    this.name
                            ),
                            this.tokenProxem
                    );
        }

        public Project.Builder withToken(
                final String separator
        ) {
            return
                    new Project.Builder(
                            this.id,
                            this.name,
                            Project.Builder.nonNullOrDefault(
                                    separator,
                                    this.tokenProxem
                            )
                    );
        }


        public Either<Collection<Project.Error>, Project> build() {
            return

                    Project.Builder.projectInstance(
                                            this.id,
                                            this.name,
                                          this.tokenProxem
                                    );

        }

        public static Project.Builder builder() {
            return
                    new Project.Builder(
                            null,
                            null,
                            null

                    );
        }

        private static Function<Collection<Project.Error>, Collection<Project.Error>> projectInstanceForErrors(
                final String id,
                final String name,
                final String token

        ) {
            return
                    headersErrors ->


                            Project.of(
                                              id,
                                                    name,token

                                            )
                                            .fold(
                                                    Function.identity(),
                                                    __ -> Collections.emptySet()
                                            );

        }

        private static  Either<Collection<Error>, Project> projectInstance(
                final String id,
                final String name,
                final String token
        ) {
            return

                            Project.of(
                                    id,
                                    name,
                                    token
                            );
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

    public static Either<Collection<Project.Error>, Project> of(
            final String id,
            final String name,
            final String token

    ) {
        return
                Project.checkThenInstantiate(
                        Project.instance(
                                id,
                                name,
                                token
                        ),
                        Project.checkId(
                                id
                        ),
                        Project.checkName(
                                name
                        ),
                        Project.checkToken(
                                token
                        )
                );}

    @SafeVarargs
    private static Either<Collection<Project.Error>, Project> checkThenInstantiate(
            final Supplier<Project> headerInstance,
            final Supplier<Optional<Project.Error>>... checks
    ) {
        final var errors =
                Arrays.stream(checks)
                        .map(Supplier::get)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toUnmodifiableSet());
        return
                errors.isEmpty()
                        ? Either.right(headerInstance.get())
                        : Either.left(errors);
    }

    private static Supplier<Project> instance(
            final String id,
            final String name,
            final String token

    ) {
        return
                () ->
                        new Project(
                                id,
                                name,
                                token

                        );
    }


    private static Supplier<Optional<Error>> checkToken(
            final String name
    ) {
        return
                () ->
                        Project.checkNonNullableAndNonBlankString(
                                name,
                                Error.TokenMalformed::new
                        );
    }

    private static Optional<Error> checkNonNullableAndNonBlankString(
            final String field,
            final Supplier<Error> errorIfInvalid
    ) {
        return
                Project.checkNonNullableField(
                        field,
                        errorIfInvalid,
                        Predicate.not(String::isBlank)
                );
    }

    @SafeVarargs
    private static <FIELD> Optional<Project.Error> checkNonNullableField(
            final FIELD field,
            final Supplier<Project.Error> errorIfInvalid,
            final Predicate<FIELD>... extraValidityChecks
    ) {
        final Predicate<FIELD> nonNullCheck = Objects::nonNull;
        final Predicate<FIELD>[] checks =
                Stream.of(
                                Stream.of(nonNullCheck),
                                Arrays.stream(extraValidityChecks)
                        )
                        .flatMap(Function.identity())
                        .filter(Objects::nonNull)
                        .toArray(Predicate[]::new);
        return
                Project.checkField(
                        field,
                        errorIfInvalid,
                        checks
                );
    }

    @SafeVarargs
    private static <FIELD> Optional<Project.Error> checkField(
            final FIELD field,
            final Supplier<Project.Error> errorIfInvalid,
            final Predicate<FIELD>... validityChecks
    ) {
        return
                Arrays.stream(validityChecks)
                        .anyMatch(Predicate.not(it -> it.test(field)))
                        ? Optional.ofNullable(errorIfInvalid.get())
                        : Optional.empty();
    }

    private static Supplier<Optional<Error>> checkName(
            final String name
    ) {
        return
                () ->
                        Project.checkNonNullableAndNonBlankString(
                                name,
                                Error.NameMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkId(
            final String id
    ) {
        return
                () ->
                        Project.checkNonNullableAndNonBlankString(
                                id,
                                Error.IdMalformed::new
                        );
    }

    public sealed   interface Error {

       default String message() {
           return this.getClass().getCanonicalName();
       }

        final class IdMalformed implements Error {
            private IdMalformed() {
            }
        }

        final class TokenMalformed implements Error {
            private TokenMalformed() {
            }
        }

        final class NameMalformed implements Error {
            private NameMalformed() {
            }
        }

   }

}
