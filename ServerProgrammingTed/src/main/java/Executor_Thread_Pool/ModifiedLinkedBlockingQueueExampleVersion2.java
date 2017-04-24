package Executor_Thread_Pool;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class ProducerVersion2 implements Runnable {

    private LinkedBlockingQueue queue;
    private boolean mFlag;
    private ReentrantLock mLock;
    private int mCounter;

    public ProducerVersion2(LinkedBlockingQueue queue, ReentrantLock lock) {
        this.queue = queue;
        mFlag = true;
        mLock = lock;
        mCounter = 50;

    }//enc onstructor

    // We need to check if the producer thread is
    // Still running, and this method will return
    // the state (running/stopped).
    public boolean getFlag() {
        return mFlag;
    }

    public String getResult() {
        String result = "";
        Random rn = new Random();
        int rand = rn.nextInt(2) + 1;

        if (rand == 1) {
            result = "1";
        } else {
            result = "2";
        }
        --mCounter;
        return result;
    }

    public void run() {
        System.out.println("(Producer thread started)");

        try {

            do {

                try {
                    // We are adding elements using put() which waits
                    // until it can actually insert elements if there is
                    // not space in the queue

                    if (mCounter > 0) {
                        String element = getResult();
                        queue.put(element);
                        TimeUnit.MILLISECONDS.sleep(50);
                    }

//                    System.out.println("Produce add element: " + element);
                } catch (InterruptedException ex) {
                    System.out.println("Producer InterruptedException occured");
                }

            } while (mCounter > 0);

        } finally {
            mFlag = false;
            System.out.println("(Producer thread ended, mCounter = " + mCounter + ")");
        }

    }//end run() method

}//end class ProducerVersion3

class ConsumerVersion2 implements Runnable {

    private LinkedBlockingQueue queue;
    private ProducerVersion2 producer;
    private Integer mID;
    private ReentrantLock mLock;
    private boolean mFlag;
    private String mName;

    public ConsumerVersion2(Integer id, LinkedBlockingQueue queue, ProducerVersion2 producer, ReentrantLock lock, String consumerName) {
        this.queue = queue;
        this.producer = producer;
        mID = id;
        mLock = lock;
        mFlag = true;
        mName = consumerName;
    }

    @Override
    public void run() {

        try {
            System.out.println("(" + mName + " thread started)");

            // As long as the producer is mFlag,
            // we remove elements from the queue.
            while (mFlag) {

                try {
                    if (queue.size() > 0) {
                        if (queue.peek().equals("" + mID)) {
                            System.out.println(mName + " consume " + queue.take() + ", after consumption size is " + queue.size());
                        }
                    }
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    mFlag = false;
                    System.out.println(mName + " InterruptedException occured");
                } finally {

                }

            }//end while
        } finally {
            System.out.println("(" + mName + " thread ended)");
        }

    }//end run

}//end class ConsumerVersion3


/*
 *modify this to have one producer, multiple clietns taking different objects from the queue 
 */
public class ModifiedLinkedBlockingQueueExampleVersion2 {

    public static void main(String[] args) {

        test();
    }

    public static void test() {

        //Creates a LinkedBlockingQueue with a capacity of Integer.MAX_VALUE.
        LinkedBlockingQueue queue = new LinkedBlockingQueue();
        ReentrantLock lock = new ReentrantLock();

        ProducerVersion2 producer = new ProducerVersion2(queue, lock);
        ConsumerVersion2 oddConsumerObject = new ConsumerVersion2(1, queue, producer, lock, "Odd Consumer");
        ConsumerVersion2 evenConsumerObject = new ConsumerVersion2(2, queue, producer, lock, "Even Consumer");

        Thread producerThread = new Thread(producer);
        Thread oddConsumerThread = new Thread(oddConsumerObject);
        Thread evenConsumerThread = new Thread(evenConsumerObject);

        ExecutorService executor = Executors.newFixedThreadPool(3);


        /*
        An ExecutorService provides two methods for that purpose: 
            _shutdown() waits for currently running tasks to finish 
            _shutdownNow() interrupts all running tasks and shut the executor down immediately.
         */
        try {
            executor.submit(producerThread);
            executor.submit(oddConsumerThread);
            executor.submit(evenConsumerThread);
            executor.awaitTermination(5, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("canceled non-finished tasks");
            }
//            executor.shutdown();
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }

    }

}

/*
------------------------------------------------------------------------
Building ServerProgrammingTed 1.0-SNAPSHOT
------------------------------------------------------------------------

--- exec-maven-plugin:1.2.1:exec (default-cli) @ ServerProgrammingTed ---
(Producer thread started)
(Even Consumer thread started)
(Odd Consumer thread started)
Odd Consumer consume 1, after consumption size is 2
Even Consumer consume 2, after consumption size is 2
Even Consumer consume 2, after consumption size is 3
Odd Consumer consume 1, after consumption size is 4
Odd Consumer consume 1, after consumption size is 5
Odd Consumer consume 1, after consumption size is 6
Even Consumer consume 2, after consumption size is 7
Even Consumer consume 2, after consumption size is 8
Even Consumer consume 2, after consumption size is 9
Even Consumer consume 2, after consumption size is 10
Odd Consumer consume 1, after consumption size is 11
Odd Consumer consume 1, after consumption size is 12
Even Consumer consume 2, after consumption size is 13
Even Consumer consume 2, after consumption size is 14
Odd Consumer consume 1, after consumption size is 13
Even Consumer consume 2, after consumption size is 14
Odd Consumer consume 1, after consumption size is 13
Even Consumer consume 2, after consumption size is 14
Even Consumer consume 2, after consumption size is 14
Even Consumer consume 2, after consumption size is 15
Odd Consumer consume 1, after consumption size is 15
Odd Consumer consume 1, after consumption size is 16
Even Consumer consume 2, after consumption size is 16
Odd Consumer consume 1, after consumption size is 16
Even Consumer consume 2, after consumption size is 16
Even Consumer consume 2, after consumption size is 17
Even Consumer consume 2, after consumption size is 18
Odd Consumer consume 1, after consumption size is 17
Odd Consumer consume 1, after consumption size is 18
Even Consumer consume 2, after consumption size is 19
Odd Consumer consume 1, after consumption size is 18
(Producer thread ended, mCounter = 0)
Even Consumer consume 2, after consumption size is 18
Even Consumer consume 2, after consumption size is 17
Even Consumer consume 2, after consumption size is 16
Even Consumer consume 2, after consumption size is 15
Odd Consumer consume 1, after consumption size is 14
Odd Consumer consume 1, after consumption size is 13
Odd Consumer consume 1, after consumption size is 12
Odd Consumer consume 1, after consumption size is 11
Even Consumer consume 2, after consumption size is 10
Odd Consumer consume 1, after consumption size is 9
Even Consumer consume 2, after consumption size is 8
Odd Consumer consume 1, after consumption size is 7
Even Consumer consume 2, after consumption size is 6
Even Consumer consume 2, after consumption size is 5
Even Consumer consume 2, after consumption size is 4
Odd Consumer consume 1, after consumption size is 3
Odd Consumer consume 1, after consumption size is 2
Even Consumer consume 2, after consumption size is 1
Odd Consumer consume 1, after consumption size is 0
canceled non-finished tasks
shutdown finished
Even Consumer InterruptedException occured
Odd Consumer InterruptedException occured
(Odd Consumer thread ended)
(Even Consumer thread ended)
------------------------------------------------------------------------
BUILD SUCCESS
------------------------------------------------------------------------
Total time: 5.483s
Finished at: Sun Apr 23 19:44:59 PDT 2017
Final Memory: 7M/245M
------------------------------------------------------------------------
*/

/*
https://examples.javacodegeeks.com/core-java/util/concurrent/linkedblockingqueue/java-util-concurrent-linkedblockingqueue-example/
 */

 /*
1. ArrayBlockingQueue vs LinkedBlockingQueue

In a previous article (java.util.concurrent.ArrayBlockingQueue Example), 
    we talked about ArrayBlockingQueue and its usage. 
    Here, we will try to make some comparisons between ArrayBlockingQueue 
    and LinkedBlockingQueue to make clear in which cases we should prefer each one.
    It is important to make clear distinctions, as both data structures serve very similar needs, 
    but performance and implementation varies.

1.1 Performance

ArrayBlockingQueue: It uses an internal array in which the elements are kept, 
    and the Queue interface imposes certain rules (like the FIFO rule, which is essential to any queue). 
    Because it uses an array, it has a fixed size which is given in the constructor.

LinkedBlocking Queue: It uses nodes (like a linked list), to keep track of the order of the elements, 
    which increases the complexity of the data structure. 
    It can have a fixed-size limit as well, but if we don’t define one the limit is Integer.MAX_VALUE by default.

According to the previous information, you can clearly see why ArrayBlockingQueue is faster than LinkedBlockingQueue,
    which is backed by a benchmark that was published in an older JavaCodeGeeks article. 
    The benchmark specifics and results can be found here. 
    In every case, the performance of ArrayBlockingQueue is better.

1.2 Implementation in synchronization
    The major implementation difference between the two data structures (synchronization-wise) 
    is that because ArrayBlockingQueue keeps the elements in an array it needs only one lock 
    to keep everything synchronized. 
    On the other hand, LinkedBlockingQueue uses two locks, 
    one for insertion and one for extraction. 
    That happens because while ArrayBlockingQueue contains just an array, 
    LinkedBlockingQueue contains a series of connected nodes, so it doesn’t need to keep track 
    of insertion and extraction at the same time.
 */
