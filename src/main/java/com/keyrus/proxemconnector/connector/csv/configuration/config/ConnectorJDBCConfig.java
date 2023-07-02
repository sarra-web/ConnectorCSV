package com.keyrus.proxemconnector.connector.csv.configuration.config;

import com.keyrus.proxemconnector.connector.csv.configuration.repository.jdbcConnector.JDBCJDBCConnectorDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.jdbcConnector.JDBCConnectorJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.jdbcConnector.JDBCConnectorRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.ConnectorJDBCRestHandler;
import com.keyrus.proxemconnector.connector.csv.configuration.service.jdbc.ConnectorJDBCService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class ConnectorJDBCConfig {

    @Bean
    public JDBCConnectorRepository connectorRepository(
            final JDBCConnectorJDBCDatabaseRepository JDBCConnectorJDBCDatabaseRepository
    ) {
        return
                JDBCJDBCConnectorDatabaseRepository.instance(
                        JDBCConnectorJDBCDatabaseRepository
                );
    }

    @Bean
    public ConnectorJDBCService connectorJDBCService(
            final JDBCConnectorRepository JDBCConnectorRepository
    ) {
        return
                ConnectorJDBCService.instance(
                        JDBCConnectorRepository
                );
    }

    @Bean
    public ConnectorJDBCRestHandler connectorJDBCRestHandler(
            final ConnectorJDBCService connectorJDBCService,
            @Value("${connectors.rest.error-header:error}") final String errorHeader,
            final ResourceBundleMessageSource messageSource
    ) {
        return
                ConnectorJDBCRestHandler.instance(
                        connectorJDBCService,
                        errorHeader,
                        messageSource
                );
    }
}
