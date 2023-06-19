package com.keyrus.proxemconnector.connector.csv.configuration.service.csv;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SchedulerService {
    Logger log = LoggerFactory.getLogger(SchedulerService.class);
  //@Scheduled(initialDelay =2000, fixedRateString = "PT02S")
final String a="2 * * * * *" ;
 // @Scheduled(cron = a)
 // @Async
    public void scan() throws InterruptedException {
        //Thread.sleep(4000);
        log.info("scaner", LocalDateTime.now());
    }

}
