package part_2_assignment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class test_part_2 {

    public static void main(String[] args) {

        RuntimeThr runtime = new RuntimeThr();
        Stateful_Server_Listener_VersionCharlie listener = new Stateful_Server_Listener_VersionCharlie();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {

            executor.submit(listener);
            executor.submit(runtime);

            executor.awaitTermination(30, TimeUnit.SECONDS);

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
