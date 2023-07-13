package com.keyrus.proxemconnector.connector.csv.configuration.repository.project;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ProjectDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Project;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProjectDatabaseRepository implements ProjectRepository{


    private static ProjectDatabaseRepository instance = null;

    public static ProjectDatabaseRepository instance(
            final ProjectJDBCDatabaseRepository ProjectJDBCDatabaseRepository
    ) {
        if (Objects.isNull(instance))
            instance =
                    new ProjectDatabaseRepository(
                            ProjectJDBCDatabaseRepository
                    );
        return instance;
    }

    private final ProjectJDBCDatabaseRepository projectJDBCDatabaseRepository;

    private ProjectDatabaseRepository(
            final ProjectJDBCDatabaseRepository projectJDBCDatabaseRepository
    ) {
        this.projectJDBCDatabaseRepository = projectJDBCDatabaseRepository;
    }

    @Override
    public Either<ProjectRepository.Error, Project> create(
            final Project project
    ) {
        return
                ProjectDatabaseRepository.checkThenExecute(
                        ProjectDatabaseRepository.createProject(
                                project,
                                this.projectJDBCDatabaseRepository
                        ),
                        ProjectDatabaseRepository.checkProjectIdDoesNotExist(
                                project.id(),
                                this.projectJDBCDatabaseRepository
                        ),
                        ProjectDatabaseRepository.checkProjectNameDoesNotExist(
                                project.name(),
                                this.projectJDBCDatabaseRepository
                        )
                );
    }

    @Override
    public Either<ProjectRepository.Error, Project> update(
            final Project project
    ) {
        return
                ProjectDatabaseRepository.checkThenExecute(
                        ProjectDatabaseRepository.updateProject(
                                project,
                                this.projectJDBCDatabaseRepository
                        ),
                        ProjectDatabaseRepository.checkProjectAlreadyExist(
                                project.id(),
                                this.projectJDBCDatabaseRepository
                        )
                );
    }

    @Override
    public Either<ProjectRepository.Error, Project> delete(
            final String id
    ) {
        return
                ProjectDatabaseRepository.checkThenExecute(
                        ProjectDatabaseRepository.deleteProject(
                                id,
                                this.projectJDBCDatabaseRepository
                        ),
                        ProjectDatabaseRepository.checkProjectAlreadyExist(
                                id,
                                this.projectJDBCDatabaseRepository
                        )
                );
    }
    @Override
    public Either<ProjectRepository.Error, Collection<Project>> findAll() {
        return ProjectDatabaseRepository.findAllProjects(this.projectJDBCDatabaseRepository).get();
    }
    @Override
    public Either<ProjectRepository.Error, Project> findOneByName(final String name) {
        return
                ProjectDatabaseRepository.checkThenExecute(
                        ProjectDatabaseRepository.findProjectByName(
                                name,
                                this.projectJDBCDatabaseRepository
                        ),
                        ProjectDatabaseRepository.checkProjectNameAlreadyExist(
                                name,
                                this.projectJDBCDatabaseRepository
                        )
                );

    }
    private static Supplier<Optional<ProjectRepository.Error>> checkProjectNameAlreadyExist(final String name, final ProjectJDBCDatabaseRepository projectJDBCDatabaseRepository) {
        return
               ProjectDatabaseRepository.evaluateOnRepositoryOrError(
                       projectJDBCDatabaseRepository,
                        it -> it.existsByName(name),
                       ProjectRepository.Error.NotFound::new
                );
    }
    private static Supplier<Either<ProjectRepository.Error, Project>> findProjectByName(
            final String name,
            final ProjectJDBCDatabaseRepository projectJDBCDatabaseRepository
    ) {
        return () ->
                ProjectDatabaseRepository.executeOnRepositoryForSingleResult(
                                projectJDBCDatabaseRepository,
                                it -> it.findByName(name)
                        )
                        .get()
                        .flatMap(conf ->
                                ProjectDatabaseRepository.findProjectByNameFromRepository(
                                                name,
                                                projectJDBCDatabaseRepository
                                        )
                                        .get()
                                        .map(Either::<ProjectRepository.Error, Project>left)
                                        .orElse(Either.right(conf))
                        );
    }
    private static Supplier<Optional<ProjectRepository.Error>> findProjectByNameFromRepository(final String name, final ProjectJDBCDatabaseRepository projectJDBCDatabaseRepository) {
        return () ->
                ProjectDatabaseRepository.tryOnRepositoryForPossibleIOException(
                        projectJDBCDatabaseRepository,
                        it -> it.findByName(name)
                );
    }
    @Override
    public Page<ProjectDAO> findAll(Pageable p) {
        return projectJDBCDatabaseRepository.findAll(p);
    }
    @Override
    public Page<ProjectDAO> findByNameContaining(String name, Pageable page){
        return projectJDBCDatabaseRepository.findByNameContaining(name,page);
    };
    private static Supplier<Either<ProjectRepository.Error, Collection<Project>>> findAllProjects(ProjectJDBCDatabaseRepository projectJDBCDatabaseRepository) {
        return
                ProjectDatabaseRepository.executeOnRepositoryForManyResult(
                        projectJDBCDatabaseRepository,
                        ListCrudRepository::findAll

                );

    }

    private static Supplier<Either<ProjectRepository.Error, Collection<Project>>> executeOnRepositoryForManyResult(
            final ProjectJDBCDatabaseRepository ProjectJDBCDatabaseRepository,
            final Function<ProjectJDBCDatabaseRepository, Collection<ProjectDAO>> operationOnRepositoryForManyResult
    ) {
        return
                () ->
                        ProjectDatabaseRepository.tryOnRepositoryForResultOrIOException(
                                        ProjectJDBCDatabaseRepository,
                                        operationOnRepositoryForManyResult
                                )
                                .flatMap(ProjectDatabaseRepository.manyProjectDAOToManyProject());
    }

    private static  Function<Collection<ProjectDAO>, Either<ProjectRepository.Error, Collection<Project>>> manyProjectDAOToManyProject() {
        return ProjectDAOS -> {
            return ProjectDAOS.isEmpty() ? Either.right(new ArrayList<Project>()) :  ProjectDatabaseRepository.findAllProjectOrRepError(ProjectDAOS);

        };


    }

    private static Either<ProjectRepository.Error, Collection<Project>> findAllProjectOrRepError(Collection<ProjectDAO> ProjectDAOS) {
        Stream<Either<ProjectRepository.Error, Project>> l=  ProjectDatabaseRepository.manyProjectDAOToManyErrorOrProject(ProjectDAOS).stream();
        return ProjectDatabaseRepository.manyProjectDAOToManyErrorOrProject(ProjectDAOS).stream().filter(Either::isRight).toList().isEmpty() ? Either.left(ProjectDatabaseRepository.manyProjectDAOToManyErrorOrProject(ProjectDAOS).stream().filter(Either::isLeft).map(Either::getLeft).findFirst().get()): Either.right(ProjectDatabaseRepository.manyProjectDAOToManyErrorOrProject(ProjectDAOS).stream().filter(Either::isRight).map(Either::get).collect(Collectors.toList()));
    }

    private static   Collection<Either<ProjectRepository.Error, Project>> manyProjectDAOToManyErrorOrProject(Collection<ProjectDAO> ProjectDAOS) {
        return ProjectDAOS.stream().map(ProjectDAO -> ProjectDAO.toProject()
                .mapLeft(ProjectDatabaseRepository::ProjectErrorsToRespositoryError)).collect(Collectors.toList());

    }
    private static Collection<Project> findValidProjects(final Collection<Either<ProjectRepository.Error, Project>> collection) {
        return collection.
                stream()
                .filter(Either::isRight).
                map(Either::get)
                .collect(Collectors.toList());
    }


    private static Supplier<Optional<ProjectRepository.Error>> checkProjectIdDoesNotExist(
            final String id,
            final ProjectJDBCDatabaseRepository ProjectJDBCDatabaseRepository
    ) {
        return
                ProjectDatabaseRepository.evaluateOnRepositoryOrError(
                        ProjectJDBCDatabaseRepository,
                        Predicate.not(it -> it.existsById(id)),
                        ProjectRepository.Error.AlreadyExist::new
                );
    }

    private static Supplier<Optional<ProjectRepository.Error>> checkProjectNameDoesNotExist(
            final String name,
            final ProjectJDBCDatabaseRepository ProjectJDBCDatabaseRepository
    ) {
        return
                ProjectDatabaseRepository.evaluateOnRepositoryOrError(
                        ProjectJDBCDatabaseRepository,
                        Predicate.not(it -> it.existsByName(name)),
                        ProjectRepository.Error.AlreadyExist::new
                );
    }

    private static Supplier<Optional<ProjectRepository.Error>> checkProjectAlreadyExist(
            final String id,
            final ProjectJDBCDatabaseRepository ProjectJDBCDatabaseRepository
    ) {
        return
                ProjectDatabaseRepository.evaluateOnRepositoryOrError(
                        ProjectJDBCDatabaseRepository,
                        it -> it.existsById(id),
                        ProjectRepository.Error.NotFound::new
                );
    }

    private static Supplier<Optional<ProjectRepository.Error>> evaluateOnRepositoryOrError(
            final ProjectJDBCDatabaseRepository ProjectJDBCDatabaseRepository,
            final Predicate<ProjectJDBCDatabaseRepository> evaluation,
            final Supplier<ProjectRepository.Error> errorIfInvalidCondition
    ) {
        return
                () ->
                        ProjectDatabaseRepository.tryOnRepositoryForResultOrIOException(
                                        ProjectJDBCDatabaseRepository,
                                        evaluation::test
                                )
                                .filterOrElse(
                                        it -> it,
                                        __ -> errorIfInvalidCondition.get()
                                )
                                .fold(
                                        Optional::of,
                                        __ -> Optional.empty()
                                );
    }

    private static Supplier<Either<ProjectRepository.Error, Project>> deleteProject(
            final String id,
            final ProjectJDBCDatabaseRepository ProjectJDBCDatabaseRepository
    ) {
        return () ->
                ProjectDatabaseRepository.executeOnRepositoryForSingleResult(
                                ProjectJDBCDatabaseRepository,
                                it -> it.findOneById(id)
                        )
                        .get()
                        .flatMap(conf ->
                                ProjectDatabaseRepository.deleteProjectFromRepository(
                                                id,
                                                ProjectJDBCDatabaseRepository
                                        )
                                        .get()
                                        .map(Either::<ProjectRepository.Error, Project>left)
                                        .orElse(Either.right(conf))
                        );
    }

    private static Supplier<Optional<ProjectRepository.Error>> deleteProjectFromRepository(String id, ProjectJDBCDatabaseRepository ProjectJDBCDatabaseRepository) {
        return () ->
                ProjectDatabaseRepository.tryOnRepositoryForPossibleIOException(
                        ProjectJDBCDatabaseRepository,
                        it ->
                                it.deleteById(
                                        id
                                )
                );
    }

    private static Supplier<Either<ProjectRepository.Error, Project>> updateProject(
            final Project project,
            final ProjectJDBCDatabaseRepository ProjectJDBCDatabaseRepository
    ) {
        return
                ProjectDatabaseRepository.executeOnRepositoryForSingleResult(
                        ProjectJDBCDatabaseRepository,
                        it ->
                                it.save(
                                        new ProjectDAO(
                                                project
                                        )
                                )
                );
    }

    private static Supplier<Either<ProjectRepository.Error, Project>> createProject(
            final Project Project,
            final ProjectJDBCDatabaseRepository ProjectJDBCDatabaseRepository
    ) {
        return
                ProjectDatabaseRepository.executeOnRepositoryForSingleResult(
                        ProjectJDBCDatabaseRepository,
                        it ->
                                it.save(
                                        new ProjectDAO(
                                                Project
                                        )
                                )
                );
    }

    private static Supplier<Either<ProjectRepository.Error, Project>> executeOnRepositoryForSingleResult(
            final ProjectJDBCDatabaseRepository ProjectJDBCDatabaseRepository,
            final Function<ProjectJDBCDatabaseRepository, ProjectDAO> operationOnRepositoryForSingleResult
    ) {
        return
                () ->
                        ProjectDatabaseRepository.tryOnRepositoryForResultOrIOException(
                                        ProjectJDBCDatabaseRepository,
                                        operationOnRepositoryForSingleResult
                                )
                                .flatMap(ProjectDatabaseRepository.ProjectDAOToProject());
    }

    private static Function<ProjectDAO, Either<ProjectRepository.Error, Project>> ProjectDAOToProject() {
        return
                ProjectDAO ->
                        ProjectDAO.toProject()
                                .mapLeft(ProjectDatabaseRepository::ProjectErrorsToRespositoryError);
    }

    private static ProjectRepository.Error ProjectErrorsToRespositoryError(
            final Collection<Project.Error> ProjectErrors
    ) {
        return
                new ProjectRepository.Error.IO(
                        ProjectErrors.stream()
                                .map(Project.Error::message)
                                .collect(Collectors.joining(", "))
                );
    }

    private static <RESULT> Either<ProjectRepository.Error, RESULT> tryOnRepositoryForResultOrIOException(
            final ProjectJDBCDatabaseRepository ProjectJDBCDatabaseRepository,
            final Function<ProjectJDBCDatabaseRepository, RESULT> actionOnRepositoryForResult
    ) {
        return
                Try.of(() -> ProjectJDBCDatabaseRepository)
                        .mapTry(actionOnRepositoryForResult::apply)
                        .toEither()
                        .mapLeft(Throwable::getMessage)
                        .mapLeft(ProjectRepository.Error.IO::new);
    }

    private static Optional<ProjectRepository.Error> tryOnRepositoryForPossibleIOException(
            final ProjectJDBCDatabaseRepository ProjectJDBCDatabaseRepository,
            final Consumer<ProjectJDBCDatabaseRepository> actionOnRepository
    ) {
        try {
            actionOnRepository.accept(ProjectJDBCDatabaseRepository);
            return Optional.empty();
        } catch (Exception exception) {
            return
                    Optional.of(
                            new ProjectRepository.Error.IO(
                                    exception.getMessage()
                            )
                    );
        }
    }

    @SafeVarargs
    private static <RESULT> Either<ProjectRepository.Error, RESULT> checkThenExecute(
            final Supplier<Either<ProjectRepository.Error, RESULT>> action,
            final Supplier<Optional<ProjectRepository.Error>>... checks
    ) {
        return
                Arrays.stream(checks)
                        .filter(Objects::nonNull)
                        .map(Supplier::get)
                        .filter(Objects::nonNull)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findFirst()
                        .map(Either::<ProjectRepository.Error, RESULT>left)
                        .orElseGet(action);
    }
}
