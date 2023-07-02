package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;


import com.keyrus.proxemconnector.connector.csv.configuration.dao.ProjectDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ProjectDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.project.ProjectJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.ProjectRestHandler;
import com.keyrus.proxemconnector.connector.csv.configuration.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
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
    public ResponseEntity<Map<String, Object>> getAllProjects(

            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {

        try {
            List<ProjectDAO> projects = new ArrayList<ProjectDAO>();
            Pageable paging = PageRequest.of(page, size);

            Page<ProjectDAO> pageTuts;

            pageTuts = projectJDBCDatabaseRepository.findByNameContaining(name, paging);

            projects = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("projects", projects);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            return new ResponseEntity<>(response, OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

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

