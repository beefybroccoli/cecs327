package stateful_server_client_version_8;

import VALUE.VALUE;
import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatefulClientVersion8 extends Thread {

    private String mHostName;
    private int mServerPort;
    private int mClientID;
    private Socket mSocket;
    private PrintWriter mOut;
    private BufferedReader mIn;
    private String mFromServer, mFromUser;
    private boolean mWhileFlag;
    private int mConnectionRetryCount;
    private int mMaxConnectionRetryCount = 5;

    public static String[] input = {
        "hi", "1", "1", "1", "1", "1", "2", "2", "2", "2", "2", "3", "3", "3", "3", "3", "4", "-1"};

    public StatefulClientVersion8(String hostName, int serverPort, int ClientID) {
        mHostName = hostName;
        mServerPort = serverPort;
        mClientID = ClientID;
        mFromServer = "";
        mFromUser = "";
        mWhileFlag = true;
        mConnectionRetryCount = 0;
        try {
            System.out.println("(Client id " + mClientID + " started)" + "\n");
            mSocket = new Socket(mHostName, mServerPort);
            mOut = new PrintWriter(mSocket.getOutputStream(), true);
            mIn = new BufferedReader(
                    new InputStreamReader(mSocket.getInputStream()));
        } catch (ConnectException ex) {
            System.out.println("Connection Error in default constructor" + "\n");
        } catch (IOException ex) {
            Logger.getLogger(StatefulClientVersion8.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sleepForAwhile() {
        try {
            long time = 5000;
            System.out.println("Thread ID " + Thread.currentThread().getId() + " sleep for " + time + " miliseconds" + "\n");
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException ex) {
            Logger.getLogger(StatefulClientVersion8.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {

        int index = -1;

        //the loop will continue when the mFlag is true
        try {

            do {

                mFromUser = input[++index];
//                mFromUser = getUserInput();

                //send message to server
                System.out.println("Thread ID " + Thread.currentThread().getId() + " Client " + mClientID + " send    : " + mFromUser + "\n");
                mOut.println(mFromUser);

                //received messge from server
                mFromServer = mIn.readLine();
                System.out.println("Thread ID " + Thread.currentThread().getId() + " Client " + mClientID + " receive : " + mFromServer + "\n");

                //quit the loop when the server or user receive -1
                if (mFromUser.equals("-1") || mFromServer.equals("-1")) {
                    mWhileFlag = false;
                }
            } while (mWhileFlag == true);

        } catch (ConnectException e) {
            System.err.println("Don't know about host " + mHostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + mHostName);
            System.exit(1);
        } catch (NullPointerException ex) {
            System.out.println("Thread ID " + Thread.currentThread().getId() + " Connection Error in run" + "\n");
            /*
                    the client will sleep for a wihle before re-attemp another connection to the server
             */
            if (mConnectionRetryCount <= mMaxConnectionRetryCount) {
                sleepForAwhile();
            } else {
                mWhileFlag = false;
            }
        } finally {

            try {
                //tell server, the client will quit
                mOut.println("-1");

                System.out.println("(Client id " + mClientID + " ended) " + "\n");
                mSocket.close();
            } catch (IOException ex) {
//                Logger.getLogger(StatefulClientVersion8.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public String getUserInput() {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String result = "";
        try {
            result = stdIn.readLine();
        } catch (IOException ex) {
            Logger.getLogger(StatefulClientVersion8.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static void main(String[] args) {

        int numberOfClients = 1;
//        numberOfClients = 5;

        for (int i = 0; i < numberOfClients; i++) {

            int id = i + 1;
            StatefulClientVersion8 client = new StatefulClientVersion8(VALUE.LOCAL_HOST, VALUE.SERVER_PORT_NUMBER, ++id);
            client.start();
        }

    }

}

/*
reference :
hostName usually is hardcoded in the client. 
It can either be an ip address or a domain name. 
If the server is running the same machine, you can use localhost or 127.0.0.1 as hostname.
http://stackoverflow.com/questions/20020604/java-getting-a-servers-hostname-and-or-ip-address-from-client
 */
