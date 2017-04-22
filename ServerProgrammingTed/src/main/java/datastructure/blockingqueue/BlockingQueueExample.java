package datastructure.blockingqueue;

import static VALUE.VALUE.echo;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

class ConsumerBlockingQueue implements Runnable {

    protected BlockingQueue mQueue = null;
    protected int mID;
    protected AtomicBoolean mFlag;

    public ConsumerBlockingQueue(BlockingQueue queue, int id, AtomicBoolean flag) {
        mQueue = queue;
        mID = id;
        mFlag = flag;
    }

    public void run() {
        echo("(consumer " + mID + " thread started)");
        do{
             echo("(consumer " + mID + " check)");
            try {
                echo("consumer " + mID + " take " + mQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while(mFlag.get());
        
        echo("(consumer " + mID + " thread ended)");
    }
}

class ProducerBlockingQueue implements Runnable {

    protected BlockingQueue mQueue = null;
    protected AtomicBoolean mFlag;

    public ProducerBlockingQueue(BlockingQueue queue, AtomicBoolean flag) {
        this.mQueue = queue;
        mFlag = flag;
    }

    public void run() {
        echo("(producer thread started)");
        
        try {

            for (int i = 0; i < 11; i++) {
                echo("producer put " + i);
                mQueue.put(i);
                Thread.sleep(1000);
            }
            mFlag.set(false);
            echo("producer set mFlag to false, result is " + mFlag.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        echo("(producer thread ended)");
    }
}

public class BlockingQueueExample {

    public static void main(String[] args) throws Exception {

        BlockingQueue queue = new ArrayBlockingQueue(1024);
        AtomicBoolean flag = new AtomicBoolean(true);

        ProducerBlockingQueue producer = new ProducerBlockingQueue(queue, flag);
        ConsumerBlockingQueue consumer1 = new ConsumerBlockingQueue(queue, 1, flag);
        ConsumerBlockingQueue consumer2 = new ConsumerBlockingQueue(queue, 2, flag);

        new Thread(producer).start();
        new Thread(consumer1).start();
        new Thread(consumer2).start();

//        Thread.sleep(4000);
    }
}

/*
------------------------------------------------------------------------
Building ServerProgrammingTed 1.0-SNAPSHOT
------------------------------------------------------------------------

--- exec-maven-plugin:1.2.1:exec (default-cli) @ ServerProgrammingTed ---
producer put 1
consumer take 1
producer put 2
consumer take 2
producer put 3
consumer take 3
------------------------------------------------------------------------
BUILD SUCCESS
------------------------------------------------------------------------
Total time: 4.480s
Finished at: Fri Apr 21 18:29:05 PDT 2017
Final Memory: 7M/245M
------------------------------------------------------------------------
 */

 /*
source : http://tutorials.jenkov.com/java-util-concurrent/blockingqueue.html

BlockingQueue Usage
    A BlockingQueue is typically used to have on thread produce objects, which another thread consumes. 

    The producing thread will keep producing new objects and insert them into the mQueue, 
        until the mQueue reaches some upper bound on what it can contain. 
        It's limit, in other words. If the blocking mQueue reaches its upper limit, 
        the producing thread is blocked while trying to insert the new object. 
        It remains blocked until a consuming thread takes an object out of the mQueue.

    The consuming thread keeps taking objects out of the blocking mQueue, and processes them. 
        If the consuming thread tries to take an object out of an empty mQueue, 
        the consuming thread is blocked until a producing thread puts an object into the mQueue.
 */
