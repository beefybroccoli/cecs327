package stateful_server_client_version_1;

import part_2_assignment_version_final.object.VALUE;
import java.io.*;
import java.net.*;

public class StatefulClientVersion1 extends Thread {

    private String mHostName;
    private int mServerPort;
    private int mClientID;

    public StatefulClientVersion1(String hostName, int serverPort, int ClientID) {
        mHostName = hostName;
        mServerPort = serverPort;
        mClientID = ClientID;
    }

    public void run() {

        System.out.println("(Client id " + mClientID + " started)");
        
        try (
                //client side
                Socket socket = new Socket(mHostName, mServerPort);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));) {
            BufferedReader stdIn
                    = new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser = "";

            while ((fromServer = in.readLine()) != "-1" && fromUser != "-1") {
                System.out.println("Server: " + fromServer + "\n");
                if (fromServer.equals("-1")) {
                    break;
                }

                fromUser = stdIn.readLine();
                if (fromUser != "-1") {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                    
                }else{
                    break;
                }
            }
            
            System.out.println("(Client id " + mClientID + " ended)");
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + mHostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + mHostName);
            System.exit(1);
        }
    }

    public static void main(String[] args) {

        int id = 0;
        StatefulClientVersion1 client = new StatefulClientVersion1(VALUE.LOCAL_HOST, VALUE.SERVER_PORT_NUMBER, ++id);

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
