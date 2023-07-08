package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.SquedulerDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.exception.ResourceNotFoundException;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.RepCommune;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.SquedulerJDBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/squeduler")
public class SquedulerController {
    @Autowired
    private RepCommune repConnector;

    @Autowired
    private SquedulerJDBC squedulerJDBC;

    @GetMapping("/ConnectorDAOs/{ConnectorDAOId}/SquedulerDAOs")
    public ResponseEntity<List<SquedulerDAO>> getAllSquedulerDAOsByConnectorDAOId(@PathVariable(value = "ConnectorDAOId") String connectorDAOId) {
        if (!repConnector.existsById(connectorDAOId)) {
            throw new ResourceNotFoundException("Not found ConnectorDAO with id = " + connectorDAOId);
        }

        List<SquedulerDAO> squedulerDAOs = squedulerJDBC.findByConnectorDAOId(connectorDAOId);
        return new ResponseEntity<>(squedulerDAOs, HttpStatus.OK);
    }
   /* @GetMapping("pagination/ConnectorDAOs/{ConnectorDAOId}/SquedulerDAOs")
    public ResponseEntity<Map<String, Object>> SquedulerDAOsByConnectorDAOIdWithPagination(
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
*/











    @GetMapping("/SquedulerDAOs/{id}")
    public ResponseEntity<SquedulerDAO> getSquedulerById(@PathVariable(value = "id") Long id) {
        SquedulerDAO SquedulerDAO = squedulerJDBC.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found SquedulerDAO with id = " + id));

        return new ResponseEntity<>(SquedulerDAO, HttpStatus.OK);
    }

    @PostMapping("/ConnectorDAOs/{ConnectorDAOId}/SquedulerDAOs")
    public ResponseEntity<SquedulerDAO> createSquedulerDAO(@PathVariable(value = "ConnectorDAOId") String connectorDAOId,
                                                           @RequestBody SquedulerDAO squedulerDAORequest) {
        SquedulerDAO squedulerDAO = repConnector.findById(connectorDAOId).map(connectorDAO -> {
            squedulerDAORequest.setConnectorDAO(connectorDAO);
            return squedulerJDBC.save(squedulerDAORequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found ConnectorDAO with id = " + connectorDAOId));

        return new ResponseEntity<>(squedulerDAO, HttpStatus.CREATED);
    }

    @PutMapping("/SquedulerDAOs/{id}")
    public ResponseEntity<SquedulerDAO> updateSquedulerDAO(@PathVariable("id") long id, @RequestBody SquedulerDAO squedulerDAORequest) {
        SquedulerDAO squedulerDAO = squedulerJDBC.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SquedulerDAOId " + id + "not found"));

        squedulerDAO.setName(squedulerDAORequest.getName());

        return new ResponseEntity<>(squedulerJDBC.save(squedulerDAO), HttpStatus.OK);
    }

    @DeleteMapping("/SquedulerDAOs/{id}")
    public ResponseEntity<HttpStatus> deleteSquedulerDAO(@PathVariable("id") long id) {
        squedulerJDBC.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

   @DeleteMapping("/ConnectorDAOs/{ConnectorDAOId}/SquedulerDAOs")
    public ResponseEntity<List<SquedulerDAO>> deleteAllSquedulerDAOsOfConnectorDAO(@PathVariable(value = "ConnectorDAOId") String connectorDAOId) {
        if (!repConnector.existsById(connectorDAOId)) {
            throw new ResourceNotFoundException("Not found ConnectorDAO with id = " + connectorDAOId);
        }

        squedulerJDBC.deleteByConnectorDAOId(connectorDAOId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
