import algorithm.ParallelMultiplier;
import algorithm.SequentialMultiplier;
import utils.Paths;

import java.util.List;

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

        byte[] number1 = numbers.get(0);
        byte[] number2 = numbers.get(1);

        double sequentialTime = SequentialMultiplier.run(number1, number2);
        double parallelTime = ParallelMultiplier.run(threadsCount, number1, number2);

        if (fileContentsEqual(Paths.SEQUENTIAL_PRODUCT, Paths.PARALLEL_PRODUCT))
        {
            System.out.println("-------------------------------------");
            System.out.println("Sequential Time: ");
            System.out.println(sequentialTime + "ms");

            System.out.println("Parallel Time: ");
            System.out.println(parallelTime + "ms");
            System.out.println("-------------------------------------");

            saveResultsToCsv(Paths.RESULTS, minDigits, maxDigits, sequentialTime, parallelTime, threadsCount);
        }
        else
        {
            System.out.println("Results are not equal!!!");
        }
    }

}
