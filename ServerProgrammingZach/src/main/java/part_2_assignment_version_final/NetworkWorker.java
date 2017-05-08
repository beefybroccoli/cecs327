package part_2_assignment_version_final;

import java.io.*;
import java.net.*;
import java.util.concurrent.Callable;

/***************************************************************************
     * NetworkWorker Class
     * 
     * This Class contains the information of a NetworkWorker Thread. The network 
     * worker thread will handle the clients request of a server command. When a 
     * client wants to get the next even Fibonacci number, next larger random 
     * number, or the next prime number, a NetworkWorker thread is created to 
     * handle the command.
     **************************************************************************/
public class NetworkWorker implements Runnable, Callable<String> {

    private String mHostName;   //The hosts name
    private int mServerPort;    //The server port number
    private int mClientID;      //The ID of the client
    private Socket mSocket;     //The Socket
    private PrintWriter mOut;   //The print writer output
    private BufferedReader mIn; //The buffered reader input
    //0 mean unprocessed, -1 mean error, otherwise valid value
    private String mFromServer; //Message from server
    private String mFromUser;   //Message from user
    private boolean mFlag;      // Simple boolean flag
    private String mResult;     //The result of the command 

     /**
     * Default constructor creates an instance of a NetworkWorker Thread. The 
     * constructor will create a new socket, input and output for the connecting
     * clients
     *  
     *  @param  hostName    - the hosts name
     *  @param serverPort  - the port of the server connection
     *  @param ClientID    - the ID of the client passed in
     *  @param command     - The command passed in from the client
     * 
    **/
    public NetworkWorker(String hostName, int serverPort, int ClientID, String command) {
        mHostName = hostName;
        mServerPort = serverPort;
        mClientID = ClientID;
        mFromServer = "0";
        mFromUser = command;
        mFlag = true;
        try {
            mSocket = new Socket(mHostName, mServerPort);
            mOut = new PrintWriter(mSocket.getOutputStream(), true);
            mIn = new BufferedReader(
                    new InputStreamReader(mSocket.getInputStream()));
        } catch (IOException ex) {
            mResult = "-5";
             System.err.println("IOException occured in Networker " +  mClientID + "," + command + "," + mResult + "\n");
        }
    }

    @Override
    public void run() {

        try {

            //send message to server
            mOut.println(mFromUser);

            //received messge from server
            mFromServer = mIn.readLine();
            mResult = mFromServer;

            //quit the loop when the server or user receive -1
            if (mFromUser.equals("-1") || mFromServer.equals("-1")) {
                mFlag = false;
            }

            //Catch errors
        } catch (UnknownHostException e) {
            System.err.println("NetworkWorker Don't know about host " + mHostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("NetworkWorker Couldn't get I/O for the connection to " + mHostName);
            System.exit(1);
        } finally {
            try {
                //tell server, the client will quit
                mOut.println("-1");
                mSocket.close();
            } catch (IOException ex) {
                System.err.println("NetworkWorker Couldn't get I/O for the connection to " + mHostName);
            }
        }
    }

    /**
     * This is the overridden method for the implemented thread runnable.\n
     * 
     * This call will output the client ID, its command, and the result
     *  
     * @throws java.lang.Exception
    **/
    @Override
    public String call() throws Exception {
 
        String command = mFromUser;
        //return mClientID + "," + mCommand + "," + mResult;
        return mClientID + "," + command + "," + mResult;
    }

}