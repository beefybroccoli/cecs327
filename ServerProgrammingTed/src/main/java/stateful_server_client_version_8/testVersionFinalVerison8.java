package stateful_server_client_version_8;

public class testVersionFinalVerison8 {

    public static void main(String[] args) {

        new StatefulServerListenerVerison8().start();
        
        new StartClientsVersion8(VALUE.VALUE.LOCAL_HOST).start();

    }
}
