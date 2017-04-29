
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FibRandPrimeProtocol {

    private final Lock lock1 = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();
    private final Lock lock3 = new ReentrantLock();

    private int fibNumInput = 0;
    private int fibNumResult = 0;

    private int randNumInput = 0;
    private int randNumResult = 0;

    private int primeNumInput = 0;
    private int primeNumResult = 0;

    private boolean twoWasGrabbed = false;
    private boolean threeWasGrabbed = false;

    public String processInput(String theInput) {
        String theOutput = null;

        if (theInput == null) {
            theOutput = "Welcome. Please type one of the following: "
                    + "nextEvenFib, nextLargerRand, or nextPrime.";
        } else if (theInput.equalsIgnoreCase("nextEvenFib")) {
            lock1.lock();
            try {
                fibNumResult = nextEvenFib(fibNumInput);
                theOutput = "nextEvenFib = " + Integer.toString(fibNumResult);
                fibNumInput = fibNumResult;
            } finally {
                lock1.unlock();
            }
        } else if (theInput.equalsIgnoreCase("nextLargerRand")) {
            lock2.lock();
            try {
                randNumResult = nextLargerRand(randNumInput);
                theOutput = "nextLargerRand = " + Integer.toString(randNumResult);
                randNumInput = randNumResult;
            } finally {
                lock2.unlock();
            }
        } else if (theInput.equalsIgnoreCase("nextPrime")) {
            lock3.lock();
            try {
                primeNumResult = nextPrime(primeNumInput);
                theOutput = "nextPrime = " + Integer.toString(primeNumResult);
                primeNumInput = primeNumResult;
            } finally {
                lock3.unlock();
            }
        }

        return theOutput;
    }

    private int nextEvenFib(int num) {
        int previous = 0;
        int next = 1;
        int fibNum = 0;
        boolean nextFibFound = false;
        //int i = 2;

        while (!nextFibFound) {
            fibNum = previous + next;

            if (fibNum % 2 == 0 && fibNum > num) {
                nextFibFound = true;
            } else {
                previous = next;
                next = fibNum;
            }
        }
        //for (int i = 0; i < num; i++ ) {

         //   answer = previous + next;
        //   previous = next;
        //   next = answer;
        //}
        return fibNum;
    }

    private int nextLargerRand(int num) {
        int max = 100;
        int min = 1;
        int random = (int) (Math.random() * max + min);

        if (num == max) {
            random = (int) (Math.random() * max + min);
        } else if (num < max) {
            while (random <= num) {
                random = (int) (Math.random() * max + min);
            }
        }

        return random;
    }

    private boolean isPrime(int num) {
        for (int i = 2; i < Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }

        return true;
    }

    private int nextPrime(int num) {
        boolean aPrime = false;

        if (!twoWasGrabbed) {
            twoWasGrabbed = true;
            num = 2;
            return num;
        } else if (!threeWasGrabbed) {
            threeWasGrabbed = true;
            num = 3;
            return num;
        }

        num += 2;

        while (!aPrime) {
            for (int i = 2; i < num; i++) {
                aPrime = isPrime(num);
            }

            if (!aPrime) {
                num += 2;
            }

        }

        return num;
    }

}
