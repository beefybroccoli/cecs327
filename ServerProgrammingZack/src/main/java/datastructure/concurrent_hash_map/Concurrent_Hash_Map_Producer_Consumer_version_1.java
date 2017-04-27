package datastructure.concurrent_hash_map;

import static VALUE.VALUE.echo;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Producer_Concurrent_Hash_Map_Version_1 implements Runnable {

    private ConcurrentHashMap<String, String> map;
    private boolean mFlag;

    public Producer_Concurrent_Hash_Map_Version_1(ConcurrentHashMap<String, String> queue) {
        this.map = queue;
        mFlag = true;

    }

    @Override
    public void run() {

        int input = 0;
        do {
            try {

                String key = ((++input % 2 == 0) ? "1" : "2");
                String value = key + "." + input;
                // We are adding elements using offer() in order to check if
                // it actually managed to insert them.
                map.put(key, value);
                System.out.println("Trying to add to queue: String " + map.get(key)
                        + " and the result was " + map.containsKey(key)
                        + "\n");
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                mFlag = false;
                echo("Interruption Occured in producer");
            }
        } while (mFlag);
    }
}

class Consumer_Concurrent_Hash_Map_Version_1 implements Runnable {

    private ConcurrentHashMap<String, String> mMap;
    private int mID;
    private boolean mFlag;

    public Consumer_Concurrent_Hash_Map_Version_1(ConcurrentHashMap<String, String> queue, int inputID) {
        this.mMap = queue;
        mID = inputID;
        mFlag = true;
    }

    @Override
    public void run() {

        // As long as there are empty positions in our array,
        // we want to check what's going on.
        while (mFlag) {
            try {
                if (mMap.size() > 0 && mMap.containsKey("" + mID)) {
                    String key = "" + mID;
                    System.out.println("consumer" + mID + " consume " + mMap.remove(key)
                            + ", after consumption, Queue size: " + mMap.size() + "\n");
                }
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                mFlag = false;
                echo("Interruption Occured in consumer" + mID);
            }
        }
    }
}

public class Concurrent_Hash_Map_Producer_Consumer_version_1 {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        ConcurrentHashMap<String, String> sharedMap = new ConcurrentHashMap<String, String>();

        // The two threads will access the same queue, in order
        // to test its blocking capabilities.
        Thread producer = new Thread(new Producer_Concurrent_Hash_Map_Version_1(sharedMap));
        Thread consumer1 = new Thread(new Consumer_Concurrent_Hash_Map_Version_1(sharedMap, 1));
        Thread consumer2 = new Thread(new Consumer_Concurrent_Hash_Map_Version_1(sharedMap, 2));

        try {
            executor.submit(producer);
            executor.submit(consumer1);
            executor.submit(consumer2);

            executor.awaitTermination(2, TimeUnit.SECONDS);

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
