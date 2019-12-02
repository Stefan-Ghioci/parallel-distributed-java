import structure.MyConcurrentLinkedQueue;
import structure.Term;
import structure.TermSortedLinkedList;
import utils.Utils;
import worker.ReadOnlyThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

public class Main
{
    public static void main(String[] args) throws InterruptedException
    {
        int threadCount = Integer.parseInt(args[0]);
        int polynomialsCount = Integer.parseInt(args[1]);
        int maxTermsCount = Integer.parseInt(args[2]);
        int maxDegree = Integer.parseInt(args[3]);
        int minCoefficient = Integer.parseInt(args[4]);
        int maxCoefficient = Integer.parseInt(args[5]);

        Queue<String> filenames = Utils.createRandomPolynomialFiles(polynomialsCount,
                                                                    maxTermsCount,
                                                                    maxDegree,
                                                                    minCoefficient,
                                                                    maxCoefficient);


        while (threadCount != 1)
        {
            System.out.println("Thread count: " + threadCount);

            runListSyncAllReadAndInsert(threadCount, new ConcurrentLinkedQueue<>(filenames));

            runNodeSyncAllReadAndInsert(threadCount, new ConcurrentLinkedQueue<>(filenames));

            runListSyncFirstReadsRestInsert(threadCount, new ArrayList<>(filenames));

            runNodeSyncFirstReadsRestInsert(threadCount, new ArrayList<>(filenames));

            threadCount /= 2;
        }
    }

    private static void runNodeSyncFirstReadsRestInsert(int threadCount, ArrayList<String> filenames)throws
            InterruptedException
    {
        TermSortedLinkedList linkedList = new TermSortedLinkedList();
        MyConcurrentLinkedQueue<Term> termQueue = new MyConcurrentLinkedQueue<>();

        Thread readThread = new ReadOnlyThread(filenames, termQueue);
        Thread[] insertThreads = IntStream.range(0, threadCount - 1)
                .mapToObj(i -> new worker.node_sync.InsertOnlyThread(readThread, linkedList, termQueue))
                .toArray(Thread[]::new);

        long startTime = System.nanoTime();

        readThread.start();

        for (Thread insertThread : insertThreads)
            insertThread.start();

        for (Thread insertThread : insertThreads)
            insertThread.join();

        long endTime = System.nanoTime();
        double elapsedTime = Utils.getElapsedTimeSeconds(startTime, endTime);


        System.out.println("Node Sync (First Reads, Rest Insert) - Elapsed time\t: " + elapsedTime + "s");

        linkedList.writePolynomialToFile("NodeSync_FirstReadsRestInsert_result.txt");
    }

    private static void runListSyncFirstReadsRestInsert(int threadCount, List<String> filenames) throws
            InterruptedException
    {
        TermSortedLinkedList linkedList = new TermSortedLinkedList();
        MyConcurrentLinkedQueue<Term> termQueue = new MyConcurrentLinkedQueue<>();

        Thread readThread = new ReadOnlyThread(filenames, termQueue);
        Thread[] insertThreads = IntStream.range(0, threadCount - 1)
                .mapToObj(i -> new worker.list_sync.InsertOnlyThread(readThread, linkedList, termQueue))
                .toArray(Thread[]::new);

        long startTime = System.nanoTime();

        readThread.start();

        for (Thread insertThread : insertThreads)
            insertThread.start();

        for (Thread insertThread : insertThreads)
            insertThread.join();

        long endTime = System.nanoTime();
        double elapsedTime = Utils.getElapsedTimeSeconds(startTime, endTime);


        System.out.println("List Sync (First Reads, Rest Insert) - Elapsed time\t: " + elapsedTime + "s");

        linkedList.writePolynomialToFile("ListSync_FirstReadsRestInsert_result.txt");
    }

    private static void runNodeSyncAllReadAndInsert(int threadCount, Queue<String> filenames) throws
            InterruptedException
    {

        TermSortedLinkedList linkedList = new TermSortedLinkedList();

        Thread[] threads = IntStream.range(0, threadCount)
                .mapToObj(i -> new worker.node_sync.ReadAndInsertThread(filenames, linkedList))
                .toArray(Thread[]::new);

        long startTime = System.nanoTime();

        for (Thread thread : threads)
        {
            thread.start();
            Thread.sleep(100);
        }
        for (Thread thread : threads)
            thread.join();

        long endTime = System.nanoTime();
        double elapsedTime = Utils.getElapsedTimeSeconds(startTime, endTime);

        System.out.println("Node Sync (All Read and Insert) - Elapsed time\t\t: " + elapsedTime + "s");

        linkedList.writePolynomialToFile("NodeSync_AllReadAndInsert_result.txt");
    }

    private static void runListSyncAllReadAndInsert(int threadCount, Queue<String> filenames) throws
            InterruptedException
    {

        TermSortedLinkedList linkedList = new TermSortedLinkedList();

        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++)
            threads[i] = new worker.list_sync.ReadAndInsertThread(filenames, linkedList);

        long startTime = System.nanoTime();

        for (Thread thread : threads)
            thread.start();
        for (Thread thread : threads)
            thread.join();

        long endTime = System.nanoTime();
        double elapsedTime = Utils.getElapsedTimeSeconds(startTime, endTime);

        System.out.println("List Sync (All Read and Insert) - Elapsed time\t\t: " + elapsedTime + "s");

        linkedList.writePolynomialToFile("ListSync_AllReadAndInsert_result.txt");
    }

}
