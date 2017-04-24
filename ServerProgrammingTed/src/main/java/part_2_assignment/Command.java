/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package part_2_assignment;

import java.util.Random;

/**
 *
 * @author fred
 */
public class Command {

    private int mCommandID;
    private int mRequestorID;
    private int mCommand;
    private String mResult;
    private boolean mResultStatus;

    public Command(int commandID, int requestorID) {
        mCommandID = commandID;
        mRequestorID = requestorID;
        mCommand = new Random().nextInt(5) + 1;
        mResult = "0";
        mResultStatus = false;
    }

    public int getmRequestorID() {
        return mRequestorID;
    }

    public int getCommandID() {
        return mCommandID;
    }

    public Integer getmID() {
        return mCommandID;
    }

    public int getmCommand() {
        return mCommand;
    }

    public String getmResult() {
        mResultStatus = true;
        return mResult;
    }

    public void setmResult(String mResult) {
        
        if(!mResult.equals("0")  && !mResult.equals("-1") ){
            this.mResult = mResult;
            this.mResultStatus = true;
        }
    }

    public boolean ismResultStatus() {
        return mResultStatus;
    }
    
    public void printOut(){
        String result = "mCommandID " + mCommandID + ", mRequestorID " 
                + mRequestorID +", mCommand " + mCommand + ", mResult " 
                + mResult + ", mResultStatus " + mResultStatus ;
        System.out.println(result);
        
    }
    
    public static void main(String[] args){
        Command[] commands = new Command[10];
        
        for ( int i = 0; i < 10; i++){
            int commandID = i+1;
            int reqestorID = 1;
            commands[i] = new Command(commandID, reqestorID);
            commands[i].setmResult("" + (new Random().nextInt(5) + -1));
            commands[i].printOut();
        }
    }

}
