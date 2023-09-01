package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;


import com.keyrus.proxemconnector.connector.csv.configuration.dto.quartzDto.JobKeyDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.quartzDto.ScheduleDTORequestCSV;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.quartzDto.ScheduleDTOResponse;
import com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ScanJob;
import com.keyrus.proxemconnector.connector.csv.configuration.service.log.Logging;
import jakarta.validation.Valid;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    static class DeleteScheduleDTOResponse {
        private boolean value;

        public DeleteScheduleDTOResponse(boolean value) {
            this.value = value;
        }

        public boolean isValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }}
    @PostMapping("/delete")
    public ResponseEntity<ScanJobSchedulerController.DeleteScheduleDTOResponse> deleteJob(@RequestBody JobKeyDTO jobKeyDTO) throws SchedulerException {

        boolean res=  scheduler.deleteJob(new JobKey(jobKeyDTO.getJobId(),jobKeyDTO.getJobGroup()));

        ScanJobSchedulerController.DeleteScheduleDTOResponse dtoResponse=new ScanJobSchedulerController.DeleteScheduleDTOResponse(res);
        // RestController response =new ResponseEntity<DeleteScheduleDTOResponse>(res) ;
        return ResponseEntity.ok(dtoResponse);
    }

    @PostMapping()
    public ResponseEntity<ScheduleDTOResponse> scheduleScanCSV(@Valid @RequestBody ScheduleDTORequestCSV scheduleDTORequest) throws SchedulerException {
        try {
            ZonedDateTime dateTime = ZonedDateTime.of(scheduleDTORequest.getDateTime(), scheduleDTORequest.getTimeZone());
            if(dateTime.isBefore(ZonedDateTime.now())) {
                ScheduleDTOResponse scheduleDTOResponse = new ScheduleDTOResponse(false,
                        "dateTime must be after current time");
                ResponseEntity<ScheduleDTOResponse> res= ResponseEntity.badRequest().body(scheduleDTOResponse);

                Logging.putInCSV(LocalDateTime.now().toString(),"/ScheduleScanCSV","POST",res.getStatusCode().toString(),"dateTime must be after current time",scheduleDTORequest.getConnectorDAO().userName());
                return res;
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
            ResponseEntity res= ResponseEntity.ok(scheduleDTOResponse);
            Logging.putInCSV(LocalDateTime.now().toString(),"/ScheduleScanCSV","POST",res.getStatusCode().toString(),"PushToProxem Scheduled Successfully!",scheduleDTORequest.getConnectorDAO().userName());
            return res;
        } catch (SchedulerException ex) {
            logger.error("Error scheduling push", ex);

            ScheduleDTOResponse scheduleDTOResponse = new ScheduleDTOResponse(false,
                    "Error scheduling push. Please try later!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleDTOResponse);
        }


    }


    private JobDetail buildJobDetail(ScheduleDTORequestCSV scheduleDTORequest) {
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
