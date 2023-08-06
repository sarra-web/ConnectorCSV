package com.keyrus.proxemconnector.connector.csv.configuration.service.jdbc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorJDBCDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.FieldDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.Meta;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ProxemDto;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.TextPart;
import com.keyrus.proxemconnector.connector.csv.configuration.service.log.Logging;
import com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ConnectorCSVService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.keyrus.proxemconnector.connector.csv.configuration.service.jdbc.ConnectorJDBCService.getNameColumns;
import static com.keyrus.proxemconnector.connector.csv.configuration.service.jdbc.ConnectorJDBCService.getNumCol;

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


     //@Scheduled(cron ="0 * * ? * *" ) every minute
    // @Async
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

    public static String generateRecordID(int position, String fileName) {
        return String.format("%s_%s_%d",  LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), fileName, position);
    }
    private static String[] addToken(String[] tokens, StringBuilder sb) {
        String[] newTokens = new String[tokens.length + 1];
        System.arraycopy(tokens, 0, newTokens, 0, tokens.length);
        newTokens[tokens.length] = sb.toString();
        return newTokens;
    }
     public static   List<ProxemDto> JDBCDataToJSON(final ConnectorJDBCDAO config)  {
        String query=config.initialQuery();
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

                     SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                     data.setDocUtcDate(format2.parse(values.get(l2.get(0).getPosition() - 1)).toString());
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
        Logging.putInCSV(LocalDateTime.now().toString(),"/pushToProxem","PUT","200","x docs pushed",connectorJDBCDAO.userName());

        List<ProxemDto> proxemDtos = JDBCDataToJSON(connectorJDBCDAO);
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode jsonArray = objectMapper.valueToTree(proxemDtos);
        String url = "https://studio3.proxem.com/validation5a/api/v1/corpus/"+new ConnectorJDBCDAO(connectorJDBCDAO.toConfiguration().get()).project().getProxemToken()+"/documents";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization","ApiKey mehdi.khayati@keyrus.com:63cdd92e-adb4-42fe-a655-8e54aeb0653f");
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
    static int countOccurrences(String str, String word)
    {
        // split the string by spaces in a
        String a[] = str.split(",");

        // search for pattern in a
        int count = 0;
        for (int i = 0; i < a.length; i++)
        {
            // if match found increase count
            if (word.equals(a[i]))
                count++;
        }

        return count;
    }

}
