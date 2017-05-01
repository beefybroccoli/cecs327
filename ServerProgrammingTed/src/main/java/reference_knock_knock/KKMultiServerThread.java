package reference_knock_knock;

import part_2_assignment_version_final.object.VALUE;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KKMultiServerThread extends Thread {

    private Socket mSocket = null;

    public KKMultiServerThread(Socket socket) {
        super("KKMultiServerThread");
        this.mSocket = socket;
    }

    public void run() {

        try (
                PrintWriter out = new PrintWriter(mSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                mSocket.getInputStream()));) {
            String inputLine, outputLine;
            KnockKnockProtocol kkp = new KnockKnockProtocol();
            outputLine = kkp.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                outputLine = kkp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye")) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(KKMultiServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//end run

    public static void main(String[] args) throws IOException {

        boolean mListeningBoolean = true;

        try (ServerSocket serverSocket = new ServerSocket(VALUE.SERVER_PORT_NUMBER)) {
            while (mListeningBoolean) {
                new KKMultiServerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + VALUE.SERVER_PORT_NUMBER);
            System.exit(-1);
        }
    }

}
