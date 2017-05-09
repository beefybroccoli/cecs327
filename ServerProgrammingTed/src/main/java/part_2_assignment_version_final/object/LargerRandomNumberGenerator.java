package part_2_assignment_version_final.object;

import java.math.BigInteger;
import java.util.Random;

/**
 * generate the next larger random number
 */
public class LargerRandomNumberGenerator {

    private BigInteger mValue = BigInteger.valueOf(1);

    /**
     * @return the next larger random value
     */
    public String getNextLargerRand() {
        BigInteger temp;
        try {
            mValue = mValue.add(new BigInteger(VALUE.getRandomNumberBetween(10000000, 1), new Random()));
        } catch (OutOfMemoryError e) {
            //reset if OUtOfMemoryError exception occur
            mValue = BigInteger.valueOf(1);
        }
        return mValue.toString();
    }

    public String getNextLargerRand2() {
        try {
            BigInteger temp;
            Random rand = new Random();
            do {
                temp = new BigInteger(VALUE.getRandomNumberBetween(1000, 1), rand);

            } while (mValue.compareTo(temp) <= 1);

            mValue = temp;
        } catch (OutOfMemoryError e) {
            System.out.println("OutOfMemoryError exception occured");
            //reset if OUtOfMemoryError exception occur
            mValue = BigInteger.valueOf(1);
        }
        return mValue.toString();
    }

    public static void main(String[] args) {

//        test_generate_random_BigInteger();
//        test_getNextLargerRand2();
        test_getNextLargerRand();
    }

    public static void test_getNextLargerRand2() {
        LargerRandomNumberGenerator generator = new LargerRandomNumberGenerator();
        int counter = 0;
        while (counter++ < 10) {
            System.out.println(generator.getNextLargerRand2());
        }
    }

    public static void test_getNextLargerRand() {
        LargerRandomNumberGenerator generator = new LargerRandomNumberGenerator();
        int counter = 0;
        while (true) {
            System.out.println("counter = " + ++counter);
            generator.getNextLargerRand();
        }
    }

    public static void test_generate_random_BigInteger() {

        BigInteger temp[] = new BigInteger[10];
        Random rand = new Random();
        int counter = 0;
        while (counter < 10) {
            temp[counter] = new BigInteger(VALUE.getRandomNumberBetween(100, 1), rand);
            System.out.println(temp[counter].toString());
            counter++;
        }

        System.out.println(temp[0] + " compare to " + temp[1] + " = " + temp[0].compareTo(temp[1]));
        System.out.println(temp[1] + " compare to " + temp[0] + " = " + temp[1].compareTo(temp[0]));
        /*
        --- exec-maven-plugin:1.2.1:exec (default-cli) @ ServerProgrammingTed ---
        56456270
        762464213
        9905067256858563060655031
        21
        3869392792394756
        30527493678160837390
        239154479952917795696059
        29911616621074568369
        47025378952057939501189703
        1040224
        
        56456270 compare to 762464213 = -1
        762464213 compare to 56456270 = 1
         */
    }
}
