package com.example.learnmultithreading;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig {

    @Bean("jobExecutor")
    public Executor jobExecutor(){

        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(4);
        threadPoolExecutor.setMaxPoolSize(10);
        threadPoolExecutor.setQueueCapacity(6);
        threadPoolExecutor.setThreadNamePrefix("hemuThread-");
        threadPoolExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                log.info("Thread rejected... Retrying ...");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                executor.submit(r);
            }
        });

        threadPoolExecutor.initialize();
        return threadPoolExecutor;
    }
}
