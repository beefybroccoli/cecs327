package part_2_assignment_version_final;

import static part_2_assignment_version_final.object.VALUE.echo;
import java.util.concurrent.locks.ReentrantLock;

public class Number {

    private int mEvenNumber;
    private int mOddNumber;
    private ReentrantLock mLock;

    public Number(ReentrantLock lock) {
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
