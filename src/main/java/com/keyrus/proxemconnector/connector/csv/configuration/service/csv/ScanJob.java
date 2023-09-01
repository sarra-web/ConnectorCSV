package com.keyrus.proxemconnector.connector.csv.configuration.service.csv;

import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorCSVDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorCSVDTO;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ConnectorCSVService.pushToProxem;

@Component
public class ScanJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(ScanJob.class);
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Executing Job with key {}", context.getJobDetail().getKey());



      JobDataMap jobDataMap = context.getMergedJobDataMap();
       ConnectorCSVDAO connectorCSV = (ConnectorCSVDAO) jobDataMap.get("config");
        logger.info("this is the config", connectorCSV);
        String cron = jobDataMap.getString("cron");
        pushToProxem(new ConnectorCSVDTO(connectorCSV.toConfiguration().get()));
    }

     //@Scheduled(cron ="" )
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


}
