
package stateful_server_client_version_6;

import VALUE.VALUE;
import java.io.IOException;
import java.net.ServerSocket;

public class StatefulServerListenerVersion6 {
    
        public static void main(String[] args) throws IOException {

        boolean mListeningBoolean = true;

        int id = 0;

        System.out.println("Server Listener version 6 started");

        try (ServerSocket serverSocket = new ServerSocket(VALUE.SERVER_PORT_NUMBER)) {
            while (mListeningBoolean) {
                new StatefulServerThreadVersion6(serverSocket.accept(), ++id).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + VALUE.SERVER_PORT_NUMBER);
        }
        
    }

}
