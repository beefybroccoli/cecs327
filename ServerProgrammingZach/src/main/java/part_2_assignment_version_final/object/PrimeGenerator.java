package part_2_assignment_version_final.object;

import java.math.BigInteger;

/*
calculate the next prime number
 */
public class PrimeGenerator {

    private BigInteger mPrimeNumber;

    public PrimeGenerator() {
        mPrimeNumber = BigInteger.valueOf(1);
    }

    private void reset() {
        mPrimeNumber = BigInteger.valueOf(2);
    }

    public boolean testPrime(BigInteger number) {
        boolean flag = true;

        if (number.equals(BigInteger.valueOf(2))) {
            flag = true;
        } else if ((number.remainder(BigInteger.valueOf(2))).equals(BigInteger.valueOf(0))) {
            flag = false;
        } else {

            for (int j = 2; j < Math.sqrt(number.doubleValue()) + 1 && flag == true; j++) {
                if ((number.remainder(BigInteger.valueOf(j))).equals(BigInteger.valueOf(0))) {
                    flag = false;
                    continue;
                }
            }
        }
        return flag;

    }

    public BigInteger getNextPrime() {

        BigInteger result = mPrimeNumber;

        boolean flag = false;

        do {
            try {
                result = result.add(BigInteger.valueOf(1));

                flag = testPrime(result);

                if (flag == true) {
                    mPrimeNumber = result;
                }
            } catch (OutOfMemoryError e) {
                System.out.println("OutOfMemory Exception Occured");
                reset();
                result = mPrimeNumber;
            }
        } while (flag == false);

        return result;

    }

}
