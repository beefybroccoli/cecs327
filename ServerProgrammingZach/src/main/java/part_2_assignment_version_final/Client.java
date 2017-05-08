package part_2_assignment_version_final;

import part_2_assignment_version_final.object.Command;
import static part_2_assignment_version_final.object.VALUE.echo;
import com.google.common.util.concurrent.Striped;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/***************************************************************************
     * Client Class
     * 
     * This Class contains the information of each client that connects to the 
     * server. Each client consists of a client ID, a Queue for the clients 
     * request, a HashMap for the results of the clients requests, a Command,
     * counter, the maxCounter, flag, and locks used for the shared resources.
     **************************************************************************/
public class Client implements Runnable {

    private int mClientID;   //ID number for the client                               
    private LinkedBlockingQueue mSharedRequestQue;  //The request queue
    private ConcurrentHashMap<String, String> mSharedResultQue; //The result queue
    private Command mCommand;   // Clients command
    private int mCounter;       //Simple counter
    private int mMaxCounter;    //The max amount to be counted
    private boolean mFlag;      // Simple boolean flag
    Striped<ReadWriteLock> mSharedRWLock;   // stripped lock for shared rwlock
    ReadWriteLock mRWLock;                  // ReadWrite lock
    Lock mLock;                             // Basic lock

     /**
     *  Default constructor creates an instance of a client.
     *  
     *  @param inputID - the hosts name
     *  @param inputRequestQue - a queue consisting of client commands in FIFO order
     *  @param inputResultQue  - a queue with the results of the clients commands
     *  @param inputRWLock     - Concurrent lock
    **/
    public Client(int inputID, LinkedBlockingQueue inputRequestQue, 
            ConcurrentHashMap<String, String> inputResultQue, 
            Striped<ReadWriteLock> inputRWLock) {
        mClientID = inputID;
        mSharedRequestQue = inputRequestQue;
        mSharedResultQue = inputResultQue;
        mCounter = 0;
        mMaxCounter = 20;
        mFlag = true;
        mSharedRWLock = inputRWLock;
        mCommand = new Command();
    }

    /**
     *  Thread run - overridden
     *  
     *  This is the overridden method for the thread runnable.\n
     * 
     *  This run will start the clients requests and run till the client completes.
     *  
    **/
    @Override
    public void run() {

        System.out.println("(Client " + mClientID + " Started)" + "\n");

        do {
            try {

                /*
                "-1" mean InterruptedException
                "-2" mean ExecutionException
                "-3" mean TimeoutException
                "-4" mean NullPointerException
                "-5" mean IOException
                 */

                //Create a new command
                mCommand = new Command(++mCounter, mClientID);

                //Put the command in the request queue
                mSharedRequestQue.put(mCommand);

                /*sleep while waiting for the result*/
                while (!mSharedResultQue.containsKey("" + mClientID) && mFlag) {
                    TimeUnit.MILLISECONDS.sleep(100);
                }
                
                //If the resultque contains the clients id
                if (mSharedResultQue.containsKey("" + mClientID)) {
                    
                    //Get the lock and write
                    mRWLock = mSharedRWLock.get("" + mClientID);
                    mLock = mRWLock.writeLock();

                    //Try to lock and if successful, get the results of the command
                    try {
//                        echo("(UThr" + mUThrID + " try mLock.lock()) + \n");
                        mLock.lock();
                        String result = (String) mSharedResultQue.remove("" + mClientID);
                        this.mCommand.setResult(result);
                        echo("commandID " + mCommand.getCommandID() + " - Client" 
                                + mClientID + " consume " + result + "\n");
                    } finally {
                        mLock.unlock();
                    }//end finally
                }//end if

                //stop the thread after mMaxCounter commands
                if (mCounter == mMaxCounter) {
                    mFlag = false;
                }
            //Catch an InterruptedException
            } catch (InterruptedException e) {
                echo("Interruption occured in Client" + mClientID + "\n");
                mFlag = false;
            }

        } while (mFlag);

        //Print out client information once finished
        System.out.println("(Client " + mClientID + " Ended, consumption count = " 
                + this.mCounter + ")" + "\n");

    }//end run method

}
