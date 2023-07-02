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

public final class CSVConnectorDatabaseRepository implements CSVConnectorRepository {

    private static ProjectJDBCDatabaseRepository projectJDBCDatabaseRepository;
    private static CSVConnectorDatabaseRepository instance = null;

    public static CSVConnectorDatabaseRepository instance(
            final CSVConnectorJDBCDatabaseRepository cSVConnectorJDBCDatabaseRepository
    ) {
        if (Objects.isNull(instance))
            instance =
                    new CSVConnectorDatabaseRepository(
                            cSVConnectorJDBCDatabaseRepository
                    );
        return instance;
    }

    private final CSVConnectorJDBCDatabaseRepository cSVConnectorJDBCDatabaseRepository;

    private CSVConnectorDatabaseRepository(
            final CSVConnectorJDBCDatabaseRepository cSVConnectorJDBCDatabaseRepository
    ) {
        this.cSVConnectorJDBCDatabaseRepository = cSVConnectorJDBCDatabaseRepository;
    }
    @Override
    public Either<Error, ConnectorCSV> create(
            final ConnectorCSV connectorCSV
    ) {
        return
                CSVConnectorDatabaseRepository.checkThenExecute(
                        CSVConnectorDatabaseRepository.createConfiguration(
                                connectorCSV,
                                this.cSVConnectorJDBCDatabaseRepository
                        ),
                        CSVConnectorDatabaseRepository.checkConfigurationIdDoesNotExist(
                                connectorCSV.id(),
                                this.cSVConnectorJDBCDatabaseRepository
                        ),
                        CSVConnectorDatabaseRepository.checkConfigurationNameDoesNotExist(
                                connectorCSV.name(),
                                this.cSVConnectorJDBCDatabaseRepository
                        )
                );
    }

    @Override
    public Either<Error, ConnectorCSV> create2(
            final ConnectorCSV connectorCSV,String projectId
    ) {
        return
                CSVConnectorDatabaseRepository.checkThenExecute(
                        CSVConnectorDatabaseRepository.createConfiguration2(
                                connectorCSV,
                                this.cSVConnectorJDBCDatabaseRepository,projectId
                        ),
                        CSVConnectorDatabaseRepository.checkConfigurationIdDoesNotExist(
                                connectorCSV.id(),
                                this.cSVConnectorJDBCDatabaseRepository
                        ),
                        CSVConnectorDatabaseRepository.checkConfigurationNameDoesNotExist(
                                connectorCSV.name(),
                                this.cSVConnectorJDBCDatabaseRepository
                        )
                );
    }

    @Override
    public Either<Error, ConnectorCSV> update(
            final ConnectorCSV connectorCSV
    ) {
        return
                CSVConnectorDatabaseRepository.checkThenExecute(
                        CSVConnectorDatabaseRepository.updateConfiguration(
                                connectorCSV,
                                this.cSVConnectorJDBCDatabaseRepository
                        ),
                        CSVConnectorDatabaseRepository.checkConfigurationAlreadyExist(
                                connectorCSV.id(),
                                this.cSVConnectorJDBCDatabaseRepository
                        )
                );
    }

    @Override
    public Either<Error, ConnectorCSV> delete(
            final String id
    ) {
        return
                CSVConnectorDatabaseRepository.checkThenExecute(
                        CSVConnectorDatabaseRepository.deleteConfiguration(
                                id,
                                this.cSVConnectorJDBCDatabaseRepository
                        ),
                        CSVConnectorDatabaseRepository.checkConfigurationAlreadyExist(
                                id,
                                this.cSVConnectorJDBCDatabaseRepository
                        )
                );
    }
    @Override
    public Either<Error, Collection<ConnectorCSV>> findAll() {
        return CSVConnectorDatabaseRepository.findAllConfiguration(this.cSVConnectorJDBCDatabaseRepository).get();
    }

    private static Supplier <Either<Error, Collection<ConnectorCSV>>> findAllConfiguration(CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository) {
        return
                CSVConnectorDatabaseRepository.executeOnRepositoryForManyResult(
                        CSVConnectorJDBCDatabaseRepository,
                        it ->
                                it.findAll()

                );

    }

    @Override
    public Either<Error, ConnectorCSV> findOneByName(final String name) {
        return
                CSVConnectorDatabaseRepository.checkThenExecute(
                        CSVConnectorDatabaseRepository.findConfigurationByName(
                                name,
                                this.cSVConnectorJDBCDatabaseRepository
                        ),
                        CSVConnectorDatabaseRepository.checkConfigurationNameAlreadyExist(
                                name,
                                this.cSVConnectorJDBCDatabaseRepository
                        )
                );

    }
    @Override
    public Either<Error, ConnectorCSV> findOneById(final String id) {
        return
                CSVConnectorDatabaseRepository.checkThenExecute(
                        CSVConnectorDatabaseRepository.findConfigurationById(
                                id,
                                this.cSVConnectorJDBCDatabaseRepository
                        ),
                        CSVConnectorDatabaseRepository.checkConfigurationAlreadyExist(
                                id,
                                this.cSVConnectorJDBCDatabaseRepository
                        )
                );

    }
    @Override
    public Either<Error, Collection<ConnectorCSV>> findManyByNameContainsIgnoreCase(String name) {
        return CSVConnectorDatabaseRepository.findAllConfiguration(this.cSVConnectorJDBCDatabaseRepository,name).get();
    }

    @Override
    public Page<ConnectorCSVDAO> findAll(Pageable p) {
        return cSVConnectorJDBCDatabaseRepository.findAll(p);
    }
    @Override
    public Page<ConnectorCSVDAO> findByNameContaining(String name, Pageable page){
        return cSVConnectorJDBCDatabaseRepository.findByNameContaining(name,page);
    }


    private static Supplier <Either<Error, Collection<ConnectorCSV>>> findAllConfiguration(CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository, String name) {
        return
                CSVConnectorDatabaseRepository.executeOnRepositoryForManyResult(
                        CSVConnectorJDBCDatabaseRepository,
                        it ->
                                it.findAll().stream()
                                        .filter(connectorDAO -> connectorDAO.name().toLowerCase().contains(name.toLowerCase())).toList());

    }


    private static Supplier<Either<Error, ConnectorCSV>> findConfigurationById(
            final String id,
            final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository
    ) {
        return () ->
                CSVConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                                CSVConnectorJDBCDatabaseRepository,
                                it -> it.findOneById(id)
                        )
                        .get()
                        .flatMap(conf ->
                                CSVConnectorDatabaseRepository.findConfigurationByIdFromRepository(
                                                id,
                                                CSVConnectorJDBCDatabaseRepository
                                        )
                                        .get()
                                        .map(Either::<Error, ConnectorCSV>left)
                                        .orElse(Either.right(conf))
                        );
    }

    private static Supplier<Either<Error, ConnectorCSV>> findConfigurationByName(
            final String name,
            final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository
    ) {
        return () ->
                CSVConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                                CSVConnectorJDBCDatabaseRepository,
                                it -> it.findByName(name)
                        )
                        .get()
                        .flatMap(conf ->
                                CSVConnectorDatabaseRepository.findConfigurationByNameFromRepository(
                                                name,
                                                CSVConnectorJDBCDatabaseRepository
                                        )
                                        .get()
                                        .map(Either::<Error, ConnectorCSV>left)
                                        .orElse(Either.right(conf))
                        );
    }
    private static Supplier<Optional<Error>> findConfigurationByNameFromRepository(final String name, final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository) {
        return () ->
                CSVConnectorDatabaseRepository.tryOnRepositoryForPossibleIOException(
                        CSVConnectorJDBCDatabaseRepository,
                        it -> it.findByName(name)
                );
    }

    private static Supplier<Optional<Error>> findConfigurationByIdFromRepository(final String id, final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository) {
        return () ->
                CSVConnectorDatabaseRepository.tryOnRepositoryForPossibleIOException(
                        CSVConnectorJDBCDatabaseRepository,
                        it -> it.findById(id)
                );
    }
    private static Supplier<Optional<Error>> checkConfigurationNameAlreadyExist(final String name, final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository) {
        return
                CSVConnectorDatabaseRepository.evaluateOnRepositoryOrError(
                        CSVConnectorJDBCDatabaseRepository,
                        it -> it.existsByName(name),
                        Error.NotFound::new
                );
    }
    private static Supplier<Either<Error, Collection<ConnectorCSV>>> executeOnRepositoryForManyResult(
            final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository,
            final Function<CSVConnectorJDBCDatabaseRepository, Collection<ConnectorCSVDAO>> operationOnRepositoryForManyResult
    ) {
        return
                () ->
                        CSVConnectorDatabaseRepository.tryOnRepositoryForResultOrIOException(
                                        CSVConnectorJDBCDatabaseRepository,
                                        operationOnRepositoryForManyResult
                                )
                                .flatMap(CSVConnectorDatabaseRepository.manyConfigurationDAOToManyConfiguration());
    }

    private static  Function<Collection<ConnectorCSVDAO>, Either<Error, Collection<ConnectorCSV>>> manyConfigurationDAOToManyConfiguration() {
        return connectorDAOS -> {
            return connectorDAOS.isEmpty() ? Either.right(new ArrayList<ConnectorCSV>()) :  CSVConnectorDatabaseRepository.findAllConnectorOrRepError(connectorDAOS);

        };


    }

    private static Either<Error, Collection<ConnectorCSV>> findAllConnectorOrRepError(Collection<ConnectorCSVDAO> connectorCSVDAOS) {
        Stream<Either<Error, ConnectorCSV>> l=  CSVConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorCSVDAOS).stream();
        return CSVConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorCSVDAOS).stream().filter(Either::isRight).toList().isEmpty() ? Either.left(CSVConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorCSVDAOS).stream().filter(Either::isLeft).map(Either::getLeft).findFirst().get()): Either.right(CSVConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorCSVDAOS).stream().filter(Either::isRight).map(Either::get).collect(Collectors.toList()));
    }

    private static   Collection<Either<Error, ConnectorCSV>> manyConfigurationDAOToManyErrorOrConfiguration(Collection<ConnectorCSVDAO> connectorCSVDAOS) {
        return connectorCSVDAOS.stream().map(connectorDAO -> connectorDAO.toConfiguration()
                .mapLeft(CSVConnectorDatabaseRepository::configurationErrorsToRespositoryError)).collect(Collectors.toList());

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
            final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository
    ) {
        return
                CSVConnectorDatabaseRepository.evaluateOnRepositoryOrError(
                        CSVConnectorJDBCDatabaseRepository,
                        Predicate.not(it -> it.existsById(id)),
                        Error.AlreadyExist::new
                );
    }

    private static Supplier<Optional<Error>> checkConfigurationNameDoesNotExist(
            final String name,
            final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository
    ) {
        return
                CSVConnectorDatabaseRepository.evaluateOnRepositoryOrError(
                        CSVConnectorJDBCDatabaseRepository,
                        Predicate.not(it -> it.existsByName(name)),
                        Error.AlreadyExist::new
                );
    }

    private static Supplier<Optional<Error>> checkConfigurationAlreadyExist(
            final String id,
            final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository
    ) {
        return
                CSVConnectorDatabaseRepository.evaluateOnRepositoryOrError(
                        CSVConnectorJDBCDatabaseRepository,
                        it -> it.existsById(id),
                        Error.NotFound::new
                );
    }

    private static Supplier<Optional<Error>> evaluateOnRepositoryOrError(
            final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository,
            final Predicate<CSVConnectorJDBCDatabaseRepository> evaluation,
            final Supplier<Error> errorIfInvalidCondition
    ) {
        return
                () ->
                        CSVConnectorDatabaseRepository.tryOnRepositoryForResultOrIOException(
                                        CSVConnectorJDBCDatabaseRepository,
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
            final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository
    ) {
        return () ->
                CSVConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                                CSVConnectorJDBCDatabaseRepository,
                                it -> it.findOneById(id)
                        )
                        .get()
                        .flatMap(conf ->
                                CSVConnectorDatabaseRepository.deleteConfigurationFromRepository(
                                                id,
                                                CSVConnectorJDBCDatabaseRepository
                                        )
                                        .get()
                                        .map(Either::<Error, ConnectorCSV>left)
                                        .orElse(Either.right(conf))
                        );
    }

    private static Supplier<Optional<Error>> deleteConfigurationFromRepository(String id, CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository) {
        return () ->
                CSVConnectorDatabaseRepository.tryOnRepositoryForPossibleIOException(
                        CSVConnectorJDBCDatabaseRepository,
                        it ->
                                it.deleteById(
                                        id
                                )
                );
    }

    private static Supplier<Either<Error, ConnectorCSV>> updateConfiguration(
            final ConnectorCSV connectorCSV,
            final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository
    ) {
        return
                CSVConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                        CSVConnectorJDBCDatabaseRepository,
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
            final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository
    ) {
        return
                CSVConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                        CSVConnectorJDBCDatabaseRepository,
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
            final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository
            ,final String idProject
    ) {
        ConnectorCSVDAO connectorCSVDAO=new ConnectorCSVDAO(
                connectorCSV);
       //  connectorCSVDAO.setProjectDAO(projectJDBCDatabaseRepository.findOneById(idProject));


        return
                CSVConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                        CSVConnectorJDBCDatabaseRepository,
                        it ->
                                it.save(connectorCSVDAO

                                        )
                                );

    }

    private static Supplier<Either<Error, ConnectorCSV>> executeOnRepositoryForSingleResult(
            final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository,
            final Function<CSVConnectorJDBCDatabaseRepository, ConnectorCSVDAO> operationOnRepositoryForSingleResult
    ) {
        return
                () ->
                        CSVConnectorDatabaseRepository.tryOnRepositoryForResultOrIOException(
                                        CSVConnectorJDBCDatabaseRepository,
                                        operationOnRepositoryForSingleResult
                                )
                                .flatMap(CSVConnectorDatabaseRepository.configurationDAOToConfiguration());
    }

    private static Function<ConnectorCSVDAO, Either<Error, ConnectorCSV>> configurationDAOToConfiguration() {
        return
                configurationDAO ->
                        configurationDAO.toConfiguration()
                                .mapLeft(CSVConnectorDatabaseRepository::configurationErrorsToRespositoryError);
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
            final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository,
            final Function<CSVConnectorJDBCDatabaseRepository, RESULT> actionOnRepositoryForResult
    ) {
        return
                Try.of(() -> CSVConnectorJDBCDatabaseRepository)
                        .mapTry(actionOnRepositoryForResult::apply)
                        .toEither()
                        .mapLeft(Throwable::getMessage)
                        .mapLeft(Error.IO::new);
    }

    private static Optional<Error> tryOnRepositoryForPossibleIOException(
            final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository,
            final Consumer<CSVConnectorJDBCDatabaseRepository> actionOnRepository
    ) {
        try {
            actionOnRepository.accept(CSVConnectorJDBCDatabaseRepository);
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
