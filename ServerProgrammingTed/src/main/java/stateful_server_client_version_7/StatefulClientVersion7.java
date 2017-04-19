package stateful_server_client_version_7;

import VALUE.VALUE;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatefulClientVersion7 extends Thread {

    private String mHostName;
    private int mServerPort;
    private int mClientID;
    private Socket mSocket;
    private PrintWriter mOut;
    private BufferedReader mIn;
    private String mFromServer, mFromUser;
    private boolean mFlag;

    public static String[] input = {"hi", "1", "1", "1", "1", "1",
            "2", "2", "2", "2", "2", "3", "3", "3", "3", "3", "4", "-1"};

    public StatefulClientVersion7(String hostName, int serverPort, int ClientID) {
        mHostName = hostName;
        mServerPort = serverPort;
        mClientID = ClientID;
        mFromServer = "";
        mFromUser = "";
        mFlag = true;
        try {
            mSocket = new Socket(mHostName, mServerPort);
            mOut = new PrintWriter(mSocket.getOutputStream(), true);
            mIn = new BufferedReader(
                    new InputStreamReader(mSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(StatefulClientVersion7.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {

        System.out.println("(Client id " + mClientID + " started)" + "\n");
        int index = -1;

        try {
            //the loop will continue when the mFlag is true
            do {
                mFromUser = input[++index];
//                mFromUser = getUserInput();

                //send message to server
                System.out.println("Client " + mClientID + " send    : " + mFromUser + "\n");
                mOut.println(mFromUser);

                //received messge from server
                mFromServer = mIn.readLine();
                System.out.println("Client " + mClientID + " receive : " + mFromServer + "\n");

                //quit the loop when the server or user receive -1
                if (mFromUser.equals("-1") || mFromServer.equals("-1")) {
                    mFlag = false;
                }

            } while (mFlag == true);

            System.out.println("(Client id " + mClientID + " ended)");

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + mHostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + mHostName);
            System.exit(1);
        } finally {
            try {
                //tell server, the client will quit
                mOut.println("-1");
                mSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(StatefulClientVersion7.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getUserInput() {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String result = "";
        try {
            result = stdIn.readLine();
        } catch (IOException ex) {
            Logger.getLogger(StatefulClientVersion7.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static void main(String[] args) {

        int numberOfClients = 1;
        numberOfClients = 10;

        for (int i = 0; i < numberOfClients; i++) {

            int id = i + 1;
            StatefulClientVersion7 client = new StatefulClientVersion7(VALUE.LOCAL_HOST, VALUE.SERVER_PORT_NUMBER, ++id);
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
