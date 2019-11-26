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
                int coefficient = random.nextInt((maxCoefficient - minCoefficient) + 1) + minCoefficient;

                writer.write(degree + " " + coefficient);
                writer.newLine();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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
