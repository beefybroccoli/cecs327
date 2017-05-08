package stateful_server_client_version_beta;

import part_2_assignment_version_final.object.VALUE;
import java.io.*;
import java.net.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

//implements Callable<String>
public class StatefulClientBetaVersion1 extends Thread {

    private String mHostName;
    private int mServerPort;
    private int mClientID;
    private Socket mSocket;
    private PrintWriter mOut;
    private BufferedReader mIn;
    private String mFromServer, mFromUser;
    private boolean mFlag;


    public StatefulClientBetaVersion1(String hostName, int serverPort, int ClientID, String input) {
        mHostName = hostName;
        mServerPort = serverPort;
        mClientID = ClientID;
        mFromServer = "";
        mFromUser = input;
        mFlag = true;
        try {
            mSocket = new Socket(mHostName, mServerPort);
            mOut = new PrintWriter(mSocket.getOutputStream(), true);
            mIn = new BufferedReader(
                    new InputStreamReader(mSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(StatefulClientBetaVersion1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {

        System.out.println("(Client id " + mClientID + " started)" + "\n");
        int index = -1;

        try {

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


            System.out.println("(Client id " + mClientID + " ended) "+ "\n");

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
                Logger.getLogger(StatefulClientBetaVersion1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

//    @Override
//    public String call() throws Exception {
//        return mResult;
//    }

    public static void main(String[] args) throws InterruptedException {

        int id = 1;
        String input = "1";
        StatefulClientBetaVersion1 client = new StatefulClientBetaVersion1(VALUE.LOCAL_HOST, VALUE.SERVER_PORT_NUMBER, id, input);
        client.start();

    }

    public static void test2() throws InterruptedException {
//        int id = 1;
//        String input = "1";
//        StatefulClientBetaVersion1 client = new StatefulClientBetaVersion1(VALUE.LOCAL_HOST, VALUE.SERVER_PORT_NUMBER, id, input);
//        Callable<String> task = client;
//
//        ExecutorService es = Executors.newFixedThreadPool(2);
//        Future<String> future = es.submit(task);
//
//        System.out.println("future done? " + future.isDone());
//
//        try {
//            System.out.println(future.get());
//        } catch (InterruptedException ex) {
//            Logger.getLogger(StatefulClientBetaVersion1.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ExecutionException ex) {
//            Logger.getLogger(StatefulClientBetaVersion1.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        es.awaitTermination(5, TimeUnit.SECONDS);
    }

}

/*
reference :
hostName usually is hardcoded in the client. 
It can either be an ip address or a domain name. 
If the server is running the same machine, you can use localhost or 127.0.0.1 as hostname.
http://stackoverflow.com/questions/20020604/java-getting-a-servers-hostname-and-or-ip-address-from-client
 */
