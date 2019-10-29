package algorithm;

import structure.MultiplierThread;
import structure.ReducerThread;
import utils.Paths;

import java.util.ArrayList;
import java.util.List;

import static algorithm.ParallelAdder.joinAll;
import static algorithm.ParallelAdder.startAll;
import static algorithm.SequentialMultiplier.addAllNumbers;
import static utils.DataUtils.getElapsedTimeMilli;
import static utils.FileUtils.saveBigNumberToFile;

public class ParallelMultiplier
{

    public static double run(int threadsCount, byte[] number1, byte[] number2)
    {
        int maxLength = number1.length + number2.length;

        byte[] multiplicand, multiplier;

        if (number2.length < number1.length)
        {
            multiplicand = number1;
            multiplier = number2;
        }
        else
        {
            multiplicand = number2;
            multiplier = number1;
        }

        byte[] product = new byte[maxLength];
        List<byte[]> partialProducts = new ArrayList<>();
        List<byte[]> reducedPartialProducts;

        Thread[] multiplierThreads = createMultiplierThreads(multiplicand, multiplier, threadsCount, partialProducts);

        long startTime = System.nanoTime();

        startAll(multiplierThreads);
        joinAll(multiplierThreads);

        do
        {
            reducedPartialProducts = new ArrayList<>();

            Thread[] reducerThreads =
                    createReducerThreads(partialProducts, reducedPartialProducts, threadsCount, multiplier.length);

            startAll(reducerThreads);
            joinAll(reducerThreads);

            partialProducts = new ArrayList<>(reducedPartialProducts);
        }
        while (2 * threadsCount <= reducedPartialProducts.size());

        addAllNumbers(product, partialProducts);

        long endTime = System.nanoTime();
        double elapsedTime = getElapsedTimeMilli(startTime, endTime);

        saveBigNumberToFile(product, Paths.PARALLEL_PRODUCT);

        return elapsedTime;
    }

    private static Thread[] createReducerThreads(List<byte[]> numbers,
                                                 List<byte[]> reducedNumbers,
                                                 int threadsCount,
                                                 int numbersSize)
    {
        int intervalLength = numbersSize / threadsCount;
        int remainder = numbersSize % threadsCount;

        Thread[] threads = new Thread[threadsCount];


        for (int i = 0, left, right = 0; i < threadsCount; i++, remainder--)
        {
            left = right;
            right = remainder > 0 ? left + intervalLength + 1 : left + intervalLength;

            threads[i] = new ReducerThread(numbers, left, right, reducedNumbers);
        }

        return threads;
    }

    private static Thread[] createMultiplierThreads(byte[] multiplicand,
                                                    byte[] multiplier,
                                                    int threadsCount,
                                                    List<byte[]> partialProducts)
    {


        int length = multiplier.length;
        int intervalLength = length / threadsCount;
        int remainder = length % threadsCount;

        Thread[] threads = new Thread[threadsCount];


        for (int i = 0, left, right = 0; i < threadsCount; i++, remainder--)
        {
            left = right;
            right = remainder > 0 ? left + intervalLength + 1 : left + intervalLength;

            threads[i] = new MultiplierThread(multiplicand, multiplier, left, right, partialProducts);
        }
        return threads;
    }
}
