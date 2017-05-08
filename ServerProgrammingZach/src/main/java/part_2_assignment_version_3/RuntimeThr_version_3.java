/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package part_2_assignment_version_3;

import part_2_assignment_version_final.object.VALUE;
import static part_2_assignment_version_final.object.VALUE.echo;
import com.google.common.util.concurrent.Striped;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

public class RuntimeThr_version_3 implements Runnable {

    private LinkedBlockingQueue mSharedRequestQue;
    private ConcurrentHashMap<String, String> mSharedResultQue;
    private Number_version_3 mNumberShareResource;
    private ReentrantLock mReentrantLock;
    private String mHOST_NAME;
    private int mHOST_SERVER_PORT;
    private boolean mFlag;
    Striped<ReadWriteLock> mSharedRWLock;
    ReadWriteLock mRWLock;
    Lock mLock;

    public RuntimeThr_version_3(String inputHostName, LinkedBlockingQueue inputRequestQue, ConcurrentHashMap<String, String> inputResultQue, Striped<ReadWriteLock> inputSharedRWLock) {

        mSharedRequestQue = inputRequestQue;
        mSharedResultQue = inputResultQue;
        mReentrantLock = new ReentrantLock();
        mNumberShareResource = new Number_version_3(mReentrantLock);
        mHOST_NAME = inputHostName;
        mHOST_SERVER_PORT = VALUE.SERVER_PORT_NUMBER;
        mSharedRWLock = inputSharedRWLock;
        mFlag = true;
    }

    @Override
    public void run() {

        System.out.println("(runtimeThr Started)\n");

        do {

            try {

                //sleep when the request data structure is empty
                while (mSharedRequestQue.isEmpty() && mFlag) {
                    TimeUnit.MILLISECONDS.sleep(100);
                }

                fetch_command_and_startWorker();

            } catch (InterruptedException e) {
                echo("Interruption occured in rumtimeThr\n");
                mFlag = false;

            }//end try 

        } while (mFlag);

        String mSharedRequestQueString = "\n" + "mSharedRequestQue = " + mSharedRequestQue.toString();
        String mSharedResultQueString = "\n" + "mSharedResultQue = " + mSharedResultQue.toString();
        System.out.println("(runtimeThr Ended, " + mSharedRequestQueString + mSharedResultQueString + ")\n");

    }

    public void fetch_command_and_startWorker() {

//        System.out.println("size of mRequestQue is " + mSharedRequestQue.size() + ", size of mResultQue is " + mSharedRequestQue.size() + "\n");
        Command_version_3 command;
        try {
            command = (Command_version_3) mSharedRequestQue.take();

            if (command.getmCommand() == 4 || command.getmCommand() == 5) {
                runLocalThr(command.getmRequestorID(), command.getmCommand());
            } else {
                runNetworkThr(command.getmRequestorID(), command.getmCommand());
            }

        } catch (InterruptedException ex) {
            echo("Interruption Exception occured in fetch_command_and_startWorker() method" + "\n");
        }
    }

    public void put_result_into_mResultQue(String inputClientID, String value) {
        String key = inputClientID;
        mRWLock = mSharedRWLock.get(key);
        mLock = mRWLock.writeLock();
        try {
            mLock.lock();

            mSharedResultQue.put(key, value);
//            System.out.println(
//                    "RuntimeThr try to add to queue: String " + mSharedResultQue.get(key)
//                    + ", the result was " + mSharedResultQue.containsKey(key)
//                    + ", size is " + mSharedResultQue.size()
//                    + ", map : " + mSharedResultQue.toString()
//                    + "\n");

        } finally {
            mLock.unlock();
        }
    }

    public String runLocalThr(int inputClientID, int inputCommand) {
        //public LocalThr_version_4(Number_version_4 inputNumber, ReentrantLock inputLock, int command) {
        LocalThr_version_3 task = new LocalThr_version_3(mNumberShareResource, mReentrantLock, inputClientID, inputCommand);

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
        } catch (InterruptedException ex) {
            result = "-1";
            echo("InterruptedException occured in runLocalThr() method" + "\n");
        } catch (ExecutionException ex) {
            result = "-1";
            echo("ExecutionException occured in runLocalThr() method" + "\n");
        } catch (TimeoutException ex) {
            result = "-1";
            echo("TimeoutException occured in runLocalThr() method" + "\n");
        } catch (NullPointerException ex) {
            result = "-1";
            echo("NullPointerException occured in runLocalThr() method" + "\n");
        } finally {
            if (result.equals("0") || result.equals("-1")) {
                runLocalThr(inputClientID, inputCommand);
            } else {
                put_result_into_mResultQue("" + inputClientID, result);
            }
//            System.out.println("runLocalThr received result " + result);
            executor.shutdownNow();
        }
        return result;
    }

    public String runNetworkThr(int inputClientID, int inputCommand) {
        NetworkThr_version_3 task = new NetworkThr_version_3(mHOST_NAME, mHOST_SERVER_PORT, inputClientID, "" + inputCommand);

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

        } catch (InterruptedException ex) {
            result = "-1";
            echo("InterruptedException occured in runNetworkThr() method" + "\n");
        } catch (ExecutionException ex) {
            result = "-1";
            echo("ExecutionException occured in runNetworkThr() method" + "\n");
        } catch (TimeoutException ex) {
            result = "-1";
            echo("TimeoutException occured in runNetworkThr() method" + "\n");
        } catch (NullPointerException ex) {
            result = "-1";
            echo("NullPointerException occured in runNetworkThr() method" + "\n");
        } finally {
            if (result.equals("0") || result.equals("-1")) {

            } else {
                put_result_into_mResultQue("" + inputClientID, result);
            }
//            System.out.println("runNetworkThr received result " + result);
            executor.shutdownNow();
        }
        return result;
    }

}

// 

// System.out.println("size of mRequestQue is " + mRequestQue.size() + ", size of mResultQue is " + mResultQue.size());
