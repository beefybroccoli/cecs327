package stateful_server_client_version_beta;

import part_2_assignment_version_final.object.SharedResource;
import part_2_assignment_version_final.object.VALUE;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

public class StatefulServerListenerBeta extends Thread {

    private boolean mListeningBoolean;

    public StatefulServerListenerBeta() {
        mListeningBoolean = true;
    }

    @Override
    public void run() {

        int id = 0;

        SharedResource sharedResource = new SharedResource();

        System.out.println("(Server Listener version beta version 1 started)");

        try (ServerSocket serverSocket = new ServerSocket(VALUE.SERVER_PORT_NUMBER)) {

            while (mListeningBoolean) {
                
                new StatefulServerThreadBetaVersion1(serverSocket.accept(), ++id, sharedResource).start();

            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + VALUE.SERVER_PORT_NUMBER);
        } finally {
            System.out.println("(Server Listener version beta version 1 ended)");
        }

    }

    public static void main(String[] args) {
        new StatefulServerListenerBeta().start();
    }

}
