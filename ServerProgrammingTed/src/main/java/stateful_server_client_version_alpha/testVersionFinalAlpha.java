package stateful_server_client_version_alpha;

public class testVersionFinalAlpha {

    public static void main(String[] args) {

        new StatefulServerListenerAlpha().start();
        
        new StartClients(VALUE.VALUE.LOCAL_HOST).start();

    }
}
