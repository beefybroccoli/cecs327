/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package part_2_assignment_version_6;

import static part_2_assignment_version_final.object.VALUE.echo;
import java.util.concurrent.locks.ReentrantLock;

public class Number_version_6 {

    private int mEvenNumber;
    private int mOddNumber;
    private ReentrantLock mLock;

    public Number_version_6(ReentrantLock lock) {
        mEvenNumber = 2;
        mOddNumber = 1;
        mLock = lock;
    }

    public int getNextEvenNumber() {
        mLock.lock();
        try {
            mEvenNumber += 2;
            return mEvenNumber;
        } catch (Exception e) {
            echo("Exception occured in getNextEvenNumber() method");
            return -1;
        } finally {
            mLock.unlock();
        }

    }

    public int getNextOddNumber() {
        mLock.lock();
        try {
            mOddNumber += 2;
            return mOddNumber;
        } catch (Exception e) {
            echo("Exception occured in getNextOddNumber() method");
            return -1;
        } finally {
            mLock.unlock();
        }

    }

}
