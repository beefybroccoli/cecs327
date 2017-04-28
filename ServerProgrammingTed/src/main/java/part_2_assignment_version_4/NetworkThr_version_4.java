package part_2_assignment_version_4;

import java.io.*;
import java.net.*;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkThr_version_4 implements Runnable, Callable<String> {

    private String mHostName;
    private int mServerPort;
    private int mClientID;
    private Socket mSocket;
    private PrintWriter mOut;
    private BufferedReader mIn;
    //0 mean unprocessed, -1 mean error, otherwise valid value
    private String mFromServer;
    private String mFromUser;
    private boolean mFlag;

    public NetworkThr_version_4(String hostName, int serverPort, int ClientID, String command) {
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
            Logger.getLogger(NetworkThr_version_4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {

//        System.out.println("(Client id " + mClientID + " started)" + "\n");
        try {

            //send message to server
//            System.out.println("Client " + mClientID + " send    : " + mFromUser + "\n");
            mOut.println(mFromUser);

            //received messge from server
            mFromServer = mIn.readLine();
//            System.out.println("Client " + mClientID + " receive : " + mFromServer + "\n");

            //quit the loop when the server or user receive -1
            if (mFromUser.equals("-1") || mFromServer.equals("-1")) {
                mFlag = false;
            }

//            System.out.println("(Client id " + mClientID + " ended) " + "\n");
        } catch (UnknownHostException e) {
//            System.err.println("Don't know about host " + mHostName);
            System.exit(1);
        } catch (IOException e) {
//            System.err.println("Couldn't get I/O for the connection to " + mHostName);
            System.exit(1);
        } finally {
            try {
                //tell server, the client will quit
                mOut.println("-1");
                mSocket.close();
            } catch (IOException ex) {
//                System.err.println("Couldn't get I/O for the connection to " + mHostName);
            }
        }
    }

    @Override
    public String call() throws Exception {
        String result =  mFromServer;
        String command = mFromUser;
        //return mClientID + "," + mCommand + "," + mResult;
        return mClientID + "," + command + "," + result;
    }

}

/*
reference :
hostName usually is hardcoded in the client. 
It can either be an ip address or a domain name. 
If the server is running the same machine, you can use localhost or 127.0.0.1 as hostname.
http://stackoverflow.com/questions/20020604/java-getting-a-servers-hostname-and-or-ip-address-from-client
 */

 /*
this class create a task, execute and return the result from the server.
 */
