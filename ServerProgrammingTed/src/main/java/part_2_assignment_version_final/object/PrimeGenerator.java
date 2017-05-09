package part_2_assignment_version_final.object;

import java.math.BigInteger;

/**
 * calculate the next prime number
 */
public class PrimeGenerator {

    private BigInteger mPrimeNumber = BigInteger.valueOf(1);

    /**
     * reset the object to value of 2
     */
    private BigInteger reset() {
        mPrimeNumber = BigInteger.valueOf(2);
        return mPrimeNumber;
    }

    /**
     * get the next prime number
     *
     * @return BigInteger result
     */
    public String getNextPrime() {

        BigInteger result = mPrimeNumber;
        boolean flag = false;

        do {
            try {
                result = result.add(BigInteger.valueOf(1));

                //test if result is a prime at 80% probability
                flag = result.isProbablePrime(80);

            } catch (OutOfMemoryError e) {
                System.out.println("OutOfMemory Exception Occured");
                result = reset();
            }
        } while (flag == false);

        mPrimeNumber = result;

        return result.toString();

    }

    public static void main(String[] args) {
        PrimeGenerator generator = new PrimeGenerator();
        int counter = 0;
        while (counter++ < 10) {
            System.out.println(generator.getNextPrime());
        }
    }

}
