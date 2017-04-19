package object;

import java.math.BigInteger;

/**
 * calculate the next even fib
 */
public class EvenFibBigInteger {

    private BigInteger valueNMinus2;
    private BigInteger valueNMinus1;
    private BigInteger valueN;
    private int mCounter;
    private int mMaxCounter;

    /**
     * initialize EvenFibBigDecimalType with seed 0 and 1
     */
    public EvenFibBigInteger() {
        valueNMinus2 = new BigInteger("0");
        valueNMinus1 = new BigInteger("1");
        valueN = new BigInteger("-1");
        mCounter = 0;
        mMaxCounter = 100;
    }

    /**
     * get the next even Fib number
     *
     * @return even Fib in BigDecimal type
     */
    public BigInteger getNextEvenFib() {
        BigInteger result;
        do {
            result = getNextFib();

        } while (evenNumber(result) == false);

        return result;
    }

    private void reset() {
        if (++mCounter == mMaxCounter) {
            valueNMinus2 = new BigInteger("0");
            valueNMinus1 = new BigInteger("1");
            valueN = new BigInteger("-1");;
            mCounter = 0;
        }
    }

    /**
     * get the next Fib number
     *
     * @return fib number in BigDecimal type
     */
    public BigInteger getNextFib() {

        reset();
        
        if (valueN.equals("-1")) {
            valueN = valueNMinus2.add(valueNMinus1);
        } else {

            valueNMinus2 = valueNMinus1;
            valueNMinus1 = valueN;
            valueN = valueNMinus2.add(valueNMinus1);
        }

        return valueN;
    }

    /**
     * test if an input is even number or not
     *
     * @param input
     * @return true if input is an even number
     */
    public boolean evenNumber(BigInteger input) {
        return ((input.remainder(BigInteger.valueOf(2))).equals(BigInteger.valueOf(0)) ? (true) : (false));
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        test3();
//        test2();
    }

    /**
     * test if the object generate a sequence of even fib numbers correctly
     */
    public static void test3() {
        EvenFibBigInteger obj = new EvenFibBigInteger();

        for (int i = 0; i < 105; i++) {
            BigInteger result = obj.getNextEvenFib();
            System.out.println("even Fib_" + i + " = " + result.abs());
        }
    }

    /**
     * test if the object generate a sequence of fib numbers correctly
     */
    public static void test2() {
        EvenFibBigInteger obj = new EvenFibBigInteger();

        for (int i = 0; i < 105; i++) {
            BigInteger result = obj.getNextFib();
            System.out.println("Fib_" + i + " = " + result.abs());
        }
    }

}
