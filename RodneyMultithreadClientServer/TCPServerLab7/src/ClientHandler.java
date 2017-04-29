import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    
    protected Socket clientSocket = null;
    
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
    public void run() {
        try (
                    
                    PrintWriter out
                    = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));) {

                String inputLine, outputLine;

                // Initiate conversation with client
                outputLine = TCPServer.frp.processInput(null);
                out.println(outputLine);

                while ((inputLine = in.readLine()) != null) {

                    outputLine = TCPServer.frp.processInput(inputLine);
                    out.println(outputLine);

                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        
    }
    
}
