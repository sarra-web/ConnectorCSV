package com.keyrus.proxemconnector.connector.csv.configuration.repository.jdbcConnector;


import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorJDBCDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC;
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

public final class JDBCJDBCConnectorDatabaseRepository implements JDBCConnectorRepository {
    private static JDBCJDBCConnectorDatabaseRepository instance = null;

    public static JDBCJDBCConnectorDatabaseRepository instance(
            final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository
    ) {
        if (Objects.isNull(instance))
            instance =
                    new JDBCJDBCConnectorDatabaseRepository(
                            JDBCConnectorJDBCDatabaseRepository
                    );
        return instance;
    }

    private final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository;

    private JDBCJDBCConnectorDatabaseRepository(
            final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository
    ) {
        this.JDBCConnectorJDBCDatabaseRepository = JDBCConnectorJDBCDatabaseRepository;
    }
    @Override
    public Either<Error, ConnectorJDBC> create(
            final ConnectorJDBC connectorJDBC
    ) {
        return
                JDBCJDBCConnectorDatabaseRepository.checkThenExecute(
                        JDBCJDBCConnectorDatabaseRepository.createConfiguration(
                                connectorJDBC,
                                this.JDBCConnectorJDBCDatabaseRepository
                        ),
                        JDBCJDBCConnectorDatabaseRepository.checkConfigurationIdDoesNotExist(
                                connectorJDBC.id(),
                                this.JDBCConnectorJDBCDatabaseRepository
                        ),
                        JDBCJDBCConnectorDatabaseRepository.checkConfigurationNameDoesNotExist(
                                connectorJDBC.name(),
                                this.JDBCConnectorJDBCDatabaseRepository
                        )
                );
    }



    @Override
    public Either<Error, ConnectorJDBC> update(
            final ConnectorJDBC connectorJDBC
    ) {
        return
                JDBCJDBCConnectorDatabaseRepository.checkThenExecute(
                        JDBCJDBCConnectorDatabaseRepository.updateConfiguration(
                                connectorJDBC,
                                this.JDBCConnectorJDBCDatabaseRepository
                        ),
                        JDBCJDBCConnectorDatabaseRepository.checkConfigurationAlreadyExist(
                                connectorJDBC.id(),
                                this.JDBCConnectorJDBCDatabaseRepository
                        )
                );
    }

    @Override
    public Either<Error, ConnectorJDBC> delete(
            final String id
    ) {
        return
                JDBCJDBCConnectorDatabaseRepository.checkThenExecute(
                        JDBCJDBCConnectorDatabaseRepository.deleteConfiguration(
                                id,
                                this.JDBCConnectorJDBCDatabaseRepository
                        ),
                        JDBCJDBCConnectorDatabaseRepository.checkConfigurationAlreadyExist(
                                id,
                                this.JDBCConnectorJDBCDatabaseRepository
                        )
                );
    }
    @Override
    public Either<Error, Collection<ConnectorJDBC>> findAll() {
        return JDBCJDBCConnectorDatabaseRepository.findAllConfiguration(this.JDBCConnectorJDBCDatabaseRepository).get();
    }

    private static Supplier <Either<Error, Collection<ConnectorJDBC>>> findAllConfiguration(JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository) {
        return
                JDBCJDBCConnectorDatabaseRepository.executeOnRepositoryForManyResult(
                        JDBCConnectorJDBCDatabaseRepository,
                        it ->
                                it.findAll()

                );

    }

    @Override
    public Either<Error, ConnectorJDBC> findOneByName(final String name) {
        return
                JDBCJDBCConnectorDatabaseRepository.checkThenExecute(
                        JDBCJDBCConnectorDatabaseRepository.findConfigurationByName(
                                name,
                                this.JDBCConnectorJDBCDatabaseRepository
                        ),
                        JDBCJDBCConnectorDatabaseRepository.checkConfigurationNameAlreadyExist(
                                name,
                                this.JDBCConnectorJDBCDatabaseRepository
                        )
                );

    }
    @Override
    public Either<Error, ConnectorJDBC> findOneById(final String id) {
        return
                JDBCJDBCConnectorDatabaseRepository.checkThenExecute(
                        JDBCJDBCConnectorDatabaseRepository.findConfigurationById(
                                id,
                                this.JDBCConnectorJDBCDatabaseRepository
                        ),
                        JDBCJDBCConnectorDatabaseRepository.checkConfigurationAlreadyExist(
                                id,
                                this.JDBCConnectorJDBCDatabaseRepository
                        )
                );

    }
    @Override
    public Either<Error, Collection<ConnectorJDBC>> findManyByNameContainsIgnoreCase(String name) {
        return JDBCJDBCConnectorDatabaseRepository.findAllConfiguration(this.JDBCConnectorJDBCDatabaseRepository,name).get();
    }

    @Override
    public Page<ConnectorJDBCDAO> findAll(Pageable p) {
        return JDBCConnectorJDBCDatabaseRepository.findAll(p);
    }
    @Override
    public Page<ConnectorJDBCDAO> findByNameContaining(String name, Pageable page){
        return JDBCConnectorJDBCDatabaseRepository.findByNameContaining(name,page);
    }


    private static Supplier <Either<Error, Collection<ConnectorJDBC>>> findAllConfiguration(JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository, String name) {
        return
                JDBCJDBCConnectorDatabaseRepository.executeOnRepositoryForManyResult(
                        JDBCConnectorJDBCDatabaseRepository,
                        it ->
                                it.findAll().stream()
                                        .filter(connectorDAO -> connectorDAO.name().toLowerCase().contains(name.toLowerCase())).toList());

    }


    private static Supplier<Either<Error, ConnectorJDBC>> findConfigurationById(
            final String id,
            final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository
    ) {
        return () ->
                JDBCJDBCConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                                JDBCConnectorJDBCDatabaseRepository,
                                it -> it.findOneById(id)
                        )
                        .get()
                        .flatMap(conf ->
                                JDBCJDBCConnectorDatabaseRepository.findConfigurationByIdFromRepository(
                                                id,
                                                JDBCConnectorJDBCDatabaseRepository
                                        )
                                        .get()
                                        .map(Either::<Error, ConnectorJDBC>left)
                                        .orElse(Either.right(conf))
                        );
    }

    private static Supplier<Either<Error, ConnectorJDBC>> findConfigurationByName(
            final String name,
            final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository
    ) {
        return () ->
                JDBCJDBCConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                                JDBCConnectorJDBCDatabaseRepository,
                                it -> it.findByName(name)
                        )
                        .get()
                        .flatMap(conf ->
                                JDBCJDBCConnectorDatabaseRepository.findConfigurationByNameFromRepository(
                                                name,
                                                JDBCConnectorJDBCDatabaseRepository
                                        )
                                        .get()
                                        .map(Either::<Error, ConnectorJDBC>left)
                                        .orElse(Either.right(conf))
                        );
    }
    private static Supplier<Optional<Error>> findConfigurationByNameFromRepository(final String name, final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository) {
        return () ->
                JDBCJDBCConnectorDatabaseRepository.tryOnRepositoryForPossibleIOException(
                        JDBCConnectorJDBCDatabaseRepository,
                        it -> it.findByName(name)
                );
    }

    private static Supplier<Optional<Error>> findConfigurationByIdFromRepository(final String id, final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository) {
        return () ->
                JDBCJDBCConnectorDatabaseRepository.tryOnRepositoryForPossibleIOException(
                        JDBCConnectorJDBCDatabaseRepository,
                        it -> it.findById(id)
                );
    }
    private static Supplier<Optional<Error>> checkConfigurationNameAlreadyExist(final String name, final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository) {
        return
                JDBCJDBCConnectorDatabaseRepository.evaluateOnRepositoryOrError(
                        JDBCConnectorJDBCDatabaseRepository,
                        it -> it.existsByName(name),
                        Error.NotFound::new
                );
    }
    private static Supplier<Either<Error, Collection<ConnectorJDBC>>> executeOnRepositoryForManyResult(
            final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository,
            final Function<JDBCConnectorJDBCDatabaseRepository, Collection<ConnectorJDBCDAO>> operationOnRepositoryForManyResult
    ) {
        return
                () ->
                        JDBCJDBCConnectorDatabaseRepository.tryOnRepositoryForResultOrIOException(
                                        JDBCConnectorJDBCDatabaseRepository,
                                        operationOnRepositoryForManyResult
                                )
                                .flatMap(JDBCJDBCConnectorDatabaseRepository.manyConfigurationDAOToManyConfiguration());
    }

    private static  Function<Collection<ConnectorJDBCDAO>, Either<Error, Collection<ConnectorJDBC>>> manyConfigurationDAOToManyConfiguration() {
        return connectorDAOS -> {
            return connectorDAOS.isEmpty() ? Either.right(new ArrayList<ConnectorJDBC>()) :  JDBCJDBCConnectorDatabaseRepository.findAllConnectorOrRepError(connectorDAOS);

        };


    }

    private static Either<Error, Collection<ConnectorJDBC>> findAllConnectorOrRepError(Collection<ConnectorJDBCDAO> connectorJDBCDAOS) {
        Stream<Either<Error, ConnectorJDBC>> l=  JDBCJDBCConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorJDBCDAOS).stream();
        return JDBCJDBCConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorJDBCDAOS).stream().filter(Either::isRight).toList().isEmpty() ? Either.left(JDBCJDBCConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorJDBCDAOS).stream().filter(Either::isLeft).map(Either::getLeft).findFirst().get()): Either.right(JDBCJDBCConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorJDBCDAOS).stream().filter(Either::isRight).map(Either::get).collect(Collectors.toList()));
    }

    private static   Collection<Either<Error, ConnectorJDBC>> manyConfigurationDAOToManyErrorOrConfiguration(Collection<ConnectorJDBCDAO> connectorJDBCDAOS) {
        return connectorJDBCDAOS.stream().map(connectorDAO -> connectorDAO.toConfiguration()
                .mapLeft(JDBCJDBCConnectorDatabaseRepository::configurationErrorsToRespositoryError)).collect(Collectors.toList());

    }
    private static Collection<ConnectorJDBC> findValidConnectors(final Collection<Either<Error, ConnectorJDBC>> collection) {
        return collection.
                stream()
                .filter(Either::isRight).
                map(Either::get)
                .collect(Collectors.toList());
    }


    private static Supplier<Optional<Error>> checkConfigurationIdDoesNotExist(
            final String id,
            final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository
    ) {
        return
                JDBCJDBCConnectorDatabaseRepository.evaluateOnRepositoryOrError(
                        JDBCConnectorJDBCDatabaseRepository,
                        Predicate.not(it -> it.existsById(id)),
                        Error.AlreadyExist::new
                );
    }

    private static Supplier<Optional<Error>> checkConfigurationNameDoesNotExist(
            final String name,
            final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository
    ) {
        return
                JDBCJDBCConnectorDatabaseRepository.evaluateOnRepositoryOrError(
                        JDBCConnectorJDBCDatabaseRepository,
                        Predicate.not(it -> it.existsByName(name)),
                        Error.AlreadyExist::new
                );
    }

    private static Supplier<Optional<Error>> checkConfigurationAlreadyExist(
            final String id,
            final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository
    ) {
        return
                JDBCJDBCConnectorDatabaseRepository.evaluateOnRepositoryOrError(
                        JDBCConnectorJDBCDatabaseRepository,
                        it -> it.existsById(id),
                        Error.NotFound::new
                );
    }

    private static Supplier<Optional<Error>> evaluateOnRepositoryOrError(
            final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository,
            final Predicate<JDBCConnectorJDBCDatabaseRepository> evaluation,
            final Supplier<Error> errorIfInvalidCondition
    ) {
        return
                () ->
                        JDBCJDBCConnectorDatabaseRepository.tryOnRepositoryForResultOrIOException(
                                        JDBCConnectorJDBCDatabaseRepository,
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

    private static Supplier<Either<Error, ConnectorJDBC>> deleteConfiguration(
            final String id,
            final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository
    ) {
        return () ->
                JDBCJDBCConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                                JDBCConnectorJDBCDatabaseRepository,
                                it -> it.findOneById(id)
                        )
                        .get()
                        .flatMap(conf ->
                                JDBCJDBCConnectorDatabaseRepository.deleteConfigurationFromRepository(
                                                id,
                                                JDBCConnectorJDBCDatabaseRepository
                                        )
                                        .get()
                                        .map(Either::<Error, ConnectorJDBC>left)
                                        .orElse(Either.right(conf))
                        );
    }

    private static Supplier<Optional<Error>> deleteConfigurationFromRepository(String id, JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository) {
        return () ->
                JDBCJDBCConnectorDatabaseRepository.tryOnRepositoryForPossibleIOException(
                        JDBCConnectorJDBCDatabaseRepository,
                        it ->
                                it.deleteById(
                                        id
                                )
                );
    }

    private static Supplier<Either<Error, ConnectorJDBC>> updateConfiguration(
            final ConnectorJDBC connectorJDBC,
            final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository
    ) {
        return
                JDBCJDBCConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                        JDBCConnectorJDBCDatabaseRepository,
                        it ->
                                it.save(
                                        new ConnectorJDBCDAO(
                                                connectorJDBC
                                        )
                                )
                );
    }
    private static Supplier<Either<Error, ConnectorJDBC>> createConfiguration(
            final ConnectorJDBC connectorJDBC,
            final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository
    ) {
        return
                JDBCJDBCConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                        JDBCConnectorJDBCDatabaseRepository,
                        it ->
                                it.save(
                                        new ConnectorJDBCDAO(
                                                connectorJDBC
                                        )
                                )
                );
    }


    private static Supplier<Either<Error, ConnectorJDBC>> executeOnRepositoryForSingleResult(
            final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository,
            final Function<JDBCConnectorJDBCDatabaseRepository, ConnectorJDBCDAO> operationOnRepositoryForSingleResult
    ) {
        return
                () ->
                        JDBCJDBCConnectorDatabaseRepository.tryOnRepositoryForResultOrIOException(
                                        JDBCConnectorJDBCDatabaseRepository,
                                        operationOnRepositoryForSingleResult
                                )
                                .flatMap(JDBCJDBCConnectorDatabaseRepository.configurationDAOToConfiguration());
    }

    private static Function<ConnectorJDBCDAO, Either<Error, ConnectorJDBC>> configurationDAOToConfiguration() {
        return
                configurationDAO ->
                        configurationDAO.toConfiguration()
                                .mapLeft(JDBCJDBCConnectorDatabaseRepository::configurationErrorsToRespositoryError);
    }

    private static Error configurationErrorsToRespositoryError(
            final Collection<ConnectorJDBC.Error> configurationErrors
    ) {
        return
                new Error.IO(
                        configurationErrors.stream()
                                .map(ConnectorJDBC.Error::message)
                                .collect(Collectors.joining(", "))
                );
    }

    private static <RESULT> Either<Error, RESULT> tryOnRepositoryForResultOrIOException(
            final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository,
            final Function<JDBCConnectorJDBCDatabaseRepository, RESULT> actionOnRepositoryForResult
    ) {
        return
                Try.of(() -> JDBCConnectorJDBCDatabaseRepository)
                        .mapTry(actionOnRepositoryForResult::apply)
                        .toEither()
                        .mapLeft(Throwable::getMessage)
                        .mapLeft(Error.IO::new);
    }

    private static Optional<Error> tryOnRepositoryForPossibleIOException(
            final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository,
            final Consumer<JDBCConnectorJDBCDatabaseRepository> actionOnRepository
    ) {
        try {
            actionOnRepository.accept(JDBCConnectorJDBCDatabaseRepository);
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
