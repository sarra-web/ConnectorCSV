package com.keyrus.proxemconnector.connector.csv.configuration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorCSV;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
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
            Collection<FieldDTO> fields;

            //ProjectDTO projectDTO


    public ConnectorCSVDTOSerialisable (
            final ConnectorCSV connectorCSV
    ) {
        this(
                connectorCSV.id(),
                connectorCSV.name(),
                connectorCSV.separator(),
                connectorCSV.encoding(),
                connectorCSV.path(),
                connectorCSV.quotingCaracter(),
                connectorCSV.escapingCaracter(),
                connectorCSV.containsHeaders(),
                ConnectorCSVDTOSerialisable.headersToHeaderDTOs(connectorCSV.fields())
                //,connectorCSV.id()

                //, ConnectorCSVDTO.projectToProjectDTO(connectorCSV.project())
        );
    }
    private static Collection<FieldDTO> headersToHeaderDTOs(
            final Collection<Field> fields
    ) {
        return
                fields.stream()
                        .map(FieldDTO::new)
                        .toList();
    }

}