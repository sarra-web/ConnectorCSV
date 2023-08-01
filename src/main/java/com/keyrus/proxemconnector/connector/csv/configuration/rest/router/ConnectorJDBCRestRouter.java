package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorJDBCDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConfigPlusCheck;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorJDBCDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ProxemDto;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.jdbcConnector.JDBCConnectorJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.ConnectorJDBCRestHandler;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.ProjectRestHandler;
import com.keyrus.proxemconnector.connector.csv.configuration.service.UserServiceConnector;
import com.keyrus.proxemconnector.connector.csv.configuration.service.jdbc.ConnectorJDBCService;
import com.keyrus.proxemconnector.connector.csv.configuration.service.log.Logging;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import static com.keyrus.proxemconnector.connector.csv.configuration.service.jdbc.ConnectorJDBCService.JDBCToJSON;
import static com.keyrus.proxemconnector.connector.csv.configuration.service.jdbc.ConnectorJDBCService.JDBCToJSONFromCheckPoint;
import static org.springframework.http.HttpStatus.OK;


@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/configurationJDBC")
@Slf4j
public class ConnectorJDBCRestRouter {
    Logger logger= LoggerFactory.getLogger(ConnectorJDBCRestRouter.class);
    private final JDBCConnectorJDBCDatabaseRepository jdbcConnectorJDBCDatabaseRepository;
    private final ConnectorJDBCRestHandler connectorRestHandler;




    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    public ConnectorJDBCRestRouter(
            final ConnectorJDBCRestHandler connectorRestHandler,
            JDBCConnectorJDBCDatabaseRepository jdbcConnectorJDBCDatabaseRepository,
            ProjectRestHandler projectRestHandler, UserServiceConnector userServiceConnector) {
        this.connectorRestHandler = connectorRestHandler;
        this.jdbcConnectorJDBCDatabaseRepository = jdbcConnectorJDBCDatabaseRepository;



    }

    /*@GetMapping(value = "/findById/{id}",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ConnectorDAO findById(@PathVariable String id) {
        return this.connectorJDBCDatabaseRepository.findOneById(id);
    }*/
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Collection<ConnectorJDBCDTO>> findAll(
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {//logger.info("getAll is called");
        return this.connectorRestHandler.findAll(
                languageCode
        );


    }

   /* @GetMapping("/message")
    public String getMessage(){
        logger.info("[getMessage] info message");
        logger.warn("[getMessage] warn message");
        logger.error("[getMessage] error message");
        logger.debug("[getMessage] debug message");
        logger.trace("[getMessage] trace message");
        return "open console to show";
    }*/


    @GetMapping("/connectors")
    public ResponseEntity<Map<String, Object>> getAllConnectors(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {

        try {
            List<ConnectorJDBCDAO> connectors = new ArrayList<ConnectorJDBCDAO>();
            Pageable paging = PageRequest.of(page, size);

            Page<ConnectorJDBCDAO> pageTuts;

            pageTuts = jdbcConnectorJDBCDatabaseRepository.findByNameContaining(name, paging);

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
    public ResponseEntity<Collection<ConnectorJDBCDTO>> findManyByNameContainsIgnoreCase(
            @PathVariable("name") final String name,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        return this.connectorRestHandler.findManyByNameContainsIgnoreCase(name, languageCode
        );
    }


    @GetMapping(value = "/{name}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorJDBCDTO> findOneByName(
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

    @GetMapping(value = "findById/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorJDBCDTO> findOneById(
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


    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorJDBCDTO> create(
            @RequestBody final ConnectorJDBCDTO connectorJDBCDTO,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        return
                this.connectorRestHandler
                        .create(
                                connectorJDBCDTO,
                                languageCode
                        );
    }



    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorJDBCDTO> update(
            @RequestBody final ConnectorJDBCDTO connectorJDBCDTO,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        return
                this.connectorRestHandler
                        .update(
                                connectorJDBCDTO,
                                languageCode
                        );
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorJDBCDTO> delete(
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
            jdbcConnectorJDBCDatabaseRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/log/{filename}")
    public ResponseEntity<byte[]> getFile(@PathVariable String filename) throws IOException {
        // Lire le contenu du fichier et le renvoyer dans la réponse HTTP
        Path file = Paths.get( filename); // Remplacez "/path/to/files/" par le chemin réel du dossier contenant les fichiers
        byte[] fileContent = Files.readAllBytes(file);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData(filename, filename);

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }


    @PostMapping("/extract")
    public ResponseEntity<List<List<String>>> readJDBC(@RequestBody ConnectorJDBCDTO config) {
        return  new ResponseEntity<>(ConnectorJDBCService.readJDBC(config), OK);
    }
    @PutMapping(value = "/pushToProxem",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> pushToProxem(@RequestBody ConnectorJDBCDTO config){
        List<ProxemDto> proxemDtos = JDBCToJSON(config);
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode jsonArray = objectMapper.valueToTree(proxemDtos);
        String url = "https://studio3.proxem.com/validation5a/api/v1/corpus/"+new ConnectorJDBCDAO(config.toConfiguration().get()).project().getProxemToken()+"/documents";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization","ApiKey mehdi.khayati@keyrus.com:63cdd92e-adb4-42fe-a655-8e54aeb0653f");
        HttpEntity<String> entity = new HttpEntity<>(jsonArray.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        if(response.getStatusCode().toString().startsWith("200")){
            Logging.putInCSV(LocalDateTime.now().toString(),"/pushToProxem","PUT",response.getStatusCode().toString(),countOccurrences(response.getBody().toString(), "\"UpsertSuccessful\":true")+" docs pushed");
            System.out.println("response body"+response.getBody().toString());//count appearence of "UpsertSuccessful":true
        }
        else{
            Logging.putInCSV(LocalDateTime.now().toString(),"/pushToProxem","PUT",response.getStatusCode().toString(),"no docs pushed");
        }

        return response;
    }
    static int countOccurrences(String str, String word)
    {
        // split the string by spaces in a
        String a[] = str.split(",");

        // search for pattern in a
        int count = 0;
        for (int i = 0; i < a.length; i++)
        {
            // if match found increase count
            if (word.equals(a[i]))
                count++;
        }

        return count;
    }
    @PutMapping(value = "/pushToProxemFromcheckPoint",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> pushToProxemFromCheckPoint(@RequestBody ConfigPlusCheck config){
        List<ProxemDto> proxemDtos = JDBCToJSONFromCheckPoint(config.getConnectorJDBCDTO(),config.getCheck());
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode jsonArray = objectMapper.valueToTree(proxemDtos);
        String url = "https://studio3.proxem.com/validation5a/api/v1/corpus/"+new ConnectorJDBCDAO(config.getConnectorJDBCDTO().toConfiguration().get()).project().getProxemToken()+"/documents";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization","ApiKey mehdi.khayati@keyrus.com:63cdd92e-adb4-42fe-a655-8e54aeb0653f");
        HttpEntity<String> entity = new HttpEntity<>(jsonArray.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        if(response.getStatusCode().toString().startsWith("200")){
            Logging.putInCSV(LocalDateTime.now().toString(),"/pushToProxem","PUT",response.getStatusCode().toString(),countOccurrences(response.getBody().toString(), "\"UpsertSuccessful\":true")+" docs pushed");
            System.out.println("response body"+response.getBody().toString());//count appearence of "UpsertSuccessful":true
        }
        else{
            Logging.putInCSV(LocalDateTime.now().toString(),"/pushToProxem","PUT",response.getStatusCode().toString(),"no docs pushed");
        }
        return response;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*@GetMapping("GetProjectByProjectName/{name}")//name unique
    public ResponseEntity<ProjectDTO> GetProjectByProjectName(@PathVariable("name") final String name, @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode){
        //return ResponseEntity.ok(connectorCSVService.getProjectByName(name));
        return projectRestHandler.findOneByName(name,languageCode);
    }*/


////////////////////////////////////////////////////////////////////////ACCSESS TO USER OR PROJECT/////////////////////////////////////////////////////////////////////////////


}
