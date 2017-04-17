package stateful_server_client_version_4;

import object.EvenFibBigDecimalType;
import object.LargerRandomNumber;
import object.PrimeGenerator;

public class StatefulServerProtocolVersion4 {

    EvenFibBigDecimalType mEvenFibBigDecimalType;
    LargerRandomNumber mLargerRandomNumber;
    PrimeGenerator mPrimeGenerator;
    int mServerID;

    public StatefulServerProtocolVersion4(int inputServerID) {
        mEvenFibBigDecimalType = new EvenFibBigDecimalType();
        mLargerRandomNumber = new LargerRandomNumber();
        mPrimeGenerator = new PrimeGenerator();
        mServerID = inputServerID;
    }

    public String process(String input) {
        String result = input + " - ";
        if (input.equals("hi")) {
            result += "hello from server " + mServerID;
        }else if (input.equals("-1")) {
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
