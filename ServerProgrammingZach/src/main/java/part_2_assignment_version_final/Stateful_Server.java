package part_2_assignment_version_final;

import part_2_assignment_version_final.object.ServerSharedResource;
import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

    /***************************************************************************
     * StatefulServerProtocol Class
     * 
     * This Class contains the process for the server. Whenever a client makes a
     * request, the server protocol decides which process to carry out.
     **************************************************************************/
class StatefulServerProtocol {

    int mServerID;                                  //The server ID
    public int mOtherCounter;                       //Counter
    private ServerSharedResource mSharedResource;   //Server shared resources

    /**
     * Default constructor creates an instance of the Stateful server. The
     * default constructor initializes the class variables
     * 
     * @param inputServerID        - the server id
     * @param inputSharedResource  - the servers shared resource
    **/
    public StatefulServerProtocol(int inputServerID, ServerSharedResource inputSharedResource) {

        mServerID = inputServerID;
        mOtherCounter = 0;
        mSharedResource = inputSharedResource;
    }

    /**
     *  The process method handles which server process to execute based on the 
     * clients command.
     *  
     *  input - which process to carry out
    **/

    public String process(String input) {
        String result = "";

        switch (input) {

            case "-1":
                result += "exit thread";
                break;

            case "1":
                result += mSharedResource.getNextEvenFib();
                break;

            case "2":
                result += mSharedResource.getNextLargerRand();
                break;

            case "3":
                result += mSharedResource.getNextPrime();
                break;

            //return -2 for invalid input
            default:
                ++mOtherCounter;
                result += "-1";
        }
        return result;
    }
}

/***************************************************************************
     * Stateful_Server Class
     * 
     * This Class contains the stateful server class information.
     * 
     * The stateful server will handle connecting clients and their requests.
     **************************************************************************/
public class Stateful_Server extends Thread {

    private Socket mSocket;                     //The TCP stream socket
    private int mServerID;                      // The server ID
    private PrintWriter mOut;                   //Output stream
    private BufferedReader mIn;                 //Input stream
    private StatefulServerProtocol mProtocol;   //The server protocol
    private String mInputLine, mOutputLine;     //Input and output strings
    private boolean mFlag;                      //Simple boolean flag               
    private int mNullCounter;                   //Null counter

    /**
     * Default constructor creates an instance of the Stateful server and 
     * initializes the classes variables.
     *  
     *  @param  socket              - the TCP stream socket
     *  @param id                   - the server id
     *  @param inputSharedResource  - the server shared resource
     * 
    **/
    public Stateful_Server(Socket socket, int id, ServerSharedResource inputSharedResource) {
        mSocket = socket;
        mServerID = id;
        mProtocol = new StatefulServerProtocol(mServerID, inputSharedResource);
        mInputLine = "";
        mOutputLine = "";
        mFlag = true;
        mNullCounter = 0;
        try {
            mOut = new PrintWriter(mSocket.getOutputStream(), true);
            mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        } catch (IOException ex) {
            System.out.println("IOException occured in Stateful Server " + mServerID + "\n");
        }
    }

    /**
     *  This is the overridden method for the implemented thread runnable.
     * 
     * This run will start the servers thread to handle client connections and 
     * requests.
     *  
    **/
    @Override
    public void run() {

        try {
            System.out.println("(Stateful Server ID " + mServerID + "  Started)" + "\n");

            //the server stay alive whenever the mFlag is true
            do {
                //get message from client
                mInputLine = mIn.readLine();
                System.out.println("server " + mServerID + " received " + "\"" + mInputLine + "\"" + "\n");
                mOutputLine = mProtocol.process(mInputLine);

                //respond to user
                mOut.println(mOutputLine);
                mOut.println("server " + mServerID + " respond to " + mOutputLine);

                //count how many times server received message "null"
                if (mInputLine.equals("null")) {
                    mNullCounter++;
                }

                //the server will quit after received message "null" for 5 times
                //the server will quit other messages for 5 times
                //the server will quit when user send "-1"
                if (mNullCounter == 1 || mProtocol.mOtherCounter == 1 || mInputLine.equals("-1")) {
                    mFlag = false;
                }

                //thread sleep for one second
                TimeUnit.MILLISECONDS.sleep(1);
            } while (mFlag == true);

        } catch (IOException ex) {
            System.out.println("IOException occured in Stateful Server " + mServerID + "\n");
        } catch (InterruptedException ex) {
            System.out.println("Producer InterruptedException occured");
            mFlag = false;
        } finally {
            System.out.println("(Stateful Server ID " + mServerID + "  ended)" + "\n");
            try {
                //tell the client that sever will quit
                mOut.println("-1");
                mSocket.close();
            } catch (IOException ex) {
                System.out.println("IOException occured in Stateful Server " + mServerID + "\n");
            }
        }
    }//end run
}
