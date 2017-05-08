package part_2_assignment_version_final;

import part_2_assignment_version_final.object.ClientSharedResource;
import static part_2_assignment_version_final.object.VALUE.echo;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/***************************************************************************
     * LocalWorker Class
     * 
     * This Class contains the information of a LocalWorker Thread. The local 
     * worker thread will handle the clients request of a local command. When a 
     * client wants to get the next Odd or Even number, a LocalWorker thread is
     * created to handle the command.
     **************************************************************************/
public class LocalWorker implements Runnable, Callable<String> {

    private ClientSharedResource mNumber;   //The clients shared resource number
    private ReentrantLock mLock;            //Reentrant lock
    private int mCommand;                   //Command number - 4 or 5
    private int mClientID;                  //Clients ID
    private String mResult;                 //Result of the command

     /**
     *  Default constructor creates an instance of a LocalWorker Thread.
     *  
     *  @param inputNumber    - the hosts shared resource on the local side
     *  @param inputLock      - lock to be passed through from client
     *  @param inputClientID  - the ID of the client passed in
     *  @param command        - The command passed in from the client
    **/
    public LocalWorker(ClientSharedResource inputNumber, ReentrantLock inputLock, int inputClientID, int command) {
        mNumber = inputNumber;
        mLock = inputLock;
        mResult = "0";
        mClientID = inputClientID;
        mCommand = command;
    }

    /**
     *  Thread run - overridden
     *  
     *  This is the overridden method for the implemented thread runnable.\n
     * 
     * This run will start the LocalWorker thread to complete a local Command.
     *  
    **/
    @Override
    public void run() {
        mLock.lock();
        try {
            TimeUnit.MILLISECONDS.sleep(10);
            
            //For a command, call the correct method
            // 4 - get the next even number
            // 5 - get the next odd number
            switch (mCommand) {
                case 4:
                    mResult = "" + mNumber.getNextEvenNumber();
                    break;
                case 5:
                    mResult = "" + mNumber.getNextOddNumber();
                    break;
                default:
                    mResult = "" + "-1";
                    break;
            }
        } catch (InterruptedException ex) {
            echo("InterruptedException occured in LocalWorker" +  mClientID + "," + mCommand + "," + mResult);
        } finally {
            mLock.unlock();
        }
    }

    /**
     * This is the overridden method for the implemented thread runnable.\n
     * 
     * This call will output the client ID, its command, and the result
     *  
    **/
    @Override
    public String call() throws Exception {
        return mClientID + "," + mCommand + "," + mResult;
    }
}
