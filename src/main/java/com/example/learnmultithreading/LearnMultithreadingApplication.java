package com.example.learnmultithreading;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.function.Supplier;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class LearnMultithreadingApplication implements CommandLineRunner  {

    @Autowired
    private TaskScheduler taskScheduler;

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
//            learnCompletableFuture();
//        learnCF2();
//        learnCF3();
//        log.info("After the method call");

        taskScheduler.schedule(()->{
            log.info("Running after 2 seconds");
        }, Instant.now().plusSeconds(2));


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

//        supplyAsync() runs on [onPool-worker-1], not main, thus starts the work asynchronously
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync( ()->{
            String food = "Biryani";
            try{
                Thread.sleep(1000);
                log.info("{} ordered", food);
                Thread.sleep(2000);
                log.info("{} is prepared", food);
            }catch (InterruptedException ie){
                throw new RuntimeException("Error");
            }
            return food;
        });

        completableFuture.thenAccept( food -> {
            log.info("{} is delivered", food);
        });

        log.info("Restaurant(main-thread) is running...");

        try{
            Thread.sleep(5000);
            log.info("Restaurant closed.");
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        /**FULL OUTPUT
         *
         * T16:21:20.659   [    main]    : Restaurant(main-thread) is running...  //main thread started
         * T16:21:21.664   [worker-1]    : Biryani ordered                      // after 1 sec delay on CF thread
         * T16:21:23.668   [worker-1]    : Biryani is prepared                  // after 2 sec delay ...
         * T16:21:23.669   [worker-1]    : Biryani is delivered                 // thenAccept on CF
         * T16:21:25.664   [    main]    : Restaurant closed.                   // main thread
         * T16:21:25.665   [    main]    : After the method call
         * */
    }

    static void learnCF3(){
        CompletableFuture<String> nameFuture = CompletableFuture.supplyAsync(() -> getName());

        CompletableFuture<String> addressFuture = CompletableFuture.supplyAsync(()-> getAddress());

        CompletableFuture<Integer> ageFuture = CompletableFuture.supplyAsync(() -> getAge());

        //Since both tasks run concurrently, it'll wait for the slower one to complete
        CompletableFuture.allOf(nameFuture,addressFuture,ageFuture).join();

        log.info("Got the name: {}, address: {} and age: {} from the completable futures", nameFuture.join(),addressFuture.join(),ageFuture.join());
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

        //creating ScheduledThreadPoolExecutor with 6 worker threads
        ScheduledThreadPoolExecutor st = new ScheduledThreadPoolExecutor(6,
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        log.info("Creating new threads via thread factory");
                        return new Thread(r,"thread "+System.nanoTime());
                    }
                });


        //scheduling 12 tasks to the above executor , so the first 6 tasks begin at the same time (after the 3 sec delay)
        //now each LongRunningTask blocks the thread for 4000ms, thus after 4sec, all 6 threads become available and next 6 tasks are assigned to them,
        for (int i = 0; i < 12; i++) {
            st.schedule(new LongRunningTask("schedule task"), 3,TimeUnit.SECONDS);
        }
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

    static String getAddress(){
        try {
            log.info("Inside address future {}",Thread.currentThread().getState());
            Thread.sleep(2000);
        }catch (InterruptedException ie){
            throw new RuntimeException(ie);
        }
        return "Kanpur Uttar Pradesh ";
    }

    static int getAge(){
        try {
            log.info("Inside age future {}",Thread.currentThread().getState());
            Thread.sleep(2500);
        }catch (InterruptedException ie){
            throw new RuntimeException(ie);
        }
        return 29;
    }
}
