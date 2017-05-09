package part_2_assignment_version_final.object;

import java.util.Random;

/**
 * VALUE is a set of reference values, which are used by other classes
 */
public class VALUE {

    /**
     *a reference value for local host
     */
    public static final String LOCAL_HOST = "127.0.0.1";

    /**
     *a reference value for Stateful Server Listenser
     */
    public static final int SERVER_PORT_NUMBER = 4444;

    /**
     * a reference value for a buffer size
     */
    public static final int BUFFER_SIZE = 1024;

    /**
     * a reference value for sleep time
     */
    public static final int SLEEP_TIME = 1000;

    /**
     * get SLEEP TIME reference value
     * @return SLEEP_TIME reference value
     */
    public static int getSLEEP_TIME() {
        return SLEEP_TIME;
    }


    /**
     * get LOCAL HOST reference value
     * @return String Local_Host value
     */
    public String getLOCAL_HOST() {
        return LOCAL_HOST;
    }

    /**
     * get reference SERVER PORT Number
     * @return int Server Port
     */
    public int getSERVER_PORT_NUMBER() {
        return SERVER_PORT_NUMBER;
    }

    /**
     * display a string
     * @param String input
     */
    public static void echo(String input) {
        System.out.println(input);
    }

    /**
     * generate a random number with specified interval
     * @param max
     * @param min
     * @return int result
     */
    public static int getRandomNumberBetween(int max, int min) {
        Random rn = new Random();
        int answer = rn.nextInt(max) + min;
        return answer;
    }

    /**
     * generate an exception message
     * @param inputErrorCode
     * @return String result
     */
    public static String getExceptionMessage(String inputErrorCode) {
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
