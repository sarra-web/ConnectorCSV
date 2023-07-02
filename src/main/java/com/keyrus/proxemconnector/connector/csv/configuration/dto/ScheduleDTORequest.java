package com.keyrus.proxemconnector.connector.csv.configuration.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class ScheduleDTORequest {
 /*  @Email
    @NotEmpty
    private String email;*/

    //@NotEmpty
   // private String subject;

    //@NotEmpty
   // private String body;

    private String cron;
    //les restes des attriut du scheduler

    @NotNull
    @JsonProperty
    ConnectorCSVDTO connectorCSVDTO;

    @NotNull
    private LocalDateTime dateTime;

    @NotNull
    private ZoneId timeZone;
}
