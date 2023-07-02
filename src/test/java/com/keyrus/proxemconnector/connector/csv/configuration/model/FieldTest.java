package com.keyrus.proxemconnector.connector.csv.configuration.model;

import com.keyrus.proxemconnector.connector.csv.configuration.enumerations.FieldType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.UUID;

class FieldTest {

    @Test
    @DisplayName("header must return error if of method is called with invalid id")
    void header_must_return_error_if_of_method_is_called_with_invalid_id() {
        final var result =
                Field.of(
                                " ",
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                new Random().nextInt(1, 10),
                                UUID.randomUUID().toString(),
                                FieldType.Text,
                                new Random().nextBoolean(), true
                        )
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Field.Error.IdMalformed))
        );
    }

    @Test
    @DisplayName("header must return error if of method is called with invalid reference connector")
    void header_must_return_error_if_of_method_is_called_with_invalid_reference_connector() {
        final var result =
                Field.of(
                                UUID.randomUUID().toString(),
                                " ",
                                UUID.randomUUID().toString(),
                                new Random().nextInt(1, 10),
                                UUID.randomUUID().toString(),
                                FieldType.Meta,
                                new Random().nextBoolean(), true
                        )
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Field.Error.ReferenceConnectorMalformed))
        );
    }

    @Test
    @DisplayName("header must return error if of method is called with invalid name")
    void header_must_return_error_if_of_method_is_called_with_invalid_name() {
        final var result =
                Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                " ",
                                new Random().nextInt(1, 10),
                                UUID.randomUUID().toString(),
                                FieldType.Meta,
                                new Random().nextBoolean(), true
                        )
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Field.Error.NameMalformed))
        );
    }

    @Test
    @DisplayName("header must return error if of method is called with invalid position")
    void header_must_return_error_if_of_method_is_called_with_invalid_position() {
        final var result =
                Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                -1,
                                UUID.randomUUID().toString(),
                                FieldType.Meta,
                                new Random().nextBoolean(), true
                        )
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Field.Error.PositionMalformed))
        );
    }

    @Test
    @DisplayName("header must return error if of method is called with invalid meta")
    void header_must_return_error_if_of_method_is_called_with_invalid_meta() {
        final var result =
                Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                new Random().nextInt(1, 10),
                                " ",
                                FieldType.Meta,
                                new Random().nextBoolean(), true
                        )
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Field.Error.MetaMalformed))
        );
    }

  /*  @Test
    @DisplayName("header must return error if of method is called with invalid value restriction")
    void header_must_return_error_if_of_method_is_called_with_invalid_value_restriction() {
        final var result =
                Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                new Random().nextInt(1, 10),
                                UUID.randomUUID().toString(),
                                FieldType.Meta,
                                true, true
                        )
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Field.Error.NullOrEmptyRestrictionCombination))
        );
    }*/

    @Test
    @DisplayName("header must return error if of method is called with multiple invalid parameters")
    void header_must_return_error_if_of_method_is_called_with_multiple_invalid_parameters() {
        final var result =
                Field.of(
                                null,
                                "",
                                null,
                                -1,
                                " ",
                                FieldType.Meta,
                                true, true
                        )
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(5, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Field.Error.IdMalformed)),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Field.Error.ReferenceConnectorMalformed)),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Field.Error.NameMalformed)),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Field.Error.PositionMalformed)),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Field.Error.MetaMalformed))
        );
    }

    @Test
    @DisplayName("header must return instance if of method is called with valid parameters")
    void header_must_return_instance_if_of_method_is_called_with_valid_parameters() {
        final var id = UUID.randomUUID().toString();
        final var referenceConnector = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var position = new Random().nextInt(1, 10);
        final var meta = UUID.randomUUID().toString();
        final var partOfDocumentIdentity = false;
        final var type=FieldType.Meta;
        final var included = new Random().nextBoolean();

        final var result =
                Field.of(
                                id,
                                referenceConnector,
                                name,
                                position,
                                meta,
                                type,
                                partOfDocumentIdentity, included
                        )
                        .get();

        Assertions.assertAll(
                () -> Assertions.assertEquals(id, result.id()),
                () -> Assertions.assertEquals(referenceConnector, result.referenceConnector()),
                () -> Assertions.assertEquals(name, result.name()),
                () -> Assertions.assertEquals(position, result.position()),
                () -> Assertions.assertEquals(meta, result.meta()),
                () -> Assertions.assertEquals(partOfDocumentIdentity, result.partOfDocumentIdentity()),
                () -> Assertions.assertEquals(included, result.isIncluded())
        );
    }
}