package assignment_1_bankaccount;

import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {

    /*
    mBalance is the balance for each account
    mID is the ID for each account
    mLock is the shared lock
    */
    private double mBalance;
    private String mID;
    private ReentrantLock mSharedLock = new ReentrantLock();

    public BankAccount(String id) {
        mBalance = 0;
        this.mID = id;
    }

    /**
     * get account's balance
     * @return double mBalance
     */
    public double getmBalance() {
        return mBalance;
    }

    /**
     * make a deposit to the account
     * @param double amount 
     */
    public void deposit(double amount) {
        mSharedLock.lock();
        try {
            if (amount >= 0) {
                //System.out.println("deposit : " + amount);
                mBalance += amount;
            }
        } finally {
            mSharedLock.unlock();
        }

    }

    /**
     * acquired locked for the shared resource
     * @return 
     */
    public ReentrantLock getLockObject() {
        return mSharedLock;

    }

    
    /**
     * withdraw an ammount for the account
     * @param double amount
     * @return true for success, false for fail
     */
    public boolean withdraw(double amount) {
        boolean retval = false;

        mSharedLock.lock();
        try {
            if (amount <= mBalance) {
                //System.out.println("withdraw : " + amount);
                mBalance -= amount;
                retval = true;
            }
        } finally {
            mSharedLock.unlock();
        }

        return retval;
    }

    /**
     * get the account's ID
     * @return String mID
     */
    public String getID() {
        return mID;
    }
}
