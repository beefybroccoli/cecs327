package part_2_assignment_version_1;

import static VALUE.VALUE.echo;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class test_part_2_version_1 {

    public static void main(String[] args) {

        String inputHostName = VALUE.VALUE.LOCAL_HOST;
//        inputHostName = "192.168.1.5";

        start_listener_on_local_machine();

        runTest_one_time(inputHostName);

//        repeat_test_1000_times(inputHostName);
    }

    public static void repeat_test_1000_times(String inputHostName) {
        int index = 0;
        int max = 10000;

        while (index < max) {

            try {
                TimeUnit.SECONDS.sleep(5);
                runTest_one_time(inputHostName);
            } catch (InterruptedException ex) {
                echo("InterruptedExceptoin occured");
            }
            index++;
        }
    }

    public static void runTest_one_time(String inputHostName) {

        RuntimeThr_version_1 runtime = new RuntimeThr_version_1(inputHostName);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
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

    public static void start_listener_on_local_machine() {
        Stateful_Server_Listener_VersionCharlie listener = new Stateful_Server_Listener_VersionCharlie();
        listener.start();
    }

}
