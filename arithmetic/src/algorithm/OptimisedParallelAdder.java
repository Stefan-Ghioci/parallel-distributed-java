package algorithm;

import structure.OptimisedAdderThread;
import utils.DataUtils;
import utils.Paths;

import java.util.HashMap;
import java.util.Map;

import static utils.FileUtils.saveBigNumberToFile;

public class OptimisedParallelAdder
{

    public static double run(Integer threadsCount, int[] number1, int[] number2)
    {
        int length = number1.length;

        int[] sum = new int[length];
        Map<Integer, Integer> carryFlags = new HashMap<>();

        Thread[] threads = createOptimisedAdderThreads(number1, number2, threadsCount, sum, carryFlags);

        Long startTime = System.nanoTime();

        ParallelAdder.compute(threads);

        Long endTime = System.nanoTime();
        double elapsedTime = DataUtils.getElapsedTimeMilli(startTime, endTime);

        saveBigNumberToFile(sum, Paths.PARALLEL_RESULT);

        return elapsedTime;
    }

    private static Thread[] createOptimisedAdderThreads(int[] number1,
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

            threads[i] = new OptimisedAdderThread(number1, number2, sum, left, right, carryFlags, previousThread);
        }
        return threads;
    }
}
