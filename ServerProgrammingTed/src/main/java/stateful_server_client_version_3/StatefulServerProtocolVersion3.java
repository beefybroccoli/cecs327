package stateful_server_client_version_3;

import object.EvenFibBigDecimalType;
import object.LargerRandomNumber;
import object.PrimeGenerator;

public class StatefulServerProtocolVersion3 {

    EvenFibBigDecimalType mEvenFibBigDecimalType;
    LargerRandomNumber mLargerRandomNumber;
    PrimeGenerator mPrimeGenerator;

    public StatefulServerProtocolVersion3() {
        mEvenFibBigDecimalType = new EvenFibBigDecimalType();
        mLargerRandomNumber = new LargerRandomNumber();
        mPrimeGenerator = new PrimeGenerator();
    }

    public String process(String input) {
        String result = input + " - ";
        if (input.equals("-1")) {
            result += "exit thread";
        }else if (input.equals("1")) {
            result += "Even Fib Big Decimal " + mEvenFibBigDecimalType.getNextEvenFib().abs();
        } else if (input.equals("2")) {
            result += "Larger RandomNumber " + mLargerRandomNumber.getNextLargerRand().abs();
        } else if (input.equals("3")) {
            result += "Prime Number " + mPrimeGenerator.getNextPrime().abs();
        } else {
            result += "invalid input, 1 - Even Fib Big Decimal, 2 - Larger RandomNumber, 3 - Prime Numbe, -1 for exit";
        }

        return result;
    }

}
