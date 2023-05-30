package com.keyrus.proxemconnector.connector.csv.configuration.repository;

import com.keyrus.proxemconnector.connector.csv.configuration.model.Connector;
import io.vavr.control.Either;

import java.util.Collection;

public interface ConnectorRepository {

    Either<Error, Connector> create(final Connector connector);

    Either<Error, Connector> update(final Connector connector);

    Either<Error, Connector> delete(final String id);
    Either<Error, Collection<Connector>>  findAll();

    sealed interface Error {

        default String message() {
            return this.getClass().getCanonicalName();
        }

        record IO(
                String message
        ) implements Error {
        }

        record AlreadyExist() implements Error{
        }

        record NotFound() implements Error{
        }
    }
}
