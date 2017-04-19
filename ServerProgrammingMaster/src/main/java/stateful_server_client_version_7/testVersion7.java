package stateful_server_client_version_7;

public class testVersion7 {

    public static void main(String[] args) {

        new StatefulServerListenerVersion7().start();

        new StartClients().start();

    }
}
