package algorithm;

import structure.AdderThread;

public class ParallelAdder
{
    @SuppressWarnings("ForLoopReplaceableByForEach")
    public static void compute(Thread[] threads)
    {
        for (int i = 0, threadsLength = threads.length; i < threadsLength; i++)
            threads[i].start();

        for (int i = 0, threadsLength = threads.length; i < threadsLength; i++)
        {
            try
            {
                threads[i].join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static Thread[] createAdderThreads(int[] number1, int[] number2, int threadsCount, int[] sum)
    {
        int length = number1.length;
        int intervalLength = length / threadsCount;
        int remainder = length % threadsCount;

        Thread[] threads = new Thread[threadsCount];


        threads[0] = new AdderThread(number1, number2, sum, 0, intervalLength + remainder, null);

        int i = 1;
        while (i < threadsCount)
        {
            int left = i * intervalLength + remainder;
            int right = (i + 1) * intervalLength + remainder;

            threads[i] = new AdderThread(number1, number2, sum, left, right, threads[i - 1]);
            i++;
        }
        return threads;
    }

}
