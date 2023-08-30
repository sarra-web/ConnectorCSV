package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorXMLDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.RepCommune;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.xmlConnector.XMLConnectorJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.ConnectorXMLRestHandler;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.ProjectRestHandler;
import com.keyrus.proxemconnector.connector.csv.configuration.service.UserServiceConnector;
import com.keyrus.proxemconnector.connector.csv.configuration.service.log.Logging;
import com.keyrus.proxemconnector.connector.csv.configuration.service.xml.ConnectorXMLService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.http.HttpStatus.OK;


@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/configurationXML")
@Slf4j
public class ConnectorXMLRestRouter {

    Logger logger= LoggerFactory.getLogger(ConnectorXMLRestRouter.class);

    private final ConnectorXMLRestHandler connectorRestHandler;
    private final RepCommune repCommune;

    private final ConnectorXMLService connectorXMLService;

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    public ConnectorXMLRestRouter(
            final ConnectorXMLRestHandler connectorRestHandler,
            XMLConnectorJDBCDatabaseRepository XMLConnectorJDBCDatabaseRepository1, ProjectRestHandler projectRestHandler, RepCommune repCommune, UserServiceConnector userServiceConnector, ConnectorXMLService connectorXMLService) {
        this.connectorRestHandler = connectorRestHandler;
        // this.projectRestHandler = projectRestHandler;
        this.repCommune = repCommune;

        this.connectorXMLService = connectorXMLService;

    }


    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Collection<ConnectorXMLDTO>> findAll(
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode

    ) {
        return this.connectorRestHandler.findAll(
                languageCode
        );


    }




    @GetMapping("/connectors")
    public ResponseEntity<Map<String, Object>> getAllConnectors(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {

        try {
            List<ConnectorDAO> connectors = new ArrayList<ConnectorDAO>();
            Pageable paging = PageRequest.of(page, size);

            Page<ConnectorDAO> pageTuts;

            pageTuts = repCommune.findByNameContaining(name, paging);

            connectors = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("connectors", connectors);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            return new ResponseEntity<>(response, OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/connectorsUser")
    public ResponseEntity<Map<String, Object>> getAllConnectorsUser(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {

        try {
            List<ConnectorDAO> connectors = new ArrayList<ConnectorDAO>();
            Pageable paging = PageRequest.of(page, size);

            Page<ConnectorDAO> pageTuts;

            pageTuts = repCommune.findByUserName(name, paging);

            connectors = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("connectors", connectors);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            return new ResponseEntity<>(response, OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/NameContainsIgnoreCase/{name}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Collection<ConnectorXMLDTO>> findManyByNameContainsIgnoreCase(
            @PathVariable("name") final String name,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        ResponseEntity<Collection<ConnectorXMLDTO>> response= this.connectorRestHandler.findManyByNameContainsIgnoreCase(name, languageCode
        );
        // Logging.putInXML(LocalDateTime.now().toString(),"/pushToProxem","PUT",response.getStatusCode().toString(),"no docs pushed",response.getBody().stream().collect(Collectors.toSet()).toArray()..userName());
        return response;
    }


    @GetMapping(value = "/{name}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorXMLDTO> findOneByName(
            @PathVariable("name") final String name,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        return
                this.connectorRestHandler
                        .findOneByName(
                                name,
                                languageCode
                        );
    }

    @GetMapping("/connectorsByProjectName")
    public ResponseEntity<Map<String, Object>> getAllConnectorsByProjectName(
            @RequestParam(defaultValue = "") String userName,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {

        try {
            List<ConnectorDAO> connectors = new ArrayList<ConnectorDAO>();
            Pageable paging = PageRequest.of(page, size);

            Page<ConnectorDAO> pageTuts;

            pageTuts = repCommune.findByProjectName(name, paging);

            connectors = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("connectors", connectors);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            return new ResponseEntity<>(response, OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/connectorsByProjectNameAndUser")
    public ResponseEntity<Map<String, Object>> getAllConnectorsByProjectNameAndUser(
            @RequestParam(defaultValue = "") String userName,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {

        try {
            List<ConnectorDAO> connectors = new ArrayList<ConnectorDAO>();
            Pageable paging = PageRequest.of(page, size);

            Page<ConnectorDAO> pageTuts;

            pageTuts = (Page<ConnectorDAO>) repCommune.findByProjectName(name, paging).and(repCommune.findByUserName(userName,paging));


            connectors = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("connectors", connectors);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            return new ResponseEntity<>(response, OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "findById2/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorDAO> findOneById2(
            @PathVariable("id") final String id

    ) {
        return
                new ResponseEntity<> (this.repCommune.findById(id).get(), OK);
    }
    @GetMapping(value = "findById/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorXMLDTO> findOneById(
            @PathVariable("id") final String id,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        return
                this.connectorRestHandler
                        .findOneById(
                                id,
                                languageCode
                        );
    }


    @PostMapping(value = "/CreateXMLConnector",produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorXMLDTO> create(
            @RequestBody final ConnectorXMLDTO connectorXMLDTO,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        ResponseEntity<ConnectorXMLDTO> a=
                this.connectorRestHandler
                        .create(
                                connectorXMLDTO,
                                languageCode
                        );
        Logging.putInCSV(LocalDateTime.now().toString(),"configuration/CreateXMLConnector","POST",a .getStatusCode().toString(),"Adding new XMLconnector",connectorXMLDTO.userName());

        return a;

    }



    @PutMapping(value="/UpadateXMLconnector",produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorXMLDTO> update(
            @RequestBody final ConnectorXMLDTO connectorXMLDTO,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {

        ResponseEntity<ConnectorXMLDTO> b=    this.connectorRestHandler
                .update(
                        connectorXMLDTO,
                        languageCode
                );

        Logging.putInCSV(LocalDateTime.now().toString(),"configuration/UpdateXMLConnector","PUT",b .getStatusCode().toString(),"Updating "+connectorXMLDTO.name()+ " XMLconnector",connectorXMLDTO.userName());
        return b;
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorXMLDTO> delete(
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

    @DeleteMapping("/connectors")
    public ResponseEntity<HttpStatus> deleteAllConnectors() {
        try {
            repCommune.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PutMapping(value = "/pushToProxem",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> pushToProxem(@RequestBody ConnectorXMLDTO config) throws ParserConfigurationException, IOException, SAXException {

        return ConnectorXMLService.pushToProxem(config);
    }

    @PostMapping("/extract")
    public ResponseEntity<List<List<String>>> readXML(@RequestBody ConnectorXMLDTO config) throws ParserConfigurationException, IOException, SAXException {
        return  new ResponseEntity<>(ConnectorXMLService.ReadXML(config), OK);
    }

}