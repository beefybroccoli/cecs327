package part_2_assignment_version_final.object;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ServerShareResource is a shared resource and it is used by StatefulServer class
 */
public class ServerSharedResource {

    private EvenFibGenerator mEvenFibBig = new EvenFibGenerator();
    private LargerRandomNumberGenerator mLargerRandomNumber = new LargerRandomNumberGenerator();
    private PrimeGenerator mPrimeGenerator = new PrimeGenerator();
    private ReentrantLock mSharedLock = new ReentrantLock();

    /**
     * get the next even fib number
     * @return String result
     */
    public String getNextEvenFib() {
        String result = "";
        mSharedLock.lock();
        try {
            result = mEvenFibBig.getNextEvenFib();
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
            result = mLargerRandomNumber.getNextLargerRand();
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
            result = mPrimeGenerator.getNextPrime();
        } finally {
            mSharedLock.unlock();
        }
        return result;
    }

}
