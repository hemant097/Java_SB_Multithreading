package com.example.learnmultithreading;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyScheduler {

    //it doesn't care whether the execution time takes 1sec, it starts every 2 sec
//    @Scheduled(fixedRate = 2000) //not concurrent, thus only 1 runs at a time
//    void logMe(){
//        log.info("Scheduler1 started ...{}",Thread.currentThread().getName());
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        log.info("Scheduler1 ended ...{}",Thread.currentThread().getName());
//
//    }

//    Exactly one of the 'cron', 'fixedDelay' or 'fixedRate' attributes is required, using both will give error
//    @Scheduled(fixedDelay = 1000) //delays the task for 1sec after the end of one execution, and start of the next
//    void logYou(){
//        log.info("Scheduler2 started ...{}",Thread.currentThread().getName());
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        log.info("Scheduler2 ended ...{}",Thread.currentThread().getName());
//
//    }

    //using cron expression, below runs every 5 seconds, using */5 as the step-up, only 5 would've run it every minute at second 5
//    @Scheduled(cron = "*/5 * * * * *")
//    void logCron(){
//        log.info("Scheduler cron started ...{}",Thread.currentThread().getName());
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        log.info("Scheduler cron ended ...{}",Thread.currentThread().getName());
//
//    }


    //now what if we want to execute tasks asynchronously
    @Scheduled(fixedRate = 500)
    @Async("jobExecutor") //we should use a custom Executor, else it keeps creating a new thread
    void logAsyncScheduled(){
        log.info("Scheduler async started ...{}",Thread.currentThread().getName());

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("Scheduler async ended ...{}",Thread.currentThread().getName());

    }


}
