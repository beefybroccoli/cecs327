package reference;

public class PseudoPrimeNumber {

    public static void main(String[] args) {
        test1GeneratePrimeNumber();
    }

    public static void test1GeneratePrimeNumber() {
        for (int i = 2; i < 1000; i++) {

            boolean flag = true;

            int number = i;

            if (number % 2 == 0) {
                flag = false;
            } else {
                for (int j = 2; j < Math.sqrt((double) number) && flag == true; j++) {
                    if (number % j == 0) {
                        flag = false;
                        break;
                    }
                }
            }

            if (flag == true) {
                System.out.println("prime number : " + number);
            }
        }
    }
}
