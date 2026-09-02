package com.example.learnmultithreading;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LongRunningTask implements Runnable{

    private String cmd;

    public LongRunningTask(String cmd){
        this.cmd=cmd;
    }

    @Override
    public void run() {
        log.info("{} Starting task... {}", cmd, Thread.currentThread().getName());

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("{} Ending task... {}", cmd, Thread.currentThread().getName());

    }
}
