package com.keyrus.proxemconnector.connector.csv.configuration.service.csv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorCSVDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.*;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorCSV;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.csvConnector.ConnectorRepository;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public final class ConnectorCSVService {
    private static final String BASE_POST_URL = "https://studio3.proxem.com/validation5a/api/v1/corpus/a0e04a5f-ab7c-4b0e-97be-af263a61ba49/documents";


    private static ConnectorCSVService instance = null;

    public static ConnectorCSVService instance(
            final ConnectorRepository connectorRepository
    ) {
        if (Objects.isNull(instance))
            instance =
                    new ConnectorCSVService(
                            connectorRepository
                    );
        return instance;
    }

    private final ConnectorRepository connectorRepository;

    private ConnectorCSVService(
            final ConnectorRepository connectorRepository
    ) {
        this.connectorRepository = connectorRepository;
    }

    public Either<Error, ConnectorCSV> create(
            final ConnectorCSV connectorCSV
    ) {
        return
                this.connectorRepository
                        .create(
                                connectorCSV
                        )
                        .mapLeft(ConnectorCSVService::repositoryErrorToServiceError);
    }

    public Either<Error, ConnectorCSV> update(
            final ConnectorCSV connectorCSV
    ) {
        return
                this.connectorRepository
                        .update(
                                connectorCSV
                        )
                        .mapLeft(ConnectorCSVService::repositoryErrorToServiceError);
    }

    public Either<Error, ConnectorCSV> delete(
            final String id
    ) {
        return
                this.connectorRepository
                        .delete(
                                id
                        )
                        .mapLeft(ConnectorCSVService::repositoryErrorToServiceError);
    }

    public Either<Error, Collection<ConnectorCSV>> findAll() {
        return this.connectorRepository
                .findAll()
                .mapLeft(ConnectorCSVService::repositoryErrorToServiceError);

    }
    public Either<Error, ConnectorCSV> findOneByName(String name) {
        return this.connectorRepository
                .findOneByName(
                        name
                )
                .mapLeft(ConnectorCSVService::repositoryErrorToServiceError);
    }
    public Either<Error, ConnectorCSV> findOneById(String id) {
        return this.connectorRepository
                .findOneById(
                        id
                )
                .mapLeft(ConnectorCSVService::repositoryErrorToServiceError);
    }

    public Either<Error, Collection<ConnectorCSV>> findManyByNameContainsIgnoreCase(String name) {
        return this.connectorRepository
                .findManyByNameContainsIgnoreCase(name)
                .mapLeft(ConnectorCSVService::repositoryErrorToServiceError);

    }
    public Page<ConnectorCSVDAO> findAll(int page, int size){
        log.info("Fetching for page {} of size {}",page,size);
        return connectorRepository.findAll(PageRequest.of(page,size));
    }

    public static int numRow(String csvFilePath, String stringSeparator) {
        int numRow = 0;
        FileReader fileReader= null;
        try {
            fileReader = new FileReader(csvFilePath);
        } catch (FileNotFoundException e){
            //System.out.println("File doesn't exist put a valid path");
            System.out.println(e.getMessage());

        }
        try (BufferedReader br = new BufferedReader(fileReader)) {
            String line;
            while ((line = br.readLine()) != null) {
                if(!line.trim().isEmpty()) {
                    numRow++;}
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
        return numRow;

    }

    public static int numCol(String csvFilePath, String stringSeparator) {
        int numCol = 0;
        FileReader fileReader= null;
        try {
            fileReader = new FileReader(csvFilePath);
        } catch (FileNotFoundException e){
            //System.out.println("File doesn't exist put a valid path");
            System.out.println(e.getMessage());

        }
        try (BufferedReader br = new BufferedReader(fileReader)) {
            String line;
            line = br.readLine();
            String[] valuesEntete = line.split(stringSeparator);

            numCol = valuesEntete.length;
        } catch (IOException e) {
            e.printStackTrace();

        }
        return numCol;

    }

    public static String generateRecordID(int position, String fileName) {
        return String.format("%s_%s_%d",  LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), fileName, position);
    }


    public static Either <String,List<String>> extractHeader(String stringSeparator, Boolean hasHeader, String csvFilePath) throws FileNotFoundException {
        return hasHeader ? Either.right(getHeader(csvFilePath,true, stringSeparator))
                : Either.left("There is no header");
    }

    public static List<String> getHeader(String stringSeparator, Boolean hasHeader, String csvFilePath) throws FileNotFoundException {
        List<String> header = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line = br.readLine();
            if (line != null) {
                String[] valuesEntete = line.split(stringSeparator);
                for (int i = 0; i < valuesEntete.length; i++) {
                    header.add(valuesEntete[i]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return header;

    }



    public static   List<ProxemDto> CSVDataToJSON(final ConnectorCSVDTO config)  {
        List<ProxemDto> dataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("uploads/"+config.path()))) {

            int position = 0;
            String line;
            if (config.containsHeaders()){
                br.readLine();
            }
            while ((line = br.readLine()) != null) {

                String[] values = parseLine(line,config.separator().charAt(0),config.quotingCaracter().charAt(0),config.escapingCaracter().charAt(0));
                ProxemDto data = new ProxemDto();
                position++;//pp les lignes
                data.setCorpusId("a0e04a5f-ab7c-4b0e-97be-af263a61ba49"/*config.getProject().getProjectName() ou project id nom doit etre unique*/);

                List<FieldDTO> l =  config.headers().stream().filter(field1 -> field1.field_type().startsWith("Id")).collect(Collectors.toList());
                if ((l.isEmpty() )) {
                    String recordId = generateRecordID(position, config.path());
                    data.setExternalId(recordId);
                } else {
                    data.setExternalId(position+"_"+values[l.get(0).position()-1]);//pour garantir l'unicité car les donne provenant des fichier j'ai pas controle sur eux
                }
                List<FieldDTO> l2 = config.headers().stream().filter(field1 -> field1.field_type().startsWith("Da")).collect(Collectors.toList());
                if (l2.isEmpty()) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    data.setDocUtcDate(LocalDateTime.now().toString());
                } else {

                    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    data.setDocUtcDate(format2.parse(values[l2.get(0).position()-1]).toString());
                }
                Collection<Meta> metasList = new ArrayList<>();


                List<FieldDTO> l22= config.headers().stream().filter(field1 -> field1.included()==true
                ).filter(field1 -> field1.field_type().startsWith("Me")).toList();
                if(!l22.isEmpty()) {
                    l22.forEach(x -> {
                        Meta meta = new Meta();
                        meta.setName(x.meta());
                        meta.setValue(values[x.position()-1]);
                        metasList.add(meta);
                    });
                }
                data.setMetas(metasList);
                List<TextPart> textPartsList = new ArrayList<>();

                TextPart titlePart = new TextPart();
                titlePart.setName("title");
                List<FieldDTO> l3=config.headers().stream().filter(field1 -> field1.field_type().startsWith("Tit")).collect(Collectors.toList());
                if (!l3.isEmpty()){
                String value =values[l3.get(0).position()-1];
                titlePart.setContent(value);
                textPartsList.add(titlePart);}
                TextPart bodyPart = new TextPart();
                config.headers().stream().filter(field1 -> field1.field_type().startsWith("Text")).collect(Collectors.toList()).forEach(x -> {

                    bodyPart.setName("body");
                    if(bodyPart.getContent()!=null){
                        bodyPart.setContent(bodyPart.getContent().toString()+ " ;" + values[x.position()-1]);}
                    else{
                        bodyPart.setContent(values[x.position()-1]);}
                    //concatenation of text fields otherwise each one will be considered as a ProxemDto
                });
                textPartsList.add(bodyPart);
                data.setTextParts(textPartsList);
                dataList.add(data);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }
    public static String[] parseLine(String line, char separator, char quote, char escape) {
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        String[] tokens = new String[0];
        if (line == null || line.isEmpty()) {
            return tokens;
        }
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == escape && i + 1 < line.length()) {
                sb.append(line.charAt(++i));
            } else if (c == quote) {
                inQuotes = !inQuotes;
            } else if (c == separator && !inQuotes) {
                tokens = addToken(tokens, sb);
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
        }
        tokens = addToken(tokens, sb);
        return tokens;
    }

    private static String[] addToken(String[] tokens, StringBuilder sb) {
        String[] newTokens = new String[tokens.length + 1];
        System.arraycopy(tokens, 0, newTokens, 0, tokens.length);
        newTokens[tokens.length] = sb.toString();
        return newTokens;
    }
    public List<ProxemDto> updatePost(Collection<ProxemDto> dtos) {
        try{
            List<ProxemDto> proxemDto = null;
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayNode jsonArray = objectMapper.valueToTree(dtos);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.add("Authorization", "ApiKey mehdi.khayati@keyrus.com:63cdd92e-adb4-42fe-a655-8e54aeb0653f");

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                String json = ow.writeValueAsString(jsonArray.toString());
                System.out.println(json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            HttpEntity<String> entity = new HttpEntity<>(jsonArray.toString(), headers);

            ResponseEntity<Object> response = restTemplate.exchange(BASE_POST_URL, HttpMethod.PUT, entity, Object.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                proxemDto = (List<ProxemDto>) response.getBody();
            }
            return proxemDto;
        }
        catch (Exception e){
            throw new RuntimeException("Error: " + e.getMessage());

        }
    }

    public List<ProxemDto> pushToProxem(ConnectorCSVDTO config) {

        return this.updatePost(CSVDataToJSON(config));
    }


    private static Error repositoryErrorToServiceError(
            final ConnectorRepository.Error repositoryError
    ) {
        if (repositoryError instanceof ConnectorRepository.Error.IO io)
            return new Error.IO(io.message());
        if (repositoryError instanceof ConnectorRepository.Error.AlreadyExist)
            return new Error.AlreadyExist();
        if (repositoryError instanceof ConnectorRepository.Error.NotFound)
            return new Error.NotFound();
        throw new IllegalStateException("repository error not mapped to service error");
    }

    public sealed interface Error {

        default String message() {
            return this.getClass().getCanonicalName();
        }

        record IO(
                String message
        ) implements Error {
        }

        record AlreadyExist() implements Error {
        }

        record NotFound() implements Error {
        }
    }
}
