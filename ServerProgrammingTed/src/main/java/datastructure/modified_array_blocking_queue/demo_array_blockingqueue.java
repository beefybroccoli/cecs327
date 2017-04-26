package datastructure.modified_array_blocking_queue;

import static VALUE.VALUE.echo;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

public class demo_array_blockingqueue {

    public static void main(String[] argv) throws Exception {
        int capacity = 10;
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(capacity);

        for (int i = 0; i < 10; i++) {
            queue.add(i);
            System.out.println("que added " + i + ", result = " + queue.contains(i));
        }
        System.out.println("after adding : " + queue + "\n");

        for (int i = 0; i < 10; i++) {
            System.out.println("removed " + i + " from que, result =  " + queue.remove(i));
        }
        System.out.println("after removing : " + queue + "\n");

        for (int i = 0; i < 10; i++) {
            queue.add(i);
            System.out.println("que added " + i + ", result = " + queue.contains(i));
        }
        System.out.println("after adding : " + queue + "\n");

        Iterator itr = queue.iterator();
        for (int i = 0; i < 10; i++) {
            echo("itr : " + (Integer) itr.next());
            itr.remove();
        }
        System.out.println("after removing : " + queue + "\n");

    }
}
