package datastructure.modified_linked_blocking_queue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class ProducerVersion1 implements Runnable {

    private LinkedBlockingQueue queue;
    private boolean mFlag;
    private ReentrantLock mLock;
    private int mIndex;

    public ProducerVersion1(LinkedBlockingQueue queue, ReentrantLock lock) {
        this.queue = queue;
        mFlag = true;
        mLock = lock;
        mIndex = 30;

    }//enc onstructor

    // We need to check if the producer thread is
    // Still running, and this method will return
    // the state (running/stopped).
    public boolean getFlag() {
        return mFlag;
    }//end ismFlag

    @Override
    public void run() {
        System.out.println("(Producer thread started)");

        try {

            do {

                try {
                    // We are adding elements using put() which waits
                    // until it can actually insert elements if there is
                    // not space in the queue

                    String element = "" + (--mIndex);
                    queue.put(element);
                    System.out.println("Produce add element: " + element);

                } catch (InterruptedException ex) {
                    System.out.println("Producer InterruptedException occured line 51");
                } finally {
                }

            } while (mIndex > 0);
        } finally {
//            mLock.unlock();
            mFlag = false;
            System.out.println("(Producer thread ended)");
        }

    }//end run() method

}//end class ProducerVersion2

class ConsumerVersion1 implements Runnable {

    private LinkedBlockingQueue queue;
    private ProducerVersion1 producer;
    private Integer mID;
    private ReentrantLock mLock;
    private boolean mFlag;

    public ConsumerVersion1(Integer id, LinkedBlockingQueue queue, ProducerVersion1 producer, ReentrantLock lock) {
        this.queue = queue;
        this.producer = producer;
        mID = id;
        mLock = lock;
        mFlag = true;
    }

    @Override
    public void run() {

        try {
            System.out.println("(Consumer" + mID + "  thread started)");

            // As long as the producer is mFlag,
            // we remove elements from the queue.
            while (producer.getFlag()) {

                if (queue.size() > 0) {

                    try {
                        System.out.println("Consumer " + mID + " removing element: " + queue.take());
                    } catch (InterruptedException e) {
                        System.out.println("Consumer " + mID + " InterruptedException occured line 106");
                    }
                }

            }//end while
        } finally {
            System.out.println("(Consumer " + mID + "  thread ended)");
        }

    }//end run

}//end class ConsumerVersion2


/*
 *modify this to have one producer, multiple clietns taking different objects from the queue 
 */
public class ModifiedLinkedBlockingQueueExampleVersion1 {

    public static void main(String[] args) {

        test();
    }

    public static void test() {

        //Creates a LinkedBlockingQueue with a capacity of Integer.MAX_VALUE.
        LinkedBlockingQueue queue = new LinkedBlockingQueue();
        ReentrantLock lock = new ReentrantLock();

        ProducerVersion1 producer = new ProducerVersion1(queue, lock);
        ConsumerVersion1 consumer1 = new ConsumerVersion1(1, queue, producer, lock);
        ConsumerVersion1 consumer2 = new ConsumerVersion1(2, queue, producer, lock);

        Thread producerThread = new Thread(producer);
        Thread consumerThread1 = new Thread(consumer1);
        Thread consumerThread2 = new Thread(consumer2);

        ExecutorService executor = Executors.newFixedThreadPool(3);


        /*
        An ExecutorService provides two methods for that purpose: 
            _shutdown() waits for currently running tasks to finish 
            _shutdownNow() interrupts all running tasks and shut the executor down immediately.
         */
        try {
            executor.submit(producerThread);
            executor.submit(consumerThread1);
            executor.submit(consumerThread2);
            executor.awaitTermination(3, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
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
(Consumer1  thread started)
Produce add element: 29
Produce add element: 28
(Consumer2  thread started)
Produce add element: 27
Consumer 1 removing element: 29
Produce add element: 26
Consumer 2 removing element: 28
Produce add element: 25
Consumer 1 removing element: 27
Produce add element: 24
Consumer 2 removing element: 26
Produce add element: 23
Consumer 1 removing element: 25
Produce add element: 22
Consumer 2 removing element: 24
Produce add element: 21
Consumer 1 removing element: 23
Produce add element: 20
Consumer 2 removing element: 22
Produce add element: 19
Consumer 1 removing element: 21
Produce add element: 18
Consumer 2 removing element: 20
Produce add element: 17
Consumer 1 removing element: 19
Produce add element: 16
Consumer 2 removing element: 18
Produce add element: 15
Consumer 1 removing element: 17
Produce add element: 14
Consumer 2 removing element: 16
Produce add element: 13
Consumer 1 removing element: 15
Produce add element: 12
Consumer 2 removing element: 14
Produce add element: 11
Consumer 1 removing element: 13
Produce add element: 10
Consumer 2 removing element: 12
Produce add element: 9
Consumer 1 removing element: 11
Produce add element: 8
Consumer 2 removing element: 10
Produce add element: 7
Consumer 1 removing element: 9
Produce add element: 6
Consumer 2 removing element: 8
Produce add element: 5
Consumer 1 removing element: 7
Produce add element: 4
Consumer 2 removing element: 6
Produce add element: 3
Consumer 1 removing element: 5
Produce add element: 2
Produce add element: 1
Produce add element: 0
Consumer 2 removing element: 4
(Producer thread ended)
Consumer 1 removing element: 3
(Consumer 1  thread ended)
(Consumer 2  thread ended)
cancel non-finished tasks
shutdown finished
------------------------------------------------------------------------
BUILD SUCCESS
------------------------------------------------------------------------
Total time: 10.526s
Finished at: Sun Apr 23 18:49:52 PDT 2017
Final Memory: 7M/245M
---------------------------------------------------------------------------------------------------------------------------------------------

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
