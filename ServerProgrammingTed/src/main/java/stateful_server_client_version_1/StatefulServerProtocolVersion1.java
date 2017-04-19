
package stateful_server_client_version_1;

import object.EvenFibBigDecimal;
import object.LargerRandomNumberBigDecimal;
import object.PrimeGeneratorBigDecimal;

public class StatefulServerProtocolVersion1 {
   EvenFibBigDecimal mEvenFibBigDecimalType;
   LargerRandomNumberBigDecimal mLargerRandomNumber;
   PrimeGeneratorBigDecimal mPrimeGenerator;
   
   public StatefulServerProtocolVersion1(){
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
            result += "exit";
        }
        
        
        return result;
    }

    
}