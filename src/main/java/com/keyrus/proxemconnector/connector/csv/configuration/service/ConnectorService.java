package com.keyrus.proxemconnector.connector.csv.configuration.service;

import com.keyrus.proxemconnector.connector.csv.configuration.model.Connector;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.ConnectorRepository;
import io.vavr.control.Either;

import java.util.Objects;

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

    public Either<Error, Connector> create(
            final Connector connector
    ) {
        return
                this.connectorRepository
                        .create(
                                connector
                        )
                        .mapLeft(ConnectorService::repositoryErrorToServiceError);
    }

    public Either<Error, Connector> update(
            final Connector connector
    ) {
        return
                this.connectorRepository
                        .update(
                                connector
                        )
                        .mapLeft(ConnectorService::repositoryErrorToServiceError);
    }

    public Either<Error, Connector> delete(
            final String id
    ) {
        return
                this.connectorRepository
                        .delete(
                                id
                        )
                        .mapLeft(ConnectorService::repositoryErrorToServiceError);
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
