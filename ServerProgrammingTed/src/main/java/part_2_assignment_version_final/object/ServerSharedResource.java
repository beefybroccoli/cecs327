package part_2_assignment_version_final.object;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ServerShareResource is a shared resource and it is used by StatefulServer class
 */
public class ServerSharedResource {

    EvenFibGenerator mEvenFibBig = new EvenFibGenerator();
    LargerRandomNumberGenerator mLargerRandomNumber = new LargerRandomNumberGenerator();
    PrimeGenerator mPrimeGenerator = new PrimeGenerator();
    private ReentrantLock mSharedLock = new ReentrantLock();

    /**
     * get the next even fib number
     * @return String result
     */
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

    /**
     * get the next larger random number
     * @return String result
     */
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

    /**
     * get the next prime number
     * @return String result
     */
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
