package com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorCSVDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorCSV;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.project.ProjectJDBCDatabaseRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ConnectorDatabaseRepository implements ConnectorRepository {

    private static ProjectJDBCDatabaseRepository projectJDBCDatabaseRepository;
    private static ConnectorDatabaseRepository instance = null;

    public static ConnectorDatabaseRepository instance(
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository
    ) {
        if (Objects.isNull(instance))
            instance =
                    new ConnectorDatabaseRepository(
                            connectorJDBCDatabaseRepository
                    );
        return instance;
    }

    private final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository;

    private ConnectorDatabaseRepository(
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository
    ) {
        this.connectorJDBCDatabaseRepository = connectorJDBCDatabaseRepository;
    }
    @Override
    public Either<Error, ConnectorCSV> create(
            final ConnectorCSV connectorCSV
    ) {
        return
                ConnectorDatabaseRepository.checkThenExecute(
                        ConnectorDatabaseRepository.createConfiguration(
                                connectorCSV,
                                this.connectorJDBCDatabaseRepository
                        ),
                        ConnectorDatabaseRepository.checkConfigurationIdDoesNotExist(
                                connectorCSV.id(),
                                this.connectorJDBCDatabaseRepository
                        ),
                        ConnectorDatabaseRepository.checkConfigurationNameDoesNotExist(
                                connectorCSV.name(),
                                this.connectorJDBCDatabaseRepository
                        )
                );
    }

    @Override
    public Either<Error, ConnectorCSV> create2(
            final ConnectorCSV connectorCSV,String projectId
    ) {
        return
                ConnectorDatabaseRepository.checkThenExecute(
                        ConnectorDatabaseRepository.createConfiguration2(
                                connectorCSV,
                                this.connectorJDBCDatabaseRepository,projectId
                        ),
                        ConnectorDatabaseRepository.checkConfigurationIdDoesNotExist(
                                connectorCSV.id(),
                                this.connectorJDBCDatabaseRepository
                        ),
                        ConnectorDatabaseRepository.checkConfigurationNameDoesNotExist(
                                connectorCSV.name(),
                                this.connectorJDBCDatabaseRepository
                        )
                );
    }

    @Override
    public Either<Error, ConnectorCSV> update(
            final ConnectorCSV connectorCSV
    ) {
        return
                ConnectorDatabaseRepository.checkThenExecute(
                        ConnectorDatabaseRepository.updateConfiguration(
                                connectorCSV,
                                this.connectorJDBCDatabaseRepository
                        ),
                        ConnectorDatabaseRepository.checkConfigurationAlreadyExist(
                                connectorCSV.id(),
                                this.connectorJDBCDatabaseRepository
                        )
                );
    }

    @Override
    public Either<Error, ConnectorCSV> delete(
            final String id
    ) {
        return
                ConnectorDatabaseRepository.checkThenExecute(
                        ConnectorDatabaseRepository.deleteConfiguration(
                                id,
                                this.connectorJDBCDatabaseRepository
                        ),
                        ConnectorDatabaseRepository.checkConfigurationAlreadyExist(
                                id,
                                this.connectorJDBCDatabaseRepository
                        )
                );
    }
    @Override
    public Either<Error, Collection<ConnectorCSV>> findAll() {
        return ConnectorDatabaseRepository.findAllConfiguration(this.connectorJDBCDatabaseRepository).get();
    }

    private static Supplier <Either<Error, Collection<ConnectorCSV>>> findAllConfiguration(ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository) {
        return
                ConnectorDatabaseRepository.executeOnRepositoryForManyResult(
                        connectorJDBCDatabaseRepository,
                        it ->
                                it.findAll()

                );

    }

    @Override
    public Either<Error, ConnectorCSV> findOneByName(final String name) {
        return
                ConnectorDatabaseRepository.checkThenExecute(
                        ConnectorDatabaseRepository.findConfigurationByName(
                                name,
                                this.connectorJDBCDatabaseRepository
                        ),
                        ConnectorDatabaseRepository.checkConfigurationNameAlreadyExist(
                                name,
                                this.connectorJDBCDatabaseRepository
                        )
                );

    }
    @Override
    public Either<Error, ConnectorCSV> findOneById(final String id) {
        return
                ConnectorDatabaseRepository.checkThenExecute(
                        ConnectorDatabaseRepository.findConfigurationById(
                                id,
                                this.connectorJDBCDatabaseRepository
                        ),
                        ConnectorDatabaseRepository.checkConfigurationAlreadyExist(
                                id,
                                this.connectorJDBCDatabaseRepository
                        )
                );

    }
    @Override
    public Either<Error, Collection<ConnectorCSV>> findManyByNameContainsIgnoreCase(String name) {
        return ConnectorDatabaseRepository.findAllConfiguration(this.connectorJDBCDatabaseRepository,name).get();
    }

    @Override
    public Page<ConnectorCSVDAO> findAll(Pageable p) {
        return connectorJDBCDatabaseRepository.findAll(p);
    }
    @Override
    public Page<ConnectorCSVDAO> findByNameContaining(String name, Pageable page){
        return connectorJDBCDatabaseRepository.findByNameContaining(name,page);
    }


    private static Supplier <Either<Error, Collection<ConnectorCSV>>> findAllConfiguration(ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository, String name) {
        return
                ConnectorDatabaseRepository.executeOnRepositoryForManyResult(
                        connectorJDBCDatabaseRepository,
                        it ->
                                it.findAll().stream()
                                        .filter(connectorDAO -> connectorDAO.name().toLowerCase().contains(name.toLowerCase())).toList());

    }


    private static Supplier<Either<Error, ConnectorCSV>> findConfigurationById(
            final String id,
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository
    ) {
        return () ->
                ConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                                connectorJDBCDatabaseRepository,
                                it -> it.findOneById(id)
                        )
                        .get()
                        .flatMap(conf ->
                                ConnectorDatabaseRepository.findConfigurationByIdFromRepository(
                                                id,
                                                connectorJDBCDatabaseRepository
                                        )
                                        .get()
                                        .map(Either::<Error, ConnectorCSV>left)
                                        .orElse(Either.right(conf))
                        );
    }

    private static Supplier<Either<Error, ConnectorCSV>> findConfigurationByName(
            final String name,
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository
    ) {
        return () ->
                ConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                                connectorJDBCDatabaseRepository,
                                it -> it.findByName(name)
                        )
                        .get()
                        .flatMap(conf ->
                                ConnectorDatabaseRepository.findConfigurationByNameFromRepository(
                                                name,
                                                connectorJDBCDatabaseRepository
                                        )
                                        .get()
                                        .map(Either::<Error, ConnectorCSV>left)
                                        .orElse(Either.right(conf))
                        );
    }
    private static Supplier<Optional<Error>> findConfigurationByNameFromRepository(final String name, final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository) {
        return () ->
                ConnectorDatabaseRepository.tryOnRepositoryForPossibleIOException(
                        connectorJDBCDatabaseRepository,
                        it -> it.findByName(name)
                );
    }

    private static Supplier<Optional<Error>> findConfigurationByIdFromRepository(final String id, final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository) {
        return () ->
                ConnectorDatabaseRepository.tryOnRepositoryForPossibleIOException(
                        connectorJDBCDatabaseRepository,
                        it -> it.findById(id)
                );
    }
    private static Supplier<Optional<Error>> checkConfigurationNameAlreadyExist(final String name, final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository) {
        return
                ConnectorDatabaseRepository.evaluateOnRepositoryOrError(
                        connectorJDBCDatabaseRepository,
                        it -> it.existsByName(name),
                        Error.NotFound::new
                );
    }
    private static Supplier<Either<Error, Collection<ConnectorCSV>>> executeOnRepositoryForManyResult(
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository,
            final Function<ConnectorJDBCDatabaseRepository, Collection<ConnectorCSVDAO>> operationOnRepositoryForManyResult
    ) {
        return
                () ->
                        ConnectorDatabaseRepository.tryOnRepositoryForResultOrIOException(
                                        connectorJDBCDatabaseRepository,
                                        operationOnRepositoryForManyResult
                                )
                                .flatMap(ConnectorDatabaseRepository.manyConfigurationDAOToManyConfiguration());
    }

    private static  Function<Collection<ConnectorCSVDAO>, Either<Error, Collection<ConnectorCSV>>> manyConfigurationDAOToManyConfiguration() {
        return connectorDAOS -> {
            return connectorDAOS.isEmpty() ? Either.right(new ArrayList<ConnectorCSV>()) :  ConnectorDatabaseRepository.findAllConnectorOrRepError(connectorDAOS);

        };


    }

    private static Either<Error, Collection<ConnectorCSV>> findAllConnectorOrRepError(Collection<ConnectorCSVDAO> connectorCSVDAOS) {
        Stream<Either<Error, ConnectorCSV>> l=  ConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorCSVDAOS).stream();
        return ConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorCSVDAOS).stream().filter(Either::isRight).toList().isEmpty() ? Either.left(ConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorCSVDAOS).stream().filter(Either::isLeft).map(Either::getLeft).findFirst().get()): Either.right(ConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorCSVDAOS).stream().filter(Either::isRight).map(Either::get).collect(Collectors.toList()));
    }

    private static   Collection<Either<Error, ConnectorCSV>> manyConfigurationDAOToManyErrorOrConfiguration(Collection<ConnectorCSVDAO> connectorCSVDAOS) {
        return connectorCSVDAOS.stream().map(connectorDAO -> connectorDAO.toConfiguration()
                .mapLeft(ConnectorDatabaseRepository::configurationErrorsToRespositoryError)).collect(Collectors.toList());

    }
    private static Collection<ConnectorCSV> findValidConnectors(final Collection<Either<Error, ConnectorCSV>> collection) {
        return collection.
                stream()
                .filter(Either::isRight).
                map(Either::get)
                .collect(Collectors.toList());
    }


    private static Supplier<Optional<Error>> checkConfigurationIdDoesNotExist(
            final String id,
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository
    ) {
        return
                ConnectorDatabaseRepository.evaluateOnRepositoryOrError(
                        connectorJDBCDatabaseRepository,
                        Predicate.not(it -> it.existsById(id)),
                        Error.AlreadyExist::new
                );
    }

    private static Supplier<Optional<Error>> checkConfigurationNameDoesNotExist(
            final String name,
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository
    ) {
        return
                ConnectorDatabaseRepository.evaluateOnRepositoryOrError(
                        connectorJDBCDatabaseRepository,
                        Predicate.not(it -> it.existsByName(name)),
                        Error.AlreadyExist::new
                );
    }

    private static Supplier<Optional<Error>> checkConfigurationAlreadyExist(
            final String id,
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository
    ) {
        return
                ConnectorDatabaseRepository.evaluateOnRepositoryOrError(
                        connectorJDBCDatabaseRepository,
                        it -> it.existsById(id),
                        Error.NotFound::new
                );
    }

    private static Supplier<Optional<Error>> evaluateOnRepositoryOrError(
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository,
            final Predicate<ConnectorJDBCDatabaseRepository> evaluation,
            final Supplier<Error> errorIfInvalidCondition
    ) {
        return
                () ->
                        ConnectorDatabaseRepository.tryOnRepositoryForResultOrIOException(
                                        connectorJDBCDatabaseRepository,
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

    private static Supplier<Either<Error, ConnectorCSV>> deleteConfiguration(
            final String id,
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository
    ) {
        return () ->
                ConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                                connectorJDBCDatabaseRepository,
                                it -> it.findOneById(id)
                        )
                        .get()
                        .flatMap(conf ->
                                ConnectorDatabaseRepository.deleteConfigurationFromRepository(
                                                id,
                                                connectorJDBCDatabaseRepository
                                        )
                                        .get()
                                        .map(Either::<Error, ConnectorCSV>left)
                                        .orElse(Either.right(conf))
                        );
    }

    private static Supplier<Optional<Error>> deleteConfigurationFromRepository(String id, ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository) {
        return () ->
                ConnectorDatabaseRepository.tryOnRepositoryForPossibleIOException(
                        connectorJDBCDatabaseRepository,
                        it ->
                                it.deleteById(
                                        id
                                )
                );
    }

    private static Supplier<Either<Error, ConnectorCSV>> updateConfiguration(
            final ConnectorCSV connectorCSV,
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository
    ) {
        return
                ConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                        connectorJDBCDatabaseRepository,
                        it ->
                                it.save(
                                        new ConnectorCSVDAO(
                                                connectorCSV
                                        )
                                )
                );
    }
    private static Supplier<Either<Error, ConnectorCSV>> createConfiguration(
            final ConnectorCSV connectorCSV,
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository
    ) {
        return
                ConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                        connectorJDBCDatabaseRepository,
                        it ->
                                it.save(
                                        new ConnectorCSVDAO(
                                                connectorCSV
                                        )
                                )
                );
    }

    private static Supplier<Either<Error, ConnectorCSV>> createConfiguration2(
            final ConnectorCSV connectorCSV,
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository
            ,final String idProject
    ) {
        ConnectorCSVDAO connectorCSVDAO=new ConnectorCSVDAO(
                connectorCSV);
       //  connectorCSVDAO.setProjectDAO(projectJDBCDatabaseRepository.findOneById(idProject));


        return
                ConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                        connectorJDBCDatabaseRepository,
                        it ->
                                it.save(connectorCSVDAO

                                        )
                                );

    }

    private static Supplier<Either<Error, ConnectorCSV>> executeOnRepositoryForSingleResult(
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository,
            final Function<ConnectorJDBCDatabaseRepository, ConnectorCSVDAO> operationOnRepositoryForSingleResult
    ) {
        return
                () ->
                        ConnectorDatabaseRepository.tryOnRepositoryForResultOrIOException(
                                        connectorJDBCDatabaseRepository,
                                        operationOnRepositoryForSingleResult
                                )
                                .flatMap(ConnectorDatabaseRepository.configurationDAOToConfiguration());
    }

    private static Function<ConnectorCSVDAO, Either<Error, ConnectorCSV>> configurationDAOToConfiguration() {
        return
                configurationDAO ->
                        configurationDAO.toConfiguration()
                                .mapLeft(ConnectorDatabaseRepository::configurationErrorsToRespositoryError);
    }

    private static Error configurationErrorsToRespositoryError(
            final Collection<ConnectorCSV.Error> configurationErrors
    ) {
        return
                new Error.IO(
                        configurationErrors.stream()
                                .map(ConnectorCSV.Error::message)
                                .collect(Collectors.joining(", "))
                );
    }

    private static <RESULT> Either<Error, RESULT> tryOnRepositoryForResultOrIOException(
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository,
            final Function<ConnectorJDBCDatabaseRepository, RESULT> actionOnRepositoryForResult
    ) {
        return
                Try.of(() -> connectorJDBCDatabaseRepository)
                        .mapTry(actionOnRepositoryForResult::apply)
                        .toEither()
                        .mapLeft(Throwable::getMessage)
                        .mapLeft(Error.IO::new);
    }

    private static Optional<Error> tryOnRepositoryForPossibleIOException(
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository,
            final Consumer<ConnectorJDBCDatabaseRepository> actionOnRepository
    ) {
        try {
            actionOnRepository.accept(connectorJDBCDatabaseRepository);
            return Optional.empty();
        } catch (Exception exception) {
            return
                    Optional.of(
                            new Error.IO(
                                    exception.getMessage()
                            )
                    );
        }
    }

    @SafeVarargs
    private static <RESULT> Either<Error, RESULT> checkThenExecute(
            final Supplier<Either<Error, RESULT>> action,
            final Supplier<Optional<Error>>... checks
    ) {
        return
                Arrays.stream(checks)
                        .filter(Objects::nonNull)
                        .map(Supplier::get)
                        .filter(Objects::nonNull)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findFirst()
                        .map(Either::<Error, RESULT>left)
                        .orElseGet(action);
    }
}
