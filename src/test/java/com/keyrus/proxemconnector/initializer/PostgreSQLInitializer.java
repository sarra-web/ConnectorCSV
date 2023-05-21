package com.keyrus.proxemconnector.initializer;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgreSQLInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try (
                final var postgreSQLContainer =
                        new PostgreSQLContainer<>(
                                DockerImageName.parse("postgres:15.2-alpine3.17")
                        )
                                .withDatabaseName("postgresql")
                                .withUsername("postgresql")
                                .withPassword("postgresql")
        ) {
            postgreSQLContainer.start();
            while (!postgreSQLContainer.isRunning())
                Thread.sleep(1_000);
            TestPropertyValues.of(
                            "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
                            "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl().replace("jdbc:", "jdbc:tc:"),
                            "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                            "spring.datasource.password=" + postgreSQLContainer.getPassword()
                    )
                    .applyTo(applicationContext.getEnvironment());
            applicationContext.addApplicationListener(
                    (ApplicationListener<ContextClosedEvent>) event ->
                            postgreSQLContainer.stop()
            );
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
