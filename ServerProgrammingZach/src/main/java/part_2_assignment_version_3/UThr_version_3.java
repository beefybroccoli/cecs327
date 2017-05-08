/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package part_2_assignment_version_3;

import static part_2_assignment_version_final.object.VALUE.echo;
import com.google.common.util.concurrent.Striped;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 *
 * @author fred
 */
public class UThr_version_3 implements Runnable {

    private int mUThrID;
    private LinkedBlockingQueue mSharedRequestQue;
    private ConcurrentHashMap<String, String> mSharedResultQue;
    private Command_version_3 mCommand;
    private int mCounter;
    private int mMaxCounter;
    private boolean mFlag;
    Striped<ReadWriteLock> mSharedRWLock;
    ReadWriteLock mRWLock;
    Lock mLock;

    public UThr_version_3(int inputID, LinkedBlockingQueue inputRequestQue, ConcurrentHashMap<String, String> inputResultQue, Striped<ReadWriteLock> inputRWLock) {
        mUThrID = inputID;
        mSharedRequestQue = inputRequestQue;
        mSharedResultQue = inputResultQue;
        mCounter = 0;
        mMaxCounter = 20;
        mFlag = true;
        mSharedRWLock = inputRWLock;
        mCommand = new Command_version_3();
    }

    @Override
    public void run() {

        System.out.println("(uThr " + mUThrID + " Started)" + "\n");

        do {
            try {
                if (mCommand.validateResult()) {
                    mCommand = new Command_version_3(++mCounter, mUThrID);
                }
                mSharedRequestQue.put(mCommand);

//                echo("(UThr" + mUThrID + " sleep)\n");
                //sleep while waiting for the result
                while (!mSharedResultQue.containsKey("" + mUThrID) && mFlag) {
                    TimeUnit.MILLISECONDS.sleep(100);
                }
//                echo("(UThr" + mUThrID + " wakeup)\n");

                if (mSharedResultQue.containsKey("" + mUThrID)) {
                    mRWLock = mSharedRWLock.get("" + mUThrID);
                    mLock = mRWLock.writeLock();

                    try {
//                        echo("(UThr" + mUThrID + " try mLock.lock()) + \n");

                        mLock.lock();

                        String result = (String) mSharedResultQue.remove("" + mUThrID);
                        mCommand.setmResult(result);
                        System.out.println("commandID " + mCommand.getCommandID() + " - uThr" + mUThrID + " consume " + mCommand.getmResult() + "\n");
//                        System.out.println("uThr" + mUThrID + " consume " + result + "\n");
//                                + ", after consumption, mMap size: " + mSharedResultQue.size()
//                                + ", map : " + mSharedResultQue.toString()
//                                + "\n");
                    } finally {
                        mLock.unlock();
                    }//end finally
                }//end if

                //stop the thread after 20 commands
                if (mCounter == mMaxCounter) {
                    mFlag = false;
                }

            } catch (InterruptedException e) {
                echo("Interruption occured in uThr" + mUThrID + "\n");
                mFlag = false;
            }

        } while (mFlag);

        System.out.println(
                "(uThr " + mUThrID + " Ended, consumption count = " + this.mCounter + ")" + "\n");

    }//end run method

}
