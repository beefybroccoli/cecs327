package object;

import java.math.BigDecimal;
import java.util.Random;

/**
 * generate the next larger random number
 */
public class LargerRandomNumberBigDecimal {

    private BigDecimal mValue;

    /**
     * initialize a LargerRandomNumber object with seed value of 0
     */
    public LargerRandomNumberBigDecimal() {
        mValue = new BigDecimal(0);
    }

    /**
     *
     * @return the next larger random value
     */
    public BigDecimal getNextLargerRand() {
        Random rn = new Random();
        int randomNumber = rn.nextInt(1000000000) + 1;
        mValue = mValue.add(new BigDecimal(randomNumber));

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

        LargerRandomNumberBigDecimal value = new LargerRandomNumberBigDecimal();

        for (int i = 0; i < 50; i++) {

            System.out.println("value = " + value.getNextLargerRand());
        }
    }

    /**
     * generate a list of random value
     */
    public static void test1() {
        BigDecimal value = new BigDecimal(0);
        Random rn = new Random();

        for (int i = 0; i < 10; i++) {
            int randomNumber = rn.nextInt(1000) + 1;
            value = value.add(new BigDecimal(randomNumber));
            System.out.println("value = " + value.abs());
        }
    }
}
