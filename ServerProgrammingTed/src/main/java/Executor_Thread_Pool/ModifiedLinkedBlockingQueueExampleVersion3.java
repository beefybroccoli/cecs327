package Executor_Thread_Pool;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class ProducerVersion3 implements Runnable {

    private LinkedBlockingQueue queue;
    private boolean mFlag;
    private int mCounter = 100;

    public ProducerVersion3(LinkedBlockingQueue queue) {
        this.queue = queue;
        mFlag = true;

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
        int rand = rn.nextInt(10) + 1;
        --mCounter;
        result = "" + rand;
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

                    }
                    TimeUnit.MILLISECONDS.sleep(50);
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

}//end class ProducerVersion4

class ConsumerVersion3 implements Runnable {

    private LinkedBlockingQueue queue;
    private ProducerVersion3 producer;
    private Integer mID;
    private ReentrantLock mLock;
    private boolean mFlag;
    private String mName;

    public ConsumerVersion3(Integer id, LinkedBlockingQueue queue, ProducerVersion3 producer) {
        this.queue = queue;
        this.producer = producer;
        mID = id;
        mFlag = true;
        mName = "consumer" + id;
    }

    @Override
    public void run() {

        try {
            System.out.println("(" + mName + " thread started)");

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ex) {
                System.out.println(mName + " InterruptedException occured");
            }
            
            // As long as the producer is mFlag,
            // we remove elements from the queue.
            while (mFlag) {

                try {
                    if (queue.size() > 0) {
                        
//                        String element = queue.peek().toString();
                        
//                        System.out.println(mName + " peek " + queue.peek() + element);
                        
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

}//end class ConsumerVersion4


/*
 *modify this to have one producer, multiple clietns taking different objects from the queue 
 */
public class ModifiedLinkedBlockingQueueExampleVersion3 {

    public static void main(String[] args) {

        test();
    }

    public static void test() {

        int numberOfConsumers = 10;

        //Creates a LinkedBlockingQueue with a capacity of Integer.MAX_VALUE.
        LinkedBlockingQueue queue = new LinkedBlockingQueue();

        ProducerVersion3 producer = new ProducerVersion3(queue);
        ConsumerVersion3[] consumers = new ConsumerVersion3[10];
        for (int i = 0; i < numberOfConsumers; i++) {
            consumers[i] = new ConsumerVersion3(i + 1, queue, producer);
        }

        Thread producerThread = new Thread(producer);
        Thread[] consumerThreads = new Thread[10];
        for (int i = 0; i < numberOfConsumers; i++) {
            consumerThreads[i] = new Thread(consumers[i]);
        }

        ExecutorService executor = Executors.newFixedThreadPool(numberOfConsumers);


        /*
        An ExecutorService provides two methods for that purpose: 
            _shutdown() waits for currently running tasks to finish 
            _shutdownNow() interrupts all running tasks and shut the executor down immediately.
         */
        try {
            executor.submit(producerThread);
            for (int i = 0; i < numberOfConsumers; i++) {
                executor.submit(consumerThreads[i]);
            }
            executor.awaitTermination(60, TimeUnit.SECONDS);

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
--- exec-maven-plugin:1.2.1:exec (default-cli) @ ServerProgrammingTed ---
(Producer thread started)
(consumer4 thread started)
(consumer3 thread started)
(consumer2 thread started)
(consumer6 thread started)
(consumer1 thread started)
(consumer7 thread started)
(consumer8 thread started)
(consumer5 thread started)
(consumer9 thread started)
consumer4 consume 4, after consumption size is 1
(Producer thread ended, mCounter = 0)
(consumer10 thread started)
consumer10 consume 10, after consumption size is 98
consumer3 consume 3, after consumption size is 97
consumer8 consume 8, after consumption size is 96
consumer8 consume 8, after consumption size is 95
consumer8 consume 8, after consumption size is 94
consumer9 consume 9, after consumption size is 93
consumer8 consume 8, after consumption size is 92
consumer7 consume 7, after consumption size is 91
consumer10 consume 10, after consumption size is 90
consumer6 consume 6, after consumption size is 89
consumer4 consume 4, after consumption size is 88
consumer5 consume 5, after consumption size is 87
consumer6 consume 6, after consumption size is 86
consumer1 consume 1, after consumption size is 85
consumer5 consume 5, after consumption size is 84
consumer9 consume 9, after consumption size is 83
consumer8 consume 8, after consumption size is 82
consumer9 consume 9, after consumption size is 81
consumer3 consume 3, after consumption size is 80
consumer10 consume 10, after consumption size is 79
consumer2 consume 2, after consumption size is 78
consumer4 consume 4, after consumption size is 77
consumer2 consume 2, after consumption size is 76
consumer4 consume 4, after consumption size is 75
consumer7 consume 7, after consumption size is 74
consumer8 consume 8, after consumption size is 73
consumer8 consume 8, after consumption size is 72
consumer3 consume 3, after consumption size is 71
consumer3 consume 3, after consumption size is 70
consumer5 consume 5, after consumption size is 69
consumer8 consume 8, after consumption size is 68
consumer10 consume 10, after consumption size is 67
consumer8 consume 8, after consumption size is 65
consumer9 consume 9, after consumption size is 65
consumer9 consume 9, after consumption size is 64
consumer6 consume 6, after consumption size is 63
consumer6 consume 6, after consumption size is 62
consumer9 consume 9, after consumption size is 61
consumer1 consume 1, after consumption size is 60
consumer2 consume 2, after consumption size is 59
consumer6 consume 6, after consumption size is 58
consumer10 consume 10, after consumption size is 57
consumer4 consume 4, after consumption size is 56
consumer3 consume 3, after consumption size is 55
consumer8 consume 8, after consumption size is 54
consumer6 consume 6, after consumption size is 53
consumer10 consume 10, after consumption size is 52
consumer8 consume 8, after consumption size is 51
consumer7 consume 7, after consumption size is 50
consumer9 consume 9, after consumption size is 49
consumer9 consume 9, after consumption size is 48
consumer3 consume 3, after consumption size is 47
consumer8 consume 8, after consumption size is 46
consumer6 consume 6, after consumption size is 45
consumer2 consume 2, after consumption size is 44
consumer10 consume 10, after consumption size is 43
consumer3 consume 3, after consumption size is 42
consumer3 consume 3, after consumption size is 41
consumer3 consume 3, after consumption size is 40
consumer4 consume 4, after consumption size is 39
consumer10 consume 10, after consumption size is 38
consumer6 consume 6, after consumption size is 37
consumer6 consume 6, after consumption size is 36
consumer4 consume 4, after consumption size is 35
consumer5 consume 5, after consumption size is 34
consumer5 consume 5, after consumption size is 33
consumer5 consume 5, after consumption size is 32
consumer3 consume 3, after consumption size is 31
consumer2 consume 2, after consumption size is 30
consumer5 consume 5, after consumption size is 29
consumer6 consume 6, after consumption size is 28
consumer5 consume 5, after consumption size is 27
consumer3 consume 3, after consumption size is 26
consumer8 consume 8, after consumption size is 25
consumer1 consume 1, after consumption size is 24
consumer8 consume 8, after consumption size is 23
consumer9 consume 9, after consumption size is 22
consumer10 consume 10, after consumption size is 21
consumer1 consume 1, after consumption size is 20
consumer8 consume 8, after consumption size is 19
consumer8 consume 8, after consumption size is 18
consumer8 consume 8, after consumption size is 17
consumer7 consume 7, after consumption size is 16
consumer3 consume 3, after consumption size is 15
consumer1 consume 1, after consumption size is 14
consumer1 consume 1, after consumption size is 13
consumer2 consume 2, after consumption size is 12
consumer2 consume 2, after consumption size is 11
consumer3 consume 3, after consumption size is 10
consumer1 consume 1, after consumption size is 9
consumer7 consume 7, after consumption size is 8
consumer8 consume 8, after consumption size is 7
consumer7 consume 7, after consumption size is 6
consumer2 consume 2, after consumption size is 5
consumer7 consume 7, after consumption size is 4
consumer10 consume 10, after consumption size is 3
consumer9 consume 9, after consumption size is 2
consumer7 consume 7, after consumption size is 1
consumer6 consume 6, after consumption size is 0
(consumer8 thread ended)
canceled non-finished tasks
consumer9 InterruptedException occured
(consumer9 thread ended)
consumer10 InterruptedException occured
(consumer10 thread ended)
consumer3 InterruptedException occured
(consumer3 thread ended)
consumer5 InterruptedException occured
(consumer5 thread ended)
consumer7 InterruptedException occured
(consumer7 thread ended)
consumer6 InterruptedException occured
(consumer6 thread ended)
shutdown finished
consumer4 InterruptedException occured
(consumer4 thread ended)
consumer1 InterruptedException occured
(consumer1 thread ended)
consumer2 InterruptedException occured
(consumer2 thread ended)
------------------------------------------------------------------------
BUILD SUCCESS
------------------------------------------------------------------------
Total time: 1:00.532s
Finished at: Sun Apr 23 20:16:13 PDT 2017
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
