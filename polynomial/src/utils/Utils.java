package utils;

import structure.MyConcurrentLinkedQueue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Utils
{
    private static void writeRandomPolynomialToFile(String filename,
                                                    Integer maxTermsCount,
                                                    Integer maxDegree,
                                                    Integer minCoefficient,
                                                    Integer maxCoefficient)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false)))
        {
            Random random = new Random();

            int termsCount = random.nextInt(maxTermsCount) + 1;

            for (int i = 0; i < termsCount; i++)
            {
                int degree = random.nextInt(maxDegree + 1);
                int coefficient = generateNonZeroNumber(minCoefficient, maxCoefficient, random);

                writer.write(degree + " " + coefficient);
                writer.newLine();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static int generateNonZeroNumber(Integer min, Integer max, Random random)
    {
        int x = 0;
        while (x == 0)
            x = random.nextInt((max - min) + 1) + min;
        return x;
    }

    public static MyConcurrentLinkedQueue<String> createRandomPolynomialFiles(int count,
                                                                              Integer maxTermsCount,
                                                                              Integer maxDegree,
                                                                              Integer minCoefficient,
                                                                              Integer maxCoefficient)
    {
        MyConcurrentLinkedQueue<String> filenames = new MyConcurrentLinkedQueue<>();
        String root = "generated/polynomial";

        for (int i = 0; i < count; i++)
        {
            String filename = root + i;
            filenames.add(filename);

            writeRandomPolynomialToFile(filename, maxTermsCount, maxDegree, minCoefficient, maxCoefficient);
        }

        return filenames;
    }

    public static double getElapsedTimeSeconds(long startTime, long endTime)
    {
        return (double) (endTime - startTime) / 1000000000;
    }

    public static boolean contentEquals(String filename1, String filename2)
    {
        try
        {
            String text1 = Files.readString(Paths.get(filename1));
            String text2 = Files.readString(Paths.get(filename2));

            return text1.equals(text2);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean anyAlive(Thread[] readThreads)
    {
        return Arrays.stream(readThreads).anyMatch(Thread::isAlive);
    }
}
