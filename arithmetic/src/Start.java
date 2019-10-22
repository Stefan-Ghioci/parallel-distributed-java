import algorithm.OptimisedParallelAdder;
import algorithm.ParallelAdder;
import algorithm.SequentialAdder;
import utils.Paths;

import java.util.List;

import static utils.DataUtils.extendArrayMemory;
import static utils.FileUtils.*;


public class Start
{

    public static void main(String[] args)
    {

        int threadsCount = Integer.parseInt(args[0]);
        int minDigits = Integer.parseInt(args[1]);
        int maxDigits = Integer.parseInt(args[2]);

        generateBigDataFile(Paths.NUMBERS, 2, minDigits, maxDigits);
        List<byte[]> numbers = getBigNumberListFromFile(Paths.NUMBERS);

        if (minDigits != maxDigits)
            numbers = extendArrayMemory(numbers);

        byte[] number1 = numbers.get(0);
        byte[] number2 = numbers.get(1);

        double sequentialTime = SequentialAdder.run(number1, number2);
        double parallelTime = ParallelAdder.run(threadsCount, number1, number2);
        double optimisedParallelTime = OptimisedParallelAdder.run(threadsCount, number1, number2);

        if (fileContentsEqual(Paths.SEQUENTIAL_SUM, Paths.PARALLEL_RESULT) &&
                fileContentsEqual(Paths.SEQUENTIAL_SUM, Paths.OPTIMISED_PARALLEL_RESULT))
        {
            System.out.println("-------------------------------------");
            System.out.println("Sequential Time: ");
            System.out.println(sequentialTime + "ms");

            System.out.println("Parallel Time: ");
            System.out.println(parallelTime + "ms");

            System.out.println("Optimised Time: ");
            System.out.println(optimisedParallelTime + "ms");
            System.out.println("-------------------------------------");

            saveResultsToCsv(Paths.RESULTS,
                             minDigits,
                             maxDigits,
                             sequentialTime,
                             parallelTime,
                             optimisedParallelTime,
                             threadsCount);
        }
        else
        {
            System.out.println("Results are not equal!!!");
        }
    }

}
