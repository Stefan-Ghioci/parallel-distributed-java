package algorithm;

import structure.AdderThread;

public class ParallelAdder
{
    public static void compute(Thread[] threads)
    {
        for (Thread thread : threads) thread.start();

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

    public static Thread[] createAdderThreads(int[] number1,
                                              int[] number2,
                                              int threadsCount,
                                              int[] sum,
                                              int[] carryFlags)
    {
        int length = number1.length;
        Thread[] threads = new Thread[threadsCount];

        for (int i = 0; i < threadsCount; i++)
        {
            int left = i * (int) Math.ceil((double) length / threadsCount);
            int right = Math.min((i + 1) * (int) Math.ceil((double) length / threadsCount), length);

            threads[i] = new AdderThread(number1, number2, sum, carryFlags, left, right);
        }
        return threads;
    }
}
