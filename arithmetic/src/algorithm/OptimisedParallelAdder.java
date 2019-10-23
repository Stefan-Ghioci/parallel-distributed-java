package algorithm;

import structure.OptimisedAdderThread;
import utils.Paths;

import static algorithm.ParallelAdder.startAll;
import static utils.DataUtils.getElapsedTimeMilli;
import static utils.FileUtils.saveBigNumberToFile;

public class OptimisedParallelAdder
{

    public static double run(int threadsCount, byte[] number1, byte[] number2)
    {
        int maxLength = number1.length + 1;

        byte[] sum = new byte[maxLength];
        byte[] carryFlags = new byte[threadsCount];

        Thread[] threads = createOptimisedAdderThreads(number1, number2, threadsCount, sum, carryFlags);

        long startTime = System.nanoTime();

        startAll(threads);
        joinLast(threads);

        long endTime = System.nanoTime();
        double elapsedTime = getElapsedTimeMilli(startTime, endTime);

        saveBigNumberToFile(sum, Paths.OPTIMISED_PARALLEL_RESULT);

        return elapsedTime;
    }

    private static void joinLast(Thread[] threads)
    {
        try
        {
            threads[threads.length - 1].join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private static Thread[] createOptimisedAdderThreads(byte[] number1,
                                                        byte[] number2,
                                                        Integer threadsCount,
                                                        byte[] sum,
                                                        byte[] carryFlags)
    {
        int length = number1.length;
        int intervalLength = length / threadsCount;
        int remainder = length % threadsCount;

        Thread[] threads = new Thread[threadsCount];


        for (int i = 0, left, right = 0; i < threadsCount; i++, remainder--)
        {
            left = right;
            right = remainder > 0 ? left + intervalLength + 1 : left + intervalLength;
            Thread previousThread = i == 0 ? null : threads[i - 1];

            threads[i] = new OptimisedAdderThread(i, number1, number2, sum, left, right, carryFlags, previousThread);
        }
        return threads;
    }

}
