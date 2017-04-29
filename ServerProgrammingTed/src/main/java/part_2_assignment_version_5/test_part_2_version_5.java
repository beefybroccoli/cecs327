package part_2_assignment_version_5;

import com.google.common.util.concurrent.Striped;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

public class test_part_2_version_5 {

    public static void main(String[] args) {
        String inputHostName = VALUE.VALUE.LOCAL_HOST;
//        inputHostName = "192.168.1.5";
//        runTest_one_time(inputHostName, 10);
        runTest_one_time(inputHostName, 100);
//        runTest_one_time(inputHostName, 1000);

    }

    public static void runTest_one_time(String inputHostName, int number_of_uThreads) {

        LinkedBlockingQueue inputRequestQue = new LinkedBlockingQueue();
        ConcurrentHashMap<String, String> inputResultQue = new ConcurrentHashMap<String, String>();
        Striped<ReadWriteLock> inputSharedRWLock = Striped.readWriteLock(12);

        ExecutorService executorRuntime = Executors.newFixedThreadPool(1);
        executorRuntime.submit(new Runtime_version_5(inputHostName, inputRequestQue, inputResultQue, inputSharedRWLock));

        ExecutorService executorUThread = Executors.newFixedThreadPool(number_of_uThreads);
        //spawn 10 uThr
        for (int i = 0; i < number_of_uThreads; i++) {
            int id = i + 1;
            executorUThread.submit(new Client_version_5(id, inputRequestQue, inputResultQue, inputSharedRWLock));
        }

        int time_in_seconds = 1;
        switch (number_of_uThreads) {

            case 10:
                time_in_seconds = 10;
                break;
            case 100:
                time_in_seconds = 30;
                break;
            case 1000:
                time_in_seconds = 200;
                break;
            default:
                time_in_seconds = 200;
                break;
        }

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
------------------------------------------------------------------------
Building ServerProgrammingTed 1.0-SNAPSHOT
------------------------------------------------------------------------

--- exec-maven-plugin:1.2.1:exec (default-cli) @ ServerProgrammingTed ---
(Runtime Started)

(Client 1 Started)

(Client 2 Started)

(Client 3 Started)

(Client 4 Started)

(Client 5 Started)

(Client 6 Started)

(Client 7 Started)

(Client 8 Started)

(Client 9 Started)

(Client 10 Started)

(Client 11 Started)

(Client 12 Started)

(Client 13 Started)

(Client 14 Started)

(Client 15 Started)

(Client 16 Started)

(Client 17 Started)

(Client 18 Started)

(Client 19 Started)

(Client 20 Started)

(Client 21 Started)

(Client 22 Started)

(Client 23 Started)

(Client 24 Started)

(Client 25 Started)

(Client 26 Started)

(Client 27 Started)

(Client 28 Started)

(Client 29 Started)

(Client 30 Started)

(Client 31 Started)

(Client 32 Started)

(Client 33 Started)

(Client 34 Started)

(Client 35 Started)

(Client 36 Started)

(Client 37 Started)

(Client 38 Started)

(Client 39 Started)

(Client 40 Started)

(Client 41 Started)

(Client 42 Started)

(Client 43 Started)

(Client 44 Started)

(Client 45 Started)

(Client 46 Started)

(Client 47 Started)

(Client 48 Started)

(Client 49 Started)

(Client 50 Started)

(Client 51 Started)

(Client 52 Started)

(Client 53 Started)

(Client 54 Started)

(Client 55 Started)

(Client 56 Started)

(Client 57 Started)

(Client 58 Started)

(Client 59 Started)

(Client 60 Started)

(Client 61 Started)

(Client 62 Started)

(Client 63 Started)

(Client 64 Started)

(Client 65 Started)

(Client 66 Started)

(Client 67 Started)

(Client 68 Started)

(Client 69 Started)

(Client 70 Started)

(Client 71 Started)

(Client 72 Started)

(Client 73 Started)

(Client 74 Started)

(Client 75 Started)

(Client 76 Started)

(Client 77 Started)

(Client 78 Started)

(Client 79 Started)

(Client 80 Started)

(Client 81 Started)

(Client 82 Started)

(Client 83 Started)

(Client 84 Started)

(Client 85 Started)

(Client 86 Started)

(Client 87 Started)

(Client 88 Started)

(Client 89 Started)

(Client 90 Started)

(Client 91 Started)

(Client 92 Started)

(Client 93 Started)

(Client 94 Started)

(Client 95 Started)

(Client 96 Started)

(Client 97 Started)

(Client 98 Started)

(Client 99 Started)

(Client 100 Started)

localWorker reprocess - coomandId 1 5,5,-1

networkWorker reprocess - coomandId 1 9,1,-1

commandID 1 - Client4 consume 4,2,857750

commandID 1 - Client9 consume 9,1,5521577958796399352092495436683350598124375604156506038828319671569975838863744971896028280751218827164657336

commandID 1 - Client5 consume 5,5,5

commandID 1 - Client7 consume 7,2,840936

commandID 1 - Client8 consume 8,2,848506

commandID 1 - Client1 consume 1,4,4

commandID 1 - Client6 consume 6,1,307706993434466865456401903551551578318956034208374691542093028553513056268030561242600951135606345897560112

localWorker reprocess - coomandId 1 10,4,-1

networkWorker reprocess - coomandId 1 13,1,-1

commandID 1 - Client15 consume 15,2,859669

commandID 1 - Client10 consume 10,4,8

commandID 1 - Client13 consume 13,1,99080696264900721472208515956748759187919804840608734007367661059706052043279378932885908102386332543066271936

commandID 1 - Client12 consume 12,4,12

commandID 1 - Client11 consume 11,5,9

localWorker reprocess - coomandId 1 18,4,-1

commandID 1 - Client2 consume 2,4,10

commandID 1 - Client17 consume 17,1,419712564636128966418863068957011388899128076671547993021605479585858227224221424221791102364954108601240491394

commandID 1 - Client14 consume 14,4,14

commandID 1 - Client16 consume 16,4,16

commandID 1 - Client3 consume 3,5,7

localWorker reprocess - coomandId 1 18,4,-1

networkWorker reprocess - coomandId 1 28,2,-1

commandID 1 - Client34 consume 34,4,28

commandID 1 - Client18 consume 18,4,22

commandID 1 - Client24 consume 24,1,7531436383873795315009506236096188648036856522778750817396763797198414070984881727501992372613765176393353441442

commandID 1 - Client19 consume 19,3,877

commandID 1 - Client21 consume 21,2,866481

commandID 1 - Client22 consume 22,2,868694

commandID 1 - Client32 consume 32,4,26

commandID 1 - Client23 consume 23,1,1777930954809416587147660791784794314784432111526800706093789579403138960940165075820050317562202766948028237512

commandID 1 - Client31 consume 31,3,883

commandID 1 - Client28 consume 28,2,881353

commandID 1 - Client20 consume 20,5,11

commandID 1 - Client30 consume 30,2,889955

commandID 1 - Client25 consume 25,4,24

commandID 1 - Client27 consume 27,5,13

commandID 1 - Client33 consume 33,1,31903676490304597847185685736169548906931858202641803975680844768196795244879691985828019808017263472521442003280

commandID 1 - Client26 consume 26,3,881

commandID 1 - Client29 consume 29,2,884037

localWorker reprocess - coomandId 1 35,5,-1

localWorker reprocess - coomandId 1 41,5,-1

commandID 1 - Client45 consume 45,4,32

commandID 1 - Client39 consume 39,1,135146142345092186703752249180774384275764289333345966720120142869985595050503649670814071604682819066479121454562

commandID 1 - Client41 consume 41,5,21

commandID 1 - Client35 consume 35,5,17

commandID 1 - Client46 consume 46,1,2425099125827785565352530979017842728315720351477448650144765807862542296838080812347151296511676978020230832740674

commandID 1 - Client42 consume 42,1,572488245870673344662194682459267086009989015536025670856161416248139175446894290669084306226748539738437927821528

commandID 1 - Client40 consume 40,3,911

commandID 1 - Client43 consume 43,3,919

commandID 1 - Client37 consume 37,3,907

commandID 1 - Client38 consume 38,4,30

commandID 1 - Client36 consume 36,3,887

commandID 1 - Client44 consume 44,4,34

commandID 1 - Client47 consume 47,4,36

networkWorker reprocess - coomandId 1 50,2,-1

localWorker reprocess - coomandId 1 55,4,-1

commandID 1 - Client56 consume 56,1,43516638122555047989641805373140394725407202037260729735885664398655775748034950972577909265605502785297675867877570

commandID 1 - Client54 consume 54,5,25

commandID 1 - Client49 consume 49,5,23

commandID 1 - Client50 consume 50,2,901494

commandID 1 - Client57 consume 57,4,44

commandID 1 - Client62 consume 62,4,46

commandID 1 - Client52 consume 52,1,10272884749181815606072318598530637999272870421445820271435224647698308362799217540057689492273456451819361258784224

commandID 1 - Client51 consume 51,3,929

commandID 1 - Client58 consume 58,3,941

commandID 1 - Client48 consume 48,4,38

commandID 1 - Client53 consume 53,3,937

commandID 1 - Client59 consume 59,5,27

commandID 1 - Client60 consume 60,5,29

commandID 1 - Client61 consume 61,3,947

commandID 1 - Client55 consume 55,4,42

commandID 1 - Client67 consume 67,1,3307836985560054320557439403041129266216957343847351485598166655714087096026103168206590188492244960222361803886516848

commandID 1 - Client71 consume 71,1,59356726302841575762469269714649234575004330510681838001552021920611246317114918006288254066305713816409502405227008760

commandID 1 - Client75 consume 75,5,37

commandID 1 - Client85 consume 85,3,983

commandID 1 - Client84 consume 84,2,920521

commandID 1 - Client76 consume 76,4,48

commandID 1 - Client80 consume 80,3,977

commandID 1 - Client64 consume 64,1,780874387080163078248199965737509262329013916319215686595797193367941421167791036694055215484387373157337934789055586

commandID 1 - Client65 consume 65,3,953

commandID 1 - Client82 consume 82,5,41

commandID 1 - Client63 consume 63,1,184339437239402007564639540091092216900901678570488739214977882242321411354939021430369326554695467593010064730294504

commandID 1 - Client66 consume 66,2,904730

commandID 1 - Client81 consume 81,5,39

commandID 1 - Client79 consume 79,1,251439127540686683410355036436498964627214165334435973635196551498669275073731875734673432234676222479684794771243158018

commandID 1 - Client73 consume 73,2,908827

commandID 1 - Client72 consume 72,5,33

commandID 1 - Client69 consume 69,1,14012222329320380360477957577902026327196843291708621628988463816224289805272203709520415969453367214046785150335122978

commandID 1 - Client77 consume 77,2,913052

commandID 1 - Client78 consume 78,3,971

commandID 1 - Client74 consume 74,5,35

commandID 1 - Client68 consume 68,5,31

commandID 1 - Client83 consume 83,1,1065113236465588309403889415460645093083860991848425732542338227915288346612042420944981983005010603735148681490199640832

commandID 1 - Client70 consume 70,3,967

networkWorker reprocess - coomandId 1 94,2,-1

commandID 2 - Client4 consume 4,3,997

commandID 2 - Client5 consume 5,3,1009

commandID 2 - Client8 consume 8,5,49

commandID 2 - Client1 consume 1,3,1013

commandID 2 - Client7 consume 7,1,6154224095958732012333665477892067256746331909998525312140891065916653824402969753281084467433624713686221488640308794432792

commandID 2 - Client9 consume 9,2,946835

commandID 1 - Client90 consume 90,5,47

commandID 1 - Client87 consume 87,5,45

commandID 1 - Client91 consume 91,1,4511892073403039921025912698279079336962658132728138903804549463159822661521901559514601364254718637420279520732041721346

commandID 1 - Client98 consume 98,4,54

commandID 1 - Client93 consume 93,1,80962618193714031895056073532586929100700632223772064294846693785378138632320496195528151124350259251085346578405507826210

commandID 1 - Client88 consume 88,2,930174

commandID 1 - Client95 consume 95,1,342963154304933875573731834338924678843737022417849238527147311222067133521981633441115991937424922157757653078040397831056

commandID 1 - Client100 consume 100,3,991

commandID 1 - Client89 consume 89,4,50

commandID 1 - Client86 consume 86,5,43

commandID 1 - Client92 consume 92,1,19112681530077747993507540208576962440934493522760981347760536080554578992699648659003387440023885153416266764418366526216

commandID 1 - Client97 consume 97,4,52

commandID 1 - Client94 consume 94,2,938077

commandID 1 - Client96 consume 96,1,1452815235413449534189983410888285644475648721895169018403435938673646672720247029959992118874049947882115958890567099150434

commandID 1 - Client99 consume 99,2,946681

networkWorker reprocess - coomandId 2 10,1,-1

networkWorker reprocess - coomandId 2 10,1,-1

localWorker reprocess - coomandId 2 11,4,-1

commandID 2 - Client19 consume 19,5,55

commandID 2 - Client14 consume 14,2,956417

commandID 2 - Client10 consume 10,1,467801993911057346969253632393329698441821925792111695787002567703451068793258021745557947676079828499403918483241873884718402

commandID 2 - Client6 consume 6,4,56

commandID 2 - Client17 consume 17,4,62

commandID 2 - Client2 consume 2,2,956147

commandID 2 - Client12 consume 12,1,1981641046217181630223446776341037079709877940526002389528019162689081976878763560907850195126187133921809903075415013440832808

commandID 2 - Client21 consume 21,4,64

commandID 2 - Client18 consume 18,3,1019

commandID 2 - Client24 consume 24,5,53

commandID 2 - Client3 consume 3,1,8394366178779783867863040737757478017281333687896121253899079218459778976308312265376958728180828364186643530784901927648049634

commandID 2 - Client11 consume 11,4,60

commandID 2 - Client15 consume 15,2,946958

commandID 2 - Client16 consume 16,5,51

commandID 2 - Client13 consume 13,2,947649

commandID 2 - Client34 consume 34,2,960218

localWorker reprocess - coomandId 2 22,5,-1

networkWorker reprocess - coomandId 2 33,3,-1

commandID 2 - Client31 consume 31,1,35559105761336317101675609727370949148835212692110487405124336036528197882112012622415685107849500590668384026215022724033031344

commandID 2 - Client23 consume 23,3,1021

commandID 2 - Client30 consume 30,3,1033

commandID 2 - Client26 consume 26,5,61

commandID 2 - Client29 consume 29,1,638082262657836526199937528316336047599323950517462770902710029494818479901137463642574481746164823498109102568794994019153731384

commandID 2 - Client39 consume 39,3,1061

commandID 2 - Client45 consume 45,3,1063

commandID 2 - Client35 consume 35,1,2702959839855471157074315592912585465009917986526189154485236541343846490109306217325337626144238124719296589910824968900395100546

commandID 2 - Client46 consume 46,4,68

commandID 2 - Client42 consume 42,1,11449921622079721154497199899966677907638995896622219388843656194870204440338362332943924986323117322375295462212094869620734133568

commandID 2 - Client25 consume 25,3,1039

commandID 2 - Client22 consume 22,5,59

commandID 2 - Client20 consume 20,1,150630789224125052274565479647241274612622184456338070874396423364572570504756362755039699159578830726860179635644992823780175010

commandID 2 - Client28 consume 28,3,1031

commandID 2 - Client40 consume 40,5,65

commandID 2 - Client37 consume 37,3,1069

commandID 2 - Client43 consume 43,2,961838

commandID 2 - Client41 consume 41,5,63

commandID 2 - Client33 consume 33,3,1051

commandID 2 - Client32 consume 32,2,960982

commandID 2 - Client27 consume 27,4,66

networkWorker reprocess - coomandId 2 44,3,-1

commandID 2 - Client56 consume 56,2,971649

commandID 2 - Client50 consume 50,5,73

commandID 2 - Client47 consume 47,5,69

commandID 2 - Client38 consume 38,5,67

commandID 2 - Client48 consume 48,4,72

commandID 2 - Client54 consume 54,3,1093

commandID 2 - Client44 consume 44,3,1091

commandID 2 - Client49 consume 49,5,71

commandID 2 - Client57 consume 57,1,48502646328174355775063115192779297095565901573015066709859861320824664251462755549101037571436707414220478438759204447383331634818

commandID 2 - Client36 consume 36,4,70

commandID 2 - Client60 consume 60,4,74

commandID 2 - Client52 consume 52,3,1097

commandID 2 - Client58 consume 58,3,1109

commandID 2 - Client51 consume 51,3,1103

commandID 2 - Client59 consume 59,5,77

commandID 2 - Client62 consume 62,2,976102

commandID 2 - Client53 consume 53,5,75

networkWorker reprocess - coomandId 2 64,2,-1

commandID 2 - Client67 consume 67,4,76

commandID 2 - Client85 consume 85,4,78

commandID 2 - Client55 consume 55,3,1117

commandID 2 - Client63 consume 63,5,85

commandID 2 - Client76 consume 76,5,83

commandID 2 - Client65 consume 65,3,1129

commandID 2 - Client61 consume 61,5,79

commandID 2 - Client71 consume 71,2,979969

commandID 2 - Client84 consume 84,3,1123

commandID 2 - Client80 consume 80,4,80

commandID 2 - Client82 consume 82,4,82

commandID 2 - Client75 consume 75,5,81

commandID 2 - Client64 consume 64,2,987201

networkWorker reprocess - coomandId 3 5,2,-1

localWorker reprocess - coomandId 3 7,4,-1

commandID 3 - Client8 consume 8,2,1027929

commandID 3 - Client4 consume 4,4,90

commandID 2 - Client81 consume 81,3,1151

commandID 2 - Client72 consume 72,5,89

commandID 3 - Client5 consume 5,2,1025835

commandID 2 - Client74 consume 74,2,1000103

commandID 2 - Client83 consume 83,2,1010022

commandID 2 - Client69 consume 69,4,84

commandID 2 - Client70 consume 70,3,1163

commandID 2 - Client78 consume 78,4,86

commandID 2 - Client77 consume 77,3,1153

commandID 3 - Client1 consume 1,5,91

commandID 2 - Client73 consume 73,1,205460506934777144254749660671083866289902602188682486228283101478168861446189384529348075272069946979257209217248912659154060672840

commandID 2 - Client66 consume 66,5,87

commandID 2 - Client68 consume 68,4,88

commandID 2 - Client79 consume 79,2,992029

localWorker reprocess - coomandId 2 92,5,-1

commandID 2 - Client98 consume 98,4,98

commandID 2 - Client95 consume 95,1,3686839203203908875430996692179542915310607843499662532720252170412169301591070559195321429910935928304254470448268332995152357977552

commandID 2 - Client91 consume 91,5,95

commandID 2 - Client86 consume 86,2,1050686

commandID 2 - Client88 consume 88,1,870344674067282932794061757877114762255176310327745011622992267233500110036220293666493338659716495331249315307754855083999574326178

commandID 3 - Client7 consume 7,4,94

commandID 2 - Client90 consume 90,5,93

commandID 2 - Client87 consume 87,4,96

commandID 2 - Client93 consume 93,2,1042350

commandID 3 - Client9 consume 9,2,1032573

commandID 2 - Client92 consume 92,5,99

commandID 2 - Client89 consume 89,4,100

commandID 2 - Client100 consume 100,2,1046414

commandID 3 - Client10 consume 10,4,102

commandID 3 - Client6 consume 6,1,280248282089825248888530811720838040860701762007547367553449024812645691585172825254393529710802567258537560232507152512078962537928770

commandID 3 - Client2 consume 2,5,103

commandID 3 - Client19 consume 19,2,1051051

commandID 2 - Client96 consume 96,5,101

commandID 2 - Client94 consume 94,3,1171

commandID 3 - Client24 consume 24,2,1051783

commandID 3 - Client11 consume 11,5,111

commandID 3 - Client15 consume 15,1,1187150773510036578167626437681912852052108086610994713316532355216523644907884381698560556506335045796647564188880191129569438534638176

commandID 3 - Client21 consume 21,5,107

commandID 3 - Client14 consume 14,1,66157645150735582613503190798560688609301038580805243102736255965940878567193080680986437663124776762497323258851581081253588382923096

commandID 3 - Client12 consume 12,5,105

commandID 2 - Client97 consume 97,1,15617701486882918434518048526595286423497607684326395142504000948882177316400502530447779058303460208548267197100828187064609006236386

commandID 2 - Client99 consume 99,3,1181

commandID 3 - Client18 consume 18,4,104

commandID 3 - Client17 consume 17,3,1187

commandID 3 - Client3 consume 3,5,109

localWorker reprocess - coomandId 3 23,5,-1

localWorker reprocess - coomandId 3 26,5,-1

commandID 3 - Client26 consume 26,5,121

commandID 3 - Client23 consume 23,5,117

commandID 3 - Client39 consume 39,3,1201

commandID 3 - Client22 consume 22,3,1213

commandID 3 - Client35 consume 35,1,90239076488249662859174127312351972042383712190119924607198962997404679190315613511621050073539766940753763145551995354034341937638737762

commandID 3 - Client20 consume 20,3,1217

commandID 3 - Client42 consume 42,2,1058864

commandID 3 - Client46 consume 46,1,382258862231028574261100281936883758817863493280896798025390698127550201491037179836377303873609973810592211414348973275388364055795515120

commandID 3 - Client13 consume 13,2,1056599

commandID 3 - Client16 consume 16,5,113

commandID 3 - Client30 consume 30,1,21302556278029922824403772687475870648328644520417099596594846137931484729774725789893103579450906047577158832140991859250996305240564072

commandID 3 - Client29 consume 29,4,106

commandID 3 - Client45 consume 45,4,108

commandID 3 - Client34 consume 34,1,5028851376129971561559036562448489449069134108451526220819578445678740271216710352048635755736142750445127816988027917030356716676481474

commandID 3 - Client31 consume 31,3,1193

commandID 3 - Client25 consume 25,4,110

localWorker reprocess - coomandId 3 33,4,-1

commandID 3 - Client50 consume 50,4,116

commandID 3 - Client56 consume 56,3,1237

commandID 3 - Client38 consume 38,5,129

commandID 3 - Client40 consume 40,5,125

commandID 3 - Client43 consume 43,2,1059218

commandID 3 - Client44 consume 44,1,1619274525412363959903575255059887007313837685313707116708761755507605485154464332857130265567979662183122608802947888455587798160820798242

commandID 3 - Client54 consume 54,3,1277

commandID 3 - Client36 consume 36,2,1068142

commandID 3 - Client48 consume 48,3,1259

commandID 3 - Client33 consume 33,4,114

commandID 3 - Client32 consume 32,3,1229

commandID 3 - Client37 consume 37,3,1223

commandID 3 - Client27 consume 27,3,1231

commandID 3 - Client41 consume 41,5,127

commandID 3 - Client57 consume 57,1,6859356963880484413875401302176431788073214234535725264860437720157972142108894511264898366145528622543082646626140527097739556699078708088

commandID 3 - Client49 consume 49,4,118

commandID 3 - Client47 consume 47,3,1249

commandID 3 - Client28 consume 28,5,123

networkWorker reprocess - coomandId 3 61,2,-1

networkWorker reprocess - coomandId 3 61,2,-1

commandID 3 - Client85 consume 85,5,135

commandID 3 - Client65 consume 65,4,122

commandID 3 - Client61 consume 61,2,1094676

commandID 3 - Client52 consume 52,2,1078134

commandID 3 - Client84 consume 84,4,126

commandID 3 - Client51 consume 51,5,131

commandID 3 - Client63 consume 63,3,1283

commandID 3 - Client55 consume 55,4,124

commandID 3 - Client59 consume 59,1,29056702380934301615405180463765614159606694623456608176150512636139494053590042377916723730150094152355453195307509996846546024957135630594

commandID 3 - Client60 consume 60,4,120

commandID 3 - Client58 consume 58,2,1081119

commandID 3 - Client76 consume 76,2,1090400

commandID 3 - Client71 consume 71,2,1096793

commandID 3 - Client67 consume 67,1,123086166487617690875496123157238888426499992728362157969462488264715948356469064022931793286745905231964895427856180514483923656527621230464

commandID 3 - Client53 consume 53,5,133

commandID 3 - Client62 consume 62,3,1279

networkWorker reprocess - coomandId 3 82,3,-1

commandID 4 - Client4 consume 4,5,137

commandID 3 - Client64 consume 64,2,1106173

commandID 3 - Client72 consume 72,2,1109030

commandID 3 - Client69 consume 69,3,1301

commandID 3 - Client83 consume 83,1,2208691639813237951345054815528123559888926654875983118185464351044729098274334257901507380795280765552825035054785108733612886260798103440264

commandID 3 - Client80 consume 80,4,128

commandID 3 - Client75 consume 75,1,521401368331405065117389673092721167865606665536905240054000465695003287479466298469643896877133715080215034906732232054782240651067620552450

commandID 3 - Client68 consume 68,1,39633363350150665433335490556348985189574179795039333969368895830540407820581547578204201061028307874718885735558275776690548029037838240694288

commandID 3 - Client73 consume 73,4,134

commandID 4 - Client1 consume 1,1,9356167927584356870497608935205215407421313285040837712795857869873919680576803330075673420058256777291515175125872666989233785694260034313506

commandID 3 - Client78 consume 78,5,143

commandID 4 - Client5 consume 5,4,130

commandID 3 - Client81 consume 81,5,139

commandID 3 - Client77 consume 77,4,132

commandID 4 - Client8 consume 8,3,1297

commandID 3 - Client82 consume 82,3,1291

commandID 3 - Client79 consume 79,5,145

commandID 3 - Client70 consume 70,3,1303

commandID 3 - Client74 consume 74,5,141

commandID 3 - Client66 consume 66,3,1307

localWorker reprocess - coomandId 4 9,5,-1

localWorker reprocess - coomandId 3 89,5,-1

commandID 3 - Client95 consume 95,5,147

commandID 3 - Client86 consume 86,5,151

commandID 4 - Client7 consume 7,1,167889621328187018603839571160601156165718032465198173590271441192035550962902993642892477664171488276167058117358975773751425901845612997090658

commandID 3 - Client91 consume 91,5,149

commandID 3 - Client98 consume 98,3,1319

commandID 3 - Client87 consume 87,4,136

commandID 3 - Client88 consume 88,3,1321

commandID 3 - Client93 consume 93,4,138

commandID 3 - Client92 consume 92,3,1327

commandID 4 - Client9 consume 9,5,155

commandID 3 - Client90 consume 90,1,711191848662898739848693775198753609852446309655832028330454660598682611672193522149774111717714260979387118204994178871696251636420290229056920

commandID 3 - Client96 consume 96,5,165

commandID 4 - Client11 consume 11,1,54059936666307888585371224524040479564193340847128274990827350063369752406767284486712908163966342091210712498754683466915904358153636317442639426

commandID 4 - Client6 consume 6,3,1361

commandID 4 - Client19 consume 19,5,163

commandID 4 - Client15 consume 15,1,229001566577813580993328050559183134248927822782523037139288215248424756229348039797969362465723196754597099236973070811577459414041072655652887976

commandID 4 - Client26 consume 26,5,167

commandID 4 - Client10 consume 10,2,1115725

commandID 4 - Client21 consume 21,4,142

commandID 4 - Client14 consume 14,2,1121717

commandID 3 - Client89 consume 89,5,159

commandID 4 - Client17 consume 17,2,1122463

commandID 4 - Client23 consume 23,5,169

commandID 4 - Client3 consume 3,2,1123873

commandID 4 - Client12 consume 12,3,1367

commandID 4 - Client18 consume 18,3,1381

commandID 3 - Client99 consume 99,3,1373

commandID 3 - Client100 consume 100,5,161

commandID 4 - Client24 consume 24,4,140

commandID 3 - Client94 consume 94,1,12761819912582026651843152463021215992154459394009937175978814994945746602278901851117729809857828389754249241954336943913841981426527385882330272

commandID 4 - Client2 consume 2,1,3012657015979781977998614671955615595575503271088526286912090083586765997651677082241988924535028532193715530937335691260536432447526773913318338

commandID 4 - Client39 consume 39,3,1399

commandID 3 - Client97 consume 97,1,970066202977562212558683426760773016559904631977220423547980211057068777324159443678590358026859129109599109446646966713225742014317926940054191330

networkWorker reprocess - coomandId 4 42,1,-1

networkWorker reprocess - coomandId 4 31,3,-1

localWorker reprocess - coomandId 4 25,4,-1

networkWorker reprocess - coomandId 4 38,1,-1

commandID 4 - Client56 consume 56,3,1429

commandID 4 - Client50 consume 50,1,5605042352914733135977054235984175329081972797846347142062056069216302203343442302671901445786714383823775378215668056310042563607303080014020120414082

commandID 4 - Client38 consume 38,1,100578404047763437394925058182912858967758816645240854669260846530023147100649579758906802170865134804283198088355735627634798716539277515304438631163554

commandID 4 - Client22 consume 22,4,144

commandID 4 - Client20 consume 20,1,4109266378488062431228061757602275200488546350691404731331209059476699865525985814512330794573159713192993537023560937664480427471312780415869653296

commandID 4 - Client42 consume 42,1,73737793246207310181111783586281770474544906489662762126822474855332172823238396621423984939851151640719286567187123807149070235069588974830000871352

commandID 4 - Client16 consume 16,5,173

commandID 4 - Client45 consume 45,1,1323171012053243520828784042795469593341319770463238313551473338336502410952765153371119398122747569819754164672344667591018783803781288766524146031040

commandID 4 - Client13 consume 13,5,171

commandID 4 - Client29 consume 29,1,312358304701759052661918064802296955716693715993393887856162715870292559532381689187423853295724104544758719526289385945967428392177924947923536289922

commandID 4 - Client30 consume 30,4,148

commandID 4 - Client46 consume 46,4,146

commandID 4 - Client35 consume 35,3,1409

commandID 4 - Client25 consume 25,4,152

commandID 4 - Client34 consume 34,2,1126914

commandID 4 - Client31 consume 31,3,1427

networkWorker reprocess - coomandId 4 33,2,-1

commandID 4 - Client40 consume 40,4,154

commandID 4 - Client44 consume 44,5,177

commandID 4 - Client54 consume 54,5,179

commandID 4 - Client32 consume 32,5,181

commandID 4 - Client57 consume 57,1,1804806230506827139972673993056447286090576726816489036904633181471200345608348993357650537629785712093273790212187573241116334334099692195465875240529890

commandID 4 - Client48 consume 48,4,156

commandID 4 - Client28 consume 28,1,137189016858942574813987960473276725913793500449015015431633921489426427977458850029545499585044983724193922911803790583217672598429809600464229122907959008

commandID 4 - Client43 consume 43,5,175

commandID 4 - Client85 consume 85,2,1150239

commandID 4 - Client37 consume 37,1,426056956614765925644437233718383606780704477542812045558843083735294299626924853399685933864730144322247648030957959403370383904390103670040359152341584

commandID 4 - Client47 consume 47,1,32385933745075125082113206816833138290662622266051561809614136419951583073849632300678802875165277682874645025731020582712459219297255182003081315698374466

commandID 4 - Client49 consume 49,1,7645281878642074485535133205944172751143011384808768193177375809620095682060320826830288084383872992695342808879708252367835721240788872451903860114461144

commandID 4 - Client27 consume 27,3,1433

commandID 4 - Client33 consume 33,2,1142880

commandID 4 - Client65 consume 65,4,158

commandID 4 - Client41 consume 41,5,183

commandID 4 - Client36 consume 36,2,1135507

networkWorker reprocess - coomandId 4 76,3,-1

localWorker reprocess - coomandId 4 64,4,-1

commandID 4 - Client61 consume 61,4,160

commandID 4 - Client64 consume 64,4,170

commandID 4 - Client51 consume 51,3,1447

commandID 4 - Client55 consume 55,1,581142001180845424338065048709940041945836624062111623536149822377657294983685032418860801215345212579650336672946182915583149613016493583859997807330210498

commandID 4 - Client76 consume 76,3,1459

commandID 4 - Client58 consume 58,3,1451

commandID 5 - Client4 consume 4,4,166

commandID 4 - Client72 consume 72,5,187

commandID 4 - Client52 consume 52,4,162

commandID 4 - Client84 consume 84,3,1439

commandID 4 - Client63 consume 63,5,185

commandID 4 - Client67 consume 67,2,1154738

commandID 4 - Client59 consume 59,1,2461757021582324272166248155313036893697139996697461509576233211000055607912198979704988704446425834042795269603588522245550271050495783935904220352228801000

commandID 4 - Client60 consume 60,1,10428170087510142513003057669962087616734396610851957661841082666377879726632480951238815619001048548750831415087300271897784233814999629327476879216245414498

commandID 4 - Client71 consume 71,3,1471

commandID 4 - Client53 consume 53,2,1160212

commandID 4 - Client62 consume 62,4,164

networkWorker reprocess - coomandId 4 68,1,-1

localWorker reprocess - coomandId 4 73,4,-1

commandID 4 - Client95 consume 95,4,182

commandID 4 - Client91 consume 91,2,1180111

commandID 4 - Client69 consume 69,4,172

commandID 5 - Client1 consume 1,2,1169711

commandID 4 - Client78 consume 78,3,1483

commandID 4 - Client80 consume 80,1,187125919574001719809716973010607637059273302371273126289603338172424177784400972089879820340803528664935315134898458711244533059056976834310723828085087250466

commandID 4 - Client81 consume 81,2,1175567

commandID 4 - Client79 consume 79,3,1489

commandID 4 - Client74 consume 74,4,180

commandID 4 - Client66 consume 66,1,60253964960827372933304527244366949193044057526925884553628738741698207589282129327908883288937520884896591823100630758837824061866733524154469212645590764439554

commandID 5 - Client7 consume 7,3,1493

commandID 4 - Client86 consume 86,1,255239891487955204763028765174429290225574698227916993274034384901108067924350903389446104490355598143955494037924409485928126310226314831809427765737181917594632

commandID 4 - Client68 consume 68,1,3357838382244520814061902456520975379450185046072064315551019004437257320392585016666597950515462467420084841013084956530503810829210583388265552026315325093890

commandID 5 - Client8 consume 8,1,14224031644645713029810656196961493453398468120213455059519429934315237567222386077810571334605514604369126745521886450576830062759380735191550915154818859836416

commandID 4 - Client82 consume 82,2,1177765

commandID 5 - Client5 consume 5,4,178

commandID 4 - Client75 consume 75,3,1481

commandID 4 - Client83 consume 83,1,44174437371622894324178478835161387360634726440105292156940563876511574514442122784660251180450620029046120929952789609836687206310494301245811737217210458992

commandID 4 - Client77 consume 77,3,1487

commandID 4 - Client73 consume 73,4,176

commandID 4 - Client70 consume 70,5,189

networkWorker reprocess - coomandId 4 89,3,-1

networkWorker reprocess - coomandId 4 89,3,-1

commandID 5 - Client6 consume 6,4,186

commandID 4 - Client98 consume 98,5,191

commandID 5 - Client21 consume 21,2,1185370

commandID 5 - Client15 consume 15,5,199

commandID 5 - Client17 consume 17,1,348147399115490473298491045414894562475320947656181150098909190608449577072992416624176576404665376671883958803044029437264675531681752487564893783189344220686328514

commandID 5 - Client23 consume 23,2,1187263

commandID 5 - Client9 consume 9,3,1511

commandID 4 - Client90 consume 90,5,197

commandID 5 - Client19 consume 19,1,19401589591466840082804248055713147032523127250367763553142164271488650419571061242614570539217540921408037631723268205887068103388029137800904775748052141062285922

commandID 5 - Client11 consume 11,4,184

commandID 5 - Client10 consume 10,2,1180212

commandID 4 - Client88 consume 88,3,1499

commandID 4 - Client96 consume 96,1,4580094015138547972704707116942765730606946099982292423873099498285629985071093874932219309491795251986829765937117484296129443521314286237378148868114455656866960

commandID 4 - Client92 consume 92,5,193

commandID 4 - Client93 consume 93,5,195

commandID 5 - Client26 consume 26,3,1523

commandID 4 - Client89 consume 89,3,1549

commandID 4 - Client87 consume 87,1,1081213530912648191985419587942084110095342850438593857649766278346130479286685742885693301250359913460718567974798268702550329302771992851392180275594318434818082

commandID 5 - Client14 consume 14,1,82186452381005908303921699339795353860699455101453346636441756584240231663355338845390501466361958937618980292830190307844401857073430837440997251860323019906010648

localWorker reprocess - coomandId 5 3,5,-1

networkWorker reprocess - coomandId 5 2,1,-1

localWorker reprocess - coomandId 5 13,5,-1

commandID 5 - Client56 consume 56,1,474873307633420493534346548974681357645627998177729316651905410024310124235629580626280239662068064136047863762846094076838503353340220619353346929698031254198069687728

commandID 5 - Client20 consume 20,1,8521255754974776469099579857385615508107448968231157950034356408851841782753799956515731961891161094026546380032432410028376648890272636645019075428893304300337436545080

commandID 5 - Client22 consume 22,2,1204546

commandID 5 - Client42 consume 42,4,192

commandID 5 - Client16 consume 16,4,194

commandID 5 - Client18 consume 18,4,188

commandID 4 - Client99 consume 99,2,1187816

commandID 4 - Client100 consume 100,1,1474776048842967801497885880999373603761983245726177947032078519018038539955325005342096807085023465625154815505006308056903103983800440787700572384617699902651324704

commandID 5 - Client50 consume 50,4,190

commandID 5 - Client13 consume 13,5,207

commandID 4 - Client97 consume 97,1,112102381301657019753922131204008107032943249802439891737991109609642417687024271467241971909001000928433174016012202680530522970872221529003044406006693244742562963426

commandID 5 - Client3 consume 3,5,203

commandID 5 - Client39 consume 39,2,1195004

commandID 5 - Client12 consume 12,3,1553

commandID 5 - Client24 consume 24,3,1559

commandID 5 - Client38 consume 38,1,2011595611835338993891308327102733537615455242513357158345612749706882914629542593972362930557273257472624629067396578987884536384233104006416432124798818261534841714338

commandID 5 - Client45 consume 45,1,36096618631734444870289627756645195570045251115437988958483038385114250045644742420035290778121917633578810149197126219101391131945323650586492733840372035462884587894658

commandID 5 - Client2 consume 2,1,26463782426792414518658024158648929513854998967969749699940971585740453487532494757312352026064060422315167698797283354716411469851334503341169305671258275227817834024

commandID 4 - Client94 consume 94,2,1191240

localWorker reprocess - coomandId 5 30,4,-1

localWorker reprocess - coomandId 5 57,5,-1

commandID 5 - Client54 consume 54,5,215

commandID 5 - Client44 consume 44,3,1583

commandID 5 - Client29 consume 29,5,209

commandID 5 - Client48 consume 48,3,1597

commandID 5 - Client57 consume 57,5,221

commandID 5 - Client40 consume 40,1,152907730281912555950258090883966397788288453429983113783966509949308841965332769636656895074378831628341786976820937286433941176671567238990990010790381446151875788123712

commandID 5 - Client34 consume 34,3,1579

commandID 5 - Client46 consume 46,3,1567

commandID 5 - Client31 consume 31,5,213

commandID 5 - Client35 consume 35,3,1571

commandID 5 - Client25 consume 25,5,211

commandID 5 - Client32 consume 32,5,217

commandID 5 - Client30 consume 30,4,198

commandID 5 - Client28 consume 28,3,1601

networkWorker reprocess - coomandId 5 85,3,-1

networkWorker reprocess - coomandId 5 41,3,-1

networkWorker reprocess - coomandId 5 64,1,-1

localWorker reprocess - coomandId 5 55,4,-1

networkWorker reprocess - coomandId 5 52,1,-1

commandID 5 - Client61 consume 61,3,1637

commandID 5 - Client64 consume 64,1,883500839105108321488176730469533687771801170063443463474008794546134903135239255428108782427408863084498422836903676843731957402396845655555436941177518324607263075960973912

commandID 5 - Client51 consume 51,5,225

commandID 5 - Client76 consume 76,2,1223231

commandID 5 - Client58 consume 58,3,1657

commandID 5 - Client43 consume 43,1,647727539759384668671321991292510786723199064835370444094349078182349617906975820966662871075637244146945958056480875364837155838631592606550452777001897820070387740389506

commandID 5 - Client65 consume 65,1,11622999097037189591213506215508548965447537915921230004739800368897178872279920034979896388583348477011448434867458630347967413963423343267321657252193788725804094739116450

commandID 5 - Client36 consume 36,1,49235814277468209595489570918088205406471236376456384909120564298267422802712916193422893933710321716261919358672578960137652220384891310734479430127573127629649805706147536

commandID 5 - Client27 consume 27,5,223

commandID 5 - Client37 consume 37,3,1613

commandID 5 - Client85 consume 85,3,1609

commandID 5 - Client52 consume 52,1,15853779289614481577191691577533518174485949824765525957623037737532160833631593681512535189759649213804709691705593604227037581022758330489263385511067756715301085561591382880

commandID 5 - Client41 consume 41,3,1627

commandID 6 - Client4 consume 4,4,204

commandID 5 - Client33 consume 33,2,1214118

commandID 5 - Client49 consume 49,3,1619

commandID 5 - Client72 consume 72,5,227

commandID 5 - Client55 consume 55,4,202

commandID 5 - Client47 consume 47,1,2743817889319451230635546056054009544681084712771464890161362822678707313593236053503308379376927808216125619202744438745782564531197937665192801118797972726433426749681736

networkWorker reprocess - coomandId 5 95,3,-1

networkWorker reprocess - coomandId 5 66,1,-1

commandID 5 - Client91 consume 91,4,210

commandID 5 - Client69 consume 69,5,233

commandID 5 - Client78 consume 78,1,284484526373955560067962271665133793452975295675716023773740670481032760102233447011797524633246276985400276027863781199242944501007253103151185502258042102550812277032683917928

commandID 6 - Client1 consume 1,2,1254010

commandID 5 - Client80 consume 80,2,1260134

commandID 5 - Client74 consume 74,5,235

commandID 5 - Client95 consume 95,3,1667

commandID 5 - Client67 consume 67,2,1239892

commandID 5 - Client84 consume 84,5,229

commandID 5 - Client66 consume 66,1,21624566574033249908479058525261934298547801008518093327427828213794236274252366061503132978728550110978101030934864543624309608482456326210698525158753672353459406510105385364770

commandID 6 - Client7 consume 7,1,91603133991574585233562363299442611958170273406410496211681021886098002618016034698543374823122873780435804382549300642479384397366933102369026176290148379503036945941448260598904

commandID 5 - Client79 consume 79,4,212

commandID 5 - Client60 consume 60,4,206

commandID 5 - Client81 consume 81,1,1205095792266907509894541731682435242631523519165601719548992090110006190226084251379761345893856764884499995695494671695725754734025136105770222538218911996662126905998508805474

commandID 5 - Client59 consume 59,1,67157686771085269622692645021900068819622336462737624454029408185875149817150463332571247360871656942898891584039546898753976729996123693165480529186743586458877797867773133762

commandID 5 - Client86 consume 86,1,388037102540331590842728511723032382131228894634160078174151915758186246746316504855676632271220045232721318561132067113541847197950188735686803230319347190365607190275898427760386

commandID 5 - Client63 consume 63,2,1230869

commandID 5 - Client68 consume 68,2,1267044

commandID 5 - Client71 consume 71,4,208

commandID 6 - Client8 consume 8,3,1669

commandID 5 - Client53 consume 53,2,1248028

commandID 5 - Client62 consume 62,5,231

localWorker reprocess - coomandId 5 70,5,-1

localWorker reprocess - coomandId 5 98,4,-1

commandID 5 - Client98 consume 98,4,220

commandID 6 - Client6 consume 6,3,1693

commandID 6 - Client5 consume 5,4,214

commandID 5 - Client73 consume 73,2,1271367

commandID 5 - Client77 consume 77,1,1643751544152900948604476410191572140483085851943050808908288684918842989603282054121249903908003054711321078627077569096646773189167688045116239097567537140965465707045041971640448

commandID 5 - Client75 consume 75,4,216

commandID 5 - Client83 consume 83,5,239

commandID 5 - Client82 consume 82,5,237

commandID 5 - Client70 consume 70,5,243

networkWorker reprocess - coomandId 6 9,2,-1

networkWorker reprocess - coomandId 6 42,3,-1

commandID 6 - Client20 consume 20,2,1296260

commandID 6 - Client15 consume 15,2,1280011

commandID 6 - Client22 consume 22,4,226

commandID 6 - Client17 consume 17,2,1280304

commandID 6 - Client21 consume 21,5,245

commandID 6 - Client23 consume 23,1,6963043279151935385260634152489320944063572302406363313807306655433558205159444721340676247903232264078005633069442343500128939954620940916151759620589495754227470018456066314322178

commandID 6 - Client56 consume 56,2,1286505

commandID 6 - Client18 consume 18,1,2242078311320349160804015718043036082054171733573840468952625315221391947825066947905636248691862060483006835747329499742497894355779460518674655899304658879188891886536343247826376546

commandID 6 - Client16 consume 16,3,1733

commandID 6 - Client9 consume 9,2,1284054

commandID 6 - Client42 consume 42,3,1723

commandID 5 - Client90 consume 90,4,222

commandID 6 - Client10 consume 10,4,224

commandID 5 - Client96 consume 96,1,529282892349538663865041757952487834360789665256290022345566986834836521594735814856589938215468774943708863917660167406652278820948558442729902757341091825700790758348602488149084432

commandID 5 - Client100 consume 100,1,9497596137630935307081104630124632162577476599551651898156068247720404312895003606479134932982917016875736206906978166376643856244066400517428526354559727342456358304493975479454590616

commandID 5 - Client93 consume 93,5,249

commandID 5 - Client88 consume 88,1,124946741922194505343848686233084744611013072548680379570357367882045861446123688479276495829986960708171380076688830115888779071985226747755044869940291576385728853141933295230038818

commandID 5 - Client99 consume 99,3,1741

commandID 6 - Client19 consume 19,1,29495924660760642489647013020148855916737375061568504064137515306653075810241060939483954895520932111023343610904846943097162533007651451709723277579925520157875345780869307228929160

commandID 6 - Client11 consume 11,3,1697

commandID 6 - Client26 consume 26,3,1699

commandID 6 - Client14 consume 14,5,253

commandID 5 - Client92 consume 92,5,247

commandID 5 - Client87 consume 87,3,1709

commandID 5 - Client89 consume 89,5,251

networkWorker reprocess - coomandId 6 54,1,-1

localWorker reprocess - coomandId 6 46,5,-1

commandID 6 - Client54 consume 54,1,721942253201873277843507800575705129100499234638474224639431544194632773641466397780893531402531680243264123105007029474740821853621031666072323047816479569603068959994684069733778925634

commandID 6 - Client29 consume 29,3,1777

commandID 6 - Client44 consume 44,4,234

commandID 6 - Client46 consume 46,5,261

commandID 6 - Client57 consume 57,1,12954728094771874910794011976124150759076622145360755595448190897197286916346990078682261389264946714250768264226751288380085720045846524926713426099379088684606226954799801010042375922402

commandID 6 - Client3 consume 3,3,1753

commandID 6 - Client39 consume 39,1,40232462861844090389128434238541564732364078131780448061576898306103009199405081373822175980623530127985951663375242165249073319332045062588388761317543568249014325104512245165644739010

commandID 6 - Client40 consume 40,5,257

commandID 6 - Client24 consume 24,2,1312298

commandID 6 - Client45 consume 45,3,1759

commandID 6 - Client34 consume 34,1,54877108839480000051413673948383714443800519309123592724494953427039811201064341234954387521525390615504949092187441218246679104731442473022013980160407007017175697317900483275246652938800

commandID 6 - Client13 consume 13,5,255

commandID 6 - Client48 consume 48,1,3058196460392500408237626043887111407494030727680570342702189838250663535676380920225341964465603758501876035280436064726336224548056373315160275762890652278750789498701279235077149249192

commandID 5 - Client94 consume 94,4,232

commandID 5 - Client97 consume 97,2,1302418

commandID 6 - Client50 consume 50,3,1747

commandID 6 - Client2 consume 2,4,230

commandID 6 - Client12 consume 12,2,1304786

commandID 6 - Client38 consume 38,4,228

commandID 6 - Client64 consume 64,5,265

commandID 6 - Client61 consume 61,5,263

commandID 6 - Client51 consume 51,3,1787

commandID 6 - Client36 consume 36,1,984729762650247500517208505027019748580915316836544098698206971848465938083481761308953633422991427320587207624093505863713887660617908141081091367124435474030411762223507419719362603649208

commandID 6 - Client27 consume 27,2,1330654

commandID 6 - Client35 consume 35,2,1316788

commandID 6 - Client25 consume 25,2,1318116

commandID 6 - Client32 consume 32,2,1322310

commandID 6 - Client58 consume 58,5,267

commandID 6 - Client31 consume 31,4,236

commandID 6 - Client28 consume 28,3,1783

commandID 7 - Client4 consume 4,3,1831

commandID 6 - Client41 consume 41,3,1823

commandID 6 - Client65 consume 65,3,1789

commandID 6 - Client76 consume 76,1,232463163452691875116448707769659008534278699381855126493428004605356531720604355018499811475366509176270564632976516161366802138971616417014769346741007116753309016226401734111028987677602

commandID 6 - Client33 consume 33,4,240

commandID 6 - Client37 consume 37,3,1801

commandID 6 - Client52 consume 52,2,1340226

commandID 6 - Client85 consume 85,3,1811

commandID 6 - Client30 consume 30,4,238

commandID 6 - Client43 consume 43,5,269

localWorker reprocess - coomandId 6 67,4,-1

localWorker reprocess - coomandId 6 59,4,-1

localWorker reprocess - coomandId 6 86,5,-1

commandID 6 - Client91 consume 91,3,1847

commandID 6 - Client69 consume 69,3,1861

commandID 6 - Client78 consume 78,3,1867

commandID 7 - Client1 consume 1,1,4171382214053681877185282727877738002857939966728031521286255891999220284054531400254314345167332218458619395129350539616222352781443248981339134815238749012874956065120431412988479402274434

commandID 6 - Client80 consume 80,1,17670258618864975009258339416537971760012675183748670183843230539845347074301607362326211014092320301155064788141495664328603298786390904066437630628079431525530236022705233071673280212746944

commandID 6 - Client84 consume 84,3,1871

commandID 7 - Client7 consume 7,3,1873

commandID 6 - Client79 consume 79,3,1877

commandID 6 - Client95 consume 95,1,317079925376919302666132900992656471931647237990639519210479942745367781399345450760562844620238773993470578978922828452051145490494418365054796259938305331985513836646470687870399681225795784

commandID 6 - Client72 consume 72,2,1349089

commandID 6 - Client74 consume 74,1,74852416689513581914218640394029625042908640701722712256659178051380608581260960849559158401536613423078878547695333196930635547927006865247089657327556475114995900155941363699681600253262210

commandID 6 - Client66 consume 66,1,1343172118197190792578750244364655512769497592664280789098578949032851734178642763891810536882491709396961194463386647005135217509904680325466274697080777803057051246741824115181280325156445346

commandID 6 - Client55 consume 55,4,244

commandID 6 - Client47 consume 47,2,1350479

commandID 6 - Client81 consume 81,1,5689768398165682472981133878451278523009637608647762675604795738876774718113916506327804992150205611581315356832469416472592015530113139666919895048261416544213718823613767148595520981851577168

commandID 6 - Client60 consume 60,2,1359893

commandID 6 - Client67 consume 67,4,248

commandID 6 - Client59 consume 59,4,252

commandID 6 - Client49 consume 49,4,242

localWorker reprocess - coomandId 6 86,5,-1

localWorker reprocess - coomandId 7 5,4,-1

localWorker reprocess - coomandId 6 73,5,-1

commandID 6 - Client98 consume 98,2,1369657

commandID 7 - Client5 consume 5,4,258

commandID 6 - Client73 consume 73,5,281

commandID 6 - Client71 consume 71,3,1889

commandID 6 - Client86 consume 86,5,275

commandID 7 - Client6 consume 6,3,1901

commandID 6 - Client68 consume 68,5,277

commandID 6 - Client63 consume 63,3,1879

commandID 6 - Client53 consume 53,1,24102245710859920684503285758169769604808048027255331491517761904539950606634308789203030505483314155722222621793264312895503279630357238993145854890126443979911926541196892709563364252562754018

commandID 7 - Client8 consume 8,2,1361808

commandID 6 - Client62 consume 62,4,254

localWorker reprocess - coomandId 6 82,4,-1

commandID 7 - Client23 consume 23,4,272

commandID 7 - Client22 consume 22,4,268

commandID 7 - Client21 consume 21,4,270

commandID 7 - Client15 consume 15,3,1931

commandID 7 - Client20 consume 20,3,1913

commandID 7 - Client17 consume 17,5,283

localWorker reprocess - coomandId 7 56,5,-1

commandID 6 - Client82 consume 82,4,266

commandID 6 - Client77 consume 77,4,260

commandID 6 - Client70 consume 70,2,1375483

commandID 6 - Client75 consume 75,3,1907

commandID 6 - Client83 consume 83,4,262

commandID 7 - Client56 consume 56,5,287

commandID 7 - Client42 consume 42,2,1384902

commandID 6 - Client88 consume 88,2,1398291

commandID 6 - Client93 consume 93,3,1949

commandID 6 - Client99 consume 99,4,280

commandID 7 - Client10 consume 10,1,102098751241605365210994276911130356942241829717669088641675843357036577144651151663139927014083462234470205844005526668054605134051542095639503314608767192463861424988401337986848977992102593240

commandID 6 - Client90 consume 90,4,276

commandID 6 - Client96 consume 96,2,1388405

commandID 7 - Client9 consume 9,3,1933

commandID 7 - Client16 consume 16,4,274

commandID 7 - Client19 consume 19,4,282

commandID 6 - Client100 consume 100,1,432497250677281381528480393402691197373775366897931686058221135332686259185238915441762738561817163093603045997815370985113923815836525621551159113325195213835357626494802244656959276220973126978

commandID 7 - Client11 consume 11,4,284

commandID 7 - Client26 consume 26,4,286

commandID 7 - Client18 consume 18,4,278

commandID 7 - Client14 consume 14,3,1951

networkWorker reprocess - coomandId 7 27,1,-1

commandID 7 - Client61 consume 61,1,3408595602048842192085573042563767667651357310208955412954617302492150110099095239235159792443674132892173477096414015495488103362893809061571240441775097232478525591507346647078766720398903771099444488

commandID 7 - Client46 consume 46,5,291

commandID 7 - Client40 consume 40,1,589926567003737181324149922734171785874301532096016810422932839073726802896447280008015135961163230698448548211599144946523844135946511442214146085371826209976759788052746908182780323165088570993776

commandID 7 - Client39 consume 39,1,139262771545966407661378107925422200898842898643541338629958106998175942605912752129483710006368244024430783850102086070559678333481851345459147768216015778077182098680079580955425705662828190441570

commandID 7 - Client29 consume 29,3,1979

commandID 7 - Client36 consume 36,5,295

commandID 7 - Client44 consume 44,2,1407658

commandID 7 - Client3 consume 3,1,32875480819871550678637491032482982278929937521851455903100411081023032472796271490080295935690254600725412811190800664285130802019106060377555012507763097668031393332428584361077500513775809227496

commandID 7 - Client57 consume 57,1,7760848266480204946828143795490271783123148556135515017556462674083812714727666169162526263607225621529132605338883413419155125405427103948927718184963387405056525350365243511115703607724953531586

commandID 7 - Client51 consume 51,4,292

commandID 7 - Client54 consume 54,2,1406305

commandID 7 - Client64 consume 64,4,290

networkWorker reprocess - coomandId 7 35,3,-1

commandID 7 - Client45 consume 45,1,10585802725247397713156061118182609163458497640206451131709690692246059419663254768654192151365247897971348454997593808373144063645018099899477074524185108681913644791617015762928968316457818468660472

commandID 6 - Client92 consume 92,1,1832087753950730891324915850521895146437343297309395832874560384687781613885606813430190881261352114608882389835267010608510300397397644581844139767909548047805291930967610316614686082875995101152

commandID 7 - Client34 consume 34,4,288

commandID 7 - Client24 consume 24,1,2498969039560915132957977798862109344396049027027608580321689463293083154191701872161544253851021166818224976696498665856655054877267897114315732109703320617984221250891067213686546998323182474416674

commandID 6 - Client87 consume 87,5,289

commandID 7 - Client38 consume 38,3,1987

commandID 6 - Client94 consume 94,1,804660269890348192607522023089803718623744663554333827348566450717698691837013275169848087213765208489846913363667231522429508515354857443705716811606284275603514186261573277563557017756457031808637442

commandID 7 - Client50 consume 50,5,293

commandID 7 - Client48 consume 48,1,189954522487449421655484950204552793156378655991620103560351499621355342751042138555767443588613298932785823641745089405770069301474379286748373195349960130064468846461053536824538649373075643864894720

commandID 7 - Client12 consume 12,2,1416544

commandID 7 - Client13 consume 13,1,44842179940550505985582222271592545998230039587853413107160452232277320832844720946778312859312012758703618796686873899349231309457340296712224030206443755345638800417359130265402420264154456349058562

commandID 6 - Client97 consume 97,2,1409648

commandID 7 - Client2 consume 2,2,1410290

commandID 6 - Client89 consume 89,3,1973

commandID 7 - Client58 consume 58,4,294

commandID 7 - Client27 consume 27,1,61164766314391710035884829815943265224568052927769577329622759945237346639032672167677108820397521093126336764093707189513015791230614183821533954756601790054548991800671186110593262317807192235925106064

commandID 7 - Client91 consume 91,2,1431728

commandID 7 - Client35 consume 35,3,1997

commandID 7 - Client25 consume 25,3,1999

commandID 7 - Client69 consume 69,1,1097557198057001938453841363644415006374573595389643436520255061711780089392489003778952798974711705543381888276590315395738796138788161499726039945177057123749403326820574003343599955000130556475552464664

commandID 7 - Client32 consume 32,3,2003

commandID 8 - Client4 consume 4,2,1422725

commandID 7 - Client76 consume 76,4,300

commandID 7 - Client33 consume 33,4,304

commandID 7 - Client31 consume 31,2,1420161

commandID 7 - Client37 consume 37,4,302

commandID 7 - Client43 consume 43,5,297

commandID 7 - Client41 consume 41,1,259098107935652557104489133457117935287501385615468464797658075441635685688364082902818922538578546112563887878124152051556445086889386828976126497605113833423713583754975704308251673170580841059906839650

commandID 7 - Client28 consume 28,4,296

commandID 7 - Client52 consume 52,3,2011

commandID 7 - Client85 consume 85,2,1426304

commandID 7 - Client65 consume 65,4,298

commandID 7 - Client30 consume 30,3,2017

localWorker reprocess - coomandId 7 81,5,-1

commandID 8 - Client5 consume 5,2,1447646

commandID 7 - Client98 consume 98,1,83428786095010233039452893451168885358856822423517291331018551725755973092961397681432523209335078083037082049842613293369652888469867204072869026512035048078160170454915915213979475203909274364258193729858

commandID 7 - Client84 consume 84,5,299

commandID 7 - Client79 consume 79,3,2029

commandID 8 - Client7 consume 7,2,1432783

commandID 7 - Client80 consume 80,2,1431995

commandID 8 - Client1 consume 1,4,306

commandID 7 - Client78 consume 78,3,2027

commandID 7 - Client95 consume 95,3,2039

commandID 7 - Client47 consume 47,5,301

commandID 7 - Client55 consume 55,1,4649326900163660310919854588034777960785795767174042210878678322288756043258320098018630118437425368286091440984485413634511629642042032827880286278313342328421326891037271717682651493171103066962116698306

commandID 7 - Client67 consume 67,4,310

commandID 7 - Client60 consume 60,3,2063

commandID 7 - Client49 consume 49,1,19694864798711643182133259715783526849517756664085812280034968350866804262425769395853473272724413178687747652214531969933785314706956292811247185058430426437434710890969660874074205927684542824324019257888

commandID 7 - Client74 consume 74,4,308

commandID 7 - Client66 consume 66,2,1438979

commandID 7 - Client72 consume 72,3,2053

commandID 7 - Client59 consume 59,5,307

commandID 7 - Client81 consume 81,5,305

networkWorker reprocess - coomandId 7 62,1,-1

networkWorker reprocess - coomandId 8 23,1,-1

commandID 8 - Client17 consume 17,5,315

commandID 8 - Client15 consume 15,2,1453231

commandID 8 - Client21 consume 21,1,482051511617926448416241857411039626258600330733909004920469712704382351844831823569922886993050824175326520025449797859766560885196970738202943545195859929088936259370887605815413541849563887924611727164704130

commandID 7 - Client73 consume 73,4,312

commandID 8 - Client22 consume 22,5,313

commandID 8 - Client20 consume 20,3,2081

commandID 8 - Client23 consume 23,1,113796925398360272257523782552224175572745930353730513145086634176691092536145985470146129334641866902783673042322088625863396052888690096969577173696370562180400527049497109023054114771394568040040412172632376

localWorker reprocess - coomandId 7 70,4,-1

commandID 7 - Client82 consume 82,3,2083

commandID 7 - Client62 consume 62,1,6341685300418834712936873743652479702279493077782703784593930186219165735154458712792650716708440646016361617676315200611489358319848695671037772054859840711063922357900430130265783715452104982240098275933872

commandID 7 - Client86 consume 86,4,314

commandID 7 - Client53 consume 53,2,1452764

commandID 8 - Client6 consume 6,5,311

commandID 7 - Client71 consume 71,5,309

commandID 7 - Client68 consume 68,3,2069

commandID 7 - Client63 consume 63,1,353410009178752575339944833520459068284945046358154977604109175253890696634271360121583566110064725510836075851584985143412396868586425109102723291106570618750075392710633321729992106743321640281356794177320

commandID 7 - Client77 consume 77,2,1462740

commandID 8 - Client8 consume 8,4,316

localWorker reprocess - coomandId 7 70,4,-1

localWorker reprocess - coomandId 7 70,4,-1

localWorker reprocess - coomandId 7 75,4,-1

commandID 8 - Client10 consume 10,2,1478129

commandID 7 - Client90 consume 90,3,2087

commandID 7 - Client93 consume 93,4,334

commandID 7 - Client88 consume 88,1,2042002971870066065922491212196382680607147253289366532826965484994220499915473279749837677306845163604089753144121280064929639593676573049781351354479810278536145564533047532284708282169650119738487320831448896

commandID 7 - Client99 consume 99,1,8650063399098190712106206706196570348687189343891375136228331652681264351506724942569273596220431478591685532601934918119485119259903262937328348963115101043233518517503077734954246670528164366878561010490499714

commandID 8 - Client56 consume 56,4,330

commandID 8 - Client42 consume 42,4,332

commandID 7 - Client83 consume 83,2,1469923

commandID 7 - Client75 consume 75,4,328

commandID 7 - Client70 consume 70,4,324

networkWorker reprocess - coomandId 8 14,2,-1

localWorker reprocess - coomandId 8 3,4,-1

commandID 8 - Client29 consume 29,5,323

commandID 7 - Client96 consume 96,4,336

commandID 8 - Client39 consume 39,5,321

commandID 8 - Client40 consume 40,3,2089

commandID 8 - Client16 consume 16,4,338

commandID 8 - Client46 consume 46,5,319

commandID 8 - Client44 consume 44,1,657518615256860854392329233453491570675799136066098240866498292237952781807047241620734939442087434239870884150789375865706732459805536673333924098370444049847927807857283404965545801074911886450810677209450610640

commandID 8 - Client9 consume 9,1,36642256568262828914347318036982664075355904628854867077740292095719277905942373050026932062188571077970831883551860952542870116633289624799094747206940214451470219634545358472101694964282307587252731362793447752

commandID 8 - Client36 consume 36,1,155219089672149506369495478854127226650110807859310843447189500035558375975276217142677001844974715790475013066809378728290965585793061762133707337790875958849114397055684511623361026527657394715889486461664290722

commandID 8 - Client61 consume 61,2,1515577

commandID 7 - Client100 consume 100,4,340

commandID 8 - Client18 consume 18,2,1503237

commandID 8 - Client19 consume 19,2,1487703

commandID 8 - Client26 consume 26,2,1495058

commandID 8 - Client14 consume 14,2,1514492

commandID 8 - Client11 consume 11,5,317

localWorker reprocess - coomandId 8 51,4,-1

commandID 8 - Client64 consume 64,3,2111

commandID 8 - Client57 consume 57,3,2099

commandID 8 - Client54 consume 54,5,325

commandID 8 - Client3 consume 3,4,344

commandID 8 - Client51 consume 51,4,348

commandID 8 - Client24 consume 24,4,350

commandID 7 - Client87 consume 87,3,2113

commandID 8 - Client34 consume 34,5,329

commandID 8 - Client45 consume 45,5,327

commandID 7 - Client92 consume 92,1,2785293550699592923938812412668093509353307352123703806913182668987369503203465183625616759613324452749958549669966882191117895425015208455469403731272652158240825628484818131485544230827304940519132195299466733282

commandID 8 - Client38 consume 38,2,1517421

commandID 8 - Client13 consume 13,5,331

commandID 7 - Client97 consume 97,2,1531954

commandID 8 - Client48 consume 48,3,2129

commandID 7 - Client94 consume 94,4,352

commandID 8 - Client50 consume 50,2,1526649

commandID 8 - Client12 consume 12,5,333

networkWorker reprocess - coomandId 7 89,2,-1

localWorker reprocess - coomandId 8 33,5,-1

commandID 8 - Client91 consume 91,3,2131

commandID 8 - Client58 consume 58,2,1549419

commandID 8 - Client35 consume 35,4,356

commandID 8 - Client27 consume 27,4,354

commandID 8 - Client32 consume 32,1,11798692818055232550147578884125865608089028544560913468519228968187430794620907976123201977895385245239705082830656904630178314159866370495211539023461052682811230321796555930907722724384131648527339458407317543768

commandID 8 - Client69 consume 69,3,2137

commandID 8 - Client25 consume 25,4,358

networkWorker reprocess - coomandId 8 65,3,-1

commandID 8 - Client33 consume 33,5,339

commandID 8 - Client31 consume 31,3,2141

commandID 8 - Client37 consume 37,3,2143

commandID 8 - Client76 consume 76,1,49980064822920523124529127949171555941709421530367357680990098541737092681687097088118424671194865433708778880992594500711831152064480690436315559825116862889485746915671041855116435128363831534628490028928736908354

commandID 8 - Client41 consume 41,1,896855873261869823317585490672419913441416280194488734450908591082280298767164282402506027321894253354008061308196734130621842841735637219398210673120830879852502618853593935260610288079721662682793688325417797617090

commandID 8 - Client85 consume 85,5,341

commandID 8 - Client43 consume 43,1,211718952109737325048264090680812089374926714666030344192479623135135801521369296328596900662674846980074820606801034907477502922417789132240473778323928504240754217984480723351373463237839457787041299574122265177184

commandID 8 - Client65 consume 65,3,2179

commandID 8 - Client30 consume 30,2,1558557

commandID 8 - Client52 consume 52,2,1556717

commandID 8 - Client28 consume 28,3,2153

commandID 9 - Client4 consume 4,2,1554645

commandID 7 - Client89 consume 89,2,1540538

commandID 8 - Client2 consume 2,5,335

commandID 9 - Client5 consume 5,3,2203

commandID 8 - Client79 consume 79,5,343

commandID 9 - Client7 consume 7,1,16093425653890736296592009704154386886003783621970429862435364540939308285127269986156990067122901694938436324666548619850481339999176989258731476556349838974455561392449019792835868750306626096755657899828591620199266

commandID 9 - Client1 consume 1,5,347

commandID 8 - Client78 consume 78,4,362

commandID 8 - Client98 consume 98,1,3799142445157216618318606053370491743140591835443985281996113987464256996590026425938621009950251860396107065839587971429964874289360338009833316470807252023650764693398856464393814615556726108518216052875793455645544

localWorker reprocess - coomandId 8 67,5,-1

commandID 8 - Client84 consume 84,4,360

commandID 8 - Client80 consume 80,5,345

commandID 8 - Client55 consume 55,1,68172845060720161804686644869988039287155726323325704731737572151221490137099106370566581278441858640149852364505782450831890234286068295044759222696206607921473010263194935635737289616783230495540847652190159936442608

commandID 8 - Client95 consume 95,4,364

commandID 8 - Client47 consume 47,5,349

localWorker reprocess - coomandId 8 74,4,-1

localWorker reprocess - coomandId 8 66,5,-1

commandID 8 - Client67 consume 67,5,353

commandID 8 - Client49 consume 49,5,357

commandID 8 - Client60 consume 60,5,355

commandID 8 - Client72 consume 72,4,370

commandID 8 - Client59 consume 59,5,363

commandID 8 - Client74 consume 74,4,368

commandID 8 - Client81 consume 81,3,2207

commandID 8 - Client66 consume 66,5,361

localWorker reprocess - coomandId 9 15,5,-1

networkWorker reprocess - coomandId 9 21,2,-1

localWorker reprocess - coomandId 9 6,5,-1

commandID 9 - Client22 consume 22,5,369

commandID 9 - Client23 consume 23,4,374

commandID 9 - Client17 consume 17,4,372

commandID 9 - Client21 consume 21,2,1571471

commandID 9 - Client15 consume 15,5,367

commandID 8 - Client73 consume 73,2,1574503

commandID 9 - Client20 consume 20,5,371

commandID 8 - Client62 consume 62,4,376

commandID 8 - Client86 consume 86,3,2221

commandID 8 - Client82 consume 82,3,2213

commandID 8 - Client53 consume 53,1,288784805896771383515338589184106544034626688915273248789385653145825268833523695468423315180890336255537845782689678423178042277143450169437768367341176270660347602445228762335785027217439548078919048508589231365969698

networkWorker reprocess - coomandId 8 68,3,-1

networkWorker reprocess - coomandId 8 88,3,-1

networkWorker reprocess - coomandId 8 88,3,-1

commandID 9 - Client10 consume 10,4,380

commandID 9 - Client56 consume 56,4,382

commandID 8 - Client90 consume 90,5,381

commandID 8 - Client88 consume 88,3,2267

commandID 8 - Client93 consume 93,2,1581277

commandID 8 - Client99 consume 99,5,383

commandID 9 - Client42 consume 42,1,1223312068647805695866041001606414215425662481984418699889280184734522565471193888244259842002003203662301235495264496143544059342859868972795832692060911690562863420044109984978877398486541422811217041686547085400321400

networkWorker reprocess - coomandId 8 70,3,-1

commandID 9 - Client6 consume 6,5,375

commandID 8 - Client75 consume 75,3,2269

commandID 8 - Client83 consume 83,2,1590879

commandID 8 - Client70 consume 70,3,2281

commandID 8 - Client71 consume 71,5,377

commandID 9 - Client8 consume 8,5,379

commandID 8 - Client77 consume 77,2,1576813

commandID 8 - Client63 consume 63,4,378

commandID 8 - Client68 consume 68,3,2239

networkWorker reprocess - coomandId 9 19,3,-1

networkWorker reprocess - coomandId 9 26,2,-1

networkWorker reprocess - coomandId 9 26,2,-1

networkWorker reprocess - coomandId 9 26,2,-1

networkWorker reprocess - coomandId 9 26,2,-1

commandID 9 - Client29 consume 29,4,384

commandID 8 - Client96 consume 96,4,386

commandID 9 - Client9 consume 9,4,390

commandID 9 - Client39 consume 39,2,1597664

commandID 9 - Client54 consume 54,3,2339

commandID 9 - Client16 consume 16,4,388

commandID 9 - Client19 consume 19,3,2309

commandID 9 - Client46 consume 46,5,387

commandID 9 - Client44 consume 44,3,2287

commandID 9 - Client40 consume 40,5,385

commandID 9 - Client61 consume 61,3,2293

commandID 9 - Client26 consume 26,2,1634966

commandID 9 - Client11 consume 11,2,1639855

commandID 9 - Client36 consume 36,2,1607475

commandID 9 - Client18 consume 18,2,1608988

commandID 8 - Client100 consume 100,5,389

commandID 9 - Client14 consume 14,3,2311

commandID 9 - Client64 consume 64,2,1643962

commandID 9 - Client91 consume 91,4,392

commandID 9 - Client57 consume 57,3,2333

commandID 9 - Client51 consume 51,5,393

commandID 9 - Client3 consume 3,5,391

commandID 9 - Client45 consume 45,2,1652618

commandID 9 - Client48 consume 48,1,21951444390599782363784051384045467838374768949396210893275305753070184688344390882026110574757615807281272386550255148132961177937191573215280229234400203822210068550530784793984055883140962380106365902705657377269342592

commandID 8 - Client92 consume 92,2,1655796

commandID 9 - Client34 consume 34,5,397

commandID 8 - Client87 consume 87,2,1644836

commandID 9 - Client24 consume 24,5,395

commandID 8 - Client97 consume 97,5,401

commandID 8 - Client94 consume 94,2,1658342

commandID 9 - Client35 consume 35,5,403

commandID 9 - Client13 consume 13,1,5182033080487994166979502595609763405737276616852948048346506392083915530718299248445462683188903150904742787763747662997354279648582926060621099135584823032911801282621668702251294621163605239323787215254777572967255298

commandID 9 - Client50 consume 50,3,2341

commandID 9 - Client12 consume 12,3,2347

commandID 9 - Client38 consume 38,5,399

networkWorker reprocess - coomandId 9 31,1,-1

localWorker reprocess - coomandId 8 89,5,-1

commandID 9 - Client58 consume 58,3,2351

networkWorker reprocess - coomandId 9 2,2,-1

commandID 9 - Client27 consume 27,4,394

commandID 9 - Client69 consume 69,5,405

commandID 9 - Client25 consume 25,2,1664772

commandID 9 - Client32 consume 32,4,396

commandID 8 - Client89 consume 89,5,411

commandID 9 - Client31 consume 31,1,393902686962148276852246883911212006875320178607147377379066223370528801824727841988225730503635081327400601722409328170249757143526588448902248293527142757109218370489510016306734128498050781419103369207015285705447845256

commandID 9 - Client2 consume 2,2,1684639

commandID 9 - Client37 consume 37,2,1672245

commandID 9 - Client33 consume 33,2,1669594

commandID 9 - Client76 consume 76,1,1668598558491480231031103243776639662260517066843027301137712622886479861583007230729452826996759691689632239223602080936528227565503703014530735190181756666758625557442784873105124032145930580436162727654138549903836006690

commandID 9 - Client28 consume 28,1,29941786242203757034937742679847722285930070850760053628857379482552272854210034290353600980959455084033350473690872688601978897187669305042631491407198434363333507958485382908014045060473022993091179846948416491187003494754

commandID 10 - Client4 consume 4,1,126835441889743097340727630578408659799637671849019471097359434645125539664996893926320440962328494184219331453380308406324278256156218620677551154683047906877477752434202181140783410498973865075528473667617235450068805851032

commandID 9 - Client41 consume 41,2,1670408

commandID 9 - Client65 consume 65,2,1671662

commandID 9 - Client85 consume 85,5,407

commandID 9 - Client30 consume 30,1,7068296920928069200976659859017770655917388445979256581929916714916448248156756764906037038490673848085929558616817651916362667405541400507025189054254169424143720600260649508727230257081773103163754279823569485320791872016

commandID 9 - Client52 consume 52,4,398

commandID 9 - Client43 consume 43,3,2357

commandID 10 - Client5 consume 5,4,400

commandID 9 - Client79 consume 79,2,1688836

commandID 10 - Client7 consume 7,2,1693992

commandID 9 - Client74 consume 74,4,404

commandID 9 - Client59 consume 59,4,402

commandID 9 - Client81 consume 81,4,406

commandID 9 - Client95 consume 95,2,1709741

commandID 9 - Client60 consume 60,3,2383

commandID 9 - Client72 consume 72,5,415

commandID 9 - Client80 consume 80,3,2381

commandID 9 - Client98 consume 98,2,1703411

commandID 10 - Client23 consume 23,5,417

commandID 9 - Client66 consume 66,4,408

commandID 10 - Client22 consume 22,1,537283553801176146397848264993482361484480758246837938018295118063054431514197609995635364830273431820910676287212106313899091921812543787752836110139390061873244517695294107471147687056368483295205074517417358291462226898882

commandID 9 - Client55 consume 55,2,1709512

commandID 9 - Client78 consume 78,2,1699826

commandID 9 - Client67 consume 67,2,1717507

commandID 10 - Client1 consume 1,3,2371

commandID 9 - Client49 consume 49,5,413

commandID 9 - Client84 consume 84,3,2377

commandID 9 - Client47 consume 47,2,1716113

networkWorker reprocess - coomandId 9 73,3,-1

networkWorker reprocess - coomandId 10 56,3,-1

networkWorker reprocess - coomandId 10 56,3,-1

networkWorker reprocess - coomandId 10 6,3,-1

commandID 10 - Client10 consume 10,3,2417

commandID 9 - Client90 consume 90,4,414

commandID 10 - Client15 consume 15,2,1718682

commandID 9 - Client62 consume 62,4,412

commandID 9 - Client83 consume 83,4,416

commandID 9 - Client70 consume 70,4,418

commandID 10 - Client56 consume 56,3,2441

commandID 9 - Client82 consume 82,3,2411

commandID 9 - Client88 consume 88,1,40840618385810315195437444799363677243476455015205662545972358889507053243327175116433193764139271492237297327386736897508247348725158869269722569559647898871790727065442612817315951446541086503538749417603542799636450036187048

commandID 9 - Client71 consume 71,5,421

commandID 9 - Client77 consume 77,3,2473

commandID 9 - Client86 consume 86,3,2399

commandID 10 - Client8 consume 8,3,2467

commandID 9 - Client99 consume 99,2,1728100

commandID 10 - Client6 consume 6,3,2459

commandID 9 - Client53 consume 53,1,9641162182178966878126331027202834784434723577592322830700454745652427494401346945631082965963962317692358822696127040961581675695438118874508418491101822679355067810556808551572644321954159676320600161466564032755133080685122

commandID 10 - Client42 consume 42,1,173003635725420227659876110224657543758340543638414973014589890303680640467710047411363858022521048286641548132243074630994571070596073595953398696729693418166517976072327259820836450108118505690475597831880735231300933225433314

commandID 9 - Client75 consume 75,2,1733862

commandID 9 - Client93 consume 93,2,1722678

commandID 10 - Client17 consume 17,5,419

commandID 10 - Client20 consume 20,4,410

commandID 10 - Client21 consume 21,1,2275969657094447682932120690552338105737560704836371223170539906897343265721787333908861900283422221467862036602228733661920645943406393771688895595240608154370455823215378611025374158724447798256348771737286668615917713446560

commandID 9 - Client73 consume 73,3,2393

networkWorker reprocess - coomandId 9 100,1,-1

commandID 10 - Client29 consume 29,2,1734552

commandID 10 - Client64 consume 64,2,1758097

commandID 9 - Client96 consume 96,2,1737306

commandID 10 - Client57 consume 57,3,2543

commandID 10 - Client91 consume 91,2,1758124

commandID 10 - Client54 consume 54,5,425

commandID 10 - Client14 consume 14,3,2539

commandID 10 - Client36 consume 36,3,2531

commandID 10 - Client26 consume 26,3,2503

commandID 10 - Client61 consume 61,5,431

commandID 9 - Client68 consume 68,1,732855161287491225834941885697993852276838629568865554604331920104229615114167364761888625854223464638803489856359035421486531631109453253083317356478421571537862631354751652100661751879015109265441140745126483724840182937920304

commandID 10 - Client40 consume 40,2,1753069

commandID 10 - Client44 consume 44,5,429

commandID 10 - Client46 consume 46,2,1744664

commandID 9 - Client100 consume 100,1,235977085964915080271168355074063468095036301160469872211371707733655038723496169665994228663159672191473255871711007176985001264571300541099056499890456505427037396840406816597802058730884140735673790971158990472729922988296891328

commandID 10 - Client19 consume 19,1,13150552284789031749833516497764525663739618877224374320332002202986626018811685390597562071611883092006225520087075900689249322011244999686229989847051940388809736637320087124994595582375730880274401783994673164247486842846378424

commandID 10 - Client11 consume 11,3,2521

commandID 10 - Client39 consume 39,3,2477

commandID 10 - Client51 consume 51,3,2549

commandID 9 - Client63 consume 63,5,423

commandID 10 - Client9 consume 9,1,3104424280875385130999643653016632952865695061913877191431917570720599100924379506458918361439414906841855507557679216316940697595033886608286668122643379704317968501491333868223483457624178942752240160812386670130661664977114530

commandID 10 - Client18 consume 18,5,433

commandID 10 - Client3 consume 3,5,435

commandID 10 - Client16 consume 16,5,427

localWorker reprocess - coomandId 10 48,5,-1

networkWorker reprocess - coomandId 10 13,2,-1

commandID 10 - Client34 consume 34,2,1761496

commandID 9 - Client87 consume 87,4,422

commandID 10 - Client24 consume 24,1,4234436995083682413131196874835377900046913802011233325484358737002804071004119368597298553865262216354512380170711053285040773440272164740096787008181165157297863406490002611635442461573538802361853835696867155344891126946497665480

commandID 10 - Client38 consume 38,1,321872918259779894910101296197132795139173273123424544111284023938595776499489243134463539260407815390217807650561946032482172719446324534132709019249279403095897175808290970166661828945376076081964741360258694885538846256970185204706

commandID 9 - Client92 consume 92,1,999614977279691833215007129940328607987969375212690863318246757317287258070155799732826081300525636040759781074750011527013943043925216049749432627072677162967706502412398948759410100710663665406545011181427041218040300989550193538

commandID 10 - Client50 consume 50,1,17937362957614421485739794629281840208175624583257624165255681705328503542086633274122020296761574501458809301757594224667177036805013875010136580659797337792159160128372409395301179947004818874853960353968895662597604808775540855458

commandID 10 - Client35 consume 35,2,1763728

commandID 10 - Client12 consume 12,1,75983888825541368356090375391962738732749412135041729986507085558316818239350652465085379740911560222189749587201087951953748920660327664780643109647370516325934503919979640192840162249592814301777695251572449805735310362048661087312

commandID 10 - Client13 consume 13,2,1772063

commandID 9 - Client94 consume 94,5,441

commandID 9 - Client97 consume 97,4,424

commandID 10 - Client45 consume 45,4,420

commandID 10 - Client48 consume 48,5,439

localWorker reprocess - coomandId 10 31,4,-1

commandID 10 - Client58 consume 58,4,426

commandID 10 - Client25 consume 25,3,2551

commandID 10 - Client32 consume 32,3,2557

commandID 10 - Client37 consume 37,1,24466576224738355695580829707856927808477215671182276585783070178070281818032186597587826282344859231872907893822878609521930167451360936758825982249953415800445483224836603735277934442310155321031682197215357678456297206656680573223136

commandID 10 - Client65 consume 65,5,449

commandID 10 - Client41 consume 41,1,103642080064671846469219402368346819706205805976367490512970137461470522745577466133497526815770016030014093303699871872447732601718672574774682554765640895119715922903767279994716349510310385834727240172992118326102290454443410085721794

commandID 10 - Client28 consume 28,3,2579

commandID 9 - Client89 consume 89,4,428

commandID 10 - Client27 consume 27,5,443

commandID 10 - Client69 consume 69,1,1363475561864660947996495560180493919289442504628739906431643181312699924237307625002939536782542821783060980189448872081882439798445625801311479186644488128709523207153143520859487478031097118629636660692607229347890695389929401906136

commandID 10 - Client76 consume 76,4,434

commandID 10 - Client33 consume 33,5,445

commandID 10 - Client31 consume 31,4,432

commandID 10 - Client2 consume 2,1,5775775165718423686896083536919108472296943291638384169837856749189395473448719743146221686390579102522461728408357434360011931913228827739378625765827231917933990004420865053604611741069764550600511384130687612277101627816687792829250

commandID 11 - Client4 consume 4,5,447

commandID 10 - Client85 consume 85,4,436

commandID 10 - Client52 consume 52,2,1781362

commandID 10 - Client30 consume 30,5,451

commandID 10 - Client43 consume 43,3,2591

localWorker reprocess - coomandId 11 10,4,-1

commandID 11 - Client5 consume 5,2,1785167

commandID 10 - Client79 consume 79,2,1792865

commandID 10 - Client59 consume 59,3,2593

commandID 10 - Client81 consume 81,5,453

commandID 10 - Client95 consume 95,2,1796277

commandID 10 - Client72 consume 72,2,1806061

commandID 10 - Client98 consume 98,1,7878161560476924992608671075554538791590930696708558018892162090253072428588124733770814977535303761102854152061379711178109560170417561308677185641375352517227119663893466423119302050261620420557899889808093600013121965233089095916762480

commandID 10 - Client80 consume 80,3,2609

commandID 11 - Client7 consume 7,4,438

commandID 10 - Client78 consume 78,2,1806561

commandID 10 - Client74 consume 74,1,439034896483425741572458439181244206633300439576652238637663620023952372800342051131577933545424923351929281108622366099312860574326051235857556201312516996279309174839905723714143332483551698659940642889183830982865459024430320916110312

commandID 10 - Client67 consume 67,4,444

commandID 11 - Client22 consume 22,4,440

commandID 10 - Client55 consume 55,4,442

commandID 11 - Client1 consume 1,3,2621

commandID 10 - Client49 consume 49,3,2617

commandID 10 - Client66 consume 66,1,141367873192101224125383620920800454042003452101177392101421254004531351341785903156743091662090042776499445455996212435106659222493190052320331785343443828313808844775242489892433293572225615871382257373656500969253329915171173405585614328

commandID 10 - Client60 consume 60,1,1859781665998374812759053159093323646239407564282976445063624617557280013946945670659809260997469709437731217738189336269699174899022877518204907360015708880236952622263390174851289679444517180474489811729727442257564126552164693750163042

commandID 11 - Client23 consume 23,1,33372427907906074783193737461311478812603130351117208520632272978569569728299444605743069171138684753849147825983708180982137415580693122752913649925517118949145431277837255867328497880490998862706089370962101842310051987484521077417212962

networkWorker reprocess - coomandId 10 62,3,-1

commandID 10 - Client47 consume 47,1,598843920676310971284728221144513294980616938755826776926317288996694975095443057232715435819498855859846929649968557921408774305553453332034240791299292432204380810378807215437061672169393462348235118865588105719323371648169214699759670274

commandID 10 - Client84 consume 84,3,2633

networkWorker reprocess - coomandId 10 93,1,-1

commandID 11 - Client64 consume 64,4,454

commandID 11 - Client29 consume 29,2,1852621

commandID 11 - Client10 consume 10,4,448

commandID 10 - Client62 consume 62,3,2657

commandID 10 - Client90 consume 90,2,1808437

commandID 10 - Client88 consume 88,1,2536743555897345109264296505498853633964471207124484499806690409991311251723558132087604834940085466215887164055870444120741756444707003380457294950540613557131332086290471351640679982249799465264322732836008923846546816507848032204624295424

commandID 10 - Client83 consume 83,2,1813715

commandID 10 - Client77 consume 77,1,10745818144265691408341914243139927830838501767253764776153078928961939981989675585583134775579840720723395585873450334404375800084381466853863420593461746660729709155540692621999781601168591323405526050209623801105510637679561343518256851970

commandID 10 - Client82 consume 82,3,2659

commandID 10 - Client86 consume 86,3,2663

commandID 10 - Client53 consume 53,5,457

commandID 10 - Client70 consume 70,5,455

commandID 11 - Client42 consume 42,3,2677

commandID 10 - Client75 consume 75,1,45520016132960110742631953478058564957318478276139543604419006125839071179682260474420143937259448349109469507549671781738244956782232870795910977324387600200050168708453241839639806386924164758886426933674504128268589367226093406277651703304

commandID 11 - Client56 consume 56,2,1818958

commandID 11 - Client6 consume 6,2,1839881

commandID 10 - Client99 consume 99,2,1832482

commandID 11 - Client15 consume 15,4,450

commandID 11 - Client8 consume 8,3,2671

commandID 10 - Client71 consume 71,2,1826004

localWorker reprocess - coomandId 11 14,4,-1

commandID 11 - Client20 consume 20,5,459

commandID 11 - Client17 consume 17,2,1843875

commandID 10 - Client73 consume 73,4,452

commandID 10 - Client93 consume 93,1,816823546837384648258110866099555315597768137763387300379735419855111969982557130407474986035729984817754563971838221627167667465635484670945940296888436190043771704665867881761875834982385166194691362073305065384988061793561833280793106364048

commandID 11 - Client21 consume 21,1,3460120070025644727411313192553595450051184965925361140712770782852766104630947239113163654667537573388179529503425023970028025489755251633821268517444756907636017202652825187028062347078405915137716682078127901854132115280831268091801289121378

localWorker reprocess - coomandId 11 40,5,-1

localWorker reprocess - coomandId 11 19,4,-1

commandID 10 - Client96 consume 96,2,1862270

commandID 11 - Client57 consume 57,3,2683

commandID 11 - Client91 consume 91,2,1865624

commandID 11 - Client19 consume 19,4,466

commandID 11 - Client39 consume 39,4,468

commandID 11 - Client54 consume 54,4,456

commandID 11 - Client36 consume 36,3,2687

commandID 11 - Client26 consume 26,3,2689

commandID 11 - Client40 consume 40,5,463

commandID 11 - Client61 consume 61,4,462

commandID 10 - Client68 consume 68,3,2693

commandID 11 - Client14 consume 14,4,460

commandID 11 - Client44 consume 44,2,1875199

commandID 11 - Client11 consume 11,3,2711

commandID 11 - Client46 consume 46,3,2699

commandID 10 - Client100 consume 100,3,2707

networkWorker reprocess - coomandId 11 3,1,-1

localWorker reprocess - coomandId 11 50,5,-1

commandID 11 - Client38 consume 38,5,467

commandID 10 - Client63 consume 63,5,465

commandID 10 - Client92 consume 92,1,4719606312258535305534140458939609692723450257993399720416719154501582958027863757708487312571356190186943094129835788565562347509782607935535590715089598962629084595750539845577628682094927918047310818677299294137960051789870357525249162985855016

commandID 11 - Client50 consume 50,5,471

commandID 10 - Client94 consume 94,5,475

networkWorker reprocess - coomandId 10 97,3,-1

commandID 11 - Client34 consume 34,3,2713

commandID 10 - Client87 consume 87,2,1877932

commandID 11 - Client24 consume 24,4,472

commandID 11 - Client13 consume 13,5,473

commandID 11 - Client51 consume 51,4,470

commandID 11 - Client35 consume 35,4,474

commandID 11 - Client18 consume 18,1,62089335377785498959024767737809343913261216971784688593636044987917471658656331586553682073491058686870070257445578293999147103188381216458745325984114612189987379263761499706524563240262441222119949043621394593060198206948378890683794340519618

commandID 11 - Client9 consume 9,1,14657303826939963557903363636313937115802508001464831863230818551266176388506346086860129604705880278370472681985538317507279769424656491206231014366667463820587840515277168629874125223296008826745558090385816672801516522916886905647998262849560

commandID 11 - Client12 consume 12,2,1884537

commandID 11 - Client16 consume 16,2,1876967

commandID 11 - Client3 consume 3,1,1114147916730113336535034506088014594988650720526199033544736038999661723751183021318853113668171518790273085104516984268014619831901106644623594599196618262512136809545054169530414075977645536083021366103106974773229435609789988764216496840231746

networkWorker reprocess - coomandId 11 69,1,-1

localWorker reprocess - coomandId 11 76,4,-1

commandID 11 - Client45 consume 45,3,2731

commandID 11 - Client76 consume 76,4,486

commandID 10 - Client97 consume 97,3,2729

commandID 11 - Client58 consume 58,2,1893227

commandID 11 - Client32 consume 32,4,478

commandID 11 - Client31 consume 31,2,1905797

networkWorker reprocess - coomandId 11 52,1,-1

commandID 11 - Client48 consume 48,5,477

commandID 11 - Client2 consume 2,4,488

commandID 11 - Client27 consume 27,4,482

commandID 11 - Client65 consume 65,2,1897446

commandID 11 - Client25 consume 25,3,2741

commandID 11 - Client69 consume 69,1,358752169067026468719553699647148145990895480824470163440264291787108222281776301917431589437496561512894545224124965509276737557846666584317163639672793635772000416656304789763606304402454784212817742168518367749078024134237095550809620181265500834

commandID 11 - Client41 consume 41,4,480

commandID 11 - Client37 consume 37,4,476

commandID 11 - Client28 consume 28,1,19992573165764254558671596341846453365882451752499797915211612657005993555862638052152802363953596279538045461623860138530264009871031538386765957459555014113028475192547213551840928804357357208272264640812304151325069642769271418865213148783651810

commandID 10 - Client89 consume 89,3,2749

commandID 11 - Client33 consume 33,3,2753

commandID 11 - Client52 consume 52,1,27269884455406270157991615313642198705000779992917725821180502894974726476373026809482509284562310031170172380127627214493597616743856443016039972205847405917634660750474914561879656763268658528092195715626073248224067794253809132219056382939163918400

localWorker reprocess - coomandId 11 81,5,-1

networkWorker reprocess - coomandId 11 72,2,-1

networkWorker reprocess - coomandId 11 78,3,-1

commandID 12 - Client5 consume 5,2,1916942

commandID 11 - Client55 consume 55,2,1948007

commandID 11 - Client95 consume 95,5,485

commandID 12 - Client22 consume 22,2,1944386

commandID 11 - Client78 consume 78,3,2789

commandID 12 - Client7 consume 7,2,1940694

commandID 11 - Client67 consume 67,3,2791

commandID 11 - Client72 consume 72,2,1936520

commandID 12 - Client4 consume 4,1,1519698575243421428418435324414918007119835180565873245142320336930958446308583623636046054518371987359917305837125138379793568618380575098751253979244484198502744651991158553107366561509343493602407338055999986895750335159815338236224582483182465592

commandID 11 - Client74 consume 74,2,1942638

networkWorker reprocess - coomandId 12 23,3,-1

commandID 11 - Client79 consume 79,5,479

commandID 11 - Client98 consume 98,1,115517084291665792814359756251875614994473356174758866428731557219409847913008218034391652945760224635633253289083134377002841479006794739043482068380040354100321622026520597249711699603514462870991229956896811308228350541788734977371933481870651036802

commandID 11 - Client59 consume 59,4,492

commandID 11 - Client80 consume 80,4,494

commandID 11 - Client66 consume 66,5,487

commandID 11 - Client49 consume 49,2,1948700

commandID 11 - Client81 consume 81,5,483

networkWorker reprocess - coomandId 12 23,3,-1

networkWorker reprocess - coomandId 12 23,3,-1

networkWorker reprocess - coomandId 12 23,3,-1

commandID 11 - Client30 consume 30,2,1913161

commandID 11 - Client85 consume 85,4,490

commandID 11 - Client43 consume 43,3,2767

networkWorker reprocess - coomandId 11 88,3,-1

networkWorker reprocess - coomandId 11 86,1,-1

commandID 12 - Client10 consume 10,4,498

commandID 12 - Client29 consume 29,4,496

commandID 11 - Client62 consume 62,1,8780818104741843675319759910466961657587094904462239721828740669012079399834933154237401669932295444295487167276155337790595745973134780742403388450862311395822946018667556549531196536428608521688935884062213659412250391511103673618503169204652661262544

commandID 11 - Client60 consume 60,2,1950740

commandID 12 - Client23 consume 23,3,2833

commandID 11 - Client88 consume 88,3,2851

commandID 12 - Client1 consume 1,1,489338221622069441415430640321144658682894204691953191536106731772614118128405898947049121067603208573703185536460164722504963532771035399189968245726008822318921148856557303560726455177326510012057115543213318481137469961408749041706790310421768065608

commandID 11 - Client83 consume 83,2,1956909

commandID 12 - Client64 consume 64,3,2837

commandID 11 - Client86 consume 86,1,157565387663731116714340247748084165177884814075628361801381225310444815078900390877326180937713714788745065825434335915508218463983655017964071023869795596302494107187159460588000811200537626880388788797576632550939369577238457376091350255373326134660184

commandID 11 - Client77 consume 77,2,1965831

commandID 11 - Client90 consume 90,4,500

commandID 12 - Client42 consume 42,4,506

commandID 11 - Client53 consume 53,4,502

commandID 11 - Client82 consume 82,2,1971078

commandID 11 - Client70 consume 70,4,504

commandID 11 - Client84 consume 84,1,2072869970779943558476082317536454249726050174942571632573158484309866320426631813822588137216173058930445995434923793267022695610090936335803355051284075643376006217452749811492617520312820502919219692129750085232778230387423731144199094723557723299234

commandID 11 - Client47 consume 47,5,489

networkWorker reprocess - coomandId 12 21,1,-1

localWorker reprocess - coomandId 12 14,4,-1

commandID 12 - Client57 consume 57,3,2897

commandID 11 - Client96 consume 96,3,2887

commandID 12 - Client91 consume 91,1,910412782651153936969187793496814992755619750728200681570654898663247246551159982116163863975600559487058959169187212792179271791299941949939959359879706749588405033692746612802554125237049644846227893580202067253254429193216012465246689556490695466902624752

commandID 11 - Client75 consume 75,1,667457693044671785117116112951740961591613686095304977725413022402137444235367927940076918567800213991092657966276888806462279535437250131161701004333915706436644219040760818361620648468177762111230118418685134926639258105385667929983612793035472906990146

commandID 12 - Client6 consume 6,3,2857

commandID 12 - Client56 consume 56,5,491

commandID 12 - Client36 consume 36,1,69203348523820113554472120640669111382434870027263174497438309844688868853700475497166944274485045979513484452306956616658996547764753956067738516552026868718113415488801184144748613072284534180776732379263665065071293243550517457543062493111795951828556954370

commandID 12 - Client19 consume 19,1,3856570434895029282740904086275403024200728883785315986538570014707992933382107974629546401093096690680308742095894574440604674152914952251057569359559296571750576197295893981972846857137431505964224753231932752709310325104567847510710337346225480585938026786

commandID 12 - Client15 consume 15,2,1971617

commandID 11 - Client71 consume 71,1,11977042332414344813848334911171933007768971919922698068537546282078115812316856338490612339403458497003555448728444453371891625958367872301605201169155749394632928152441571754499554268761172463412467168307953823956624866100510184314086818503096343957473218

commandID 11 - Client99 consume 99,1,2827396159842418257182804699555048011544339558456848272703033314918994592020372102637633855208914570753115697690541891141357336605732655542610875041205458422049070983350202734034483405073248675325309262472317172257496401998781129096025801427515217762620768

commandID 12 - Client54 consume 54,1,16336694522231271067932804138598427089558535285869464627724934957495218980079591880634349468347987322208293927552765510554597968402959750954170236798116893036590709822876322540693941553786775668703126906507933078090495729611487402508088038941392617810654731896

commandID 12 - Client26 consume 26,5,497

commandID 12 - Client39 consume 39,5,495

commandID 12 - Client44 consume 44,1,1241803702993867014697757267445768601859626931606951824967351007189691646433226450974375450539637730940562411399429324525421333185612656256968235728576924340354290902601125420623502188443984183748016958073514038418573968058804746388264414538666101652328087151874

commandID 11 - Client68 consume 68,4,508

commandID 12 - Client61 consume 61,1,293150088617511725285821286701274872619298015394922162617478174336250694394881493869302126566288171240262231736780591977190584159461975575225124303006224367909044371778081059119688393842924912391810056423562593338375668703813557232680338011388576425124882549376

commandID 12 - Client8 consume 8,2,1971521

commandID 12 - Client14 consume 14,4,512

localWorker reprocess - coomandId 12 40,5,-1

commandID 12 - Client21 consume 21,1,214919304290413534864152912288143053178249880872513260255950420055003947177468046164890945190694452732072905419145723271887586987715184451297731920040469573396956062524907530762630356189232926579313178911124483696292608331703797649723579120262698718327527778

commandID 11 - Client93 consume 93,3,2879

commandID 11 - Client73 consume 73,3,2861

commandID 12 - Client17 consume 17,2,1972890

commandID 12 - Client20 consume 20,5,493

localWorker reprocess - coomandId 12 18,5,-1

commandID 11 - Client63 consume 63,3,2909

commandID 11 - Client92 consume 92,2,1974472

commandID 11 - Client94 consume 94,4,514

commandID 12 - Client50 consume 50,2,1984086

commandID 12 - Client51 consume 51,1,94393418122056124388097485130017012168421205337414208162146401481374060347905289865933168590480815538804951560284181429442575920074964835280540085608644366759962699307508408289926860263296584740517991940493574852889712068198772212910603592977565118194745278274320

commandID 12 - Client34 consume 34,3,2917

commandID 12 - Client35 consume 35,3,2939

commandID 12 - Client11 consume 11,3,2903

commandID 12 - Client13 consume 13,5,507

commandID 11 - Client100 consume 100,1,22283263305365786151005158693383165722090849898897869674914879819569760766944375642041591165438994110950609920737420884840925000793263058669360504597832611257659122831331456387078290778919430773283528512943989026469260131814934917531216399202878033790077011779362

commandID 12 - Client38 consume 38,5,503

commandID 12 - Client46 consume 46,1,5260364900592979784076850356484349280057805741822729462486882203095017280127787297766803928724839095002511877334497890078875916901912600603098067217313921729326207982182582741613697147618861647383877888717618747012671540939032542785737996166052983034437231156872

commandID 12 - Client40 consume 40,5,501

commandID 12 - Client18 consume 18,5,511

commandID 11 - Client87 consume 87,3,2927

commandID 12 - Client24 consume 24,5,505

commandID 12 - Client9 consume 9,2,1993176

commandID 12 - Client3 consume 3,2,1999667

localWorker reprocess - coomandId 11 97,4,-1

commandID 12 - Client76 consume 76,1,399856935793590283703395099213451214395775671248554702323500485745066002158565535105774265527362256266170416161874146602611228681093122399791520847032410078297509920061365089546785731832105769735355496274918288438028108404610023769173630771113138506569058124876642

commandID 12 - Client45 consume 45,3,2953

commandID 12 - Client58 consume 58,1,1693821161296417259201677881983821869751523890331633017456148344461638068982167430289030230699929840603486616207780767839887490644447454434446623473738284679950002379552968766477069787591719663681939977040166728605002145686638867289605126677430119144470977777780888

commandID 12 - Client2 consume 2,1,30394387485213454541242104390578776643359008820631980106048523798828111181331108455336610984008256315323954140179769639688532255679979214984758682441680479872340080132645929388297329316387657361534401594782507540037148910291300838999981676600764579482282854721781664

commandID 11 - Client97 consume 97,4,518

commandID 12 - Client65 consume 65,1,545405153572545764483156201148434157710710634881044008891417280034444363194977784765769967481448683835227687907028072746553693111595178415291209660476510353022171440008073760222874857907386112843937288729044968992063678239556776234710065052136332311536620407214289064

commandID 12 - Client48 consume 48,4,522

commandID 12 - Client27 consume 27,1,128752691521833077485478524189463845266837906515103007196342189058904063003411669077608339124360106879975933441712075776716290213978799800076612744508707468287457839968856957708644382147749613870600721783565615363006632332316368848927520843883891933013584388123126850

commandID 12 - Client12 consume 12,5,513

commandID 12 - Client25 consume 25,5,517

commandID 12 - Client32 consume 32,4,520

commandID 12 - Client69 consume 69,2,2006666

commandID 12 - Client31 consume 31,1,7175141580979259320510106627148738693401871232575086772148093863591618278087235256261895188327081618680116880992997217962161191258882940137578014741985548798097519438273240155455064882198984424463115404435585202858036691151165492927594137480833615084452969236000194

commandID 12 - Client41 consume 41,5,519

commandID 12 - Client37 consume 37,3,2957

localWorker reprocess - coomandId 12 28,4,-1

commandID 12 - Client16 consume 16,5,515

commandID 13 - Client5 consume 5,2,2009024

commandID 12 - Client95 consume 95,5,523

commandID 12 - Client72 consume 72,4,530

commandID 13 - Client22 consume 22,1,2310373305812016135418103328783200476109680446039279042762011309196681515783322808140688209050154842220886685069824366762931062660359513461241451386414748880376143600001151998600143813777294065246349876699745491331261345290543473787767781052429221179160066016980283106

commandID 12 - Client78 consume 78,2,2016248

commandID 13 - Client4 consume 4,5,527

commandID 12 - Client74 consume 74,2,2032726

commandID 12 - Client67 consume 67,5,525

commandID 12 - Client55 consume 55,2,2015153

commandID 13 - Client7 consume 7,2,2024805

commandID 12 - Client33 consume 33,5,521

commandID 11 - Client89 consume 89,3,2963

commandID 12 - Client28 consume 28,4,526

commandID 12 - Client52 consume 52,4,528

networkWorker reprocess - coomandId 12 59,1,-1

networkWorker reprocess - coomandId 12 80,3,-1

networkWorker reprocess - coomandId 12 66,3,-1

localWorker reprocess - coomandId 12 81,5,-1

networkWorker reprocess - coomandId 12 43,1,-1

commandID 13 - Client10 consume 10,2,2048447

commandID 13 - Client23 consume 23,1,3151350882948751305127552142138167433235473879921466945960400881892618049166521292471329006174641904719130041921856644045405809356221020808587774137693191211117739820403950879059362639062016696715684913758429890342569079978446984885382542960640135118493474518138883937472

commandID 13 - Client29 consume 29,4,538

commandID 12 - Client60 consume 60,2,2056505

commandID 12 - Client59 consume 59,1,41457966813094457360040381393908144724707410122191919762519861376481363221096398877454779423778427053095984397815126525956042837672492442502269512210956772378483126960051879017093944265843543560563697058811853228599697582897466159330892538099842089291867603917521969058

commandID 12 - Client79 consume 79,5,529

commandID 12 - Client98 consume 98,4,532

commandID 12 - Client62 consume 62,3,3011

commandID 12 - Client49 consume 49,2,2038575

commandID 12 - Client80 consume 80,3,2971

commandID 12 - Client66 consume 66,3,3001

commandID 12 - Client81 consume 81,5,533

networkWorker reprocess - coomandId 12 83,1,-1

commandID 12 - Client30 consume 30,4,534

commandID 12 - Client43 consume 43,1,743933029329888216345308761761563404568623701753415276682595493467467856463951856986045341418961532113506832475602453100445840015444504451579609768410807153932320141680932670309090852971406490024900197181913612623463295146863847394168297904744728386074456804498415159938

commandID 12 - Client85 consume 85,4,536

networkWorker reprocess - coomandId 12 82,3,-1

networkWorker reprocess - coomandId 12 84,2,-1

commandID 13 - Client64 consume 64,5,537

commandID 13 - Client57 consume 57,5,539

commandID 12 - Client83 consume 83,1,239544125070918193647054003183894633070620722284153679812752986885215453099876714626698459248696563185706979170458920073976797553910470073895173103976893488817326709477660318687528654512979112493952617142699483519263849775944868317448404157546750111094795930982472701216930

commandID 12 - Client77 consume 77,4,540

commandID 13 - Client42 consume 42,2,2065326

commandID 12 - Client53 consume 53,1,4298444914715402592210116539979789162133662481893326953569029564912840215744650826253700905110420608191735598068097532302300286892948132742427185165264899226713477491174589000188969239824404551614259468716375070172755556351946977927135576366094196730846278402807454670994914

commandID 12 - Client88 consume 88,5,535

commandID 12 - Client86 consume 86,2,2064552

commandID 12 - Client90 consume 90,1,1014725197411121099640765634198973632265760439902293318439069144506906190661193527906750611465431011251507154724409653057080872334759415667133003015322001434474037695424232170375360146327856359780076712893418896663372926644000527402421793052136861654937870617956245492444496

commandID 13 - Client91 consume 91,4,544

commandID 13 - Client1 consume 1,1,13349336561124893436855517330314233137510519221439283060524199021037940053130037026871361366117529150990027000163029029282069077440328587685930706319183571998403279423296736186546541409219473276887639852215633173993739615060651786935698469747305268860048354877053950909826

commandID 12 - Client96 consume 96,5,541

commandID 12 - Client70 consume 70,4,542

commandID 12 - Client82 consume 82,3,3023

commandID 12 - Client84 consume 84,2,2075549

commandID 12 - Client47 consume 47,1,18208504856272731468481231794118130280800410367475601132715187404158267053639796832921554231907113444018449546996799782266282019906551946636841743676381598341327947660122588171131237105625474566237114587758919177354395152051788439110964098516513648578322984229186064176424152

localWorker reprocess - coomandId 13 21,5,-1

commandID 12 - Client75 consume 75,5,543

commandID 12 - Client99 consume 99,1,77132464339806328466135043716452310285335303951795731484429779181545908430303838157939917832738874384265533786055296661367428366519155919289794159870791292592025268131664941684713917662326302816562717819752051779590336164559100734370991970432148791044138215319551711376691522

commandID 13 - Client36 consume 36,5,545

commandID 13 - Client54 consume 54,3,3041

commandID 13 - Client26 consume 26,3,3049

commandID 13 - Client44 consume 44,1,326738362215498045333021406659927371422141626174658527070434304130341900774855149464681225562862610981080584691217986427735995485983175623796018383159546768709429020186782354909986907754930685832487985866767126295715739810288191376594931980245108812754875845507392909683190240

commandID 12 - Client68 consume 68,2,2092946

commandID 13 - Client15 consume 15,3,3037

commandID 13 - Client6 consume 6,4,546

commandID 13 - Client39 consume 39,2,2087164

commandID 13 - Client19 consume 19,5,547

commandID 12 - Client71 consume 71,4,548

commandID 13 - Client56 consume 56,2,2082906

commandID 13 - Client8 consume 8,2,2097442

commandID 13 - Client14 consume 14,1,1384085913201798509798220670356161795973901808650429839766166995702913511529724436016664820084189318308587872550927242372311410310451858414473867692508978367429741348878794361324661548682049046146514661286820556962453295405711866240750719891412584042063641597349123350109452482

commandID 13 - Client61 consume 61,4,550

commandID 13 - Client21 consume 21,5,551

commandID 12 - Client93 consume 93,2,2102171

commandID 13 - Client17 consume 17,5,553

commandID 13 - Client20 consume 20,4,552

commandID 12 - Client73 consume 73,2,2108961

localWorker reprocess - coomandId 12 63,5,-1

networkWorker reprocess - coomandId 13 50,1,-1

commandID 12 - Client63 consume 63,5,557

commandID 12 - Client94 consume 94,4,554

commandID 13 - Client50 consume 50,1,24836413973292566847901837022694460017244897251755941384306576143470897299104736010142026843682668855170316172130635066040237957221614295541239824305290819321143319011686633562159193958614556527820701185343017973544568981138254491599141966074994363966101410536964668590593453154

commandID 13 - Client34 consume 34,3,3067

commandID 13 - Client11 consume 11,3,3079

commandID 13 - Client13 consume 13,5,559

commandID 13 - Client51 consume 51,4,556

commandID 12 - Client92 consume 92,3,3061

commandID 13 - Client35 consume 35,4,558

networkWorker reprocess - coomandId 13 2,2,-1

commandID 13 - Client72 consume 72,1,2575089073339375790853240072539953000324090803785938792323520747894596586880561785903668912580008012993072158173141525746725031806674615836396357389725989624877888112952366831514129229329545795404526827804782172390348517583112652422037369009139161487186576336198774837827437708696098

commandID 13 - Client46 consume 46,5,561

commandID 13 - Client31 consume 31,5,567

commandID 13 - Client58 consume 58,1,445671365606064404752434845738144118514434248722956515077752203586773237872355523746539818366203850074757103225800503946351971819678605461327842969802725769413150000861480609757540829706379968454626106674887502966839788365082868982543804669458485967347761748068014911280572704290

commandID 13 - Client69 consume 69,2,2143443

commandID 13 - Client45 consume 45,2,2126336

commandID 13 - Client32 consume 32,1,7997248166935866718695925386263899673242571579761461330015233088418447384403294691427574703747986632490457541892278435968295254796993284008359933632143773030115556696494964342073575740756224875655449218962632035429571621590353387194189342084177753048293610054687303734459715224066

commandID 13 - Client78 consume 78,3,3119

commandID 12 - Client100 consume 100,4,560

commandID 13 - Client95 consume 95,1,607896069425034063580366462608235237581059737399738861224581076126662826799793709245429776932727614364579669880576578600810517298037103832426801606829301109026305310604079738491640601706410031902795841992532420813895767045712843580381126164073355104571159779240617846379610851841800

commandID 13 - Client12 consume 12,2,2137664

commandID 13 - Client27 consume 27,1,1887894200332450578485872635131438888682034332759626203734370221207918536632734791920258721345445695603925109666619483005485820744328669636758022665585261815175601673908370933079008727762461226800205778071936133115682958306317629552911384353679816770236462076654822205794785629944

commandID 13 - Client38 consume 38,2,2111068

commandID 13 - Client65 consume 65,3,3089

commandID 13 - Client76 consume 76,1,105208737908192959476133252178862414624297337867800143423361406860825585143312696934099447880630295304896696763417467220077933465614247791446650786374358737523001670462448494048845408936941352981701351372386121248323804845986153622736165675845872900845415084382762560672494812784

commandID 14 - Client22 consume 22,1,10908252362782537226993326752768047238877422952543494030518664067705049174322040852860105427252759666336868302573142681587710644524735567178012231165733259608537857762413547064548157519024593213520903153211661110375289837378163453268530602200630001053317465124035717197689361686626192

commandID 13 - Client2 consume 2,2,2133128

commandID 14 - Client5 consume 5,1,143504795639239536531774222107012049999851854186983347425196443387945279681386948921949804849097555534753478650835211343482962614526200506689150962408785188772666870536047877547566822503905667793343459834652489134765449400261278100512864352845741068901937219236303452308994301328898

commandID 13 - Client25 consume 25,2,2139165

commandID 13 - Client41 consume 41,1,33876886868075917453269574180187037581652320651805471523795302574881708074245913557630557536337392225565755277235733226878666839932301805670197757194160353935637828459888228301373311690787360729422002653922464274833969444667731178329668752690390828963410902295404037143633646526208

commandID 12 - Client97 consume 97,3,3083

commandID 13 - Client48 consume 48,2,2136294

commandID 13 - Client40 consume 40,4,562

commandID 12 - Client87 consume 87,2,2118208

commandID 13 - Client37 consume 37,3,3109

commandID 13 - Client9 consume 9,2,2125098

commandID 13 - Client18 consume 18,5,563

commandID 13 - Client24 consume 24,2,2121468

commandID 13 - Client16 consume 16,2,2150031

commandID 13 - Client3 consume 3,5,565

localWorker reprocess - coomandId 14 7,5,-1

networkWorker reprocess - coomandId 13 52,2,-1

networkWorker reprocess - coomandId 13 59,2,-1

commandID 13 - Client60 consume 60,5,577

commandID 14 - Client10 consume 10,2,2171351

commandID 13 - Client98 consume 98,2,2179988

commandID 13 - Client74 consume 74,5,569

commandID 13 - Client49 consume 49,1,829170684367112068788024607432478602204683996247492529666843665588971682528156491766289962276058832197136744474209679012009491946494429306035618719558136515437649856813965624783207538268372989895381983103920896877656793090140683726508838631600725821121029286645950810476700482484919490

commandID 13 - Client79 consume 79,3,3163

commandID 13 - Client62 consume 62,1,195740646460660636022299515087216615062212553408383153688111372142564222310996941642236467913616946379699049776435991689977981084147203105371793359376369371844655134412839767423375194740736267811473460915817367565941321305761229315253169713447266663855143212453402291712028899507429656

commandID 14 - Client7 consume 7,5,573

commandID 14 - Client23 consume 23,1,46208098524469524698826547083612141955833782613959914914398177018714793284168725197344090621591046678340545368465712252097567609905616884548445282052659028059029319162606555089706759305427918649488139440651426613891507867095766465496159777811659165700456436832341643628584884455200866

commandID 14 - Client29 consume 29,4,568

commandID 13 - Client55 consume 55,3,3121

commandID 13 - Client59 consume 59,2,2179272

commandID 14 - Client4 consume 4,4,564

commandID 13 - Client33 consume 33,3,3137

commandID 13 - Client67 consume 67,4,566

commandID 12 - Client89 consume 89,5,575

commandID 13 - Client28 consume 28,2,2155061

commandID 13 - Client52 consume 52,2,2165435

commandID 13 - Client83 consume 83,4,576

commandID 13 - Client77 consume 77,2,2194416

commandID 13 - Client53 consume 53,5,583

commandID 14 - Client64 consume 64,4,578

commandID 13 - Client86 consume 86,5,585

commandID 13 - Client90 consume 90,2,2203549

commandID 14 - Client57 consume 57,3,3167

commandID 13 - Client88 consume 88,3,3169

commandID 14 - Client42 consume 42,1,3512423383929108911174397944817131023880948538398353272355486034498450952423622908707396317017852275168246027673274707738015948870124920329514268237608915433595254561668702266556205347814228227393001393331500955076568493666323964221288524239850169948339260359037205533618830829447107616

commandID 13 - Client80 consume 80,5,579

commandID 13 - Client66 consume 66,5,581

commandID 13 - Client81 consume 81,4,570

commandID 13 - Client43 consume 43,4,574

commandID 13 - Client85 consume 85,2,2185168

commandID 13 - Client30 consume 30,4,572

commandID 13 - Client84 consume 84,2,2206831

localWorker reprocess - coomandId 14 44,4,-1

networkWorker reprocess - coomandId 14 56,2,-1

commandID 13 - Client99 consume 99,1,266990385277136746773953070353185569956907922700888808613931336798900987177479509786959464183978363959465038648537343500341309681739399561927632831340330231981298376005983978813361313193186773200517594032634724012433097026507717047283424002006424575239484243723659962198659727922435379682

commandID 14 - Client44 consume 44,4,586

commandID 14 - Client26 consume 26,5,587

commandID 13 - Client68 consume 68,2,2211344

commandID 13 - Client75 consume 75,1,63027880264263299765116863491621141814794861137761975748710637248829552921314215415090897238407724006648729448342508747594309098578101362825885034917584108432869926975623801030588321065915371825262551619051199823812291564688070126667940266603855792406251543250216297313426926030540507432

commandID 14 - Client15 consume 15,2,2212638

commandID 14 - Client6 consume 6,2,2221681

commandID 14 - Client54 consume 54,2,2207710

commandID 14 - Client19 consume 19,3,3209

commandID 13 - Client71 consume 71,1,1130989421372810286860929144904363421642426551941317210204435984444433501631232254562928753974321179844508884042491882748959547825535699610536416360278905036358063430999559716284033573838662464627332927749590095873544679670718938315801636274629554093364188518144856146108065837720282026160

commandID 14 - Client56 consume 56,2,2229836

commandID 14 - Client36 consume 36,3,3203

commandID 13 - Client96 consume 96,3,3187

commandID 14 - Client91 consume 91,4,580

commandID 14 - Client39 consume 39,5,589

commandID 14 - Client1 consume 1,3,3181

commandID 13 - Client82 consume 82,3,3191

commandID 14 - Client61 consume 61,1,20294781704446321863731607744786920447748883073805947807931137082750973476440866366717626674299373513194511183316511380733677551761064491626829609450102706546012271831016451092082016008030008991466730147873570525899991942508252819557761512676728117888149141783357194332631758152934535963448

commandID 13 - Client70 consume 70,1,14878864220083547713485616386701002697728478149840905619088787803582775492222648126595875230347467932870120855167308509964073287426994110624092691669993798249818668103488774691008028929525285899467387556429924717183930767755436540611662935591001405614478070722794772944952023800273349954

commandID 14 - Client8 consume 8,5,591

commandID 14 - Client14 consume 14,1,4790948070768377894217669649970639256526614130466157649431675274576634993702408528038674480081263083337500574818504874496179500983882198004073298272455950377413552100004222843949495608547836631709849305030995107506611815709383470310489969100524640948696238316303084546630923078803563484322

commandID 14 - Client17 consume 17,3,3217

commandID 14 - Client20 consume 20,5,593

commandID 13 - Client93 consume 93,1,85970074888553665349144100629118321047522146425689948881156223605580528899465873994909181177278757136115545308084550397430889708028140164511391736072866776561462639424070027212277559640667872597576769896525277211106579585742394748541536019807437112501292805449731861877157955690541707338114

commandID 13 - Client47 consume 47,4,582

commandID 14 - Client21 consume 21,2,2230320

localWorker reprocess - coomandId 14 50,5,-1

networkWorker reprocess - coomandId 13 92,2,-1

networkWorker reprocess - coomandId 13 92,2,-1

commandID 13 - Client63 consume 63,2,2236699

commandID 14 - Client34 consume 34,2,2237253

commandID 14 - Client11 consume 11,1,1542670399923197598390376141674159139598872021531952922211380349625872885196683323380326586710936365366742314970703402279259835243522640763200977951039146027728913957533256266977046577923473870124672008832423994692411820727653722003437158387433343384074574259778870429242212279350947168601730

commandID 14 - Client13 consume 13,4,590

commandID 14 - Client72 consume 72,3,3229

commandID 14 - Client46 consume 46,4,592

commandID 14 - Client58 consume 58,4,594

commandID 14 - Client31 consume 31,3,3251

commandID 13 - Client94 consume 94,1,364175081258660983260308010261260204637837468776565743332556031505073089074304362346354351383414402057656692415654712970457236383873625149672396553741569812791862829527296559941192254570701499381773809733974679370326310285477831813723905591906476567893320363582284641841263580915101365315904

commandID 14 - Client50 consume 50,5,597

commandID 13 - Client92 consume 92,2,2264505

commandID 14 - Client51 consume 51,2,2245189

commandID 14 - Client35 consume 35,3,3221

localWorker reprocess - coomandId 14 32,5,-1

commandID 13 - Client73 consume 73,4,588

networkWorker reprocess - coomandId 14 38,3,-1

networkWorker reprocess - coomandId 15 22,3,-1

localWorker reprocess - coomandId 14 2,4,-1

localWorker reprocess - coomandId 14 2,4,-1

networkWorker reprocess - coomandId 15 5,2,-1

networkWorker reprocess - coomandId 14 25,1,-1

networkWorker reprocess - coomandId 14 41,2,-1

commandID 14 - Client45 consume 45,2,2267126

commandID 13 - Client100 consume 100,2,2272690

commandID 14 - Client32 consume 32,5,603

commandID 14 - Client2 consume 2,4,600

commandID 14 - Client95 consume 95,3,3253

commandID 14 - Client65 consume 65,1,27682097123729003105677626449505746191732174241149462650923690069660131404640833946850969379619575819465246124164576690629246144675379393573106211382631761722558988596174542778374560842981861789646519389087106627252306193512024601313327314953992743800841043870569935864482663072626507327493026

commandID 15 - Client22 consume 22,3,3301

commandID 14 - Client78 consume 78,1,6534856680951451376821812576957896763033325554904377432178077430008564629861037655867660698227159863524625952298468322087496577357964188202476308357898153923707518659660321627849378566264596979880461845063670658139973593196092719827472539141639850104191617402697766358810112698318890039722824

commandID 14 - Client25 consume 25,1,496735077827198858303806899949429272311580264319158374794415040904256492398338327719937122246441428385007687919991677029047170768913306443552710826936332564978332880773608513743765048595750038343512676994735495295849099662488789101636454510784436045031064215410479975131445723027926184726272738

commandID 14 - Client69 consume 69,5,599

commandID 13 - Client97 consume 97,3,3307

commandID 15 - Client5 consume 5,2,2289451

commandID 14 - Client48 consume 48,3,3313

commandID 14 - Client12 consume 12,5,605

commandID 14 - Client41 consume 41,2,2294106

commandID 14 - Client27 consume 27,2,2278396

commandID 14 - Client38 consume 38,3,3259

commandID 14 - Client76 consume 76,3,3271

commandID 14 - Client37 consume 37,5,607

commandID 13 - Client87 consume 87,1,2104203556484662897014759918172697970776283079796135727213533001325675059841777684323020027202471176681416362128923483200793164231712707536705744461633755460727274996138792547716407816321192197512517247380354078350545597017199347531626599842095355005431812654526897410342523657100529658254785880

commandID 14 - Client40 consume 40,4,602

commandID 14 - Client3 consume 3,1,8913549303765850446362846572640221155416712583503701283648547046206956731765449065012017231056326135110673136435685609832219827695764136590375688673471354407887432865328778704609396313880518828393581666516151808698031487731286179228142853879165856066758314833518069616501540351430044817745416258

commandID 14 - Client16 consume 16,3,3329

localWorker reprocess - coomandId 14 60,4,-1

networkWorker reprocess - coomandId 15 7,2,-1

commandID 14 - Client60 consume 60,4,608

commandID 14 - Client74 consume 74,1,37758400771548064682466146208733582592443133413810940861807721186153501986903573944371088951427775717124108907871665922529672475014769253898208499155519173092277006457453907366153993071843267511086843913444961313142671547942344064444198015358758779272465071988599175876348685062820708929236450912

commandID 14 - Client98 consume 98,4,610

commandID 15 - Client10 consume 10,5,609

commandID 14 - Client49 consume 49,4,612

commandID 14 - Client79 consume 79,2,2301134

commandID 14 - Client62 consume 62,4,614

commandID 15 - Client29 consume 29,1,159947152389958109176227431407574551525189246238747464730879431790820964679379744842496373036767429003607108767922349299950909727754841152183209685295548046776995458695144408169225368601253588872740957320295997061268717679500662437004934915314200973156618602787914773121896280602712880534691219906

commandID 15 - Client23 consume 23,4,616

commandID 15 - Client7 consume 7,2,2312258

commandID 14 - Client9 consume 9,4,604

commandID 14 - Client24 consume 24,3,3323

commandID 14 - Client18 consume 18,3,3319

networkWorker reprocess - coomandId 14 53,3,-1

networkWorker reprocess - coomandId 14 81,2,-1

commandID 14 - Client77 consume 77,2,2319494

commandID 15 - Client64 consume 64,4,622

commandID 14 - Client86 consume 86,3,3361

commandID 15 - Client57 consume 57,5,613

commandID 14 - Client90 consume 90,1,12158087785193300960290299546893838613885158997224603455274050349103718990692702385714047370821527075450821682724227470279469932473599640273460641826923285310512382135827113813408844421511593946525825273589876130734773089239067544559906680163721369314908445624536049654674459849463279450294787498736

commandID 15 - Client42 consume 42,2,2330987

commandID 14 - Client88 consume 88,2,2322577

commandID 14 - Client83 consume 83,4,620

commandID 14 - Client55 consume 55,1,677547010331380501387375871839031788693200118368800799785325448349437360704422553314356581098497491731552543979561063122333311386034133862631047240337711360200258841238031540043055467476857623002050673194628949558217542265944993812463937676615562671898939483140258268363933807473672231068001330536

commandID 14 - Client59 consume 59,3,3331

commandID 15 - Client4 consume 4,3,3343

commandID 14 - Client33 consume 33,2,2312439

commandID 14 - Client67 consume 67,5,611

commandID 14 - Client80 consume 80,3,3371

commandID 14 - Client53 consume 53,3,3359

commandID 14 - Client81 consume 81,2,2341601

commandID 14 - Client28 consume 28,4,618

commandID 14 - Client66 consume 66,5,615

commandID 13 - Client89 consume 89,1,2870135193715480114725730918763701706297989719713950663872181225188570407497069958099922697430757395929817284686166601789284155271891376602707398646646393487578030823647270568341447238508684080880943650098811795294138886743280637686860685621776451660752376535348947846577631510497401804806696542050

commandID 14 - Client84 consume 84,4,624

commandID 14 - Client85 consume 85,2,2349460

commandID 14 - Client52 consume 52,2,2318350

commandID 14 - Client43 consume 43,1,51502486334488683955886929106339056161838625708612364484968382621603446370267879500956112180716865697733104015583076482907163885166289937696549965954339534729627559366955725821976824924555059866984244744458316318233231243699550815926487406276661928920386159033493146465275470908350519605985846536994

commandID 14 - Client30 consume 30,1,218168033123148036783838015972250063261239661831674061395147580835517504471764220389538496093688989866383237745056533401908125473138759391059660505644281424229022619603650017101316144119731833414462804251423141403667698064037270808265856305270369084996453081758508635515776343482865357874238173646712

localWorker reprocess - coomandId 15 54,4,-1

networkWorker reprocess - coomandId 15 19,3,-1

networkWorker reprocess - coomandId 15 39,2,-1

commandID 15 - Client26 consume 26,2,2356697

commandID 14 - Client75 consume 75,5,617

commandID 15 - Client15 consume 15,3,3373

commandID 14 - Client71 consume 71,2,2365662

commandID 15 - Client91 consume 91,4,632

commandID 15 - Client1 consume 1,3,3413

commandID 14 - Client96 consume 96,2,2370238

commandID 15 - Client44 consume 44,1,924174618827080831091238992995339309206797273035308610065558705963673464257324761059110096555472825163266054995809210090539665777721327501935191988531465231645718037781555794227241401403482393524835461750150881932904023499848634048989912627358138268906198486067527688528380844839811951102938541123842

commandID 15 - Client39 consume 39,2,2382950

commandID 15 - Client36 consume 36,3,3407

commandID 15 - Client19 consume 19,3,3391

commandID 15 - Client54 consume 54,4,630

commandID 14 - Client68 consume 68,1,3914866508431471361148793987953607300088428753972908501657382404690211361501063264625978882315580290519447457728293373764066788584024069398800428459770142350811894770729873194010281749733661407513804651252026669135283792063431807004225506814702922160621247026028619389629299722842113162285992338142080

commandID 15 - Client56 consume 56,5,619

commandID 14 - Client99 consume 99,4,626

commandID 15 - Client6 consume 6,1,16583640652552966275686414944809768509560512288926942616695088324724518910261577819563025625817793987241055885908982705146806820113817605097136905827612034634893297120701048570268368400338128023580054066758257558474039191753575862065891939886169826911391186590182005247045579736208264600246907893692162

commandID 14 - Client82 consume 82,5,621

commandID 14 - Client70 consume 70,5,623

commandID 15 - Client61 consume 61,2,2384678

commandID 15 - Client14 consume 14,5,625

commandID 15 - Client8 consume 8,3,3433

commandID 15 - Client20 consume 20,4,634

commandID 15 - Client17 consume 17,3,3449

localWorker reprocess - coomandId 14 47,5,-1

commandID 14 - Client63 consume 63,5,631

commandID 15 - Client11 consume 11,2,2391054

commandID 15 - Client34 consume 34,5,633

commandID 15 - Client13 consume 13,4,638

localWorker reprocess - coomandId 15 72,4,-1

commandID 14 - Client93 consume 93,4,636

commandID 14 - Client47 consume 47,5,629

commandID 15 - Client21 consume 21,3,3457

networkWorker reprocess - coomandId 15 50,3,-1

networkWorker reprocess - coomandId 14 73,2,-1

networkWorker reprocess - coomandId 14 73,2,-1

localWorker reprocess - coomandId 15 32,5,-1

networkWorker reprocess - coomandId 15 78,2,-1

commandID 15 - Client72 consume 72,4,642

commandID 15 - Client65 consume 65,1,1260574857627148584988951373821514656789860173620279312930221860259898954684351678507179486058246032020186630566827742124559226454123276746773464503404158913676119603792883341357497314569817461625498571877878997585430646271335802787816053287654177214350726633935590907410979836295310974976639238094251024

commandID 15 - Client51 consume 51,2,2408669

commandID 15 - Client25 consume 25,3,3491

commandID 15 - Client35 consume 35,1,70249429118643336463894453767192681338330477909680678968437735703588287002547374542878081385586756239483671001364224194351294069039294489787348051770218280890385083253534067475083755351086173501834020918285056903031440559077735255267793266359382229806185993386756640377811618667675171563273623912910728

commandID 15 - Client78 consume 78,2,2436217

commandID 16 - Client22 consume 22,1,5339880787635720652087069725299639121022323118408766910211333472178673485657857790019793295401148947025922262158676847980788888912764102551340387126525120812900912045306370683900592648083952668532910425251414475512322386513407728034401278155940407603539041695879572196402211399588152850759898355922339170

commandID 15 - Client95 consume 95,1,297581357127126312131264230013580493862882423927649658490446031139077666920451075991075351168164818945175739891365879482551983096270995564246529112908485158196433630134837318470603389804682822030916137739898485170599801428064516883137065005323698746136135160137208566758292054406908950853341403545335074

commandID 15 - Client2 consume 2,2,2431178

commandID 15 - Client45 consume 45,3,3469

commandID 14 - Client100 consume 100,5,639

commandID 14 - Client94 consume 94,2,2401550

commandID 15 - Client46 consume 46,5,635

commandID 15 - Client58 consume 58,5,637

commandID 15 - Client31 consume 31,2,2393683

commandID 15 - Client32 consume 32,5,643

commandID 15 - Client50 consume 50,3,3463

commandID 14 - Client92 consume 92,3,3467

commandID 14 - Client73 consume 73,2,2422017

networkWorker reprocess - coomandId 15 49,1,-1

networkWorker reprocess - coomandId 15 18,2,-1

commandID 15 - Client79 consume 79,3,3529

commandID 15 - Client49 consume 49,1,130698922376339931803631155380271983098392443907412640726006659460192793070479231740288681087777017721095463154979012276234322246936939647185366706368489362660844147449941348462800922755818969634743348982916424954062744135969865615407276492410653721774590669544801490837649161732095972658064630033793347171632

commandID 16 - Client23 consume 23,3,3539

commandID 15 - Client74 consume 74,2,2463646

commandID 15 - Client69 consume 69,4,644

commandID 15 - Client48 consume 48,2,2444355

commandID 15 - Client27 consume 27,5,645

commandID 16 - Client7 consume 7,4,650

commandID 16 - Client29 consume 29,2,2463826

commandID 15 - Client98 consume 98,1,7283601309201631400918124254102695714681748821938312038436760513434115324648679526650262680906049479323648485031892948939169808543798819904097686841008724558939194841692660342713602382268261173540297333847580596625476870488080204470730347630209530674149413494426762504512005978337963330549663643470408769960

commandID 15 - Client60 consume 60,3,3527

commandID 15 - Client12 consume 12,3,3511

commandID 14 - Client97 consume 97,3,3499

commandID 15 - Client76 consume 76,4,648

commandID 16 - Client10 consume 10,5,647

commandID 15 - Client38 consume 38,2,2450262

commandID 16 - Client5 consume 5,1,22620098008170031193337230275020071140879152647255346953775555748974592897315782838586352667662841820123875679201535134047714782105179686952135013009504642165279767785018366076959867906905628135757140272883536899634720192324966714925421165911415807628506893417453879693019825434647922378016232661783607704

commandID 15 - Client62 consume 62,3,3533

commandID 15 - Client41 consume 41,4,646

commandID 15 - Client9 consume 9,2,2472739

commandID 14 - Client87 consume 87,3,3517

commandID 15 - Client40 consume 40,1,95820272820315845425435990825379923684538933707430154725313556468077045074920989144365203966052516227521424978964817384171648017333482850359880439164543689474019983185379834991740064275706465211561471516785562074051203155813274587736085941801603638117566615365695090968481513138179842362824829003056769986

commandID 15 - Client24 consume 24,5,649

commandID 15 - Client18 consume 18,2,2485565

commandID 15 - Client37 consume 37,2,2457348

commandID 15 - Client3 consume 3,1,405901189289433412895081193576539765879034887476975965855029781621282773196999739416047168531872906730209575595060804670734306851439111088391656769667679400061359700526537706043920125009731488982003026340025785195839532815578065065869764933117830360098773354880234243566945877987367291829315548674010687648

commandID 15 - Client16 consume 16,1,1719425029978049497005760765131538987200678483615334018145432682953208137862919946808553878093544143148359727359208036067108875423089927203926507517835261289719458785291530659167420564314632421139573576876888702857409334418125534851215145674272925078512660034886632065236265025087649009680087023699099520578

commandID 15 - Client88 consume 88,5,657

commandID 15 - Client83 consume 83,4,656

commandID 15 - Client55 consume 55,3,3541

commandID 15 - Client59 consume 59,3,3547

commandID 15 - Client67 consume 67,5,659

commandID 15 - Client33 consume 33,2,2498613

commandID 15 - Client86 consume 86,4,652

commandID 16 - Client4 consume 4,1,553649519772144302315202879302630254239497449401019145075919112577460841718374565014564328952825812944824806287402828936761077097346043795561784080355827610168852827951827565881225521116663555654274158843932910905610293360249908814363242505837725934873472992191799645433880935866823392964137261732754123286946

commandID 15 - Client77 consume 77,5,651

commandID 16 - Client42 consume 42,5,655

commandID 15 - Client90 consume 90,2,2494571

commandID 16 - Client57 consume 57,4,654

commandID 16 - Client64 consume 64,5,653

commandID 15 - Client28 consume 28,2,2507366

commandID 15 - Client81 consume 81,3,3557

commandID 15 - Client84 consume 84,5,663

networkWorker reprocess - coomandId 15 85,3,-1

commandID 16 - Client15 consume 15,5,665

commandID 16 - Client26 consume 26,3,3583

commandID 16 - Client91 consume 91,4,662

commandID 15 - Client75 consume 75,2,2522165

commandID 16 - Client1 consume 1,5,667

commandID 15 - Client80 consume 80,4,658

commandID 16 - Client39 consume 39,3,3593

commandID 16 - Client19 consume 19,3,3607

commandID 16 - Client44 consume 44,5,669

commandID 15 - Client53 consume 53,1,2345297001464917141064442672590793000056382241511489221029683109770036159943977491798545996899080269500394688304590328023278630636321114829432503027791799803336255459257251611987703007222473192251839984358648068576503917576969500872860246515761557461268482638312000072573172905199389544514613676964809840319416

commandID 15 - Client71 consume 71,1,9934837525631812866572973569665802254465026415446976029194651551657605481494284532208748316549146890946403559505764141029875599642630503113291796191523026823513874664980834013832037550006556324661634096278525185211625963668127912305804228568883955779947403545439799935726572556664381571022591969591993484564610

commandID 16 - Client36 consume 36,1,178273425941600487295998321374681810326130978028644549380427808817259437825178747014742905368931818224090439264816351709600999716470003012243690547367058655213080891141703184683095450379001350288255139574169520422903657052666052512690112871734073478104179790825724599197644425084092044885442518190923128598876034

commandID 15 - Client96 consume 96,1,42084647103992168607356336951254002017916487903299393337808289316400458085921115620633539263095667833286008926327646892142781029206843127282599687793883907097391754119180587667315853207248698490898376369472748809423007772249481150096077160791297380581058096820071199815479463131856915828604981555332783778577856

commandID 15 - Client66 consume 66,5,661

commandID 14 - Client89 consume 89,3,3559

commandID 15 - Client85 consume 85,3,3581

commandID 15 - Client43 consume 43,4,660

commandID 15 - Client52 consume 52,2,2511073

commandID 15 - Client30 consume 30,2,2515424

commandID 16 - Client11 consume 11,1,57403489503675584765009144279768240294759935427774143881352678520044961518865838164182200964467092642344176618464577847662585147626243623898672794468112531151001878094800473640390853796517318129262500668723741643264071960665108659177401981455865822223611019172891129141996070996141771629719526720215514654714796002

commandID 15 - Client63 consume 63,5,675

commandID 16 - Client34 consume 34,5,677

commandID 16 - Client6 consume 6,4,666

commandID 15 - Client68 consume 68,3,3613

commandID 16 - Client54 consume 54,4,664

commandID 16 - Client13 consume 13,1,243165083683265441011673513986221369556825752423515072767544257233401333386336881407341063211904087834677080252672658710908039847587331045640041091975374374200004996361430593849090657117880597612149645122516208773265542282580631597174929364321768634787823009276957898107077833463863281319716252877049181202214082008

commandID 16 - Client56 consume 56,1,755178350870394117791349622449981243322440400017877590859519524585438209386636103679605160738822940729647765985593053730546779895086855176257361877262118527949715318685993326399697654723254099643918934666150830501037635982913691200856528647727591292997777260122969596606057163468225095370375054319025298174081992

commandID 15 - Client99 consume 99,5,671

commandID 16 - Client61 consume 61,3,3617

commandID 16 - Client14 consume 14,3,3623

commandID 15 - Client70 consume 70,5,673

commandID 16 - Client8 consume 8,1,3198986829423176958461396811174606783615892578100154912818505907159012275371723161733163548324223581142681503207188566631788119296817423717273138056415532767011942165885676490281886069272017748863930878238772842427054200984320817316116227462644438650095288831317602985621873078956992426366942735467024321295204002

commandID 15 - Client82 consume 82,4,668

commandID 15 - Client93 consume 93,2,2527969

commandID 16 - Client20 consume 20,1,13551125668563101951636936867148408377786010712418497242133543153221487310873528750612259354035717265300373778814347320257699257082356550045349914102924249595997483982228699287527241931811325095099642447621242200209254439920196960465321438498305345893378932585393381539093549479296194800838145996187122583354898000

commandID 16 - Client21 consume 21,1,1030063824236737348811703200224653718522062945121834434951529707453650295064213363793546453812083443981052497629155212691294744537975567806458837162369610027951021863540522849036753482268039708577861081158788576736326241090987635047877119438742940361374903056280722721570307404851594896908584538228412239463571124034

commandID 16 - Client17 consume 17,4,670

commandID 15 - Client47 consume 47,5,679

networkWorker reprocess - coomandId 16 35,3,-1

commandID 15 - Client100 consume 100,5,685

commandID 16 - Client2 consume 2,4,676

commandID 16 - Client95 consume 95,5,683

commandID 16 - Client45 consume 45,4,678

commandID 17 - Client22 consume 22,5,681

commandID 16 - Client78 consume 78,1,18483745346757596693845648459763998693102373076765245685246182055645660349636974710119653967653034899016600780706329250595642816535933976892360396128184867971967391665634610809021171827028197436272236960189470639610608267677112322202610747915917060682524643993880117859123537216332566372724802161391204795689565436610

commandID 16 - Client35 consume 35,3,3637

commandID 16 - Client25 consume 25,4,674

commandID 16 - Client51 consume 51,1,4363420380630214836258486314884836243645077532910852812573663087048002513643190336581526878460237863758887070769293509476087017999489602271475389741453814486004092450523521989996104586190039431923593969757670515718570506646531171788683407119293530080287435234399848784388307452870242868954054405790698139056498578144

commandID 16 - Client72 consume 72,4,672

commandID 16 - Client65 consume 65,2,2533703

commandID 16 - Client31 consume 31,3,3643

commandID 15 - Client94 consume 94,5,687

commandID 16 - Client46 consume 46,2,2534014

localWorker reprocess - coomandId 15 92,4,-1

networkWorker reprocess - coomandId 16 49,3,-1

commandID 17 - Client23 consume 23,5,693

commandID 16 - Client74 consume 74,4,688

commandID 16 - Client49 consume 49,3,3671

commandID 16 - Client79 consume 79,2,2541711

networkWorker reprocess - coomandId 17 7,3,-1

networkWorker reprocess - coomandId 17 29,2,-1

networkWorker reprocess - coomandId 17 29,2,-1

networkWorker reprocess - coomandId 17 29,2,-1

commandID 16 - Client50 consume 50,4,680

commandID 16 - Client58 consume 58,5,689

commandID 16 - Client32 consume 32,5,691

commandID 15 - Client92 consume 92,4,684

commandID 15 - Client73 consume 73,4,686

networkWorker reprocess - coomandId 16 40,3,-1

commandID 16 - Client62 consume 62,2,2575359

commandID 16 - Client98 consume 98,2,2562667

commandID 16 - Client38 consume 38,1,1405007811437260614173280956456050122045337179586582187151477380486303587905796414850501042604842556413096336413933695703979762096578569574865030146834025340243721771584591852079458149511260885754302158619522284819179493885743117118995591770974018380506660766544165915191495906274738907608404680518608613653609187264368

commandID 16 - Client60 consume 60,2,2566216

commandID 17 - Client29 consume 29,2,2562074

commandID 16 - Client41 consume 41,3,3691

commandID 15 - Client97 consume 97,1,331677352417400003140409969075527322757320652436652587899479747294168235998401331418360224963942544738317761555084771298030275953108836016256028293144958013467462028117882471713344339404239514144322404202251682936254622577097034164599116343047764151924068688833561398742653362489134599812137854366813274082948606734946

commandID 17 - Client5 consume 5,4,694

commandID 17 - Client7 consume 7,3,3677

commandID 16 - Client67 consume 67,3,3733

commandID 16 - Client27 consume 27,1,78298401767660601611641080153940831016054569839971835553558391309630643912191089177060142749072377459825290193594610511858658284143225509840916974254193286373873659113061965226080791894302829177012541810515553074161003577354980460599126398782961772810386011209920320220882456318200508359853263051355517321814760324584

commandID 16 - Client69 consume 69,4,690

commandID 16 - Client83 consume 83,5,699

commandID 17 - Client10 consume 10,2,2572482

commandID 16 - Client76 consume 76,5,695

commandID 16 - Client12 consume 12,4,692

commandID 16 - Client48 consume 48,2,2544179

commandID 16 - Client59 consume 59,2,2582827

commandID 16 - Client55 consume 55,1,106799077414578564273863198339119573274138728021657011469197527099014718341190164503348198891935687322294338168239667202753057562156507221666634651555514110726494822032094615368847840534682855514763236292043883116897252143584154013365867585341941313979188742901350489672412812414096489544611480521575645842469987797528578

commandID 15 - Client87 consume 87,2,2577365

commandID 16 - Client24 consume 24,1,5951708598166442459833533794899727810938669370782981336505389269239382587621586990820364395383312770390703107210819554113949324339423114315716148880481059374442349114456249880031176937449283057161531038680340822212972598120069502640581483426943837673950711755010225059508636987588090230245756576441247728697385355792418

commandID 16 - Client37 consume 37,3,3719

commandID 16 - Client18 consume 18,5,697

commandID 16 - Client40 consume 40,3,3709

commandID 16 - Client9 consume 9,3,3697

localWorker reprocess - coomandId 17 4,5,-1

networkWorker reprocess - coomandId 16 77,3,-1

commandID 16 - Client16 consume 16,1,25211842204103030453507416136054961365800014662718507533173034457443833938392144378131958624138093637975908765257211912159777059454271026837729625668758262838013118229409591372204165899308393114400426313340885573671069886366021127681321525478749369076309507786585066153226043856627099828591430986283599528443150610434040

commandID 16 - Client3 consume 3,3,3727

localWorker reprocess - coomandId 16 88,5,-1

commandID 17 - Client4 consume 4,5,703

commandID 17 - Client57 consume 57,3,3767

commandID 16 - Client88 consume 88,5,707

commandID 17 - Client42 consume 42,5,709

commandID 16 - Client86 consume 86,1,452408151862417287548960209492533254462354926749346553409963142853502707303152802391524754191880842927153261438215880723172007308080299913504268231890814705743992406357788052847595528038039815173453371481516418041260078460702637181144791866846514624993064479391987024842877293513013058007037353072586182898323101800548352

commandID 16 - Client77 consume 77,3,3761

commandID 17 - Client15 consume 15,5,713

commandID 16 - Client90 consume 90,1,1916431684864247714469704036309252591123558435019043225109050098513025547553801374069447215659459059030907383921103190095441086794477706875683707579118772933702464447463246826759229952686842116208576722218109555281937565986394702737945035052727999813951446660469298589043921986466148721572760892811920377435762394999721986

commandID 16 - Client33 consume 33,4,696

commandID 17 - Client64 consume 64,2,2587803

commandID 16 - Client81 consume 81,5,711

commandID 16 - Client28 consume 28,4,698

localWorker reprocess - coomandId 17 39,5,-1

commandID 16 - Client84 consume 84,2,2591160

networkWorker reprocess - coomandId 17 36,3,-1

localWorker reprocess - coomandId 16 96,4,-1

networkWorker reprocess - coomandId 16 43,3,-1

commandID 17 - Client26 consume 26,5,715

commandID 16 - Client71 consume 71,2,2614142

commandID 16 - Client96 consume 96,4,704

commandID 17 - Client44 consume 44,4,700

commandID 17 - Client1 consume 1,3,3769

commandID 17 - Client39 consume 39,5,719

commandID 17 - Client36 consume 36,3,3797

commandID 17 - Client91 consume 91,1,8118134891319408145427776354729543618956588666825519453846163536905604897518358298669313616829717079050782797122628641104936354485991127416239098548365906440553850196210775359884515338785408280007760260353954639169010342406281448132924932077758513880798851121269181381018565239377607944298080924320267692641372681799436296

commandID 16 - Client80 consume 80,3,3779

commandID 17 - Client19 consume 19,2,2601223

commandID 16 - Client53 consume 53,2,2608847

commandID 16 - Client75 consume 75,2,2600622

commandID 15 - Client89 consume 89,2,2621574

commandID 16 - Client66 consume 66,3,3803

commandID 16 - Client85 consume 85,5,721

commandID 16 - Client30 consume 30,5,723

commandID 16 - Client52 consume 52,1,34388971250141880296180809455227427066949913102321121040493704246135445137627234568746701682978327375234038572411617754515186504738442216540640101772582398695917865232306348266297291307828475236239617763633928111957978935611520495269644763363762055337146851145546024113118182943976580498765084590092991148001253122197467170

commandID 16 - Client43 consume 43,3,3823

networkWorker reprocess - coomandId 16 82,2,-1

commandID 16 - Client99 consume 99,3,3853

commandID 17 - Client54 consume 54,3,3847

commandID 17 - Client13 consume 13,3,3851

commandID 17 - Client34 consume 34,1,145674019891886929330151014175639251886756241076110003615820980521447385448027296573656120348743026579986937086769099659165682373439759993578799505638695501224225311125436168425073680570099309224966231314889667087000926084852363429211503985532806735229386255703453277833491297015283929939358419284692232284646385170589304976

commandID 17 - Client56 consume 56,5,727

commandID 16 - Client63 consume 63,3,3833

commandID 17 - Client11 consume 11,4,706

commandID 17 - Client6 consume 6,5,725

commandID 16 - Client68 consume 68,4,708

networkWorker reprocess - coomandId 17 95,1,-1

commandID 17 - Client14 consume 14,3,3863

commandID 16 - Client70 consume 70,4,710

commandID 17 - Client8 consume 8,1,617085050817689597616784866157784434613974877406761135503777626331924986929736420863371183077950433695181786919488016391177915998497482190855838124327364403592819109734051021966592013588225712136104543023192596459961683275020974212115660705494988996254691873959359135447083371005112300256198761728861920286586793804554687074

commandID 16 - Client82 consume 82,2,2628547

commandID 17 - Client61 consume 61,5,729

commandID 17 - Client21 consume 21,3,3877

commandID 17 - Client20 consume 20,1,11073141943468270876805946781384892395984597880219379318027503569728514319597628340971934593720129479138038125978372677286687301468216237218864446136119976865974826109980612047132358953280234343213642156653832808167352320014766015322812247935546039877247306880122918413934382495148044824112812626529421574010561035359786900162

commandID 16 - Client93 consume 93,1,2614014223162645319797290478806776990342655750703154545630931485849147333166972980027140852660544761360714084764721165223877346367429688757002152002948153115595501750061640256291441734923002157769384403407660052926847659184936260277674146807512762720248153751540889819621824781035733130964153466200139913430993560388808053272

commandID 16 - Client47 consume 47,5,731

commandID 17 - Client17 consume 17,4,712

localWorker reprocess - coomandId 18 22,5,-1

networkWorker reprocess - coomandId 17 35,1,-1

localWorker reprocess - coomandId 17 46,5,-1

commandID 17 - Client2 consume 2,4,714

commandID 17 - Client95 consume 95,1,841704461723480473566582106399427461346716195137748938173706092279888535674867781210440685243078583441070884511443092573447400593957873788627276705850756937315311009669651951750484354129867909393461770137006183087805777247207069527962942347087031837406024709145045252736846560928266690562513118035520731857087285072514393717288

commandID 17 - Client45 consume 45,4,716

commandID 18 - Client22 consume 22,5,735

commandID 16 - Client100 consume 100,1,46906581997035728827021077604346346574281047271580671817740945764763204611557486343914879227541062677912866588678211874370626552240294637632459936547428060579494806189984088444820877548043939530623953030022991285596256939244000321568923138549696922229237381272032563475359354761627912427415403972317826209473237701827955653920

commandID 17 - Client72 consume 72,4,720

commandID 17 - Client65 consume 65,1,271026222660737549843119640970136835776652272178604454937387730782638259339974258576781873507418643323263464098599911087484839113908067930249226097131940785662414549611877866823399670588082543822536920599712583294220533425941491451743789761615216738882019708190953030491444970794120838627998259853971475518068674799789245968913464

commandID 17 - Client25 consume 25,1,63980612232927984261937046033137871954746415428349138680519690516841257225609549000334464013067692471000525260995653408259289132442266624172891894090793647212829611561003528945083943272823241348246308172569123747481406423107752050140506430626549965682735125201903562126414273013043416527575109783326105042712644226546453709414050

commandID 17 - Client78 consume 78,3,3881

commandID 17 - Client51 consume 51,4,718

commandID 17 - Client35 consume 35,1,15103773729025612795371456837585347957666610465207900215308968715273230437536062575444017455147873439261363054617297454447682584139001433557658520768766196811096103367863751043063897496789578429551687909436088304294907733510483251181764039109016876151079207383338781985787878741947172517697820720667055347218097893603431131257264

commandID 17 - Client46 consume 46,5,739

commandID 16 - Client94 consume 94,1,1148085502875878183634415609913685215061355504142766958430070613647394294585506583307461958042742265764054381655395297758198645588074538345169796282618556789862487810008514996238682625625153416638393990571419456924363540126873717857115665477087416921210813957965715684092194156189526771039568149199212007114987343425703437585067906

commandID 17 - Client31 consume 31,2,2638258

commandID 18 - Client29 consume 29,1,6633637836917354213428467209191015973854233409828120519266881416663357605333724272522941476939513307700326026415369549755651599438701172128996295172265828805993235382275148778350142384446267295880648011812207345363174584300696264787647013528106292836422354254929073254431525519091284141406930232298618461309670458410202619694912845026

commandID 18 - Client5 consume 5,4,730

commandID 17 - Client38 consume 38,1,369679966408715949597201375173523842769632392360403443076663453778805214521057654526304192095570533377311555819994759414549495320564115086254732145192159557475752629624652280264082977098013735229932825407462240359114758145364271330945830833347957766168803676169291901843503255534982365548284160215245608407372084330424374173207480738

commandID 16 - Client97 consume 97,1,28100540815296576419671685295268436928188083893679411346115080157374568519038063744590925228969038924382057723310321896607881923784338952776670571445831732536102312217263219237922084389622132573685270843850015657703713293741618057514763349786114755113252804664406238355873107642254212009592382447215317058464256427160755040160077721176

commandID 17 - Client74 consume 74,5,741

commandID 17 - Client41 consume 41,5,745

commandID 17 - Client60 consume 60,1,1565989467627159565957816458504373032771150254366929269047554490721138097703166654499159321210985693580753617648843697585275526029534264260685390756768417312129370688162624124521514851837063390162678796601186276251014956538832998364175295673689583767563387644689945338147005565889075443964661518020843213225574593519944561380426341072

commandID 18 - Client23 consume 23,1,4863368234164250284380782080624877696022074288749672288657670185372215437682000591806629705678387706379480990720181102120279421466206221310928411227606167945112365789645937851778130173088696210376112882885390410991674693933436362880206451669964884423725275540053815766860221595552227922786270856650819503978018048502602996309185088

commandID 17 - Client62 consume 62,1,87269601992295767569010957810277661692620684925315496740900675605917239618936036393942552828703560071507394368864659927077544747277803915666462175999779082226360169664015003465182943445008449242947494971337314814555923957375913040391972340297752702888172940012777730772992543749145981771524877159860779596086256198247064687596418120

commandID 17 - Client49 consume 49,5,743

commandID 17 - Client79 consume 79,3,3889

commandID 17 - Client98 consume 98,4,728

localWorker reprocess - coomandId 18 7,4,-1

commandID 17 - Client50 consume 50,4,722

commandID 17 - Client32 consume 32,4,726

commandID 17 - Client58 consume 58,4,724

commandID 16 - Client92 consume 92,1,20601558439532879321157543932413195999149652659141456113060751355136256045313508950533980780756293091281978344536119706239316331452899423588883441193043228570311950968592266403351203317979938258142845522112981100891062315860619169377941472156946954616111916118180978751533080538398438462184651575802490023027059537436115422821808258

commandID 16 - Client73 consume 73,2,2641657

localWorker reprocess - coomandId 18 10,5,-1

commandID 18 - Client7 consume 7,4,734

commandID 17 - Client27 consume 27,5,747

commandID 18 - Client10 consume 10,5,751

commandID 17 - Client59 consume 59,2,2662913

commandID 17 - Client55 consume 55,3,3911

commandID 17 - Client69 consume 69,1,119035801098103659892115208390264763686606568984545765903727202046161631681485979250886642392815669005228556919656657136187179294576056983235678580955592758950402484251328025730038479942934797590621731387212269976178027759267168494846700412672565313289433572912554026677923956088108132179776460021159886695166696167053222780335223729730

commandID 17 - Client48 consume 48,3,3907

commandID 17 - Client12 consume 12,5,753

commandID 17 - Client83 consume 83,2,2654372

commandID 17 - Client76 consume 76,1,504243745207711215988132518856327491674614359831862474961023888342021095244981980748137494800231714945296285401936950441356599102088566885719384895268202768337712249222575322158076004161361322936172196392699095562415824330810292036901565000476376008270987096314622345067568931994686740728698222531854863839131041095373646161500972640096

commandID 17 - Client67 consume 67,2,2650177

localWorker reprocess - coomandId 18 4,4,-1

commandID 17 - Client24 consume 24,2,2678098

commandID 17 - Client9 consume 9,5,755

commandID 17 - Client40 consume 40,2,2672699

commandID 17 - Client18 consume 18,1,9048286872923505311366713654118626413214870393079845137952314909999005145890637589721883981175201830090951079511554786047810901913809864990172257543381818097542718173789092579607445990514881680277414264224733704465781124660843638606713406658788653393764514928998795972860367668262107121106975623126172232045894483289564875866857429800552

commandID 17 - Client37 consume 37,1,2136010781928948523844645283815574730385064008311995665747822755414246012661413902243436621593742528786413698527404458901613575702930324526113218162028403832301251481141629314362342496588380089335310516958008652225841325082508336642452960414578069346373381958171043406948199684066855095094569350148579342051690860548547807426339114290114

commandID 16 - Client87 consume 87,4,736

commandID 17 - Client16 consume 16,2,2681935

commandID 17 - Client3 consume 3,3,3917

networkWorker reprocess - coomandId 17 84,3,-1

commandID 17 - Client71 consume 71,3,3967

commandID 18 - Client4 consume 4,4,740

commandID 18 - Client26 consume 26,5,763

commandID 17 - Client90 consume 90,5,761

commandID 18 - Client64 consume 64,1,162364919967415384388612713255278947946193052715605350008180644491640071530786494634245774166353401226691823145806049198419239635346489002937381250885604522987431214878981091110775951825106508922057284559652507584821644419564375202883939754857719385079490281625663705166419049096723241439196862993739245312986969658116794119441932763769840

commandID 18 - Client44 consume 44,4,742

commandID 17 - Client86 consume 86,2,2687518

commandID 17 - Client96 consume 96,1,2913520272540553413683662124940902436618260078487816455009299285939522282408266265826702051013186020250361865544997330785498502534322992187882690258397499595676219149647870547414359686861402278916753707809520402822323818427497910013304202180780160278037060554332947897022682516072756238784436558264180243401719559362812729274087932318056568

commandID 17 - Client77 consume 77,3,3929

commandID 17 - Client88 consume 88,3,3923

commandID 18 - Client15 consume 15,1,38329158273622969769311499900290080383244545580631376217557082395410266596223964261130972546294549849150218016573623603092857183358169784486802248335555676222472124176297999632792126458647906810444967573856943470088965823725882891069306587049732682921431441674166227298389670357115283579522471842653268270235268793706807310893768833492322

commandID 18 - Client1 consume 1,1,12341869928305498162058410852685015618641057070394318596287476804120059682352435006104922273264452235757364972779787143538763825862036094547767088285467972150877073582283704553733334681204683058165688937050548585098670817211975023755821874329601251335387634785508612636054795931035033204474056156874331223129061384877424900885013229160797954

commandID 18 - Client57 consume 57,3,3919

commandID 17 - Client33 consume 33,5,759

commandID 18 - Client42 consume 42,5,757

commandID 17 - Client81 consume 81,3,3931

commandID 17 - Client28 consume 28,1,687788838143284507323762352921405872168016756443052776250279660361970552719369942798114069211708154755917510599797820396769815724744125796236327251877973768172196983692222364075895933759073942498674105812466973809375543501983383702605065606480610223239392568176821047964065866744008249336309923817610249522183147426173983788661499888571682

commandID 17 - Client84 consume 84,3,3947

localWorker reprocess - coomandId 17 53,5,-1

commandID 17 - Client99 consume 99,3,4003

commandID 18 - Client36 consume 36,5,765

commandID 18 - Client19 consume 19,5,769

commandID 17 - Client53 consume 53,5,773

commandID 18 - Client34 consume 34,2,2709570

commandID 18 - Client54 consume 54,2,2698234

commandID 18 - Client39 consume 39,4,744

commandID 18 - Client13 consume 13,2,2706426

commandID 18 - Client91 consume 91,2,2695104

commandID 17 - Client80 consume 80,5,767

commandID 17 - Client75 consume 75,5,775

networkWorker reprocess - coomandId 17 63,1,-1

networkWorker reprocess - coomandId 17 63,1,-1

commandID 16 - Client89 consume 89,2,2697090

commandID 17 - Client66 consume 66,1,52280999985762546061917305535680964911182488360065090840159206502419761011818006290246391144070994963279821756664145904940553805982467370378951043400269388199184513478782688762347698411680134511579509456011714743217007087275398005036591699499185165619587599696367398441241866240212889056680661185761505135917965098872512332814140848961248384

commandID 17 - Client52 consume 52,4,746

commandID 17 - Client43 consume 43,3,4001

commandID 17 - Client30 consume 30,1,221465869871355682409727632995408875263371010510654681956924302813799103729624460167090486849548432088876651999436370763300979049791905576063571261886545524947615127497414459603124128327925221104483726761097407557966699166313567043902188672326341913813738033570978206401022260891886589431196700899920351766800921780367474232141576625005791490

localWorker reprocess - coomandId 17 70,5,-1

commandID 17 - Client85 consume 85,3,3989

networkWorker reprocess - coomandId 18 61,3,-1

commandID 18 - Client95 consume 95,5,789

commandID 18 - Client56 consume 56,4,748

commandID 18 - Client11 consume 11,2,2718613

commandID 17 - Client68 consume 68,1,71311322309738386451424974062168736428933297367674364537353375226382949430386356803860338651485383424463526026307911587962518484217268851366673710000215781059363898857183763769841893425658162121701261342967552766691467756009466604752802147423475615637800407417286805640081203941320737788596001379850535658660374630130900528765799011751976288098

commandID 18 - Client2 consume 2,3,4049

commandID 18 - Client6 consume 6,2,2726484

commandID 18 - Client45 consume 45,2,2735127

commandID 17 - Client63 consume 63,1,16834319630495572416552983769776015422452815058888243645181256313134671405733867238964703702616694024774875913822709175341659906286719146773019298593635857395614425908953146800384848112609178206219679987551212494808291460458258593246579643298982765610122127611948676634374714510299453553009733704952210914520271774867290854372033911432758209808

commandID 18 - Client14 consume 14,5,777

commandID 17 - Client70 consume 70,5,781

commandID 18 - Client61 consume 61,3,4019

commandID 18 - Client8 consume 8,5,783

commandID 17 - Client82 consume 82,3,4007

localWorker reprocess - coomandId 18 51,4,-1

commandID 18 - Client20 consume 20,3,4027

commandID 17 - Client93 consume 93,5,785

commandID 17 - Client47 consume 47,5,787

commandID 18 - Client17 consume 17,4,750

commandID 18 - Client21 consume 21,3,4021

commandID 19 - Client22 consume 22,4,752

commandID 18 - Client72 consume 72,1,302079608869449118222252880018450961138186004529585701794594757218666469127279294454406058308558227722628980019054355527191733843155794552239714138594498981633070021337688201879752421815241826693024725359421423561574162484496125012257788232992885228161323757281095899194699530275582404707393739224354353549161770295390892969435229958440663362200

commandID 18 - Client25 consume 25,3,4073

commandID 19 - Client29 consume 29,4,760

commandID 18 - Client78 consume 78,1,1279629757787534859340436494135972580981677315486017171715732404101048825939503534621484571885718294314979446102525333696729453856840447060325530264378211707591643984207936571288851580686625468893800162780653247012988117693993966653783955079395016528283095436541670402418879325043650356618170958277267949855307455811694472406506718845514629736898

commandID 18 - Client51 consume 51,4,756

commandID 17 - Client97 consume 97,5,793

commandID 18 - Client65 consume 65,3,4057

commandID 18 - Client35 consume 35,5,791

commandID 17 - Client100 consume 100,3,4051

commandID 18 - Client41 consume 41,1,5420598640019588555583998856562341285064895266473654388657524373622861772885293432940344345851431404982546764429155690314109549270517582793541835196107345811999645958169434487035158744561743702268225376482034411613526633260471991627393608550572951341293705503447777508870216830450183831180077572333426152970391593542168782595462105340499182309792

commandID 18 - Client74 consume 74,5,795

commandID 18 - Client38 consume 38,3,4091

commandID 19 - Client5 consume 5,2,2744603

networkWorker reprocess - coomandId 18 60,2,-1

networkWorker reprocess - coomandId 19 23,1,-1

commandID 18 - Client46 consume 46,2,2744110

commandID 17 - Client94 consume 94,3,4079

commandID 18 - Client31 consume 31,4,758

localWorker reprocess - coomandId 18 49,5,-1

commandID 19 - Client10 consume 10,3,4129

commandID 18 - Client49 consume 49,5,799

commandID 18 - Client76 consume 76,3,4139

commandID 18 - Client48 consume 48,1,31320218003888703202979069692389370427788498885018244654979357162936477566115049525213462671721232115724431886085231824225296017540580588230995049129872153155282466356657968997648620051233543388324787295798778329901611911895203415093414089559864124045442209524381524465971888801010252368799241431475071526420010424364999005494170783276956926401563832

commandID 19 - Client7 consume 7,3,4127

commandID 18 - Client27 consume 27,4,764

commandID 18 - Client12 consume 12,2,2754390

commandID 18 - Client69 consume 69,4,768

commandID 18 - Client59 consume 59,4,766

commandID 18 - Client79 consume 79,1,1745415927766677019325631078829304117775473822989457824904077667050248356197658751539551914660458295710355283270274309571967933205200374940373697913847971715606490384104648951678520730900768862952162351547227529327060767618411523045427495373641191349130963049982572409179575444690448894186431968557658269045551572848803480704884425885729303945543216

commandID 18 - Client60 consume 60,2,2750625

commandID 18 - Client98 consume 98,3,4093

commandID 18 - Client55 consume 55,2,2753909

commandID 18 - Client62 consume 62,1,412036807963798468610835338072800106401360973549365407902509205770563877688712687260270030623360272162098017622642140375460288263043553561160546148614158497493032456719734204778441906480118179534506829873977582835394715595551880830286667051990967777553959418669448377479776560318155291907474491498720238161408421483834958377783885804889689831832290

commandID 18 - Client83 consume 83,3,4133

commandID 19 - Client23 consume 23,1,97268695911483144882289726538103692170029928791996193294040843967992845442808002498471792167017207061963212779705748070126780153026160695731513319391337725634360557225712132564753104980296144814135032051317197985481905236203999724280827165677320238915125375304778899260469203417827726556534002562777316399917886913463647193748882666170544618214056

commandID 17 - Client92 consume 92,3,4111

commandID 18 - Client50 consume 50,3,4099

commandID 18 - Client58 consume 58,5,801

commandID 18 - Client32 consume 32,1,7393700519030506545913359653390016577503256265507196707518819873971557302479347693418477689265193455003519150703739378663332021083845053322655337804006045359918993993138330011492524830083193631343156236062887700143637786069197973011996648546555733174077811618599738014198078339079950868653202365729353314343614712879048881197321589347806905614005154

commandID 17 - Client73 consume 73,4,762

networkWorker reprocess - coomandId 18 77,2,-1

networkWorker reprocess - coomandId 18 77,2,-1

localWorker reprocess - coomandId 19 57,5,-1

commandID 19 - Client4 consume 4,2,2762218

commandID 19 - Client64 consume 64,1,2380748605103505241895020131959664952618327276234935959186333653588942858902432476603483433081437001067218921360100260781497957621347168259116784280018897798298960475562725378026073565800229415692218341310581130655357900019631011427929757473601664395231161883271665307791343325437097335320649823283604156246082200673223759375934763414853616096350683522

commandID 18 - Client71 consume 71,3,4177

commandID 18 - Client86 consume 86,1,10085012928556250948214378151222839174055726611047082782710058965621577783443603139116236508502069724056205104106664941652475190867913923249684674306499441978275319806286640305110290903392220674895767374215155305030333553725019295660355056011110570622393476341475546099143687724721883435026799206932410254190843438758616216121929243332513959756685339848

commandID 18 - Client88 consume 88,4,774

commandID 19 - Client15 consume 15,1,42720800319328509034752532736851021648841233720423267090026569516075253992676845033068429467089715897292039337786760027391398721093002861257855481506016665711400239700709286598467237179369112115275287838171202350776692114919708194069349981518043946884805067249173849704366094224324631075427846651013245173009455955707688623863651736744909455123092042914

commandID 18 - Client67 consume 67,5,803

commandID 18 - Client90 consume 90,4,772

commandID 18 - Client77 consume 77,2,2780669

commandID 19 - Client44 consume 44,3,4201

commandID 19 - Client1 consume 1,4,776

commandID 19 - Client57 consume 57,5,809

commandID 18 - Client96 consume 96,3,4211

commandID 18 - Client33 consume 33,1,180968214205870287087224509098626925769420661492740151142816337029922593754150983271389954376860933313224362455253705051218070075239925368281106600330566104823876278609123786698979239620868669135996918726899964708137102013403852071937754982083286358161613745338170944916608064622020407736738185810985390946228667261589370711576536190312151780249053511504

commandID 19 - Client26 consume 26,2,2769348

commandID 18 - Client37 consume 37,3,4157

commandID 18 - Client40 consume 40,3,4153

commandID 17 - Client87 consume 87,1,562018508142229980634297623384179363582417506107338945964724351265806347833873232702302776176321719787329418666263898526483360382525250213217537186423850785079477904035738793005996640191303012126894008972830782408901953646495249948636026116703913041468828808388884867978314422973494093744199913797993629206514636065721178618190189673099495371282605760

commandID 18 - Client9 consume 9,1,132674572534585319357829638422947498288657251805580175327436248525717467566939545794272328376150121917901246695044666675564516091246167406246635534323494657981048859419770206002087005035017367184642305419258001019750085433650011633385653006786012229355846649716125835878085633543120960343850168091629639420023656410339044903174004722455634611220260482

commandID 18 - Client18 consume 18,5,805

commandID 18 - Client24 consume 24,4,770

commandID 18 - Client3 consume 3,3,4159

commandID 18 - Client84 consume 84,1,766593657142809657383650569131358724726523879691383871661291917635765629009280778118628246974533449150189489158801580232263679022052704334382281882828281085006905354137204433394384195662843788659262962745771061183325100168535116481820369909851189379531260048601857629370798352712406262022380589894954808957924125002065171470169796497993516576119306088930

commandID 18 - Client16 consume 16,2,2757822

commandID 18 - Client99 consume 99,1,3247342842777108916621826785624061824675516180258275637787984007572985109791274095745902942274994729913982319090460025980272786163450742705810234131643690444851497695157941520276516022272243823773048769709984209441437502687544317999219234621488043876286653939745601462399801475471645455826260545390804626777925167269850056592255722182286218084726277867224

commandID 19 - Client91 consume 91,5,817

commandID 18 - Client80 consume 80,4,782

commandID 19 - Client42 consume 42,5,811

commandID 19 - Client36 consume 36,4,784

commandID 18 - Client75 consume 75,4,786

commandID 19 - Client19 consume 19,1,13755965028251245323870957711627606023428588600724486422813227947927706068174377161102240016074512368806118765520641684153354823675855675157623218409403042864412896134768970514500448284751819083751458041585707898949075110918712388478697308395803364884677875807584263478970004254598988085327422771458173316069624794081465397839192685227138388915024417557826

commandID 19 - Client34 consume 34,2,2784299

commandID 19 - Client39 consume 39,5,813

commandID 19 - Client54 consume 54,4,780

commandID 19 - Client13 consume 13,5,815

commandID 18 - Client81 consume 81,3,4217

commandID 18 - Client28 consume 28,4,778

commandID 19 - Client2 consume 2,5,823

commandID 19 - Client11 consume 11,3,4229

commandID 19 - Client6 consume 6,2,2798127

commandID 19 - Client45 consume 45,1,246840776851379606172293588240165549696988070933349371738976811145062943598129508121721692042366689189359948290212748734528123147143349448502835649486426490474425225071704264827613684929869899718866981785796971119900026896368287876134731181214609378544670504487914884992089278230069379273871229296352164880295322168464311989635298537590497483894320209951938

commandID 18 - Client63 consume 63,2,2804652

commandID 18 - Client53 consume 53,4,788

commandID 19 - Client95 consume 95,4,794

commandID 19 - Client56 consume 56,2,2803189

commandID 18 - Client70 consume 70,2,2804937

commandID 17 - Client89 consume 89,5,819

commandID 18 - Client68 consume 68,2,2790979

commandID 19 - Client14 consume 14,1,1045634310361300514901280010592796684706342154316553708284948140379535583775006815227041631176039800962578250542024021700706184669440271237347645705714961823800203982521050882888733048880759119034246808079240700284837845531835545376452933193063139017593680175121742195346636931414145114892620868816632157412237713017452959606490220613452829709322104787906280

commandID 19 - Client61 consume 61,2,2806618

commandID 18 - Client66 consume 66,3,4219

commandID 18 - Client82 consume 82,3,4231

commandID 19 - Client8 consume 8,2,2815810

networkWorker reprocess - coomandId 20 22,2,-1

commandID 18 - Client30 consume 30,4,792

commandID 18 - Client85 consume 85,5,821

commandID 19 - Client20 consume 20,2,2825523

commandID 18 - Client52 consume 52,4,790

commandID 19 - Client21 consume 21,2,2831387

commandID 18 - Client93 consume 93,4,796

commandID 19 - Client17 consume 17,3,4243

commandID 18 - Client47 consume 47,3,4241

commandID 18 - Client43 consume 43,1,58271202955782090212105657632134485918389870583156221329040895799283809382488782740154863006573044205138457381173026762593692080866873443336303107769255861902503082234233823578278309161279520158778880936052815805237737946362393871914008468204701503414998157170082655378279818493867597797135951631223497891056424343595711647949026463090839773744823948098528

networkWorker reprocess - coomandId 19 31,3,-1

commandID 19 - Client51 consume 51,1,4429378018296581665777413630611352288522356688199564204878769372663205278698156769029888216746525893039672950458308835537352861824904434397893418472346273785675241155155907796382545880452906375855854214102759772259251409023710469381946463953467165448919391204974883666378637003886649838844354704562880794529246174238276150415596180991401816321182739361577058

commandID 19 - Client35 consume 35,1,18763146383547627178010934533038205838795768907114810527800025631032356698567633891346594498162143373121270052375259363850117631969058008828921319595100056966501168603144682068418916570692384622457663664490279789321843481626677422904238789006931800813271244995021276860861184946960744470270039687068155335529222409970557561268874944579060094994053062234214512

commandID 19 - Client74 consume 74,3,4259

commandID 19 - Client78 consume 78,2,2849472

commandID 19 - Client49 consume 49,5,829

commandID 20 - Client10 consume 10,1,25592945423123991722052238574021824391723452217893202284405657773956082464552320802173915997733179635449781157558619292933244603360618799898323837550934887105350458387585481110293916702872697376851336989822783218342893458013898923553770186902763372112666862851084829222478135237658710056436419460583735335835175436824634595036143263598523196710277291911886152194

commandID 18 - Client100 consume 100,1,79481963552487090377821151762764175643705432316658806316078871896792632072968692334416266209395099385524753159959346290937823389701136469713578696852746501651679915567734636070058212163222444865686508872063878929546625335530420160998901619981194368702004371185059991109823376791729627719924513452835502136646135814120506395491095959307642196297394988298435106

commandID 20 - Client22 consume 22,2,2844929

(Client 22 Ended, consumption count = 20)

commandID 19 - Client46 consume 46,5,827

commandID 18 - Client94 consume 94,1,6041674864299380169229308813980670145606319198220385833730279212296619573129395624230863273545203593100843818407452342132975283801955811969469328606514449087351473787130353392207312857453786568911208921084934039345828363345843767781292976051763835160369925890239681066541923273102865851829155643284314794542518559586175939016929783127994259748311341215468974336

commandID 19 - Client31 consume 31,3,4271

commandID 19 - Client65 consume 65,2,2857026

(Client 10 Ended, consumption count = 20)

commandID 18 - Client97 consume 97,2,2849960

commandID 20 - Client29 consume 29,3,4253

commandID 20 - Client5 consume 5,1,1426245965926471045135003318099143809298175425011658949484540924769604172034738305250462903552365263046405883928809924401343468152795552020446523124877090755944563239064067541464665273057551101206501305483047060959580004630523852428598282695708031471187159290126104956310442145247246649119796887446476157665101198479930838968424131086546157717031927050010254850

commandID 19 - Client38 consume 38,5,825

commandID 19 - Client41 consume 41,1,336691000593495988689295541584094908413617498173750035792115513218202884990442403229011659335742540915220282692212644527601411190773603887683236107006086063573220830874083226348651765223582164085203699152745795507508344823748358066899845268931709275621288729735261241300154692113879255349968093498410163882113765666452583143233258781809628880183633015427954936

commandID 19 - Client76 consume 76,4,802

commandID 19 - Client25 consume 25,4,800

(Client 5 Ended, consumption count = 20)

(Client 29 Ended, consumption count = 20)

commandID 19 - Client72 consume 72,4,798

localWorker reprocess - coomandId 19 12,5,-1

networkWorker reprocess - coomandId 19 55,2,-1

commandID 19 - Client48 consume 48,5,831

commandID 19 - Client27 consume 27,3,4273

commandID 19 - Client12 consume 12,5,835

commandID 20 - Client7 consume 7,2,2862999

(Client 7 Ended, consumption count = 20)

commandID 19 - Client59 consume 59,3,4289

commandID 19 - Client98 consume 98,5,839

commandID 19 - Client83 consume 83,5,841

commandID 18 - Client92 consume 92,5,843

commandID 19 - Client69 consume 69,3,4283

commandID 20 - Client23 consume 23,3,4327

(Client 23 Ended, consumption count = 20)

commandID 19 - Client60 consume 60,3,4297

commandID 19 - Client62 consume 62,1,108413456556795347057438263110067967712500128069793194971352910308120949431338678832926527264477922134899968448641929513865953697244431011562764678810253997508753307337472277833382979668944576076316556880376066912717402195401439461996373723662817323611037377294578997956454464223737706077574833485619256137883220306884714319161502837522087046589420508863013583112

commandID 19 - Client55 consume 55,2,2869688

commandID 19 - Client79 consume 79,5,837

localWorker reprocess - coomandId 19 58,4,-1

localWorker reprocess - coomandId 20 4,4,-1

localWorker reprocess - coomandId 20 4,4,-1

commandID 20 - Client4 consume 4,4,812

(Client 4 Ended, consumption count = 20)

commandID 19 - Client71 consume 71,2,2872051

commandID 19 - Client86 consume 86,5,849

commandID 19 - Client50 consume 50,5,845

commandID 19 - Client58 consume 58,4,806

commandID 19 - Client32 consume 32,5,847

commandID 20 - Client64 consume 64,4,814

(Client 64 Ended, consumption count = 20)

networkWorker reprocess - coomandId 19 33,3,-1

commandID 18 - Client73 consume 73,3,4337

commandID 19 - Client88 consume 88,5,851

commandID 20 - Client15 consume 15,1,459246771650305379951805291014293695241723964497065982169817299006439880189907036133880025055644868175049654952126337348397059392338342846149382552791950877140363687737474592443825835378651001682117564511327050869212502239619656771539265081554032666556816372029400821048295992132609534366735753403060759887368056664363491871682154613686871383067959327363940484642

(Client 15 Ended, consumption count = 20)

commandID 19 - Client67 consume 67,3,4339

commandID 19 - Client90 consume 90,3,4349

commandID 19 - Client77 consume 77,3,4357

commandID 20 - Client57 consume 57,1,1945400543158016866864659427167242748679395986058057123650622106333880470190966823368446627487057394835098588257147278907454191266597802396160294889978057506070208058287370647608686321183548582804786814925684270389567411153880066548153434049878947989838302865412182282149638432754175843544517847097862295687355446964338681805890121292269572578861257818318775521680

commandID 20 - Client26 consume 26,4,818

(Client 57 Ended, consumption count = 20)

(Client 26 Ended, consumption count = 20)

commandID 18 - Client87 consume 87,4,822

commandID 19 - Client18 consume 18,1,34908796320287508256506431425900301508516627620975235030739845003701727514006064141799112767502555184896874620180009090820309489101516012119322543340794781111754991741835199379122970801634929914409846111781940800099495998574439758404765439174158246493478414200124702080737037325351427477723746414275902066234514825051211558186860680423330219372913220220874945807128

commandID 19 - Client96 consume 96,1,8240848944282372847410442999683264689959307908729294476772305724341961760953774329607666535003874447515444007980715452978213824458729552430790562112704180901421195920886957182878571120112845332901264824214064132427482146855139922964153001281069824625910027833678129949646849723149312908544807141794509942636789844521718219095242639782765161698512990600639042571362

commandID 19 - Client37 consume 37,3,4391

commandID 19 - Client40 consume 40,4,820

commandID 19 - Client33 consume 33,3,4373

commandID 20 - Client1 consume 1,2,2879149

commandID 19 - Client24 consume 24,5,853

(Client 1 Ended, consumption count = 20)

commandID 20 - Client44 consume 44,4,816

(Client 44 Ended, consumption count = 20)

commandID 19 - Client9 consume 9,2,2888251

networkWorker reprocess - coomandId 20 19,3,-1

commandID 19 - Client84 consume 84,1,147876034225432405873436168703284470724025818392630234599731685739148871816978030896804117605014095187102942488700751816259451780864793600908080735475883305348441162888227754699370454326652564990540649271341827332825466141152898956583214757977702810599823684634176938272594999024555022819439792798898118207574849144726564451842685361476086039190165871484138825799874

commandID 19 - Client3 consume 3,5,855

commandID 19 - Client16 consume 16,5,857

commandID 19 - Client63 consume 63,5,865

commandID 20 - Client91 consume 91,5,859

commandID 20 - Client36 consume 36,3,4397

commandID 20 - Client19 consume 19,3,4423

(Client 19 Ended, consumption count = 20)

commandID 20 - Client45 consume 45,4,830

(Client 45 Ended, consumption count = 20)

commandID 19 - Client99 consume 99,5,861

commandID 20 - Client34 consume 34,5,863

commandID 19 - Client81 consume 81,4,826

(Client 34 Ended, consumption count = 20)

commandID 20 - Client2 consume 2,2,2897465

(Client 2 Ended, consumption count = 20)

commandID 19 - Client75 consume 75,3,4409

commandID 20 - Client54 consume 54,1,2653527767113500932874440593659437208342505423158614928318398037580337730944650781812866450355249838920337520788632817239691918231107555263914662676453195315370519736067212627405789606759633324496830422059938827858430908393897041295533712642317580766170916295581506758957063132718841097841371463238371617793710494760556441914073093866786783543724472696113859821826370

(Client 54 Ended, consumption count = 20)

commandID 20 - Client11 consume 11,3,4441

commandID 20 - Client6 consume 6,4,828

commandID 19 - Client28 consume 28,1,11240524001676020863248013480876787017774641593825955886703258738281648138560521314980481384608558291614658727729514285314625789536990911471410296191057109263987598587563596727799763215146778487863894131436904561565124994138774200766872475040355292553576438335062859490999369564298935910120968770563354846071375890446183237021849977593474808551031467490612869536312104

commandID 20 - Client42 consume 42,2,2893130

(Client 42 Ended, consumption count = 20)

(Client 11 Ended, consumption count = 20)

commandID 20 - Client13 consume 13,2,2896706

(Client 13 Ended, consumption count = 20)

commandID 20 - Client39 consume 39,1,626412933222017131750251106239038184404619901191496173429666587960297214781918187729015583187558935933308644574983016355858116612560690415751645485244328002505519643294746218176604788108245189876572443197149250131401360563186035584737624471084969488892773152736832455171117033423571518755482917609868374896533911403957469365557602126327674376133576706157430249006624

(Client 39 Ended, consumption count = 20)

(Client 36 Ended, consumption count = 20)

(Client 91 Ended, consumption count = 20)

commandID 19 - Client53 consume 53,3,4447

(Client 6 Ended, consumption count = 20)

commandID 19 - Client80 consume 80,4,824

networkWorker reprocess - coomandId 19 30,3,-1

commandID 20 - Client74 consume 74,1,64947745736283505389830155027846648221459130449730387055314305339169256610722221966990398071821622321892103293722545283400628904490542219884006295231633087349262838568734403605856384248431764919328997486655619631039021826566426178150922612629738830495616670861690336726812075192880818934503114011797217202738114207642599779173567364644976151538287240299503341862035815232

(Client 74 Ended, consumption count = 20)

commandID 19 - Client70 consume 70,5,869

commandID 18 - Client89 consume 89,4,832

commandID 19 - Client68 consume 68,4,834

commandID 20 - Client14 consume 14,1,47615623773817584385866494517166585279441071798462438475131432990706930285186736041734791988789483005378972431706689958498195076379071201149555847440681632371320914086321599538604842467346747275952406947807557074118930884948993844363023612803738750980476669635832944722954541389914584738325246545491791002079214056545289390001473004240686017747850342658565337967074786

(Client 14 Ended, consumption count = 20)

commandID 20 - Client95 consume 95,5,867

(Client 95 Ended, consumption count = 20)

commandID 19 - Client66 consume 66,2,2910981

commandID 20 - Client8 consume 8,4,836

(Client 8 Ended, consumption count = 20)

commandID 20 - Client78 consume 78,1,275123065924269058299163757909745530061345822892208060780014943047998918845847199895369298503559097801205255194957246599487118173104448941512581255721696359137311340169570281247101596897758385647607674443167240560195050225753139577589627470627001222095369571245814920931101390504076747468730365995044426615503756181928837573710291923260044027781917581368199922134715542146

(Client 78 Ended, consumption count = 20)

commandID 20 - Client56 consume 56,2,2901996

commandID 20 - Client35 consume 35,1,15332082979135036739843137798358937175509301093286512558757721691321892402958312027407706216272608513636842020067065465884602555142280061976556074795164009740259985894632666823676059904031325970291684496544762036038962919487434864985937020108045900112902887799053574023853089732553471730717909947855557804551299351358438457016022464680139421628768620170186554686572281218

(Client 56 Ended, consumption count = 20)

commandID 20 - Client51 consume 51,3,4493

(Client 51 Ended, consumption count = 20)

commandID 19 - Client82 consume 82,3,4457

commandID 20 - Client61 consume 61,3,4451

(Client 61 Ended, consumption count = 20)

(Client 35 Ended, consumption count = 20)

commandID 19 - Client52 consume 52,5,871

commandID 20 - Client17 consume 17,1,3619413819743358430457603834410899519421926076584336820283418573881686998888973857359573206731188267344735213454283419862218683921421971977781996050977048388222894990203736311152144632306461038162259500476571486883170148616686718207174532197555230044005119665476040631399716262666932011631474220374985984532916802208845951109477505924418465023212759618757123115746690360

(Client 17 Ended, consumption count = 20)

commandID 19 - Client85 consume 85,1,201703019096946358406713991549543128135538928787675709787228990701109369279307465481919649339766490313130548454556274119307406095053275716069633685953783638749271254932849994882219133084533767591673521922667132858040848533934749578218966926255310296475483116878394638382817535123957274863421954952530518854388232116627340797027741994556218879542432838124874221404611248

commandID 19 - Client93 consume 93,1,854427700161603018012722460715339097821596786949165277624047395795144407402416597969413389347855444257901166249931786435727819456592174065428090591255816187368405933817721579067481374805481817642646494638476088506282325020687992157238891317824979936882409137149411498254224681885743684192013066355613866419632142523054652578112440982465561535917581695158062223585519778

commandID 20 - Client21 consume 21,2,2927595

commandID 20 - Client20 consume 20,2,2919773

(Client 20 Ended, consumption count = 20)

commandID 19 - Client47 consume 47,3,4483

commandID 19 - Client43 consume 43,4,838

(Client 21 Ended, consumption count = 20)

commandID 19 - Client30 consume 30,3,4481

localWorker reprocess - coomandId 20 59,5,-1

commandID 20 - Client48 consume 48,5,879

(Client 48 Ended, consumption count = 20)

commandID 19 - Client100 consume 100,1,1165440009433359738586485186666828768466842422018562630175374077531164931994111021548467592086058013526713124073551531681349101596908337985934331318118418523898508199247015528594262771839465307509759695259324581871819222729578984488509432495137743718877094955844950020451217637209187808809424577991974923664753138935357950074014735057685152262665957565772303030400897983816

commandID 19 - Client94 consume 94,3,4507

commandID 20 - Client65 consume 65,2,2936847

(Client 65 Ended, consumption count = 20)

commandID 20 - Client27 consume 27,2,2951555

(Client 27 Ended, consumption count = 20)

commandID 20 - Client12 consume 12,3,4517

(Client 12 Ended, consumption count = 20)

commandID 20 - Client83 consume 83,3,4519

(Client 83 Ended, consumption count = 20)

commandID 20 - Client41 consume 41,1,4936883103657708012645104504577060603928715510966458581481511253172658646822291286089239666847791151908057751489163373324883524560737800885249906528195370454731344137157632395624152684255619615686646455480465568047471941144069077531627357451177976097603749394625615002735971939340827982706428677962944121274516311923360637869769232154000653078445747844457412043738307477410

(Client 41 Ended, consumption count = 20)

commandID 20 - Client72 consume 72,2,2944025

commandID 19 - Client97 consume 97,4,840

commandID 20 - Client25 consume 25,3,4513

(Client 25 Ended, consumption count = 20)

commandID 20 - Client46 consume 46,5,875

commandID 20 - Client98 consume 98,2,2957780

commandID 20 - Client31 consume 31,2,2927904

commandID 20 - Client59 consume 59,5,883

commandID 20 - Client49 consume 49,5,873

(Client 59 Ended, consumption count = 20)

(Client 98 Ended, consumption count = 20)

(Client 31 Ended, consumption count = 20)

(Client 46 Ended, consumption count = 20)

(Client 72 Ended, consumption count = 20)

commandID 20 - Client38 consume 38,5,877

(Client 49 Ended, consumption count = 20)

commandID 20 - Client76 consume 76,4,842

(Client 76 Ended, consumption count = 20)

(Client 38 Ended, consumption count = 20)

networkWorker reprocess - coomandId 20 55,2,-1

networkWorker reprocess - coomandId 20 50,1,-1

networkWorker reprocess - coomandId 20 58,1,-1

networkWorker reprocess - coomandId 20 67,3,-1

networkWorker reprocess - coomandId 19 87,1,-1

networkWorker reprocess - coomandId 19 87,1,-1

networkWorker reprocess - coomandId 19 87,1,-1

commandID 19 - Client92 consume 92,4,844

commandID 20 - Client18 consume 18,5,889

commandID 20 - Client50 consume 50,1,375268063623722092466417772502884452546803837963900582579650169546461226415104859964749205078503949167334281216470138917974548495520563409498876902438079787646931417262548796471041460387675522557104459614002038791238906548775816318581830088902155922248380570662408430544660679465095807504623082639195550434065977820383051077881635211068694610113415123419062818665973404098392

commandID 20 - Client32 consume 32,4,846

commandID 20 - Client86 consume 86,1,20912972424064191789166903204975071184181704465884396956101419090221799519283276165905426259477222621158944130030205024980883199839859541526933957430899900342823884747877545111090873508861943770256345517181186854061706987305855294615018862299849648109292092534347410031395105394572499739635139289843751408762818386628800501553091663673687764576448948943601951205354127893456

commandID 20 - Client69 consume 69,5,885

(Client 69 Ended, consumption count = 20)

commandID 20 - Client96 consume 96,2,2999729

commandID 20 - Client79 consume 79,2,2977185

(Client 79 Ended, consumption count = 20)

commandID 20 - Client40 consume 40,3,4567

(Client 40 Ended, consumption count = 20)

commandID 20 - Client55 consume 55,2,2976325

(Client 55 Ended, consumption count = 20)

commandID 19 - Client87 consume 87,1,2168298806670120513987456499691511338968784354296287116414831624325147627057219270154098939953197746467235154976661168945511657806488910889542290858280929781390882379680168377275273952263604920903184648320706293480158770999804840122339636102754044289012312441670725049996712679137248382880893236986157878610816017108059061485400309075987552812259161044828104666748652466844693744

commandID 20 - Client77 consume 77,2,2993565

(Client 77 Ended, consumption count = 20)

commandID 20 - Client90 consume 90,4,848

(Client 90 Ended, consumption count = 20)

commandID 20 - Client71 consume 71,5,887

commandID 20 - Client88 consume 88,2,2983613

commandID 20 - Client62 consume 62,3,4523

(Client 88 Ended, consumption count = 20)

commandID 20 - Client67 consume 67,3,4561

commandID 20 - Client58 consume 58,1,6733912172802933472606353001846945074658287378884326089477601632746080275952604203199580265153593862390858117766432295498560989719530281829452850286454536277301941625978000791367655413469297462257623927534855511388238610890658838439857922737938956952361558179389004339772497124977152035343580348215676156404424782380266118900316342135562815217465023272599528784782167145877600

(Client 58 Ended, consumption count = 20)

(Client 96 Ended, consumption count = 20)

commandID 20 - Client60 consume 60,2,2962770

(Client 60 Ended, consumption count = 20)

(Client 86 Ended, consumption count = 20)

(Client 32 Ended, consumption count = 20)

(Client 18 Ended, consumption count = 20)

(Client 50 Ended, consumption count = 20)

(Client 67 Ended, consumption count = 20)

(Client 62 Ended, consumption count = 20)

(Client 71 Ended, consumption count = 20)

commandID 20 - Client37 consume 37,5,891

(Client 37 Ended, consumption count = 20)

commandID 20 - Client3 consume 3,1,9185061140586304914343078139623737658894507913513731752506030108576906669057998955455513570909168876092282380691165140223410514860963865153174573040630515677610303109178130237057877608124805994139014723803998397999125512726799799701043738241906188460997262960435996545064688814440774717036626301394860240279756677682994909540669360442139833957275370189573601719854919725284523810

(Client 3 Ended, consumption count = 20)

commandID 19 - Client73 consume 73,3,4547

commandID 20 - Client16 consume 16,3,4597

(Client 16 Ended, consumption count = 20)

commandID 20 - Client84 consume 84,3,4591

(Client 84 Ended, consumption count = 20)

localWorker reprocess - coomandId 20 70,5,-1

commandID 20 - Client63 consume 63,1,38908543369015340171359769058186461974546816008351214126438952058632774303289215091976153223589873250836364677741321729839153717250344371502240583020802992491832094816392689325506784384762828897459243543536699885476660821907004038926514589070378798133001364283414711230255467936900347251027398442565598839729842727840038699648077750844546888641360641803122511546168331367982788984

commandID 20 - Client81 consume 81,2,3012737

(Client 81 Ended, consumption count = 20)

commandID 20 - Client68 consume 68,2,3036396

(Client 68 Ended, consumption count = 20)

commandID 20 - Client66 consume 66,4,852

(Client 66 Ended, consumption count = 20)

commandID 20 - Client70 consume 70,5,899

(Client 70 Ended, consumption count = 20)

commandID 19 - Client89 consume 89,4,850

commandID 20 - Client33 consume 33,5,893

(Client 33 Ended, consumption count = 20)

(Client 63 Ended, consumption count = 20)

commandID 20 - Client99 consume 99,2,3007614

(Client 99 Ended, consumption count = 20)

commandID 20 - Client28 consume 28,3,4603

commandID 20 - Client9 consume 9,3,4583

commandID 20 - Client75 consume 75,2,3020368

commandID 20 - Client24 consume 24,5,895

(Client 75 Ended, consumption count = 20)

(Client 24 Ended, consumption count = 20)

commandID 20 - Client80 consume 80,2,3028075

commandID 20 - Client82 consume 82,2,3046141

(Client 82 Ended, consumption count = 20)

commandID 20 - Client53 consume 53,3,4621

(Client 53 Ended, consumption count = 20)

(Client 28 Ended, consumption count = 20)

(Client 80 Ended, consumption count = 20)

(Client 9 Ended, consumption count = 20)

localWorker reprocess - coomandId 20 43,4,-1

commandID 20 - Client85 consume 85,2,3049955

(Client 85 Ended, consumption count = 20)

commandID 20 - Client43 consume 43,4,856

(Client 43 Ended, consumption count = 20)

commandID 20 - Client30 consume 30,2,3055037

(Client 30 Ended, consumption count = 20)

commandID 20 - Client93 consume 93,5,903

(Client 93 Ended, consumption count = 20)

commandID 20 - Client47 consume 47,3,4637

(Client 47 Ended, consumption count = 20)

commandID 20 - Client52 consume 52,5,901

(Client 52 Ended, consumption count = 20)

commandID 20 - Client92 consume 92,3,4639

(Client 92 Ended, consumption count = 20)

commandID 20 - Client89 consume 89,2,3060415

(Client 89 Ended, consumption count = 20)

commandID 20 - Client94 consume 94,4,860

(Client 94 Ended, consumption count = 20)

commandID 20 - Client100 consume 100,4,858

(Client 100 Ended, consumption count = 20)

commandID 20 - Client87 consume 87,5,905

(Client 87 Ended, consumption count = 20)

commandID 20 - Client97 consume 97,2,3059886

(Client 97 Ended, consumption count = 20)

commandID 20 - Client73 consume 73,4,862

(Client 73 Ended, consumption count = 20)

executor.isTerminated() status = false
canceled non-finished tasks
executor.isTerminated() status = false
canceled non-finished tasks
Interruption occured in Rumtime

(Rumtime Ended, 
mSharedRequestQue = []
mSharedResultQue = {})

------------------------------------------------------------------------
BUILD SUCCESS
------------------------------------------------------------------------
Total time: 30.469s
Finished at: Fri Apr 28 17:41:51 PDT 2017
Final Memory: 7M/245M
------------------------------------------------------------------------
*/