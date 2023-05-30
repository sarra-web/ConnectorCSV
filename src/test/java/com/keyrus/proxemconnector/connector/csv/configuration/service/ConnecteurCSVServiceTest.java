package com.keyrus.proxemconnector.connector.csv.configuration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ProxemDto;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Connector;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.keyrus.proxemconnector.connector.csv.configuration.service.ConnecteurCSVService.CSVDataToJSON;

class ConnecteurCSVServiceTest {

    @Test
    void extractHeader() {
    }

    @Test
    void CSVDataToJSON_test() {
        List<String> list = List.of("titre","identifiant","texte","meta");
        final var id=UUID.randomUUID().toString();
        final var configuration =
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
                                                        false, list.get(it-1)
                                                )
                                                .get()
                                )
                                .collect(Collectors.toUnmodifiableSet())
                )
                .build()
                .get();

        List<ProxemDto> proxemDtos = CSVDataToJSON(new ConnectorDTO(configuration));
        ObjectMapper objectMapper = new ObjectMapper();
       ArrayNode jsonArray = objectMapper.valueToTree(proxemDtos);
        System.out.println( jsonArray);
    }
}