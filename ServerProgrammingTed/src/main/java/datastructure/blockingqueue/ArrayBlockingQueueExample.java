package datastructure.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

class ProducerArrayBlockingQueue implements Runnable {

    private BlockingQueue queue;

    public ProducerArrayBlockingQueue(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

        int input = 0;
        do {
            ++input;
            
            // We are adding elements using offer() in order to check if
            // it actually managed to insert them.
            System.out.println("Trying to add to queue: String " + input
                    + " and the result was " + queue.offer("String " + input));

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (input < 10);
    }
}

class ConsumerArrayBlockingQueue implements Runnable {

    private BlockingQueue queue;
    private int mID;

    public ConsumerArrayBlockingQueue(BlockingQueue queue, int inputID) {
        this.queue = queue;
        mID = inputID;
    }

    @Override
    public void run() {

        // As long as there are empty positions in our array,
        // we want to check what's going on.
        while (queue.remainingCapacity() > 0) {
            try {
                System.out.println("consumer " + mID + " consume " + queue.take().toString());
                System.out.println("after consumption, Queue size: " + queue.size()
                        + ", remaining capacity: " + queue.remainingCapacity());
            } catch (InterruptedException ex) {
                Logger.getLogger(ConsumerArrayBlockingQueue.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class ArrayBlockingQueueExample {

    public static void main(String[] args) {

        // Let's create a blocking queue that can hold at most 5 elements.
        BlockingQueue sharedQueue = new ArrayBlockingQueue<>(100);

        // The two threads will access the same queue, in order
        // to test its blocking capabilities.
        Thread producer = new Thread(new ProducerArrayBlockingQueue(sharedQueue));

        Thread consumer1 = new Thread(new ConsumerArrayBlockingQueue(sharedQueue, 1));
        Thread consumer2 = new Thread(new ConsumerArrayBlockingQueue(sharedQueue, 2));

        producer.start();

        consumer1.start();
        consumer2.start();

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
