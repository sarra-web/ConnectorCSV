package com.keyrus.proxemconnector.connector.csv.configuration.repository.jdbcConnector;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorJDBCDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC;
import io.vavr.control.Either;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface JDBCConnectorRepository {


    Either<Error, ConnectorJDBC> create(final ConnectorJDBC connectorJDBC);
    Either<Error, ConnectorJDBC> update(final ConnectorJDBC connectorJDBC);

    Either<Error, ConnectorJDBC> delete(final String id);
    Either<Error, Collection<ConnectorJDBC>>  findAll();
    Either<Error, ConnectorJDBC> findOneByName(String name);
    Either<Error, ConnectorJDBC> findOneById(String id);

    Either<Error, Collection<ConnectorJDBC>> findManyByNameContainsIgnoreCase(String name);
    Page<ConnectorJDBCDAO> findAll(Pageable p);
    Page<ConnectorJDBCDAO> findByNameContaining(String name, Pageable page);
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
