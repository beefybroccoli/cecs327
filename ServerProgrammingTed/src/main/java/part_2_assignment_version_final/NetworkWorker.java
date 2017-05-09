package part_2_assignment_version_final;

import java.io.*;
import java.net.*;
import java.util.concurrent.Callable;

public class NetworkWorker implements Runnable, Callable<String> {

    private String mHostName;
    private int mServerPort;
    private int mClientID;
    private Socket mSocket;
    private PrintWriter mOut;
    private BufferedReader mIn;
    //0 mean unprocessed, -1 mean error, otherwise valid value
    private String mFromServer = "0";
    private String mFromUser;
    private boolean mFlag = true;
    private String mResult;

    public NetworkWorker(String hostName, int serverPort, int ClientID, String command) {
        mHostName = hostName;
        mServerPort = serverPort;
        mClientID = ClientID;
        mFromUser = command;

        try {
            mSocket = new Socket(mHostName, mServerPort);
            mOut = new PrintWriter(mSocket.getOutputStream(), true);
            mIn = new BufferedReader(
                    new InputStreamReader(mSocket.getInputStream()));
        } catch (IOException ex) {
            mResult = "-5";
            System.err.println("IOException occured in Networker " + mClientID + "," + command + "," + mResult + "\n");
        }
    }

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

    @Override
    public String call() throws Exception {
        String command = mFromUser;
        //return mClientID + "," + mCommand + "," + mResult;
        return mClientID + "," + command + "," + mResult;
    }

}
