package stateful_server_client_version_7;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatefulServerThreadVersion7 extends Thread {

    private Socket mSocket;
    private int mServerID;
    private PrintWriter mOut;
    private BufferedReader mIn;
    private StatefulServerProtocolVersion7 mProtocol;
    private String mInputLine, mOutputLine;
    private boolean mFlag;
    private int mNullCounter;

    public StatefulServerThreadVersion7(Socket socket, int id, SharedResource inputSharedResource) {
        mSocket = socket;
        mServerID = id;
        mProtocol = new StatefulServerProtocolVersion7(mServerID, inputSharedResource);
        mInputLine = "";
        mOutputLine = "";
        mFlag = true;
        mNullCounter = 0;
        try {
            mOut = new PrintWriter(mSocket.getOutputStream(), true);
            mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(StatefulServerThreadVersion7.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        try {
            System.out.println("(Stateful Server ID " + mServerID + "  Started)");

            //the server stay alive whenever the mFlag is true
            do {
                //get message from client
                mInputLine = mIn.readLine();
                System.out.println("server " + mServerID + " received " + "\"" + mInputLine + "\"");
                mOutputLine = mProtocol.process(mInputLine);

                //respond to user
                mOut.println("server " + mServerID + " respond to " + mOutputLine);

                //count how many times server received message "null"
                if (mInputLine.equals("null")) {
                    mNullCounter++;
                } 
                
                //the server will quit after received message "null" for 5 times
                //the server will quit other messages for 5 times
                //the server will quit when user send "-1"
                if (mNullCounter == 5 || mProtocol.mOtherCounter == 5 || mInputLine.equals("-1")) {
                    mFlag = false;
                    mOut.println("Server " + mServerID + " end" + "\n");
                }

            } while (mFlag == true);

            System.out.println("(Stateful Server ID " + mServerID + "  ended)" + "\n");

        } catch (IOException ex) {
            Logger.getLogger(StatefulServerThreadVersion7.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //tell the client that sever will quit
                mOut.println("-1");
                mSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(StatefulServerThreadVersion7.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//end run

}
