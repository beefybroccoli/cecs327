package datastructure.blockingqueue;

import java.util.concurrent.SynchronousQueue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

class SynchronousQueueProducer implements Runnable {

    protected BlockingQueue<String> blockingQueue;
    final Random random = new Random();

    public SynchronousQueueProducer(BlockingQueue<String> queue) {
        this.blockingQueue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String data = UUID.randomUUID().toString();
                System.out.println("Put: " + data);
                blockingQueue.put(data);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class SynchronousQueueConsumer implements Runnable {

    protected BlockingQueue<String> blockingQueue;

    public SynchronousQueueConsumer(BlockingQueue<String> queue) {
        this.blockingQueue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String data = blockingQueue.take();
                System.out.println(Thread.currentThread().getName()
                        + " take(): " + data);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

public class SynchronousQueueExample {

    public static void main(String[] args) {
        final BlockingQueue<String> synchronousQueue = new SynchronousQueue<String>();

        SynchronousQueueProducer queueProducer = new SynchronousQueueProducer(
                synchronousQueue);
        new Thread(queueProducer).start();

        SynchronousQueueConsumer queueConsumer1 = new SynchronousQueueConsumer(
                synchronousQueue);
        new Thread(queueConsumer1).start();

        SynchronousQueueConsumer queueConsumer2 = new SynchronousQueueConsumer(
                synchronousQueue);
        new Thread(queueConsumer2).start();

    }
}


/*
source : http://javapapers.com/java/java-synchronousqueue/
*/

/*
Java SynchronousQueue

Share on Facebook
 
Share on Twitter
 
Share on Linkedin
 
Share on Email
This Java tutorial is to learn about the concurrent collection SynchronousQueue. It is an implementation of BlockingQueue. Among all Java concurrent collections, SynchronousQueue is different. Capacity of a synchrounous queue is always zero. It is because in SynchronousQueue an insert will wait for a remove operation by another thread and vice versa.

put() call to a SynchronousQueue will not return until there is a corresponding take() call.
peek is not possible with a SynchronousQueue
As there is no element iteration is also not possible.
Insert is not possible if there is a thread trying to remove it.
SynchronousQueue should be imagined like a baton in a relay race.
If there are more than one thread waiting for a  removal so that they can do insert then with fairness set to true, threads are granted access in FIFO order.
SynchronousQueue is the default BlockingQueue used for the Executors.newCachedThreadPool() methods.

SynchronousQueue Example

    Let us have a look at a producer-consumer based example to understand the SynchronousQueue.
*/