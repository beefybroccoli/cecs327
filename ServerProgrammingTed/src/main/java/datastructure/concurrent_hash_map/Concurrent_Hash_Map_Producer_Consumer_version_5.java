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
    int mID;

    public Producer_Concurrent_Hash_Map_Version_5(int inputID, ConcurrentHashMap<String, String> queue, Striped<ReadWriteLock> inputRWLock, int inputNUMBER_OF_CONSUMER) {
        this.mMap = queue;
        mFlag = true;
        mSharedRWLock = inputRWLock;
        mNUMBER_OF_CONSUMER = inputNUMBER_OF_CONSUMER;
        mID = inputID;

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

                        mMap.put(key, value);
                        System.out.println(
                                //                            mLock.toString() + ", " +
                                "Producer" + mID + " try to add to queue: String " + mMap.get(key)
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
                echo("Interruption Occured in producer" + mID);
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

        
        //half consumer and half producer
        run_specified_number_of_threads(6);

    }//end main method

    public static void run_specified_number_of_threads(int NUMBER_OF_THREADS) {
        int time_in_seconds = 5;
        Striped<ReadWriteLock> rwLockStripes = Striped.readWriteLock(NUMBER_OF_THREADS*2);
        ExecutorService executorProducer = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        ExecutorService executorConsumer = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        ConcurrentHashMap<String, String> sharedMap = new ConcurrentHashMap<>();        

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            int id = i + 1;
            executorProducer.submit(new Thread(new Producer_Concurrent_Hash_Map_Version_5(id, sharedMap, rwLockStripes, NUMBER_OF_THREADS/2)));
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
Producer3 try to add to queue: String 1.62, the result was true, size is 3, map : {1=1.62, 2=2.559, 5=5.239}

Producer1 try to add to queue: String 2.559, the result was true, size is 3, map : {1=1.62, 2=2.559, 5=5.239}

Producer6 try to add to queue: String 3.840, the result was true, size is 4, map : {1=1.62, 2=2.559, 3=3.840, 5=5.239}

Producer2 try to add to queue: String 5.239, the result was true, size is 3, map : {1=1.62, 2=2.559, 5=5.239}

Producer4 try to add to queue: String 2.975, the result was true, size is 4, map : {1=1.62, 2=2.975, 3=3.840, 5=5.239}

Producer5 try to add to queue: String 1.20, the result was true, size is 4, map : {1=1.20, 2=2.975, 3=3.840, 5=5.239}

Producer7 try to add to queue: String 2.565, the result was true, size is 3, map : {1=1.20, 2=2.565, 3=3.840}

consumer5 consume 5.239, after consumption, mMap size: 3, map : {1=1.20, 2=2.565, 3=3.840}

Producer8 try to add to queue: String 3.191, the result was true, size is 3, map : {1=1.20, 2=2.565, 3=3.191}

Producer9 try to add to queue: String 5.364, the result was true, size is 4, map : {1=1.20, 2=2.565, 3=3.191, 5=5.364}

Producer10 try to add to queue: String 2.682, the result was true, size is 4, map : {1=1.20, 2=2.682, 3=3.191, 5=5.364}

consumer3 consume 3.191, after consumption, mMap size: 3, map : {2=2.682, 4=4.432, 5=5.364}

consumer2 consume 2.682, after consumption, mMap size: 2, map : {4=4.432, 5=5.334}

Producer1 try to add to queue: String 5.334, the result was true, size is 3, map : {2=2.682, 4=4.432, 5=5.334}

Producer8 try to add to queue: String 2.716, the result was true, size is 4, map : {2=2.716, 3=3.534, 4=4.432, 5=5.334}

Producer10 try to add to queue: String 4.432, the result was true, size is 3, map : {2=2.682, 4=4.432, 5=5.364}

consumer1 consume 1.20, after consumption, mMap size: 4, map : {2=2.682, 4=4.432, 5=5.364}

Producer6 try to add to queue: String 5.802, the result was true, size is 4, map : {2=2.716, 3=3.534, 4=4.432, 5=5.802}

Producer9 try to add to queue: String 1.926, the result was true, size is 5, map : {1=1.926, 2=2.716, 3=3.534, 4=4.487, 5=5.802}

Producer3 try to add to queue: String 5.283, the result was true, size is 5, map : {1=1.926, 2=2.716, 3=3.534, 4=4.487, 5=5.283}

Producer4 try to add to queue: String 1.791, the result was true, size is 5, map : {1=1.791, 2=2.716, 3=3.534, 4=4.487, 5=5.283}

Producer5 try to add to queue: String 3.534, the result was true, size is 3, map : {3=3.534, 4=4.432, 5=5.334}

Producer7 try to add to queue: String 5.924, the result was true, size is 5, map : {1=1.791, 2=2.716, 3=3.534, 4=4.487, 5=5.924}

Producer2 try to add to queue: String 4.487, the result was true, size is 4, map : {2=2.716, 3=3.534, 4=4.487, 5=5.802}

consumer5 consume 5.924, after consumption, mMap size: 4, map : {1=1.791, 2=2.716, 3=3.534, 4=4.487}

consumer4 consume 4.487, after consumption, mMap size: 3, map : {1=1.791, 2=2.716, 3=3.534}

Producer6 try to add to queue: String 2.639, the result was true, size is 3, map : {1=1.791, 2=2.639, 3=3.534}

Producer2 try to add to queue: String 2.22, the result was true, size is 3, map : {1=1.791, 2=2.22, 3=3.143}

Producer9 try to add to queue: String 3.143, the result was true, size is 3, map : {1=1.791, 2=2.22, 3=3.143}

Producer3 try to add to queue: String 1.927, the result was true, size is 3, map : {1=1.927, 2=2.22, 3=3.143}

consumer2 consume 2.22, after consumption, mMap size: 1, map : {1=1.927}

consumer3 consume 3.143, after consumption, mMap size: 2, map : {1=1.927, 2=2.22}

Producer4 try to add to queue: String 2.784, the result was true, size is 2, map : {1=1.282, 2=2.784}

Producer7 try to add to queue: String 1.282, the result was true, size is 2, map : {1=1.282, 2=2.784}

Producer5 try to add to queue: String 3.534, the result was true, size is 3, map : {1=1.282, 2=2.784, 3=3.534}

consumer1 consume 1.282, after consumption, mMap size: 2, map : {2=2.631, 3=3.534}

Producer1 try to add to queue: String 2.631, the result was true, size is 2, map : {2=2.631, 3=3.534}

Producer8 try to add to queue: String 1.902, the result was true, size is 3, map : {1=1.902, 2=2.631, 3=3.534, 5=5.609}

Producer10 try to add to queue: String 5.609, the result was true, size is 4, map : {1=1.902, 2=2.631, 3=3.534, 5=5.609}

Producer6 try to add to queue: String 1.338, the result was true, size is 4, map : {1=1.338, 2=2.631, 3=3.534, 5=5.193}

consumer2 consume 2.631, after consumption, mMap size: 3, map : {1=1.338, 3=3.534, 5=5.193}

Producer3 try to add to queue: String 5.193, the result was true, size is 4, map : {1=1.338, 2=2.631, 3=3.534, 5=5.193}

consumer3 consume 3.534, after consumption, mMap size: 2, map : {1=1.338, 5=5.193}

Producer2 try to add to queue: String 5.437, the result was true, size is 2, map : {1=1.494, 5=5.437}

Producer7 try to add to queue: String 1.494, the result was true, size is 2, map : {1=1.494, 5=5.193}

consumer5 consume 5.437, after consumption, mMap size: 3, map : {1=1.494, 2=2.757, 3=3.60}

Producer9 try to add to queue: String 3.60, the result was true, size is 4, map : {1=1.494, 2=2.757, 3=3.60, 5=5.437}

Producer4 try to add to queue: String 5.67, the result was true, size is 4, map : {2=2.757, 3=3.60, 4=4.715, 5=5.67}

Producer5 try to add to queue: String 2.757, the result was true, size is 3, map : {1=1.494, 2=2.757, 3=3.60, 5=5.437}

Producer1 try to add to queue: String 4.715, the result was true, size is 4, map : {2=2.757, 3=3.60, 4=4.715, 5=5.67}

consumer1 consume 1.494, after consumption, mMap size: 2, map : {2=2.757, 3=3.60}

Producer8 try to add to queue: String 3.504, the result was true, size is 4, map : {2=2.757, 3=3.504, 4=4.715, 5=5.67}

Producer10 try to add to queue: String 4.294, the result was true, size is 4, map : {2=2.757, 3=3.504, 4=4.294, 5=5.67}

consumer3 consume 3.504, after consumption, mMap size: 3, map : {2=2.176, 4=4.869, 5=5.230}

Producer3 try to add to queue: String 4.869, the result was true, size is 3, map : {2=2.176, 4=4.869, 5=5.230}

Producer2 try to add to queue: String 2.176, the result was true, size is 3, map : {2=2.176, 4=4.869, 5=5.230}

Producer6 try to add to queue: String 5.230, the result was true, size is 3, map : {2=2.176, 4=4.869, 5=5.230}

consumer2 consume 2.176, after consumption, mMap size: 3, map : {3=3.798, 4=4.599, 5=5.230}

Producer7 try to add to queue: String 4.599, the result was true, size is 4, map : {2=2.176, 3=3.798, 4=4.599, 5=5.230}

Producer1 try to add to queue: String 3.798, the result was true, size is 4, map : {2=2.176, 3=3.798, 4=4.869, 5=5.230}

Producer5 try to add to queue: String 4.816, the result was true, size is 2, map : {3=3.798, 4=4.816}

consumer5 consume 5.230, after consumption, mMap size: 2, map : {3=3.798, 4=4.599}

Producer4 try to add to queue: String 4.85, the result was true, size is 2, map : {3=3.584, 4=4.85}

Producer8 try to add to queue: String 3.584, the result was true, size is 2, map : {3=3.584, 4=4.816}

consumer4 consume 4.85, after consumption, mMap size: 2, map : {3=3.584, 5=5.256}

Producer9 try to add to queue: String 5.256, the result was true, size is 3, map : {3=3.584, 4=4.85, 5=5.256}

Producer10 try to add to queue: String 5.387, the result was true, size is 2, map : {3=3.584, 5=5.387}

consumer3 consume 3.584, after consumption, mMap size: 1, map : {5=5.387}

Producer8 try to add to queue: String 1.209, the result was true, size is 2, map : {1=1.209, 2=2.532}

Producer7 try to add to queue: String 3.379, the result was true, size is 3, map : {1=1.209, 2=2.532, 3=3.379}

Producer2 try to add to queue: String 2.532, the result was true, size is 2, map : {2=2.532, 5=5.387}

consumer5 consume 5.387, after consumption, mMap size: 1, map : {2=2.532}

Producer6 try to add to queue: String 2.690, the result was true, size is 3, map : {1=1.337, 2=2.690, 3=3.127}

Producer3 try to add to queue: String 3.127, the result was true, size is 3, map : {1=1.337, 2=2.532, 3=3.127}

Producer1 try to add to queue: String 2.967, the result was true, size is 3, map : {1=1.337, 2=2.967, 3=3.127}

Producer5 try to add to queue: String 3.366, the result was true, size is 3, map : {1=1.337, 2=2.967, 3=3.366}

Producer10 try to add to queue: String 2.621, the result was true, size is 3, map : {1=1.337, 2=2.621, 3=3.366}

Producer9 try to add to queue: String 1.337, the result was true, size is 3, map : {1=1.337, 2=2.532, 3=3.379}

Producer4 try to add to queue: String 1.366, the result was true, size is 3, map : {1=1.366, 2=2.621, 3=3.366}

consumer2 consume 2.621, after consumption, mMap size: 2, map : {3=3.366}

consumer1 consume 1.366, after consumption, mMap size: 1, map : {3=3.366}

consumer3 consume 3.366, after consumption, mMap size: 0, map : {}

Producer8 try to add to queue: String 3.606, the result was true, size is 1, map : {3=3.606}

Producer7 try to add to queue: String 4.300, the result was true, size is 2, map : {3=3.606, 4=4.300}

Producer2 try to add to queue: String 3.452, the result was true, size is 2, map : {3=3.452, 4=4.300}

Producer6 try to add to queue: String 4.719, the result was true, size is 2, map : {3=3.452, 4=4.719}

Producer3 try to add to queue: String 5.327, the result was true, size is 3, map : {3=3.452, 4=4.719, 5=5.327}

Producer1 try to add to queue: String 2.108, the result was true, size is 4, map : {2=2.108, 3=3.452, 4=4.719, 5=5.327}

Producer10 try to add to queue: String 5.256, the result was true, size is 4, map : {2=2.108, 3=3.452, 4=4.719, 5=5.256}

Producer5 try to add to queue: String 2.933, the result was true, size is 4, map : {2=2.933, 3=3.452, 4=4.719, 5=5.256}

Producer9 try to add to queue: String 2.591, the result was true, size is 4, map : {2=2.591, 3=3.452, 4=4.719, 5=5.256}

Producer4 try to add to queue: String 1.112, the result was true, size is 5, map : {1=1.112, 2=2.591, 3=3.452, 4=4.719, 5=5.256}

consumer1 consume 1.112, after consumption, mMap size: 4, map : {3=3.452, 5=5.256}

consumer3 consume 3.452, after consumption, mMap size: 3, map : {5=5.256}

consumer5 consume 5.256, after consumption, mMap size: 2, map : {}

Producer7 try to add to queue: String 3.797, the result was true, size is 4, map : {1=1.662, 3=3.797}

Producer1 try to add to queue: String 1.662, the result was true, size is 3, map : {1=1.662}

Producer6 try to add to queue: String 3.459, the result was true, size is 5, map : {1=1.662, 3=3.459, 5=5.904}

Producer2 try to add to queue: String 5.904, the result was true, size is 5, map : {1=1.662, 3=3.797, 5=5.904}

consumer2 consume 2.591, after consumption, mMap size: 3, map : {1=1.662, 3=3.459, 5=5.904}

consumer4 consume 4.719, after consumption, mMap size: 3, map : {1=1.662, 3=3.459, 5=5.904}

Producer3 try to add to queue: String 2.578, the result was true, size is 4, map : {1=1.662, 2=2.578, 3=3.459, 5=5.904}

Producer8 try to add to queue: String 4.185, the result was true, size is 5, map : {1=1.662, 2=2.578, 3=3.459, 4=4.185, 5=5.904}

consumer3 consume 3.459, after consumption, mMap size: 4, map : {2=2.578, 4=4.185}

consumer4 consume 4.185, after consumption, mMap size: 0, map : {}

consumer2 consume 2.578, after consumption, mMap size: 1, map : {4=4.185}

consumer5 consume 5.904, after consumption, mMap size: 2, map : {2=2.578, 4=4.185}

consumer1 consume 1.662, after consumption, mMap size: 2, map : {2=2.578, 4=4.185}

Producer3 try to add to queue: String 4.482, the result was true, size is 2, map : {3=3.268, 4=4.482}

Producer7 try to add to queue: String 3.268, the result was true, size is 1, map : {3=3.268}

Producer8 try to add to queue: String 1.902, the result was true, size is 3, map : {1=1.902, 3=3.268, 4=4.482}

Producer1 try to add to queue: String 3.918, the result was true, size is 3, map : {1=1.902, 3=3.918, 4=4.482}

Producer6 try to add to queue: String 1.10, the result was true, size is 3, map : {1=1.10, 3=3.918, 4=4.482}

Producer2 try to add to queue: String 3.948, the result was true, size is 3, map : {1=1.10, 3=3.948, 4=4.482}

Producer10 try to add to queue: String 1.35, the result was true, size is 4, map : {1=1.35, 2=2.722, 3=3.948, 4=4.482}

Producer5 try to add to queue: String 2.722, the result was true, size is 4, map : {1=1.35, 2=2.722, 3=3.948, 4=4.482}

Producer4 try to add to queue: String 1.151, the result was true, size is 4, map : {1=1.151, 2=2.722, 3=3.271, 4=4.482}

Producer9 try to add to queue: String 3.271, the result was true, size is 4, map : {1=1.151, 2=2.722, 3=3.271, 4=4.482}

Producer7 try to add to queue: String 5.619, the result was true, size is 5, map : {1=1.52, 2=2.722, 3=3.271, 4=4.482, 5=5.619}

consumer4 consume 4.482, after consumption, mMap size: 4, map : {1=1.52, 2=2.722, 3=3.271, 5=5.619}

Producer8 try to add to queue: String 1.52, the result was true, size is 5, map : {1=1.52, 2=2.722, 3=3.271, 4=4.482, 5=5.619}

consumer3 consume 3.271, after consumption, mMap size: 4, map : {1=1.52, 2=2.722, 4=4.17, 5=5.372}

consumer2 consume 2.722, after consumption, mMap size: 3, map : {1=1.52, 4=4.17, 5=5.372}

consumer1 consume 1.52, after consumption, mMap size: 2, map : {4=4.17, 5=5.372}

Producer2 try to add to queue: String 4.17, the result was true, size is 5, map : {1=1.52, 2=2.722, 3=3.271, 4=4.17, 5=5.372}

Producer6 try to add to queue: String 5.372, the result was true, size is 4, map : {1=1.52, 2=2.722, 3=3.271, 5=5.372}

Producer3 try to add to queue: String 5.995, the result was true, size is 2, map : {4=4.17, 5=5.995}

Producer5 try to add to queue: String 5.646, the result was true, size is 2, map : {4=4.17, 5=5.646}

Producer4 try to add to queue: String 5.176, the result was true, size is 2, map : {4=4.17, 5=5.176}

Producer9 try to add to queue: String 5.620, the result was true, size is 2, map : {4=4.17, 5=5.620}

consumer5 consume 5.620, after consumption, mMap size: 1, map : {4=4.17}

Producer8 try to add to queue: String 3.718, the result was true, size is 2, map : {3=3.718, 4=4.471}

Producer7 try to add to queue: String 4.471, the result was true, size is 1, map : {4=4.471}

Producer1 try to add to queue: String 2.214, the result was true, size is 3, map : {2=2.214, 3=3.718, 4=4.471}

consumer4 consume 4.471, after consumption, mMap size: 2, map : {2=2.214, 3=3.718}

consumer3 consume 3.718, after consumption, mMap size: 1, map : {2=2.214}

consumer2 consume 2.214, after consumption, mMap size: 0, map : {}

Producer10 try to add to queue: String 4.210, the result was true, size is 1, map : {4=4.210}

Producer2 try to add to queue: String 4.789, the result was true, size is 1, map : {4=4.789}

Producer3 try to add to queue: String 2.858, the result was true, size is 3, map : {2=2.858, 3=3.819, 4=4.789}

Producer6 try to add to queue: String 3.819, the result was true, size is 3, map : {2=2.858, 3=3.819, 4=4.789}

Producer5 try to add to queue: String 2.261, the result was true, size is 3, map : {2=2.261, 3=3.819, 4=4.789}

Producer4 try to add to queue: String 4.557, the result was true, size is 4, map : {1=1.855, 2=2.261, 3=3.819, 4=4.557}

Producer9 try to add to queue: String 1.855, the result was true, size is 4, map : {1=1.855, 2=2.261, 3=3.819, 4=4.557}

consumer2 consume 2.261, after consumption, mMap size: 2, map : {1=1.855, 3=3.819}

Producer5 try to add to queue: String 1.977, the result was true, size is 2, map : {1=1.977, 5=5.952}

Producer8 try to add to queue: String 2.145, the result was true, size is 3, map : {1=1.977, 2=2.145, 5=5.952}

Producer2 try to add to queue: String 5.952, the result was true, size is 2, map : {1=1.855, 5=5.952}

consumer3 consume 3.819, after consumption, mMap size: 1, map : {1=1.855}

consumer4 consume 4.557, after consumption, mMap size: 2, map : {1=1.855}

Producer1 try to add to queue: String 2.203, the result was true, size is 2, map : {2=2.203, 5=5.952}

Producer7 try to add to queue: String 4.495, the result was true, size is 4, map : {2=2.203, 3=3.511, 4=4.495, 5=5.952}

consumer1 consume 1.977, after consumption, mMap size: 2, map : {2=2.145, 5=5.952}

Producer3 try to add to queue: String 4.790, the result was true, size is 4, map : {2=2.203, 3=3.511, 4=4.790, 5=5.952}

Producer10 try to add to queue: String 3.511, the result was true, size is 3, map : {2=2.203, 3=3.511, 5=5.952}

Producer6 try to add to queue: String 3.418, the result was true, size is 4, map : {2=2.203, 3=3.418, 4=4.790, 5=5.952}

Producer4 try to add to queue: String 4.236, the result was true, size is 4, map : {2=2.203, 3=3.418, 4=4.236, 5=5.952}

Producer9 try to add to queue: String 3.129, the result was true, size is 4, map : {2=2.203, 3=3.129, 4=4.236, 5=5.952}

consumer2 consume 2.203, after consumption, mMap size: 3, map : {3=3.129, 4=4.236}

consumer3 consume 3.129, after consumption, mMap size: 1, map : {}

consumer4 consume 4.236, after consumption, mMap size: 0, map : {}

consumer5 consume 5.952, after consumption, mMap size: 2, map : {3=3.129, 4=4.236}

Producer7 try to add to queue: String 4.151, the result was true, size is 3, map : {1=1.301, 2=2.772, 4=4.151}

Producer8 try to add to queue: String 2.772, the result was true, size is 2, map : {1=1.301, 2=2.772}

Producer3 try to add to queue: String 4.658, the result was true, size is 4, map : {1=1.301, 2=2.772, 4=4.658, 5=5.146}

Producer5 try to add to queue: String 1.301, the result was true, size is 1, map : {1=1.301}

Producer10 try to add to queue: String 5.146, the result was true, size is 4, map : {1=1.301, 2=2.772, 4=4.151, 5=5.146}

Producer2 try to add to queue: String 1.748, the result was true, size is 4, map : {1=1.748, 2=2.772, 4=4.658, 5=5.146}

Producer6 try to add to queue: String 5.474, the result was true, size is 4, map : {1=1.748, 2=2.772, 4=4.658, 5=5.474}

Producer1 try to add to queue: String 1.220, the result was true, size is 4, map : {1=1.220, 2=2.772, 4=4.658, 5=5.474}

consumer1 consume 1.220, after consumption, mMap size: 3, map : {2=2.772, 4=4.658, 5=5.474}

Producer4 try to add to queue: String 2.853, the result was true, size is 3, map : {2=2.853, 4=4.658, 5=5.474}

Producer9 try to add to queue: String 2.780, the result was true, size is 3, map : {2=2.780, 4=4.658, 5=5.474}

consumer4 consume 4.658, after consumption, mMap size: 1, map : {5=5.474}

Producer5 try to add to queue: String 1.589, the result was true, size is 2, map : {1=1.589, 3=3.180}

Producer7 try to add to queue: String 3.180, the result was true, size is 2, map : {1=1.589, 3=3.180}

Producer8 try to add to queue: String 1.259, the result was true, size is 2, map : {1=1.259, 3=3.180}

Producer3 try to add to queue: String 3.418, the result was true, size is 2, map : {1=1.259, 3=3.418}

consumer1 consume 1.259, after consumption, mMap size: 1, map : {3=3.418}

Producer10 try to add to queue: String 3.354, the result was true, size is 1, map : {3=3.354}

consumer5 consume 5.474, after consumption, mMap size: 0, map : {1=1.589, 3=3.180}

Producer1 try to add to queue: String 3.738, the result was true, size is 1, map : {3=3.738}

consumer2 consume 2.780, after consumption, mMap size: 2, map : {5=5.474}

Producer2 try to add to queue: String 5.700, the result was true, size is 2, map : {3=3.738, 5=5.700}

Producer6 try to add to queue: String 2.398, the result was true, size is 3, map : {2=2.398, 3=3.738, 5=5.700}

Producer4 try to add to queue: String 1.573, the result was true, size is 4, map : {1=1.573, 2=2.398, 3=3.738, 5=5.700}

Producer9 try to add to queue: String 1.639, the result was true, size is 4, map : {1=1.639, 2=2.398, 3=3.738, 5=5.700}

consumer3 consume 3.738, after consumption, mMap size: 3, map : {1=1.639, 2=2.398, 5=5.700}

Producer7 try to add to queue: String 4.609, the result was true, size is 1, map : {4=4.609}

consumer2 consume 2.398, after consumption, mMap size: 0, map : {}

consumer5 consume 5.700, after consumption, mMap size: 1, map : {}

consumer1 consume 1.639, after consumption, mMap size: 2, map : {2=2.398, 5=5.700}

Producer2 try to add to queue: String 3.216, the result was true, size is 4, map : {2=2.334, 3=3.216, 4=4.599, 5=5.211}

Producer10 try to add to queue: String 2.334, the result was true, size is 4, map : {2=2.334, 3=3.216, 4=4.599, 5=5.211}

Producer3 try to add to queue: String 5.211, the result was true, size is 4, map : {2=2.334, 3=3.216, 4=4.599, 5=5.211}

Producer5 try to add to queue: String 4.599, the result was true, size is 2, map : {3=3.216, 4=4.599}

Producer1 try to add to queue: String 5.689, the result was true, size is 4, map : {2=2.334, 3=3.216, 4=4.599, 5=5.689}

Producer8 try to add to queue: String 4.65, the result was true, size is 4, map : {2=2.334, 3=3.216, 4=4.65, 5=5.689}

Producer6 try to add to queue: String 4.810, the result was true, size is 4, map : {2=2.334, 3=3.216, 4=4.810, 5=5.689}

Producer4 try to add to queue: String 2.326, the result was true, size is 5, map : {1=1.474, 2=2.326, 3=3.216, 4=4.810, 5=5.689}

Producer9 try to add to queue: String 1.474, the result was true, size is 5, map : {1=1.474, 2=2.326, 3=3.216, 4=4.810, 5=5.689}

consumer1 consume 1.474, after consumption, mMap size: 1, map : {}

consumer3 consume 3.216, after consumption, mMap size: 1, map : {}

consumer4 consume 4.810, after consumption, mMap size: 0, map : {}

consumer2 consume 2.326, after consumption, mMap size: 1, map : {}

Producer5 try to add to queue: String 4.458, the result was true, size is 3, map : {1=1.370, 3=3.487, 4=4.458}

consumer5 consume 5.689, after consumption, mMap size: 1, map : {}

Producer3 try to add to queue: String 4.491, the result was true, size is 3, map : {1=1.370, 3=3.487, 4=4.491}

Producer10 try to add to queue: String 3.487, the result was true, size is 2, map : {1=1.370, 3=3.487}

Producer2 try to add to queue: String 4.351, the result was true, size is 4, map : {1=1.370, 3=3.487, 4=4.351, 5=5.499}

Producer8 try to add to queue: String 1.370, the result was true, size is 1, map : {1=1.370}

Producer1 try to add to queue: String 5.499, the result was true, size is 4, map : {1=1.370, 3=3.487, 4=4.491, 5=5.499}

Producer6 try to add to queue: String 5.7, the result was true, size is 4, map : {1=1.370, 3=3.487, 4=4.351, 5=5.7}

Producer4 try to add to queue: String 3.786, the result was true, size is 4, map : {1=1.370, 3=3.786, 4=4.351, 5=5.7}

Producer9 try to add to queue: String 3.955, the result was true, size is 4, map : {1=1.370, 3=3.955, 4=4.351, 5=5.7}

Producer7 try to add to queue: String 1.319, the result was true, size is 4, map : {1=1.319, 3=3.955, 4=4.351, 5=5.7}

consumer5 consume 5.7, after consumption, mMap size: 2, map : {1=1.319}

consumer4 consume 4.351, after consumption, mMap size: 1, map : {1=1.319}

consumer3 consume 3.955, after consumption, mMap size: 3, map : {1=1.319, 4=4.351, 5=5.7}

Producer2 try to add to queue: String 4.926, the result was true, size is 3, map : {2=2.193, 4=4.926, 5=5.630}

Producer5 try to add to queue: String 5.630, the result was true, size is 2, map : {2=2.193, 5=5.630}

consumer1 consume 1.319, after consumption, mMap size: 1, map : {2=2.193}

Producer3 try to add to queue: String 5.746, the result was true, size is 3, map : {2=2.193, 4=4.926, 5=5.746}

Producer1 try to add to queue: String 2.193, the result was true, size is 2, map : {1=1.319, 2=2.193}

Producer8 try to add to queue: String 5.933, the result was true, size is 4, map : {1=1.680, 2=2.193, 4=4.926, 5=5.933}

Producer10 try to add to queue: String 1.680, the result was true, size is 4, map : {1=1.680, 2=2.193, 4=4.926, 5=5.746}

Producer6 try to add to queue: String 5.890, the result was true, size is 4, map : {1=1.680, 2=2.193, 4=4.926, 5=5.890}

Producer4 try to add to queue: String 4.552, the result was true, size is 4, map : {1=1.680, 2=2.193, 4=4.552, 5=5.890}

Producer9 try to add to queue: String 3.61, the result was true, size is 5, map : {1=1.680, 2=2.193, 3=3.61, 4=4.552, 5=5.890}

consumer5 consume 5.890, after consumption, mMap size: 4, map : {1=1.680, 2=2.193, 3=3.61}

Producer8 try to add to queue: String 1.376, the result was true, size is 2, map : {1=1.376, 2=2.298}

Producer5 try to add to queue: String 2.298, the result was true, size is 2, map : {1=1.680, 2=2.298}

consumer3 consume 3.61, after consumption, mMap size: 2, map : {1=1.680, 2=2.298}

consumer4 consume 4.552, after consumption, mMap size: 3, map : {1=1.680, 2=2.193}

Producer10 try to add to queue: String 2.823, the result was true, size is 3, map : {1=1.796, 2=2.823, 5=5.400}

Producer6 try to add to queue: String 1.796, the result was true, size is 3, map : {1=1.796, 2=2.298, 5=5.400}

Producer2 try to add to queue: String 5.400, the result was true, size is 3, map : {1=1.376, 2=2.298, 5=5.400}

consumer1 consume 1.796, after consumption, mMap size: 3, map : {3=3.612, 4=4.238, 5=5.400}

consumer2 consume 2.823, after consumption, mMap size: 4, map : {1=1.796, 3=3.612, 4=4.238, 5=5.400}

Producer1 try to add to queue: String 4.238, the result was true, size is 5, map : {1=1.796, 2=2.823, 3=3.612, 4=4.238, 5=5.400}

Producer3 try to add to queue: String 3.612, the result was true, size is 4, map : {1=1.796, 2=2.823, 3=3.612, 5=5.400}

Producer4 try to add to queue: String 1.459, the result was true, size is 4, map : {1=1.459, 3=3.612, 4=4.238, 5=5.400}

Producer9 try to add to queue: String 1.407, the result was true, size is 4, map : {1=1.407, 3=3.612, 4=4.238, 5=5.400}

consumer5 consume 5.400, after consumption, mMap size: 3, map : {1=1.407, 3=3.612, 4=4.238}

Producer7 try to add to queue: String 5.399, the result was true, size is 4, map : {1=1.407, 3=3.612, 4=4.238, 5=5.399}

Producer6 try to add to queue: String 2.249, the result was true, size is 3, map : {1=1.926, 2=2.249, 5=5.399}

Producer5 try to add to queue: String 1.926, the result was true, size is 3, map : {1=1.926, 2=2.249, 5=5.399}

consumer3 consume 3.612, after consumption, mMap size: 3, map : {1=1.926, 2=2.249, 5=5.399}

consumer4 consume 4.238, after consumption, mMap size: 2, map : {1=1.926, 2=2.249, 5=5.399}

Producer10 try to add to queue: String 3.404, the result was true, size is 4, map : {1=1.145, 2=2.856, 3=3.404, 5=5.919}

Producer8 try to add to queue: String 1.145, the result was true, size is 3, map : {1=1.145, 2=2.856, 5=5.919}

Producer2 try to add to queue: String 2.856, the result was true, size is 3, map : {1=1.926, 2=2.856, 5=5.919}

consumer1 consume 1.145, after consumption, mMap size: 3, map : {2=2.856, 3=3.404, 5=5.919}

consumer2 consume 2.856, after consumption, mMap size: 2, map : {3=3.404, 5=5.919}

Producer1 try to add to queue: String 5.919, the result was true, size is 3, map : {1=1.926, 2=2.249, 5=5.919}

Producer3 try to add to queue: String 1.524, the result was true, size is 3, map : {1=1.524, 3=3.404, 5=5.919}

Producer9 try to add to queue: String 1.828, the result was true, size is 3, map : {1=1.828, 2=2.558, 3=3.404, 5=5.919}

Producer4 try to add to queue: String 2.558, the result was true, size is 4, map : {1=1.828, 2=2.558, 3=3.404, 5=5.919}

consumer5 consume 5.919, after consumption, mMap size: 3, map : {1=1.828, 2=2.558, 3=3.404}

Producer10 try to add to queue: String 5.820, the result was true, size is 4, map : {1=1.932, 2=2.400, 3=3.302, 5=5.820}

Producer7 try to add to queue: String 2.400, the result was true, size is 3, map : {1=1.932, 2=2.400, 3=3.302, 5=5.820}

Producer6 try to add to queue: String 3.302, the result was true, size is 3, map : {1=1.932, 2=2.400, 3=3.302, 5=5.820}

Producer5 try to add to queue: String 1.932, the result was true, size is 3, map : {1=1.932, 2=2.400, 3=3.302, 5=5.820}

Producer2 try to add to queue: String 2.334, the result was true, size is 4, map : {1=1.932, 2=2.334, 3=3.302, 5=5.820}

consumer1 consume 1.932, after consumption, mMap size: 2, map : {2=2.334, 5=5.820}

consumer3 consume 3.302, after consumption, mMap size: 3, map : {1=1.932, 2=2.334, 5=5.820}

Producer8 try to add to queue: String 1.166, the result was true, size is 2, map : {1=1.166, 5=5.820}

consumer2 consume 2.334, after consumption, mMap size: 1, map : {5=5.820}

Producer3 try to add to queue: String 1.703, the result was true, size is 3, map : {1=1.703, 3=3.531, 5=5.820}

Producer1 try to add to queue: String 3.531, the result was true, size is 3, map : {1=1.166, 3=3.531, 5=5.820}

Producer4 try to add to queue: String 2.559, the result was true, size is 4, map : {1=1.703, 2=2.559, 3=3.531, 5=5.820}

Producer9 try to add to queue: String 2.753, the result was true, size is 4, map : {1=1.703, 2=2.753, 3=3.531, 5=5.820}

consumer5 consume 5.820, after consumption, mMap size: 3, map : {1=1.703, 2=2.753, 3=3.531}

consumer1 consume 1.703, after consumption, mMap size: 3, map : {2=2.768, 3=3.531, 5=5.79}

Producer6 try to add to queue: String 5.79, the result was true, size is 4, map : {1=1.703, 2=2.768, 3=3.531, 5=5.79}

consumer3 consume 3.531, after consumption, mMap size: 2, map : {2=2.768, 5=5.79}

Producer10 try to add to queue: String 2.768, the result was true, size is 4, map : {1=1.703, 2=2.768, 3=3.531, 5=5.79}

Producer8 try to add to queue: String 3.469, the result was true, size is 5, map : {1=1.599, 2=2.768, 3=3.469, 4=4.178, 5=5.884}

Producer5 try to add to queue: String 2.814, the result was true, size is 5, map : {1=1.599, 2=2.814, 3=3.469, 4=4.178, 5=5.884}

Producer7 try to add to queue: String 5.884, the result was true, size is 4, map : {1=1.599, 2=2.768, 4=4.178, 5=5.884}

consumer2 consume 2.814, after consumption, mMap size: 4, map : {1=1.599, 3=3.469, 4=4.178, 5=5.884}

Producer3 try to add to queue: String 4.178, the result was true, size is 3, map : {1=1.599, 2=2.768, 4=4.178, 5=5.79}

Producer1 try to add to queue: String 1.599, the result was true, size is 4, map : {1=1.599, 2=2.768, 4=4.178, 5=5.79}

Producer2 try to add to queue: String 5.321, the result was true, size is 4, map : {1=1.599, 3=3.469, 4=4.178, 5=5.321}

Producer9 try to add to queue: String 3.220, the result was true, size is 4, map : {1=1.599, 3=3.220, 4=4.749, 5=5.321}

Producer4 try to add to queue: String 4.749, the result was true, size is 4, map : {1=1.599, 3=3.220, 4=4.749, 5=5.321}

consumer5 consume 5.321, after consumption, mMap size: 3, map : {1=1.599, 3=3.220, 4=4.749}

consumer4 consume 4.749, after consumption, mMap size: 2, map : {1=1.599, 3=3.220}

Producer5 try to add to queue: String 2.97, the result was true, size is 2, map : {2=2.97, 3=3.220}

consumer1 consume 1.599, after consumption, mMap size: 1, map : {3=3.220}

Producer7 try to add to queue: String 4.687, the result was true, size is 2, map : {2=2.97, 4=4.687}

consumer3 consume 3.220, after consumption, mMap size: 1, map : {2=2.97}

Producer10 try to add to queue: String 4.727, the result was true, size is 2, map : {1=1.950, 4=4.727}

Producer6 try to add to queue: String 1.950, the result was true, size is 2, map : {1=1.950, 4=4.687}

consumer2 consume 2.97, after consumption, mMap size: 1, map : {4=4.687}

Producer8 try to add to queue: String 4.342, the result was true, size is 2, map : {1=1.950, 4=4.342}

Producer1 try to add to queue: String 4.768, the result was true, size is 2, map : {1=1.950, 2=2.734, 4=4.768}

Producer3 try to add to queue: String 2.734, the result was true, size is 3, map : {1=1.950, 2=2.734, 4=4.768}

Producer2 try to add to queue: String 2.821, the result was true, size is 3, map : {1=1.950, 2=2.821, 4=4.768}

Producer9 try to add to queue: String 2.619, the result was true, size is 3, map : {1=1.950, 2=2.619, 4=4.768}

Producer4 try to add to queue: String 1.125, the result was true, size is 3, map : {1=1.125, 2=2.619, 4=4.768}

consumer4 consume 4.768, after consumption, mMap size: 3, map : {2=2.619, 3=3.462}

consumer2 consume 2.619, after consumption, mMap size: 1, map : {3=3.462}

consumer1 consume 1.125, after consumption, mMap size: 2, map : {2=2.619, 3=3.462}

Producer7 try to add to queue: String 2.465, the result was true, size is 3, map : {2=2.465, 3=3.462, 4=4.985}

Producer3 try to add to queue: String 1.453, the result was true, size is 4, map : {1=1.453, 2=2.465, 3=3.462, 4=4.985}

Producer8 try to add to queue: String 2.677, the result was true, size is 4, map : {1=1.453, 2=2.677, 3=3.462, 4=4.985}

Producer5 try to add to queue: String 3.462, the result was true, size is 2, map : {2=2.619, 3=3.462}

Producer1 try to add to queue: String 2.454, the result was true, size is 4, map : {1=1.453, 2=2.454, 3=3.462, 4=4.985}

Producer10 try to add to queue: String 3.598, the result was true, size is 4, map : {1=1.453, 2=2.454, 3=3.598, 4=4.985}

Producer6 try to add to queue: String 4.985, the result was true, size is 2, map : {3=3.462, 4=4.985}

consumer3 consume 3.598, after consumption, mMap size: 3, map : {1=1.453, 2=2.454, 4=4.985}

Producer2 try to add to queue: String 3.525, the result was true, size is 4, map : {1=1.453, 2=2.454, 3=3.525, 4=4.985}

Producer9 try to add to queue: String 4.83, the result was true, size is 4, map : {1=1.453, 2=2.454, 3=3.525, 4=4.83}

Producer4 try to add to queue: String 1.699, the result was true, size is 4, map : {1=1.699, 2=2.454, 3=3.525, 4=4.83}

consumer1 consume 1.699, after consumption, mMap size: 2, map : {3=3.525}

Producer10 try to add to queue: String 5.891, the result was true, size is 2, map : {3=3.371, 5=5.891}

Producer3 try to add to queue: String 1.363, the result was true, size is 3, map : {1=1.363, 3=3.371, 5=5.891}

Producer8 try to add to queue: String 3.371, the result was true, size is 1, map : {3=3.371}

consumer2 consume 2.454, after consumption, mMap size: 1, map : {3=3.525}

consumer4 consume 4.83, after consumption, mMap size: 2, map : {3=3.525}

Producer7 try to add to queue: String 2.593, the result was true, size is 4, map : {1=1.363, 2=2.593, 3=3.448, 5=5.891}

Producer1 try to add to queue: String 3.448, the result was true, size is 3, map : {1=1.363, 3=3.448, 5=5.891}

Producer5 try to add to queue: String 2.658, the result was true, size is 4, map : {1=1.363, 2=2.658, 3=3.448, 5=5.891}

consumer3 consume 3.448, after consumption, mMap size: 3, map : {1=1.363, 2=2.658, 5=5.891}

Producer6 try to add to queue: String 2.277, the result was true, size is 3, map : {1=1.363, 2=2.277, 5=5.891}

Producer2 try to add to queue: String 2.623, the result was true, size is 3, map : {1=1.363, 2=2.623, 5=5.891}

Producer9 try to add to queue: String 2.334, the result was true, size is 3, map : {1=1.363, 2=2.334, 4=4.509, 5=5.891}

Producer4 try to add to queue: String 4.509, the result was true, size is 4, map : {1=1.363, 2=2.334, 4=4.509, 5=5.891}

consumer5 consume 5.891, after consumption, mMap size: 3, map : {1=1.363, 2=2.334, 4=4.509}

Producer8 try to add to queue: String 3.470, the result was true, size is 3, map : {2=2.334, 3=3.470, 4=4.375}

consumer1 consume 1.363, after consumption, mMap size: 3, map : {2=2.334, 3=3.470, 4=4.375}

Producer3 try to add to queue: String 4.375, the result was true, size is 4, map : {2=2.334, 3=3.470, 4=4.375}

Producer7 try to add to queue: String 3.430, the result was true, size is 4, map : {1=1.851, 2=2.334, 3=3.430, 4=4.375}

Producer1 try to add to queue: String 1.851, the result was true, size is 4, map : {1=1.851, 3=3.430, 4=4.994}

Producer10 try to add to queue: String 4.994, the result was true, size is 3, map : {1=1.851, 3=3.430, 4=4.994}

consumer3 consume 3.430, after consumption, mMap size: 2, map : {1=1.851, 4=4.994}

consumer2 consume 2.334, after consumption, mMap size: 3, map : {1=1.851, 3=3.430, 4=4.994}

consumer4 consume 4.994, after consumption, mMap size: 1, map : {1=1.851}

Producer5 try to add to queue: String 3.351, the result was true, size is 2, map : {1=1.851, 3=3.351}

Producer6 try to add to queue: String 2.487, the result was true, size is 3, map : {1=1.851, 2=2.487, 3=3.351}

Producer2 try to add to queue: String 5.345, the result was true, size is 4, map : {1=1.851, 2=2.487, 3=3.351, 5=5.345}

Producer9 try to add to queue: String 3.58, the result was true, size is 4, map : {1=1.851, 2=2.487, 3=3.58, 4=4.738, 5=5.345}

Producer4 try to add to queue: String 4.738, the result was true, size is 5, map : {1=1.851, 2=2.487, 3=3.58, 4=4.738, 5=5.345}

consumer5 consume 5.345, after consumption, mMap size: 3, map : {2=2.487, 3=3.58, 4=4.738}

consumer1 consume 1.851, after consumption, mMap size: 3, map : {2=2.487, 3=3.58, 4=4.738}

Producer7 try to add to queue: String 3.355, the result was true, size is 3, map : {2=2.487, 3=3.355, 4=4.738}

Producer3 try to add to queue: String 1.711, the result was true, size is 3, map : {1=1.711, 3=3.355, 5=5.572}

consumer2 consume 2.487, after consumption, mMap size: 1, map : {3=3.355, 5=5.572}

Producer10 try to add to queue: String 3.950, the result was true, size is 3, map : {1=1.711, 3=3.950, 5=5.572}

Producer5 try to add to queue: String 5.572, the result was true, size is 2, map : {3=3.355, 5=5.572}

Producer1 try to add to queue: String 1.28, the result was true, size is 4, map : {1=1.28, 2=2.217, 3=3.950, 5=5.572}

consumer4 consume 4.738, after consumption, mMap size: 2, map : {3=3.355}

consumer3 consume 3.950, after consumption, mMap size: 3, map : {1=1.28, 2=2.217, 5=5.572}

Producer6 try to add to queue: String 4.454, the result was true, size is 4, map : {1=1.28, 2=2.217, 4=4.454, 5=5.572}

Producer2 try to add to queue: String 2.217, the result was true, size is 4, map : {1=1.28, 2=2.217, 3=3.950, 5=5.572}

Producer4 try to add to queue: String 4.547, the result was true, size is 4, map : {1=1.28, 2=2.217, 4=4.547, 5=5.572}

Producer9 try to add to queue: String 5.851, the result was true, size is 4, map : {1=1.28, 2=2.217, 4=4.547, 5=5.851}

Producer8 try to add to queue: String 4.938, the result was true, size is 4, map : {1=1.28, 2=2.217, 4=4.938, 5=5.851}

Producer5 try to add to queue: String 3.959, the result was true, size is 2, map : {3=3.959, 4=4.938}

consumer4 consume 4.938, after consumption, mMap size: 1, map : {3=3.959}

consumer2 consume 2.217, after consumption, mMap size: 1, map : {4=4.938}

consumer1 consume 1.28, after consumption, mMap size: 2, map : {4=4.938}

Producer2 try to add to queue: String 4.970, the result was true, size is 1, map : {4=4.970}

consumer5 consume 5.851, after consumption, mMap size: 2, map : {4=4.938}

Producer10 try to add to queue: String 1.835, the result was true, size is 2, map : {1=1.835, 4=4.970}

consumer3 consume 3.959, after consumption, mMap size: 0, map : {4=4.970}

Producer7 try to add to queue: String 5.102, the result was true, size is 3, map : {1=1.835, 4=4.38, 5=5.102}

Producer6 try to add to queue: String 4.38, the result was true, size is 2, map : {1=1.835, 4=4.38}

Producer3 try to add to queue: String 5.127, the result was true, size is 3, map : {1=1.835, 4=4.38, 5=5.127}

Producer1 try to add to queue: String 5.680, the result was true, size is 3, map : {1=1.835, 4=4.38, 5=5.680}

Producer4 try to add to queue: String 5.743, the result was true, size is 3, map : {1=1.835, 4=4.38, 5=5.743}

Producer9 try to add to queue: String 1.746, the result was true, size is 3, map : {1=1.746, 4=4.38, 5=5.743}

Producer5 try to add to queue: String 5.868, the result was true, size is 3, map : {1=1.746, 2=2.430, 4=4.38, 5=5.868}

Producer7 try to add to queue: String 3.487, the result was true, size is 3, map : {2=2.430, 3=3.487, 5=5.868}

consumer5 consume 5.868, after consumption, mMap size: 2, map : {2=2.430, 3=3.487}

consumer4 consume 4.38, after consumption, mMap size: 2, map : {2=2.430, 5=5.868}

consumer1 consume 1.746, after consumption, mMap size: 2, map : {2=2.430, 5=5.868}

Producer2 try to add to queue: String 4.3, the result was true, size is 4, map : {2=2.430, 3=3.487, 4=4.3, 5=5.47}

Producer8 try to add to queue: String 2.430, the result was true, size is 4, map : {1=1.746, 2=2.430, 4=4.38, 5=5.868}

Producer6 try to add to queue: String 4.505, the result was true, size is 4, map : {2=2.430, 3=3.487, 4=4.505, 5=5.47}

Producer10 try to add to queue: String 5.47, the result was true, size is 3, map : {2=2.430, 3=3.487, 5=5.47}

Producer1 try to add to queue: String 4.652, the result was true, size is 3, map : {3=3.487, 4=4.652, 5=5.47}

Producer3 try to add to queue: String 5.53, the result was true, size is 3, map : {3=3.487, 4=4.652, 5=5.53}

consumer2 consume 2.430, after consumption, mMap size: 3, map : {3=3.487, 4=4.505, 5=5.47}

Producer4 try to add to queue: String 1.176, the result was true, size is 4, map : {1=1.176, 3=3.487, 4=4.652, 5=5.53}

Producer9 try to add to queue: String 1.944, the result was true, size is 4, map : {1=1.944, 3=3.487, 4=4.652, 5=5.53}

consumer3 consume 3.487, after consumption, mMap size: 3, map : {1=1.944, 4=4.652, 5=5.53}

consumer1 consume 1.944, after consumption, mMap size: 3, map : {2=2.296, 4=4.652}

consumer4 consume 4.652, after consumption, mMap size: 2, map : {2=2.296, 3=3.828}

Producer5 try to add to queue: String 2.296, the result was true, size is 4, map : {1=1.944, 2=2.296, 4=4.652, 5=5.53}

Producer1 try to add to queue: String 3.828, the result was true, size is 3, map : {2=2.296, 3=3.828, 4=4.652}

Producer6 try to add to queue: String 4.514, the result was true, size is 3, map : {2=2.296, 3=3.828, 4=4.514}

consumer5 consume 5.53, after consumption, mMap size: 2, map : {2=2.296, 4=4.652}

Producer10 try to add to queue: String 4.802, the result was true, size is 3, map : {2=2.737, 3=3.99, 4=4.802}

Producer3 try to add to queue: String 3.99, the result was true, size is 3, map : {2=2.737, 3=3.99, 4=4.514}

Producer7 try to add to queue: String 2.737, the result was true, size is 3, map : {2=2.737, 3=3.828, 4=4.514}

Producer8 try to add to queue: String 5.218, the result was true, size is 4, map : {2=2.737, 3=3.99, 4=4.802, 5=5.218}

consumer2 consume 2.737, after consumption, mMap size: 3, map : {3=3.99, 4=4.802, 5=5.218}

Producer2 try to add to queue: String 5.360, the result was true, size is 3, map : {3=3.99, 4=4.802, 5=5.360}

Producer9 try to add to queue: String 2.863, the result was true, size is 4, map : {2=2.863, 3=3.704, 4=4.802, 5=5.360}

Producer4 try to add to queue: String 3.704, the result was true, size is 4, map : {2=2.863, 3=3.704, 4=4.802, 5=5.360}

consumer3 consume 3.704, after consumption, mMap size: 3, map : {2=2.863, 4=4.802, 5=5.360}

Producer6 try to add to queue: String 5.934, the result was true, size is 2, map : {2=2.941, 5=5.934}

Producer8 try to add to queue: String 1.227, the result was true, size is 4, map : {1=1.227, 2=2.941, 3=3.955, 5=5.934}

Producer2 try to add to queue: String 5.526, the result was true, size is 4, map : {1=1.227, 2=2.941, 3=3.955, 5=5.526}

Producer3 try to add to queue: String 3.955, the result was true, size is 3, map : {2=2.941, 3=3.955, 5=5.934}

consumer5 consume 5.526, after consumption, mMap size: 3, map : {1=1.227, 2=2.941, 3=3.955}

Producer5 try to add to queue: String 2.941, the result was true, size is 2, map : {2=2.941, 5=5.934}

consumer4 consume 4.802, after consumption, mMap size: 2, map : {2=2.941, 5=5.934}

Producer1 try to add to queue: String 2.867, the result was true, size is 4, map : {1=1.227, 2=2.867, 3=3.955, 5=5.264}

Producer7 try to add to queue: String 5.264, the result was true, size is 4, map : {1=1.227, 2=2.941, 3=3.955, 5=5.264}

Producer10 try to add to queue: String 2.734, the result was true, size is 4, map : {1=1.227, 2=2.734, 3=3.955, 5=5.264}

consumer2 consume 2.734, after consumption, mMap size: 3, map : {1=1.227, 3=3.955, 5=5.264}

Producer9 try to add to queue: String 2.116, the result was true, size is 4, map : {1=1.227, 2=2.116, 3=3.955, 5=5.264}

Producer4 try to add to queue: String 2.548, the result was true, size is 4, map : {1=1.227, 2=2.548, 3=3.955, 5=5.264}

Producer8 try to add to queue: String 1.598, the result was true, size is 4, map : {1=1.598, 2=2.548, 3=3.955, 5=5.264}

Producer2 try to add to queue: String 2.718, the result was true, size is 3, map : {1=1.598, 2=2.718, 5=5.231}

consumer1 consume 1.598, after consumption, mMap size: 3, map : {2=2.718, 4=4.41, 5=5.231}

Producer3 try to add to queue: String 5.231, the result was true, size is 3, map : {1=1.598, 2=2.548, 5=5.231}

Producer6 try to add to queue: String 1.567, the result was true, size is 4, map : {1=1.567, 2=2.637, 4=4.41, 5=5.231}

consumer3 consume 3.955, after consumption, mMap size: 3, map : {1=1.598, 2=2.548, 5=5.264}

Producer5 try to add to queue: String 2.637, the result was true, size is 3, map : {2=2.637, 4=4.41, 5=5.231}

Producer7 try to add to queue: String 4.41, the result was true, size is 4, map : {1=1.598, 2=2.718, 4=4.41, 5=5.231}

consumer2 consume 2.637, after consumption, mMap size: 2, map : {1=1.567, 4=4.41}

consumer5 consume 5.231, after consumption, mMap size: 3, map : {1=1.567, 2=2.637, 4=4.41}

Producer10 try to add to queue: String 4.786, the result was true, size is 2, map : {1=1.567, 4=4.786}

Producer1 try to add to queue: String 5.692, the result was true, size is 3, map : {1=1.567, 4=4.786, 5=5.692}

Producer9 try to add to queue: String 4.744, the result was true, size is 3, map : {1=1.567, 4=4.744, 5=5.86}

Producer4 try to add to queue: String 5.86, the result was true, size is 3, map : {1=1.567, 4=4.744, 5=5.86}

Producer2 try to add to queue: String 5.976, the result was true, size is 3, map : {1=1.567, 3=3.24, 4=4.744, 5=5.976}

consumer5 consume 5.976, after consumption, mMap size: 2, map : {2=2.42, 3=3.24}

consumer4 consume 4.744, after consumption, mMap size: 2, map : {3=3.24, 5=5.976}

consumer1 consume 1.567, after consumption, mMap size: 3, map : {3=3.24, 4=4.744, 5=5.976}

Producer6 try to add to queue: String 5.679, the result was true, size is 3, map : {2=2.42, 3=3.24, 5=5.679}

Producer8 try to add to queue: String 3.24, the result was true, size is 4, map : {1=1.567, 3=3.24, 4=4.744, 5=5.976}

Producer3 try to add to queue: String 1.813, the result was true, size is 4, map : {1=1.813, 2=2.42, 3=3.24, 5=5.679}

Producer5 try to add to queue: String 2.42, the result was true, size is 3, map : {2=2.42, 3=3.24, 5=5.976}

consumer3 consume 3.24, after consumption, mMap size: 3, map : {1=1.813, 2=2.42, 5=5.679}

Producer10 try to add to queue: String 2.204, the result was true, size is 3, map : {1=1.930, 2=2.204, 5=5.679}

Producer1 try to add to queue: String 1.930, the result was true, size is 3, map : {1=1.930, 2=2.42, 5=5.679}

Producer7 try to add to queue: String 3.547, the result was true, size is 4, map : {1=1.930, 2=2.204, 3=3.547, 5=5.679}

consumer2 consume 2.204, after consumption, mMap size: 3, map : {1=1.930, 3=3.547, 5=5.679}

Producer9 try to add to queue: String 1.611, the result was true, size is 3, map : {1=1.611, 3=3.547, 5=5.679}

Producer4 try to add to queue: String 3.52, the result was true, size is 3, map : {1=1.611, 3=3.52, 5=5.679}

Producer3 try to add to queue: String 1.666, the result was true, size is 4, map : {1=1.666, 2=2.838, 3=3.52, 5=5.679}

Producer8 try to add to queue: String 2.838, the result was true, size is 4, map : {1=1.666, 2=2.838, 3=3.52, 5=5.679}

Producer2 try to add to queue: String 2.363, the result was true, size is 4, map : {1=1.666, 2=2.363, 3=3.52, 5=5.679}

consumer3 consume 3.52, after consumption, mMap size: 2, map : {2=2.363, 4=4.883}

consumer2 consume 2.363, after consumption, mMap size: 1, map : {4=4.883}

Producer6 try to add to queue: String 4.883, the result was true, size is 3, map : {2=2.363, 3=3.52, 4=4.883}

Producer5 try to add to queue: String 3.484, the result was true, size is 2, map : {3=3.484, 4=4.883}

consumer1 consume 1.666, after consumption, mMap size: 3, map : {2=2.363, 3=3.52, 4=4.883}

Producer10 try to add to queue: String 4.80, the result was true, size is 2, map : {3=3.484, 4=4.80}

consumer5 consume 5.679, after consumption, mMap size: 3, map : {2=2.363, 3=3.52, 4=4.883}

Producer1 try to add to queue: String 5.929, the result was true, size is 3, map : {3=3.484, 4=4.80, 5=5.929}

Producer7 try to add to queue: String 5.426, the result was true, size is 3, map : {3=3.484, 4=4.80, 5=5.426}

Producer9 try to add to queue: String 4.30, the result was true, size is 3, map : {3=3.832, 4=4.30, 5=5.426}

Producer4 try to add to queue: String 3.832, the result was true, size is 3, map : {3=3.832, 4=4.30, 5=5.426}

Producer3 try to add to queue: String 1.102, the result was true, size is 4, map : {1=1.102, 3=3.832, 4=4.30, 5=5.426}

Producer8 try to add to queue: String 1.770, the result was true, size is 4, map : {1=1.770, 3=3.832, 4=4.30, 5=5.426}

Producer2 try to add to queue: String 1.625, the result was true, size is 4, map : {1=1.625, 5=5.426}

consumer5 consume 5.426, after consumption, mMap size: 1, map : {1=1.625}

Producer5 try to add to queue: String 1.408, the result was true, size is 1, map : {1=1.408}

consumer4 consume 4.30, after consumption, mMap size: 2, map : {1=1.625, 5=5.426}

consumer1 consume 1.408, after consumption, mMap size: 0, map : {}

consumer3 consume 3.832, after consumption, mMap size: 2, map : {1=1.625, 5=5.426}

Producer6 try to add to queue: String 4.526, the result was true, size is 1, map : {4=4.526}

Producer7 try to add to queue: String 3.682, the result was true, size is 2, map : {3=3.682, 4=4.526}

Producer10 try to add to queue: String 2.160, the result was true, size is 3, map : {2=2.160, 3=3.682, 4=4.526}

Producer1 try to add to queue: String 2.478, the result was true, size is 3, map : {2=2.478, 3=3.682, 4=4.526}

Producer9 try to add to queue: String 3.483, the result was true, size is 3, map : {2=2.478, 3=3.483, 4=4.96}

Producer4 try to add to queue: String 4.96, the result was true, size is 3, map : {2=2.478, 3=3.483, 4=4.96}

Producer8 try to add to queue: String 3.64, the result was true, size is 3, map : {2=2.478, 3=3.64, 4=4.931}

Producer3 try to add to queue: String 4.931, the result was true, size is 3, map : {2=2.478, 3=3.64, 4=4.931}

consumer2 consume 2.478, after consumption, mMap size: 2, map : {3=3.64, 4=4.931}

consumer4 consume 4.931, after consumption, mMap size: 2, map : {1=1.590, 2=2.599}

consumer3 consume 3.64, after consumption, mMap size: 2, map : {1=1.590, 2=2.599}

Producer6 try to add to queue: String 2.599, the result was true, size is 2, map : {1=1.590, 2=2.599}

Producer5 try to add to queue: String 1.590, the result was true, size is 4, map : {1=1.590, 2=2.599}

Producer2 try to add to queue: String 4.35, the result was true, size is 3, map : {1=1.590, 2=2.599, 4=4.35}

Producer7 try to add to queue: String 2.927, the result was true, size is 3, map : {1=1.590, 2=2.927, 4=4.35}

Producer10 try to add to queue: String 4.560, the result was true, size is 3, map : {1=1.590, 2=2.927, 4=4.560}

Producer1 try to add to queue: String 4.986, the result was true, size is 3, map : {1=1.590, 2=2.927, 4=4.986}

Producer4 try to add to queue: String 3.659, the result was true, size is 4, map : {1=1.590, 2=2.927, 3=3.659, 4=4.986}

Producer9 try to add to queue: String 4.754, the result was true, size is 4, map : {1=1.590, 2=2.927, 3=3.659, 4=4.754}

Producer8 try to add to queue: String 5.248, the result was true, size is 5, map : {1=1.590, 2=2.927, 3=3.659, 4=4.754, 5=5.248}

Producer3 try to add to queue: String 5.709, the result was true, size is 5, map : {1=1.590, 2=2.927, 3=3.659, 4=4.754, 5=5.709}

consumer2 consume 2.927, after consumption, mMap size: 4, map : {1=1.590, 3=3.659, 4=4.754, 5=5.709}

consumer5 consume 5.709, after consumption, mMap size: 2, map : {1=1.590, 3=3.659}

Producer6 try to add to queue: String 1.789, the result was true, size is 1, map : {1=1.789}

consumer3 consume 3.659, after consumption, mMap size: 1, map : {1=1.590}

consumer4 consume 4.754, after consumption, mMap size: 2, map : {1=1.590, 3=3.659}

Producer2 try to add to queue: String 1.3, the result was true, size is 1, map : {1=1.3}

Producer5 try to add to queue: String 3.805, the result was true, size is 2, map : {1=1.3, 3=3.805}

Producer7 try to add to queue: String 1.629, the result was true, size is 2, map : {1=1.629, 3=3.805}

Producer1 try to add to queue: String 1.490, the result was true, size is 2, map : {1=1.490, 3=3.805}

Producer10 try to add to queue: String 1.659, the result was true, size is 2, map : {1=1.659, 3=3.805}

consumer1 consume 1.659, after consumption, mMap size: 1, map : {3=3.805}

Producer4 try to add to queue: String 2.427, the result was true, size is 2, map : {1=1.519, 2=2.427, 3=3.805}

Producer9 try to add to queue: String 1.519, the result was true, size is 3, map : {1=1.519, 2=2.427, 3=3.805}

Producer3 try to add to queue: String 1.457, the result was true, size is 4, map : {1=1.457, 2=2.427, 3=3.805, 4=4.861}

Producer8 try to add to queue: String 4.861, the result was true, size is 4, map : {1=1.457, 2=2.427, 3=3.805, 4=4.861}

consumer2 consume 2.427, after consumption, mMap size: 3, map : {1=1.457, 3=3.805, 4=4.861}

Producer5 try to add to queue: String 5.528, the result was true, size is 4, map : {1=1.653, 3=3.868, 5=5.528}

Producer2 try to add to queue: String 3.868, the result was true, size is 4, map : {1=1.653, 3=3.868, 5=5.528}

Producer6 try to add to queue: String 1.653, the result was true, size is 4, map : {1=1.653, 3=3.868, 5=5.528}

consumer4 consume 4.861, after consumption, mMap size: 3, map : {1=1.653, 3=3.868, 5=5.528}

consumer1 consume 1.653, after consumption, mMap size: 1, map : {5=5.404}

consumer3 consume 3.868, after consumption, mMap size: 2, map : {1=1.653, 5=5.404}

Producer1 try to add to queue: String 4.868, the result was true, size is 2, map : {4=4.868, 5=5.404}

Producer7 try to add to queue: String 5.404, the result was true, size is 2, map : {5=5.404}

Producer10 try to add to queue: String 1.612, the result was true, size is 3, map : {1=1.612, 4=4.868, 5=5.404}

Producer4 try to add to queue: String 3.829, the result was true, size is 5, map : {1=1.612, 2=2.960, 3=3.829, 4=4.868, 5=5.404}

Producer9 try to add to queue: String 2.960, the result was true, size is 5, map : {1=1.612, 2=2.960, 3=3.829, 4=4.868, 5=5.404}

consumer2 consume 2.960, after consumption, mMap size: 4, map : {1=1.612, 3=3.829, 4=4.868, 5=5.404}

Producer6 try to add to queue: String 1.387, the result was true, size is 4, map : {1=1.387, 3=3.829, 5=5.404}

consumer3 consume 3.829, after consumption, mMap size: 2, map : {1=1.387, 2=2.975}

Producer10 try to add to queue: String 1.441, the result was true, size is 2, map : {1=1.441, 2=2.975}

Producer1 try to add to queue: String 3.967, the result was true, size is 3, map : {1=1.441, 2=2.975, 3=3.967}

consumer5 consume 5.404, after consumption, mMap size: 3, map : {1=1.387, 2=2.975, 3=3.829}

Producer5 try to add to queue: String 2.975, the result was true, size is 4, map : {1=1.387, 2=2.975, 3=3.829, 5=5.404}

Producer7 try to add to queue: String 5.862, the result was true, size is 4, map : {1=1.811, 2=2.975, 3=3.967, 5=5.862}

consumer4 consume 4.868, after consumption, mMap size: 3, map : {1=1.387, 3=3.829, 5=5.404}

Producer2 try to add to queue: String 1.811, the result was true, size is 3, map : {1=1.811, 2=2.975, 3=3.967}

consumer1 consume 1.811, after consumption, mMap size: 3, map : {2=2.975, 3=3.967, 5=5.862}

Producer4 try to add to queue: String 5.332, the result was true, size is 3, map : {2=2.975, 3=3.967, 5=5.332}

Producer9 try to add to queue: String 5.406, the result was true, size is 3, map : {2=2.975, 3=3.967, 5=5.406}

Producer3 try to add to queue: String 5.897, the result was true, size is 3, map : {2=2.975, 3=3.967, 5=5.897}

Producer8 try to add to queue: String 1.157, the result was true, size is 4, map : {1=1.157, 2=2.975, 3=3.967, 5=5.897}

consumer2 consume 2.975, after consumption, mMap size: 3, map : {1=1.157, 3=3.967, 5=5.897}

Producer10 try to add to queue: String 2.426, the result was true, size is 4, map : {1=1.790, 2=2.426, 3=3.967, 5=5.897}

consumer5 consume 5.897, after consumption, mMap size: 3, map : {1=1.790, 2=2.426, 3=3.785}

Producer7 try to add to queue: String 2.128, the result was true, size is 3, map : {1=1.790, 2=2.128, 3=3.785}

Producer5 try to add to queue: String 3.785, the result was true, size is 4, map : {1=1.790, 2=2.426, 3=3.785}

Producer2 try to add to queue: String 5.689, the result was true, size is 4, map : {1=1.790, 2=2.128, 3=3.785, 5=5.689}

Producer1 try to add to queue: String 1.790, the result was true, size is 4, map : {1=1.790, 2=2.426, 3=3.785}

consumer3 consume 3.785, after consumption, mMap size: 3, map : {1=1.790, 2=2.128, 5=5.689}

Producer6 try to add to queue: String 1.635, the result was true, size is 3, map : {1=1.635, 2=2.128, 5=5.689}

consumer1 consume 1.635, after consumption, mMap size: 2, map : {2=2.128, 5=5.689}

Producer4 try to add to queue: String 5.816, the result was true, size is 2, map : {2=2.449, 5=5.816}

Producer9 try to add to queue: String 2.449, the result was true, size is 2, map : {2=2.449, 5=5.816}

Producer3 try to add to queue: String 4.889, the result was true, size is 3, map : {2=2.81, 4=4.889, 5=5.816}

Producer8 try to add to queue: String 2.81, the result was true, size is 3, map : {2=2.81, 4=4.889, 5=5.816}

consumer2 consume 2.81, after consumption, mMap size: 2, map : {4=4.889, 5=5.816}

consumer4 consume 4.889, after consumption, mMap size: 2, map : {1=1.276, 5=5.816}

Producer1 try to add to queue: String 4.728, the result was true, size is 3, map : {1=1.276, 3=3.472, 4=4.728}

Producer7 try to add to queue: String 3.472, the result was true, size is 2, map : {1=1.276, 3=3.472}

Producer10 try to add to queue: String 1.276, the result was true, size is 3, map : {1=1.276, 5=5.816}

consumer5 consume 5.816, after consumption, mMap size: 1, map : {1=1.276}

Producer2 try to add to queue: String 1.42, the result was true, size is 2, map : {1=1.42, 4=4.728}

consumer3 consume 3.472, after consumption, mMap size: 2, map : {1=1.42, 4=4.728}

Producer5 try to add to queue: String 5.289, the result was true, size is 3, map : {1=1.42, 4=4.728, 5=5.289}

consumer1 consume 1.42, after consumption, mMap size: 2, map : {4=4.728, 5=5.289}

Producer6 try to add to queue: String 1.171, the result was true, size is 3, map : {1=1.171, 4=4.728, 5=5.289}

Producer4 try to add to queue: String 3.681, the result was true, size is 4, map : {1=1.915, 3=3.681, 4=4.728, 5=5.289}

Producer9 try to add to queue: String 1.915, the result was true, size is 4, map : {1=1.915, 3=3.681, 4=4.728, 5=5.289}

Producer10 try to add to queue: String 4.162, the result was true, size is 5, map : {1=1.915, 2=2.920, 3=3.681, 4=4.162, 5=5.289}

Producer8 try to add to queue: String 2.920, the result was true, size is 5, map : {1=1.915, 2=2.920, 3=3.681, 4=4.162, 5=5.289}

consumer4 consume 4.162, after consumption, mMap size: 3, map : {2=2.920, 3=3.681, 5=5.289}

consumer5 consume 5.289, after consumption, mMap size: 2, map : {2=2.920, 3=3.681}

consumer1 consume 1.915, after consumption, mMap size: 4, map : {2=2.920, 3=3.681, 4=4.162, 5=5.289}

Producer7 try to add to queue: String 4.411, the result was true, size is 2, map : {2=2.920, 4=4.411}

Producer1 try to add to queue: String 1.164, the result was true, size is 3, map : {1=1.164, 2=2.920, 4=4.411}

consumer3 consume 3.681, after consumption, mMap size: 1, map : {2=2.920}

Producer9 try to add to queue: String 4.416, the result was true, size is 3, map : {1=1.164, 2=2.252, 4=4.416}

Producer4 try to add to queue: String 2.252, the result was true, size is 3, map : {1=1.164, 2=2.252, 4=4.416}

Producer6 try to add to queue: String 1.221, the result was true, size is 4, map : {1=1.221, 2=2.252, 4=4.416, 5=5.323}

Producer5 try to add to queue: String 5.323, the result was true, size is 4, map : {1=1.221, 2=2.252, 4=4.416, 5=5.323}

Producer3 try to add to queue: String 4.673, the result was true, size is 3, map : {1=1.221, 4=4.673, 5=5.323}

Producer10 try to add to queue: String 1.588, the result was true, size is 3, map : {1=1.588, 4=4.673, 5=5.323}

consumer4 consume 4.673, after consumption, mMap size: 1, map : {1=1.588}

consumer2 consume 2.252, after consumption, mMap size: 3, map : {1=1.221, 4=4.673, 5=5.323}

consumer5 consume 5.323, after consumption, mMap size: 2, map : {1=1.588, 4=4.673}

Producer2 try to add to queue: String 2.885, the result was true, size is 2, map : {2=2.885, 4=4.503}

consumer1 consume 1.588, after consumption, mMap size: 1, map : {4=4.503}

Producer8 try to add to queue: String 2.292, the result was true, size is 2, map : {2=2.292, 4=4.503}

Producer1 try to add to queue: String 4.503, the result was true, size is 2, map : {4=4.503}

Producer7 try to add to queue: String 4.248, the result was true, size is 2, map : {2=2.292, 4=4.248}

Producer4 try to add to queue: String 3.330, the result was true, size is 3, map : {2=2.292, 3=3.330, 4=4.248}

Producer9 try to add to queue: String 2.24, the result was true, size is 3, map : {2=2.24, 3=3.330, 4=4.248}

Producer6 try to add to queue: String 2.312, the result was true, size is 3, map : {2=2.312, 3=3.330, 4=4.248}

consumer3 consume 3.330, after consumption, mMap size: 2, map : {2=2.312, 4=4.248}

Producer10 try to add to queue: String 5.160, the result was true, size is 3, map : {2=2.312, 4=4.248, 5=5.160}

Producer3 try to add to queue: String 1.294, the result was true, size is 4, map : {1=1.294, 2=2.312, 4=4.248, 5=5.160}

consumer2 consume 2.312, after consumption, mMap size: 3, map : {1=1.294, 4=4.248, 5=5.160}

consumer4 consume 4.248, after consumption, mMap size: 2, map : {1=1.150, 5=5.160}

Producer1 try to add to queue: String 2.887, the result was true, size is 3, map : {1=1.150, 2=2.887, 5=5.542}

Producer8 try to add to queue: String 5.542, the result was true, size is 2, map : {1=1.150, 5=5.542}

Producer5 try to add to queue: String 4.792, the result was true, size is 4, map : {1=1.150, 2=2.887, 4=4.792, 5=5.542}

Producer7 try to add to queue: String 1.150, the result was true, size is 3, map : {1=1.150, 4=4.248, 5=5.160}

consumer5 consume 5.542, after consumption, mMap size: 3, map : {1=1.150, 2=2.887, 4=4.792}

consumer1 consume 1.150, after consumption, mMap size: 2, map : {2=2.887, 4=4.792}

Producer2 try to add to queue: String 1.896, the result was true, size is 3, map : {1=1.896, 2=2.887, 4=4.792}

Producer9 try to add to queue: String 3.670, the result was true, size is 4, map : {1=1.279, 2=2.887, 3=3.670, 4=4.792}

Producer4 try to add to queue: String 1.279, the result was true, size is 4, map : {1=1.279, 2=2.887, 3=3.670, 4=4.792}

Producer6 try to add to queue: String 5.553, the result was true, size is 5, map : {1=1.279, 2=2.887, 3=3.670, 4=4.792, 5=5.553}

consumer3 consume 3.670, after consumption, mMap size: 4, map : {1=1.279, 4=4.792, 5=5.553}

consumer2 consume 2.887, after consumption, mMap size: 3, map : {1=1.279, 4=4.792, 5=5.553}

consumer4 consume 4.792, after consumption, mMap size: 2, map : {1=1.279, 5=5.553}

Producer7 try to add to queue: String 2.619, the result was true, size is 3, map : {1=1.33, 2=2.619, 5=5.986}

Producer1 try to add to queue: String 1.33, the result was true, size is 2, map : {1=1.33, 5=5.986}

Producer5 try to add to queue: String 5.986, the result was true, size is 2, map : {1=1.279, 5=5.986}

consumer1 consume 1.33, after consumption, mMap size: 3, map : {2=2.619, 4=4.653, 5=5.986}

Producer8 try to add to queue: String 4.653, the result was true, size is 4, map : {1=1.33, 2=2.619, 4=4.653, 5=5.986}

Producer2 try to add to queue: String 1.565, the result was true, size is 3, map : {1=1.565, 2=2.619, 4=4.653}

consumer5 consume 5.986, after consumption, mMap size: 2, map : {2=2.619, 4=4.653}

Producer4 try to add to queue: String 3.529, the result was true, size is 5, map : {1=1.565, 2=2.619, 3=3.529, 4=4.653, 5=5.918}

Producer9 try to add to queue: String 5.918, the result was true, size is 5, map : {1=1.565, 2=2.619, 3=3.529, 4=4.653, 5=5.918}

consumer2 consume 2.619, after consumption, mMap size: 2, map : {1=1.565, 5=5.918}

consumer3 consume 3.529, after consumption, mMap size: 2, map : {1=1.565, 5=5.918}

consumer4 consume 4.653, after consumption, mMap size: 2, map : {1=1.565, 5=5.918}

consumer1 consume 1.565, after consumption, mMap size: 1, map : {5=5.918}

Producer8 try to add to queue: String 3.844, the result was true, size is 1, map : {3=3.844}

consumer5 consume 5.918, after consumption, mMap size: 0, map : {}

Producer2 try to add to queue: String 3.542, the result was true, size is 1, map : {3=3.542}

Producer9 try to add to queue: String 5.936, the result was true, size is 2, map : {3=3.542, 5=5.936}

Producer4 try to add to queue: String 5.730, the result was true, size is 2, map : {3=3.542, 5=5.730}

Producer3 try to add to queue: String 2.709, the result was true, size is 4, map : {2=2.709, 3=3.542, 4=4.603, 5=5.730}

Producer1 try to add to queue: String 1.480, the result was true, size is 5, map : {1=1.480, 2=2.709, 3=3.542, 4=4.603, 5=5.730}

Producer7 try to add to queue: String 4.603, the result was true, size is 4, map : {2=2.709, 3=3.542, 4=4.603, 5=5.730}

consumer1 consume 1.480, after consumption, mMap size: 3, map : {3=3.118, 4=4.603, 5=5.539}

Producer6 try to add to queue: String 3.118, the result was true, size is 5, map : {1=1.480, 3=3.118, 4=4.603, 5=5.539}

consumer2 consume 2.709, after consumption, mMap size: 4, map : {1=1.480, 3=3.118, 4=4.603, 5=5.539}

Producer5 try to add to queue: String 3.167, the result was true, size is 2, map : {3=3.167, 5=5.539}

Producer10 try to add to queue: String 5.539, the result was true, size is 4, map : {1=1.480, 3=3.118, 4=4.603, 5=5.539}

consumer3 consume 3.167, after consumption, mMap size: 1, map : {5=5.539}

consumer4 consume 4.603, after consumption, mMap size: 2, map : {3=3.118, 5=5.539}

consumer5 consume 5.539, after consumption, mMap size: 0, map : {}

Producer9 try to add to queue: String 1.664, the result was true, size is 1, map : {1=1.664, 4=4.270}

Producer4 try to add to queue: String 4.270, the result was true, size is 2, map : {1=1.664, 4=4.270}

Producer3 try to add to queue: String 3.592, the result was true, size is 4, map : {1=1.664, 3=3.592, 4=4.660, 5=5.316}

consumer1 consume 1.664, after consumption, mMap size: 3, map : {3=3.592, 4=4.660, 5=5.316}

Producer10 try to add to queue: String 3.899, the result was true, size is 3, map : {3=3.899, 4=4.660, 5=5.316}

Producer8 try to add to queue: String 4.660, the result was true, size is 3, map : {1=1.664, 3=3.592, 4=4.660, 5=5.316}

Producer1 try to add to queue: String 5.316, the result was true, size is 4, map : {1=1.664, 3=3.592, 4=4.660, 5=5.316}

Producer6 try to add to queue: String 5.100, the result was true, size is 3, map : {3=3.899, 4=4.660, 5=5.100}

consumer3 consume 3.899, after consumption, mMap size: 2, map : {4=4.279, 5=5.100}

Producer2 try to add to queue: String 4.279, the result was true, size is 2, map : {4=4.279, 5=5.100}

consumer5 consume 5.100, after consumption, mMap size: 1, map : {4=4.279}

Producer7 try to add to queue: String 4.648, the result was true, size is 1, map : {4=4.648}

Producer5 try to add to queue: String 4.604, the result was true, size is 1, map : {4=4.604}

consumer4 consume 4.604, after consumption, mMap size: 0, map : {}

Producer4 try to add to queue: String 3.729, the result was true, size is 2, map : {2=2.428, 3=3.729}

Producer9 try to add to queue: String 2.428, the result was true, size is 2, map : {2=2.428, 3=3.729}

Producer10 try to add to queue: String 1.165, the result was true, size is 3, map : {1=1.165, 3=3.561, 4=4.987, 5=5.589}

Producer6 try to add to queue: String 3.561, the result was true, size is 2, map : {1=1.165, 3=3.561, 4=4.987, 5=5.589}

consumer2 consume 2.428, after consumption, mMap size: 2, map : {1=1.165, 3=3.561, 4=4.987, 5=5.589}

Producer1 try to add to queue: String 5.589, the result was true, size is 4, map : {1=1.165, 3=3.561, 4=4.987, 5=5.589}

Producer5 try to add to queue: String 2.309, the result was true, size is 5, map : {1=1.165, 2=2.309, 4=4.987, 5=5.589}

Producer8 try to add to queue: String 4.987, the result was true, size is 3, map : {1=1.165, 3=3.561, 4=4.987, 5=5.589}

consumer3 consume 3.561, after consumption, mMap size: 4, map : {1=1.165, 2=2.309, 4=4.987, 5=5.589}

Producer3 try to add to queue: String 4.36, the result was true, size is 3, map : {1=1.165, 2=2.309, 4=4.36}

Producer2 try to add to queue: String 3.455, the result was true, size is 4, map : {1=1.165, 2=2.309, 3=3.455, 4=4.36}

consumer5 consume 5.589, after consumption, mMap size: 3, map : {1=1.165, 2=2.309, 4=4.987}

Producer7 try to add to queue: String 4.221, the result was true, size is 4, map : {1=1.165, 2=2.309, 3=3.455, 4=4.221}

consumer4 consume 4.221, after consumption, mMap size: 3, map : {1=1.165, 2=2.309, 3=3.455}

Producer9 try to add to queue: String 4.261, the result was true, size is 4, map : {1=1.165, 2=2.309, 3=3.455, 4=4.261}

Producer4 try to add to queue: String 4.519, the result was true, size is 4, map : {1=1.165, 2=2.309, 3=3.455, 4=4.519}

Producer6 try to add to queue: String 3.187, the result was true, size is 4, map : {1=1.165, 2=2.309, 3=3.187, 4=4.519}

consumer2 consume 2.309, after consumption, mMap size: 2, map : {3=3.187, 4=4.519}

Producer7 try to add to queue: String 3.649, the result was true, size is 3, map : {3=3.649, 5=5.940}

consumer1 consume 1.165, after consumption, mMap size: 3, map : {2=2.309, 3=3.187, 4=4.519}

Producer10 try to add to queue: String 3.413, the result was true, size is 2, map : {3=3.413, 5=5.940}

Producer5 try to add to queue: String 1.541, the result was true, size is 3, map : {1=1.541, 3=3.413, 5=5.940}

consumer4 consume 4.519, after consumption, mMap size: 2, map : {3=3.649, 5=5.940}

Producer1 try to add to queue: String 3.546, the result was true, size is 3, map : {1=1.541, 3=3.546, 5=5.940}

Producer2 try to add to queue: String 1.506, the result was true, size is 3, map : {1=1.506, 3=3.546, 5=5.940}

consumer3 consume 3.546, after consumption, mMap size: 2, map : {1=1.506, 5=5.940}

Producer8 try to add to queue: String 5.940, the result was true, size is 3, map : {3=3.187, 4=4.519, 5=5.940}

Producer3 try to add to queue: String 3.886, the result was true, size is 3, map : {1=1.506, 3=3.886, 5=5.940}

consumer5 consume 5.940, after consumption, mMap size: 2, map : {1=1.506, 3=3.886}

Producer4 try to add to queue: String 5.851, the result was true, size is 4, map : {1=1.506, 3=3.886, 4=4.618, 5=5.851}

Producer9 try to add to queue: String 4.618, the result was true, size is 4, map : {1=1.506, 3=3.886, 4=4.618, 5=5.851}

executor.isTerminated() status = false
canceled non-finished tasks
Interruption Occured in consumer5
Interruption Occured in consumer1
Interruption Occured in consumer4
Interruption Occured in consumer10
Interruption Occured in consumer9
Interruption Occured in consumer3
Interruption Occured in consumer8
Interruption Occured in consumer2
Interruption Occured in consumer7
Interruption Occured in consumer6
Producer5 try to add to queue: String 4.923, the result was true, size is 4, map : {1=1.506, 3=3.886, 4=4.923, 5=5.851}

Producer2 try to add to queue: String 2.365, the result was true, size is 5, map : {1=1.506, 2=2.365, 3=3.886, 4=4.923, 5=5.266}

executor.isTerminated() status = false
Producer6 try to add to queue: String 5.266, the result was true, size is 4, map : {1=1.506, 2=2.365, 3=3.886, 4=4.923, 5=5.266}

canceled non-finished tasks
Producer7 try to add to queue: String 1.92, the result was true, size is 5, map : {1=1.92, 2=2.365, 3=3.886, 4=4.923, 5=5.266}

Producer1 try to add to queue: String 4.954, the result was true, size is 5, map : {1=1.92, 2=2.365, 3=3.886, 4=4.954, 5=5.266}

Producer10 try to add to queue: String 1.557, the result was true, size is 5, map : {1=1.557, 2=2.365, 3=3.886, 4=4.954, 5=5.266}

Interruption Occured in producer1
Interruption Occured in producer6
Interruption Occured in producer5
Interruption Occured in producer7
Interruption Occured in producer9
Interruption Occured in producer2
Interruption Occured in producer10
Interruption Occured in producer4
Interruption Occured in producer3
Interruption Occured in producer8
------------------------------------------------------------------------
BUILD SUCCESS
------------------------------------------------------------------------
Total time: 5.529s
Finished at: Wed Apr 26 00:04:33 PDT 2017
Final Memory: 8M/309M
------------------------------------------------------------------------
*/