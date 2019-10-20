import algorithm.ParallelAdder;
import algorithm.SequentialAdder;
import utils.Paths;

import java.util.List;

import static utils.FileUtils.*;


public class Start
{

    public static void main(String[] args)
    {
        Integer threadsCount = Integer.valueOf(args[0]);
        Integer minDigits = Integer.valueOf(args[1]);
        Integer maxDigits = Integer.valueOf(args[2]);

        generateBigDataFile(Paths.NUMBERS, 2, minDigits, maxDigits);

        List<int[]> numbers = getNumbers(Paths.NUMBERS);

        double sequentialTime = runSequential(numbers);
        double parallelTime = runParallel(threadsCount, numbers);

        if (fileContentsEqual(Paths.SEQUENTIAL_SUM, Paths.PARALLEL_RESULT))
            saveResultsToCsv(Paths.RESULTS, minDigits, maxDigits, sequentialTime, parallelTime, threadsCount);
    }

    private static double runParallel(Integer threadsCount, List<int[]> numbers)
    {
        int[] sum = new int[numbers.get(0).length + 1];
        int[] carryFlags = new int[numbers.get(0).length + 1];

        Thread[] threads = ParallelAdder
                .createAdderThreads(numbers.get(0), numbers.get(1), threadsCount, sum, carryFlags);


        Long startTime = System.nanoTime();

        ParallelAdder.compute(threads);
        sum = SequentialAdder.compute(sum, carryFlags);

        Long endTime = System.nanoTime();
        double elapsedTime = (double) (endTime - startTime) / 1000000;

        saveBigNumberToFile(sum, Paths.PARALLEL_RESULT);

        return elapsedTime;
    }

    private static double runSequential(List<int[]> numbers)
    {
        Long startTime = System.nanoTime();

        int[] sum = SequentialAdder.compute(numbers.get(0), numbers.get(1));

        Long endTime = System.nanoTime();
        double elapsedTime = (double) (endTime - startTime) / 1000000;

        saveBigNumberToFile(sum, Paths.SEQUENTIAL_SUM);

        return elapsedTime;
    }


}
