package com.keyrus.proxemconnector.connector.csv.configuration.config;

import com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector.CSVConnectorDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector.CSVConnectorJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector.CSVConnectorRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.ConnectorCSVRestHandler;
import com.keyrus.proxemconnector.connector.csv.configuration.service.UserServiceConnector;
import com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ConnectorCSVService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;


@EnableScheduling
//@ConditionalOnProperty(name = "scheduler.enabled",matchIfMissing = true)
@EnableAsync
@Configuration
public class ConnectorCSVConfig {
    @Bean
    public CSVConnectorRepository csvConnectorRepository(
            final CSVConnectorJDBCDatabaseRepository csvConnectorJDBCDatabaseRepository
    ) {
        return
                CSVConnectorDatabaseRepository.instance(
                        csvConnectorJDBCDatabaseRepository
                );
    }
    @Bean
    public ConnectorCSVService connectorCSVService(
            final CSVConnectorRepository csvConnectorRepository
    ) {
        return
                ConnectorCSVService.instance(
                        csvConnectorRepository
                );
    }
    @Bean
    public ConnectorCSVRestHandler connectorRestHandler(
            final ConnectorCSVService connectorCSVService,
            @Value("${connectors.rest.error-header:error}") final String errorHeader,
            final ResourceBundleMessageSource messageSource
    ) {
        return
                ConnectorCSVRestHandler.instance(
                        connectorCSVService,
                        errorHeader,
                        messageSource
                );
    }


///////////////////////
@Bean
public RestTemplate restTemplate() {
    return new RestTemplate();
}
    @Bean
    public UserServiceConnector userServiceConnector(){
        return
                UserServiceConnector.instance(

                );

    }


}
