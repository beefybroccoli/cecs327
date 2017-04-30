package VALUE;

import java.util.Random;

public class VALUE {

    public static final String LOCAL_HOST = "127.0.0.1";
    public static final int SERVER_PORT_NUMBER = 4444;
    public static final int PORT_NUMBER = 5000;
    public static final int BUFFER_SIZE = 1024;
    public static final String HOST_NAME = "www.java2s.com";
    public static final int CLIENT_PORT_NUMBER = 50001;
    public static final int PORT_DAY_TIME = 13; //a well-known daytime port
    public static final int SLEEP_TIME = 1000;
    public static final int POP_3_PORT_NUMBER = 100;

    public static int getPOP_3_PORT_NUMBER() {
        return POP_3_PORT_NUMBER;
    }

    public static int getSLEEP_TIME() {
        return SLEEP_TIME;
    }

    public static int getPORT_DAY_TIME() {
        return PORT_DAY_TIME;
    }

    public static int getCLIENT_PORT_NUMBER() {
        return CLIENT_PORT_NUMBER;
    }

    public static String getHOST_NAME() {
        return HOST_NAME;
    }

    public static int getPORT_NUMBER() {
        return PORT_NUMBER;
    }

    public String getLOCAL_HOST() {
        return LOCAL_HOST;
    }

    public int getSERVER_PORT_NUMBER() {
        return SERVER_PORT_NUMBER;
    }

    public static void echo(String input) {
        System.out.println(input);
    }

    public static int getRandomNumberBetween(int max, int min) {
        Random rn = new Random();
        int answer = rn.nextInt(max) + min;
        return answer;
    }

    public static String getException(String inputErrorCode) {
        /*
        "-1" mean InterruptedException
        "-2" mean ExecutionException
        "-3" mean TimeoutException
        "-4" mean NullPointerException
        "-5" mean IOException
         */

        String result = "";

        switch (inputErrorCode) {

            case "-1":
                result = "InterruptedException";
                break;

            case "-2":
                result = "ExecutionException";
                break;

            case "-3":
                result = "TimeoutException";
                break;

            case "-4":
                result = "NullPointerException";
                break;

            case "-5":
                result = "IOException";
                break;

            default:
                result = "Unknown Error";
        };

        return result;
    }

}
