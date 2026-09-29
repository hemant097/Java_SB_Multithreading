package com.example.learnmultithreading;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class StudentInfoService {

    @Async
    CompletableFuture<String> getNameFuture() throws InterruptedException {
        log.info("Getting the name CF ... {}",Thread.currentThread().getName());
        Thread.sleep(2500);
        log.info("returning name");
        return CompletableFuture.completedFuture("Hemant");
    }

    @Async
    CompletableFuture<String> getCollegeFuture()throws InterruptedException {
        log.info("Getting the college CF ... {}",Thread.currentThread().getName());
        Thread.sleep(2500);
        log.info("returning college");
        return CompletableFuture.completedFuture("HCST");
    }

    @Async
    CompletableFuture<String> getIdFuture()throws InterruptedException {
        log.info("Getting the id CF ... {}",Thread.currentThread().getName());
        Thread.sleep(2500);
        log.info("returning id");
        return CompletableFuture.completedFuture("2016043");
    }

    public String getNameString() throws InterruptedException {
        log.info("Getting the name String ... {}",Thread.currentThread().getName());
        Thread.sleep(2500);
        return "Hemant";
    }

    public String getCollegeString()throws InterruptedException {
        log.info("Getting the college String ... {}",Thread.currentThread().getName());
        Thread.sleep(2500);
        return "HCST";
    }

    public String getIdString()throws InterruptedException {
        log.info("Getting the id String... {}",Thread.currentThread().getName());
        Thread.sleep(2500);
        return "1606440043";
    }
}
