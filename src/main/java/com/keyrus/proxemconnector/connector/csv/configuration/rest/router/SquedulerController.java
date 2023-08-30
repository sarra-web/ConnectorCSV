package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.SquedulerDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.exception.ResourceNotFoundException;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.RepCommune;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.SquedulerJDBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

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
        return new ResponseEntity<>(squedulerDAOs, OK);
    }
    @GetMapping("pagination/ConnectorDAOs/{ConnectorDAOId}/SquedulerDAOs")
    public ResponseEntity<Map<String, Object>> SquedulerDAOsByConnectorDAOIdWithPagination(
            @PathVariable String ConnectorDAOId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {

        try {
            List<SquedulerDAO> schedulers = new ArrayList<SquedulerDAO>();
            Pageable paging = PageRequest.of(page, size);

            Page<SquedulerDAO> pageTuts;

            pageTuts = squedulerJDBC.findByConnectorDAOId(ConnectorDAOId,paging);

            schedulers = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("schedulers", schedulers);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            return new ResponseEntity<>(response, OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
<<<<<<< HEAD
=======










>>>>>>> 19e97ebe77e87f9c7cbee704e10b94b253111afa


    @GetMapping("/SquedulerDAOs/{id}")
    public ResponseEntity<SquedulerDAO> getSquedulerById(@PathVariable(value = "id") Long id) {
        SquedulerDAO SquedulerDAO = squedulerJDBC.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found SquedulerDAO with id = " + id));

        return new ResponseEntity<>(SquedulerDAO, OK);
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
<<<<<<< HEAD
        squedulerDAO.setStartsTime(squedulerDAORequest.getStartsTime());
        squedulerDAO.setEndTime(squedulerDAORequest.getEndTime());
        squedulerDAO.setScanMode(squedulerDAORequest.getScanMode());
        squedulerDAO.setCronExpression(squedulerDAORequest.getCronExpression());
        squedulerDAO.setJobId(squedulerDAORequest.getJobId());
=======

>>>>>>> 19e97ebe77e87f9c7cbee704e10b94b253111afa
        return new ResponseEntity<>(squedulerJDBC.save(squedulerDAO), OK);
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
