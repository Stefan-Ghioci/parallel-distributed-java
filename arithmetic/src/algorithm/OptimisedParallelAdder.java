package algorithm;

import structure.OptimisedAdderThread;
import utils.Paths;

import static utils.DataUtils.getElapsedTimeMilli;
import static utils.DataUtils.initializeCarryFlags;
import static utils.FileUtils.saveBigNumberToFile;

public class OptimisedParallelAdder
{

    public static double run(Integer threadsCount, int[] number1, int[] number2)
    {
        int maxLength = number1.length + 1;

        int[] sum = new int[maxLength];
        int[] carryFlags = initializeCarryFlags(threadsCount);

        Thread[] threads = createOptimisedAdderThreads(number1, number2, threadsCount, sum, carryFlags);

        long startTime = System.nanoTime();

        ParallelAdder.compute(threads);

        long endTime = System.nanoTime();
        double elapsedTime = getElapsedTimeMilli(startTime, endTime);

        saveBigNumberToFile(sum, Paths.PARALLEL_RESULT);

        return elapsedTime;
    }

    private static Thread[] createOptimisedAdderThreads(int[] number1,
                                                        int[] number2,
                                                        Integer threadsCount,
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

            threads[i] = new OptimisedAdderThread(i, number1, number2, sum, left, right, carryFlags);
        }
        return threads;
    }
}
