package com.keyrus.proxemconnector.connector.csv.configuration.repository.xmlConnector;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorXMLDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML;
import io.vavr.control.Either;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;



public interface XMLConnectorRepository {

    Either<Error, ConnectorXML> create(final ConnectorXML connectorXML);
    Either<Error, ConnectorXML> update(final ConnectorXML connectorXML);

    Either<Error, ConnectorXML> delete(final String id);
    Either<Error, Collection<ConnectorXML>>  findAll();
    Either<Error, ConnectorXML> findOneByName(String name);
    Either<Error, ConnectorXML> findOneById(String id);

    Either<Error, Collection<ConnectorXML>> findManyByNameContainsIgnoreCase(String name);
    Page<ConnectorXMLDAO> findAll(Pageable p);
    Page<ConnectorXMLDAO> findByNameContaining(String name, Pageable page);
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
