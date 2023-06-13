package com.keyrus.proxemconnector.connector.csv.configuration.config;

import com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector.ConnectorDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector.ConnectorJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector.ConnectorRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.ConnectorRestHandler;
import com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ConnectorCSVService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
//@ConditionalOnProperty(name = "scheduler.enabled",matchIfMissing = true)
@EnableAsync
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
    public ConnectorCSVService configurationService(
            final ConnectorRepository connectorRepository
    ) {
        return
                ConnectorCSVService.instance(
                        connectorRepository
                );
    }

    @Bean
    public ConnectorRestHandler configurationRestHandler(
            final ConnectorCSVService connectorCSVService,
            @Value("${connectors.rest.error-header:error}") final String errorHeader,
            final ResourceBundleMessageSource messageSource
    ) {
        return
                ConnectorRestHandler.instance(
                        connectorCSVService,
                        errorHeader,
                        messageSource
                );
    }
}
