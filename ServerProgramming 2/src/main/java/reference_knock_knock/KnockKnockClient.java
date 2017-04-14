package reference_knock_knock;

import VALUE.VALUE;
import java.io.*;
import java.net.*;

public class KnockKnockClient extends Thread {

    private String mHostName;
    private int mServerPort;

    public KnockKnockClient(String hostName, int serverPort) {
        mHostName = hostName;
        mServerPort = serverPort;
    }

    public void run() {

        try (
                //client side
                Socket socket = new Socket(mHostName, mServerPort);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));) {
            BufferedReader stdIn
                    = new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye.")) {
                    break;
                }

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + mHostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + mHostName);
            System.exit(1);
        }
    }

    public static void main(String[] args) {

        KnockKnockClient client = new KnockKnockClient(VALUE.LOCAL_HOST, VALUE.SERVER_PORT_NUMBER);

        client.start();
    }

}

/*
reference :
hostName usually is hardcoded in the client. 
It can either be an ip address or a domain name. 
If the server is running the same machine, you can use localhost or 127.0.0.1 as hostname.
http://stackoverflow.com/questions/20020604/java-getting-a-servers-hostname-and-or-ip-address-from-client
*/
