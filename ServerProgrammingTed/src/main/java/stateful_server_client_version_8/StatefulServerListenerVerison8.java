package stateful_server_client_version_8;

import part_2_assignment_version_final.object.ServerSharedResource;
import part_2_assignment_version_final.object.VALUE;
import java.io.IOException;
import java.lang.management.RuntimeMXBean;
import java.net.ServerSocket;

public class StatefulServerListenerVerison8 extends Thread {

    @Override
    public void run() {
        
        boolean mListeningBoolean = true;

        int id = 0;

        ServerSharedResource sharedResource = new ServerSharedResource();

        System.out.println("Server Listener version 8 started, process id " + RuntimeMXBean.class.toString());

        try (ServerSocket serverSocket = new ServerSocket(VALUE.SERVER_PORT_NUMBER)) {
            while (mListeningBoolean) {
                new StatefulServerThreadVerison8(serverSocket.accept(), ++id, sharedResource).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + VALUE.SERVER_PORT_NUMBER);
        }

    }
    
    public static void main(String[] args){
        new StatefulServerListenerVerison8().start();
    }

}
