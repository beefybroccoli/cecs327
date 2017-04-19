package stateful_server_client_version_7;

import VALUE.VALUE;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartClients extends Thread {

    public void run() {

        int id = 0;
        
        int numberOfClients = 1;
        numberOfClients = 100;

        while (true) {

            for (int i = 0; i < numberOfClients; i++) {

                StatefulClientVersion7 client = new StatefulClientVersion7(VALUE.LOCAL_HOST, VALUE.SERVER_PORT_NUMBER, ++id);
                client.start();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(StartClients.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {

        StartClients ss = new StartClients();
        ss.start();
    }
}
