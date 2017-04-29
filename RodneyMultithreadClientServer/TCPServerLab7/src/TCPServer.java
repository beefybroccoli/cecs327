
import java.io.*;
import java.net.*;

public class TCPServer {
    static FibRandPrimeProtocol frp = new FibRandPrimeProtocol();
    public static void main(String[] args) throws IOException {
        
        //declare a TCP socket and initialize it to null
        ServerSocket serverSocket = null;
        
        //create the port nuber
        int port = 4445;
        
        try {
            //create the TCP socket server
            serverSocket = new ServerSocket(port);
            System.out.println("server created on port " + port);
        } catch (IOException ex) {
            //will be executed when the server connot be created
            System.out.println("Error: The server with port " + port + " cannot be created");
        }
        
        //starts an endless loop
        while (true) {
            Socket clientSocket = null;
            try {
                //start listening to incoming client request (blocking function)
                System.out.println("Listening...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Error: cannot accept client request. Exiting program");
                return;
            }
            try {
                //create a new thread for each incoming client
                new Thread(new ClientHandler(clientSocket)).start();
            } catch (Exception e) {
                //log exception and go on to next request
            }
            
        }
        

      /*  int portNumber = 4445;
        boolean moreRequests = true;
        FibRandPrimeProtocol frp = new FibRandPrimeProtocol();

        while (moreRequests) {
            try (
                    ServerSocket serverSocket = new ServerSocket(portNumber);
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out
                    = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));) {

                String inputLine, outputLine;

                // Initiate conversation with client
                outputLine = frp.processInput(null);
                out.println(outputLine);

                while ((inputLine = in.readLine()) != null) {

                    outputLine = frp.processInput(inputLine);
                    out.println(outputLine);

                }

            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port "
                        + portNumber + " or listening for a connection");
                System.out.println(e.getMessage());
            }

        } */

    }

}
