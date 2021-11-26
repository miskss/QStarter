package com.qstarter.app.quartz.job;

import com.qstarter.core.constant.JobKeyConstant;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author peter
 * date: 2019-11-21 14:25
 **/
@Component
@Slf4j
public class QuartzJobDemo extends QuartzJobBean {

    public static final String JOB_KEY = JobKeyConstant.JOB_KEY_PREFIX + QuartzJobDemo.class.getSimpleName();
    public static final String JOB_GROUP_KEY = JobKeyConstant.JOB_GROUP_KEY_PREFIX + QuartzJobDemo.class.getSimpleName();

    private Scheduler scheduler;

    public QuartzJobDemo(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void publishJob(Date triggerStartTime) {

        Class<QuartzJobDemo> jobClass = QuartzJobDemo.class;
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(JOB_KEY, JOB_GROUP_KEY)
                .build();

        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .startAt(triggerStartTime)
                .withIdentity(JobKeyConstant.TRIGGER_KEY_PREFIX + jobClass.getSimpleName(), JobKeyConstant.TRIGGER_GROUP_KEY_PREFIX + jobClass.getSimpleName())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }

    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

    }
}
