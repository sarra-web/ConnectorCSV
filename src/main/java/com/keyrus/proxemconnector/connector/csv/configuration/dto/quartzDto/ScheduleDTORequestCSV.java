package com.keyrus.proxemconnector.connector.csv.configuration.dto.quartzDto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorCSVDAO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class ScheduleDTORequestCSV {
 /*  @Email
    @NotEmpty
    private String email;*/

    //@NotEmpty
   // private String subject;

    //@NotEmpty
   // private String body;


    private String cronExpression;
    //les restes des attriut du scheduler

   @NotNull
    @JsonProperty
   ConnectorCSVDAO connectorDAO;

    @NotNull
    private LocalDateTime dateTime;

    private LocalDateTime endTime;

    @NotNull
    private ZoneId timeZone;
}
