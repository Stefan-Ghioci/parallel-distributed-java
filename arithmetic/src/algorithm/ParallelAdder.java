package algorithm;

import structure.AdderThread;
import utils.Paths;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static utils.DataUtils.getElapsedTimeMilli;
import static utils.FileUtils.saveBigNumberToFile;

public class ParallelAdder
{
    public static double run(int threadsCount, byte[] number1, byte[] number2)
    {
        int maxLength = number1.length + 1;

        byte[] sum = new byte[maxLength];
        Map<Integer, Byte> carryFlags = new ConcurrentHashMap<>();

        Thread[] threads = createAdderThreads(number1, number2, threadsCount, sum, carryFlags);

        long startTime = System.nanoTime();

        startAll(threads);
        joinAll(threads);

        addLeftOvers(sum, carryFlags);

        long endTime = System.nanoTime();
        double elapsedTime = getElapsedTimeMilli(startTime, endTime);

        saveBigNumberToFile(sum, Paths.PARALLEL_RESULT);

        return elapsedTime;
    }

    private static void addLeftOvers(byte[] sum, Map<Integer, Byte> carryFlags)
    {
        carryFlags.forEach((index, carry) -> {

            int i = index;
            while (carry != 0)
            {
                int digitSum = sum[i] + carry;
                sum[i] = (byte) (digitSum % 10);
                carry = (byte) (digitSum / 10);
                i++;
            }
        });
    }

    private static Thread[] createAdderThreads(byte[] number1,
                                               byte[] number2,
                                               int threadsCount,
                                               byte[] sum,
                                               Map<Integer, Byte> carryFlags)
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

    static void startAll(Thread[] threads)
    {
        for (Thread thread : threads) thread.start();

    }

    private static void joinAll(Thread[] threads)
    {
        for (Thread thread : threads)
        {
            try
            {
                thread.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
