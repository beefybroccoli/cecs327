/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Executor_Thread_Pool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  Counter class is a concurrent object
 */
class Counter {

    private AtomicInteger value = new AtomicInteger();

    public int getValue() {
        return value.get();
    }

    public int increment() {
        return value.incrementAndGet();
    }

    // Alternative implementation as increment but just make the
    // implementation explicit
    public int incrementLongVersion() {
        int oldValue = value.get();
        while (!value.compareAndSet(oldValue, oldValue + 1)) {
            oldValue = value.get();
        }
        return oldValue + 1;
    }

}

public class Executor_Service_And_Future {

    //constant
    private static final int NTHREDS = 10;
    //shared resoure
    private static Counter counter = new Counter();

    
    /**
     * run 10 ExecutorService and store result in Set<Integer>
     * @param args 
     */
    public static void main(String[] args) {
        
        List<Future<Integer>> list = new ArrayList<Future<Integer>>();

        Callable<Integer> worker = getWorker();

        for (int i = 0; i < 10; i++) {
            ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
            runExecutor(executor, worker, list);
        }

    }

    /**
     * create a worker
     * @return Callable<Integer>
     */
    public static Callable<Integer> getWorker() {
        Callable<Integer> worker = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int number = counter.increment();
                System.out.println(number);
                return number;
            }
        };
        return worker;
    }

    /**
     * run an ExecutorService with fixed size pool of workers
     * @param executor
     * @param worker
     * @param list
     * @throws RuntimeException 
     */
    public static void runExecutor(ExecutorService executor, Callable<Integer> worker, List<Future<Integer>> list) throws RuntimeException {
        for (int i = 0; i < 500; i++) {

            Future<Integer> submit = executor.submit(worker);
            list.add(submit);

        }

        // This will make the executor accept no new threads
        // and finish all existing threads in the queue
        executor.shutdown();
        // Wait until all threads are finish
        while (!executor.isTerminated()) {
        }
        Set<Integer> set = new HashSet<Integer>();
        for (Future<Integer> future : list) {
            try {
                set.add(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("");
            } catch (ExecutionException e) {
                e.printStackTrace();
                System.out.println("");
            }
        }
        if (list.size() != set.size()) {
            throw new RuntimeException("Double-entries!!!");
        }
    }

}
