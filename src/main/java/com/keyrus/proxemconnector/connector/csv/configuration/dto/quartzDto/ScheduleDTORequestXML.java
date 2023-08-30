package com.keyrus.proxemconnector.connector.csv.configuration.dto.quartzDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorXMLDAO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class ScheduleDTORequestXML {
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
    ConnectorXMLDAO connectorDAO;

    @NotNull
    private LocalDateTime dateTime;

    private LocalDateTime endTime;

    @NotNull
    private ZoneId timeZone;
}
