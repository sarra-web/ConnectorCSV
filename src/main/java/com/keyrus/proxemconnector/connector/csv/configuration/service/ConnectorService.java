package com.keyrus.proxemconnector.connector.csv.configuration.service;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorCSVDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorCSV;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.ConnectorRepository;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Collection;
import java.util.Objects;

@Slf4j
public final class ConnectorService {

    private static ConnectorService instance = null;

    public static ConnectorService instance(
            final ConnectorRepository connectorRepository
    ) {
        if (Objects.isNull(instance))
            instance =
                    new ConnectorService(
                            connectorRepository
                    );
        return instance;
    }

    private final ConnectorRepository connectorRepository;

    private ConnectorService(
            final ConnectorRepository connectorRepository
    ) {
        this.connectorRepository = connectorRepository;
    }

    public Either<Error, ConnectorCSV> create(
            final ConnectorCSV connectorCSV
    ) {
        return
                this.connectorRepository
                        .create(
                                connectorCSV
                        )
                        .mapLeft(ConnectorService::repositoryErrorToServiceError);
    }

    public Either<Error, ConnectorCSV> update(
            final ConnectorCSV connectorCSV
    ) {
        return
                this.connectorRepository
                        .update(
                                connectorCSV
                        )
                        .mapLeft(ConnectorService::repositoryErrorToServiceError);
    }

    public Either<Error, ConnectorCSV> delete(
            final String id
    ) {
        return
                this.connectorRepository
                        .delete(
                                id
                        )
                        .mapLeft(ConnectorService::repositoryErrorToServiceError);
    }

    public Either<Error, Collection<ConnectorCSV>> findAll() {
        return this.connectorRepository
                .findAll()
                .mapLeft(ConnectorService::repositoryErrorToServiceError);

    }
    public Either<Error, ConnectorCSV> findOneByName(String name) {
        return this.connectorRepository
                .findOneByName(
                        name
                )
                .mapLeft(ConnectorService::repositoryErrorToServiceError);
    }
    public Either<Error, ConnectorCSV> findOneById(String id) {
        return this.connectorRepository
                .findOneById(
                        id
                )
                .mapLeft(ConnectorService::repositoryErrorToServiceError);
    }

    public Either<Error, Collection<ConnectorCSV>> findManyByNameContainsIgnoreCase(String name) {
        return this.connectorRepository
                .findManyByNameContainsIgnoreCase(name)
                .mapLeft(ConnectorService::repositoryErrorToServiceError);

    }
    public Page<ConnectorCSVDAO> findAll(int page, int size){
        log.info("Fetching for page {} of size {}",page,size);
        return connectorRepository.findAll(PageRequest.of(page,size));
    }

    private static Error repositoryErrorToServiceError(
            final ConnectorRepository.Error repositoryError
    ) {
        if (repositoryError instanceof ConnectorRepository.Error.IO io)
            return new Error.IO(io.message());
        if (repositoryError instanceof ConnectorRepository.Error.AlreadyExist)
            return new Error.AlreadyExist();
        if (repositoryError instanceof ConnectorRepository.Error.NotFound)
            return new Error.NotFound();
        throw new IllegalStateException("repository error not mapped to service error");
    }

    public sealed interface Error {

        default String message() {
            return this.getClass().getCanonicalName();
        }

        record IO(
                String message
        ) implements Error {
        }

        record AlreadyExist() implements Error {
        }

        record NotFound() implements Error {
        }
    }
}
