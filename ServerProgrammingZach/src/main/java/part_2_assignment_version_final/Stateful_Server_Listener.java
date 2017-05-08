package part_2_assignment_version_final;

import part_2_assignment_version_final.object.ServerSharedResource;
import part_2_assignment_version_final.object.VALUE;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;
import java.net.Inet4Address;
import java.net.UnknownHostException;

    /***************************************************************************
     * StatefulServerListener Class
     * 
     * This Class will listen for clients to connect to the server and initialize
     * the server shared resources.
     **************************************************************************/
public class Stateful_Server_Listener extends Thread {

    private boolean mListeningBoolean;  //The server listener boolean
    private int mServerID;              //The server ID

    /**
     * Default constructor creates an instance of the stateful server listener
    **/
    public Stateful_Server_Listener() {
        mListeningBoolean = true;
        mServerID = 0;
    }
    
    /**
     * get server ID method gets the servers ID
    **/
    public int getServerID(){
        return (mServerID == Integer.MAX_VALUE?mServerID = 1:mServerID++);
    }

    /**
     *  This is the overridden method for the implemented thread runnable.
     * 
     * This run will start the servers thread to handle client connections and 
     * requests.
     *  
    **/
    @Override
    public void run() {

        //Server shared resource initialization
        ServerSharedResource sharedResource = new ServerSharedResource();

        //Server listener started
        try {
            System.out.println("(Server Listener started at " + Inet4Address.getLocalHost().getHostAddress() + " )");
        } catch (UnknownHostException ex) {
            System.out.println("UnknownHostException occured in Stateful Server Listener");
        }

        try (ServerSocket serverSocket = new ServerSocket(VALUE.SERVER_PORT_NUMBER)) {

            while (mListeningBoolean) {
                
                //Start the server
                try {
                    new Stateful_Server(serverSocket.accept(), getServerID(), sharedResource).start();
                    TimeUnit.NANOSECONDS.sleep(1);
                } catch (InterruptedException e) {
                    mListeningBoolean = false;
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + VALUE.SERVER_PORT_NUMBER);
        } finally {
            System.out.println("(Server Listener ended)");
        }

    }

    //Main method
    public static void main(String[] args) {
        new Stateful_Server_Listener().start();
    }

}
