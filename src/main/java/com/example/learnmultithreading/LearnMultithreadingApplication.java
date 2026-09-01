package com.example.learnmultithreading;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class LearnMultithreadingApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(LearnMultithreadingApplication.class, args);

        log.info("Before thread, name: {}, state: {}",Thread.currentThread().getName(), Thread.currentThread().getState());

        Thread workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("Inside the thread");
                try {
                    Thread.sleep(5000);
                }
                catch (InterruptedException ie){
                    throw new RuntimeException(ie);
                }
            }
        });

        workerThread.start();

        workerThread.join(); // blocks the calling thread

        log.info("After thread, name: {}, state: {}",Thread.currentThread().getName(), Thread.currentThread().getState());
        log.info("After thread, state: {}", Thread.currentThread().getState());
        log.info("After thread");


    }


}
