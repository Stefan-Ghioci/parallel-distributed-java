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
        int[] number1 = numbers.get(0);
        int[] number2 = numbers.get(1);

        double sequentialTime = SequentialAdder.runSequential(number1, number2);
        double parallelTime = ParallelAdder.runParallel(threadsCount, number1, number2);

        printTimeResults(sequentialTime, parallelTime);

        if (fileContentsEqual(Paths.SEQUENTIAL_SUM, Paths.PARALLEL_RESULT))
            saveResultsToCsv(Paths.RESULTS, minDigits, maxDigits, sequentialTime, parallelTime, threadsCount);
    }

    private static void printTimeResults(double sequentialTime, double parallelTime)
    {
        System.out.println("-------------------------------------");

        System.out.println("Sequential Time: ");
        System.out.println(sequentialTime + "ms");
        System.out.println("Parallel Time: ");
        System.out.println(parallelTime + "ms");

        System.out.println("-------------------------------------");
    }


}
