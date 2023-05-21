package com.keyrus.proxemconnector.connector.csv.configuration.service;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Connector;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.ConnectorJDBCDatabaseRepository;
import com.keyrus.proxemconnector.initializer.PostgreSQLInitializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {PostgreSQLInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConnectorServiceTest {

    private final ConnectorService connectorService;
    private final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository;

    @Autowired
    ConnectorServiceTest(
            final ConnectorService connectorService,
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository
    ) {
        this.connectorService = connectorService;
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
    @DisplayName("configuration service must return error if create method is called with invalid configuration")
    void configuration_service_must_return_error_if_create_method_is_called_with_invalid_configuration() {
        final var id=UUID.randomUUID().toString();
        final var configuration =
                Connector.Builder
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
                                                                false
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.connectorJDBCDatabaseRepository.save(
                new ConnectorDAO(
                        configuration
                )
        );

        final var result =
                this.connectorService
                        .create(
                                configuration
                        )
                        .isLeft();

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("configuration service must return created configuration if create method is called with valid configuration")
    void configuration_service_must_return_created_configuration_if_create_method_is_called_with_valid_configuration() {
        final var id=UUID.randomUUID().toString();
        final var configuration =
                Connector.Builder
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
                                                                false
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();

        final var result =
                this.connectorService
                        .create(
                                configuration
                        )
                        .isRight();

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("configuration service must return error if update method is called with invalid configuration")
    void configuration_service_must_return_error_if_update_method_is_called_with_invalid_configuration() {
        final var id=UUID.randomUUID().toString();
        final var configuration =
                Connector.Builder
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
                                                                false
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();

        final var result =
                this.connectorService
                        .update(
                                configuration
                        )
                        .isLeft();

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("configuration service must return updated configuration if update method is called with valid configuration")
    void configuration_service_must_return_updated_configuration_if_update_method_is_called_with_valid_configuration() {
        final var id=UUID.randomUUID().toString();
        final var configuration =
                Connector.Builder
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
                                                                false
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.connectorJDBCDatabaseRepository.save(
                new ConnectorDAO(
                        configuration
                )
        );

        final var result =
                this.connectorService
                        .update(
                                configuration
                        )
                        .isRight();

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("configuration service must return error if delete method is called with invalid configuration")
    void configuration_service_must_return_error_if_delete_method_is_called_with_invalid_configuration() {
        final var result =
                this.connectorService
                        .delete(
                                UUID.randomUUID().toString()
                        )
                        .isLeft();

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("configuration service must return deleted configuration if delete method is called with valid configuration")
    void configuration_service_must_return_deleted_configuration_if_delete_method_is_called_with_valid_configuration() {
        final var id=UUID.randomUUID().toString();
        final var configuration =
                Connector.Builder
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
                                                                false
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.connectorJDBCDatabaseRepository.save(
                new ConnectorDAO(
                        configuration
                )
        );

        final var result =
                this.connectorService
                        .delete(
                                configuration.id()
                        )
                        .isRight();

        Assertions.assertTrue(result);
    }
}