package com.keyrus.proxemconnector.connector.csv.configuration.config;


import com.keyrus.proxemconnector.connector.csv.configuration.service.FilesStorageServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class uploadFileConfig {
    @Bean
    public FilesStorageServiceImpl FilesStorageServiceImpl () {
        return
                FilesStorageServiceImpl.instance();
    }


}
