package part_2_assignment_version_5;

import com.google.common.util.concurrent.Striped;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

public class test_part_2_version_5 {

    public static void main(String[] args) {
        String inputHostName = VALUE.VALUE.LOCAL_HOST;
//        inputHostName = "192.168.1.5";
        runTest_one_time(inputHostName, 10);
//        runTest_one_time(inputHostName,100);
    }

    public static void runTest_one_time(String inputHostName, int number_of_uThreads) {

        LinkedBlockingQueue inputRequestQue = new LinkedBlockingQueue();
        ConcurrentHashMap<String, String> inputResultQue = new ConcurrentHashMap<String, String>();
        Striped<ReadWriteLock> inputSharedRWLock = Striped.readWriteLock(12);

        ExecutorService executorRuntime = Executors.newFixedThreadPool(1);
        executorRuntime.submit(new Runtime_version_5(inputHostName, inputRequestQue, inputResultQue, inputSharedRWLock));

        ExecutorService executorUThread = Executors.newFixedThreadPool(number_of_uThreads);
        //spawn 10 uThr
        for (int i = 0; i < number_of_uThreads; i++) {
            int id = i + 1;
            executorUThread.submit(new Client_version_5(id, inputRequestQue, inputResultQue, inputSharedRWLock));
        }

        int time_in_seconds = 1;
        if (number_of_uThreads == 100) {
            time_in_seconds = 15;
        } else if (number_of_uThreads == 10) {
            time_in_seconds = 15;
        }
        
        
        shutExecutor(executorUThread, time_in_seconds);
        if (executorUThread.isShutdown()) {
            shutExecutor(executorRuntime);
        }
    }

    public static void shutExecutor(ExecutorService executor, int time_in_seconds) {
        try {
            executor.awaitTermination(time_in_seconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            executor.shutdown();
            System.err.println("executor.isTerminated() status = " + executor.isTerminated());
            if (!executor.isTerminated()) {
                System.err.println("canceled non-finished tasks");
                executor.shutdownNow();
            } else {
                System.out.println("shutdown finished");
            }
        }
    }

    public static void shutExecutor(ExecutorService executor) {
        try {
            executor.awaitTermination(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            executor.shutdown();
            System.err.println("executor.isTerminated() status = " + executor.isTerminated());
            if (!executor.isTerminated()) {
                System.err.println("canceled non-finished tasks");
                executor.shutdownNow();
            } else {
                System.out.println("shutdown finished");
            }
        }
    }

}