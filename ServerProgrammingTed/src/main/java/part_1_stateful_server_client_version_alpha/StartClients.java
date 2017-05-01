package part_1_stateful_server_client_version_alpha;

import part_2_assignment_version_final.object.VALUE;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartClients extends Thread {

    private String HOST_NAME;
    
    public StartClients(String inputHOST_NAME){
        HOST_NAME = inputHOST_NAME;
    }
    
    /**
     * repeat the process in a continuous loop.
     * process - start 100 threads, then sleep for 1000 ms.
     */
    public void run() {

        int id = 0;
        
        int numberOfClients = 1;
//        numberOfClients = 100;

        while (true) {

            for (int i = 0; i < numberOfClients; i++) {

                StatefulClientAlpha client = new StatefulClientAlpha(HOST_NAME, VALUE.SERVER_PORT_NUMBER, ++id);
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

        String HOST_NAME = VALUE.LOCAL_HOST;
//        HOST_NAME = "192.168.1.2";
        StartClients ss = new StartClients(HOST_NAME);
        ss.start();
    }
}
