package com.keyrus.proxemconnector.connector.csv.configuration.rest.handler;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorCSVDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorCSVDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ProjectDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.enumerations.FieldType;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorCSV;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector.CSVConnectorJDBCDatabaseRepository;
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
    private final ConnectorCSVRestHandler connectorCSVRestHandler;
    private final String errorHeader;
    private final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository;

    @Autowired
    ConnectorCSVRestHandlerTest(
            final ConnectorCSVRestHandler connectorCSVRestHandler,
            @Value("${connectors.rest.error-header:error}") final String errorHeader,
            final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository
    ) {
        this.connectorCSVRestHandler = connectorCSVRestHandler;
        this.errorHeader = errorHeader;
        this.CSVConnectorJDBCDatabaseRepository = CSVConnectorJDBCDatabaseRepository;
    }

    @BeforeAll
    void beforeAll() {
        this.CSVConnectorJDBCDatabaseRepository.deleteAll();
    }

    @BeforeEach
    void beforeEach() {
        this.CSVConnectorJDBCDatabaseRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        this.CSVConnectorJDBCDatabaseRepository.deleteAll();
    }

    @AfterAll
    void afterAll() {
        this.CSVConnectorJDBCDatabaseRepository.deleteAll();
    }

    @Test
    @DisplayName("configuration rest handler must return error if create method is called with invalid configuration dto")
    void configuration_rest_handler_must_return_error_if_create_method_is_called_with_invalid_configuration_dto() {
        ProjectDTO project= new ProjectDTO("id","name","prox");
        final var result =
                this.connectorCSVRestHandler
                        .create(
                                new ConnectorCSVDTO(
                                        " ",
                                        " ",
                                        " ",
                                        " ",
                                        " ",
                                        "",
                                        " ",
                                        true,
                                        Collections.emptySet(),
                                        "",88L

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
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
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
                                                                FieldType.Text,
                                                                false, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );

        final var result =
                this.connectorCSVRestHandler
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
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
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
                                                                FieldType.Text,
                                                                false, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();

        final var result =
                this.connectorCSVRestHandler
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
        ProjectDTO project= new ProjectDTO("id","name","prox");
        final var result =
                this.connectorCSVRestHandler
                        .update(
                                new ConnectorCSVDTO( " ",
                                        " ",
                                        " ",
                                        " ",
                                        " ",
                                        "",
                                        " ",
                                        true,
                                        Collections.emptySet(),
                                        "",88L
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
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
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
                                                                FieldType.Text,
                                                                false, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();

        final var result =
                this.connectorCSVRestHandler
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
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
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
                                                                FieldType.Text,
                                                                false, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );

        final var result =
                this.connectorCSVRestHandler
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
                this.connectorCSVRestHandler
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
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
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
                                                                FieldType.Text,
                                                                false, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );

        final var result =
                this.connectorCSVRestHandler
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
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
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
                                                                FieldType.Text,
                                                                false,true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );

        final var result =
                this.connectorCSVRestHandler.findAll(null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );
    }

    @Test
    @DisplayName("configuration rest handler must return an empty list of configurations if findAll method is called with no saved configurations ")
    void configuration_rest_handler_must_return_ampty_List_of_configurations_if_findAll_method_is_called_with_no_saved_configurations() {
        final var result =
                this.connectorCSVRestHandler.findAll(null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );

    }
    @Test
    @DisplayName("configuration rest handler must return error if findOneByName method is called with invalid configuration")
    void configuration_rest_handler_must_return_error_if_findOneByName_method_is_called_with_invalid_configuration() {
        final var result =
                this.connectorCSVRestHandler
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
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
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
                                                                FieldType.Text,
                                                                false,true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );

        final var result =
                this.connectorCSVRestHandler
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
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
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
                                                                FieldType.Text,
                                                                false,true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.CSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );

        final var result =
                this.connectorCSVRestHandler.findManyByNameContainsIgnoreCase("BilL", null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertFalse(result.getHeaders().containsKey(this.errorHeader))
        );
    }
}