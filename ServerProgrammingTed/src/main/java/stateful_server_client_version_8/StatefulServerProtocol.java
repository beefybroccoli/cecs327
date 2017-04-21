package stateful_server_client_version_8;

import stateful_server_client_version_alpha.*;
import object.SharedResource;

public class StatefulServerProtocol {


    int mServerID;
    public int mOtherCounter;
    private SharedResource mSharedResource;

    public StatefulServerProtocol(int inputServerID, SharedResource inputSharedResource) {

        mServerID = inputServerID;
        mOtherCounter = 0;
        mSharedResource = inputSharedResource;
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
