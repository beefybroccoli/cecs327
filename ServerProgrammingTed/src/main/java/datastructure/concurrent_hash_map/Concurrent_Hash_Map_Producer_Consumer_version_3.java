package datastructure.concurrent_hash_map;

import static VALUE.VALUE.echo;
import com.google.common.util.concurrent.Striped;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

class Producer_Concurrent_Hash_Map_Version_3 implements Runnable {

    private ConcurrentHashMap<String, String> mMap;
    private boolean mFlag;
    Striped<ReadWriteLock> mSharedRWLock;
    ReadWriteLock mRWLock;
    Lock mLock;

    public Producer_Concurrent_Hash_Map_Version_3(ConcurrentHashMap<String, String> queue, Striped<ReadWriteLock> inputRWLock) {
        this.mMap = queue;
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

        int input = 0;
        do {
            if (mMap.size() < 2) {
                String key = ((++input % 2 == 0) ? "1" : "2");
//                key = "1";
                String value = key + "." + input;
                mRWLock = mSharedRWLock.get(key);
                mLock = mRWLock.writeLock();
                try {
                    mLock.lock();
                    
                    
                    // We are adding elements using offer() in order to check if
                    // it actually managed to insert them.
                    mMap.put(key, value);
                    System.out.println(
                            mLock.toString() + ", "
                            + "Trying to add to queue: String " + mMap.get(key)
                            + ", the result was " + mMap.containsKey(key)
                            + ", size is " + mMap.size()
                            + ", map : " + mMap.toString()
                            + "\n");
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    mFlag = false;
                    echo("Interruption Occured in producer");
                } finally {
                    mLock.unlock();
                }
            }
        } while (mFlag);
    }
}

class Consumer_Concurrent_Hash_Map_Version_3 implements Runnable {

    private ConcurrentHashMap<String, String> mMap;
    private int mID;
    private boolean mFlag;
    Striped<ReadWriteLock> mSharedRWLock;
    ReadWriteLock mRWLock;
    Lock mLock;

    public Consumer_Concurrent_Hash_Map_Version_3(ConcurrentHashMap<String, String> queue, int inputID, Striped<ReadWriteLock> inputRWLock) {
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
            int keyIndex = mID - 1;
            mRWLock = mSharedRWLock.getAt(keyIndex);
            mLock = mRWLock.writeLock();
            try {
                mLock.lock();
                if (mMap.size() > 0 && mMap.containsKey(key)) {
//                if (mMap.containsKey(key)) {
                    System.out.println(
                            mLock.toString() + ", "
                            + "consumer" + mID + " consume " + mMap.remove(key)
                            + ", after consumption, mMap size: " + mMap.size()
                            + ", map : " + mMap.toString()
                            + "\n");
                }
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                mFlag = false;
                echo("Interruption Occured in consumer" + mID);
            } finally {
                mLock.unlock();
            }
        }
    }
}

public class Concurrent_Hash_Map_Producer_Consumer_version_3 {

    public static void main(String[] args) {

        Striped<ReadWriteLock> rwLockStripes = Striped.readWriteLock(10);
        ExecutorService executor = Executors.newFixedThreadPool(3);

        ConcurrentHashMap<String, String> sharedMap = new ConcurrentHashMap<>();

        // The two threads will access the same queue, in order
        // to test its blocking capabilities.
        Thread producer = new Thread(new Producer_Concurrent_Hash_Map_Version_3(sharedMap, rwLockStripes));
        Thread consumer1 = new Thread(new Consumer_Concurrent_Hash_Map_Version_3(sharedMap, 1, rwLockStripes));
        Thread consumer2 = new Thread(new Consumer_Concurrent_Hash_Map_Version_3(sharedMap, 2, rwLockStripes));
        try {
            executor.submit(producer);
            executor.submit(consumer1);
            executor.submit(consumer2);

            executor.awaitTermination(10, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            System.err.println("tasks interrupted  in runtime line 164");
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
java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@6fea821f[Locked by thread pool-1-thread-1], Trying to add to queue: String 2.1, the result was true, size is 1, map : {2=2.1}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@612d7192[Locked by thread pool-1-thread-1], Trying to add to queue: String 1.2, the result was true, size is 2, map : {1=1.2, 2=2.1}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@6fea821f[Locked by thread pool-1-thread-3], consumer2 consume 2.1, after consumption, mMap size: 1, map : {1=1.2}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@20d3c69b[Locked by thread pool-1-thread-2], consumer1 consume 1.2, after consumption, mMap size: 0, map : {}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@6fea821f[Locked by thread pool-1-thread-1], Trying to add to queue: String 2.3, the result was true, size is 1, map : {2=2.3}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@612d7192[Locked by thread pool-1-thread-1], Trying to add to queue: String 1.4, the result was true, size is 2, map : {1=1.4, 2=2.3}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@6fea821f[Locked by thread pool-1-thread-3], consumer2 consume 2.3, after consumption, mMap size: 1, map : {1=1.4}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@20d3c69b[Locked by thread pool-1-thread-2], consumer1 consume 1.4, after consumption, mMap size: 0, map : {}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@6fea821f[Locked by thread pool-1-thread-1], Trying to add to queue: String 2.5, the result was true, size is 1, map : {2=2.5}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@612d7192[Locked by thread pool-1-thread-1], Trying to add to queue: String 1.6, the result was true, size is 2, map : {1=1.6, 2=2.5}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@6fea821f[Locked by thread pool-1-thread-3], consumer2 consume 2.5, after consumption, mMap size: 1, map : {1=1.6}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@20d3c69b[Locked by thread pool-1-thread-2], consumer1 consume 1.6, after consumption, mMap size: 0, map : {}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@6fea821f[Locked by thread pool-1-thread-1], Trying to add to queue: String 2.7, the result was true, size is 1, map : {2=2.7}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@612d7192[Locked by thread pool-1-thread-1], Trying to add to queue: String 1.8, the result was true, size is 2, map : {1=1.8, 2=2.7}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@6fea821f[Locked by thread pool-1-thread-3], consumer2 consume 2.7, after consumption, mMap size: 1, map : {1=1.8}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@20d3c69b[Locked by thread pool-1-thread-2], consumer1 consume 1.8, after consumption, mMap size: 0, map : {}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@6fea821f[Locked by thread pool-1-thread-1], Trying to add to queue: String 2.9, the result was true, size is 1, map : {2=2.9}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@612d7192[Locked by thread pool-1-thread-1], Trying to add to queue: String 1.10, the result was true, size is 2, map : {1=1.10, 2=2.9}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@6fea821f[Locked by thread pool-1-thread-3], consumer2 consume 2.9, after consumption, mMap size: 1, map : {1=1.10}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@20d3c69b[Locked by thread pool-1-thread-2], consumer1 consume 1.10, after consumption, mMap size: 0, map : {}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@6fea821f[Locked by thread pool-1-thread-1], Trying to add to queue: String 2.11, the result was true, size is 1, map : {2=2.11}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@6fea821f[Locked by thread pool-1-thread-3], consumer2 consume 2.11, after consumption, mMap size: 1, map : {1=1.12}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@612d7192[Locked by thread pool-1-thread-1], Trying to add to queue: String 1.12, the result was true, size is 1, map : {1=1.12}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@20d3c69b[Locked by thread pool-1-thread-2], consumer1 consume 1.12, after consumption, mMap size: 0, map : {}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@6fea821f[Locked by thread pool-1-thread-1], Trying to add to queue: String 2.13, the result was true, size is 1, map : {2=2.13}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@612d7192[Locked by thread pool-1-thread-1], Trying to add to queue: String 1.14, the result was true, size is 2, map : {1=1.14}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@6fea821f[Locked by thread pool-1-thread-3], consumer2 consume 2.13, after consumption, mMap size: 1, map : {1=1.14}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@20d3c69b[Locked by thread pool-1-thread-2], consumer1 consume 1.14, after consumption, mMap size: 0, map : {}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@6fea821f[Locked by thread pool-1-thread-1], Trying to add to queue: String 2.15, the result was true, size is 1, map : {2=2.15}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@612d7192[Locked by thread pool-1-thread-1], Trying to add to queue: String 1.16, the result was true, size is 2, map : {1=1.16, 2=2.15}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@6fea821f[Locked by thread pool-1-thread-3], consumer2 consume 2.15, after consumption, mMap size: 1, map : {1=1.16}

java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@20d3c69b[Locked by thread pool-1-thread-2], consumer1 consume 1.16, after consumption, mMap size: 0, map : {}

executor.isTerminated() status = false
canceled non-finished tasks
Interruption Occured in consumer1
Interruption Occured in consumer2
java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock@6fea821f[Locked by thread pool-1-thread-1], Trying to add to queue: String 2.17, the result was true, size is 1, map : {2=2.17}

Interruption Occured in producer
------------------------------------------------------------------------
BUILD SUCCESS
------------------------------------------------------------------------
Total time: 10.538s
Finished at: Tue Apr 25 21:54:57 PDT 2017
Final Memory: 7M/245M
------------------------------------------------------------------------

*/