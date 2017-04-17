package object;

import java.math.BigDecimal;

/*
calculate the next prime number
 */
public class PrimeGenerator {

    private BigDecimal mPrimeNumber;

    public PrimeGenerator() {
        mPrimeNumber = new BigDecimal(1);
    }

    public boolean testPrime(BigDecimal number) {
        boolean flag = true;

        if (number.equals(new BigDecimal(2))) {
            flag = true;
        }
        else if ((number.remainder(new BigDecimal(2))).equals(new BigDecimal(0))) {
            flag = false;
        } else {

            for (int j = 2; j < Math.sqrt(number.doubleValue()) + 1 && flag == true; j++) {
                if ((number.remainder(new BigDecimal(j))).equals(new BigDecimal(0))) {
                    flag = false;
                    continue;
                }
            }
        }
        return flag;

    }

    public BigDecimal getNextPrime() {

        BigDecimal result = mPrimeNumber;

        boolean flag = false;

        do {

            result = result.add(new BigDecimal(1));

            flag = testPrime(result);

            if (flag == true) {
                mPrimeNumber = result;
            }
        } while (flag == false);

        return result;

    }
    
//
//    public static void main(String[] args) {
//
//        demonTestPrimeMthod();
//        demoGetNextPrime();
//    }

    public static void demoGetNextPrime() {

        PrimeGenerator obj = new PrimeGenerator();

        for (int i = 0; i < 25; i++) {
            System.out.println("next prime i = " + (i + 1) + ", " + obj.getNextPrime());
        }
        System.out.println("");

    }

    public static void demonTestPrimeMthod() {
        PrimeGenerator obj = new PrimeGenerator();

        for (int i = 2; i < 100; i++) {

            boolean result = obj.testPrime(new BigDecimal(i));

            if (result == true) {
                System.out.println("test prime number " + i + " : " + obj.testPrime(new BigDecimal(i)));
            }
        }
        System.out.println("");
    }

}