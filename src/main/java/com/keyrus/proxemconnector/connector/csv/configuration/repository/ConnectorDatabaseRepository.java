package com.keyrus.proxemconnector.connector.csv.configuration.repository;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Connector;
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
    public Either<Error, Connector> create(
            final Connector connector
    ) {
        return
                ConnectorDatabaseRepository.checkThenExecute(
                        ConnectorDatabaseRepository.createConfiguration(
                                connector,
                                this.connectorJDBCDatabaseRepository
                        ),
                        ConnectorDatabaseRepository.checkConfigurationIdDoesNotExist(
                                connector.id(),
                                this.connectorJDBCDatabaseRepository
                        ),
                        ConnectorDatabaseRepository.checkConfigurationNameDoesNotExist(
                                connector.name(),
                                this.connectorJDBCDatabaseRepository
                        )
                );
    }

    @Override
    public Either<Error, Connector> update(
            final Connector connector
    ) {
        return
                ConnectorDatabaseRepository.checkThenExecute(
                        ConnectorDatabaseRepository.updateConfiguration(
                                connector,
                                this.connectorJDBCDatabaseRepository
                        ),
                        ConnectorDatabaseRepository.checkConfigurationAlreadyExist(
                                connector.id(),
                                this.connectorJDBCDatabaseRepository
                        )
                );
    }

    @Override
    public Either<Error, Connector> delete(
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
    public Either<Error, Collection<Connector>> findAll() {
        return ConnectorDatabaseRepository.findAllConfiguration(this.connectorJDBCDatabaseRepository).get();
    }

    private static Supplier <Either<Error, Collection<Connector>>> findAllConfiguration(ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository) {
        return
                ConnectorDatabaseRepository.executeOnRepositoryForManyResult(
                        connectorJDBCDatabaseRepository,
                        it ->
                                it.findAll()

                );

    }

    @Override
    public Either<Error, Connector> findOneByName(final String name) {
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
    public Either<Error, Connector> findOneById(final String id) {
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
    public Either<Error, Collection<Connector>> findManyByNameContainsIgnoreCase(String name) {
        return ConnectorDatabaseRepository.findAllConfiguration(this.connectorJDBCDatabaseRepository,name).get();
    }

    @Override
    public Page<ConnectorDAO> findAll(Pageable p) {
        return connectorJDBCDatabaseRepository.findAll(p);
    }
    @Override
    public Page<ConnectorDAO> findByNameContaining(String name, Pageable page){
        return connectorJDBCDatabaseRepository.findByNameContaining(name,page);
    }


    private static Supplier <Either<Error, Collection<Connector>>> findAllConfiguration(ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository,String name) {
        return
                ConnectorDatabaseRepository.executeOnRepositoryForManyResult(
                        connectorJDBCDatabaseRepository,
                        it ->
                                it.findAll().stream()
                                        .filter(connectorDAO -> connectorDAO.name().toLowerCase().contains(name.toLowerCase())).toList());

    }


    private static Supplier<Either<Error, Connector>> findConfigurationById(
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
                                        .map(Either::<Error, Connector>left)
                                        .orElse(Either.right(conf))
                        );
    }

    private static Supplier<Either<Error, Connector>> findConfigurationByName(
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
                                        .map(Either::<Error, Connector>left)
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
    private static Supplier<Either<Error, Collection<Connector>>> executeOnRepositoryForManyResult(
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository,
            final Function<ConnectorJDBCDatabaseRepository, Collection<ConnectorDAO>> operationOnRepositoryForManyResult
    ) {
        return
                () ->
                        ConnectorDatabaseRepository.tryOnRepositoryForResultOrIOException(
                                        connectorJDBCDatabaseRepository,
                                        operationOnRepositoryForManyResult
                                )
                                .flatMap(ConnectorDatabaseRepository.manyConfigurationDAOToManyConfiguration());
    }

    private static  Function<Collection<ConnectorDAO>, Either<Error, Collection<Connector>>> manyConfigurationDAOToManyConfiguration() {
        return connectorDAOS -> {
            return connectorDAOS.isEmpty() ? Either.right(new ArrayList<Connector>()) :  ConnectorDatabaseRepository.findAllConnectorOrRepError(connectorDAOS);

        };


    }

    private static Either<Error, Collection<Connector>> findAllConnectorOrRepError(Collection<ConnectorDAO> connectorDAOS) {
        Stream<Either<Error, Connector>> l=  ConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorDAOS).stream();
        return ConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorDAOS).stream().filter(Either::isRight).toList().isEmpty() ? Either.left(ConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorDAOS).stream().filter(Either::isLeft).map(Either::getLeft).findFirst().get()): Either.right(ConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorDAOS).stream().filter(Either::isRight).map(Either::get).collect(Collectors.toList()));
    }

    private static   Collection<Either<Error, Connector>> manyConfigurationDAOToManyErrorOrConfiguration(Collection<ConnectorDAO> connectorDAOS) {
        return connectorDAOS.stream().map(connectorDAO -> connectorDAO.toConfiguration()
                .mapLeft(ConnectorDatabaseRepository::configurationErrorsToRespositoryError)).collect(Collectors.toList());

    }
    private static Collection<Connector> findValidConnectors(final Collection<Either<Error, Connector>> collection) {
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

    private static Supplier<Either<Error, Connector>> deleteConfiguration(
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
                                        .map(Either::<Error, Connector>left)
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

    private static Supplier<Either<Error, Connector>> updateConfiguration(
            final Connector connector,
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository
    ) {
        return
                ConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                        connectorJDBCDatabaseRepository,
                        it ->
                                it.save(
                                        new ConnectorDAO(
                                                connector
                                        )
                                )
                );
    }

    private static Supplier<Either<Error, Connector>> createConfiguration(
            final Connector connector,
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository
    ) {
        return
                ConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                        connectorJDBCDatabaseRepository,
                        it ->
                                it.save(
                                        new ConnectorDAO(
                                                connector
                                        )
                                )
                );
    }

    private static Supplier<Either<Error, Connector>> executeOnRepositoryForSingleResult(
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository,
            final Function<ConnectorJDBCDatabaseRepository, ConnectorDAO> operationOnRepositoryForSingleResult
    ) {
        return
                () ->
                        ConnectorDatabaseRepository.tryOnRepositoryForResultOrIOException(
                                        connectorJDBCDatabaseRepository,
                                        operationOnRepositoryForSingleResult
                                )
                                .flatMap(ConnectorDatabaseRepository.configurationDAOToConfiguration());
    }

    private static Function<ConnectorDAO, Either<Error, Connector>> configurationDAOToConfiguration() {
        return
                configurationDAO ->
                        configurationDAO.toConfiguration()
                                .mapLeft(ConnectorDatabaseRepository::configurationErrorsToRespositoryError);
    }

    private static Error configurationErrorsToRespositoryError(
            final Collection<Connector.Error> configurationErrors
    ) {
        return
                new Error.IO(
                        configurationErrors.stream()
                                .map(Connector.Error::message)
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
