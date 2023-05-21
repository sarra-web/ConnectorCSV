package com.keyrus.proxemconnector.connector.csv.configuration.config;

import com.keyrus.proxemconnector.connector.csv.configuration.repository.ConnectorDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.ConnectorJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.ConnectorRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.ConnectorRestHandler;
import com.keyrus.proxemconnector.connector.csv.configuration.service.ConnectorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class ConfigurationConfig {

    @Bean
    public ConnectorRepository configurationRepository(
            final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository
    ) {
        return
                ConnectorDatabaseRepository.instance(
                        connectorJDBCDatabaseRepository
                );
    }

    @Bean
    public ConnectorService configurationService(
            final ConnectorRepository connectorRepository
    ) {
        return
                ConnectorService.instance(
                        connectorRepository
                );
    }

    @Bean
    public ConnectorRestHandler configurationRestHandler(
            final ConnectorService connectorService,
            @Value("${connectors.rest.error-header:error}") final String errorHeader,
            final ResourceBundleMessageSource messageSource
    ) {
        return
                ConnectorRestHandler.instance(
                        connectorService,
                        errorHeader,
                        messageSource
                );
    }
}
