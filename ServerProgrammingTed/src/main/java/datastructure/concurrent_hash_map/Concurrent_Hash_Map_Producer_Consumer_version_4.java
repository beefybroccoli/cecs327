package datastructure.concurrent_hash_map;

import static part_2_assignment_version_final.object.VALUE.echo;
import com.google.common.util.concurrent.Striped;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

class Producer_Concurrent_Hash_Map_Version_4 implements Runnable {

    private ConcurrentHashMap<String, String> mMap;
    private boolean mFlag;
    Striped<ReadWriteLock> mSharedRWLock;
    ReadWriteLock mRWLock;
    Lock mLock;
    int mNUMBER_OF_CONSUMER;

    public Producer_Concurrent_Hash_Map_Version_4(ConcurrentHashMap<String, String> queue, Striped<ReadWriteLock> inputRWLock, int inputNUMBER_OF_CONSUMER) {
        this.mMap = queue;
        mFlag = true;
        mSharedRWLock = inputRWLock;
        mNUMBER_OF_CONSUMER = inputNUMBER_OF_CONSUMER;

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

        do {
            try {
                if (mMap.size() < mNUMBER_OF_CONSUMER) {
                    String key = "" + part_2_assignment_version_final.object.VALUE.getRandomNumberBetween(mNUMBER_OF_CONSUMER, 1);
                    String value = key + "." + part_2_assignment_version_final.object.VALUE.getRandomNumberBetween(1000, 1);
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

class Consumer_Concurrent_Hash_Map_Version_4 implements Runnable {

    private ConcurrentHashMap<String, String> mMap;
    private int mID;
    private boolean mFlag;
    Striped<ReadWriteLock> mSharedRWLock;
    ReadWriteLock mRWLock;
    Lock mLock;

    public Consumer_Concurrent_Hash_Map_Version_4(ConcurrentHashMap<String, String> queue, int inputID, Striped<ReadWriteLock> inputRWLock) {
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

                if (mMap.size() > 0 && mMap.containsKey(key)) {
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

public class Concurrent_Hash_Map_Producer_Consumer_version_4 {

    public static void main(String[] args) {

        run_with_consumers(100);

    }//end main method

    public static void run_with_consumers(int NUMBER_OF_CONSUMER) {
        int time_in_seconds = NUMBER_OF_CONSUMER * 2;
        int NUMBER_OF_THREADS = NUMBER_OF_CONSUMER;
        Striped<ReadWriteLock> rwLockStripes = Striped.readWriteLock(NUMBER_OF_CONSUMER);
        ExecutorService executorProducer = Executors.newFixedThreadPool(1);
        ExecutorService executorConsumer = Executors.newFixedThreadPool(NUMBER_OF_CONSUMER);

        ConcurrentHashMap<String, String> sharedMap = new ConcurrentHashMap<>();

        // The two threads will access the same queue, in order
        // to test its blocking capabilities.
        Thread producer = new Thread(new Producer_Concurrent_Hash_Map_Version_4(sharedMap, rwLockStripes, NUMBER_OF_CONSUMER));

        executorProducer.submit(producer);

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            int id = i + 1;
            executorConsumer.submit(new Thread(new Consumer_Concurrent_Hash_Map_Version_4(sharedMap, id, rwLockStripes)));
        }

        
        /*
        the producer will shut down after all the consumers die
        */
        time_in_seconds = 10;
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
Trying to add to queue: String 55.257, the result was true, size is 1, map : {55=55.257}

consumer55 consume 55.257, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 45.80, the result was true, size is 1, map : {45=45.80}

consumer45 consume 45.80, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 80.961, the result was true, size is 1, map : {80=80.961}

consumer80 consume 80.961, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 72.175, the result was true, size is 1, map : {72=72.175}

consumer72 consume 72.175, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 42.627, the result was true, size is 1, map : {42=42.627}

consumer42 consume 42.627, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 18.366, the result was true, size is 1, map : {18=18.366}

consumer18 consume 18.366, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 18.925, the result was true, size is 1, map : {18=18.925}

consumer18 consume 18.925, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 8.904, the result was true, size is 1, map : {8=8.904}

consumer8 consume 8.904, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 72.703, the result was true, size is 1, map : {72=72.703}

consumer72 consume 72.703, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 9.797, the result was true, size is 1, map : {9=9.797}

consumer9 consume 9.797, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 92.371, the result was true, size is 1, map : {92=92.371}

consumer92 consume 92.371, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 65.446, the result was true, size is 1, map : {65=65.446}

consumer65 consume 65.446, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 21.456, the result was true, size is 1, map : {21=21.456}

consumer21 consume 21.456, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 64.306, the result was true, size is 1, map : {64=64.306}

consumer64 consume 64.306, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 20.487, the result was true, size is 1, map : {20=20.487}

consumer20 consume 20.487, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 51.894, the result was true, size is 1, map : {51=51.894}

consumer51 consume 51.894, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 94.282, the result was true, size is 1, map : {94=94.282}

consumer94 consume 94.282, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 72.60, the result was true, size is 1, map : {72=72.60}

consumer72 consume 72.60, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 97.365, the result was true, size is 1, map : {97=97.365}

consumer97 consume 97.365, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 87.112, the result was true, size is 1, map : {87=87.112}

consumer87 consume 87.112, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 45.178, the result was true, size is 1, map : {45=45.178}

consumer45 consume 45.178, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 59.755, the result was true, size is 1, map : {59=59.755}

consumer59 consume 59.755, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 89.169, the result was true, size is 1, map : {89=89.169}

consumer89 consume 89.169, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 84.891, the result was true, size is 1, map : {84=84.891}

consumer84 consume 84.891, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 22.961, the result was true, size is 1, map : {22=22.961}

consumer22 consume 22.961, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 46.280, the result was true, size is 1, map : {46=46.280}

consumer46 consume 46.280, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 48.683, the result was true, size is 1, map : {48=48.683}

consumer48 consume 48.683, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 75.974, the result was true, size is 1, map : {75=75.974}

consumer75 consume 75.974, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 59.857, the result was true, size is 1, map : {59=59.857}

consumer59 consume 59.857, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 90.332, the result was true, size is 1, map : {90=90.332}

consumer90 consume 90.332, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 81.100, the result was true, size is 1, map : {81=81.100}

consumer81 consume 81.100, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 29.403, the result was true, size is 1, map : {29=29.403}

consumer29 consume 29.403, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 12.142, the result was true, size is 1, map : {12=12.142}

consumer12 consume 12.142, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 83.983, the result was true, size is 1, map : {83=83.983}

consumer83 consume 83.983, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 38.287, the result was true, size is 1, map : {38=38.287}

consumer38 consume 38.287, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 54.399, the result was true, size is 1, map : {54=54.399}

consumer54 consume 54.399, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 20.578, the result was true, size is 1, map : {20=20.578}

consumer20 consume 20.578, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 39.50, the result was true, size is 1, map : {39=39.50}

consumer39 consume 39.50, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 5.458, the result was true, size is 1, map : {5=5.458}

consumer5 consume 5.458, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 22.315, the result was true, size is 1, map : {22=22.315}

consumer22 consume 22.315, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 22.673, the result was true, size is 1, map : {22=22.673}

consumer22 consume 22.673, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 53.140, the result was true, size is 1, map : {53=53.140}

consumer53 consume 53.140, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 11.565, the result was true, size is 1, map : {11=11.565}

consumer11 consume 11.565, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 56.305, the result was true, size is 1, map : {56=56.305}

consumer56 consume 56.305, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 89.417, the result was true, size is 1, map : {89=89.417}

consumer89 consume 89.417, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 87.891, the result was true, size is 1, map : {87=87.891}

Trying to add to queue: String 79.443, the result was true, size is 1, map : {79=79.443}

consumer87 consume 87.891, after consumption, mMap size: 1, map : {79=79.443}

consumer79 consume 79.443, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 34.139, the result was true, size is 1, map : {34=34.139}

consumer34 consume 34.139, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 36.924, the result was true, size is 1, map : {36=36.924}

consumer36 consume 36.924, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 42.163, the result was true, size is 1, map : {42=42.163}

Trying to add to queue: String 37.994, the result was true, size is 2, map : {37=37.994, 42=42.163}

consumer37 consume 37.994, after consumption, mMap size: 1, map : {42=42.163}

consumer42 consume 42.163, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 46.985, the result was true, size is 1, map : {46=46.985}

consumer46 consume 46.985, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 39.996, the result was true, size is 1, map : {39=39.996}

consumer39 consume 39.996, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 74.179, the result was true, size is 1, map : {74=74.179}

consumer74 consume 74.179, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 57.503, the result was true, size is 1, map : {57=57.503}

consumer57 consume 57.503, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 17.732, the result was true, size is 1, map : {17=17.732}

consumer17 consume 17.732, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 99.35, the result was true, size is 1, map : {99=99.35}

consumer99 consume 99.35, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 19.924, the result was true, size is 1, map : {19=19.924}

Trying to add to queue: String 53.153, the result was true, size is 2, map : {19=19.924, 53=53.153}

consumer19 consume 19.924, after consumption, mMap size: 1, map : {53=53.153}

consumer53 consume 53.153, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 3.808, the result was true, size is 1, map : {3=3.808}

consumer3 consume 3.808, after consumption, mMap size: 1, map : {40=40.560}

Trying to add to queue: String 40.560, the result was true, size is 1, map : {40=40.560}

consumer40 consume 40.560, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 37.132, the result was true, size is 1, map : {37=37.132}

consumer37 consume 37.132, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 35.111, the result was true, size is 1, map : {35=35.111}

consumer35 consume 35.111, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 70.689, the result was true, size is 1, map : {70=70.689}

consumer70 consume 70.689, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 70.255, the result was true, size is 1, map : {70=70.255}

consumer70 consume 70.255, after consumption, mMap size: 0, map : {54=54.348}

Trying to add to queue: String 54.348, the result was true, size is 1, map : {54=54.348}

consumer54 consume 54.348, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 65.291, the result was true, size is 1, map : {65=65.291}

consumer65 consume 65.291, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 45.534, the result was true, size is 1, map : {45=45.534}

Trying to add to queue: String 86.402, the result was true, size is 1, map : {86=86.402}

consumer45 consume 45.534, after consumption, mMap size: 1, map : {86=86.402}

consumer86 consume 86.402, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 91.226, the result was true, size is 1, map : {91=91.226}

consumer91 consume 91.226, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 33.988, the result was true, size is 1, map : {33=33.988}

consumer33 consume 33.988, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 16.188, the result was true, size is 1, map : {16=16.188}

consumer16 consume 16.188, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 16.418, the result was true, size is 1, map : {16=16.418}

consumer16 consume 16.418, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 95.58, the result was true, size is 1, map : {95=95.58}

consumer95 consume 95.58, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 2.100, the result was true, size is 1, map : {2=2.100}

consumer2 consume 2.100, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 77.543, the result was true, size is 1, map : {77=77.543}

consumer77 consume 77.543, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 46.813, the result was true, size is 1, map : {46=46.813}

consumer46 consume 46.813, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 40.734, the result was true, size is 1, map : {40=40.734}

Trying to add to queue: String 86.348, the result was true, size is 2, map : {86=86.348}

consumer40 consume 40.734, after consumption, mMap size: 1, map : {86=86.348}

Trying to add to queue: String 63.13, the result was true, size is 1, map : {63=63.13}

consumer86 consume 86.348, after consumption, mMap size: 0, map : {63=63.13}

consumer63 consume 63.13, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 60.243, the result was true, size is 1, map : {60=60.243}

consumer60 consume 60.243, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 80.885, the result was true, size is 1, map : {80=80.885}

consumer80 consume 80.885, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 31.46, the result was true, size is 1, map : {31=31.46}

consumer31 consume 31.46, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 34.624, the result was true, size is 1, map : {34=34.624}

consumer34 consume 34.624, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 16.879, the result was true, size is 1, map : {16=16.879}

consumer16 consume 16.879, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 64.302, the result was true, size is 1, map : {64=64.302}

consumer64 consume 64.302, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 24.247, the result was true, size is 1, map : {24=24.247}

consumer24 consume 24.247, after consumption, mMap size: 0, map : {31=31.452}

Trying to add to queue: String 31.452, the result was true, size is 1, map : {31=31.452}

consumer31 consume 31.452, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 11.544, the result was true, size is 1, map : {11=11.544}

consumer11 consume 11.544, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 35.853, the result was true, size is 1, map : {35=35.853}

consumer35 consume 35.853, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 95.801, the result was true, size is 1, map : {95=95.801}

consumer95 consume 95.801, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 9.879, the result was true, size is 1, map : {9=9.879}

consumer9 consume 9.879, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 23.765, the result was true, size is 1, map : {23=23.765}

consumer23 consume 23.765, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 8.559, the result was true, size is 1, map : {8=8.559}

consumer8 consume 8.559, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 25.873, the result was true, size is 1, map : {25=25.873}

consumer25 consume 25.873, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 17.772, the result was true, size is 1, map : {17=17.772}

consumer17 consume 17.772, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 98.957, the result was true, size is 1, map : {98=98.957}

consumer98 consume 98.957, after consumption, mMap size: 0, map : {}

Trying to add to queue: String 67.92, the result was true, size is 1, map : {67=67.92}

executor.isTerminated() status = false
canceled non-finished tasks
Interruption Occured in consumer91
Interruption Occured in consumer93
Interruption Occured in consumer33
Interruption Occured in consumer81
Interruption Occured in consumer100
Interruption Occured in consumer8
Interruption Occured in consumer72
Interruption Occured in consumer82
Interruption Occured in consumer34
Interruption Occured in consumer18
Interruption Occured in consumer68
Interruption Occured in consumer27
Interruption Occured in consumer22
Interruption Occured in consumer54
Interruption Occured in consumer57
Interruption Occured in consumer5
Interruption Occured in consumer47
Interruption Occured in consumer83
Interruption Occured in consumer20
Interruption Occured in consumer95
Interruption Occured in consumer41
Interruption Occured in consumer11
Interruption Occured in consumer28
Interruption Occured in consumer92
Interruption Occured in consumer21
Interruption Occured in consumer50
Interruption Occured in consumer31
Interruption Occured in consumer10
Interruption Occured in consumer56
Interruption Occured in consumer36
Interruption Occured in consumer39
Interruption Occured in consumer29
Interruption Occured in consumer70
Interruption Occured in consumer3
Interruption Occured in consumer30
Interruption Occured in consumer52
Interruption Occured in consumer94
Interruption Occured in consumer45
Interruption Occured in consumer77
Interruption Occured in consumer62
Interruption Occured in consumer80
Interruption Occured in consumer90
Interruption Occured in consumer43
Interruption Occured in consumer4
Interruption Occured in consumer64
Interruption Occured in consumer74
Interruption Occured in consumer6
Interruption Occured in consumer73
Interruption Occured in consumer87
Interruption Occured in consumer23
Interruption Occured in consumer96
Interruption Occured in consumer98
Interruption Occured in consumer7
Interruption Occured in consumer19
Interruption Occured in consumer38
Interruption Occured in consumer46
Interruption Occured in consumer65executor.isTerminated() status = false
canceled non-finished tasks

Interruption Occured in consumer97
Interruption Occured in consumer35
Interruption Occured in consumer61
Interruption Occured in consumer58
Interruption Occured in consumer88
Interruption Occured in consumer60
Interruption Occured in consumer12
Interruption Occured in consumer26
Interruption Occured in consumer1
Interruption Occured in consumer16
Interruption Occured in consumer32
Interruption Occured in consumer66
Interruption Occured in consumer99
Interruption Occured in consumer86
Interruption Occured in consumer2
Interruption Occured in consumer67
Interruption Occured in consumer14
Interruption Occured in consumer49
Interruption Occured in consumer84
Interruption Occured in consumer76
Interruption Occured in consumer85
Interruption Occured in consumer9
Interruption Occured in consumer75
Interruption Occured in consumer25
Interruption Occured in consumer63
Interruption Occured in consumer24
Interruption Occured in consumer59
Interruption Occured in consumer17
Interruption Occured in consumer55
Interruption Occured in consumer51
Interruption Occured in consumer13
Interruption Occured in consumer15
Interruption Occured in consumer53
Interruption Occured in consumer89
Interruption Occured in consumer48
Interruption Occured in consumer44
Interruption Occured in consumer40
Interruption Occured in consumer78
Interruption Occured in consumer37
Interruption Occured in consumer71
Interruption Occured in consumer79
Interruption Occured in consumer69
Interruption Occured in producer
Interruption Occured in consumer42
------------------------------------------------------------------------
BUILD SUCCESS
------------------------------------------------------------------------
Total time: 10.521s
Finished at: Tue Apr 25 23:26:35 PDT 2017
Final Memory: 7M/245M
------------------------------------------------------------------------
*/