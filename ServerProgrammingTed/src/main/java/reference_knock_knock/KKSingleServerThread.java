package reference_knock_knock;

import part_2_assignment_version_final.object.VALUE;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KKSingleServerThread extends Thread {

    private int mPortNumber;

    public KKSingleServerThread(int portNumber) {

            mPortNumber = portNumber;
    }

    public void run() {
        System.out.println("(server started)");
        
        try (
                ServerSocket serverSocket = new ServerSocket(mPortNumber);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));) {

            String inputLine, outputLine;

            // Initiate conversation with client
            KnockKnockProtocol kkp = new KnockKnockProtocol();
            outputLine = kkp.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                System.out.println("(server start while loop)");
                outputLine = kkp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye.")) {
                    break;
                }
                System.out.println("(server end while loop)");
            }
            
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + mPortNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
        
        System.out.println("(server ended)");
    }
    
    public static void main (String[] args) {

        KKSingleServerThread server = new KKSingleServerThread(VALUE.SERVER_PORT_NUMBER);
        server.start();
    }
    
}
