package stateful_server_client_version_alpha;

import object.SharedResource;
import VALUE.VALUE;
import java.io.IOException;
import java.net.ServerSocket;

public class StatefulServerListenerAlpha extends Thread {

    @Override
    public void run() {
        
        boolean mListeningBoolean = true;

        int id = 0;

        SharedResource sharedResource = new SharedResource();

        System.out.println("Server Listener version 7 started");

        try (ServerSocket serverSocket = new ServerSocket(VALUE.SERVER_PORT_NUMBER)) {
            while (mListeningBoolean) {
                new StatefulServerThreadAlpha(serverSocket.accept(), ++id, sharedResource).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + VALUE.SERVER_PORT_NUMBER);
        }

    }
    
    public static void main(String[] args){
        new StatefulServerListenerAlpha().start();
    }

}
