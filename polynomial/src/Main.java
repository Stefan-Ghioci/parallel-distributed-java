import structure.TermSortedLinkedList;
import utils.Utils;
import worker.ListSyncReadAndInsertThread;
import worker.NodeSyncReadAndInsertThread;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main
{
    public static void main(String[] args) throws InterruptedException
    {
        int threadCount = Integer.parseInt(args[0]);
        int count = Integer.parseInt(args[1]);
        int maxTermsCount = Integer.parseInt(args[2]);
        int maxDegree = Integer.parseInt(args[3]);
        int minCoefficient = Integer.parseInt(args[4]);
        int maxCoefficient = Integer.parseInt(args[5]);

        Queue<String> filenames = Utils.createRandomPolynomialFiles(count,
                                                                    maxTermsCount,
                                                                    maxDegree,
                                                                    minCoefficient,
                                                                    maxCoefficient);


        while (threadCount != 1)
        {
            System.out.println("Thread count: " + threadCount);

            runListSyncAllReadAndInsert(threadCount, new ConcurrentLinkedQueue<>(filenames));

            runNodeSyncAllReadAndInsert(threadCount, new ConcurrentLinkedQueue<>(filenames));

            runListSyncFirstReadsRestInsert(threadCount, new ConcurrentLinkedQueue<>(filenames));

            if (!Utils.contentEquals("ListSync_result.txt", "NodeSync_result.txt"))
            {
                System.out.println("Results are not the same!");
            }

            threadCount /= 2;
        }
    }

    private static void runListSyncFirstReadsRestInsert(int threadCount, Queue<String> filenames)
    {
        //TODO
    }

    private static void runNodeSyncAllReadAndInsert(int threadCount, Queue<String> filenames) throws InterruptedException
    {

        TermSortedLinkedList linkedList = new TermSortedLinkedList();

        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++)
            threads[i] = new NodeSyncReadAndInsertThread(filenames, linkedList);

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

        System.out.println("Node Sync Elapsed time: " + elapsedTime + "s");

        linkedList.writePolynomialToFile("NodeSync_result.txt");
    }

    private static void runListSyncAllReadAndInsert(int threadCount, Queue<String> filenames) throws InterruptedException
    {

        TermSortedLinkedList linkedList = new TermSortedLinkedList();

        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++)
            threads[i] = new ListSyncReadAndInsertThread(filenames, linkedList);

        long startTime = System.nanoTime();

        for (Thread thread : threads)
            thread.start();
        for (Thread thread : threads)
            thread.join();

        long endTime = System.nanoTime();
        double elapsedTime = Utils.getElapsedTimeSeconds(startTime, endTime);

        System.out.println("List Sync Elapsed time: " + elapsedTime + "s");

        linkedList.writePolynomialToFile("ListSync_result.txt");
    }

}
