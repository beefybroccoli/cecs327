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
                TimeUnit.MILLISECONDS.sleep(100);
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