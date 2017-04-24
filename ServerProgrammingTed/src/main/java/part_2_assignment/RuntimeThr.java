/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package part_2_assignment;

import VALUE.VALUE;
import static VALUE.VALUE.echo;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

public class RuntimeThr implements Runnable {

    private LinkedBlockingQueue mRequestQue, mResultQue;
    private Number mNumberShareResource;
    private ReentrantLock mLock;
    private String mHOST_NAME;
    private int mHOST_SERVER_PORT;

    public RuntimeThr() {

        mRequestQue = new LinkedBlockingQueue();
        mResultQue = new LinkedBlockingQueue();
        mLock = new ReentrantLock();
        mNumberShareResource = new Number(mLock);
        mHOST_NAME = VALUE.LOCAL_HOST;
        mHOST_SERVER_PORT = VALUE.SERVER_PORT_NUMBER;
    }

    public RuntimeThr(String inputHostName) {

        mRequestQue = new LinkedBlockingQueue();
        mResultQue = new LinkedBlockingQueue();
        mLock = new ReentrantLock();
        mNumberShareResource = new Number(mLock);
        mHOST_NAME = inputHostName;
        mHOST_SERVER_PORT = VALUE.SERVER_PORT_NUMBER;
    }

    @Override
    public void run() {

        System.out.println("(runtimeThr Started)");

        try {

            //spawn 10 uThr
            for (int i = 0; i < 10; i++) {
                //uThr(int inputID, LinkedBlockingQueue inputRequestQue, LinkedBlockingQueue inputResultQue) {
                int id = i + 1;
                new UThr(id, mRequestQue, mResultQue).run();
            }

            do {
                System.out.println("size of mRequestQue is " + mRequestQue.size() + ", size of mResultQue is " + mResultQue.size());
            } while (mRequestQue.size() < 200);

            for (int i = 0; i < 200; i++) {
                Command command = (Command) mRequestQue.peek();
                if (command.getmCommand() == 4 || command.getmCommand() == 5) {
                    try {
                        command = (Command) mRequestQue.take();
                        runLocalThr(command.getmRequestorID(), command.getmCommand());
                    } catch (InterruptedException ex) {
                        echo("Interruption Exception occured in runtime line 65");
                    }
                } else {
                    try {
                        command = (Command) mRequestQue.take();
                        runNetworkThr(command.getmRequestorID(), command.getmCommand());
                    } catch (InterruptedException ex) {
                        echo("Interruption Exception occured in runtime line 72");
                    }
                }

            }
        } finally {
            System.out.println("size of mRequestQue is " + mRequestQue.size() + ", size of mResultQue is " + mResultQue.size());
            System.out.println("(runtimeThr Ended)");
        }

    }

    public String runLocalThr(int inputClientID, int inputCommand) {
        //public LocalThr(Number inputNumber, ReentrantLock inputLock, int command) {
        LocalThr task = new LocalThr(mNumberShareResource, mLock, inputClientID, inputCommand);

        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<String> future = executor.submit(
                () -> {
                    task.run();
                    return task.call();
                }
        );

        String result = "";

        try {
            result = future.get(5, TimeUnit.MINUTES);
            mResultQue.put(result);
        } catch (InterruptedException ex) {
            echo("InterruptedException occured  in runtime line 104");
        } catch (ExecutionException ex) {
            echo("ExecutionException occured");
        } catch (TimeoutException ex) {
            echo("TimeoutException occured");
        } catch (NullPointerException ex) {
            echo("NullPointerException occured");
        } finally {
            System.out.println("result = " + result);
            executor.shutdownNow();
        }
        return result;
    }

    public String runNetworkThr(int inputClientID, int input) {
        NetworkThr task = new NetworkThr(mHOST_NAME, mHOST_SERVER_PORT, inputClientID, "" + input);

        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<String> future = executor.submit(
                () -> {
                    task.run();
                    return task.call();
                }
        );

        String result = "";

        try {
            result = future.get(5, TimeUnit.MINUTES);
            mResultQue.put(result);
        } catch (InterruptedException ex) {
            echo("InterruptedException occured in runtime line 136");
        } catch (ExecutionException ex) {
            echo("ExecutionException occured");
        } catch (TimeoutException ex) {
            echo("TimeoutException occured");
        } catch (NullPointerException ex) {
            echo("NullPointerException occured");
        } finally {
            System.out.println("result = " + result);
            executor.shutdownNow();
        }
        return result;
    }



}
