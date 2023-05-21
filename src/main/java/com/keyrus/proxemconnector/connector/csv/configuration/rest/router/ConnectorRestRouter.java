package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;

import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.ConnectorRestHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configuration")
public class ConnectorRestRouter {

    private final ConnectorRestHandler connectorRestHandler;

    public ConnectorRestRouter(
            final ConnectorRestHandler connectorRestHandler
    ) {
        this.connectorRestHandler = connectorRestHandler;
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorDTO> create(
            @RequestBody final ConnectorDTO connectorDTO,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        return
                this.connectorRestHandler
                        .create(
                                connectorDTO,
                                languageCode
                        );
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorDTO> update(
            @RequestBody final ConnectorDTO connectorDTO,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        return
                this.connectorRestHandler
                        .update(
                                connectorDTO,
                                languageCode
                        );
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorDTO> delete(
            @PathVariable("id") final String id,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        return
                this.connectorRestHandler
                        .delete(
                                id,
                                languageCode
                        );
    }
}
