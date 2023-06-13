package com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorCSVDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorCSV;
import io.vavr.control.Either;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface ConnectorRepository {

    Either<Error, ConnectorCSV> create(final ConnectorCSV connectorCSV);

    Either<Error, ConnectorCSV> update(final ConnectorCSV connectorCSV);

    Either<Error, ConnectorCSV> delete(final String id);
    Either<Error, Collection<ConnectorCSV>>  findAll();
    Either<Error, ConnectorCSV> findOneByName(String name);
    Either<Error, ConnectorCSV> findOneById(String id);

    Either<Error, Collection<ConnectorCSV>> findManyByNameContainsIgnoreCase(String name);
    Page<ConnectorCSVDAO> findAll(Pageable p);
    Page<ConnectorCSVDAO> findByNameContaining(String name, Pageable page);
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
