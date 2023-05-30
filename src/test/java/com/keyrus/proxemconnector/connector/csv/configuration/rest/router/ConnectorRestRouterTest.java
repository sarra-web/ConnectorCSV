package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Connector;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.ConnectorJDBCDatabaseRepository;
import com.keyrus.proxemconnector.initializer.PostgreSQLInitializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {PostgreSQLInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConnectorRestRouterTest {

    private final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository;
    private final int port;
    private final TestRestTemplate restTemplate;
    private final String baseUrl;
    private final String errorHeader;

    @Autowired
    ConnectorRestRouterTest(
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository,
            @Value(value = "${local.server.port}") final int port,
            final TestRestTemplate restTemplate,
            @Value("${connectors.rest.error-header:error}") final String errorHeader
    ) {
        this.connectorJDBCDatabaseRepository = connectorJDBCDatabaseRepository;
        this.port = port;
        this.restTemplate = restTemplate;
        baseUrl = "http://localhost:" + this.port + "/configuration";
        this.errorHeader = errorHeader;
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
    @DisplayName("configuration rest router must return error if create method is called with configuration that have id already exist")
    void configuration_rest_router_must_return_error_if_create_method_is_called_with_configuration_that_have_id_already_exist() {
        final var id=UUID.randomUUID().toString();
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
                this.restTemplate.postForEntity(
                        this.baseUrl,
                        new ConnectorDTO(configuration2),
                        ConnectorDTO.class
                );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> Assertions.assertTrue(result.getHeaders().containsKey(this.errorHeader))
        );
    }

    @Test
    @DisplayName("configuration rest router must return error if create method is called with configuration that have name already exist")
    void configuration_rest_router_must_return_error_if_create_method_is_called_with_configuration_that_have_name_already_exist() {
        final var id=UUID.randomUUID().toString();
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
        final var id2=UUID.randomUUID().toString();
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
                this.restTemplate.postForEntity(
                        this.baseUrl,
                        new ConnectorDTO(configuration2),
                        ConnectorDTO.class
                );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> Assertions.assertTrue(result.getHeaders().containsKey(this.errorHeader))
        );
    }

    @Test
    @DisplayName("configuration rest router must return created configuration if create method is called with configuration that does not exist")
    void configuration_rest_router_must_return_created_configuration_if_create_method_is_called_with_configuration_that_does_not_exist() {
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
                                                                false, "texte"
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();

        final var result =
                this.restTemplate.postForEntity(
                        this.baseUrl,
                        new ConnectorDTO(configuration),
                        ConnectorDTO.class
                );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );
    }

    @Test
    @DisplayName("configuration rest router must return error if update method is called with configuration that have id does not exist")
    void configuration_rest_router_must_return_error_if_update_method_is_called_with_configuration_that_have_id_does_not_exist() {
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
                                                                false, "texte"
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();

        final var result =
                this.restTemplate.exchange(
                        this.baseUrl,
                        HttpMethod.PUT,
                        new HttpEntity<>(new ConnectorDTO(configuration)),
                        ConnectorDTO.class
                );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> Assertions.assertTrue(result.getHeaders().containsKey(this.errorHeader))
        );
    }

    @Test
    @DisplayName("configuration rest router must return updated configuration if update method is called with configuration that already exist")
    void configuration_rest_router_must_return_updated_configuration_if_update_method_is_called_with_configuration_that_already_exist() {
        final var id=UUID.randomUUID().toString();
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
                this.restTemplate.exchange(
                        this.baseUrl,
                        HttpMethod.PUT,
                        new HttpEntity<>(new ConnectorDTO(configuration2)),
                        ConnectorDTO.class
                );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );
    }

    @Test
    @DisplayName("configuration rest router must return error if delete method is called with configuration that have id does not exist")
    void configuration_rest_router_must_return_error_if_delete_method_is_called_with_configuration_that_have_id_does_not_exist() {
        final var result =
                this.restTemplate.exchange(
                        this.baseUrl + "/" + UUID.randomUUID(),
                        HttpMethod.DELETE,
                        HttpEntity.EMPTY,
                        ConnectorDTO.class
                );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode()),
                () -> Assertions.assertTrue(result.getHeaders().containsKey(this.errorHeader))
        );
    }

    @Test
    @DisplayName("configuration rest router must return deleted configuration if update method is called with configuration that already exist")
    void configuration_rest_router_must_return_deleted_configuration_if_update_method_is_called_with_configuration_that_already_exist() {
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
                this.restTemplate.exchange(
                        this.baseUrl + "/" + configuration.id(),
                        HttpMethod.DELETE,
                        HttpEntity.EMPTY,
                        ConnectorDTO.class
                );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );
    }
    @Test
    @DisplayName("configuration rest router must return a liste of configurations if findAll method is called with valid configurations")
    void configuration_rest_router_must_return_list_of_configurations_if_findAll_method_is_called_with_valid_configurations() {
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
                                                                false,""
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
        final var id2=UUID.randomUUID().toString();
        final var configuration2 =
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
        this.connectorJDBCDatabaseRepository.save(
                new ConnectorDAO(
                        configuration
                )
        );

        final ResponseEntity<Collection<ConnectorDTO>> result = this.restTemplate.exchange(
                this.baseUrl ,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Collection<ConnectorDTO>>() {}
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );
    }
    @Test
    @DisplayName("configuration rest router must return an empty list of configurations if findAll method is called with no saved configurations ")
    void configuration_rest_router_must_return_ampty_List_of_configurations_if_findAll_method_is_called_with_no_saved_configurations() {

        final ResponseEntity<Collection<ConnectorDTO>> result = this.restTemplate.exchange(
                this.baseUrl ,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Collection<ConnectorDTO>>() {}
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader)));



    }
}