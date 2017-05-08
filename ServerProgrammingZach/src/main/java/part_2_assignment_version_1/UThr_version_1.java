/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package part_2_assignment_version_1;

import static part_2_assignment_version_final.object.VALUE.echo;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fred
 */
public class UThr_version_1 implements Runnable {

    private int mUThrID;
    private LinkedBlockingQueue mRequestQue, mResultQue;
    private Command_version_1[] commands;
    private int mCounter;

    public UThr_version_1(int inputID, LinkedBlockingQueue inputRequestQue, LinkedBlockingQueue inputResultQue) {
        mUThrID = inputID;
        mRequestQue = inputRequestQue;
        mResultQue = inputResultQue;
        mCounter = 20;
    }

    @Override
    public void run() {

        System.out.println("(uThr " + mUThrID + " Started)");

        commands = new Command_version_1[mCounter];
        for (int i = 0; i < 20; i++) {
            int commandID = i + 1;
            int reqestorID = mUThrID;
            commands[i] = new Command_version_1(commandID, reqestorID);
            try {
                mRequestQue.put(commands[i]);
            } catch (InterruptedException ex) {
                echo("Interruption occured in uThr line 36");
            }
        }

//        try {
//            TimeUnit.MINUTES.sleep(1);
//        } catch (InterruptedException e) {
//            System.out.println("mUThr" + mUThrID + " InterruptedException occured in UThr_version_1 line 59");
//        }
//
//        do {
//
//            if (mResultQue.peek().equals("" + mUThrID)) {
//                try {
//                    System.out.println("mUThr" + mUThrID + " take " + mResultQue.take() + ", after consumption size is " + mResultQue.size());
//                    --mCounter;
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(UThr_version_1.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//
//        } while (mCounter > 0);

        System.out.println("(uThr " + mUThrID + " Ended)");

    }

}

/*
		for (String ssn : input) {
			if (ssn.matches("^(\\d{3}-?\\d{2}-?\\d{4})$")) {
				System.out.println("Found good SSN: " + ssn);
			}
		}
*/
