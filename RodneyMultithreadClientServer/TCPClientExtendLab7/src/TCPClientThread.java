import java.io.*;
import java.net.*;
import java.util.*;

public class TCPClientThread extends Thread {
    
    String hostName = "localhost";
    int portNumber = 4445;
    Socket clientSocket = null;
    
    public TCPClientThread() throws IOException {
        this ("TCPClientThread");
    }
    
    public TCPClientThread(String name) throws IOException {
        super(name);
        clientSocket = new Socket(hostName, portNumber);
        
    }
    
    public void run() {
        
        try (
                //Socket clientSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));){
            BufferedReader stdIn
                    = new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;

            fromServer = in.readLine();
            System.out.println("Server: " + fromServer);

            for (int i = 0; i < 5; i++) {
                fromUser = "nextEvenFib";
                System.out.println("Client: " + fromUser);
                out.println(fromUser);

                fromServer = in.readLine();

                System.out.println("Server: " + fromServer);
            }

            for (int i = 0; i < 5; i++) {
                fromUser = "nextPrime";
                System.out.println("Client: " + fromUser);
                out.println(fromUser);

                fromServer = in.readLine();

                System.out.println("Server: " + fromServer);
            }

            for (int i = 0; i < 5; i++) {
                fromUser = "nextLargerRand";
                System.out.println("Client: " + fromUser);
                out.println(fromUser);

                fromServer = in.readLine();

                System.out.println("Server: " + fromServer);
            }

           
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to "
                    + hostName);
            System.exit(1);
        }
        
        
    }
    
}
