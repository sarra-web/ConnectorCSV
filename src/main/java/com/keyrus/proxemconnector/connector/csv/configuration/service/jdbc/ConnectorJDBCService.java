package com.keyrus.proxemconnector.connector.csv.configuration.service.jdbc;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorJDBCDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.*;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorJDBC;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.jdbcConnector.JDBCConnectorRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ConnectorCSVService;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public final class ConnectorJDBCService {
    private static final String BASE_POST_URL = "https://studio3.proxem.com/validation5a/api/v1/corpus/a0e04a5f-ab7c-4b0e-97be-af263a61ba49/documents";
    private static final String PROJECT_URL= "http://localhost:8080/project";

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



    public  static String testConnection(String className, String password, String jdbcUrl, String username){
        Connection connection = null;
        try {
            // Register the JDBC driver
            Class.forName(className);

            // Open a connection
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // If the connection is successful, print a success message
           // System.out.println("Database connection successful!");
            return "Database connection successful!";
        } catch (SQLException e) {
            // Handle any errors that may occur
            System.out.println("Database connection failed!");
            e.printStackTrace();
            return "Database connection failed!";
        } catch (ClassNotFoundException e) {
            // Handle any errors that may occur while loading the JDBC driver
            System.out.println("JDBC driver not found!");
            e.printStackTrace();
            return "JDBC driver not found!";
        } finally {
            // Close the connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
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
        String query= jdbcdto.initialQuery();
      /*  String query2="";
        if(jdbcdto.mode()== QueryMode.Full){
            query = jdbcdto.initialQuery();}
        else{

            String query1 = jdbcdto.incrementalQuery();
            String var=jdbcdto.incrementalVariable();
            query = query1.replace("$("+var+")", "2022-04-23 10:34:23");
            query2="Select * from"+jdbcdto.tableName()+"where"+jdbcdto.checkpointColumn()+">="+/*$(jdbcdto.incrementalVariable())*///"2022-04-22 10:34:23";
        // }*/
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


    public static List<ProxemDto>  JDBCToJSONFromCheckPoint(final ConnectorJDBCDTO jdbcdto,String check){
        String query1 = jdbcdto.incrementalQuery();
        String  query = query1.replace("$("+jdbcdto.incrementalVariable()+")", check);
        System.out.println("voila"+query);
        // String query2="Select * from"+jdbcdto.tableName()+"where"+jdbcdto.checkpointColumn()+">="+/*$(jdbcdto.incrementalVariable())  "2022-04-22 10:34:23"*/check;
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
                data.setCorpusId(ConnectorCSVService.getProjectByName(jdbcdto.projectName()).proxemToken()/*"a0e04a5f-ab7c-4b0e-97be-af263a61ba49"*//*config.getProject().getProjectName() ou project id nom doit etre unique*/);
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

                    String dateStr=values.get(l2.get(0).position() - 1);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
                    String isoFormat = dateTime.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
                    //  SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    // data.setDocUtcDate(format2.parse(values.get(l2.get(0).position() - 1)).toString());
                    data.setDocUtcDate(isoFormat);
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

    public static List<ProxemDto>  JDBCToJSON(ConnectorJDBCDTO jdbcdto){
        String query=jdbcdto.initialQuery();
        /* "Select * from "+jdbcdto.tableName()
       String query="";
       if(jdbcdto.mode()== QueryMode.Full){
          query = jdbcdto.initialQuery();}
       else{
           query = jdbcdto.incrementalQuery();
       }*/
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
                data.setCorpusId(ConnectorCSVService.getProjectByName(jdbcdto.projectName()).proxemToken()/*"a0e04a5f-ab7c-4b0e-97be-af263a61ba49"*//*config.getProject().getProjectName() ou project id nom doit etre unique*/);
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
                    String dateStr=values.get(l2.get(0).position() - 1);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
                    String isoFormat = dateTime.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
                  //  SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                   // data.setDocUtcDate(format2.parse(values.get(l2.get(0).position() - 1)).toString());
                    data.setDocUtcDate(isoFormat);

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

    //////////////////////////getProjectByName///////////////////////////////////

    public static ProjectDTO getProjectByName(String id ){
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<ProjectDTO> result= restTemplate.getForEntity(PROJECT_URL+"/"+id,ProjectDTO.class);
        return  result.getBody();
    }


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
