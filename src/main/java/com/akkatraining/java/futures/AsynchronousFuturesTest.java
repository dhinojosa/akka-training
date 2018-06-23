package com.akkatraining.java.futures;

import java.util.concurrent.*;

public class AsynchronousFuturesTest {

    public static void main(String[] args) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(3000);
                return 5 + 3;
            }
        };

        Future<Integer> future = cachedThreadPool.submit(callable);

        //This is proper asynchrony, but rather ugly
        while (!future.isDone()) {
            System.out.println(
                    "I am doing something else on thread: " +
                            Thread.currentThread().getName());
        }

        try {
            Integer result = future.get();
            System.out.println("result = " + result);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
