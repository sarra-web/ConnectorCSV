package com.keyrus.proxemconnector.connector.csv.configuration.repository;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ProjectDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Project;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.project.ProjectJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.project.ProjectRepository;
import com.keyrus.proxemconnector.initializer.PostgreSQLInitializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.UUID;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {PostgreSQLInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectRepositoryTest {

    private final ProjectRepository projectRepository;
    private final ProjectJDBCDatabaseRepository projectJDBCDatabaseRepository;

    @Autowired
    ProjectRepositoryTest(
            final ProjectRepository projectRepository,
            final ProjectJDBCDatabaseRepository projectJDBCDatabaseRepository
    ) {
        this.projectRepository = projectRepository;
        this.projectJDBCDatabaseRepository = projectJDBCDatabaseRepository;
    }

    @BeforeAll
    void beforeAll() {
        this.projectJDBCDatabaseRepository.deleteAll();
    }

    @BeforeEach
    void beforeEach() {
        this.projectJDBCDatabaseRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        this.projectJDBCDatabaseRepository.deleteAll();
    }

    @AfterAll
    void afterAll() {
        this.projectJDBCDatabaseRepository.deleteAll();
    }

    @Test
    @DisplayName("project repository must return error if create method is called with project that have id already exist")
    void project_repository_must_return_error_if_create_method_is_called_with_project_that_have_id_already_exist() {
        final var id = UUID.randomUUID().toString();
        final var project1 =
                Project.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withToken("tk")
                        .build()
                        .get();
        final var project2 =
                Project.Builder
                        .builder()
                        .withId(project1.id())
                        .withName(UUID.randomUUID().toString())
                        .withToken("tk")
                        .build()
                        .get();
        this.projectJDBCDatabaseRepository.save(
                new ProjectDAO(
                        project1
                )
        );

        final var result =
                this.projectRepository
                        .create(
                                project2
                        )
                        .getLeft();

        Assertions.assertInstanceOf(ProjectRepository.Error.AlreadyExist.class, result);
    }

    @Test
    @DisplayName("project repository must return error if create method is called with project that have name already exist")
    void project_repository_must_return_error_if_create_method_is_called_with_project_that_have_name_already_exist() {
        final var id = UUID.randomUUID().toString();
        final var project1 =
                Project.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withToken("gg")
                        .build()
                        .get();
        final var id2 = UUID.randomUUID().toString();
        final var project2 =
                Project.Builder
                        .builder()
                        .withId(id2)
                        .withName(project1.name())
                        .withToken("ee")
                        .build()
                        .get();
        this.projectJDBCDatabaseRepository.save(
                new ProjectDAO(
                        project1
                )
        );

        final var result =
                this.projectRepository
                        .create(
                                project2
                        )
                        .getLeft();

        Assertions.assertInstanceOf(ProjectRepository.Error.AlreadyExist.class, result);
    }

    @Test
    @DisplayName("project repository must return created project if create method is called with project that does not exist")
    void project_repository_must_return_created_project_if_create_method_is_called_with_project_that_does_not_exist() {
        final var id = UUID.randomUUID().toString();
        final var project =
                Project.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withToken("ss")
                        .build()
                        .get();

        final var result =
                this.projectRepository
                        .create(
                                project
                        )
                        .get();

        Assertions.assertEquals(project, result);
    }

    @Test
    @DisplayName("project repository must return error if update method is called with project that have id does not exist")
    void project_repository_must_return_error_if_update_method_is_called_with_project_that_have_id_does_not_exist() {
        final var id = UUID.randomUUID().toString();
        final var project =
                Project.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withToken("qq")
                        .build()
                        .get();

        final var result =
                this.projectRepository
                        .update(
                                project
                        )
                        .getLeft();

        Assertions.assertInstanceOf(ProjectRepository.Error.NotFound.class, result);
    }

    @Test
    @DisplayName("project repository must return updated project if update method is called with project that already exist")
    void project_repository_must_return_updated_project_if_update_method_is_called_with_project_that_already_exist() {
        final var id = UUID.randomUUID().toString();
        final var project1 =
                Project.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withToken("ss")
                        .build()
                        .get();
        final var project2 =
                Project.Builder
                        .builder()
                        .withId(project1.id())
                        .withName(UUID.randomUUID().toString())
                        .withToken("zz")
                        .build()
                        .get();
        this.projectJDBCDatabaseRepository.save(
                new ProjectDAO(
                        project1
                )
        );

        final var result =
                this.projectRepository
                        .update(
                                project2
                        )
                        .get();

        Assertions.assertEquals(project2, result);
    }

    @Test
    @DisplayName("project repository must return error if delete method is called with project that have id does not exist")
    void project_repository_must_return_error_if_delete_method_is_called_with_project_that_have_id_does_not_exist() {
        final var result =
                this.projectRepository
                        .delete(
                                UUID.randomUUID().toString()
                        )
                        .getLeft();

        Assertions.assertInstanceOf(ProjectRepository.Error.NotFound.class, result);
    }

    @Test
    @DisplayName("repository must return deleted project if update method is called with project that already exist")
    void repository_must_return_deleted_project_if_update_method_is_called_with_project_that_already_exist() {
        final var id = UUID.randomUUID().toString();
        final var project =
                Project.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withToken("ee")
                        .build()
                        .get();
        this.projectJDBCDatabaseRepository.save(
                new ProjectDAO(
                        project
                )
        );

        final var result =
                this.projectRepository
                        .delete(
                                project.id()
                        )
                        .get();

        Assertions.assertEquals(project, result);
    }
    @Test
    @DisplayName("project repository must return list of Projects if findAll method is called with valid project ")
    void project_repository_must_return_listOf_Projects_if_findAll_method_is_called_with_valid_project() {
        final var id = UUID.randomUUID().toString();
        final var project1 =
                Project.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withToken("ss")
                        .build()
                        .get();
        ;
        this.projectJDBCDatabaseRepository.save(
                new ProjectDAO(
                        project1
                )
        );


        final var result =
                this.projectRepository
                        .findAll().get();


        Assertions.assertEquals(List.of(project1), result);
    }

    @Test
    @DisplayName("project repository must return empty list if findAll method is called with no projects exist")
    void project_repository_must_return_empty_list_if_findAll_method_is_called_with_no_projects_exist() {


        final var result =
                this.projectRepository
                        .findAll().get();
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("project repository must return error if findAll method is called with invalid  saved project ")
    void project_repository_must_return_error_if_findAll_method_is_called_with_invalid_saved_project() {

        this.projectJDBCDatabaseRepository.save(
                new ProjectDAO("1", " ", ";")
        );
        final var result =
                this.projectRepository
                        .findAll().getLeft();
        Assertions.assertInstanceOf(ProjectRepository.Error.class, result);
    }
}