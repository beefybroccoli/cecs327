package part_2_assignment_version_2;

import com.google.common.util.concurrent.Striped;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

public class test_part_2_version_2 {

    public static void main(String[] args) {

        new Stateful_Server_Listener_VersionCharlie().start();
        
        runTest_one_time();

    }

    public static void runTest_one_time() {

        String inputHostName = VALUE.VALUE.LOCAL_HOST;
        LinkedBlockingQueue inputRequestQue = new LinkedBlockingQueue();
        ConcurrentHashMap<String, String> inputResultQue = new ConcurrentHashMap<String, String>();
        Striped<ReadWriteLock> inputSharedRWLock = Striped.readWriteLock(12);

        ExecutorService executorRuntime = Executors.newFixedThreadPool(1);
        executorRuntime.submit(new RuntimeThr_version_2(inputHostName, inputRequestQue, inputResultQue, inputSharedRWLock));

        ExecutorService executorUThread = Executors.newFixedThreadPool(10);
        //spawn 10 uThr
        for (int i = 0; i < 10; i++) {
            int id = i + 1;
            executorUThread.submit(new UThr_version_2(id, inputRequestQue, inputResultQue, inputSharedRWLock));
        }

        int time_in_seconds = 20;
        shutExecutor(executorUThread, time_in_seconds);
        if (executorUThread.isShutdown()) {
            shutExecutor(executorRuntime);
        }
    }

    public static void shutExecutor(ExecutorService executor, int time_in_seconds) {
        try {
            executor.awaitTermination(time_in_seconds, TimeUnit.SECONDS);
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

    public static void shutExecutor(ExecutorService executor) {
        try {
            executor.awaitTermination(1, TimeUnit.MILLISECONDS);
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

}



/*
(runtimeThr Ended)
(runtimeThr Started)
(uThr 1 Ended, mCounter = 20)
(uThr 1 Started)
(uThr 10 Ended, mCounter = 20)
(uThr 10 Started)
(uThr 2 Ended, mCounter = 20)
(uThr 2 Started)
(uThr 3 Ended, mCounter = 20)
(uThr 3 Started)
(uThr 4 Ended, mCounter = 20)
(uThr 4 Started)
(uThr 5 Ended, mCounter = 20)
(uThr 5 Started)
(uThr 6 Ended, mCounter = 20)
(uThr 6 Started)
(uThr 7 Ended, mCounter = 20)
(uThr 7 Started)
(uThr 8 Ended, mCounter = 20)
(uThr 8 Started)
(uThr 9 Ended, mCounter = 20)
(uThr 9 Started)
BUILD SUCCESS
Building ServerProgrammingTed 1.0-SNAPSHOT
canceled non-finished tasks
canceled non-finished tasks
executor.isTerminated() status = false
executor.isTerminated() status = false
Final Memory: 7M/245M
Finished at: Fri Apr 28 00:10:57 PDT 2017
Interruption occured in rumtimeThr
mSharedRequestQue = []
mSharedResultQue = {}
Running NetBeans Compile On Save execution. Phase execution is skipped and output directories of dependency projects (with Compile on Save turned on) will be used instead of their jar artifacts.
Scanning for projects...
Total time: 20.462s
uThr1 consume 1,1,12200160415121876738
uThr1 consume 1,1,244006547798191185585064349218729154
uThr1 consume 1,1,42230279526998466217810220532898
uThr1 consume 1,1,51680708854858323072
uThr1 consume 1,1,555565404224292694404015791808
uThr1 consume 1,1,757791618667731139247631372100066
uThr1 consume 1,2,230483
uThr1 consume 1,2,300279
uThr1 consume 1,2,352420
uThr1 consume 1,2,447181
uThr1 consume 1,2,483435
uThr1 consume 1,3,229
uThr1 consume 1,3,353
uThr1 consume 1,4,20
uThr1 consume 1,4,56
uThr1 consume 1,4,62
uThr1 consume 1,4,70
uThr1 consume 1,4,74
uThr1 consume 1,5,25
uThr1 consume 1,5,45
uThr10 consume 10,1,13598018856492162040239554477268290
uThr10 consume 10,1,146178119651438213260386312206974243796773058
uThr10 consume 10,1,4378519841510949178490918731459856482
uThr10 consume 10,1,47068900554068939361891195233676009091941690850
uThr10 consume 10,1,8146227408089084511865756065370647467555938
uThr10 consume 10,2,249831
uThr10 consume 10,2,292601
uThr10 consume 10,2,332734
uThr10 consume 10,2,373022
uThr10 consume 10,2,402839
uThr10 consume 10,2,432180
uThr10 consume 10,2,492910
uThr10 consume 10,3,227
uThr10 consume 10,3,233
uThr10 consume 10,3,241
uThr10 consume 10,3,263
uThr10 consume 10,3,373
uThr10 consume 10,3,401
uThr10 consume 10,4,36
uThr10 consume 10,5,13
uThr2 consume 2,1,22698374052006863956975682
uThr2 consume 2,1,2353412818241252672952597492098
uThr2 consume 2,1,2623059926317798754175087863660165740874359106
uThr2 consume 2,1,3210056809456107725247980776292056
uThr2 consume 2,1,3928413764606871165730
uThr2 consume 2,1,453973694165307953197296969697410619233826
uThr2 consume 2,1,7308805952221443105020355490
uThr2 consume 2,2,221503
uThr2 consume 2,2,256613
uThr2 consume 2,2,277570
uThr2 consume 2,2,297293
uThr2 consume 2,2,388107
uThr2 consume 2,2,400393
uThr2 consume 2,2,420773
uThr2 consume 2,3,251
uThr2 consume 2,3,317
uThr2 consume 2,3,367
uThr2 consume 2,5,39
uThr2 consume 2,5,53
uThr2 consume 2,5,55
uThr3 consume 3,1,16641027750620563662096
uThr3 consume 3,1,18547707689471986212190138521399707760
uThr3 consume 3,1,218922995834555169026
uThr3 consume 3,1,407305795904080553832073954
uThr3 consume 3,1,927372692193078999176
uThr3 consume 3,2,271450
uThr3 consume 3,2,276306
uThr3 consume 3,2,340566
uThr3 consume 3,2,344321
uThr3 consume 3,2,399501
uThr3 consume 3,2,478987
uThr3 consume 3,3,379
uThr3 consume 3,4,22
uThr3 consume 3,4,26
uThr3 consume 3,4,38
uThr3 consume 3,4,42
uThr3 consume 3,4,46
uThr3 consume 3,4,60
uThr3 consume 3,5,5
uThr3 consume 3,5,51
uThr4 consume 4,1,131151201344081895336534324866
uThr4 consume 4,1,178890334785183168257455287891792
uThr4 consume 4,1,34507973060837282187130139035400899082304280
uThr4 consume 4,1,78569350599398894027251472817058687522
uThr4 consume 4,2,271672
uThr4 consume 4,2,305934
uThr4 consume 4,2,322392
uThr4 consume 4,2,327699
uThr4 consume 4,2,363983
uThr4 consume 4,2,382821
uThr4 consume 4,2,417416
uThr4 consume 4,2,442147
uThr4 consume 4,3,331
uThr4 consume 4,3,383
uThr4 consume 4,4,6
uThr4 consume 4,5,19
uThr4 consume 4,5,35
uThr4 consume 4,5,43
uThr4 consume 4,5,57
uThr4 consume 4,5,59
uThr5 consume 5,1,107168651819712326877926895128666735145224
uThr5 consume 5,1,1264937032042997393488322
uThr5 consume 5,1,332825110087067562321196029789634457848
uThr5 consume 5,2,227329
uThr5 consume 5,2,272518
uThr5 consume 5,2,389861
uThr5 consume 5,2,426652
uThr5 consume 5,2,470983
uThr5 consume 5,2,485895
uThr5 consume 5,3,269
uThr5 consume 5,3,307
uThr5 consume 5,3,313
uThr5 consume 5,3,349
uThr5 consume 5,3,389
uThr5 consume 5,4,8
uThr5 consume 5,5,15
uThr5 consume 5,5,23
uThr5 consume 5,5,27
uThr5 consume 5,5,31
uThr5 consume 5,5,37
uThr6 consume 6,1,619220451666590135228675387863297874269396512
uThr6 consume 6,1,70492524767089125814114
uThr6 consume 6,1,96151855463018422468774568
uThr6 consume 6,2,317393
uThr6 consume 6,2,357080
uThr6 consume 6,2,411329
uThr6 consume 6,2,452593
uThr6 consume 6,2,469138
uThr6 consume 6,3,271
uThr6 consume 6,3,311
uThr6 consume 6,3,359
uThr6 consume 6,4,10
uThr6 consume 6,4,12
uThr6 consume 6,4,24
uThr6 consume 6,4,28
uThr6 consume 6,4,34
uThr6 consume 6,4,48
uThr6 consume 6,5,3
uThr6 consume 6,5,49
uThr6 consume 6,5,9
uThr7 consume 7,1,11111460156937785151929026842503960837766832936
uThr7 consume 7,1,1725375039079340637797070384
uThr7 consume 7,1,25299086886458645685589389182743678652930
uThr7 consume 7,1,30960598847965113057878492344
uThr7 consume 7,1,9969216677189303386214405760200
uThr7 consume 7,2,283799
uThr7 consume 7,2,375799
uThr7 consume 7,3,283
uThr7 consume 7,3,337
uThr7 consume 7,3,347
uThr7 consume 7,3,397
uThr7 consume 7,4,16
uThr7 consume 7,4,18
uThr7 consume 7,4,4
uThr7 consume 7,4,40
uThr7 consume 7,4,54
uThr7 consume 7,4,58
uThr7 consume 7,4,68
uThr7 consume 7,5,21
uThr7 consume 7,5,33
uThr8 consume 8,1,1033628323428189498226463595560281832
uThr8 consume 8,1,298611126818977066918552
uThr8 consume 8,2,289538
uThr8 consume 8,2,326642
uThr8 consume 8,2,433996
uThr8 consume 8,2,461490
uThr8 consume 8,3,211
uThr8 consume 8,3,239
uThr8 consume 8,3,277
uThr8 consume 8,4,14
uThr8 consume 8,4,32
uThr8 consume 8,4,44
uThr8 consume 8,4,50
uThr8 consume 8,4,64
uThr8 consume 8,4,66
uThr8 consume 8,4,72
uThr8 consume 8,5,17
uThr8 consume 8,5,29
uThr8 consume 8,5,41
uThr8 consume 8,5,7
uThr9 consume 9,1,1409869790947669143312035591975596518914
uThr9 consume 9,1,1923063428480944139667114773918309212080528
uThr9 consume 9,1,5358359254990966640871840
uThr9 consume 9,1,57602132235424755886206198685365216
uThr9 consume 9,1,5972304273877744135569338397692020533504
uThr9 consume 9,2,239908
uThr9 consume 9,2,263233
uThr9 consume 9,2,310169
uThr9 consume 9,2,332160
uThr9 consume 9,2,359283
uThr9 consume 9,2,480383
uThr9 consume 9,2,499679
uThr9 consume 9,3,223
uThr9 consume 9,3,257
uThr9 consume 9,3,281
uThr9 consume 9,3,293
uThr9 consume 9,4,30
uThr9 consume 9,4,52
uThr9 consume 9,5,11
uThr9 consume 9,5,47
*/