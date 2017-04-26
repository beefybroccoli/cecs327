package datastructure.concurrent_hash_map;

import static VALUE.VALUE.echo;
import com.google.common.util.concurrent.Striped;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

class Producer_Concurrent_Hash_Map_Version_5 implements Runnable {

    private ConcurrentHashMap<String, String> mMap;
    private boolean mFlag;
    Striped<ReadWriteLock> mSharedRWLock;
    ReadWriteLock mRWLock;
    Lock mLock;
    int mNUMBER_OF_CONSUMER;

    public Producer_Concurrent_Hash_Map_Version_5(ConcurrentHashMap<String, String> queue, Striped<ReadWriteLock> inputRWLock, int inputNUMBER_OF_CONSUMER) {
        this.mMap = queue;
        mFlag = true;
        mSharedRWLock = inputRWLock;
        mNUMBER_OF_CONSUMER = inputNUMBER_OF_CONSUMER;

    }

    /*
    source : http://codingjunkie.net/striped-concurrency/
     */
 /*˙
        String key = "taskA";
        ReadWriteLock mRWLock = rwLockStripes.get(key);
        try{
             mRWLock.lock();
             .....
        }finally{
             mRWLock.unLock();
        }
     */
    @Override
    public void run() {

        do {
            try {
                if (mMap.size() < mNUMBER_OF_CONSUMER) {
                    String key = "" + VALUE.VALUE.getRandomNumberBetween(mNUMBER_OF_CONSUMER, 1);
                    String value = key + "." + VALUE.VALUE.getRandomNumberBetween(1000, 1);
                    mRWLock = mSharedRWLock.get(key);
                    mLock = mRWLock.writeLock();
                    try {
                        mLock.lock();

                        // We are adding elements using offer() in order to check if
                        // it actually managed to insert them.
                        mMap.put(key, value);
                        System.out.println(
                                //                            mLock.toString() + ", " +
                                "Trying to add to queue: String " + mMap.get(key)
                                + ", the result was " + mMap.containsKey(key)
                                + ", size is " + mMap.size()
                                + ", map : " + mMap.toString()
                                + "\n");

                    } finally {
                        mLock.unlock();
                    }
                }
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                mFlag = false;
                echo("Interruption Occured in producer");
            }

        } while (mFlag);

    }//end run
}//end class

class Consumer_Concurrent_Hash_Map_Version_5 implements Runnable {

    private ConcurrentHashMap<String, String> mMap;
    private int mID;
    private boolean mFlag;
    Striped<ReadWriteLock> mSharedRWLock;
    ReadWriteLock mRWLock;
    Lock mLock;

    public Consumer_Concurrent_Hash_Map_Version_5(ConcurrentHashMap<String, String> queue, int inputID, Striped<ReadWriteLock> inputRWLock) {
        this.mMap = queue;
        mID = inputID;
        mFlag = true;
        mSharedRWLock = inputRWLock;
    }

    /*
    source : http://codingjunkie.net/striped-concurrency/
     */
 /*
        String key = "taskA";
        ReadWriteLock mRWLock = rwLockStripes.get(key);
        try{
             mRWLock.lock();
             .....
        }finally{
             mRWLock.unLock();
        }
     */
    @Override
    public void run() {

        // As long as there are empty positions in our array,
        // we want to check what's going on.
        while (mFlag) {
            String key = "" + mID;
            mRWLock = mSharedRWLock.get(key);
            mLock = mRWLock.writeLock();
            try {
                if (mMap.containsKey(key)) {
                    try {
                        mLock.lock();
                        System.out.println(
                                //                                mLock.toString() + ", " +
                                "consumer" + mID + " consume " + mMap.remove(key)
                                + ", after consumption, mMap size: " + mMap.size()
                                + ", map : " + mMap.toString()
                                + "\n");
                    } finally {
                        mLock.unlock();
                    }//end finally
                }//end if
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                mFlag = false;
                echo("Interruption Occured in consumer" + mID);
            }//end catch
        }//end while
    }//end run
}//end class

public class Concurrent_Hash_Map_Producer_Consumer_version_5 {

    public static void main(String[] args) {

        run_with_consumers(10);

    }//end main method

    public static void run_with_consumers(int NUMBER_OF_CONSUMER) {
        int time_in_seconds = 5;
        int NUMBER_OF_THREADS = NUMBER_OF_CONSUMER;
        Striped<ReadWriteLock> rwLockStripes = Striped.readWriteLock(NUMBER_OF_CONSUMER);
        ExecutorService executorProducer = Executors.newFixedThreadPool(1);
        ExecutorService executorConsumer = Executors.newFixedThreadPool(NUMBER_OF_CONSUMER);

        ConcurrentHashMap<String, String> sharedMap = new ConcurrentHashMap<>();

        // The two threads will access the same queue, in order
        // to test its blocking capabilities.
        Thread producer = new Thread(new Producer_Concurrent_Hash_Map_Version_5(sharedMap, rwLockStripes, NUMBER_OF_CONSUMER));

        executorProducer.submit(producer);

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            int id = i + 1;
            executorConsumer.submit(new Thread(new Consumer_Concurrent_Hash_Map_Version_5(sharedMap, id, rwLockStripes)));
        }

        /*
        the producer will shut down after all the consumers die
         */
        shutExecutor(executorConsumer, time_in_seconds);
        if (executorConsumer.isShutdown()) {
            shutExecutor(executorProducer);
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
Trying to add to queue: String 6.532, the result was true, size is 1, map : {6=6.532}

consumer6 consume 6.532, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 3.466, the result was true, size is 1, map : {3=3.466}

Trying to add to queue: String 10.895, the result was true, size is 2, map : {3=3.466, 10=10.895}

Trying to add to queue: String 3.435, the result was true, size is 2, map : {3=3.435, 10=10.895}

Trying to add to queue: String 1.324, the result was true, size is 3, map : {1=1.324, 3=3.435, 10=10.895}

Trying to add to queue: String 6.460, the result was true, size is 4, map : {1=1.324, 3=3.435, 6=6.460, 10=10.895}

Trying to add to queue: String 8.760, the result was true, size is 5, map : {1=1.324, 3=3.435, 6=6.460, 8=8.760, 10=10.895}

Trying to add to queue: String 8.87, the result was true, size is 5, map : {1=1.324, 3=3.435, 6=6.460, 8=8.87, 10=10.895}

Trying to add to queue: String 7.733, the result was true, size is 6, map : {1=1.324, 3=3.435, 6=6.460, 7=7.733, 8=8.87, 10=10.895}

Trying to add to queue: String 2.365, the result was true, size is 7, map : {1=1.324, 2=2.365, 3=3.435, 6=6.460, 7=7.733, 8=8.87, 10=10.895}

consumer3 consume 3.435, after consumption, mMap size: 6, map : {6=6.460, 8=8.87, 10=10.895}

consumer6 consume 6.460, after consumption, mMap size: 4, map : {10=10.895}

consumer8 consume 8.87, after consumption, mMap size: 5, map : {6=6.460, 10=10.895}

consumer2 consume 2.365, after consumption, mMap size: 3, map : {10=10.895}

consumer7 consume 7.733, after consumption, mMap size: 1, map : {10=10.895}

consumer1 consume 1.324, after consumption, mMap size: 2, map : {10=10.895}

consumer10 consume 10.895, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 4.377, the result was true, size is 1, map : {4=4.377}

Trying to add to queue: String 4.151, the result was true, size is 1, map : {4=4.151}

Trying to add to queue: String 9.246, the result was true, size is 2, map : {4=4.151, 9=9.246}

Trying to add to queue: String 4.894, the result was true, size is 2, map : {4=4.894, 9=9.246}

Trying to add to queue: String 4.227, the result was true, size is 2, map : {4=4.227, 9=9.246}

Trying to add to queue: String 2.524, the result was true, size is 3, map : {2=2.524, 4=4.227, 9=9.246}

Trying to add to queue: String 3.445, the result was true, size is 4, map : {2=2.524, 3=3.445, 4=4.227, 9=9.246}

Trying to add to queue: String 9.982, the result was true, size is 4, map : {2=2.524, 3=3.445, 4=4.227, 9=9.982}

Trying to add to queue: String 10.967, the result was true, size is 5, map : {2=2.524, 3=3.445, 4=4.227, 9=9.982, 10=10.967}

Trying to add to queue: String 1.53, the result was true, size is 6, map : {1=1.53, 2=2.524, 3=3.445, 4=4.227, 9=9.982, 10=10.967}

consumer9 consume 9.982, after consumption, mMap size: 3, map : {1=1.53, 10=10.967}

consumer2 consume 2.524, after consumption, mMap size: 2, map : {10=10.967}

consumer1 consume 1.53, after consumption, mMap size: 1, map : {10=10.967}

consumer4 consume 4.227, after consumption, mMap size: 4, map : {1=1.53, 10=10.967}

consumer3 consume 3.445, after consumption, mMap size: 5, map : {1=1.53, 10=10.967}

consumer10 consume 10.967, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 6.438, the result was true, size is 1, map : {6=6.438}

Trying to add to queue: String 3.858, the result was true, size is 2, map : {3=3.858, 6=6.438}

Trying to add to queue: String 8.863, the result was true, size is 3, map : {3=3.858, 6=6.438, 8=8.863}

Trying to add to queue: String 10.589, the result was true, size is 4, map : {3=3.858, 6=6.438, 8=8.863, 10=10.589}

Trying to add to queue: String 1.88, the result was true, size is 5, map : {1=1.88, 3=3.858, 6=6.438, 8=8.863, 10=10.589}

Trying to add to queue: String 2.682, the result was true, size is 6, map : {1=1.88, 2=2.682, 3=3.858, 6=6.438, 8=8.863, 10=10.589}

Trying to add to queue: String 8.452, the result was true, size is 6, map : {1=1.88, 2=2.682, 3=3.858, 6=6.438, 8=8.452, 10=10.589}

Trying to add to queue: String 4.436, the result was true, size is 7, map : {1=1.88, 2=2.682, 3=3.858, 4=4.436, 6=6.438, 8=8.452, 10=10.589}

Trying to add to queue: String 10.248, the result was true, size is 7, map : {1=1.88, 2=2.682, 3=3.858, 4=4.436, 6=6.438, 8=8.452, 10=10.248}

Trying to add to queue: String 6.641, the result was true, size is 7, map : {1=1.88, 2=2.682, 3=3.858, 4=4.436, 6=6.641, 8=8.452, 10=10.248}

consumer3 consume 3.858, after consumption, mMap size: 1, map : {10=10.248}

consumer8 consume 8.452, after consumption, mMap size: 3, map : {10=10.248}

consumer2 consume 2.682, after consumption, mMap size: 5, map : {3=3.858, 10=10.248}

consumer4 consume 4.436, after consumption, mMap size: 2, map : {10=10.248}

consumer6 consume 6.641, after consumption, mMap size: 3, map : {10=10.248}

consumer1 consume 1.88, after consumption, mMap size: 5, map : {3=3.858, 10=10.248}

consumer10 consume 10.248, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 6.599, the result was true, size is 1, map : {6=6.599}

Trying to add to queue: String 7.915, the result was true, size is 2, map : {6=6.599, 7=7.915}

Trying to add to queue: String 4.574, the result was true, size is 3, map : {4=4.574, 6=6.599, 7=7.915}

Trying to add to queue: String 3.41, the result was true, size is 4, map : {3=3.41, 4=4.574, 6=6.599, 7=7.915}

Trying to add to queue: String 9.489, the result was true, size is 5, map : {3=3.41, 4=4.574, 6=6.599, 7=7.915, 9=9.489}

Trying to add to queue: String 2.541, the result was true, size is 6, map : {2=2.541, 3=3.41, 4=4.574, 6=6.599, 7=7.915, 9=9.489}

Trying to add to queue: String 10.593, the result was true, size is 7, map : {2=2.541, 3=3.41, 4=4.574, 6=6.599, 7=7.915, 9=9.489, 10=10.593}

Trying to add to queue: String 9.817, the result was true, size is 7, map : {2=2.541, 3=3.41, 4=4.574, 6=6.599, 7=7.915, 9=9.817, 10=10.593}

Trying to add to queue: String 9.652, the result was true, size is 7, map : {2=2.541, 3=3.41, 4=4.574, 6=6.599, 7=7.915, 9=9.652, 10=10.593}

Trying to add to queue: String 2.416, the result was true, size is 7, map : {2=2.416, 3=3.41, 4=4.574, 6=6.599, 7=7.915, 9=9.652, 10=10.593}

consumer4 consume 4.574, after consumption, mMap size: 5, map : {9=9.652, 10=10.593}

consumer2 consume 2.416, after consumption, mMap size: 3, map : {}

consumer9 consume 9.652, after consumption, mMap size: 4, map : {10=10.593}

consumer10 consume 10.593, after consumption, mMap size: 0, map : {}

consumer6 consume 6.599, after consumption, mMap size: 1, map : {}

consumer7 consume 7.915, after consumption, mMap size: 5, map : {9=9.652, 10=10.593}

consumer3 consume 3.41, after consumption, mMap size: 1, map : {}

Trying to add to queue: String 4.260, the result was true, size is 1, map : {4=4.260}

Trying to add to queue: String 7.935, the result was true, size is 2, map : {4=4.260, 7=7.935}

Trying to add to queue: String 8.638, the result was true, size is 3, map : {4=4.260, 7=7.935, 8=8.638}

Trying to add to queue: String 2.20, the result was true, size is 4, map : {2=2.20, 4=4.260, 7=7.935, 8=8.638}

Trying to add to queue: String 5.258, the result was true, size is 5, map : {2=2.20, 4=4.260, 5=5.258, 7=7.935, 8=8.638}

Trying to add to queue: String 10.697, the result was true, size is 6, map : {2=2.20, 4=4.260, 5=5.258, 7=7.935, 8=8.638, 10=10.697}

Trying to add to queue: String 3.876, the result was true, size is 7, map : {2=2.20, 3=3.876, 4=4.260, 5=5.258, 7=7.935, 8=8.638, 10=10.697}

Trying to add to queue: String 9.431, the result was true, size is 8, map : {2=2.20, 3=3.876, 4=4.260, 5=5.258, 7=7.935, 8=8.638, 9=9.431, 10=10.697}

Trying to add to queue: String 2.263, the result was true, size is 8, map : {2=2.263, 3=3.876, 4=4.260, 5=5.258, 7=7.935, 8=8.638, 9=9.431, 10=10.697}

executor.isTerminated() status = false
canceled non-finished tasks
Interruption Occured in consumer7
Interruption Occured in consumer10
Interruption Occured in consumer5
Interruption Occured in consumer2
Interruption Occured in consumer3
Interruption Occured in consumer4
Interruption Occured in consumer6
Interruption Occured in consumer1
Interruption Occured in consumer9
Interruption Occured in consumer8
executor.isTerminated() status = false
canceled non-finished tasks
Interruption Occured in producer
------------------------------------------------------------------------
BUILD SUCCESS
------------------------------------------------------------------------
Total time: 5.436s
Finished at: Tue Apr 25 23:38:16 PDT 2017
Final Memory: 7M/245M
------------------------------------------------------------------------
*/
