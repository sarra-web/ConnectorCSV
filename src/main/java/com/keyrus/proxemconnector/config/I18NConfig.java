package com.keyrus.proxemconnector.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

@Configuration
public class I18NConfig {
    @Bean
    public MessageSource resourceBundle() {
        final var resourceBundle = new ResourceBundleMessageSource();
        resourceBundle.setBasenames("messages");
        resourceBundle.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return resourceBundle;
    }
}
