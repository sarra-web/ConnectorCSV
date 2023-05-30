package com.keyrus.proxemconnector.connector.csv.configuration.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ProxemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;

import static com.keyrus.proxemconnector.connector.csv.configuration.service.ConnecteurCSVService.CSVDataToJSON;


@Service
public class ProxemPostService {
    private static final String BASE_POST_URL = "https://studio3.proxem.com/validation5a/api/v1/corpus/a0e04a5f-ab7c-4b0e-97be-af263a61ba49/documents";
    @Autowired
    ConnecteurCSVService connecteurCSVService;

    public List<ProxemDto> pushToProxem(ConnectorDTO config) {
        return this.updatePost(CSVDataToJSON(config));
    }


    public List<ProxemDto> updatePost(Collection<ProxemDto> dtos) {
        List<ProxemDto> proxemDto = null;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "ApiKey mehdi.khayati@keyrus.com:63cdd92e-adb4-42fe-a655-8e54aeb0653f");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String json = ow.writeValueAsString(dtos);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        HttpEntity<Collection<ProxemDto>> entity = new HttpEntity<>(dtos, headers);

        ResponseEntity<Object> response = restTemplate.exchange(BASE_POST_URL, HttpMethod.PUT, entity, Object.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            proxemDto = (List<ProxemDto>) response.getBody();
        }
        return proxemDto;

    }
}

