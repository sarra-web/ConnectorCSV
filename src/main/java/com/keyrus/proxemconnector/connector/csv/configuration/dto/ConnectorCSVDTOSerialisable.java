package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.FieldDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class ConnectorCSVDTOSerialisable implements Serializable {

    @JsonProperty
            String id;
    @JsonProperty
            String name;
    @JsonProperty
            String separator;
    @JsonProperty
            String encoding;
    @JsonProperty
            String path;
    @JsonProperty
            String quotingCaracter;
    @JsonProperty
            String escapingCaracter;
    @JsonProperty
            boolean containsHeaders;
    @JsonProperty
            Collection<FieldDAO> fields;

            //ProjectDTO projectDTO

}
