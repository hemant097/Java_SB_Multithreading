package com.example.learnmultithreading;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor@Slf4j
public class StudentService {
    private final StudentInfoService infoService;

    public Student getStudentInfo(){

        try {
            long start = System.currentTimeMillis();
            log.info("Starting now ... {}", Thread.currentThread().getName());

            CompletableFuture<String> nameFuture = infoService.getNameFuture();
            CompletableFuture<String> collegeFuture = infoService.getCollegeFuture();
            CompletableFuture<String> idFuture = infoService.getIdFuture();

            CompletableFuture.allOf(nameFuture,collegeFuture,idFuture); //this waits for all the CFs to complete, and works in parallel

                Student student = new Student(nameFuture.get(),
                        collegeFuture.get(),
                        idFuture.get());

            long end = System.currentTimeMillis();
            log.info("Ended in {}",(end- start));
            return student;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
