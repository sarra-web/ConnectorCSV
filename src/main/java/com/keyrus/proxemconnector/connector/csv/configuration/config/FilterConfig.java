package com.keyrus.proxemconnector.connector.csv.configuration.config;


import com.keyrus.proxemconnector.connector.csv.configuration.service.log.RequestResponseLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    FilterRegistrationBean<RequestResponseLoggingFilter> createLoggers(RequestResponseLoggingFilter requestResponseLoggers){
        FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(requestResponseLoggers);
        registrationBean.addUrlPatterns("/configuration","/configuration/connectors","/configuration/NameContainsIgnoreCase/*", "/configuration/connectorsByProjectName","/configuration/findById2/*","/configuration/findById/*","/configuration/add","/configurationJDBC","/configurationJDBC/connectors","/configurationJDBC/NameContainsIgnoreCase/*", "/configurationJDBC/connectorsByProjectName","/configurationJDBC/findById2/*","/configurationJDBC/findById/*","/configurationJDBC/add","/project/*","/squeduler/*","/ScheduleScanCSV","/ScheduleScanJDBC");
        return registrationBean;
    }
}
