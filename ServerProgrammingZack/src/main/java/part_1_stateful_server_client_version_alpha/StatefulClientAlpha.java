package part_1_stateful_server_client_version_alpha;

import VALUE.VALUE;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

    /***************************************************************************
     * StatefulClientAlpha Class
     * 
     * This Class creates an instance of a client using threads to control 
     * each client. The thread running process will loop through a predefined
     * input for the client to send to the server. The string mInput contains
     * all of the instructions the client wants from the server as provided by
     * the assignment guidelines. 
     **************************************************************************/
public class StatefulClientAlpha extends Thread {

    private String mHostName;   //Host name
    private int mServerPort;    //Server port ID
    private int mClientID;      //Client ID
    private Socket mSocket;     //DatagramSocket
    private PrintWriter mOut;   //Output stream
    private BufferedReader mIn; //Input stream
    private String mFromServer, mFromUser;  //Message to send/recieve
    private boolean mFlag;      //Flag to break out of loop

    //Client input commands
    public static String[] mInput = {
        "hi", "1", "1", "1", "1", "1", "2", "2", "2", "2", "2",
        "3", "3", "3", "3", "3", "4", "-1"};

    /**
     *  Default Constructor
     *  
     *  Default constructor creates an instance of a client and automatically
     *  sends messages to the server.
     *  
     *  hostName - the hosts name
     *  serverPort - the ports server
     *  ClientID - the clients ID number
    **/
    public StatefulClientAlpha(String hostName, int serverPort, int ClientID) {
        mHostName = hostName;
        mServerPort = serverPort;
        mClientID = ClientID;
        mFromServer = "";
        mFromUser = "";
        mFlag = true;
        
        //Try to send and recieve message to server
        try {
            mSocket = new Socket(mHostName, mServerPort);
            mOut = new PrintWriter(mSocket.getOutputStream(), true);
            mIn = new BufferedReader(
                    new InputStreamReader(mSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(StatefulClientAlpha.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *  Run method
     *  
     *  Once a client is created and starts, automatically executes sending 
     *  messages to the server. Once sent, the server will send a message back 
     *  for the client to recieve.
     *  
    **/
    public void run() {

        System.out.println("(Client id " + mClientID + " started)" + "\n");
        int index = -1;

        try {
            //the loop will continue when the mFlag is true
            do {
                mFromUser = mInput[++index];
//                mFromUser = getUserInput();

                //send message to server
                System.out.println("Client " + mClientID + " send    : " 
                                    + mFromUser + "\n");
                mOut.println(mFromUser);

                //received messge from server
                mFromServer = mIn.readLine();
                System.out.println("Client " + mClientID + " receive : " 
                                    + mFromServer + "\n");

                //quit the loop when the server or user receive -1
                if (mFromUser.equals("-1") || mFromServer.equals("-1")) {
                    mFlag = false;
                }

            } while (mFlag == true);

            System.out.println("(Client id " + mClientID + " ended) " + "\n");

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
                Logger.getLogger(StatefulClientAlpha.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *  getUserInput method
     *  
     *  Returns the users input from the console.
     *  
    **/
    public String getUserInput() {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String result = "";
        try {
            result = stdIn.readLine();
        } catch (IOException ex) {
            Logger.getLogger(StatefulClientAlpha.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     *  Main method
     *  
     *  Creates a number of clients.
     *  
    **/
    public static void main(String[] args) {

        int numberOfClients = 1;
        numberOfClients = 5;

        for (int i = 0; i < numberOfClients; i++) {

            int id = i + 1;
            StatefulClientAlpha client = 
                    new StatefulClientAlpha(VALUE.LOCAL_HOST, VALUE.SERVER_PORT_NUMBER, ++id);
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
