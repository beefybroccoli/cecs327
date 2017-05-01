
package stateful_server_client_version_5;

import part_2_assignment_version_final.object.VALUE;
import java.io.IOException;
import java.net.ServerSocket;

public class StatefulServerListenerVersion5 {
    
        public static void main(String[] args) throws IOException {

        boolean mListeningBoolean = true;

        int id = 0;

        System.out.println("Server Listener version 5 started");

        try (ServerSocket serverSocket = new ServerSocket(VALUE.SERVER_PORT_NUMBER)) {
            while (mListeningBoolean) {
                new StatefulServerThreadVersion5(serverSocket.accept(), ++id).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + VALUE.SERVER_PORT_NUMBER);
        }
        
    }

}
