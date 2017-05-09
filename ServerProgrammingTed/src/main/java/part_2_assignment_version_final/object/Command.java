package part_2_assignment_version_final.object;

import java.util.Random;

/**
 * Command is an object for passing request to runtime
 *
 * @author macbook
 */
public class Command {

    private int mCommandID;
    private int mRequestorID;
    private int mCommand = new Random().nextInt(5) + 1;
    private String mResult = "";
    private boolean mResultStatus = false;

    /**
     * construct a Command object with specified inputs
     * @param commandID
     * @param requestorID
     */
    public Command(int commandID, int requestorID) {
        mCommandID = commandID;
        mRequestorID = requestorID;
    }

    /**
     * get the requestor ID
     * @return int requestorID
     */
    public int getmUThreadID() {
        return mRequestorID;
    }

    /**
     * get the command ID
     * @return int id
     */
    public int getCommandID() {
        return mCommandID;
    }

    /**
     * get the command, which is "1", "2", "3", "4" or "5"
     * @return int mCommand
     */
    public int getCommand() {
        return mCommand;
    }

    /**
     * get the result from the Command object
     * @return string result
     */
    public String getResult() {
        return mResult;
    }

    /**
     * set the result of the in the command
     * @param inputResult
     */
    public void setResult(String inputResult) {
        mResult = inputResult;
    }

    /**
     * validate the result is correct
     * @return boolean true/false
     */
    public boolean validateResult() {
        boolean result = true;
        if (mResult.equals("0") || mResult.equals("-1")) {
            result = false;
        }
        return result;
    }

    /**
     * print out the command in string
     */
    public void printOut() {
        String result = "mCommandID " + mCommandID + ", mRequestorID "
                + mRequestorID + ", mCommand " + mCommand + ", mResult "
                + mResult + ", mResultStatus " + mResultStatus;
        System.out.println(result);
    }

}
