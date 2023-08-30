package com.keyrus.proxemconnector.connector.csv.configuration.service.jdbc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorJDBCDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.FieldDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.Meta;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ProxemDto;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.TextPart;
import com.keyrus.proxemconnector.connector.csv.configuration.enumerations.QueryMode;
import com.keyrus.proxemconnector.connector.csv.configuration.service.UserServiceConnector;
import com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ConnectorCSVService;
import com.keyrus.proxemconnector.connector.csv.configuration.service.log.Logging;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.keyrus.proxemconnector.connector.csv.configuration.rest.router.ConnectorJDBCRestRouter.countOccurrences;
import static com.keyrus.proxemconnector.connector.csv.configuration.service.jdbc.ConnectorJDBCService.*;

@Component
public class ScanJobJDBC extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(ScanJobJDBC.class);
    public String cron;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Executing Job with key {}", context.getJobDetail().getKey());



      JobDataMap jobDataMap = context.getMergedJobDataMap();
       ConnectorJDBCDAO connectorJDBCDAO = (ConnectorJDBCDAO) jobDataMap.get("config");
        logger.info("this is the config", connectorJDBCDAO);
         this.cron = jobDataMap.getString("cron");

       // @Scheduled(cron="")

        pushToProxem(connectorJDBCDAO);
    }




    public static String maxValue(final ConnectorJDBCDAO config){
        try{
            Class.forName(config.className());
            Connection connection= DriverManager.getConnection(config.jdbcUrl(),config.username(),config.password());
            Statement statement=connection.createStatement();
            String queryGlobal="Select * from "+config.getTableName();
            String queryMax = "SELECT MAX("+config.checkpointColumn()+") AS valeur_max FROM "+ config.getTableName();
            System.out.println(queryMax);
        ResultSet resultSet = statement.executeQuery(queryGlobal);

            ResultSet resultSet2 = statement.executeQuery(queryMax);

           // int columnIndex = resultSet2.findColumn(config.getCheckpointColumn());

            if (resultSet2.next()) {
                java.sql.Timestamp valeurMax = resultSet2.getTimestamp("valeur_max");

                System.out.println("La valeur maximale de ModifiedDate est : " + valeurMax);
                System.out.println("maaaax"+valeurMax.toString());
            return valeurMax.toString();
            }
            else return "error";

    } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
        public static   List<ProxemDto> JDBCDataToJSON(final ConnectorJDBCDAO config)  {
            String  query="";
           if (config.getMode().equals(QueryMode.Incremental)){
         String query1 = config.incrementalQuery();
           query = query1.replace("$("+config.incrementalVariable()+")", maxValue(config));
         System.out.println("voila"+query);}
           else {
                 query=config.initialQuery();
           }

         //   String query=config.initialQuery();

       /*  if(config.mode()== QueryMode.Full){
             query = config.initialQuery();}
         else{
             query = config.incrementalQuery();
         }*/
         List<ProxemDto> dataList = new ArrayList<>();
         int numCol=getNumCol(config.className(), config.password(), config.jdbcUrl(), config.username(), config.tableName(),query);
         List<String> nameCols=getNameColumns( config.className(),  config.password(),  config.jdbcUrl(), config.username(), config.tableName(),numCol);

         try{
             Class.forName(config.className());
             Connection connection= DriverManager.getConnection(config.jdbcUrl(),config.username(),config.password());
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
                 data.setCorpusId(ConnectorCSVService.getProjectByName(config.project().getName()).proxemToken()/*"a0e04a5f-ab7c-4b0e-97be-af263a61ba49"*//*config.getProject().getProjectName() ou project id nom doit etre unique*/);
                 List<FieldDAO> l =  config.fields().stream().filter(field1 -> field1.getType().toString()=="Identifier").collect(Collectors.toList());
                 if ((l.isEmpty() )) {
                     String recordId = generateRecordID(position, config.tableName());
                     data.setExternalId(recordId);
                 } else {
                     data.setExternalId(position+"_"+ values.get(l.get(0).getPosition() - 1));//pour garantir l'unicité car les donne provenant des fichier j'ai pas controle sur eux
                 }
                 List<FieldDAO> l2 = config.fields().stream().filter(field1 -> field1.getType().toString()=="Date").collect(Collectors.toList());
                 if (l2.isEmpty()) {
                     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                     data.setDocUtcDate(LocalDateTime.now().toString());
                 } else {

                     String dateStr=values.get(l2.get(0).getPosition() - 1);
                     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                     LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
                     String isoFormat = dateTime.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
                     //  SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                     // data.setDocUtcDate(format2.parse(values.get(l2.get(0).position() - 1)).toString());
                     data.setDocUtcDate(isoFormat);
                 }
                 Collection<Meta> metasList = new ArrayList<>();


                 List<FieldDAO> l22= config.fields().stream().filter(field1 -> field1.isincluded()==true
                 ).filter(field1 -> field1.getType().toString()=="Meta").toList();
                 if(!l22.isEmpty()) {
                     l22.forEach(x -> {
                         Meta meta = new Meta();
                         meta.setName(x.getMeta());
                         meta.setValue(values.get(x.getPosition() - 1));
                         metasList.add(meta);
                     });
                 }
                 data.setMetas(metasList);
                 List<TextPart> textPartsList = new ArrayList<>();

                 TextPart titlePart = new TextPart();
                 titlePart.setName("title");
                 List<FieldDAO> l3=config.fields().stream().filter(field1 -> field1.getType().toString()=="Title").collect(Collectors.toList());
                 if (!l3.isEmpty()){
                     String value = values.get(l3.get(0).getPosition() - 1);
                     titlePart.setContent(value);
                     textPartsList.add(titlePart);}
                 TextPart bodyPart = new TextPart();
                 config.fields().stream().filter(field1 -> field1.getType().toString()=="Text").collect(Collectors.toList()).forEach(x -> {

                     bodyPart.setName("body");
                     if(bodyPart.getContent()!=null){
                         bodyPart.setContent(bodyPart.getContent().toString()+ " ;" + values.get(x.getPosition() - 1));}
                     else{
                         bodyPart.setContent(values.get(x.getPosition() - 1));}
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
    //@Scheduled(cron = "${cron-string}")
    private void pushToProxem(ConnectorJDBCDAO connectorJDBCDAO) {

        List<ProxemDto> proxemDtos = JDBCDataToJSON(connectorJDBCDAO);
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode jsonArray = objectMapper.valueToTree(proxemDtos);
        String url = "https://studio3.proxem.com/validation5a/api/v1/corpus/"+new ConnectorJDBCDAO(connectorJDBCDAO.toConfiguration().get()).project().getProxemToken()+"/documents";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String mail= UserServiceConnector.getUserByName(connectorJDBCDAO.userName()).get().getEmail() /*"mehdi.khayati@keyrus.com"*/;
        String userToken=UserServiceConnector.getUserByName(connectorJDBCDAO.userName()).get().getUserToken()  /*"63cdd92e-adb4-42fe-a655-8e54aeb0653f"*/;
        headers.add("Authorization", "ApiKey "+mail+":"+userToken);
        HttpEntity<String> entity = new HttpEntity<>(jsonArray.toString(), headers);

        ResponseEntity<String> response =   restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        if(response.getStatusCode().toString().startsWith("200")){
            Logging.putInCSV(LocalDateTime.now().toString(),"/pushToProxem","PUT",response.getStatusCode().toString(),countOccurrences(response.getBody().toString(), "\"UpsertSuccessful\":true")+" docs pushed",connectorJDBCDAO.userName());
            System.out.println("response body"+response.getBody().toString());//count appearence of "UpsertSuccessful":true
        }
        else{
            Logging.putInCSV(LocalDateTime.now().toString(),"/pushToProxem","PUT",response.getStatusCode().toString(),"no docs pushed",connectorJDBCDAO.userName());
        }
    }


}
