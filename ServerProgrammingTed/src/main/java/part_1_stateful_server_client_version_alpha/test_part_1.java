package part_1_stateful_server_client_version_alpha;

public class test_part_1 {

    public static void main(String[] args) {

        new StatefulServerListenerAlpha().start();
        
        new StartClients(VALUE.VALUE.LOCAL_HOST).start();

    }
}
