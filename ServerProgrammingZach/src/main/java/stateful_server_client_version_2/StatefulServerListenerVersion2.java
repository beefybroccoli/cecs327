
package stateful_server_client_version_2;

import part_2_assignment_version_final.object.VALUE;
import java.io.IOException;
import java.net.ServerSocket;

public class StatefulServerListenerVersion2 {
    
        public static void main(String[] args) throws IOException {

        boolean mListeningBoolean = true;

        int id = 0;

        System.out.println("Server Listener version 2 started");

        try (ServerSocket serverSocket = new ServerSocket(VALUE.SERVER_PORT_NUMBER + 2)) {

            while (mListeningBoolean) {
                new StatefulServerThreadVersion2(serverSocket.accept(), ++id).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + VALUE.SERVER_PORT_NUMBER);
            System.exit(-1);
        }
    }

}
