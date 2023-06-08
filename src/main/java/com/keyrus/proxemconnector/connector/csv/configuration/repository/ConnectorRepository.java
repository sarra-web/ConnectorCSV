package com.keyrus.proxemconnector.connector.csv.configuration.repository;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Connector;
import io.vavr.control.Either;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface ConnectorRepository {

    Either<Error, Connector> create(final Connector connector);

    Either<Error, Connector> update(final Connector connector);

    Either<Error, Connector> delete(final String id);
    Either<Error, Collection<Connector>>  findAll();
    Either<Error, Connector> findOneByName(String name);
    Either<Error, Connector> findOneById(String id);

    Either<Error, Collection<Connector>> findManyByNameContainsIgnoreCase(String name);
    Page<ConnectorDAO> findAll(Pageable p);
    Page<ConnectorDAO> findByNameContaining(String name, Pageable page);
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
