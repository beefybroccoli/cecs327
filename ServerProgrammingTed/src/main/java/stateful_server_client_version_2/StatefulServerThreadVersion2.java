package stateful_server_client_version_2;

import stateful_server_client_version_1.*;
import VALUE.VALUE;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatefulServerThreadVersion2 extends Thread {

    private Socket mSocket = null;
    private int mServerID;

    public StatefulServerThreadVersion2(Socket socket, int id) {
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
            StatefulServerProtocolVersion2 kkp = new StatefulServerProtocolVersion2();

            System.out.println("(Stateful Server ID " + mServerID + "  Started)");

            outputLine = "from server " + mServerID + ", hello client";
            out.println(outputLine);

            boolean flag = true;

            do {
                inputLine = in.readLine();
                System.out.println("server " + mServerID + " received " + "\"" + inputLine + "\"");

                if (inputLine.equals("-1") || inputLine.equals(null) || inputLine.length() == 0) {
                    flag = false;
                } else {
                    outputLine = kkp.process(inputLine);
                    out.println(outputLine);
                }

            } while (flag == true);

            System.out.println("(Stateful Server ID " + mServerID + "  ended)");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(StatefulServerThreadVersion2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//end run

    public static void main(String[] args) throws IOException {

        boolean mListeningBoolean = true;

        int id = 0;

        System.out.println("Server Listener started");

        try (ServerSocket serverSocket = new ServerSocket(VALUE.SERVER_PORT_NUMBER)) {

            while (mListeningBoolean) {
                new StatefulServerThreadVersion2(serverSocket.accept(), ++id).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + VALUE.SERVER_PORT_NUMBER);
            System.exit(-1);
        }
    }

}
