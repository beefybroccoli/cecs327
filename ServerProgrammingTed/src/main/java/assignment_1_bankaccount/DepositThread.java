/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment_1_bankaccount;


public class DepositThread extends Thread {

    /*
    mBankAccount is the account the thread can access
    mTurns is amount of turns the thread run through the account
    */
    private BankAccount mBankAccount;
    private int mTurns;

    /**
     * initialize DepositThread
     * @param account
     * @param turns 
     */
    public DepositThread(BankAccount account, int turns) {
        this.mBankAccount = account;
        this.mTurns = turns;
    }

    /**
     * deposit five times into an account
     */
    public void run() {
        for (int k = 0; k < mTurns; k++) {
            mBankAccount.deposit(5);
        }
        System.out.println("Thread ID"  + this.getId());
        System.out.println("Bank Account ID: " + mBankAccount.getID());
        System.out.println("Bank Account Balance: " + mBankAccount.getmBalance());
        System.out.println("");
    }
}
