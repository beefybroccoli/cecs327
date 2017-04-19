
package stateful_server_client_version_7;

import VALUE.VALUE;
import java.io.IOException;
import java.net.ServerSocket;

public class StatefulServerListenerVersion7 {
    
        public static void main(String[] args) throws IOException {

        boolean mListeningBoolean = true;

        int id = 0;
        
        SharedResource sharedResource = new SharedResource();

        System.out.println("Server Listener version 7 started");

        try (ServerSocket serverSocket = new ServerSocket(VALUE.SERVER_PORT_NUMBER)) {
            while (mListeningBoolean) {
                new StatefulServerThreadVersion7(serverSocket.accept(), ++id, sharedResource).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + VALUE.SERVER_PORT_NUMBER);
        }
        
    }

}
