package stateful_server_client_version_5;

import object.EvenFibBigDecimalType;
import object.LargerRandomNumber;
import object.PrimeGenerator;

public class StatefulServerProtocolVersion5 {

    EvenFibBigDecimalType mEvenFibBigDecimalType;
    LargerRandomNumber mLargerRandomNumber;
    PrimeGenerator mPrimeGenerator;
    int mServerID;

    public StatefulServerProtocolVersion5(int inputServerID) {
        mEvenFibBigDecimalType = new EvenFibBigDecimalType();
        mLargerRandomNumber = new LargerRandomNumber();
        mPrimeGenerator = new PrimeGenerator();
        mServerID = inputServerID;
    }

    public String process(String input) {
        String result = input + " - ";

        switch (input) {
            case "hi":
                result += "hello from server " + mServerID;
                break;
                
            case "-1":
                result += "exit thread";
                break;

            case "1":
                result += "Even Fib Big Decimal " + mEvenFibBigDecimalType.getNextEvenFib().abs();
                break;

            case "2":
                result += "Larger RandomNumber " + mLargerRandomNumber.getNextLargerRand().abs();
                break;

            case "3":
                result += "Prime Number " + mPrimeGenerator.getNextPrime().abs();
                break;

            default:
                result += "invalid input, 1 - Even Fib Big Decimal, 2 - Larger RandomNumber, 3 - Prime Numbe, -1 for exit";
        }
        return result;
    }
}
