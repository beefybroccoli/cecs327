package part_2_assignment_version_final.object;

import static part_2_assignment_version_final.object.VALUE.echo;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ClientSharedResource is a shared resource used in Runtime and Client threads
 *
 * @author macbook
 */
public class ClientSharedResource {

    private int mEvenNumber = 2;
    private int mOddNumber = 1;
    private ReentrantLock mLock;

    /**
     * construct a ClientSharedResource object
     */
    public ClientSharedResource(ReentrantLock inputLock) {
        mLock = inputLock;
    }

    /**
     * get the next even number
     *
     * @return int number
     */
    public int getNextEvenNumber() {
        int result = -1;
        mLock.lock();
        try {
            mEvenNumber += 2;
            result = mEvenNumber;
        } catch (Exception e) {
            echo("Exception occured in getNextEvenNumber() method");
        } finally {
            mLock.unlock();
        }
        return result;
    }

    /**
     * get the next odd number
     *
     * @return int number
     */
    public int getNextOddNumber() {
        int result = -1;
        mLock.lock();
        try {
            mOddNumber += 2;
            result = mOddNumber;
        } catch (Exception e) {
            echo("Exception occured in getNextOddNumber() method");
        } finally {
            mLock.unlock();
        }
        return result;
    }

}
