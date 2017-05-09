package part_2_assignment_version_final.object;

import static part_2_assignment_version_final.object.VALUE.echo;
import java.util.concurrent.locks.ReentrantLock;

/**
 *ClientSharedResource is a shared resource used in Runtime and Client threads
 * @author macbook
 */
public class ClientSharedResource {

    private int mEvenNumber;
    private int mOddNumber;
    private ReentrantLock mLock;

    /**
     *construct a ClientSharedResource object
     * @param lock
     */
    public ClientSharedResource(ReentrantLock lock) {
        mEvenNumber = 2;
        mOddNumber = 1;
        mLock = lock;
    }

    /**
     *get the next even number
     * @return int number
     */
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

    /**
     *get the next odd number
     * @return int number
     */
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
