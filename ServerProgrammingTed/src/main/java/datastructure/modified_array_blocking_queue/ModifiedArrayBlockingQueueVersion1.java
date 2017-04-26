package datastructure.modified_array_blocking_queue;

import static VALUE.VALUE.echo;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ProducerArrayBlockingQueueVersion1 implements Runnable {

    private BlockingQueue queue;
    private boolean mFlag;

    public ProducerArrayBlockingQueueVersion1(BlockingQueue queue) {
        this.queue = queue;
        mFlag = true;

    }

    @Override
    public void run() {

        int input = 0;
        do {
            try {

                String item = ((++input % 2 == 0) ? "1" : "2");
                // We are adding elements using offer() in order to check if
                // it actually managed to insert them.
                System.out.println("Trying to add to queue: String " + input
                        + " and the result was " + queue.offer("String \"" + item + "\"")
                        + "\n");
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                mFlag = false;
                echo("Interruption Occured in producer");
            }
        } while (mFlag);
    }
}

class ConsumerArrayBlockingQueueVersion1 implements Runnable {

    private BlockingQueue queue;
    private int mID;
    private boolean mFlag;

    public ConsumerArrayBlockingQueueVersion1(BlockingQueue queue, int inputID) {
        this.queue = queue;
        mID = inputID;
        mFlag = true;
    }

    @Override
    public void run() {

        // As long as there are empty positions in our array,
        // we want to check what's going on.
        while (mFlag && queue.remainingCapacity() > 0) {
            try {
                System.out.println("consumer" + mID + " consume " + queue.take().toString());
                System.out.println("after consumption, Queue size: " + queue.size()
                        + ", remaining capacity: " + queue.remainingCapacity() + "\n");
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                mFlag = false;
                echo("Interruption Occured in consumer" + mID);
            }
        }
    }
}

public class ModifiedArrayBlockingQueueVersion1 {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        BlockingQueue sharedQueue = new ArrayBlockingQueue<>(2);

        // The two threads will access the same queue, in order
        // to test its blocking capabilities.
        Thread producer = new Thread(new ProducerArrayBlockingQueueVersion1(sharedQueue));
        Thread consumer1 = new Thread(new ConsumerArrayBlockingQueueVersion1(sharedQueue, 1));
        Thread consumer2 = new Thread(new ConsumerArrayBlockingQueueVersion1(sharedQueue, 2));

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

/*
------------------------------------------------------------------------
Building ServerProgrammingTed 1.0-SNAPSHOT
------------------------------------------------------------------------

--- exec-maven-plugin:1.2.1:exec (default-cli) @ ServerProgrammingTed ---
Queue size: 0, remaining capacity: 5
Trying to add to queue: String 0 and the result was true
Queue size: 1, remaining capacity: 4
Trying to add to queue: String 1 and the result was true
Trying to add to queue: String 2 and the result was true
Queue size: 3, remaining capacity: 2
Trying to add to queue: String 3 and the result was true
Queue size: 3, remaining capacity: 1
Trying to add to queue: String 4 and the result was true
Trying to add to queue: String 5 and the result was false
Trying to add to queue: String 6 and the result was false
Trying to add to queue: String 7 and the result was false
------------------------------------------------------------------------
BUILD SUCCESS
------------------------------------------------------------------------
Total time: 4.439s
Finished at: Fri Apr 21 18:30:18 PDT 2017
Final Memory: 7M/245M
------------------------------------------------------------------------

 */

 /*

source : http://tutorials.jenkov.com/java-util-concurrent/arrayblockingqueue.html

    The ArrayBlockingQueue class implements the BlockingQueue interface. 
        Read the BlockingQueue text for more information about the interface.

    ArrayBlockingQueue is a bounded, blocking queue that stores the elements internally in an array. 
        That it is bounded means that it cannot store unlimited amounts of elements. 
        There is an upper bound on the number of elements it can store at the same time. 
        You set the upper bound at instantiation time, and after that it cannot be changed.

    The ArrayBlockingQueue stores the elements internally in FIFO (First In, First Out) order. 
        The head of the queue is the element which has been in queue the longest time, 
        and the tail of the queue is the element which has been in the queue the shortest time.

    Here is how to instantiate and use an ArrayBlockingQueue:

        //------------------------------------------------
        BlockingQueue queue = new ArrayBlockingQueue(1024);
        queue.put("1");
        Object object = queue.take();
        //------------------------------------------------

    Here is a BlockingQueue example that uses Java Generics. Notice how you can put and take String's instead of :

        //----------------------------------------------------------------
        BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024);
        queue.put("1");
        String string = queue.take();
        //----------------------------------------------------------------
 */
