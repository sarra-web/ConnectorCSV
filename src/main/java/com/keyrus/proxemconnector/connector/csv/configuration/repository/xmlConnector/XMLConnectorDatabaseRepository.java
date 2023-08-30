package com.keyrus.proxemconnector.connector.csv.configuration.repository.xmlConnector;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorXMLDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Connector;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML;
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

public final class XMLConnectorDatabaseRepository implements XMLConnectorRepository {

    private static XMLConnectorDatabaseRepository instance = null;

    public static XMLConnectorDatabaseRepository instance(
            final XMLConnectorJDBCDatabaseRepository cSVConnectorJDBCDatabaseRepository
    ) {
        if (Objects.isNull(instance))
            instance =
                    new XMLConnectorDatabaseRepository(
                            cSVConnectorJDBCDatabaseRepository
                    );
        return instance;
    }

    private final XMLConnectorJDBCDatabaseRepository cSVConnectorJDBCDatabaseRepository;

    private XMLConnectorDatabaseRepository(
            final XMLConnectorJDBCDatabaseRepository cSVConnectorJDBCDatabaseRepository
    ) {
        this.cSVConnectorJDBCDatabaseRepository = cSVConnectorJDBCDatabaseRepository;
    }
    @Override
    public Either<Error, ConnectorXML> create(
            final ConnectorXML connectorXML
    ) {
        return
                XMLConnectorDatabaseRepository.checkThenExecute(
                        XMLConnectorDatabaseRepository.createConfiguration(
                                connectorXML,
                                this.cSVConnectorJDBCDatabaseRepository
                        ),
                        XMLConnectorDatabaseRepository.checkConfigurationIdDoesNotExist(
                                connectorXML.id(),
                                this.cSVConnectorJDBCDatabaseRepository
                        ),
                        XMLConnectorDatabaseRepository.checkConfigurationNameDoesNotExist(
                                connectorXML.name(),
                                this.cSVConnectorJDBCDatabaseRepository
                        )
                );
    }



    @Override
    public Either<Error, ConnectorXML> update(
            final ConnectorXML connectorXML
    ) {
        return
                XMLConnectorDatabaseRepository.checkThenExecute(
                        XMLConnectorDatabaseRepository.updateConfiguration(
                                connectorXML,
                                this.cSVConnectorJDBCDatabaseRepository
                        ),
                        XMLConnectorDatabaseRepository.checkConfigurationAlreadyExist(
                                connectorXML.id(),
                                this.cSVConnectorJDBCDatabaseRepository
                        )
                );
    }

    @Override
    public Either<Error, ConnectorXML> delete(
            final String id
    ) {
        return
                XMLConnectorDatabaseRepository.checkThenExecute(
                        XMLConnectorDatabaseRepository.deleteConfiguration(
                                id,
                                this.cSVConnectorJDBCDatabaseRepository
                        ),
                        XMLConnectorDatabaseRepository.checkConfigurationAlreadyExist(
                                id,
                                this.cSVConnectorJDBCDatabaseRepository
                        )
                );
    }
    @Override
    public Either<Error, Collection<ConnectorXML>> findAll() {
        return XMLConnectorDatabaseRepository.findAllConfiguration(this.cSVConnectorJDBCDatabaseRepository).get();
    }

    private static Supplier <Either<Error, Collection<ConnectorXML>>> findAllConfiguration(XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository) {
        return
                XMLConnectorDatabaseRepository.executeOnRepositoryForManyResult(
                        XMLConnectorJDBCDatabaseRepository,
                        it ->
                                it.findAll()

                );

    }

    @Override
    public Either<Error, ConnectorXML> findOneByName(final String name) {
        return
                XMLConnectorDatabaseRepository.checkThenExecute(
                        XMLConnectorDatabaseRepository.findConfigurationByName(
                                name,
                                this.cSVConnectorJDBCDatabaseRepository
                        ),
                        XMLConnectorDatabaseRepository.checkConfigurationNameAlreadyExist(
                                name,
                                this.cSVConnectorJDBCDatabaseRepository
                        )
                );

    }
    @Override
    public Either<Error, ConnectorXML> findOneById(final String id) {
        return
                XMLConnectorDatabaseRepository.checkThenExecute(
                        XMLConnectorDatabaseRepository.findConfigurationById(
                                id,
                                this.cSVConnectorJDBCDatabaseRepository
                        ),
                        XMLConnectorDatabaseRepository.checkConfigurationAlreadyExist(
                                id,
                                this.cSVConnectorJDBCDatabaseRepository
                        )
                );

    }
    @Override
    public Either<Error, Collection<ConnectorXML>> findManyByNameContainsIgnoreCase(String name) {
        return XMLConnectorDatabaseRepository.findAllConfiguration(this.cSVConnectorJDBCDatabaseRepository,name).get();
    }

    @Override
    public Page<ConnectorXMLDAO> findAll(Pageable p) {
        return cSVConnectorJDBCDatabaseRepository.findAll(p);
    }
    @Override
    public Page<ConnectorXMLDAO> findByNameContaining(String name, Pageable page){
        return cSVConnectorJDBCDatabaseRepository.findByNameContaining(name,page);
    }


    private static Supplier <Either<Error, Collection<ConnectorXML>>> findAllConfiguration(XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository, String name) {
        return
                XMLConnectorDatabaseRepository.executeOnRepositoryForManyResult(
                        XMLConnectorJDBCDatabaseRepository,
                        it ->
                                it.findAll().stream()
                                        .filter(connectorDAO -> connectorDAO.name().toLowerCase().contains(name.toLowerCase())).toList());

    }


    private static Supplier<Either<Error, ConnectorXML>> findConfigurationById(
            final String id,
            final XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository
    ) {
        return () ->
                XMLConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                                XMLConnectorJDBCDatabaseRepository,
                                it -> it.findOneById(id)
                        )
                        .get()
                        .flatMap(conf ->
                                XMLConnectorDatabaseRepository.findConfigurationByIdFromRepository(
                                                id,
                                                XMLConnectorJDBCDatabaseRepository
                                        )
                                        .get()
                                        .map(Either::<Error, ConnectorXML>left)
                                        .orElse(Either.right(conf))
                        );
    }

    private static Supplier<Either<Error, ConnectorXML>> findConfigurationByName(
            final String name,
            final XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository
    ) {
        return () ->
                XMLConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                                XMLConnectorJDBCDatabaseRepository,
                                it -> it.findByName(name)
                        )
                        .get()
                        .flatMap(conf ->
                                XMLConnectorDatabaseRepository.findConfigurationByNameFromRepository(
                                                name,
                                                XMLConnectorJDBCDatabaseRepository
                                        )
                                        .get()
                                        .map(Either::<Error, ConnectorXML>left)
                                        .orElse(Either.right(conf))
                        );
    }
    private static Supplier<Optional<Error>> findConfigurationByNameFromRepository(final String name, final XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository) {
        return () ->
                XMLConnectorDatabaseRepository.tryOnRepositoryForPossibleIOException(
                        XMLConnectorJDBCDatabaseRepository,
                        it -> it.findByName(name)
                );
    }

    private static Supplier<Optional<Error>> findConfigurationByIdFromRepository(final String id, final XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository) {
        return () ->
                XMLConnectorDatabaseRepository.tryOnRepositoryForPossibleIOException(
                        XMLConnectorJDBCDatabaseRepository,
                        it -> it.findById(id)
                );
    }
    private static Supplier<Optional<Error>> checkConfigurationNameAlreadyExist(final String name, final XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository) {
        return
                XMLConnectorDatabaseRepository.evaluateOnRepositoryOrError(
                        XMLConnectorJDBCDatabaseRepository,
                        it -> it.existsByName(name),
                        Error.NotFound::new
                );
    }
    private static Supplier<Either<Error, Collection<ConnectorXML>>> executeOnRepositoryForManyResult(
            final XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository,
            final Function<XMLConnectorJDBCDatabaseRepository, Collection<ConnectorXMLDAO>> operationOnRepositoryForManyResult
    ) {
        return
                () ->
                        XMLConnectorDatabaseRepository.tryOnRepositoryForResultOrIOException(
                                        XMLConnectorJDBCDatabaseRepository,
                                        operationOnRepositoryForManyResult
                                )
                                .flatMap(XMLConnectorDatabaseRepository.manyConfigurationDAOToManyConfiguration());
    }

    private static  Function<Collection<ConnectorXMLDAO>, Either<Error, Collection<ConnectorXML>>> manyConfigurationDAOToManyConfiguration() {
        return connectorDAOS -> {
            return connectorDAOS.isEmpty() ? Either.right(new ArrayList<ConnectorXML>()) :  XMLConnectorDatabaseRepository.findAllConnectorOrRepError(connectorDAOS);

        };


    }

    private static Either<Error, Collection<ConnectorXML>> findAllConnectorOrRepError(Collection<ConnectorXMLDAO> connectorXMLDAOS) {
        Stream<Either<Error, ConnectorXML>> l=  XMLConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorXMLDAOS).stream();
        return XMLConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorXMLDAOS).stream().filter(Either::isRight).toList().isEmpty() ? Either.left(XMLConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorXMLDAOS).stream().filter(Either::isLeft).map(Either::getLeft).findFirst().get()): Either.right(XMLConnectorDatabaseRepository.manyConfigurationDAOToManyErrorOrConfiguration(connectorXMLDAOS).stream().filter(Either::isRight).map(Either::get).collect(Collectors.toList()));
    }

    private static   Collection<Either<Error, ConnectorXML>> manyConfigurationDAOToManyErrorOrConfiguration(Collection<ConnectorXMLDAO> connectorXMLDAOS) {
        return connectorXMLDAOS.stream().map(connectorDAO -> connectorDAO.toConfiguration()
                .mapLeft(XMLConnectorDatabaseRepository::configurationErrorsToRespositoryError)).collect(Collectors.toList());

    }



    private static Supplier<Optional<Error>> checkConfigurationIdDoesNotExist(
            final String id,
            final XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository
    ) {
        return
                XMLConnectorDatabaseRepository.evaluateOnRepositoryOrError(
                        XMLConnectorJDBCDatabaseRepository,
                        Predicate.not(it -> it.existsById(id)),
                        Error.AlreadyExist::new
                );
    }

    private static Supplier<Optional<Error>> checkConfigurationNameDoesNotExist(
            final String name,
            final XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository
    ) {
        return
                XMLConnectorDatabaseRepository.evaluateOnRepositoryOrError(
                        XMLConnectorJDBCDatabaseRepository,
                        Predicate.not(it -> it.existsByName(name)),
                        Error.AlreadyExist::new
                );
    }

    private static Supplier<Optional<Error>> checkConfigurationAlreadyExist(
            final String id,
            final XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository
    ) {
        return
                XMLConnectorDatabaseRepository.evaluateOnRepositoryOrError(
                        XMLConnectorJDBCDatabaseRepository,
                        it -> it.existsById(id),
                        Error.NotFound::new
                );
    }

    private static Supplier<Optional<Error>> evaluateOnRepositoryOrError(
            final XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository,
            final Predicate<XMLConnectorJDBCDatabaseRepository> evaluation,
            final Supplier<Error> errorIfInvalidCondition
    ) {
        return
                () ->
                        XMLConnectorDatabaseRepository.tryOnRepositoryForResultOrIOException(
                                        XMLConnectorJDBCDatabaseRepository,
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

    private static Supplier<Either<Error, ConnectorXML>> deleteConfiguration(
            final String id,
            final XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository
    ) {
        return () ->
                XMLConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                                XMLConnectorJDBCDatabaseRepository,
                                it -> it.findOneById(id)
                        )
                        .get()
                        .flatMap(conf ->
                                XMLConnectorDatabaseRepository.deleteConfigurationFromRepository(
                                                id,
                                                XMLConnectorJDBCDatabaseRepository
                                        )
                                        .get()
                                        .map(Either::<Error, ConnectorXML>left)
                                        .orElse(Either.right(conf))
                        );
    }

    private static Supplier<Optional<Error>> deleteConfigurationFromRepository(String id, XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository) {
        return () ->
                XMLConnectorDatabaseRepository.tryOnRepositoryForPossibleIOException(
                        XMLConnectorJDBCDatabaseRepository,
                        it ->
                                it.deleteById(
                                        id
                                )
                );
    }

    private static Supplier<Either<Error, ConnectorXML>> updateConfiguration(
            final ConnectorXML connectorXML,
            final XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository
    ) {
        return
                XMLConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                        XMLConnectorJDBCDatabaseRepository,
                        it ->
                                it.save(
                                        new ConnectorXMLDAO(
                                                connectorXML
                                        )
                                )
                );
    }
    private static Supplier<Either<Error, ConnectorXML>> createConfiguration(
            final ConnectorXML connectorXML,
            final XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository
    ) {
        return
                XMLConnectorDatabaseRepository.executeOnRepositoryForSingleResult(
                        XMLConnectorJDBCDatabaseRepository,
                        it ->
                                it.save(
                                        new ConnectorXMLDAO(
                                                connectorXML
                                        )
                                )
                );
    }



    private static Supplier<Either<Error, ConnectorXML>> executeOnRepositoryForSingleResult(
            final XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository,
            final Function<XMLConnectorJDBCDatabaseRepository, ConnectorXMLDAO> operationOnRepositoryForSingleResult
    ) {
        return
                () ->
                        XMLConnectorDatabaseRepository.tryOnRepositoryForResultOrIOException(
                                        XMLConnectorJDBCDatabaseRepository,
                                        operationOnRepositoryForSingleResult
                                )
                                .flatMap(XMLConnectorDatabaseRepository.configurationDAOToConfiguration());
    }

    private static Function<ConnectorXMLDAO, Either<Error, ConnectorXML>> configurationDAOToConfiguration() {
        return
                configurationDAO ->
                        configurationDAO.toConfiguration()
                                .mapLeft(XMLConnectorDatabaseRepository::configurationErrorsToRespositoryError);
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
            final XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository,
            final Function<XMLConnectorJDBCDatabaseRepository, RESULT> actionOnRepositoryForResult
    ) {
        return
                Try.of(() -> XMLConnectorJDBCDatabaseRepository)
                        .mapTry(actionOnRepositoryForResult::apply)
                        .toEither()
                        .mapLeft(Throwable::getMessage)
                        .mapLeft(Error.IO::new);
    }

    private static Optional<Error> tryOnRepositoryForPossibleIOException(
            final XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository,
            final Consumer<XMLConnectorJDBCDatabaseRepository> actionOnRepository
    ) {
        try {
            actionOnRepository.accept(XMLConnectorJDBCDatabaseRepository);
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
