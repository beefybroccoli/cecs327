package datastructure.blocking_queue;

import java.util.concurrent.LinkedBlockingQueue;

class Producer implements Runnable {

    private LinkedBlockingQueue queue;
    private boolean running;

    public Producer(LinkedBlockingQueue queue) {
        this.queue = queue;
        running = true;
    }

    // We need to check if the producer thread is
    // Still running, and this method will return
    // the state (running/stopped).
    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {

        // We are adding elements using put() which waits
        // until it can actually insert elements if there is
        // not space in the queue.
        for (int i = 0; i < 15; i++) {
            String element = "String" + i;

            try {
                queue.put(element);
                System.out.println("P\tAdding element: " + element);

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("P Completed.");
        running = false;
    }

}

class ObservingConsumer implements Runnable {

    private LinkedBlockingQueue queue;
    private Producer producer;

    public ObservingConsumer(LinkedBlockingQueue queue, Producer producer) {
        this.queue = queue;
        this.producer = producer;
    }

    @Override
    public void run() {

        // As long as the producer is running,
        // we want to check for elements.
        while (producer.isRunning()) {
            System.out.println("OC\tElements right now: " + queue);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("OC Completed.");
        System.out.println("Final elements in the queue: " + queue);
    }
}

class Consumer implements Runnable {

    private LinkedBlockingQueue queue;
    private Producer producer;

    public Consumer(LinkedBlockingQueue queue, Producer producer) {
        this.queue = queue;
        this.producer = producer;
    }

    @Override
    public void run() {

        // As long as the producer is running,
        // we remove elements from the queue.
        while (producer.isRunning()) {

            try {
                System.out.println("RC\tRemoving element: " + queue.take());

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("RC completed.");
    }
}

public class LinkedBlockingQueueExample {

    public static void main(String[] args) {
        tedTest();
    }

    public static void tedTest() {
        //create a queue
        LinkedBlockingQueue queue = new LinkedBlockingQueue(10);

        Producer producer = new Producer(queue);
        Consumer consumer1 = new Consumer(queue, producer);
        Consumer consumer2 = new Consumer(queue, producer);

        Thread producerThread = new Thread(producer);
        Thread consumerThread1 = new Thread(consumer1);
        Thread consumerThread2 = new Thread(consumer2);

        producerThread.start();
        consumerThread1.start();
        consumerThread2.start();
    }

    public static void originalTest() {
        //create a queue
        LinkedBlockingQueue queue = new LinkedBlockingQueue(10);

        Producer producer = new Producer(queue);
        ObservingConsumer obsConsumer = new ObservingConsumer(queue, producer);
        Consumer remConsumer = new Consumer(queue, producer);

        Thread producerThread = new Thread(producer);
        Thread obsConsumerThread = new Thread(obsConsumer);
        Thread remConsumerThread = new Thread(remConsumer);

        producerThread.start();
        obsConsumerThread.start();
        remConsumerThread.start();
    }
}

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