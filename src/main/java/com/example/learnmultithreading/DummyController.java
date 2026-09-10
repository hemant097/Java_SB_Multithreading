package com.example.learnmultithreading;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@Slf4j
public class DummyController {

    ExecutorService executor = Executors.newFixedThreadPool(6);

    //this creates a new thread on each request, and blocks it, thus tomcat's thread is blocked (sleep)
    @GetMapping("/hello")
    public ResponseEntity<String> getName(){
        log.info("START");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("END");
        return ResponseEntity.ok("Xulfi");
    }


    //here another threadpool's thread is blocked
    @GetMapping("/hellocf")
    public CompletableFuture<ResponseEntity<String>> getNamecf(){
        log.info("thread is called");

        return CompletableFuture.supplyAsync(()->{
            log.info("inside the future call");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.ok("Xulfi");
        }, executor);

    }
}
