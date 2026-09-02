package com.example.learnmultithreading;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.*;
import java.util.function.Supplier;

@SpringBootApplication
@Slf4j
public class LearnMultithreadingApplication implements CommandLineRunner  {

    public static void main(String[] args) throws InterruptedException  {
        SpringApplication.run(LearnMultithreadingApplication.class, args);

//        log.info("Before thread, name: {}, state: {}",Thread.currentThread().getName(), Thread.currentThread().getState());
//
//        Thread workerThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                log.info("Inside the thread");
//                try {
//                    Thread.sleep(5000);
//                }
//                catch (InterruptedException ie){
//                    throw new RuntimeException(ie);
//                }
//            }
//        });
//
//        workerThread.start();
//
//        workerThread.join(); // blocks the calling thread
//
//        log.info("worker thread, name: {}, state: {}",workerThread.getName(), workerThread.getState());
//        log.info("After thread, name {}, state: {}", Thread.currentThread().getName(),Thread.currentThread().getState());

    }


    @Override
    public void run(String... args) throws Exception {
//        learnThread();
//        learnFuture();
            learnCompletableFuture();
//        learnCF2();
        log.info("After the method call");

    }

    static void learnCompletableFuture(){
        CompletableFuture<String> myNameCf = CompletableFuture.supplyAsync(() -> getName())
                .thenApply(String::toUpperCase)
                        .thenApply(str-> str.length()+" characters")
//                                .thenApplyAsync( len ->{
//                                    log.info("Inside method with length");
//                                    if(len<8)
//                                        throw new RuntimeException("Faking an error");
//                                    else
//                                        return "length was too much";
//                                })
//                                        .exceptionally(err->
//                                            {return "default value in case of failure";
//                                            })
                ;


        myNameCf.thenAccept(length -> log.info("got the name length : {}",length));

    }

    static void learnCF2(){
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync( ()->{
            String food = "Biryani";
            try{
                Thread.sleep(1000);
                log.info(food+" ordered");
                Thread.sleep(2000);
                log.info(food+" is prepared");
            }catch (InterruptedException ie){
                throw new RuntimeException("Error");
            }
            return food;
        });

        completableFuture.thenAccept( food -> {
            log.info(food+ " delivered");
        });

        log.info("Restaurant(main-thread) is running...");

        try{
            Thread.sleep(5000);
            log.info("Restaurant closed.");
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    static void learnFuture() throws InterruptedException, ExecutionException{
        try (ExecutorService executorService = Executors.newFixedThreadPool(4)) {

            Future<String> myNameFuture = executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return getName();
                }
            });

            System.out.println(myNameFuture.get()); //blocks the calling thread
            log.info("After name future {}",Thread.currentThread().getState());

        }
    }

    static void learnThread(){
        //this can keep 10 in Task Queue, and 6 in parallel, this can take at most 16 tasks in parallel
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                4, 6, 2, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10),
                new RejectedExecutionHandler() {
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
                }
        );
        log.info("Starting main thread {}", Thread.currentThread().getName());

        //thus when, it is 100 it will give error
        for (int i=1; i<=30; i++){
//            threadPoolExecutor.submit(new LongRunningTask(i+""));
//            Thread.sleep(200);
        }

        log.info("Ending main thread {}", Thread.currentThread().getName());

        ScheduledThreadPoolExecutor st = new ScheduledThreadPoolExecutor(6,
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        log.info("");
                        return new Thread(r,"thread "+System.nanoTime());
                    }
                });


        st.schedule(new LongRunningTask("schedule task"), 4,TimeUnit.SECONDS);
    }

    static String getName(){
        try {
            log.info("Inside name future {}",Thread.currentThread().getState());
            Thread.sleep(5000);
        }catch (InterruptedException ie){
            throw new RuntimeException(ie);
        }
        return "Hemant";
    }
}
