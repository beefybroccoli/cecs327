package object;

import java.math.BigInteger;
import java.util.Random;

/**
 * generate the next larger random number
 */
public class LargerRandomNumberBigInteger {

    private BigInteger mValue;
    private int mCounter;
    private int mMaxCounter;

    /**
     * initialize a LargerRandomNumber object with seed value of 0
     */
    public LargerRandomNumberBigInteger() {
        mValue = BigInteger.valueOf(0);
        mCounter = 0;
        mMaxCounter = 500;
    }

    private void reset() {
        if (++mCounter == mMaxCounter) {
            mValue = BigInteger.valueOf(1);
            mCounter = 0;
        }
    }

    /**
     *
     * @return the next larger random value
     */
    public BigInteger getNextLargerRand() {
        reset();
        
        Random rn = new Random();
        int randomNumber = rn.nextInt(10000) + 1;
        mValue = mValue.add(BigInteger.valueOf(randomNumber));

        return mValue.abs();
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        test2();
//        test1();
    }

    /**
     * generate a list of random value using LargerRandomNumberBigDecimal object
     */
    public static void test2() {

        LargerRandomNumberBigInteger value = new LargerRandomNumberBigInteger();

        for (int i = 0; i < 1000; i++) {

            System.out.println(i + " value = " + value.getNextLargerRand());
        }
    }

    /**
     * generate a list of random value
     */
    public static void test1() {
        BigInteger value = BigInteger.valueOf(0);
        Random rn = new Random();

        for (int i = 0; i < 10; i++) {
            int randomNumber = rn.nextInt(1000) + 1;
            value = value.add(BigInteger.valueOf(randomNumber));
            System.out.println("value = " + value.abs());
        }
    }
}
