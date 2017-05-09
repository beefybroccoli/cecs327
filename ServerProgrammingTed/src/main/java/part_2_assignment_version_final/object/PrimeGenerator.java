package part_2_assignment_version_final.object;

import java.math.BigInteger;

/**
 * calculate the next prime number
 */
public class PrimeGenerator {

    private BigInteger mPrimeNumber;

    /**
     * construct a PrimeGenerator object
     */
    public PrimeGenerator() {
        mPrimeNumber = BigInteger.valueOf(1);
    }

    /**
     * reset the object to value of 2
     */
    private void reset() {
        mPrimeNumber = BigInteger.valueOf(2);
    }

    /**
     * get the next prime number
     *
     * @return BigInteger result
     */
    public BigInteger getNextPrime() {

        BigInteger result = mPrimeNumber;

        boolean flag = false;

        do {
            try {
                result = result.add(BigInteger.valueOf(1));

                //test if result is a value of prime at 80% probability
                flag = result.isProbablePrime(80);

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
    
    public static void main(String[] args){
        PrimeGenerator generator = new PrimeGenerator();
        
        int counter = 0;
        
        do {
            System.out.println(generator.getNextPrime().toString());
        } while(counter++ < 20);
    }


}
