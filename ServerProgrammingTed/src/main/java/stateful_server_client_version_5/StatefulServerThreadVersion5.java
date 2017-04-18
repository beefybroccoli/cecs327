package stateful_server_client_version_5;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatefulServerThreadVersion5 extends Thread {

    private Socket mSocket;
    private int mServerID;
    private PrintWriter mOut;
    private BufferedReader mIn;
    private StatefulServerProtocolVersion5 mProtocol;
    private String mInputLine, mOutputLine;
    private boolean mFlag;
    private int mNullCounter;

    public StatefulServerThreadVersion5(Socket socket, int id) {
        mSocket = socket;
        mServerID = id;
        mProtocol = new StatefulServerProtocolVersion5(mServerID);
        mInputLine = "";
        mOutputLine = "";
        mFlag = true;
        mNullCounter = 0;
        try {
            mOut = new PrintWriter(mSocket.getOutputStream(), true);
            mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(StatefulServerThreadVersion5.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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

                //the server will quit after received message "null" 5 times
                if (mInputLine.equals("null")) {
                    mFlag = (++mNullCounter == 5);
                }

                //the server will quit when user send "-1"
                if (mInputLine.equals("-1")) {
                    mFlag = false;
                    mOut.println("Server " + mServerID + " end");
                }

            } while (mFlag == true);

            System.out.println("(Stateful Server ID " + mServerID + "  ended)");

        } catch (IOException ex) {
            Logger.getLogger(StatefulServerThreadVersion5.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //tell the client that sever will quit
                mOut.println("-1");
                mSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(StatefulServerThreadVersion5.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//end run

}
