package stateful_server_client_version_3;

import VALUE.VALUE;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatefulClientVersion3 extends Thread {

    private String mHostName;
    private int mServerPort;
    private int mClientID;

    public StatefulClientVersion3(String hostName, int serverPort, int ClientID) {
        mHostName = hostName;
        mServerPort = serverPort;
        mClientID = ClientID;
    }

    public void run() {

        System.out.println("(Client id " + mClientID + " started)");

        String[] input = {"1", "1", "1", "1", "1",
            "2", "2", "2", "2", "2",
            "3", "3", "3", "3", "3",
            "4", "-1"};

        int index = -1;

        try (
                //client side
                Socket socket = new Socket(mHostName, mServerPort);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));) {

            String fromServer = "";
            String fromUser = "";

            do {
                System.out.println("Server: " + fromServer + "\n");

                fromUser = input[++index];

                System.out.println("Client " + mClientID + ": " + fromUser);
                out.println(fromUser);

            } while ((fromServer = in.readLine()) != null && fromUser != "-1");

            System.out.println("(Client id " + mClientID + " ended)");

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + mHostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + mHostName);
            System.exit(1);
        }
    }

    public String getUserInput() {
        BufferedReader stdIn
                = new BufferedReader(new InputStreamReader(System.in));
        String result = "";
        try {
            result = stdIn.readLine();
        } catch (IOException ex) {
            Logger.getLogger(StatefulClientVersion3.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static void main(String[] args) {

        for (int i = 0; i < 5; i++) {
            int id = i + 1;
            StatefulClientVersion3 client = new StatefulClientVersion3(VALUE.LOCAL_HOST, VALUE.SERVER_PORT_NUMBER, ++id);
            client.start();
        }

    }

}

/*
reference :
hostName usually is hardcoded in the client. 
It can either be an ip address or a domain name. 
If the server is running the same machine, you can use localhost or 127.0.0.1 as hostname.
http://stackoverflow.com/questions/20020604/java-getting-a-servers-hostname-and-or-ip-address-from-client
 */
