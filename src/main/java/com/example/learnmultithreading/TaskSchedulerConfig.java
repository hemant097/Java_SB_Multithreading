package com.example.learnmultithreading;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class TaskSchedulerConfig {

    //TaskScheduler controls the @Scheduled execution, it does not execute or runs the task
    @Bean
    @Primary
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler =  new ThreadPoolTaskScheduler();
        taskScheduler.setThreadNamePrefix("Custom_Task->");
        taskScheduler.setPoolSize(4);
        taskScheduler.initialize();

        return taskScheduler;
    }
}
