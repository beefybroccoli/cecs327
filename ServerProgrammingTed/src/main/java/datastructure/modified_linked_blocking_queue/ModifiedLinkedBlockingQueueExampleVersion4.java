package datastructure.modified_linked_blocking_queue;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class ProducerVersion4 implements Runnable {

    private LinkedBlockingQueue queue;
    private boolean mFlag;
    private int mCounter = 100;

    public ProducerVersion4(LinkedBlockingQueue queue) {
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

    public void setCounter(int input) {
        mCounter = input;
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

class ConsumerVersion4 implements Runnable {

    private LinkedBlockingQueue queue;
    private ProducerVersion4 producer;
    private Integer mID;
    private ReentrantLock mLock;
    private boolean mFlag;
    private String mName;

    public ConsumerVersion4(Integer id, LinkedBlockingQueue queue, ProducerVersion4 producer) {
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

                        if (queue.peek().equals("" + mID)) {
                            System.out.println(mName + " consume " + queue.take() + ", after consumption size is " + queue.size());
                        }
                    }
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    mFlag = false;
                    System.out.println(mName + " InterruptedException occured");
                }

            }//end while
        } finally {
            System.out.println("(" + mName + " thread ended)");
        }

    }//end run

}//end class ConsumerVersion4

/*
    ModifiedLinkedBlockingQueueExampleVersion4 class is a simulation of 10 consumers and 1 producer
*/
public class ModifiedLinkedBlockingQueueExampleVersion4 {

    public static void main(String[] args) {

        test();
    }

    public static void test() {

        int numberOfConsumers = 10;

        //Creates a LinkedBlockingQueue with a capacity of Integer.MAX_VALUE.
        LinkedBlockingQueue queue = new LinkedBlockingQueue();

        ProducerVersion4 producer = new ProducerVersion4(queue);
        producer.setCounter(numberOfConsumers * 10);
        ConsumerVersion4[] consumers = new ConsumerVersion4[10];
        for (int i = 0; i < numberOfConsumers; i++) {
            consumers[i] = new ConsumerVersion4(i + 1, queue, producer);
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
//            executor.awaitTermination(120, TimeUnit.SECONDS);
            executor.awaitTermination(1, TimeUnit.MINUTES);

        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("canceled non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }

    }

}

/*
https://examples.javacodegeeks.com/core-java/util/concurrent/linkedblockingqueue/java-util-concurrent-linkedblockingqueue-example/
 */
/**
 *
 * 1. ArrayBlockingQueue vs LinkedBlockingQueue
 *
 * In a previous article (java.util.concurrent.ArrayBlockingQueue Example), we
 * talked about ArrayBlockingQueue and its usage. Here, we will try to make some
 * comparisons between ArrayBlockingQueue and LinkedBlockingQueue to make clear
 * in which cases we should prefer each one. It is important to make clear
 * distinctions, as both data structures serve very similar needs, but
 * performance and implementation varies.
 *
 * 1.1 Performance
 *
 * ArrayBlockingQueue: It uses an internal array in which the elements are kept,
 * and the Queue interface imposes certain rules (like the FIFO rule, which is
 * essential to any queue). Because it uses an array, it has a fixed size which
 * is given in the constructor.
 *
 * LinkedBlocking Queue: It uses nodes (like a linked list), to keep track of
 * the order of the elements, which increases the complexity of the data
 * structure. It can have a fixed-size limit as well, but if we don’t define one
 * the limit is Integer.MAX_VALUE by default.
 *
 * According to the previous information, you can clearly see why
 * ArrayBlockingQueue is faster than LinkedBlockingQueue, which is backed by a
 * benchmark that was published in an older JavaCodeGeeks article. The benchmark
 * specifics and results can be found here. In every case, the performance of
 * ArrayBlockingQueue is better.
 *
 * 1.2 Implementation in synchronization The major implementation difference
 * between the two data structures (synchronization-wise) is that because
 * ArrayBlockingQueue keeps the elements in an array it needs only one lock to
 * keep everything synchronized. On the other hand, LinkedBlockingQueue uses two
 * locks, one for insertion and one for extraction. That happens because while
 * ArrayBlockingQueue contains just an array, LinkedBlockingQueue contains a
 * series of connected nodes, so it doesn’t need to keep track of insertion and
 * extraction at the same time.
 */
