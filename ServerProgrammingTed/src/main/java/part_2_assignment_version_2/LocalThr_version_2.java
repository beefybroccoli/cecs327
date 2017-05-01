/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package part_2_assignment_version_2;


import static part_2_assignment_version_final.object.VALUE.echo;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author fred
 */
public class LocalThr_version_2 implements Runnable, Callable<String> {

    private Number_version_2 mNumber;
    private ReentrantLock mLock;
    private int mCommand;
    private int mClientID;
    private String mResult;

    public LocalThr_version_2(Number_version_2 inputNumber, ReentrantLock inputLock, int inputClientID, int command) {
        mNumber = inputNumber;
        mLock = inputLock;
        mResult = "0";
        mClientID = inputClientID;
        mCommand = command;
    }

    @Override
    public void run() {
        mLock.lock();
        try {
            TimeUnit.MILLISECONDS.sleep(10);
            if (mCommand == 4) {
                mResult = "" + mNumber.getNextEvenNumber();
            } else if (mCommand == 5) {
                mResult = "" + mNumber.getNextOddNumber();
            } else {
                mResult = "" + "-1";
            }
        } catch (InterruptedException ex) {
            echo("InterruptedException occured in LocalThr line 47");
        } finally {
            mLock.unlock();
        }
    }

    @Override
    public String call() throws Exception {
        return mClientID + "," + mCommand + "," + mResult;
    }
}
