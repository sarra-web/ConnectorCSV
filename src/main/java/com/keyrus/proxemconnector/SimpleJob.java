/*
* package com.keyrus.proxemconnector;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class SimpleJob implements Job {
    public SimpleJob() throws SchedulerException {
    }

    @Override
    public void execute(JobExecutionContext context) {
        System.out.println(MessageFormat.format("Job: {0}", getClass()));
    }
    JobDetail job = JobBuilder.newJob(SimpleJob.class).build();
    Date afterFiveSeconds = Date.from(LocalDateTime.now().plusSeconds(5)
            .atZone(ZoneId.systemDefault()).toInstant());
    Trigger trigger = TriggerBuilder.newTrigger()
            .startAt(afterFiveSeconds)
            .build();

    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    Scheduler scheduler = schedulerFactory.getScheduler();

    //scheduler.start();
   // scheduler.scheduleJob(job, trigger);
}


* */