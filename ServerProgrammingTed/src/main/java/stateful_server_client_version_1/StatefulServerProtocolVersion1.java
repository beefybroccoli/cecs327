
package stateful_server_client_version_1;


import reference_knock_knock.*;
import java.net.*;
import java.io.*;
import object.EvenFibBigDecimalType;
import object.LargerRandomNumber;
import object.PrimeGenerator;

public class StatefulServerProtocolVersion1 {
   EvenFibBigDecimalType mEvenFibBigDecimalType;
   LargerRandomNumber mLargerRandomNumber;
   PrimeGenerator mPrimeGenerator;
   
   public StatefulServerProtocolVersion1(){
       mEvenFibBigDecimalType = new EvenFibBigDecimalType();
       mLargerRandomNumber = new LargerRandomNumber();
       mPrimeGenerator = new PrimeGenerator();
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