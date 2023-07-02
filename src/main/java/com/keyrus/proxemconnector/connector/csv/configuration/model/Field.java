package com.keyrus.proxemconnector.connector.csv.configuration.model;

import com.keyrus.proxemconnector.connector.csv.configuration.enumerations.FieldType;
import io.vavr.control.Either;
import lombok.Data;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Data

public final class Field {
    private final String id;
    private final String referenceConnector;
    private final String name;
    private final int position;

    private final FieldType fieldType;
    private final String meta;
    private final boolean partOfDocumentIdentity;
    private final boolean included;

    private Field(
            final String id,
            final String referenceConnector,
            final String name,
            final int position,
            final String meta,
            // final boolean partOfDocumentIdentity,
            //  final boolean canBeNullOrEmpty,
            final FieldType field_type,
            boolean partOfDocumentIdentity, boolean included) {
        this.id = id;
        this.referenceConnector = referenceConnector;
        this.name = name;
        this.position = position;
        this.meta = meta;
        this.partOfDocumentIdentity = partOfDocumentIdentity;
        this.included = included;
        //this.partOfDocumentIdentity = partOfDocumentIdentity;
       // this.canBeNullOrEmpty = canBeNullOrEmpty;
        this.fieldType = field_type;
    }

    public String id() {
        return this.id;
    }

    public String referenceConnector() {
        return this.referenceConnector;
    }

    public String name() {
        return this.name;
    }

    public int position() {
        return this.position;
    }
    public boolean partOfDocumentIdentity() {
        return this.partOfDocumentIdentity;
    }


    public FieldType field_type(){ return this.fieldType; };

    public String meta() {
        return this.meta;
    }




    @Override
    public String toString() {
        return
                """
                        Header[
                            id=%s,
                            referenceConnector=%s,
                            name=%s,
                            position=%d,
                            meta=%s,
                          
                        ]
                        """
                        .formatted(
                                this.id,
                                this.referenceConnector,
                                this.name,
                                this.position,
                                this.meta,
                                this.fieldType
                        );
    }

    public static Either<Collection<Error>, Field> of(
            final String id,
            final String referenceConnector,
            final String name,
            final int position,
            final String meta,
            final FieldType field_type,
            final boolean partofDocumentIdentity,
            final boolean included
    ) {
        return
                Field.checkThenInstantiate(
                        Field.instance(
                                id,
                                referenceConnector,
                                name,
                                position,
                                meta,
                                field_type,
                                partofDocumentIdentity,
                                included
                        ),
                        Field.checkId(
                                id
                        ),
                        Field.checkReferenceConnector(
                                referenceConnector
                        ),
                        Field.checkName(
                                name
                        ),
                        Field.checkPosition(
                                position
                        ),
                        Field.checkMeta(
                                meta
                        )
                );
    }

    private static Supplier<Optional<Error>> checkNullOrEmptyValueRestriction(
            final boolean partOfDocumentIdentity,
            final boolean canBeNullOrEmpty
    ) {
        return
                () ->
                        partOfDocumentIdentity &&
                        canBeNullOrEmpty
                                ? Optional.of(new Error.NullOrEmptyRestrictionCombination())
                                : Optional.empty();
    }

    private static Supplier<Field> instance(
            final String id,
            final String referenceConnector,
            final String name,
            final int position,
            final String meta,
            final FieldType field_type,
            final boolean partOfDocumentIdentity,
            final boolean included
    ) {
        return
                () ->
                        new Field(
                                id,
                                referenceConnector,
                                name,
                                position,
                                meta,
                                field_type,
                                partOfDocumentIdentity
                                , included);
    }

    private static Supplier<Optional<Error>> checkId(
            final String id
    ) {
        return
                () ->
                        Field.checkNonNullableAndNonBlankString(
                                id,
                                Error.IdMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkReferenceConnector(
            final String referenceConnector
    ) {
        return
                () ->
                        Field.checkNonNullableAndNonBlankString(
                                referenceConnector,
                                Error.ReferenceConnectorMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkName(
            final String name
    ) {
        return
                () ->
                        Field.checkNonNullableAndNonBlankString(
                                name,
                                Error.NameMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkPosition(
            final int position
    ) {
        return
                () ->
                        Field.checkNonNullableAndPositiveInt(
                                position,
                                Error.PositionMalformed::new
                        );
    }

    private static Supplier<Optional<Error>> checkMeta(
            final String meta
    ) {
        return
                () ->
                        Field.checkNonNullableAndNonBlankString(
                                meta,
                                Error.MetaMalformed::new
                        );
    }

    @SafeVarargs
    private static <FIELD> Optional<Error> checkNonNullableField(
            final FIELD field,
            final Supplier<Error> errorIfInvalid,
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
                Field.checkField(
                        field,
                        errorIfInvalid,
                        checks
                );
    }

    private static Optional<Error> checkNonNullableAndPositiveInt(
            final Integer field,
            final Supplier<Error> errorIfInvalid
    ) {
        return
                Field.checkNonNullableField(
                        field,
                        errorIfInvalid,
                        it -> it > 0
                );
    }

    private static Optional<Error> checkNonNullableAndNonBlankString(
            final String field,
            final Supplier<Error> errorIfInvalid
    ) {
        return
                Field.checkNonNullableField(
                        field,
                        errorIfInvalid,
                        Predicate.not(String::isBlank)
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
    private static Either<Collection<Error>, Field> checkThenInstantiate(
            final Supplier<Field> headerInstance,
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
                        ? Either.right(headerInstance.get())
                        : Either.left(errors);
    }



    public sealed interface Error {

        default String message() {
            return this.getClass().getCanonicalName();
        }

        final class IdMalformed implements Error {
            private IdMalformed() {
            }
        }

        final class ReferenceConnectorMalformed implements Error {
            private ReferenceConnectorMalformed() {
            }
        }

        final class NameMalformed implements Error {
            private NameMalformed() {
            }
        }

        final class PositionMalformed implements Error {
            private PositionMalformed() {
            }
        }

        final class MetaMalformed implements Error {
            private MetaMalformed() {
            }
        }

        final class NullOrEmptyRestrictionCombination implements Error {
            private NullOrEmptyRestrictionCombination() {
            }
        }
    }
}
