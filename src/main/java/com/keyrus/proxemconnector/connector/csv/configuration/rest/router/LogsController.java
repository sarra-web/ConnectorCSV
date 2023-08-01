package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
public class LogsController {
/*
    @GetMapping
    public String getLoggedData() {
        ServletOutputStream loggedData = RequestResponseLoggingFilter.getLoggedData();
        if (loggedData != null) {
            return loggedData.toString();
        } else {
            return "No logged data available";
        }
    }*/
}
