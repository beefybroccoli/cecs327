package part_1_stateful_server_client_version_alpha;

import object.SharedResource;

/***************************************************************************
     * StatefulServerProtocolAlpha Class
     * 
     * This Class contains the process for the server. Whenever a client makes a
     * request, the server protocol decides which process to carry out.
     **************************************************************************/
public class StatefulServerProtocolAlpha {


    int mServerID;
    public int mOtherCounter;
    private SharedResource mSharedResource;

    public StatefulServerProtocolAlpha(int inputServerID, SharedResource inputSharedResource) {

        mServerID = inputServerID;
        mOtherCounter = 0;
        mSharedResource = inputSharedResource;
    }

    /**
     *  Default Constructor
     *  
     *  Default constructor creates an instance of a request from the client.
     *  
     *  input - which process to carry out
    **/
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
