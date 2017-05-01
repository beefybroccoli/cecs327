package stateful_server_client_version_8;

import part_2_assignment_version_final.object.ServerSharedResource;

public class StatefulServerProtocolVerison8 {


    int mServerID;
    public int mOtherCounter;
    private ServerSharedResource mSharedResource;

    public StatefulServerProtocolVerison8(int inputServerID, ServerSharedResource inputSharedResource) {

        mServerID = inputServerID;
        mOtherCounter = 0;
        mSharedResource = inputSharedResource;
    }

    public String process(String input) {
        String result = input + " - ";

        switch (input) {
  
            case "null":
                result += "null " + mServerID;
                break;
                
            case "hi":
                result += "hello from server " + mServerID;
                break;
                
            case "-1":
                result += "exit thread";
                break;

            case "1":
                result += "Even Fib Big Decimal " + mSharedResource.getNextEvenFib();
                break;

            case "2":
                result += "Larger RandomNumber " + mSharedResource.getNextLargerRand();
                break;

            case "3":
                result += "Prime Number " + mSharedResource.getNextPrime();
                break;

            default:
                ++mOtherCounter;
                result += "invalid input, 1 - Even Fib Big Decimal, 2 - Larger RandomNumber, 3 - Prime Numbe, -1 for exit";
        }
        return result;
    }
}
