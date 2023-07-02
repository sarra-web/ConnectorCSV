package com.keyrus.proxemconnector.connector.csv.configuration.config;


import com.keyrus.proxemconnector.connector.csv.configuration.rest.router.log.RequestResponseLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    FilterRegistrationBean<RequestResponseLoggingFilter> createLoggers(RequestResponseLoggingFilter requestResponseLoggers){
        FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(requestResponseLoggers);
        registrationBean.addUrlPatterns("/configuration/*", "/configurationJDBC/*","/project/*");

        return registrationBean;
    }
}
