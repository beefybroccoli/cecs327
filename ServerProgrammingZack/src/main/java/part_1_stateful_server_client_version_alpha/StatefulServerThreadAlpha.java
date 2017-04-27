package part_1_stateful_server_client_version_alpha;

import object.SharedResource;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/***************************************************************************
     * StatefulServerThreadAlpha Class
     * 
     * This Class contains the server thread. Here the server will remain open 
     * waiting for a client request and fulfill a clients request based on the
     * client.
     **************************************************************************/
public class StatefulServerThreadAlpha extends Thread {

    private Socket mSocket;     //Socket
    private int mServerID;      //Id of the server
    private PrintWriter mOut;   //Output stream
    private BufferedReader mIn; //Input stream
    private StatefulServerProtocolAlpha mProtocol;  //Server protocol class
    private String mInputLine, mOutputLine;     //Input and output string
    private boolean mFlag;                      //Flag
    private int mNullCounter;                   //Simple counter

     /**
     *  Default Constructor
     *  
     *  Default constructor creates an instance of a server.
     *  
     *  socket - the socket
     *  id - id of the server
     *  inputSharedResource - the shared resource on the server 
    **/
    public StatefulServerThreadAlpha(Socket socket, int id, SharedResource inputSharedResource) {
        mSocket = socket;
        mServerID = id;
        mProtocol = new StatefulServerProtocolAlpha(mServerID, inputSharedResource);
        mInputLine = "";
        mOutputLine = "";
        mFlag = true;
        mNullCounter = 0;
        try {
            mOut = new PrintWriter(mSocket.getOutputStream(), true);
            mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(StatefulServerThreadAlpha.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

      /**
     *  Run method
     *  
     *  Once a server is created, wait for a client to request and respond with 
     *  the appropriate response.
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
                mOut.println("server " + mServerID + " respond to " + mOutputLine);
//                System.out.println("\n");

                //count how many times server received message "null"
                if (mInputLine.equals("null")) {
                    mNullCounter++;
                } 
                
                //the server will quit after received message "null" for 5 times
                //the server will quit other messages for 5 times
                //the server will quit when user send "-1"
                if (mNullCounter == 5 || mProtocol.mOtherCounter == 5 || mInputLine.equals("-1")) {
                    mFlag = false;
                    mOut.println("Server " + mServerID + " end");
//                    System.out.println("\n");
                }

            } while (mFlag == true);

            System.out.println("(Stateful Server ID " + mServerID + "  ended)" + "\n");

        } catch (IOException ex) {
            Logger.getLogger(StatefulServerThreadAlpha.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //tell the client that sever will quit
                mOut.println("-1");
                mSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(StatefulServerThreadAlpha.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//end run

}
