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



}
