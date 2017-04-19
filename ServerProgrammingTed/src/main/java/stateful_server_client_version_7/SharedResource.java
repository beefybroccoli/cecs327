package stateful_server_client_version_7;

import java.util.concurrent.locks.ReentrantLock;
import object.EvenFibBigDecimalType;
import object.LargerRandomNumber;
import object.PrimeGenerator;

public class SharedResource {

    EvenFibBigDecimalType mEvenFibBigDecimalType;
    LargerRandomNumber mLargerRandomNumber;
    PrimeGenerator mPrimeGenerator;
    private ReentrantLock mSharedLock = new ReentrantLock();

    public SharedResource() {
        mEvenFibBigDecimalType = new EvenFibBigDecimalType();
        mLargerRandomNumber = new LargerRandomNumber();
        mPrimeGenerator = new PrimeGenerator();

    }

    public String getNextEvenFib() {
        String result = "";
        mSharedLock.lock();
        try {
            result = "" + mEvenFibBigDecimalType.getNextEvenFib().abs();
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
