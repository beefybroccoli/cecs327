package part_2_assignment_version_final.object;

import java.util.concurrent.locks.ReentrantLock;
import part_2_assignment_version_final.object.EvenFibBigInteger;
import part_2_assignment_version_final.object.LargerRandomNumberBigInteger;
import part_2_assignment_version_final.object.PrimeGeneratorBigInteger;

public class SharedResource {

    EvenFibBigInteger mEvenFibBig = new EvenFibBigInteger();
    LargerRandomNumberBigInteger mLargerRandomNumber = new LargerRandomNumberBigInteger();
    PrimeGeneratorBigInteger mPrimeGenerator = new PrimeGeneratorBigInteger();
    private ReentrantLock mSharedLock = new ReentrantLock();

    public SharedResource() {

    }

    public String getNextEvenFib() {
        String result = "";
        mSharedLock.lock();
        try {
            result = "" + mEvenFibBig.getNextEvenFib().abs();
        } finally {
            mSharedLock.unlock();
        }

        return result;
    }

    public String getNextLargerRand() {
        String result = "";
        mSharedLock.lock();
        try {
            result = "" + mLargerRandomNumber.getNextLargerRand().abs();
        } finally {
            mSharedLock.unlock();
        }

        return result;
    }

    public String getNextPrime() {
        String result = "";
        mSharedLock.lock();
        try {
            result = "" + mPrimeGenerator.getNextPrime().abs();
        } finally {
            mSharedLock.unlock();
        }

        return result;
    }

}
