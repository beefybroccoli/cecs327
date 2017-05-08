package assignment_1_bankaccount;

import java.util.concurrent.locks.ReentrantLock;

public class WithdrawThread extends Thread {

    /*
    mBankAccount is the account the thread can access
    mTurns is amount of turns the thread run through the account
     */
    private BankAccount mBankAccount;
    private int mTurns;

    /**
     * initialize a WithdrawThread
     * @param acct
     * @param turns 
     */
    public WithdrawThread(BankAccount acct, int turns) {
        this.mBankAccount = acct;
        this.mTurns = turns;
        //this.mLock = lock;
    }

    /**
     * run five transaction for withdrawing from the thread
     */
    public void run() {
        for (int k = 0; k < mTurns; k++) {
            mBankAccount.withdraw(5);
        }
        System.out.println("Thread ID" + this.getId());
        System.out.println("Bank Account ID: " + mBankAccount.getID());
        System.out.println("Bank Account Balance: " + mBankAccount.getmBalance());

        System.out.println("");
    }
}
