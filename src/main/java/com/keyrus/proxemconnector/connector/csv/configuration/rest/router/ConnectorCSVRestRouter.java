package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorCSVDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ProjectDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ProxemDto;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.RepCommune;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector.CSVConnectorJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.ConnectorCSVRestHandler;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.handler.ProjectRestHandler;
import com.keyrus.proxemconnector.connector.csv.configuration.rest.router.log.Logging;
import com.keyrus.proxemconnector.connector.csv.configuration.service.UserServiceConnector;
import com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ConnectorCSVService;
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

import java.util.*;

import static com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ConnectorCSVService.CSVDataToJSON;
import static org.springframework.http.HttpStatus.OK;


@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/configuration")
@Slf4j
public class ConnectorCSVRestRouter {

Logger logger= LoggerFactory.getLogger(ConnectorCSVRestRouter.class);
    private final CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository;
    private final ConnectorCSVRestHandler connectorRestHandler;
    private final ProjectRestHandler projectRestHandler;
    private final RepCommune repCommune;
    private final UserServiceConnector userServiceConnector;
    private final Logging logging;

    private final ConnectorCSVService connectorCSVService;

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    public ConnectorCSVRestRouter(
            final ConnectorCSVRestHandler connectorRestHandler,
            CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository1, ProjectRestHandler projectRestHandler, RepCommune repCommune, UserServiceConnector userServiceConnector, Logging logging, ConnectorCSVService connectorCSVService) {
        this.connectorRestHandler = connectorRestHandler;
        this.CSVConnectorJDBCDatabaseRepository = CSVConnectorJDBCDatabaseRepository1;
        this.projectRestHandler = projectRestHandler;
        this.repCommune = repCommune;
        this.userServiceConnector = userServiceConnector;
        this.logging = logging;
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
        /*List<String> l=List.of(LocalDateTime.now().toString(),"/configuration","Get","Body","","Response Body ");
        listLogs.add(l);
        logger.info("getAll is called");*/

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

    @GetMapping("/connectorsByProjectName")
    public ResponseEntity<Map<String, Object>> getAllConnectorsByProjectName(
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

    @GetMapping(value = "findById2/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorDAO> findOneById2(
            @PathVariable("id") final String id

    ) {
        return
                new ResponseEntity<> (this.repCommune.findById(id).get(), OK);
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
    @PostMapping(value = "add/{idProject}",produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectorCSVDTO> create2(@PathVariable(value = "idProject") final String idProject,
            @RequestBody final ConnectorCSVDTO connectorCSVDTO,
            @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode
    ) {
        return
                this.connectorRestHandler
                        .create2(
                                idProject,connectorCSVDTO,
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
            repCommune.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
   @PutMapping(value = "/pushProxem",consumes = MediaType.APPLICATION_JSON_VALUE)
    public Collection<ProxemDto> pushProxem(@RequestBody ConnectorCSVDTO config){
       Collection<ProxemDto> pushToProxem= connectorCSVService.pushToProxem(config);
       System.out.println(pushToProxem);
        return pushToProxem;
    }
  /* @PutMapping(value = "/pushToProxem",consumes = MediaType.APPLICATION_JSON_VALUE)
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

     /*   int j=0;
        for (int i = 0; i < response.getBody().length(); i = i + 1){
            if(response[i].UpsertSuccessful===true){
                j=j+1;
            }}
             Logging.putInCSV("","","",bo)
                System.out.println("body response"+response.getBody().+"nombre"+response.getBody().length());
            */

        return response;
    }
    @GetMapping(value = "csvToJson")
    public Collection<ProxemDto> csvToJson(@RequestBody ConnectorCSVDTO config){
        System.out.println(config);
        return  CSVDataToJSON(config);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("GetProjectByProjectName/{name}")//name unique
    public ResponseEntity<ProjectDTO> GetProjectByProjectName(@PathVariable("name") final String name, @RequestParam(name = "languageCode", required = false, defaultValue = "en") final String languageCode){
        //return ResponseEntity.ok(connectorCSVService.getProjectByName(name));
        return projectRestHandler.findOneByName(name,languageCode);
    }
    @GetMapping("GetUserByUserId/{id}")
    public ResponseEntity<?> GetUserById(@PathVariable("id") final Long id){
        return ResponseEntity.ok(userServiceConnector.getUserById(id));
    }

////////////////////////////////////////////////////////////////////////ACCSESS TO USER OR PROJECT/////////////////////////////////////////////////////////////////////////////











   /* public static void main(String[] args) {
        Timer timer = new Timer();
        int a=2;
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
    }*/






}