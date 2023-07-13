package com.keyrus.proxemconnector.connector.csv.configuration.service.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorJDBCDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.*;
import com.keyrus.proxemconnector.connector.csv.configuration.enumerations.QueryMode;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.jdbcConnector.JDBCConnectorRepository;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.sql.*;
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
public final class ConnectorJDBCService {
    private static final String BASE_POST_URL = "https://studio3.proxem.com/validation5a/api/v1/corpus/a0e04a5f-ab7c-4b0e-97be-af263a61ba49/documents";


    private static ConnectorJDBCService instance = null;

    public static ConnectorJDBCService instance(
            final JDBCConnectorRepository JDBCConnectorRepository
    ) {
        if (Objects.isNull(instance))
            instance =
                    new ConnectorJDBCService(
                            JDBCConnectorRepository
                    );
        return instance;
    }

    private final JDBCConnectorRepository JDBCConnectorRepository;

    private ConnectorJDBCService(
            final JDBCConnectorRepository JDBCConnectorRepository
    ) {
        this.JDBCConnectorRepository = JDBCConnectorRepository;
    }

    public Either<Error, ConnectorJDBC> create(
            final ConnectorJDBC connectorJDBC
    ) {
        return
                this.JDBCConnectorRepository
                        .create(
                                connectorJDBC
                        )
                        .mapLeft(ConnectorJDBCService::repositoryErrorToServiceError);
    }
    public Either<Error, ConnectorJDBC> create2(
            final ConnectorJDBC connectorJDBC ,final String idProject
    ) {
        return
                this.JDBCConnectorRepository
                        .create2(
                                connectorJDBC,idProject
                        )
                        .mapLeft(ConnectorJDBCService::repositoryErrorToServiceError);
    }

    public Either<Error, ConnectorJDBC> update(
            final ConnectorJDBC connectorJDBC
    ) {
        return
                this.JDBCConnectorRepository
                        .update(
                                connectorJDBC
                        )
                        .mapLeft(ConnectorJDBCService::repositoryErrorToServiceError);
    }

    public Either<Error, ConnectorJDBC> delete(
            final String id
    ) {
        return
                this.JDBCConnectorRepository
                        .delete(
                                id
                        )
                        .mapLeft(ConnectorJDBCService::repositoryErrorToServiceError);
    }

    public Either<Error, Collection<ConnectorJDBC>> findAll() {
        return this.JDBCConnectorRepository
                .findAll()
                .mapLeft(ConnectorJDBCService::repositoryErrorToServiceError);

    }
    public Either<Error, ConnectorJDBC> findOneByName(String name) {
        return this.JDBCConnectorRepository
                .findOneByName(
                        name
                )
                .mapLeft(ConnectorJDBCService::repositoryErrorToServiceError);
    }
    public Either<Error, ConnectorJDBC> findOneById(String id) {
        return this.JDBCConnectorRepository
                .findOneById(
                        id
                )
                .mapLeft(ConnectorJDBCService::repositoryErrorToServiceError);
    }

    public Either<Error, Collection<ConnectorJDBC>> findManyByNameContainsIgnoreCase(String name) {
        return this.JDBCConnectorRepository
                .findManyByNameContainsIgnoreCase(name)
                .mapLeft(ConnectorJDBCService::repositoryErrorToServiceError);

    }
    public Page<ConnectorJDBCDAO> findAll(int page, int size){
        log.info("Fetching for page {} of size {}",page,size);
        return JDBCConnectorRepository.findAll(PageRequest.of(page,size));
    }

    public static String generateRecordID(int position, String tableName) {
        return String.format("%s_%s_%d",  LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), tableName, position);
    }

    public static int getNumCol(String className, String password, String jdbcUrl, String username, String tableName,String query) {
        int columnCount = 0;
        try {
            Class.forName(className);
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            columnCount = metaData.getColumnCount();
        } catch (Exception e) {
            System.out.println(e);
        }
        return columnCount;
    }

    public static List<String> getNameColumns(String className, String password, String jdbcUrl, String username, String tableName, int columnCount) {
        List<String> columnNames = new ArrayList<>();
        try {
            Class.forName(className);
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, null, tableName, null);


            int i=1;
            while ((resultSet.next())&&(i<=columnCount)) {
                i++;
                String columnName = resultSet.getString("COLUMN_NAME");
                columnNames.add(columnName);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return columnNames;
    }

    public static List<List<String>>  readJDBC(ConnectorJDBCDTO jdbcdto) {
        String query="";
        String query2="";
        if(jdbcdto.mode()== QueryMode.Full){
            query = jdbcdto.initialQuery();}
        else{

            String query1 = jdbcdto.incrementalQuery();
            String var=jdbcdto.incrementalVariable();
            query = query1.replace("$(a)", "2022-04-23 10:34:23");
            query2="Select * from"+jdbcdto.tableName()+"where"+jdbcdto.checkpointColumn()+">="+/*$(jdbcdto.incrementalVariable())*/"2022-04-22 10:34:23";
        }
        int numCol=getNumCol(jdbcdto.className(), jdbcdto.password(), jdbcdto.jdbcUrl(), jdbcdto.username(), jdbcdto.tableName(),query);
        List<List<String>> l=new ArrayList<List<String>>();
        l.add(getNameColumns( jdbcdto.className(),  jdbcdto.password(),  jdbcdto.jdbcUrl(), jdbcdto.username(), jdbcdto.tableName(),numCol));
        try {
            Class.forName(jdbcdto.className());
            Connection connection = DriverManager.getConnection(jdbcdto.jdbcUrl(), jdbcdto.username(), jdbcdto.password());
            Statement statement = connection.createStatement();
            ResultSet resultSet= statement.executeQuery(query);


            int k=1;
            while ((resultSet.next())&&(k<=5)) {
                k++;
                List<String> values = new ArrayList<>();
                for (int i = 1; i <= numCol; i++) {
                    values.add(resultSet.getString(i));
                }
                l.add(values);
                //  System.out.println(values);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return l;
    }
   public static List<ProxemDto>  JDBCToJSON(ConnectorJDBCDTO jdbcdto){
      // "Select * from "+jdbcdto.tableName()
       String query="";
       if(jdbcdto.mode()== QueryMode.Full){
          query = jdbcdto.initialQuery();}
       else{
           query = jdbcdto.incrementalQuery();
       }
       List<ProxemDto> dataList = new ArrayList<>();
       int numCol=getNumCol(jdbcdto.className(), jdbcdto.password(), jdbcdto.jdbcUrl(), jdbcdto.username(), jdbcdto.tableName(),query);
       List<String> nameCols=getNameColumns( jdbcdto.className(),  jdbcdto.password(),  jdbcdto.jdbcUrl(), jdbcdto.username(), jdbcdto.tableName(),numCol);

       try{
            Class.forName(jdbcdto.className());
            Connection connection= DriverManager.getConnection(jdbcdto.jdbcUrl(),jdbcdto.username(),jdbcdto.password());
            Statement statement=connection.createStatement();

           ResultSet resultSet= statement.executeQuery(query);


           ResultSetMetaData metaData= resultSet.getMetaData();
           int position = 0;
           String line;
            while (resultSet.next()){
                List<String> values=new ArrayList<>();
                for (int i=1;i<=numCol;i++){
                    values.add(resultSet.getString(i));
                }
                ProxemDto data = new ProxemDto();
                position++;//pp les lignes
                data.setCorpusId("a0e04a5f-ab7c-4b0e-97be-af263a61ba49"/*config.getProject().getProjectName() ou project id nom doit etre unique*/);
                List<FieldDTO> l =  jdbcdto.fields().stream().filter(field1 -> field1.fieldType().toString()=="Identifier").collect(Collectors.toList());
                if ((l.isEmpty() )) {
                    String recordId = generateRecordID(position, jdbcdto.tableName());
                    data.setExternalId(recordId);
                } else {
                    data.setExternalId(position+"_"+ values.get(l.get(0).position() - 1));//pour garantir l'unicité car les donne provenant des fichier j'ai pas controle sur eux
                }
                List<FieldDTO> l2 = jdbcdto.fields().stream().filter(field1 -> field1.fieldType().toString()=="Date").collect(Collectors.toList());
                if (l2.isEmpty()) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    data.setDocUtcDate(LocalDateTime.now().toString());
                } else {

                    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    data.setDocUtcDate(format2.parse(values.get(l2.get(0).position() - 1)).toString());
                }
                Collection<Meta> metasList = new ArrayList<>();


                List<FieldDTO> l22= jdbcdto.fields().stream().filter(field1 -> field1.included()==true
                ).filter(field1 -> field1.fieldType().toString()=="Meta").toList();
                if(!l22.isEmpty()) {
                    l22.forEach(x -> {
                        Meta meta = new Meta();
                        meta.setName(x.meta());
                        meta.setValue(values.get(x.position() - 1));
                        metasList.add(meta);
                    });
                }
                data.setMetas(metasList);
                List<TextPart> textPartsList = new ArrayList<>();

                TextPart titlePart = new TextPart();
                titlePart.setName("title");
                List<FieldDTO> l3=jdbcdto.fields().stream().filter(field1 -> field1.fieldType().toString()=="Title").collect(Collectors.toList());
                if (!l3.isEmpty()){
                    String value = values.get(l3.get(0).position() - 1);
                    titlePart.setContent(value);
                    textPartsList.add(titlePart);}
                TextPart bodyPart = new TextPart();
                jdbcdto.fields().stream().filter(field1 -> field1.fieldType().toString()=="Text").collect(Collectors.toList()).forEach(x -> {

                    bodyPart.setName("body");
                    if(bodyPart.getContent()!=null){
                        bodyPart.setContent(bodyPart.getContent().toString()+ " ;" + values.get(x.position() - 1));}
                    else{
                        bodyPart.setContent(values.get(x.position() - 1));}
                    //concatenation of text fields otherwise each one will be considered as a ProxemDto
                });
                textPartsList.add(bodyPart);
                data.setTextParts(textPartsList);
                dataList.add(data);
            }
       }
       catch (Exception  e) {
           e.printStackTrace();
       }
       return dataList;

        }


//mettre dans les testes
    /*public static void main(String[] args){
       ConnectorJDBC connectorJDBC= ConnectorJDBC.Builder
                .builder()
                .withId("test")
                .withName(UUID.randomUUID().toString())
                .withclassName("com.mysql.cj.jdbc.Driver")
                .withjdbcUrl("jdbc:mysql://localhost:3306/testdb_spring")
                .withpassword("123456")
                .withtableName("tutorials")
                .withusername("root")
                .withHeaders(

                        Collections.singleton(Field.of(
                                        UUID.randomUUID().toString(),
                                        "test",
                                        UUID.randomUUID().toString(),
                                        1,
                                        UUID.randomUUID().toString(),
                                        FieldType.Text,
                                        false, true
                                )
                                .get())
                                )
                .build()
                .get();
        System.out.println(connectorJDBC);

        ConnectorJDBCDTO jdbcdto=new ConnectorJDBCDTO(connectorJDBC);


}*/

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

/*    public List<ProxemDto> pushToProxem(ConnectorJDBCDTO config) {

        return this.updatePost(CSVDataToJSON(config));
    }*/


    private static Error repositoryErrorToServiceError(
            final JDBCConnectorRepository.Error repositoryError
    ) {
        if (repositoryError instanceof JDBCConnectorRepository.Error.IO io)
            return new Error.IO(io.message());
        if (repositoryError instanceof JDBCConnectorRepository.Error.AlreadyExist)
            return new Error.AlreadyExist();
        if (repositoryError instanceof JDBCConnectorRepository.Error.NotFound)
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
