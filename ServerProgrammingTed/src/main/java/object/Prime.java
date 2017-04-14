package object;

import java.math.BigDecimal;

/*
calculate the next prime number
 */
public class Prime {

    private BigDecimal mPrimeNumber;

    public Prime() {
        mPrimeNumber = new BigDecimal(1);
    }

    public BigDecimal getNextPrime() {
        BigDecimal result = new BigDecimal(-1);

        BigDecimal number = mPrimeNumber;

        boolean flag = true;

        do {
            number = number.add(new BigDecimal(1));

            if ((number.remainder(new BigDecimal(2))).equals(new BigDecimal(0))) {
                flag = false;
            } else {
                for (int j = 2; j < Math.sqrt(number.floatValue()) && flag == true; j++) {
                    if ((number.remainder(new BigDecimal(j))).equals(new BigDecimal(0))) {
                        flag = false;
                        break;
                    }
                }
            }

            if (flag == true) {
                result = number;

                System.out.println("prime number : " + number.abs());
            }
        } while (result.equals(new BigDecimal(-1)));

        mPrimeNumber = result;
        return result;

    }

    public static void main(String[] args) {

        Prime obj = new Prime();

        for (int i = 0; i < 100; i++) {
            obj.getNextPrime().abs();
        }

//        test1GeneratePrimeNumber();
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


/*
void main()
{
   int num,i;
   int FLAG=1;
   printf(“Enter any Positive Number : “);
   scanf(“%d”,&num);
   for(i=2;i<sqrt(num);i++)
   {
      if(num%i == 0)
      {
         FLAG = 0;
         break;
      }
   }
   if(FLAG == 1)
   {
      printf(“%d is Prime Number n“,num);
   }
   else
   {
      printf(“%d is not a Prime Number n“,num);
   }
   return;
}
 */
