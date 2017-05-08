package part_2_assignment_version_final.object;

import java.util.Random;

public class Command {

    private int mCommandID;
    private int mRequestorID;
    private int mCommand;
    private String mResult;
    private boolean mResultStatus;

    public Command() {
        mResult = "";
    }

    public Command(int commandID, int requestorID) {
        mCommandID = commandID;
        mRequestorID = requestorID;
        mCommand = new Random().nextInt(5) + 1;
        mResult = "";
        mResultStatus = false;
    }

    public int getmUThreadID() {
        return mRequestorID;
    }

    public int getCommandID() {
        return mCommandID;
    }


    public int getCommand() {
        return mCommand;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String inputResult) {
        mResult = inputResult;
    }

    public boolean validateResult() {
        boolean result = true;
        if (mResult.equals("0") || mResult.equals("-1")) {
            result = false;
        }
        return result;
    }


    public void printOut() {
        String result = "mCommandID " + mCommandID + ", mRequestorID "
                + mRequestorID + ", mCommand " + mCommand + ", mResult "
                + mResult + ", mResultStatus " + mResultStatus;
        System.out.println(result);

    }

}
