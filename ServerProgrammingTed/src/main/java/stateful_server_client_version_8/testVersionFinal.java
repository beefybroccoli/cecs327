package stateful_server_client_version_8;

import stateful_server_client_version_alpha.*;

public class testVersionFinal {

    public static void main(String[] args) {

        new StatefulServerListener().start();
        
        new StartClients(VALUE.VALUE.LOCAL_HOST).start();

    }
}
