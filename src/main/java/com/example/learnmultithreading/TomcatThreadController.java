package com.example.learnmultithreading;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TomcatThreadController {

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final StudentService studentService;

    TomcatThreadController(@Qualifier("jobExecutor") ThreadPoolTaskExecutor threadPoolTaskExecutor,
                           StudentService studentService){
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.studentService = studentService;
    }

    @GetMapping("/info")
    public ResponseEntity<Student> getStudentInfo(){
        log.info("Starting ... {}", Thread.currentThread().getName());
        Student student = studentService.getStudentInfo();

//        threadPoolTaskExecutor.execute(() -> { //this task happens in the background
//            log.info("middle, {}", Thread.currentThread().getName());
//        });

        log.info("end ... {}", Thread.currentThread().getName());

        return ResponseEntity.ok(student);
    }
}
