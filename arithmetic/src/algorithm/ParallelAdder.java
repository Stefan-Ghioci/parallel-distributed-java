package algorithm;

import structure.AdderThread;
import utils.DataUtils;
import utils.Paths;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.FileUtils.saveBigNumberToFile;

public class ParallelAdder
{
    @SuppressWarnings("ForLoopReplaceableByForEach")
    private static void compute(Thread[] threads)
    {
        for (int i = 0, threadsLength = threads.length; i < threadsLength; i++)
            threads[i].start();

        for (int i = 0, threadsLength = threads.length; i < threadsLength; i++)
        {
            try
            {
                threads[i].join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static Thread[] createAdderThreads(int[] number1,
                                               int[] number2,
                                               int threadsCount,
                                               int[] sum,
                                               int[] carryFlags)
    {
        int length = number1.length;
        int intervalLength = length / threadsCount;
        int remainder = length % threadsCount;

        Thread[] threads = new Thread[threadsCount];


        for (int i = 0, left, right = 0; i < threadsCount; i++, remainder--)
        {
            left = right;
            right = remainder > 0 ? left + intervalLength + 1 : left + intervalLength;

            threads[i] = new AdderThread(number1, number2, sum, left, right, carryFlags);
        }
        return threads;
    }

    public static double runParallel(Integer threadsCount, int[] number1, int[] number2)
    {
        int length = number1.length;

        int[] incompleteSum = new int[length];
        int[] carryFlags = new int[length];

        Thread[] threads = createAdderThreads(number1, number2, threadsCount, incompleteSum, carryFlags);

        Long startTime = System.nanoTime();

        compute(threads);
        int[] sum = SequentialAdder.compute(incompleteSum, carryFlags);

        Long endTime = System.nanoTime();
        double elapsedTime = DataUtils.getElapsedTimeMilli(startTime, endTime);

        saveBigNumberToFile(sum, Paths.PARALLEL_RESULT);

        return elapsedTime;
    }

}
