package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;


import com.keyrus.proxemconnector.connector.csv.configuration.dto.ProjectDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.ProjectRestHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/project")
public class ProjectRestRouter {

    private final ProjectRestHandler projectRestHandler;

    public ProjectRestRouter(
            final ProjectRestHandler projectRestHandler
    ) {
        this.projectRestHandler = projectRestHandler;
    }
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Collection<ProjectDTO>> findAll(
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        return this.projectRestHandler.findAll(
                languageCode
        );
    }


    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ProjectDTO> create(
            @RequestBody final ProjectDTO projectDTO,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        return
                this.projectRestHandler
                        .create(
                                projectDTO,
                                languageCode
                        );
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ProjectDTO> update(
            @RequestBody final ProjectDTO projectDTO,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        return
                this.projectRestHandler
                        .update(
                                projectDTO,
                                languageCode
                        );
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ProjectDTO> delete(
            @PathVariable("id") final String id,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        return
                this.projectRestHandler
                        .delete(
                                id,
                                languageCode
                        );
    }
}

