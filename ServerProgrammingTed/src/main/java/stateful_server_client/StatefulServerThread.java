package stateful_server_client;

import VALUE.VALUE;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatefulServerThread extends Thread {

    private Socket mSocket = null;
    private int mServerID;

    public StatefulServerThread(Socket socket, int id) {
        super("KKMultiServerThread");
        this.mSocket = socket;
        this.mServerID = id;
    }

    public void run() {

        try (
                PrintWriter out = new PrintWriter(mSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                mSocket.getInputStream()));) {
            String inputLine, outputLine;
            StatefulServerProtocol kkp = new StatefulServerProtocol();

            System.out.println("(Stateful Server ID " + mServerID +  "  Started)");

            boolean flag = true;

            do {
                inputLine = in.readLine();
                if (inputLine != "-1") {
                    outputLine = kkp.process(inputLine);
                    out.println(outputLine);
                } else {
                    flag = false;
                    System.out.println("exit the thread");
                }

            } while (flag == true);
            
            System.out.println("(Stateful Server ID " + mServerID +  "  Started)");
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(StatefulServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//end run

    public static void main(String[] args) throws IOException {

        boolean mListeningBoolean = true;

        int id = 0;

        System.out.println("Server Listener started");
        
        try (ServerSocket serverSocket = new ServerSocket(VALUE.SERVER_PORT_NUMBER)) {

            while (mListeningBoolean) {
                new StatefulServerThread(serverSocket.accept(), ++id).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + VALUE.SERVER_PORT_NUMBER);
            System.exit(-1);
        }
    }

}
