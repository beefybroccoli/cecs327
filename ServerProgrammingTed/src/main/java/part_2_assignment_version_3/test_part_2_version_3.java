package part_2_assignment_version_3;

import com.google.common.util.concurrent.Striped;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

public class test_part_2_version_3 {

    public static void main(String[] args) {

        runTest_one_time(10);
//        runTest_one_time(100);

    }

    public static void runTest_one_time(int number_of_uThreads) {
        String inputHostName = VALUE.VALUE.LOCAL_HOST;
        LinkedBlockingQueue inputRequestQue = new LinkedBlockingQueue();
        ConcurrentHashMap<String, String> inputResultQue = new ConcurrentHashMap<String, String>();
        Striped<ReadWriteLock> inputSharedRWLock = Striped.readWriteLock(12);

        ExecutorService executorRuntime = Executors.newFixedThreadPool(1);
        executorRuntime.submit(new RuntimeThr_version_3(inputHostName, inputRequestQue, inputResultQue, inputSharedRWLock));

        ExecutorService executorUThread = Executors.newFixedThreadPool(number_of_uThreads);
        //spawn 10 uThr
        for (int i = 0; i < number_of_uThreads; i++) {
            int id = i + 1;
            executorUThread.submit(new UThr_version_3(id, inputRequestQue, inputResultQue, inputSharedRWLock));
        }

        int time_in_seconds = 1;
        if (number_of_uThreads == 100) {
            time_in_seconds = 15;
        } else if (number_of_uThreads == 10){
            time_in_seconds = 5;
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

/*
------------------------------------------------------------------------
Building ServerProgrammingTed 1.0-SNAPSHOT
------------------------------------------------------------------------

--- exec-maven-plugin:1.2.1:exec (default-cli) @ ServerProgrammingTed ---
(runtimeThr Started)

(uThr 1 Started)

(uThr 2 Started)

(uThr 3 Started)

(uThr 4 Started)

(uThr 5 Started)

(uThr 6 Started)

(uThr 7 Started)

(uThr 8 Started)

(uThr 9 Started)

(uThr 10 Started)

(uThr 2 Ended, mCounter = 20)

(uThr 6 Ended, mCounter = 20)

(uThr 3 Ended, mCounter = 20)

(uThr 5 Ended, mCounter = 20)

(uThr 8 Ended, mCounter = 20)

(uThr 10 Ended, mCounter = 20)

(uThr 1 Ended, mCounter = 20)

(uThr 7 Ended, mCounter = 20)

(uThr 4 Ended, mCounter = 20)

(uThr 9 Ended, mCounter = 20)

executor.isTerminated() status = false
canceled non-finished tasks
executor.isTerminated() status = false
canceled non-finished tasks
Interruption occured in rumtimeThr

(runtimeThr Ended, 
mSharedRequestQue = []
mSharedResultQue = {})

------------------------------------------------------------------------
BUILD SUCCESS
------------------------------------------------------------------------
Total time: 5.487s
Finished at: Fri Apr 28 00:44:46 PDT 2017
Final Memory: 8M/309M
------------------------------------------------------------------------
*/

/*
--- exec-maven-plugin:1.2.1:exec (default-cli) @ ServerProgrammingTed ---
(runtimeThr Started)

(uThr 1 Started)

(uThr 2 Started)

(uThr 3 Started)

(uThr 4 Started)

(uThr 5 Started)

(uThr 6 Started)

(uThr 7 Started)

(uThr 8 Started)

(uThr 9 Started)

(uThr 10 Started)

(uThr 11 Started)

(uThr 12 Started)

(uThr 13 Started)

(uThr 14 Started)

(uThr 15 Started)

(uThr 16 Started)

(uThr 17 Started)

(uThr 18 Started)

(uThr 19 Started)

(uThr 20 Started)

(uThr 21 Started)

(uThr 22 Started)

(uThr 23 Started)

(uThr 24 Started)

(uThr 25 Started)

(uThr 26 Started)

(uThr 27 Started)

(uThr 28 Started)

(uThr 29 Started)

(uThr 30 Started)

(uThr 31 Started)

(uThr 32 Started)

(uThr 33 Started)

(uThr 34 Started)

(uThr 35 Started)

(uThr 36 Started)

(uThr 37 Started)

(uThr 38 Started)

(uThr 39 Started)

(uThr 40 Started)

(uThr 41 Started)

(uThr 42 Started)

(uThr 43 Started)

(uThr 44 Started)

(uThr 45 Started)

(uThr 46 Started)

(uThr 47 Started)

(uThr 48 Started)

(uThr 49 Started)

(uThr 51 Started)

(uThr 50 Started)

(uThr 52 Started)

(uThr 53 Started)

(uThr 54 Started)

(uThr 55 Started)

(uThr 56 Started)

(uThr 57 Started)

(uThr 58 Started)

(uThr 59 Started)

(uThr 60 Started)

(uThr 61 Started)

(uThr 62 Started)

(uThr 63 Started)

(uThr 64 Started)

(uThr 65 Started)

(uThr 66 Started)

(uThr 67 Started)

(uThr 68 Started)

(uThr 69 Started)

(uThr 70 Started)

(uThr 71 Started)

(uThr 72 Started)

(uThr 73 Started)

(uThr 74 Started)

(uThr 75 Started)

(uThr 76 Started)

(uThr 77 Started)

(uThr 78 Started)

(uThr 79 Started)

(uThr 80 Started)

(uThr 81 Started)

(uThr 82 Started)

(uThr 83 Started)

(uThr 84 Started)

(uThr 85 Started)

(uThr 86 Started)

(uThr 87 Started)

(uThr 88 Started)

(uThr 89 Started)

(uThr 90 Started)

(uThr 91 Started)

(uThr 92 Started)

(uThr 93 Started)

(uThr 94 Started)

(uThr 95 Started)

(uThr 96 Started)

(uThr 97 Started)

(uThr 98 Started)

(uThr 99 Started)

(uThr 100 Started)

(uThr 35 Ended, mCounter = 20)

(uThr 42 Ended, mCounter = 20)

(uThr 52 Ended, mCounter = 20)

(uThr 34 Ended, mCounter = 20)

(uThr 55 Ended, mCounter = 20)

(uThr 27 Ended, mCounter = 20)

(uThr 6 Ended, mCounter = 20)

(uThr 22 Ended, mCounter = 20)

(uThr 10 Ended, mCounter = 20)

(uThr 65 Ended, mCounter = 20)

(uThr 60 Ended, mCounter = 20)

(uThr 63 Ended, mCounter = 20)

(uThr 5 Ended, mCounter = 20)

(uThr 9 Ended, mCounter = 20)

(uThr 16 Ended, mCounter = 20)

(uThr 41 Ended, mCounter = 20)

(uThr 13 Ended, mCounter = 20)

(uThr 54 Ended, mCounter = 20)

(uThr 46 Ended, mCounter = 20)

(uThr 33 Ended, mCounter = 20)

(uThr 17 Ended, mCounter = 20)

(uThr 53 Ended, mCounter = 20)

(uThr 12 Ended, mCounter = 20)

(uThr 39 Ended, mCounter = 20)

(uThr 58 Ended, mCounter = 20)

(uThr 4 Ended, mCounter = 20)

(uThr 37 Ended, mCounter = 20)

(uThr 85 Ended, mCounter = 20)

(uThr 45 Ended, mCounter = 20)

(uThr 18 Ended, mCounter = 20)

(uThr 15 Ended, mCounter = 20)

(uThr 21 Ended, mCounter = 20)

(uThr 26 Ended, mCounter = 20)

(uThr 3 Ended, mCounter = 20)

(uThr 59 Ended, mCounter = 20)

(uThr 32 Ended, mCounter = 20)

(uThr 24 Ended, mCounter = 20)

(uThr 2 Ended, mCounter = 20)

(uThr 67 Ended, mCounter = 20)

(uThr 64 Ended, mCounter = 20)

(uThr 62 Ended, mCounter = 20)

(uThr 73 Ended, mCounter = 20)

(uThr 61 Ended, mCounter = 20)

(uThr 44 Ended, mCounter = 20)

(uThr 25 Ended, mCounter = 20)

(uThr 7 Ended, mCounter = 20)

(uThr 30 Ended, mCounter = 20)

(uThr 20 Ended, mCounter = 20)

(uThr 51 Ended, mCounter = 20)

(uThr 14 Ended, mCounter = 20)

(uThr 49 Ended, mCounter = 20)

(uThr 19 Ended, mCounter = 20)

(uThr 8 Ended, mCounter = 20)

(uThr 1 Ended, mCounter = 20)

(uThr 68 Ended, mCounter = 20)

(uThr 50 Ended, mCounter = 20)

(uThr 66 Ended, mCounter = 20)

(uThr 36 Ended, mCounter = 20)

(uThr 83 Ended, mCounter = 20)

(uThr 40 Ended, mCounter = 20)

(uThr 79 Ended, mCounter = 20)

(uThr 29 Ended, mCounter = 20)

(uThr 76 Ended, mCounter = 20)

(uThr 31 Ended, mCounter = 20)

(uThr 56 Ended, mCounter = 20)

(uThr 82 Ended, mCounter = 20)

(uThr 48 Ended, mCounter = 20)

(uThr 43 Ended, mCounter = 20)

(uThr 47 Ended, mCounter = 20)

(uThr 28 Ended, mCounter = 20)

(uThr 57 Ended, mCounter = 20)

(uThr 70 Ended, mCounter = 20)

(uThr 23 Ended, mCounter = 20)

(uThr 97 Ended, mCounter = 20)

(uThr 99 Ended, mCounter = 20)

(uThr 78 Ended, mCounter = 20)

(uThr 11 Ended, mCounter = 20)

(uThr 38 Ended, mCounter = 20)

(uThr 86 Ended, mCounter = 20)

(uThr 72 Ended, mCounter = 20)

(uThr 71 Ended, mCounter = 20)

(uThr 89 Ended, mCounter = 20)

(uThr 92 Ended, mCounter = 20)

(uThr 87 Ended, mCounter = 20)

(uThr 91 Ended, mCounter = 20)

(uThr 69 Ended, mCounter = 20)

(uThr 80 Ended, mCounter = 20)

(uThr 74 Ended, mCounter = 20)

(uThr 88 Ended, mCounter = 20)

(uThr 90 Ended, mCounter = 20)

(uThr 94 Ended, mCounter = 20)

(uThr 98 Ended, mCounter = 20)

(uThr 96 Ended, mCounter = 20)

(uThr 95 Ended, mCounter = 20)

(uThr 93 Ended, mCounter = 20)

(uThr 75 Ended, mCounter = 20)

(uThr 100 Ended, mCounter = 20)

(uThr 84 Ended, mCounter = 20)

(uThr 81 Ended, mCounter = 20)

(uThr 77 Ended, mCounter = 20)

executor.isTerminated() status = false
canceled non-finished tasks
executor.isTerminated() status = false
canceled non-finished tasks
Interruption occured in rumtimeThr

(runtimeThr Ended)

mSharedRequestQue = []
mSharedResultQue = {}
------------------------------------------------------------------------
BUILD SUCCESS
------------------------------------------------------------------------
Total time: 30.649s
Finished at: Fri Apr 28 00:38:33 PDT 2017
Final Memory: 7M/245M
------------------------------------------------------------------------
*/
