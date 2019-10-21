package algorithm;

import structure.AdderThread;
import utils.Paths;

import java.util.HashMap;
import java.util.Map;

import static utils.DataUtils.getElapsedTimeMilli;
import static utils.FileUtils.saveBigNumberToFile;

public class OptimisedParallelAdder
{

    public static double run(Integer threadsCount, int[] number1, int[] number2)
    {
        int maxLength = number1.length + 1;

        int[] sum = new int[maxLength];
        int[] carryFlags = new int[threadsCount];

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
        //TODO implement OptimisedAdderThread
        return new Thread[0];
    }
}
