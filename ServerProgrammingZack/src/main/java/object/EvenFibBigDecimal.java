package object;

import java.math.BigDecimal;

/**
 *calculate the next even fib
 */

public class EvenFibBigDecimal {

    private BigDecimal valueNMinus2;
    private BigDecimal valueNMinus1;
    private BigDecimal valueN;

    /**
     *initialize EvenFibBigDecimalType with seed 0 and 1
     */
    public EvenFibBigDecimal() {
        valueNMinus2 = new BigDecimal("0");
        valueNMinus1 = new BigDecimal("1");
        valueN = new BigDecimal("-1");
    }

    /**
     *get the next even Fib number
     * @return even Fib in BigDecimal type
     */
    public BigDecimal getNextEvenFib() {
        BigDecimal result;
        do {
            result = getNextFib();

        } while (evenNumber(result) == false);

        return result;
    }

    /**
     *get the next Fib number
     * @return fib number in BigDecimal type
     */
    public BigDecimal getNextFib() {

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
     *test if an input is even number or not
     * @param input
     * @return true if input is an even number
     */
    public boolean evenNumber(BigDecimal input) {
        return ((input.remainder(new BigDecimal(2))).equals(new BigDecimal(0)) ? (true) : (false));
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        test3();
    }

    /**
     *test if the object generate a sequence of even fib numbers correctly
     */
    public static void test3() {
        EvenFibBigDecimal obj = new EvenFibBigDecimal();

        for (int i = 0; i < 200; i++) {
            BigDecimal result = obj.getNextEvenFib();
            System.out.println("even Fib_"+i + " = " + result.abs());
        }
    }

    /**
     *test if the object generate a sequence of fib numbers correctly
     */
    public static void test2() {
        EvenFibBigDecimal obj = new EvenFibBigDecimal();

        for (int i = 0; i < 100; i++) {
            BigDecimal result = obj.getNextFib();
            System.out.println("Fib_"+i + " = " + result.abs());
        }
    }

}
