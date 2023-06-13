package com.keyrus.proxemconnector.connector.csv.configuration.config;

import com.keyrus.proxemconnector.connector.csv.configuration.repository.project.ProjectDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.project.ProjectJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.project.ProjectRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.ProjectRestHandler;
import com.keyrus.proxemconnector.connector.csv.configuration.service.ProjectService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class ProjectConfig {

    @Bean
    public ProjectRepository projectRepository(
            final ProjectJDBCDatabaseRepository projectJDBCDatabaseRepository
    ) {
        return
                ProjectDatabaseRepository.instance(
                        projectJDBCDatabaseRepository
                );
    }

    @Bean
    public ProjectService projectService(
            final ProjectRepository projectRepository
    ) {
        return
                ProjectService.instance(
                        projectRepository
                );
    }

    @Bean
    public ProjectRestHandler projectRestHandler(
            final ProjectService projectService,
            @Value("${Projects.rest.error-header:error}") final String errorHeader,
            final ResourceBundleMessageSource messageSource
    ) {
        return
                ProjectRestHandler.instance(
                        projectService,
                        errorHeader,
                        messageSource
                );
    }
}
