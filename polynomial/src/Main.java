import structure.TermSortedLinkedList;
import utils.Utils;
import worker.ListSyncThread;
import worker.NodeSyncThread;

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

        Queue<String> polynomialFilenames = Utils.createRandomPolynomialFiles(count,
                                                                              maxTermsCount,
                                                                              maxDegree,
                                                                              minCoefficient,
                                                                              maxCoefficient);

        Queue<String> copy = new ConcurrentLinkedQueue<>(polynomialFilenames);

        ///////////////////////////////////////////////////////////////////////////////////////////

        {
            TermSortedLinkedList linkedList = new TermSortedLinkedList();

            Thread[] threads = new Thread[threadCount];

            for (int i = 0; i < threadCount; i++)
                threads[i] = new ListSyncThread(polynomialFilenames, linkedList);

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
        ///////////////////////////////////////////////////////////////////////////////////////////
        {
            polynomialFilenames = copy;
            TermSortedLinkedList linkedList = new TermSortedLinkedList();

            Thread[] threads = new Thread[threadCount];

            for (int i = 0; i < threadCount; i++)
                threads[i] = new NodeSyncThread(polynomialFilenames, linkedList);

            long startTime = System.nanoTime();

            for (Thread thread : threads)
                thread.start();
            for (Thread thread : threads)
                thread.join();

            long endTime = System.nanoTime();
            double elapsedTime = Utils.getElapsedTimeSeconds(startTime, endTime);

            System.out.println("Node Sync Elapsed time: " + elapsedTime + "s");

            linkedList.writePolynomialToFile("NodeSync_result.txt");
        }

        if (!Utils.contentEquals("ListSync_result.txt", "NodeSync_result.txt"))
        {
            System.out.println("Results are not the same!");
        }
    }

}
