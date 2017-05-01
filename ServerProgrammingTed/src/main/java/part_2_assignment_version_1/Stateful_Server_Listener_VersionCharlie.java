package part_2_assignment_version_1;

import part_2_assignment_version_final.object.ServerSharedResource;
import part_2_assignment_version_final.object.VALUE;
import java.io.IOException;
import java.net.ServerSocket;

public class Stateful_Server_Listener_VersionCharlie extends Thread {

    private boolean mListeningBoolean;

    public Stateful_Server_Listener_VersionCharlie() {
        mListeningBoolean = true;
    }

    @Override
    public void run() {

        int id = 0;

        ServerSharedResource sharedResource = new ServerSharedResource();

        System.out.println("(Server Listener Version Charlie started)");

        try (ServerSocket serverSocket = new ServerSocket(VALUE.SERVER_PORT_NUMBER)) {

            while (mListeningBoolean) {
                
                new Stateful_Server_VersionCharlie(serverSocket.accept(), ++id, sharedResource).start();

            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + VALUE.SERVER_PORT_NUMBER);
        } finally {
            System.out.println("(Server Listener Version Charlie ended)");
        }

    }

    public static void main(String[] args) {
        new Stateful_Server_Listener_VersionCharlie().start();
    }

}
