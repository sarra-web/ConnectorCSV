package com.keyrus.proxemconnector.connector.csv.configuration.service.xml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorXMLDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorXMLDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ProxemDto;
import com.keyrus.proxemconnector.connector.csv.configuration.service.UserServiceConnector;
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
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.keyrus.proxemconnector.connector.csv.configuration.rest.router.ConnectorJDBCRestRouter.countOccurrences;
import static com.keyrus.proxemconnector.connector.csv.configuration.service.xml.ConnectorXMLService.XMLDataToJSON;

@Component
public class ScanJobXML extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(ScanJobXML.class);
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Executing Job with key {}", context.getJobDetail().getKey());



      JobDataMap jobDataMap = context.getMergedJobDataMap();
       ConnectorXMLDAO connectorXMLDAO = (ConnectorXMLDAO) jobDataMap.get("config");
        logger.info("this is the config", connectorXMLDAO);
        String cron = jobDataMap.getString("cron");
        try {
            pushToProxem(connectorXMLDAO);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private void pushToProxem(ConnectorXMLDAO connectorXMLDAO) throws ParserConfigurationException, IOException, SAXException {

        List<ProxemDto> proxemDtos = XMLDataToJSON(new ConnectorXMLDTO(connectorXMLDAO.toConfiguration().get()));
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode jsonArray = objectMapper.valueToTree(proxemDtos);
        String url = "https://studio3.proxem.com/validation5a/api/v1/corpus/"+new ConnectorXMLDAO(connectorXMLDAO.toConfiguration().get()).project().getProxemToken()+"/documents";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String mail= UserServiceConnector.getUserByName(connectorXMLDAO.userName()).get().getEmail() /*"mehdi.khayati@keyrus.com"*/;
        String userToken=UserServiceConnector.getUserByName(connectorXMLDAO.userName()).get().getUserToken()  /*"63cdd92e-adb4-42fe-a655-8e54aeb0653f"*/;

        headers.add("Authorization", "ApiKey "+mail+":"+userToken);
        HttpEntity<String> entity = new HttpEntity<>(jsonArray.toString(), headers);

        ResponseEntity<String> response =   restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        if(response.getStatusCode().toString().startsWith("200")){
            Logging.putInCSV(LocalDateTime.now().toString(),"/pushToProxem","PUT",response.getStatusCode().toString(),countOccurrences(response.getBody().toString(), "\"UpsertSuccessful\":true")+" docs pushed",connectorXMLDAO.userName());
            System.out.println("response body"+response.getBody().toString());//count appearence of "UpsertSuccessful":true
        }
        else{
            Logging.putInCSV(LocalDateTime.now().toString(),"/pushToProxem","PUT",response.getStatusCode().toString(),"no docs pushed",connectorXMLDAO.userName());
        }

    }


}
