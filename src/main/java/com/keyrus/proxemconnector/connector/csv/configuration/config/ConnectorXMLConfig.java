package com.keyrus.proxemconnector.connector.csv.configuration.config;

import com.keyrus.proxemconnector.connector.csv.configuration.repository.xmlConnector.XMLConnectorDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.xmlConnector.XMLConnectorJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.xmlConnector.XMLConnectorRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.ConnectorXMLRestHandler;
import com.keyrus.proxemconnector.connector.csv.configuration.service.xml.ConnectorXMLService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
//@ConditionalOnProperty(name = "scheduler.enabled",matchIfMissing = true)
@EnableAsync
@Configuration
public class ConnectorXMLConfig {
    @Bean
    public XMLConnectorRepository XMLConnectorRepository(
            final XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository
    ) {
        return
                XMLConnectorDatabaseRepository.instance(
                        XMLConnectorJDBCDatabaseRepository
                );
    }
    @Bean
    public ConnectorXMLService connectorXMLService(
            final XMLConnectorRepository XMLConnectorRepository
    ) {
        return
                ConnectorXMLService.instance(
                        XMLConnectorRepository
                );
    }
    @Bean
    public ConnectorXMLRestHandler connectorXMLRestHandler(
            final ConnectorXMLService connectorXMLService,
            @Value("${connectors.rest.error-header:error}") final String errorHeader,
            final ResourceBundleMessageSource messageSource
    ) {
        return
                ConnectorXMLRestHandler.instance(
                        connectorXMLService,
                        errorHeader,
                        messageSource
                );
    }




}
