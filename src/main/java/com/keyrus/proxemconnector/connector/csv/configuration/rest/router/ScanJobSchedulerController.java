package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;


import com.keyrus.proxemconnector.connector.csv.configuration.dto.ScheduleDTORequest;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ScheduleDTOResponse;
import com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ScanJob;
import jakarta.validation.Valid;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/ScheduleScanCSV")
public class ScanJobSchedulerController {
    private static final Logger logger = LoggerFactory.getLogger(ScanJobSchedulerController.class);

    @Autowired
    private Scheduler scheduler;
    @PostMapping()
    public ResponseEntity<ScheduleDTOResponse> scheduleScanCSV(@Valid @RequestBody ScheduleDTORequest scheduleDTORequest) throws SchedulerException {



        try {
            ZonedDateTime dateTime = ZonedDateTime.of(scheduleDTORequest.getDateTime(), scheduleDTORequest.getTimeZone());
            if(dateTime.isBefore(ZonedDateTime.now())) {
                ScheduleDTOResponse scheduleDTOResponse = new ScheduleDTOResponse(false,
                        "dateTime must be after current time");
                return ResponseEntity.badRequest().body(scheduleDTOResponse);
            }


            JobDetail jobDetail = buildJobDetail(scheduleDTORequest);
            if( (scheduleDTORequest.getCronExpression()!=null)&&(scheduleDTORequest.getEndTime()!=null)){
                ZonedDateTime dateTimeEnd = ZonedDateTime.of(scheduleDTORequest.getEndTime(), scheduleDTORequest.getTimeZone());

                CronTrigger crontrigger = TriggerBuilder.newTrigger()
                    .withSchedule(CronScheduleBuilder.cronSchedule(scheduleDTORequest.getCronExpression()))
                    .startAt(Date.from(dateTime.toInstant()))
                    .endAt(Date.from(dateTimeEnd.toInstant()))
                    .build();

            scheduler.scheduleJob(jobDetail, crontrigger);}

            else{
             Trigger trigger = buildJobTrigger(jobDetail, dateTime);
            scheduler.scheduleJob(jobDetail, trigger);}

            ScheduleDTOResponse scheduleDTOResponse = new ScheduleDTOResponse(true,
                    jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), "PushToProxem Scheduled Successfully!");
            return ResponseEntity.ok(scheduleDTOResponse);
        } catch (SchedulerException ex) {
            logger.error("Error scheduling push", ex);

            ScheduleDTOResponse scheduleDTOResponse = new ScheduleDTOResponse(false,
                    "Error scheduling push. Please try later!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleDTOResponse);
        }
    }


    private JobDetail buildJobDetail(ScheduleDTORequest scheduleDTORequest) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("config",scheduleDTORequest.getConnectorDAO());
        jobDataMap.put("cron",scheduleDTORequest.getCronExpression());


        return JobBuilder.newJob(ScanJob.class)
                .withIdentity(UUID.randomUUID().toString(), "scan-jobs")
                .withDescription("Send document to proxem Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "scan-triggers")
                .withDescription("Send document to proxem Trigger")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
