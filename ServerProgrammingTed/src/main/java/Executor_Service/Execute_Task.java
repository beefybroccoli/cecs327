package Executor_Service;

import static VALUE.VALUE.echo;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Execute_Task {

    public static void main(String[] args) {

//        execute_runnable();
//
//        execute_pool_size_of_1_and_shutdown();
//
//        execute_pool_size_of_1_and_shutdownnow();
//
//        lambda_callable();
//        
//        callable_future();
//        
//        feature_with_timeout();
//
//        invoke_all_tasks();
//        
//        invoke_any();
//        
//        scheduled_executors_with_initial_delay();
//
//        scheduled_executors_scheduleAtFixedRate();
//
//        scheduled_executors_scheduleWithFixedDelay();
//

    }

    /*
    A ScheduledExecutorService is capable of scheduling tasks to run either periodically 
        or once after a certain amount of time has elapsed.

    This code sample schedules a task to run after an initial delay of three seconds has passed:
    
    Scheduling a task produces a specialized future of type ScheduledFuture 
        which - in addition to Future - provides the method getDelay() to retrieve the remaining delay. 
        After this delay has elapsed the task will be executed concurrently.

    In order to schedule tasks to be executed periodically, 
        executors provide the two methods scheduleAtFixedRate() and scheduleWithFixedDelay().
    
    This code sample schedules a task to run after an initial delay of three seconds has passed:
     */
    public static void scheduled_executors_with_initial_delay() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> System.out.println("Scheduling: " + System.nanoTime());
        ScheduledFuture<?> future = executor.schedule(task, 3, TimeUnit.SECONDS);

        try {
            TimeUnit.MILLISECONDS.sleep(1337);
        } catch (InterruptedException ex) {
            Logger.getLogger(Execute_Task.class.getName()).log(Level.SEVERE, null, ex);
        }

        long remainingDelay = future.getDelay(TimeUnit.MILLISECONDS);
        System.out.printf("Remaining Delay: %sms", remainingDelay);

        /*shut down the task after running for 10 seconds*/
        try {
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }
    }

    /*
    In order to schedule tasks to be executed periodically, 
        executors provide the two methods scheduleAtFixedRate() and scheduleWithFixedDelay(). 
    
    The first method is capable of executing tasks with a fixed time rate,
        e.g. once every second as demonstrated in this example:
    
    Additionally this method accepts an initial delay which describes 
        the leading wait time before the task will be executed for the first time.
    
    Please keep in mind that scheduleAtFixedRate() 
        doesn't take into account the actual duration of the task. 
        So if you specify a period of one second but the task needs 2 seconds 
        to be executed then the thread pool will working to capacity very soon.
     */
    public static void scheduled_executors_scheduleAtFixedRate() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> System.out.println("Scheduling: " + System.nanoTime());

        int initialDelay = 0;
        int period = 1;
        executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);

        /*shut down the task after running for 10 seconds*/
        try {
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }
    }

    /*
    In that case you should consider using scheduled_executors_scheduleAtFixedRate() instead. 
        scheduleWithFixedDelay method works just like the counterpart described above. 
    
    The difference is that the wait time period applies between the end of a task 
        and the start of the next task. 
     */
    public static void scheduled_executors_scheduleWithFixedDelay() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Scheduling: " + System.nanoTime());
            } catch (InterruptedException e) {
                System.err.println("task interrupted");
            }
        };

        executor.scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);

        /*shut down the task after running for 10 seconds*/
        try {
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }

    }

    /**
     * Helper method for invoke_any In order to test this behavior we use this
     * helper method to simulate callables with different durations. The method
     * returns a callable that sleeps for a certain amount of time until
     * returning the given result:
     *
     * @param result
     * @param sleepSeconds
     * @return
     */
    public static Callable<String> callable(String result, long sleepSeconds) {
        return () -> {
            TimeUnit.SECONDS.sleep(sleepSeconds);
            return result;
        };
    }

    /**
     * We use this method to create a bunch of callables with different
     * durations from one to three seconds.
     *
     * Submitting those callables to an executor via invokeAny() returns the
     * string result of the fastest callable - in that case task2:
     */
    public static void invoke_any() {
        ExecutorService executor = Executors.newWorkStealingPool();

        List<Callable<String>> callables = Arrays.asList(
                callable("task1", 2),
                callable("task2", 1),
                callable("task3", 3));

        String result = "-1";
        try {
            result = executor.invokeAny(callables);
        } catch (InterruptedException ex) {
            echo("InterruptedException occured");
//            Logger.getLogger(Execute_Task.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            echo("ExecutionException occured");
//            Logger.getLogger(Execute_Task.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.out.println(result);
        }

    }

    public static void invoke_all_tasks() {

//        Callable<Integer> task1 = () -> {
//            try {
//                TimeUnit.SECONDS.sleep(1);
//                return 1;
//            } catch (InterruptedException e) {
//                throw new IllegalStateException("task interrupted", e);
//            }
//        };
//
//        Callable<Integer> task2 = () -> {
//            try {
//                TimeUnit.SECONDS.sleep(1);
//                return 2;
//            } catch (InterruptedException e) {
//                throw new IllegalStateException("task interrupted", e);
//            }
//        };
//
//        Callable<Integer> task3 = () -> {
//            try {
//                TimeUnit.SECONDS.sleep(1);
//                return 3;
//            } catch (InterruptedException e) {
//                throw new IllegalStateException("task interrupted", e);
//            }
//        };
        ExecutorService executor = Executors.newWorkStealingPool();

        List<Callable<Integer>> callables = Arrays.asList(
                () -> {
                    return 1;
                },
                () -> {
                    return 2;
                },
                () -> {
                    return 3;
                });

        try {
            executor.invokeAll(callables)
                    .stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    })
                    .forEach(System.out::println);
        } catch (InterruptedException ex) {
            Logger.getLogger(Execute_Task.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*
        shut down the task after running for 10 seconds
         */
        try {
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }

    }

    /*
    a task run longer than expected time so, it get error and shut the task down.
     */
    public static void feature_with_timeout() {

        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<Integer> future = executor.submit(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(10000);
                return 123;
            } catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        });

        Integer result = null;

        try {
            result = future.get(1000, TimeUnit.MILLISECONDS);

        } catch (InterruptedException ex) {
            result = -1;
            echo("InterruptedException occured");
        } catch (ExecutionException ex) {
            result = -1;
            echo("ExecutionException occured");
        } catch (TimeoutException ex) {
            result = -1;
            echo("TimeoutException occured");
        } catch (NullPointerException ex) {
            result = -1;
            echo("NullPointerException occured");
        } finally {
            System.out.println("result = " + result);
            executor.shutdownNow();
            System.out.println("executor executor.isShutdown() = " + executor.isShutdown());
        }

    }

    /*
    use callable and feature to get result, then shut down the task
     */
    public static void callable_future() {
        Callable<Integer> task = () -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return 123;
            } catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Integer> future = executor.submit(task);

        System.out.println("future done? " + future.isDone());

        Integer result = null;
        try {
            result = future.get();
        } catch (InterruptedException ex) {
            Logger.getLogger(Execute_Task.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(Execute_Task.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("future done? " + future.isDone());
        System.out.println("result: " + result);

        executor.shutdownNow();
        System.out.println("executor executor.isShutdown() = " + executor.isShutdown());

    }

    public static void lambda_callable() {

//        Integer result;
        Callable<Integer> task = () -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return 123;
            } catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        };

        System.out.println("lambda_callable()  : " + task.toString());
    }

    public static Integer get_integer_from_calalble() {

        return null;
    }

    /*
    start execute with size of 1 and end thread with shutdown()
     */
    public static void execute_pool_size_of_1_and_shutdownnow() {
        ExecutorService executor2 = Executors.newSingleThreadExecutor();
        executor2.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
        });

        System.out.println("attempt to shutdownnow() executor");
        executor2.shutdownNow();
        System.out.println("executor2's status " + executor2.isTerminated());
    }

    public static void execute_runnable() {
        Runnable runnable = () -> {
            try {
                String name = Thread.currentThread().getName();
                System.out.println("Foo " + name);
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Bar " + name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        runnable.run();

        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
        };

        task.run();
    }

    /*
    start execute with size of 1 and end thread with shutdown()
     */
    public static void execute_pool_size_of_1_and_shutdown() {
        /*
        The class Executors provides convenient factory methods
        for creating different kinds of executor services.
        In this sample we use an executor with a thread pool of size one.
         */
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
        });

        /*
        An ExecutorService provides two methods for that purpose: 
            _shutdown() waits for currently running tasks to finish 
            _shutdownNow() interrupts all running tasks and shut the executor down immediately.

        This is the preferred way how I typically shutdown executors:
         */
        try {
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }

    }

}
