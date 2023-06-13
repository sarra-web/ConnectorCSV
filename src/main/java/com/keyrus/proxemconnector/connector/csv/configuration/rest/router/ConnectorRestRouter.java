package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorCSVDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorCSVDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ProxemDto;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector.ConnectorJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.ConnectorRestHandler;
import com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ConnectorCSVService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ConnectorCSVService.CSVDataToJSON;
import static org.springframework.http.HttpStatus.OK;


@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/configuration")
public class ConnectorRestRouter {


    private final ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository;
    private final ConnectorRestHandler connectorRestHandler;

    private final ConnectorCSVService connectorCSVService;

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    public ConnectorRestRouter(
            final ConnectorRestHandler connectorRestHandler,
            ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository, ConnectorJDBCDatabaseRepository connectorJDBCDatabaseRepository1, ConnectorCSVService connectorCSVService) {
        this.connectorRestHandler = connectorRestHandler;
        this.connectorJDBCDatabaseRepository = connectorJDBCDatabaseRepository1;
        this.connectorCSVService = connectorCSVService;

    }

    /*@GetMapping(value = "/findById/{id}",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ConnectorDAO findById(@PathVariable String id) {
        return this.connectorJDBCDatabaseRepository.findOneById(id);
    }*/
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Collection<ConnectorCSVDTO>> findAll(
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
            List<ConnectorCSVDAO> connectors = new ArrayList<ConnectorCSVDAO>();
            Pageable paging = PageRequest.of(page, size);

            Page<ConnectorCSVDAO> pageTuts;

            pageTuts = connectorJDBCDatabaseRepository.findByNameContaining(name, paging);

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





/* @GetMapping("/connectors")
 public ResponseEntity<HttpResponse> getAllConnectors(

         @RequestParam(defaultValue = "0") Optional<Integer> page,
         @RequestParam(defaultValue = "3") Optional<Integer> size)throws InterruptedException{
     //throw new RuntimeException("Forced exception for testing");
     return ResponseEntity.ok().body(
             HttpResponse.builder()
                     .timeStamp(now().toString())
                     .data(of("page", connectorService.findAll( page.orElse(0), size.orElse(10))))
                     .message("Users Retrieved")
                     .status(OK)
                     .statusCode(OK.value())
                     .build());

 }*/


    @GetMapping(value = "/NameContainsIgnoreCase/{name}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Collection<ConnectorCSVDTO>> findManyByNameContainsIgnoreCase(
            @PathVariable("name") final String name,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        return this.connectorRestHandler.findManyByNameContainsIgnoreCase(name, languageCode
        );
    }


    @GetMapping(value = "/{name}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorCSVDTO> findOneByName(
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
    public ResponseEntity<ConnectorCSVDTO> findOneById(
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
    public ResponseEntity<ConnectorCSVDTO> create(
            @RequestBody final ConnectorCSVDTO connectorCSVDTO,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        return
                this.connectorRestHandler
                        .create(
                                connectorCSVDTO,
                                languageCode
                        );
    }


    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorCSVDTO> update(
            @RequestBody final ConnectorCSVDTO connectorCSVDTO,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        return
                this.connectorRestHandler
                        .update(
                                connectorCSVDTO,
                                languageCode
                        );
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorCSVDTO> delete(
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
            connectorJDBCDatabaseRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
  /* @PutMapping(value = "/pushToProxem",consumes = MediaType.APPLICATION_JSON_VALUE)
    public Collection<ProxemDto> pushToProxem(@RequestBody ConnectorDTO config){
       Collection<ProxemDto> pushToProxem= proxemPostService.pushToProxem(config);
       System.out.println(pushToProxem);
        return pushToProxem;
    }*/
   /*@PutMapping(value = "/pushToProxem",consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> pushToProxem(@RequestBody final ConnectorDTO config){
       return  ResponseEntity.ok(proxemPostService.updatePost(CSVDataToJSON(config)));
   }*/
    //@Scheduled(cron = "2 * * * * *")
    //@Async
    @PutMapping(value = "/pushToProxem",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> pushToProxem(@RequestBody ConnectorCSVDTO config){
        List<ProxemDto> proxemDtos = CSVDataToJSON(config);
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode jsonArray = objectMapper.valueToTree(proxemDtos);
        String url = "https://studio3.proxem.com/validation5a/api/v1/corpus/a0e04a5f-ab7c-4b0e-97be-af263a61ba49/documents";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization","ApiKey mehdi.khayati@keyrus.com:63cdd92e-adb4-42fe-a655-8e54aeb0653f");
        HttpEntity<String> entity = new HttpEntity<>(jsonArray.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        return response;
    }
    @GetMapping(value = "csvToJson")
    public Collection<ProxemDto> csvToJson(@RequestBody ConnectorCSVDTO config){
        System.out.println(config);
        return  CSVDataToJSON(config);
    }

    public static void main(String[] args) {
        Timer timer = new Timer();
        int a=60;
        timer.schedule(new LireFichierTask(), 0, a * 1000); // Planifie la tâche toutes les minutes
    }

    static class LireFichierTask extends TimerTask {

        @Override
        public void run() {
            String cheminFichier = "email.csv";

            try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
                String ligne;
                while ((ligne = reader.readLine()) != null) {
                    // Faites quelque chose avec chaque ligne
                    System.out.println(ligne);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}