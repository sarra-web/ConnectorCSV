package com.keyrus.proxemconnector.connector.csv.configuration.repository;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.FieldDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Connector;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import com.keyrus.proxemconnector.initializer.PostgreSQLInitializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {PostgreSQLInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConnectorRepositoryTest {

    private final ConnectorRepository connectorRepository;
    private final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository;

    @Autowired
    ConnectorRepositoryTest(
            final ConnectorRepository connectorRepository,
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository
    ) {
        this.connectorRepository = connectorRepository;
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
    @DisplayName("configuration repository must return error if create method is called with configuration that have id already exist")
    void configuration_repository_must_return_error_if_create_method_is_called_with_configuration_that_have_id_already_exist() {
        final var id = UUID.randomUUID().toString();
        final var configuration1 =
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
                                                                false, "texte"
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        final var configuration2 =
                Connector.Builder
                        .builder()
                        .withId(configuration1.id())
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
                                                                configuration1.id(),
                                                                UUID.randomUUID().toString(),
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
                new ConnectorDAO(
                        configuration1
                )
        );

        final var result =
                this.connectorRepository
                        .create(
                                configuration2
                        )
                        .getLeft();

        Assertions.assertInstanceOf(ConnectorRepository.Error.AlreadyExist.class, result);
    }

    @Test
    @DisplayName("configuration repository must return error if create method is called with configuration that have name already exist")
    void configuration_repository_must_return_error_if_create_method_is_called_with_configuration_that_have_name_already_exist() {
        final var id = UUID.randomUUID().toString();
        final var configuration1 =
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
                                                                false, "texte"
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        final var id2 = UUID.randomUUID().toString();
        final var configuration2 =
                Connector.Builder
                        .builder()
                        .withId(id2)
                        .withName(configuration1.name())
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
                                                                id2,
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
                new ConnectorDAO(
                        configuration1
                )
        );

        final var result =
                this.connectorRepository
                        .create(
                                configuration2
                        )
                        .getLeft();

        Assertions.assertInstanceOf(ConnectorRepository.Error.AlreadyExist.class, result);
    }

    @Test
    @DisplayName("configuration repository must return created configuration if create method is called with configuration that does not exist")
    void configuration_repository_must_return_created_configuration_if_create_method_is_called_with_configuration_that_does_not_exist() {
        final var id = UUID.randomUUID().toString();
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
                                                                false, "texte"
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();

        final var result =
                this.connectorRepository
                        .create(
                                configuration
                        )
                        .get();

        Assertions.assertEquals(configuration, result);
    }

    @Test
    @DisplayName("configuration repository must return error if update method is called with configuration that have id does not exist")
    void configuration_repository_must_return_error_if_update_method_is_called_with_configuration_that_have_id_does_not_exist() {
        final var id = UUID.randomUUID().toString();
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
                                                                false, "texte"
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();

        final var result =
                this.connectorRepository
                        .update(
                                configuration
                        )
                        .getLeft();

        Assertions.assertInstanceOf(ConnectorRepository.Error.NotFound.class, result);
    }

    @Test
    @DisplayName("configuration repository must return updated configuration if update method is called with configuration that already exist")
    void configuration_repository_must_return_updated_configuration_if_update_method_is_called_with_configuration_that_already_exist() {
        final var id = UUID.randomUUID().toString();
        final var configuration1 =
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
                                                                false, "texte"
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        final var configuration2 =
                Connector.Builder
                        .builder()
                        .withId(configuration1.id())
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
                                                                configuration1.id(),
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
                new ConnectorDAO(
                        configuration1
                )
        );

        final var result =
                this.connectorRepository
                        .update(
                                configuration2
                        )
                        .get();

        Assertions.assertEquals(configuration2, result);
    }

    @Test
    @DisplayName("configuration repository must return error if delete method is called with configuration that have id does not exist")
    void configuration_repository_must_return_error_if_delete_method_is_called_with_configuration_that_have_id_does_not_exist() {
        final var result =
                this.connectorRepository
                        .delete(
                                UUID.randomUUID().toString()
                        )
                        .getLeft();

        Assertions.assertInstanceOf(ConnectorRepository.Error.NotFound.class, result);
    }

    @Test
    @DisplayName("repository must return deleted configuration if update method is called with configuration that already exist")
    void repository_must_return_deleted_configuration_if_update_method_is_called_with_configuration_that_already_exist() {
        final var id = UUID.randomUUID().toString();
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
                                                                false, "texte"
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
                this.connectorRepository
                        .delete(
                                configuration.id()
                        )
                        .get();

        Assertions.assertEquals(configuration, result);
    }
    @Test
    @DisplayName("configuration repository must return list of Connectors if findAll method is called with valid configuration ")
    void configuration_repository_must_return_listOf_connectors_if_findAll_method_is_called_with_valid_configuration() {
        final var id = UUID.randomUUID().toString();
        final var configuration1 =
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
                                                                false,""
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        ;
        this.connectorJDBCDatabaseRepository.save(
                new ConnectorDAO(
                        configuration1
                )
        );


        final var result =
                this.connectorRepository
                        .findAll().get();


        Assertions.assertEquals(List.of(configuration1), result);
    }

    @Test
    @DisplayName("configuration repository must return empty list if findAll method is called with no configurations exist")
    void configuration_repository_must_return_empty_list_if_findAll_method_is_called_with_no_configurations_exist() {


        final var result =
                this.connectorRepository
                        .findAll().get();
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("configuration repository must return error if findAll method is called with invalid  saved configuration ")
    void configuration_repository_must_return_error_if_findAll_method_is_called_with_invalid_saved_configuration() {
        Collection<FieldDAO> fields = IntStream.iterate(1, it -> it + 1)
                .limit(10)
                .mapToObj(it ->
                        Field.of(
                                        UUID.randomUUID().toString(),
                                        "1",
                                        UUID.randomUUID().toString(),
                                        it,
                                        UUID.randomUUID().toString(),
                                        true,
                                        false,"meta"
                                )
                                .get()
                ).map(field -> new FieldDAO(field))
                .collect(Collectors.toUnmodifiableSet());
        this.connectorJDBCDatabaseRepository.save(
                new ConnectorDAO("1", " ", ";", "ee", "dd", "dd", "dd", true, fields)
        );
        final var result =
                this.connectorRepository
                        .findAll().getLeft();
        Assertions.assertInstanceOf(ConnectorRepository.Error.class, result);
    }
}