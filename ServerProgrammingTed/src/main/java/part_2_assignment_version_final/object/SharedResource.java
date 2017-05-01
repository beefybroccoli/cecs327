package part_2_assignment_version_final.object;

import java.util.concurrent.locks.ReentrantLock;

public class SharedResource {

    EvenFibBigInteger mEvenFibBig = new EvenFibBigInteger();
    LargerRandomNumberBigInteger mLargerRandomNumber = new LargerRandomNumberBigInteger();
    PrimeGeneratorBigInteger mPrimeGenerator = new PrimeGeneratorBigInteger();
    private ReentrantLock mSharedLock = new ReentrantLock();

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
