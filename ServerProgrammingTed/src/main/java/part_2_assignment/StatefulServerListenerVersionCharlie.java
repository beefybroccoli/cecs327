package part_2_assignment;

import object.SharedResource;
import VALUE.VALUE;
import java.io.IOException;
import java.net.ServerSocket;

public class StatefulServerListenerVersionCharlie extends Thread {

    private boolean mListeningBoolean;

    public StatefulServerListenerVersionCharlie() {
        mListeningBoolean = true;
    }

    @Override
    public void run() {

        int id = 0;

        SharedResource sharedResource = new SharedResource();

        System.out.println("(Server Listener Version Charlie 1 started)");

        try (ServerSocket serverSocket = new ServerSocket(VALUE.SERVER_PORT_NUMBER)) {

            while (mListeningBoolean) {
                
                new StatefulServerThreadVersionCharlie(serverSocket.accept(), ++id, sharedResource).start();

            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + VALUE.SERVER_PORT_NUMBER);
        } finally {
            System.out.println("(Server Listener version beta version 1 ended)");
        }

    }

    public static void main(String[] args) {
        new StatefulServerListenerVersionCharlie().start();
    }

}
