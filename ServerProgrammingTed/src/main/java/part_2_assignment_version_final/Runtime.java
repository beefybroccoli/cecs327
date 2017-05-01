package part_2_assignment_version_final;

import part_2_assignment_version_final.object.Command;
import part_2_assignment_version_final.object.ClientSharedResource;
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

public class Runtime implements Runnable {

    private LinkedBlockingQueue mSharedRequestQue;
    private ConcurrentHashMap<String, String> mSharedResultQue;
    private ClientSharedResource mNumberShareResource;
    private ReentrantLock mReentrantLock;
    private String mHOST_NAME;
    private int mHOST_SERVER_PORT;
    private boolean mFlag;
    Striped<ReadWriteLock> mSharedRWLock;
    ReadWriteLock mRWLock;
    Lock mLock;

    public Runtime(String inputHostName, LinkedBlockingQueue inputRequestQue, ConcurrentHashMap<String, String> inputResultQue, Striped<ReadWriteLock> inputSharedRWLock) {

        mSharedRequestQue = inputRequestQue;
        mSharedResultQue = inputResultQue;
        mReentrantLock = new ReentrantLock();
        mNumberShareResource = new ClientSharedResource(mReentrantLock);
        mHOST_NAME = inputHostName;
        mHOST_SERVER_PORT = VALUE.SERVER_PORT_NUMBER;
        mSharedRWLock = inputSharedRWLock;
        mFlag = true;
    }

    @Override
    public void run() {

        System.out.println("(Runtime Started)\n");

        do {

            try {

                //sleep when the request data structure is empty
                while (mSharedRequestQue.isEmpty() && mFlag) {
                    TimeUnit.MILLISECONDS.sleep(100);
                }

                fetch_command_and_startWorker();

            } catch (InterruptedException e) {
                echo("Interruption occured in Rumtime\n");
                mFlag = false;

            }//end try 

        } while (mFlag);

        String mSharedRequestQueString = "\n" + "mSharedRequestQue = " + mSharedRequestQue.toString();
        String mSharedResultQueString = "\n" + "mSharedResultQue = " + mSharedResultQue.toString();
        System.out.println("(Rumtime Ended, " + mSharedRequestQueString + mSharedResultQueString + ")\n");

    }

    public void fetch_command_and_startWorker() {

//        System.out.println("size of mRequestQue is " + mSharedRequestQue.size() + ", size of mResultQue is " + mSharedRequestQue.size() + "\n");
        Command command;
        try {
            command = (Command) mSharedRequestQue.take();

            if (command.getCommand() == 4 || command.getCommand() == 5) {
                runWorker("localWorker", command);
            } else {
                runWorker("networkWorker", command);
            }

        } catch (InterruptedException ex) {
            echo("Interruption Exception occured in fetch_command_and_startWorker() method" + "\n");
        }
    }

    public String runWorker(String inputWorker, Command inputCommand) {

        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<String> future = null;

        if (inputWorker.equals("networkWorker")) {

            future = executor.submit(
                    () -> {
                        NetworkWorker task = new NetworkWorker(mHOST_NAME, mHOST_SERVER_PORT, inputCommand.getmUThreadID(), "" + inputCommand.getCommand());
                        task.run();
                        return task.call();
                    }
            );

        } else if (inputWorker.equals("localWorker")) {
            future = executor.submit(
                    () -> {
                        LocalWorker task = new LocalWorker(mNumberShareResource, mReentrantLock, inputCommand.getmUThreadID(), inputCommand.getCommand());
                        task.run();
                        return task.call();
                    }
            );
        }

        String result = "";

        try {
            result = future.get(1, TimeUnit.MINUTES);

        /*
        "-1" mean InterruptedException
        "-2" mean ExecutionException
        "-3" mean TimeoutException
        "-4" mean NullPointerException
        "-5" mean IOException
        */
            
//            result = simulate_error(result);
        } catch (InterruptedException ex) {
            result = "-1";
            echo(inputWorker + ", " + "InterruptedException occured in runWorker() method" + "\n");
        } catch (ExecutionException ex) {
            result = "-2";
            echo(inputWorker + ", " + "ExecutionException occured in runWorker() method" + "\n");
        } catch (TimeoutException ex) {
            result = "-3";
            echo(inputWorker + ", " + "TimeoutException occured in runWorker() method" + "\n");
        } catch (NullPointerException ex) {
            result = "-4";
            echo(inputWorker + ", " + "NullPointerException occured in runWorker() method" + "\n");
        } finally {
            //|| result.equals("-2")
            inputCommand.setResult(result);
            if (result.equals("0") || result.equals("-1")) {
                System.out.println(inputWorker + " reprocess - coomandId " + inputCommand.getCommandID() + " " + inputCommand.getmUThreadID() + "," + inputCommand.getCommand() + "," + inputCommand.getResult() + "\n");
                runWorker(inputWorker, inputCommand);
            } else {
                put_result_into_mResultQue(inputCommand);
            }
            executor.shutdownNow();
        }
        return result;
    }

    //    public void put_result_into_mResultQue(String inputClientID, String value) {
    public void put_result_into_mResultQue(Command inputCommand) {

        String key = "" + inputCommand.getmUThreadID();
        String value = "" + inputCommand.getResult();
        mRWLock = mSharedRWLock.get(key);
        mLock = mRWLock.writeLock();
        try {
            mLock.lock();

            mSharedResultQue.put(key, value);
//            debugHashMapInsertion(key);

        } finally {
            mLock.unlock();
        }
    }

    public String simulate_error(String result) {
        if (VALUE.getRandomNumberBetween(10, 1) == 3) {
            result = "" + -1;
        }
        return result;
    }

    public void debugHashMapInsertion(String key) {
        System.out.println(
                "RuntimeThr try to add to queue: String " + mSharedResultQue.get(key)
                + ", the result was " + mSharedResultQue.containsKey(key)
                + ", size is " + mSharedResultQue.size()
                + ", map : " + mSharedResultQue.toString()
                + "\n");
    }
}
