package part_2_assignment_version_final;

import part_2_assignment_version_final.object.Command;
import static part_2_assignment_version_final.object.VALUE.echo;
import com.google.common.util.concurrent.Striped;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;


public class Client implements Runnable {

    private int mClientID;
    private LinkedBlockingQueue mSharedRequestQue;
    private ConcurrentHashMap<String, String> mSharedResultQue;
    private Command mCommand;
    private int mCounter;
    private int mMaxCounter;
    private boolean mFlag;
    Striped<ReadWriteLock> mSharedRWLock;
    ReadWriteLock mRWLock;
    Lock mLock;

    public Client(int inputID, LinkedBlockingQueue inputRequestQue, ConcurrentHashMap<String, String> inputResultQue, Striped<ReadWriteLock> inputRWLock) {
        mClientID = inputID;
        mSharedRequestQue = inputRequestQue;
        mSharedResultQue = inputResultQue;
        mCounter = 0;
        mMaxCounter = 20;
        mFlag = true;
        mSharedRWLock = inputRWLock;
        mCommand = new Command();
    }

    @Override
    public void run() {

        System.out.println("(Client " + mClientID + " Started)" + "\n");

        do {
            try {

                /*
                "-1" mean InterruptedException
                "-2" mean ExecutionException
                "-3" mean TimeoutException
                "-4" mean NullPointerException
                "-5" mean IOException
                 */

                mCommand = new Command(++mCounter, mClientID);

                mSharedRequestQue.put(mCommand);

                /*sleep while waiting for the result*/
                while (!mSharedResultQue.containsKey("" + mClientID) && mFlag) {
                    TimeUnit.MILLISECONDS.sleep(100);
                }

                if (mSharedResultQue.containsKey("" + mClientID)) {
                    mRWLock = mSharedRWLock.get("" + mClientID);
                    mLock = mRWLock.writeLock();

                    try {
//                        echo("(UThr" + mUThrID + " try mLock.lock()) + \n");
                        mLock.lock();
                        String result = (String) mSharedResultQue.remove("" + mClientID);
                        this.mCommand.setResult(result);
                        echo("commandID " + mCommand.getCommandID() + " - Client" + mClientID + " consume " + result + "\n");
                    } finally {
                        mLock.unlock();
                    }//end finally
                }//end if

                //stop the thread after 20 commands
                if (mCounter == mMaxCounter) {
                    mFlag = false;
                }

            } catch (InterruptedException e) {
                echo("Interruption occured in Client" + mClientID + "\n");
                mFlag = false;
            }

        } while (mFlag);

        System.out.println("(Client " + mClientID + " Ended, consumption count = " + this.mCounter + ")" + "\n");

    }//end run method

}
