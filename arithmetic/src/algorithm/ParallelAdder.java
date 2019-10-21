package algorithm;

import structure.AdderThread;
import utils.DataUtils;
import utils.Paths;

import java.util.HashMap;
import java.util.Map;

import static utils.DataUtils.getElapsedTimeMilli;
import static utils.FileUtils.saveBigNumberToFile;

public class ParallelAdder
{
    @SuppressWarnings("ForLoopReplaceableByForEach")
    static void compute(Thread[] threads)
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
                                               Integer threadsCount,
                                               int[] sum,
                                               Map<Integer, Integer> carryFlags)
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

            threads[i] = new AdderThread(number1, number2, sum, left, right, carryFlags, previousThread);
        }
        return threads;
    }
    public static double run(Integer threadsCount, int[] number1, int[] number2)
    {
        int maxLength = number1.length + 1;

        int[] sum = new int[maxLength];
        Map<Integer, Integer> carryFlags = new HashMap<>();

        Thread[] threads = createAdderThreads(number1, number2, threadsCount, sum, carryFlags);

        long startTime = System.nanoTime();

        ParallelAdder.compute(threads);
        sum[maxLength - 1] = carryFlags.get(maxLength - 1);

        long endTime = System.nanoTime();
        double elapsedTime = getElapsedTimeMilli(startTime, endTime);

        saveBigNumberToFile(sum, Paths.PARALLEL_RESULT);

        return elapsedTime;
    }

}
