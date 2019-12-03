import structure.MyConcurrentLinkedQueue;
import structure.Term;
import structure.TermSortedLinkedList;
import utils.Utils;

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

        MyConcurrentLinkedQueue<String> filenames = Utils.createRandomPolynomialFiles(polynomialsCount,
                                                                                      maxTermsCount,
                                                                                      maxDegree,
                                                                                      minCoefficient,
                                                                                      maxCoefficient);

        while (threadCount != 1)
        {
            System.out.println("Thread count: " + threadCount);

            for (int i = 1; i < threadCount; i++)
                runListSyncFirstReadsRestInsert(threadCount, i, new MyConcurrentLinkedQueue<>(filenames));

            for (int i = 1; i < threadCount; i++)
                runNodeSyncFirstReadsRestInsert(threadCount, i, new MyConcurrentLinkedQueue<>(filenames));

            threadCount /= 2;
        }
    }

    private static void runNodeSyncFirstReadsRestInsert(int threadCount,
                                                        int readCount,
                                                        MyConcurrentLinkedQueue<String> filenames) throws
            InterruptedException
    {
        TermSortedLinkedList linkedList = new TermSortedLinkedList();
        MyConcurrentLinkedQueue<Term> termQueue = new MyConcurrentLinkedQueue<>();
        int insertCount = threadCount - readCount;


        Thread[] readThreads = IntStream.range(0, readCount)
                .mapToObj(i -> new worker.ReadOnlyThread(filenames, termQueue))
                .toArray(Thread[]::new);
        Thread[] insertThreads = IntStream.range(0, insertCount)
                .mapToObj(i -> new worker.node_sync.InsertOnlyThread(readThreads, linkedList, termQueue))
                .toArray(Thread[]::new);

        long startTime = System.nanoTime();

        for (Thread thread : readThreads)
            thread.start();

        for (Thread thread : insertThreads)
        {
            thread.start();
            Thread.sleep(100);
        }

        for (Thread insertThread : insertThreads)
            insertThread.join();

        long endTime = System.nanoTime();
        double elapsedTime = Utils.getElapsedTimeSeconds(startTime, endTime);


        System.out.println("Node Sync (" + readCount + " Read, " + insertCount + " Insert)\t\t: " + elapsedTime + "s");

        linkedList.writePolynomialToFile("results\\NodeSync_" + readCount + "Read" + insertCount + "Insert.txt");
    }

    private static void runListSyncFirstReadsRestInsert(int threadCount,
                                                        int readCount,
                                                        MyConcurrentLinkedQueue<String> filenames) throws
            InterruptedException
    {
        TermSortedLinkedList linkedList = new TermSortedLinkedList();
        MyConcurrentLinkedQueue<Term> termQueue = new MyConcurrentLinkedQueue<>();
        int insertCount = threadCount - readCount;

        Thread[] readThreads = IntStream.range(0, readCount)
                .mapToObj(i -> new worker.ReadOnlyThread(filenames, termQueue))
                .toArray(Thread[]::new);
        Thread[] insertThreads = IntStream.range(0, threadCount - readCount)
                .mapToObj(i -> new worker.list_sync.InsertOnlyThread(readThreads, linkedList, termQueue))
                .toArray(Thread[]::new);

        long startTime = System.nanoTime();

        for (Thread thread : readThreads)
            thread.start();

        for (Thread insertThread : insertThreads)
            insertThread.start();

        for (Thread insertThread : insertThreads)
            insertThread.join();

        long endTime = System.nanoTime();
        double elapsedTime = Utils.getElapsedTimeSeconds(startTime, endTime);


        System.out.println("List Sync (" + readCount + " Read, " + insertCount + " Insert)\t\t: " + elapsedTime + "s");

        linkedList.writePolynomialToFile("results\\ListSync_" + readCount + "Read" + insertCount + "Insert.txt");
    }

}
