
package stateful_server_client_version_2;

import object.EvenFibBigDecimal;
import object.LargerRandomNumberBigDecimal;
import object.PrimeGeneratorBigDecimal;

public class StatefulServerProtocolVersion2 {
   EvenFibBigDecimal mEvenFibBigDecimalType;
   LargerRandomNumberBigDecimal mLargerRandomNumber;
   PrimeGeneratorBigDecimal mPrimeGenerator;
   
   public StatefulServerProtocolVersion2(){
       mEvenFibBigDecimalType = new EvenFibBigDecimal();
       mLargerRandomNumber = new LargerRandomNumberBigDecimal();
       mPrimeGenerator = new PrimeGeneratorBigDecimal();
   }
    
    public String process(String input){
        String result = input + " - ";
        
        if(input.equals("1")){
            result += "Even Fib Big Decimal " + mEvenFibBigDecimalType.getNextEvenFib().abs();
        }else if ( input.equals("2")){
            result += "Larger RandomNumber " + mLargerRandomNumber.getNextLargerRand().abs();
        }else if ( input.equals("3")){
            result += "Prime Number " + mPrimeGenerator.getNextPrime().abs();
        }else{
            result += "invalid input, 1 - Even Fib Big Decimal, 2 - Larger RandomNumber, 3 - Prime Numbe, -1 for exit";
        }
        
        
        return result;
    }

    
}