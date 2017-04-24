package stateful_server_client_version_beta;

import VALUE.VALUE;
import static VALUE.VALUE.echo;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class testVersionBeta {

    public static void main(String[] args) {

        test();
    }

    public static void test() {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        try {

            StatefulServerListenerBeta listener = new StatefulServerListenerBeta();

            executor.submit(listener);

            for (int i = 0; i < 100; i++) {
                String task = getRandomTask();
                int id = i + 1;
                System.out.println(" i = " + i + " ,result for input " + task + " = " + getResultForInput(id, task));
            }

            executor.awaitTermination(100, TimeUnit.MILLISECONDS);

        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            executor.shutdown();
            System.err.println("executor.isTerminated() status = " + executor.isTerminated());
            if (!executor.isTerminated()) {
                System.err.println("canceled non-finished tasks");

                executor.shutdownNow();

            } else {
                System.out.println("shutdown finished");
            }
        }
    }

    public static String getResultForInput(int id, String input) {
        StatefulClientBetaVersion2 client = new StatefulClientBetaVersion2(VALUE.LOCAL_HOST, VALUE.SERVER_PORT_NUMBER, id, input);

        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<String> future = executor.submit(
                () -> {
                    client.run();
                    return client.call();
                }
        );

        String result = "";

        try {
            result = future.get(100, TimeUnit.MILLISECONDS);

        } catch (InterruptedException ex) {
            echo("InterruptedException occured");
        } catch (ExecutionException ex) {
            echo("ExecutionException occured");
        } catch (TimeoutException ex) {
            echo("TimeoutException occured");
        } catch (NullPointerException ex) {
            echo("NullPointerException occured");
        } finally {
//            System.out.println("result = " + result);
            executor.shutdownNow();
        }
        return result;
    }

    public static String getRandomTask() {
        String result = "";
        Random rn = new Random();
        int rand = rn.nextInt(3) + 1;
        result = "" + rand;
        return result;
    }

}

/*
------------------------------------------------------------------------
Building ServerProgrammingTed 1.0-SNAPSHOT
------------------------------------------------------------------------

--- exec-maven-plugin:1.2.1:exec (default-cli) @ ServerProgrammingTed ---
(Server Listener version beta version 1 started)
Could not listen on port 4444
(Server Listener version beta version 1 ended)
 i = 0 ,result for input 3 = 3,151
 i = 1 ,result for input 1 = 1,2880067194370816120
 i = 2 ,result for input 2 = 2,177431
 i = 3 ,result for input 2 = 2,181769
 i = 4 ,result for input 1 = 1,12200160415121876738
 i = 5 ,result for input 2 = 2,186940
 i = 6 ,result for input 2 = 2,187082
 i = 7 ,result for input 2 = 2,191359
 i = 8 ,result for input 3 = 3,157
 i = 9 ,result for input 1 = 1,51680708854858323072
 i = 10 ,result for input 2 = 2,192251
 i = 11 ,result for input 3 = 3,163
 i = 12 ,result for input 3 = 3,167
 i = 13 ,result for input 2 = 2,194604
 i = 14 ,result for input 2 = 2,201182
 i = 15 ,result for input 3 = 3,173
 i = 16 ,result for input 2 = 2,205493
 i = 17 ,result for input 1 = 1,0
 i = 18 ,result for input 1 = 1,2
 i = 19 ,result for input 2 = 2,210162
 i = 20 ,result for input 3 = 3,179
 i = 21 ,result for input 1 = 1,8
 i = 22 ,result for input 3 = 3,181
 i = 23 ,result for input 3 = 3,191
 i = 24 ,result for input 2 = 2,216581
 i = 25 ,result for input 2 = 2,225583
 i = 26 ,result for input 2 = 2,228641
 i = 27 ,result for input 3 = 3,193
 i = 28 ,result for input 3 = 3,197
 i = 29 ,result for input 2 = 2,230642
 i = 30 ,result for input 2 = 2,235058
 i = 31 ,result for input 2 = 2,239192
 i = 32 ,result for input 3 = 3,199
 i = 33 ,result for input 1 = 1,34
 i = 34 ,result for input 1 = 1,144
 i = 35 ,result for input 1 = 1,610
 i = 36 ,result for input 3 = 3,211
 i = 37 ,result for input 3 = 3,223
 i = 38 ,result for input 2 = 2,248835
 i = 39 ,result for input 2 = 2,250213
 i = 40 ,result for input 3 = 3,227
 i = 41 ,result for input 1 = 1,2584
 i = 42 ,result for input 2 = 2,254066
 i = 43 ,result for input 1 = 1,10946
 i = 44 ,result for input 1 = 1,46368
 i = 45 ,result for input 2 = 2,263648
 i = 46 ,result for input 2 = 2,265456
 i = 47 ,result for input 1 = 1,196418
 i = 48 ,result for input 3 = 3,229
 i = 49 ,result for input 3 = 3,233
 i = 50 ,result for input 1 = 1,832040
 i = 51 ,result for input 2 = 2,273623
 i = 52 ,result for input 2 = 2,281832
 i = 53 ,result for input 2 = 2,285925
 i = 54 ,result for input 1 = 1,3524578
 i = 55 ,result for input 1 = 1,14930352
 i = 56 ,result for input 3 = 3,239
 i = 57 ,result for input 1 = 1,63245986
 i = 58 ,result for input 1 = 1,267914296
 i = 59 ,result for input 3 = 3,241
 i = 60 ,result for input 2 = 2,289221
 i = 61 ,result for input 2 = 2,296246
 i = 62 ,result for input 3 = 3,251
 i = 63 ,result for input 3 = 3,257
 i = 64 ,result for input 2 = 2,303819
 i = 65 ,result for input 3 = 3,263
 i = 66 ,result for input 1 = 1,1134903170
 i = 67 ,result for input 1 = 1,4807526976
 i = 68 ,result for input 2 = 2,304568
 i = 69 ,result for input 2 = 2,311873
 i = 70 ,result for input 3 = 3,269
 i = 71 ,result for input 3 = 3,271
 i = 72 ,result for input 2 = 2,319381
 i = 73 ,result for input 3 = 3,277
 i = 74 ,result for input 2 = 2,320280
 i = 75 ,result for input 3 = 3,281
 i = 76 ,result for input 2 = 2,326466
 i = 77 ,result for input 3 = 3,283
 i = 78 ,result for input 3 = 3,293
 i = 79 ,result for input 1 = 1,20365011074
 i = 80 ,result for input 2 = 2,330004
 i = 81 ,result for input 3 = 3,307
 i = 82 ,result for input 3 = 3,311
 i = 83 ,result for input 3 = 3,313
 i = 84 ,result for input 2 = 2,335882
 i = 85 ,result for input 3 = 3,317
 i = 86 ,result for input 3 = 3,331
 i = 87 ,result for input 1 = 1,86267571272
 i = 88 ,result for input 2 = 2,341114
 i = 89 ,result for input 1 = 1,365435296162
 i = 90 ,result for input 3 = 3,337
 i = 91 ,result for input 2 = 2,341633
 i = 92 ,result for input 3 = 3,347
 i = 93 ,result for input 2 = 2,351281
 i = 94 ,result for input 3 = 3,349
 i = 95 ,result for input 2 = 2,352389
 i = 96 ,result for input 3 = 3,353
 i = 97 ,result for input 2 = 2,361523
 i = 98 ,result for input 1 = 1,1548008755920
 i = 99 ,result for input 2 = 2,369201
executor.isTerminated() status = true
shutdown finished
------------------------------------------------------------------------
BUILD SUCCESS
------------------------------------------------------------------------
Total time: 0.755s
Finished at: Sun Apr 23 23:57:50 PDT 2017
Final Memory: 7M/245M
------------------------------------------------------------------------
*/
