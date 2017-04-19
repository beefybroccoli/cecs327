package stateful_server_client_version_5;

import object.EvenFibBigDecimal;
import object.LargerRandomNumberBigDecimal;
import object.PrimeGeneratorBigDecimal;

public class StatefulServerProtocolVersion5 {

    EvenFibBigDecimal mEvenFibBigDecimalType;
    LargerRandomNumberBigDecimal mLargerRandomNumber;
    PrimeGeneratorBigDecimal mPrimeGenerator;
    int mServerID;
    public int mOtherCounter;

    public StatefulServerProtocolVersion5(int inputServerID) {
        mEvenFibBigDecimalType = new EvenFibBigDecimal();
        mLargerRandomNumber = new LargerRandomNumberBigDecimal();
        mPrimeGenerator = new PrimeGeneratorBigDecimal();
        mServerID = inputServerID;
        mOtherCounter = 0;
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
                ++mOtherCounter;
                result += "invalid input, 1 - Even Fib Big Decimal, 2 - Larger RandomNumber, 3 - Prime Numbe, -1 for exit";
        }
        return result;
    }
}
