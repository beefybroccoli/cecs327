package reference;

import static sun.security.jgss.GSSToken.debug;

/*
calculate the next even fib
 */
public class EvenFibIntType {

    private int valueNMinus2;
    private int valueNMinus1;
    private int valueN;

    public EvenFibIntType() {
        valueNMinus2 = 0;
        valueNMinus1 = 1;
        valueN = -1;
//        debug("debug - initialize EvenFibIntType object\n");
//        debug("debug - valueNMinus2 = " + valueNMinus2 + "\n");
//        debug("debug - valueNMinus1 = " + valueNMinus1 + "\n\n");
    }

    public int getNextEvenFib() {
        int result;

        do {
            result = getNextFib();

        } while (evenNumber(result) == false);

        return result;
    }

    //return the next fib number
    public int getNextFib() {

        if (valueN == -1) {
            valueN = valueNMinus2 + valueNMinus1;
        } else {
            valueNMinus2 = valueNMinus1;
            valueNMinus1 = valueN;
            valueN = valueNMinus2 + valueNMinus1;
        }

//        debug("debug - valueNMinus2 = " + valueNMinus2 + "\n");
//        debug("debug - valueNMinus1 = " + valueNMinus1 + "\n");
//        debug("debug - valueN = " + valueN + "\n\n");
        return valueN;
    }

    //test if a number is even
    public boolean evenNumber(int input) {
        return ((input % 2) == 0 ? (true) : (false));
    }

    public static void main(String[] args) {

        test2();

    }

    public static void test3() {
        EvenFibIntType obj = new EvenFibIntType();

        for (int i = 0; i < 20; i++) {
            int result = obj.getNextEvenFib();
            System.out.println(result);
        }
    }

    /*
    test if the object generate a sequence of fib numbers correctly
    */
    public static void test2() {
        EvenFibIntType obj = new EvenFibIntType();

        for (int i = 0; i < 75; i++) {
            int result = obj.getNextFib();
            System.out.println(result);
        }
    }

    public void test1() {
        /*
        assume the seed is 0 and 1
         */
        int valueNMinus2 = 0;
        int valueNMinus1 = 1;
        int valueN;
        for (int i = 0; i < 10; i++) {
            valueN = valueNMinus2 + valueNMinus1;

            System.out.println("ValueN = " + valueN + ", " + valueNMinus1 + " + " + valueNMinus2);

            valueNMinus2 = valueNMinus1;
            valueNMinus1 = valueN;
        }
    }

}
