package part_2_assignment_version_4;

import part_2_assignment_version_final.object.SharedResource;
import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

class StatefulServerProtocolBetaVersion1 {

    int mServerID;
    public int mOtherCounter;
    private SharedResource mSharedResource;

    public StatefulServerProtocolBetaVersion1(int inputServerID, SharedResource inputSharedResource) {

        mServerID = inputServerID;
        mOtherCounter = 0;
        mSharedResource = inputSharedResource;
    }

    public String process(String input) {
//        String result = input + " - ";
        String result = "";

        switch (input) {
            case "hi":
                result += "hello from server " + mServerID;
                break;

            case "-1":
                result += "exit thread";
                break;

            case "1":
//                result += "Even Fib Big Decimal " + mSharedResource.getNextEvenFib();
                result += mSharedResource.getNextEvenFib();
                break;

            case "2":
//                result += "Larger RandomNumber " + mSharedResource.getNextLargerRand();
                result += mSharedResource.getNextLargerRand();
                break;

            case "3":
//                result += "Prime Number " + mSharedResource.getNextPrime();
                result += mSharedResource.getNextPrime();
                break;

            //return -2 for invalid input
            default:
                ++mOtherCounter;
//                result += "invalid input, 1 - Even Fib Big Decimal, 2 - Larger RandomNumber, 3 - Prime Numbe, -1 for exit";
                result += "-2";
        }
        return result;
    }
}

public class Stateful_Server_VersionCharlie extends Thread {

    private Socket mSocket;
    private int mServerID;
    private PrintWriter mOut;
    private BufferedReader mIn;
    private StatefulServerProtocolBetaVersion1 mProtocol;
    private String mInputLine, mOutputLine;
    private boolean mFlag;
    private int mNullCounter;

    public Stateful_Server_VersionCharlie(Socket socket, int id, SharedResource inputSharedResource) {
        mSocket = socket;
        mServerID = id;
        mProtocol = new StatefulServerProtocolBetaVersion1(mServerID, inputSharedResource);
        mInputLine = "";
        mOutputLine = "";
        mFlag = true;
        mNullCounter = 0;
        try {
            mOut = new PrintWriter(mSocket.getOutputStream(), true);
            mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(Stateful_Server_VersionCharlie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
                mOut.println(mOutputLine);
                mOut.println("server " + mServerID + " respond to " + mOutputLine);
//                System.out.println("\n");

                //count how many times server received message "null"
                if (mInputLine.equals("null")) {
                    mNullCounter++;
                }

                //the server will quit after received message "null" for 5 times
                //the server will quit other messages for 5 times
                //the server will quit when user send "-1"
                if (mNullCounter == 1 || mProtocol.mOtherCounter == 1 || mInputLine.equals("-1")) {
                    mFlag = false;
//                    mOut.println("Server " + mServerID + " end");
//                    System.out.println("\n");
                }

                //thread sleep for one second
                TimeUnit.MILLISECONDS.sleep(1);
            } while (mFlag == true);

           

        } catch (IOException ex) {
            Logger.getLogger(Stateful_Server_VersionCharlie.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            System.out.println("Producer InterruptedException occured");
            mFlag = false;
        } finally {
             System.out.println("(Stateful Server ID " + mServerID + "  ended)" + "\n");
            try {
                //tell the client that sever will quit
                mOut.println("-1");
                mSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(Stateful_Server_VersionCharlie.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//end run


}
