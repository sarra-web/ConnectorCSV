package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;


import com.keyrus.proxemconnector.connector.csv.configuration.dao.ProjectDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ProjectDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.ProjectJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.HttpResponse;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.ProjectRestHandler;
import com.keyrus.proxemconnector.connector.csv.configuration.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

import static java.time.LocalTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/project")
public class ProjectRestRouter {

    private final ProjectService projectService;
    private final ProjectJDBCDatabaseRepository projectJDBCDatabaseRepository;
    private final ProjectRestHandler projectRestHandler;

    public ProjectRestRouter(
            ProjectJDBCDatabaseRepository projectJDBCDatabaseRepository, ProjectService projectService, ProjectJDBCDatabaseRepository projectJDBCDatabaseRepository1, final ProjectRestHandler projectRestHandler
    ) {
        this.projectService = projectService;
        this.projectJDBCDatabaseRepository = projectJDBCDatabaseRepository1;

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
    @GetMapping(value = "/findById/{id}",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ProjectDAO findById(@PathVariable String id) {
        return this.projectJDBCDatabaseRepository.findOneById(id);
    }

    @GetMapping("/projects")
    public ResponseEntity<HttpResponse> getAllProjects(
                     @RequestParam Optional<String> name,
            @RequestParam(defaultValue = "0") Optional<Integer> page,
            @RequestParam(defaultValue = "3") Optional<Integer> size)throws InterruptedException{
        //throw new RuntimeException("Forced exception for testing");
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("page", projectService.getProjects(name.orElse(""), page.orElse(0), size.orElse(10))))
                        .message("Users Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());

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

    @DeleteMapping("/projects")
    public ResponseEntity<HttpStatus> deleteAllProjects() {
        try {
            projectJDBCDatabaseRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

