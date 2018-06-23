package com.akkatraining.java.futures;

import java.io.IOException;
import java.util.concurrent.*;

public class BasicFuturesTest {

    public static void main(String[] args) throws IOException {
        ExecutorService fixedThreadPool =
                Executors.newFixedThreadPool(5);

        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("Inside the future: " +
                        Thread.currentThread().getName());
                Thread.sleep(5000);
                return 5 + 3;
            }
        };

        System.out.println("In test:" + Thread.currentThread().getName());
        Future<Integer> future = fixedThreadPool.submit(callable);

        //This will block
        Integer result = null; //block
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("result = " + result);
        System.exit(0);
    }
}
