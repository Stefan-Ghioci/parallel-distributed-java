package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

    public static Queue<String> createRandomPolynomialFiles(int count,
                                                            Integer maxTermsCount,
                                                            Integer maxDegree,
                                                            Integer minCoefficient,
                                                            Integer maxCoefficient)
    {
        Queue<String> filenames = new ConcurrentLinkedQueue<>();
        String root = "polynomial";

        for (int i = 0; i < count; i++)
        {
            String filename = root + i;
            filenames.add(filename);

            writeRandomPolynomialToFile(filename, maxTermsCount, maxDegree, minCoefficient, maxCoefficient);
        }

        return filenames;
    }

    public static double getElapsedTimeMilli(long startTime, long endTime)
    {
        return (double) (endTime - startTime) / 1000000;
    }
}
