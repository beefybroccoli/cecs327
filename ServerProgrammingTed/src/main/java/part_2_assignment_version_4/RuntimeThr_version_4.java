/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package part_2_assignment_version_4;

import VALUE.VALUE;
import static VALUE.VALUE.echo;
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

public class RuntimeThr_version_4 implements Runnable {

    private LinkedBlockingQueue mSharedRequestQue;
    private ConcurrentHashMap<String, String> mSharedResultQue;
    private Number_version_4 mNumberShareResource;
    private ReentrantLock mReentrantLock;
    private String mHOST_NAME;
    private int mHOST_SERVER_PORT;
    private boolean mFlag;
    Striped<ReadWriteLock> mSharedRWLock;
    ReadWriteLock mRWLock;
    Lock mLock;

    public RuntimeThr_version_4(String inputHostName, LinkedBlockingQueue inputRequestQue, ConcurrentHashMap<String, String> inputResultQue, Striped<ReadWriteLock> inputSharedRWLock) {

        mSharedRequestQue = inputRequestQue;
        mSharedResultQue = inputResultQue;
        mReentrantLock = new ReentrantLock();
        mNumberShareResource = new Number_version_4(mReentrantLock);
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
        Command_version_4 command;
        try {
            command = (Command_version_4) mSharedRequestQue.take();

            if (command.getmCommand() == 4 || command.getmCommand() == 5) {
//                runLocalThr(command.getmRequestorID(), command.getmCommand());
                runLocalThr(command);
            } else {
//                runNetworkThr(command.getmRequestorID(), command.getmCommand());
                runNetworkThr(command);
            }

        } catch (InterruptedException ex) {
            echo("Interruption Exception occured in fetch_command_and_startWorker() method" + "\n");
        }
    }

//    public void put_result_into_mResultQue(String inputClientID, String value) {
    public void put_result_into_mResultQue(Command_version_4 inputCommand) {

//        String key = inputClientID;
        String key = "" + inputCommand.getmRequestorID();
        String value = "" + inputCommand.getmResult();
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

    public String runLocalThr(Command_version_4 inputCommand) {

        LocalThr_version_4 task = new LocalThr_version_4(mNumberShareResource, mReentrantLock, inputCommand.getmRequestorID(), inputCommand.getmCommand());

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

            result = simulate_error(result);

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
            inputCommand.setmResult(result);
            if (result.equals("0") || result.equals("-1")) {
                System.out.println("runLocalThr reprocess - coomandId " + inputCommand.getCommandID() + " " + inputCommand.getmRequestorID() + "," + inputCommand.getmCommand() + "," + inputCommand.getmResult() + "\n");
                runLocalThr(inputCommand);
            } else {
                put_result_into_mResultQue(inputCommand);
            }
            executor.shutdownNow();
        }
        return result;
    }

    public String runNetworkThr(Command_version_4 inputCommand) {

        NetworkThr_version_4 task = new NetworkThr_version_4(mHOST_NAME, mHOST_SERVER_PORT, inputCommand.getmRequestorID(), "" + inputCommand.getmCommand());

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

            result = simulate_error(result);

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
            inputCommand.setmResult(result);
            if (result.equals("0") || result.equals("-1")) {
                System.out.println("runNetworkThr reprocess - coomandId " + inputCommand.getCommandID() + " " + inputCommand.getmRequestorID() + "," + inputCommand.getmCommand() + "," + inputCommand.getmResult() + "\n");
                runNetworkThr(inputCommand);
            } else {
                put_result_into_mResultQue(inputCommand);
            }
            executor.shutdownNow();
        }
        return result;
    }

    public String simulate_error(String result) {
        if (VALUE.getRandomNumberBetween(10, 1) == 3) {
            result = "" + -1;
        }
        return result;
    }
}
