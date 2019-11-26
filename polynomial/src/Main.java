import structure.AdderThread;
import structure.TermSortedLinkedList;
import utils.Utils;

import java.util.Queue;

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

        TermSortedLinkedList linkedList = new TermSortedLinkedList();

        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++)
            threads[i] = new AdderThread(polynomialFilenames, linkedList);

        long startTime = System.nanoTime();

        for (Thread thread : threads)
            thread.start();
        for (Thread thread : threads)
            thread.join();

        long endTime = System.nanoTime();
        double elapsedTime = Utils.getElapsedTimeMilli(startTime, endTime);

        System.out.println("Elapsed time: " + elapsedTime);
        linkedList.writePolynomialToFile("result.txt");
    }

}
