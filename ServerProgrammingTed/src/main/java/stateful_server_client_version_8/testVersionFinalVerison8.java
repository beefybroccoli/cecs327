package stateful_server_client_version_8;

public class testVersionFinalVerison8 {

    public static void main(String[] args) {

        new StatefulServerListenerVerison8().start();
        
        new StartClientsVersion8(part_2_assignment_version_final.object.VALUE.LOCAL_HOST).start();

    }
}
