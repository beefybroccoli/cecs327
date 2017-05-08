package object;

import java.math.BigInteger;

public class OutOfMemoryExceptionDemo {

    public static void main(String[] args) {
        test2();
    }
    
    public static void test2(){
        BigInteger value1 = new BigInteger("44444455555577744");
        BigInteger value2 = value1.add(new BigInteger("1")); 
        BigInteger result;
        
        for ( int i = 1; i < 100; i++){
            result = value1.multiply(value2);
            value1 = result;
            value2 = result.add(BigInteger.ONE);
            System.out.println("result : " + i);
        }
    }

    public static void test1() {
        int size = Integer.MAX_VALUE - 1;
        int factor = 10;

        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("Trying to allocate " + size + " bytes");
                byte[] bytes = new byte[size];
                System.out.println("Succeed!");
                break;
            } catch (OutOfMemoryError e) {
                System.out.println("OutOfMemoryError Occured");
                size /= factor;
            }
        }
    }

}
