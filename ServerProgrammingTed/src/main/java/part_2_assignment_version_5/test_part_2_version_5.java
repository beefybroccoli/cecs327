package part_2_assignment_version_5;

import static VALUE.VALUE.echo;
import com.google.common.util.concurrent.Striped;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

public class test_part_2_version_5 {

    public static void main(String[] args) {
        String inputHostName = VALUE.VALUE.LOCAL_HOST;
//        inputHostName = "192.168.1.4";
//        test4(inputHostName, 10, 10);
//        test4(inputHostName, 1, 1000);
//        test4(inputHostName, 2, 100);

    }
    
        public static void test4(String inputHostName, int input_number_of_batch, int input_number_of_uThreads) {

        LinkedBlockingQueue inputRequestQue = new LinkedBlockingQueue();
        ConcurrentHashMap<String, String> inputResultQue = new ConcurrentHashMap<String, String>();
        Striped<ReadWriteLock> inputSharedRWLock = Striped.readWriteLock(input_number_of_uThreads);

        ExecutorService executorRuntime = Executors.newFixedThreadPool(1);
        executorRuntime.submit(new Runtime_version_5(inputHostName, inputRequestQue, inputResultQue, inputSharedRWLock));

        int number_of_batch = input_number_of_batch;
        int number_of_uThreads = input_number_of_uThreads;

        int time_in_seconds = 0;
        if (number_of_uThreads == 100) {
            time_in_seconds = 20;
        }
        if (number_of_uThreads == 10) {
            time_in_seconds = 10;
        }

        if (number_of_uThreads == 1000) {
            time_in_seconds = 120;
        }

        int executor_time_in_seconds = time_in_seconds;

        Runnable task = () -> {

            for (int i = 0; i < number_of_batch; i++) {

                echo("Start batch " + (i + 1) + "-------------------------------------\n");

                ExecutorService executorUThread = Executors.newFixedThreadPool(number_of_uThreads);

                //spawn 10 uThr
                for (int j = 0; j < number_of_uThreads; j++) {
                    int id = j + 1;
                    executorUThread.submit(new Client_version_5(id, inputRequestQue, inputResultQue, inputSharedRWLock));
                }

                shutExecutor(executorUThread, executor_time_in_seconds);

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    echo("Interruption occured in Runnable");
                }

                echo("-------------------------------------end batch " + (i + 1) + "\n");
                echo("time_in_seconds = " + executor_time_in_seconds);
            }//end for

            echo("exit for loop already");

        };

        task.run();

        shutExecutor(executorRuntime);

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
