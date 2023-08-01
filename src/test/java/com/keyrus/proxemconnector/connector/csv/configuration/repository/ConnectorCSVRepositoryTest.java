package com.keyrus.proxemconnector.connector.csv.configuration.repository;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.*;
import com.keyrus.proxemconnector.connector.csv.configuration.enumerations.FieldType;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorCSV;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector.CSVConnectorJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector.CSVConnectorRepository;
import com.keyrus.proxemconnector.initializer.PostgreSQLInitializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {PostgreSQLInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConnectorCSVRepositoryTest {

    private final CSVConnectorRepository cSVConnectorRepository;
    private final CSVConnectorJDBCDatabaseRepository cSVConnectorJDBCDatabaseRepository;

    @Autowired
    ConnectorCSVRepositoryTest(
            final CSVConnectorRepository cSVConnectorRepository,
            final CSVConnectorJDBCDatabaseRepository cSVConnectorJDBCDatabaseRepository
    ) {
        this.cSVConnectorRepository = cSVConnectorRepository;
        this.cSVConnectorJDBCDatabaseRepository = cSVConnectorJDBCDatabaseRepository;
    }

    @BeforeAll
    void beforeAll() {
        this.cSVConnectorJDBCDatabaseRepository.deleteAll();
    }

    @BeforeEach
    void beforeEach() {
        this.cSVConnectorJDBCDatabaseRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        this.cSVConnectorJDBCDatabaseRepository.deleteAll();
    }

    @AfterAll
    void afterAll() {
        this.cSVConnectorJDBCDatabaseRepository.deleteAll();
    }

    @Test
    @DisplayName("configuration repository must return error if create method is called with configuration that have id already exist")
    void configuration_repository_must_return_error_if_create_method_is_called_with_configuration_that_have_id_already_exist() {
        final var id = UUID.randomUUID().toString();
        final var configuration1 =
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
                        .withUserId(20L)
                        .withProjectName("")
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
                                                                true, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        final var configuration2 =
                ConnectorCSV.Builder
                        .builder()
                        .withId(configuration1.id())
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withpath(UUID.randomUUID().toString())
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withProjectName("")
                        .withUserId(20L)
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
                                                                FieldType.Text,
                                                                false, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.cSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration1
                )
        );

        final var result =
                this.cSVConnectorRepository
                        .create(
                                configuration2
                        )
                        .getLeft();

        Assertions.assertInstanceOf(CSVConnectorRepository.Error.AlreadyExist.class, result);
    }

    @Test
    @DisplayName("configuration repository must return error if create method is called with configuration that have name already exist")
    void configuration_repository_must_return_error_if_create_method_is_called_with_configuration_that_have_name_already_exist() {
        final var id = UUID.randomUUID().toString();
        final var configuration1 =
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
        final var id2 = UUID.randomUUID().toString();
        final var configuration2 =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id2)
                        .withName(configuration1.name())
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
                                                                id2,
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
        this.cSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration1
                )
        );

        final var result =
                this.cSVConnectorRepository
                        .create(
                                configuration2
                        )
                        .getLeft();

        Assertions.assertInstanceOf(CSVConnectorRepository.Error.AlreadyExist.class, result);
    }

    @Test
    @DisplayName("configuration repository must return created configuration if create method is called with configuration that does not exist")
    void configuration_repository_must_return_created_configuration_if_create_method_is_called_with_configuration_that_does_not_exist() {
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
                                                                false, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();

        final var result =
                this.cSVConnectorRepository
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
                this.cSVConnectorRepository
                        .update(
                                configuration
                        )
                        .getLeft();

        Assertions.assertInstanceOf(CSVConnectorRepository.Error.NotFound.class, result);
    }

    @Test
    @DisplayName("configuration repository must return updated configuration if update method is called with configuration that already exist")
    void configuration_repository_must_return_updated_configuration_if_update_method_is_called_with_configuration_that_already_exist() {
        final var id = UUID.randomUUID().toString();
        final var configuration1 =
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
        final var configuration2 =
                ConnectorCSV.Builder
                        .builder()
                        .withId(configuration1.id())
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
                                                                configuration1.id(),
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
        this.cSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration1
                )
        );

        final var result =
                this.cSVConnectorRepository
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
                this.cSVConnectorRepository
                        .delete(
                                UUID.randomUUID().toString()
                        )
                        .getLeft();

        Assertions.assertInstanceOf(CSVConnectorRepository.Error.NotFound.class, result);
    }

    @Test
    @DisplayName("repository must return deleted configuration if update method is called with configuration that already exist")
    void repository_must_return_deleted_configuration_if_update_method_is_called_with_configuration_that_already_exist() {
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
                                                                false, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        this.cSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration
                )
        );

        final var result =
                this.cSVConnectorRepository
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
                                                                FieldType.Meta,
                                                                false,true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        ;
        this.cSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration1
                )
        );


        final var result =
                this.cSVConnectorRepository
                        .findAll().get();


        Assertions.assertEquals(List.of(configuration1), result);
    }

    @Test
    @DisplayName("configuration repository must return empty list if findAll method is called with no configurations exist")
    void configuration_repository_must_return_empty_list_if_findAll_method_is_called_with_no_configurations_exist() {


        final var result =
                this.cSVConnectorRepository
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
                                        FieldType.Meta,
                                        false,true
                                )
                                .get()
                ).map(field -> new FieldDAO(field))
                .collect(Collectors.toUnmodifiableSet());
        ProjectDAO projectDAO=new ProjectDAO("77","","");
        RoleDAO roleDAO= new RoleDAO(8L, ERole.ROLE_USER);
        UserDAO userDAO =new UserDAO(88L,"","","", Set.of(roleDAO));
        this.cSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO("id","","","","","","",true,fields,projectDAO,userDAO)
        );
        final var result =
                this.cSVConnectorRepository
                        .findAll().getLeft();
        Assertions.assertInstanceOf(CSVConnectorRepository.Error.class, result);
    }
    @Test
    @DisplayName("configuration repository must return error if findOneByName method is called with configuration that have name does not exist")
    void configuration_repository_must_return_error_if_findOneByName_method_is_called_with_configuration_that_have_id_does_not_exist() {
        final var result =
                this.cSVConnectorRepository
                        .findOneByName(
                                UUID.randomUUID().toString()
                        )
                        .getLeft();

        Assertions.assertInstanceOf(CSVConnectorRepository.Error.NotFound.class, result);
    }
    @Test
    @DisplayName("configuration repository must return list of Connectors if findManyByNameContainsIgnoreCase method is called with valid configuration ")
    void configuration_repository_must_return_listOf_connectors_if_findManyByNameContainsIgnoreCase_method_is_called_with_valid_configuration() {
        final var id = UUID.randomUUID().toString();
        final var configuration1 =
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
                                                                FieldType.Meta,
                                                                false,true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        ;
        final var id2 = UUID.randomUUID().toString();
        final var configuration2 =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id2)
                        .withName("actual bills")
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
                                                                id2,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                FieldType.Meta,
                                                                false,true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        final var id3 = UUID.randomUUID().toString();
        final var configuration3 =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id3)
                        .withName("history")
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
                                                                id3,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                FieldType.Meta,
                                                                false,true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();
        ;
        this.cSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration1
                )
        );
        this.cSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration2
                )
        );
        this.cSVConnectorJDBCDatabaseRepository.save(
                new ConnectorCSVDAO(
                        configuration3
                )
        );


        final var result =
                this.cSVConnectorRepository
                        .findManyByNameContainsIgnoreCase("BiLl").get();
        List<ConnectorCSV> connectorCSVS = List.of(configuration1, configuration2);

        Assertions.assertTrue(result.size()==2&&result.containsAll(List.of(configuration1,configuration2)));
    }

}