package com.keyrus.proxemconnector.connector.csv.configuration.rest.handler;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorCSVDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorCSVDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorCSV;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.ConnectorJDBCDatabaseRepository;
import com.keyrus.proxemconnector.initializer.PostgreSQLInitializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {PostgreSQLInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConnectorCSVRestHandlerTest {
    private final ConnectorRestHandler connectorRestHandler;
    private final String errorHeader;
    private final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository;

    @Autowired
    ConnectorCSVRestHandlerTest(
            final ConnectorRestHandler connectorRestHandler,
            @Value("${connectors.rest.error-header:error}") final String errorHeader,
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository
    ) {
        this.connectorRestHandler = connectorRestHandler;
        this.errorHeader = errorHeader;
        this.connectorJDBCDatabaseRepository = connectorJDBCDatabaseRepository;
    }

    @BeforeAll
    void beforeAll() {
        this.connectorJDBCDatabaseRepository.deleteAll();
    }

    @BeforeEach
    void beforeEach() {
        this.connectorJDBCDatabaseRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        this.connectorJDBCDatabaseRepository.deleteAll();
    }

    @AfterAll
    void afterAll() {
        this.connectorJDBCDatabaseRepository.deleteAll();
    }

    @Test
    @DisplayName("configuration rest handler must return error if create method is called with invalid configuration dto")
    void configuration_rest_handler_must_return_error_if_create_method_is_called_with_invalid_configuration_dto() {
        final var result =
                this.connectorRestHandler
                        .create(
                                new ConnectorCSVDTO(
                                        " ",
                                        " ",
                                        " ",
                                        " ",
                                        " ",
                                        " ",
                                        " ",
                                        true,
                                        Collections.emptySet()
                                ),
                                null
                        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> Assertions.assertTrue(result.getHeaders().containsKey(this.errorHeader))
        );
    }

    @Test
    @DisplayName("configuration rest handler must return error if create method is called with invalid configuration")
    void configuration_rest_handler_must_return_error_if_create_method_is_called_with_invalid_configuration() {
        final var id=UUID.randomUUID().toString();
        final var configuration =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withFolderToScan(UUID.randomUUID().toString())
                        .withArchiveFolder(UUID.randomUUID().toString())
                        .withFailedRecordsFolder(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                true,
                                                                false, "texte"
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.connectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );

        final var result =
                this.connectorRestHandler
                        .create(
                                new ConnectorCSVDTO(configuration),
                                null
                        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> Assertions.assertTrue(result.getHeaders().containsKey(this.errorHeader))
        );
    }

    @Test
    @DisplayName("configuration rest handler must return created configuration if create method is called with valid configuration")
    void configuration_rest_handler_must_return_created_configuration_if_create_method_is_called_with_valid_configuration() {
        final var id=UUID.randomUUID().toString();
        final var configuration =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withFolderToScan(UUID.randomUUID().toString())
                        .withArchiveFolder(UUID.randomUUID().toString())
                        .withFailedRecordsFolder(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                true,
                                                                false, "texte"
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();

        final var result =
                this.connectorRestHandler
                        .create(
                                new ConnectorCSVDTO(configuration),
                                null
                        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );
    }

    @Test
    @DisplayName("configuration rest handler must return error if update method is called with invalid configuration dto")
    void configuration_rest_handler_must_return_error_if_update_method_is_called_with_invalid_configuration_dto() {
        final var result =
                this.connectorRestHandler
                        .update(
                                new ConnectorCSVDTO(
                                        " ",
                                        " ",
                                        " ",
                                        " ",
                                        " ",
                                        " ",
                                        " ",
                                        true,
                                        Collections.emptySet()
                                ),
                                null
                        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> Assertions.assertTrue(result.getHeaders().containsKey(this.errorHeader))
        );
    }

    @Test
    @DisplayName("configuration rest handler must return error if update method is called with invalid configuration")
    void configuration_rest_handler_must_return_error_if_update_method_is_called_with_invalid_configuration() {
        final var id=UUID.randomUUID().toString();
        final var configuration =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withFolderToScan(UUID.randomUUID().toString())
                        .withArchiveFolder(UUID.randomUUID().toString())
                        .withFailedRecordsFolder(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                true,
                                                                false, "texte"
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();

        final var result =
                this.connectorRestHandler
                        .update(
                                new ConnectorCSVDTO(configuration),
                                null
                        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> Assertions.assertTrue(result.getHeaders().containsKey(this.errorHeader))
        );
    }

    @Test
    @DisplayName("configuration rest handler must return updated configuration if update method is called with valid configuration")
    void configuration_rest_handler_must_return_updated_configuration_if_update_method_is_called_with_valid_configuration() {
        final var id=UUID.randomUUID().toString();
        final var configuration =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withFolderToScan(UUID.randomUUID().toString())
                        .withArchiveFolder(UUID.randomUUID().toString())
                        .withFailedRecordsFolder(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                true,
                                                                false, "texte"
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.connectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );

        final var result =
                this.connectorRestHandler
                        .update(
                                new ConnectorCSVDTO(configuration),
                                null
                        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );
    }

    @Test
    @DisplayName("configuration rest handler must return error if delete method is called with invalid configuration")
    void configuration_rest_handler_must_return_error_if_delete_method_is_called_with_invalid_configuration() {
        final var result =
                this.connectorRestHandler
                        .delete(
                                UUID.randomUUID().toString(),
                                null
                        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> Assertions.assertTrue(result.getHeaders().containsKey(this.errorHeader))
        );
    }

    @Test
    @DisplayName("configuration rest handler must return deleted configuration if delete method is called with valid configuration")
    void configuration_rest_handler_must_return_deleted_configuration_if_delete_method_is_called_with_valid_configuration() {
        final var id=UUID.randomUUID().toString();
        final var configuration =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withFolderToScan(UUID.randomUUID().toString())
                        .withArchiveFolder(UUID.randomUUID().toString())
                        .withFailedRecordsFolder(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                true,
                                                                false, "texte"
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.connectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );

        final var result =
                this.connectorRestHandler
                        .delete(
                                configuration.id(),
                                null
                        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );
    }
    @Test
    @DisplayName("configuration rest handler must return a list of configurations if findAll method is called with valid configurations ")
    void configuration_rest_handler_must_return_List_of_configurations_if_findAll_method_is_called_with_valid_configurations() {
        final var id=UUID.randomUUID().toString();
        final var configuration =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withFolderToScan(UUID.randomUUID().toString())
                        .withArchiveFolder(UUID.randomUUID().toString())
                        .withFailedRecordsFolder(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                true,
                                                                false,""
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.connectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );

        final var result =
                this.connectorRestHandler.findAll(null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );
    }

    @Test
    @DisplayName("configuration rest handler must return an empty list of configurations if findAll method is called with no saved configurations ")
    void configuration_rest_handler_must_return_ampty_List_of_configurations_if_findAll_method_is_called_with_no_saved_configurations() {
        final var result =
                this.connectorRestHandler.findAll(null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );

    }
    @Test
    @DisplayName("configuration rest handler must return error if findOneByName method is called with invalid configuration")
    void configuration_rest_handler_must_return_error_if_findOneByName_method_is_called_with_invalid_configuration() {
        final var result =
                this.connectorRestHandler
                        .findOneByName(
                                UUID.randomUUID().toString(),
                                null
                        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> Assertions.assertTrue(result.getHeaders().containsKey(this.errorHeader))
        );
    }

    @Test
    @DisplayName("configuration rest handler must return searched configuration if findOneByName method is called with valid configuration")
    void configuration_rest_handler_must_return_searched_configuration_if_findOneByName_method_is_called_with_valid_configuration() {
        final var id = UUID.randomUUID().toString();
        final var configuration =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withFolderToScan(UUID.randomUUID().toString())
                        .withArchiveFolder(UUID.randomUUID().toString())
                        .withFailedRecordsFolder(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                true,
                                                                false,""
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.connectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );

        final var result =
                this.connectorRestHandler
                        .findOneByName(
                                configuration.name(),
                                null
                        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );
    }
    @Test
    @DisplayName("configuration rest handler must return a list of configurations if findManyByNameContainsIgnoreCase method is called with valid configurations ")
    void configuration_rest_handler_must_return_List_of_configurations_if_findManyByNameContainsIgnoreCase_method_is_called_with_valid_configurations() {
        final var id = UUID.randomUUID().toString();
        final var configuration =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName("billing history")
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withFolderToScan(UUID.randomUUID().toString())
                        .withArchiveFolder(UUID.randomUUID().toString())
                        .withFailedRecordsFolder(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(10)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                true,
                                                                false,""
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.connectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );

        final var result =
                this.connectorRestHandler.findManyByNameContainsIgnoreCase("BilL", null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );
    }
}