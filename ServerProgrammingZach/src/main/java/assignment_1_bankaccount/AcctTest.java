/*
Ted
AccTest.java
this file run all files to produce the program
*/

package assignment_1_bankaccount;

import java.util.*;

public class AcctTest {

    /*
    NUM_ACC is the quantity of availab accounts to be access
    TURNS is the number of time a thread execute with a given account
    */
    private static final int NUM_ACCT = 500;
    private static final int TURNS = 10000000;

    public static void init(List<BankAccount> accts) {
        for (int k = 0; k < NUM_ACCT; k++) {
            accts.add(new BankAccount("" + (k + 1)));
        }
    }

    public static void main(String a[]) {

            runProgram();
        
    }

    
    /**
     * input : none
     * output :  
     * this method start four threads, two for withdrawing and two for depositing
     */
    private static void runProgram() {
        //initialize a list of accounts and start all threads
        List<BankAccount> accts = new LinkedList<BankAccount>();
        init(accts);
        Random rand = new Random();
        int account_id = rand.nextInt(NUM_ACCT);
        DepositThread one = new DepositThread((BankAccount) accts.get(account_id), TURNS);
        WithdrawThread two = new WithdrawThread((BankAccount) accts.get(account_id), TURNS);
        DepositThread three = new DepositThread((BankAccount) accts.get(account_id), TURNS);
        WithdrawThread four = new WithdrawThread((BankAccount) accts.get(account_id), TURNS);
        one.start();
        two.start();
        three.start();
        four.start();
    }
}
