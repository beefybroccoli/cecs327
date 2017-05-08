package part_2_assignment_version_final;

import static part_2_assignment_version_final.object.VALUE.echo;
import com.google.common.util.concurrent.Striped;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/***************************************************************************
     * test_part_2 Class
     * 
     * This Class will start the client connections based on hard coded input.
     **************************************************************************/
public class test_part_2 {

    public static void main(String[] args) {

        //Initialze Clients
        String inputHostName = part_2_assignment_version_final.object.VALUE.LOCAL_HOST;
//        inputHostName = "192.168.1.4";
        test(inputHostName, 10, 10);
//        test(inputHostName, 1, 1000);
//        test(inputHostName, 2, 100);

    }

    /**
     *  This method tests the clients connecting to the server.
     *  
    **/
    public static void test(String inputHostName, int input_number_of_batch, int input_number_of_uThreads) {

        //Initialziue queues
        LinkedBlockingQueue inputRequestQue = new LinkedBlockingQueue();
        ConcurrentHashMap<String, String> inputResultQue = new ConcurrentHashMap<String, String>();
        Striped<ReadWriteLock> inputSharedRWLock = Striped.readWriteLock(input_number_of_uThreads);

        //Intialize executor service
        ExecutorService executorRuntime = Executors.newFixedThreadPool(1);
        executorRuntime.submit(new Runtime(inputHostName, inputRequestQue, inputResultQue, inputSharedRWLock));

        //Initialize number of batches and threads
        int number_of_batch = input_number_of_batch;
        int number_of_uThreads = input_number_of_uThreads;

        //Set the time based on the number of threads
        int time_in_seconds = 0;
        if (number_of_uThreads == 100) {
            time_in_seconds = 20;
        }
        if (number_of_uThreads == 10) {
            time_in_seconds = 3;
        }
        if (number_of_uThreads == 1000) {
            time_in_seconds = 120;
        }

        int executor_time_in_seconds = time_in_seconds;

        //Create a runnable task and start the clients
        Runnable task = () -> {

            for (int i = 0; i < number_of_batch; i++) {

                echo("Start batch " + (i + 1) + "-------------------------------------\n");

                ExecutorService executorUThread = Executors.newFixedThreadPool(number_of_uThreads);

                //spawn uThrs
                for (int j = 0; j < number_of_uThreads; j++) {
                    int id = j + 1;
                    executorUThread.submit(new Client(id, inputRequestQue, inputResultQue, inputSharedRWLock));
                }

                shutExecutor(executorUThread, executor_time_in_seconds);

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    echo("Interruption occured in Runnable");
                }

                echo("---------------------------------------end batch " + (i + 1) + "\n");
                echo("time_in_seconds = " + executor_time_in_seconds);
            }//end for

            echo("exit for loop already");

        };

        task.run();

        shutExecutor(executorRuntime);

    }

    /**
     *  This method shuts down the client connections.
     *  
    **/
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

    /**
     *  This method shuts down the executor service.
     *  
    **/
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
