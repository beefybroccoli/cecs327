package part_2_assignment;

import static VALUE.VALUE.echo;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class test_part_2 {

    public static void main(String[] args) {

        int index = 0;
        int max = 10000;

        while (index < max) {

            try {
                TimeUnit.SECONDS.sleep(5);
                runTest();
            } catch (InterruptedException ex) {
                echo("InterruptedExceptoin occured");
            }
            index++;
        }

        runTest();

    }

    public static void runTest() {
        String inputHostName = VALUE.VALUE.LOCAL_HOST;
//        inputHostName = "192.168.1.5";
        RuntimeThr runtime = new RuntimeThr(inputHostName);
        Stateful_Server_Listener_VersionCharlie listener = new Stateful_Server_Listener_VersionCharlie();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {

//            executor.submit(listener);
            executor.submit(runtime);

            executor.awaitTermination(5, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            System.err.println("tasks interrupted  in runtime line 164");
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
}
