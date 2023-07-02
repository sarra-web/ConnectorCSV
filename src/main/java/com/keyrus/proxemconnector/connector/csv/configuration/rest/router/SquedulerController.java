package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.SquedulerDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.exception.ResourceNotFoundException;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.SquedulerJDBC;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector.CSVConnectorJDBCDatabaseRepository;
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
    private CSVConnectorJDBCDatabaseRepository CSVConnectorJDBCDatabaseRepository;

    @Autowired
    private SquedulerJDBC squedulerJDBC;

    @GetMapping("/ConnectorDAOs/{ConnectorDAOId}/SquedulerDAOs")
    public ResponseEntity<List<SquedulerDAO>> getAllSquedulerDAOsByConnectorDAOId(@PathVariable(value = "ConnectorDAOId") String connectorDAOId) {
        if (!CSVConnectorJDBCDatabaseRepository.existsById(connectorDAOId)) {
            throw new ResourceNotFoundException("Not found ConnectorDAO with id = " + connectorDAOId);
        }

        List<SquedulerDAO> squedulerDAOs = squedulerJDBC.findByConnectorDAOId(connectorDAOId);
        return new ResponseEntity<>(squedulerDAOs, HttpStatus.OK);
    }

    @GetMapping("/SquedulerDAOs/{id}")
    public ResponseEntity<SquedulerDAO> getSquedulerById(@PathVariable(value = "id") Long id) {
        SquedulerDAO SquedulerDAO = squedulerJDBC.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found SquedulerDAO with id = " + id));

        return new ResponseEntity<>(SquedulerDAO, HttpStatus.OK);
    }

    @PostMapping("/ConnectorDAOs/{ConnectorDAOId}/SquedulerDAOs")
    public ResponseEntity<SquedulerDAO> createSquedulerDAO(@PathVariable(value = "ConnectorDAOId") String connectorDAOId,
                                                           @RequestBody SquedulerDAO squedulerDAORequest) {
        SquedulerDAO squedulerDAO = CSVConnectorJDBCDatabaseRepository.findById(connectorDAOId).map(connectorDAO -> {
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
        if (!CSVConnectorJDBCDatabaseRepository.existsById(connectorDAOId)) {
            throw new ResourceNotFoundException("Not found ConnectorDAO with id = " + connectorDAOId);
        }

        squedulerJDBC.deleteByConnectorDAOId(connectorDAOId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
