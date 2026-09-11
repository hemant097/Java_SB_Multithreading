package com.example.learnmultithreading;

import lombok.extern.slf4j.Slf4j;
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

//    Exactly one of the 'cron', 'fixedDelay' or 'fixedRate' attributes is required
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

    @Scheduled(cron = "*/5 * * * * *")
    void logCron(){
        log.info("Scheduler cron started ...{}",Thread.currentThread().getName());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("Scheduler cron ended ...{}",Thread.currentThread().getName());

    }
}
