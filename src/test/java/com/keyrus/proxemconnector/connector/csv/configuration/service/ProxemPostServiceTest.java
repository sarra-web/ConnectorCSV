package com.keyrus.proxemconnector.connector.csv.configuration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ProxemDto;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Connector;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.keyrus.proxemconnector.connector.csv.configuration.service.ConnecteurCSVService.CSVDataToJSON;

class ProxemPostServiceTest {

    @Test
    void updatePost_test() {
        UUID uuid = UUID.randomUUID();
        List<String> list = List.of("titre", "identifiant", "texte", "meta");
        final var id = UUID.randomUUID().toString();
        final var connnectorCSV =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withFolderToScan("email.csv")
                        .withArchiveFolder(UUID.randomUUID().toString())
                        .withFailedRecordsFolder(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(4)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                true,
                                                                false, list.get(it - 1)
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();


        List<ProxemDto> proxemDtos = CSVDataToJSON(new ConnectorDTO(connnectorCSV));
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode metas1 = objectMapper.createArrayNode();
        ObjectNode meta1 = objectMapper.createObjectNode();

        ArrayNode jsonArray = objectMapper.valueToTree(proxemDtos);


        String url = "https://studio3.proxem.com/validation5a/api/v1/corpus/a0e04a5f-ab7c-4b0e-97be-af263a61ba49/documents";
        String json="[{\n" +
                "   \"CorpusId\":\"a0e04a5f-ab7c-4b0e-97be-af263a61ba49\",\n" +
                "  \"ExternalId\":\"AA\",\n" +
                "  \"DocUtcDate\": \"2023-01-04T15:00:00Z\",\n" +
                "  \"Metas\": [{\"Name\": \"Type\", \"Value\": \"BigCar\" }],\n" +
                "  \"TextParts\":[\n" +
                "  {\n" +
                "  \"Name\":\"title\",\n" +
                "  \"Content\":\"BMW2\"\n" +
                "  },\n" +
                "  {\n" +
                "  \"Name\":\"body\",\n" +
                "  \"Content\":\"BMW black and blue\"\n" +
                "  }]\n" +
                " },{\"CorpusId\":\"a0e04a5f-ab7c-4b0e-97be-af263a61ba49\",\n" +
                "  \"ExternalId\":\"BB\",\n" +
                "  \"DocUtcDate\": \"2023-01-04T15:00:00Z\",\n" +
                "  \"Metas\": [{\"Name\": \"Type\", \"Value\": \"Car\" }],\n" +
                "  \"TextParts\":[\n" +
                "  {\n" +
                "  \"Name\":\"title\",\n" +
                "  \"Content\":\"\"\n" +
                "  },\n" +
                "  {\n" +
                "  \"Name\":\"body\",\n" +
                "  \"Content\":\"BMBMBMBM\"\n" +
                "  }]\n" +
                " },{\"CorpusId\":\"a0e04a5f-ab7c-4b0e-97be-af263a61ba49\",\n" +
                "  \"ExternalId\":\"CC\",\n" +
                "  \"DocUtcDate\": \"2023-01-04T15:00:00Z\",\n" +
                "  \"Metas\": [{\"Name\": \"Type\", \"Value\": \"Car\" }],\n" +
                "  \"TextParts\":[\n" +
                "  {\n" +
                "  \"Name\":\"title\",\n" +
                "  \"Content\":\"Clio\"\n" +
                "  },\n" +
                "  {\n" +
                "  \"Name\":\"body\",\n" +
                "  \"Content\":\"Clio red\"\n" +
                "  }]\n" +
                " }]";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization","ApiKey mehdi.khayati@keyrus.com:63cdd92e-adb4-42fe-a655-8e54aeb0653f");
        HttpEntity<String> entity = new HttpEntity<>(jsonArray.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
    }











    }
