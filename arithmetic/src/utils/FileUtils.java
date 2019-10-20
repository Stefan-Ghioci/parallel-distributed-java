package utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static utils.DataUtils.bigNumberToString;

public class FileUtils
{


    private static void writeDataToCsvFile(String filename, List<String> data)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true)))
        {
            String line = DataUtils.stringListToCsvString(data);

            writer.write(line);
            writer.newLine();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static List<int[]> getBigNumberListFromFile(String filename)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            return reader.lines()
                    .map(DataUtils::stringToBigNumber)
                    .collect(Collectors.toList());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void generateBigDataFile(String filename, Integer size, Integer min, Integer max)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, Charset.defaultCharset(), false)))
        {
            Random random = new Random();


            for (int i = 0; i < size; i++)
            {
                int randomSize = random.nextInt(max - min + 1) + min;
                int firstDigit = random.nextInt(9) + 1;

                writer.write(Integer.toString(firstDigit));

                for (int j = 0; j < randomSize - 1; j++)
                {
                    int digit = random.nextInt(10);
                    writer.write(Integer.toString(digit));
                }

                writer.newLine();
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean fileContentsEqual(String filename1, String filename2)
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

    public static List<int[]> getNumbers(String filename)
    {
        List<int[]> generatedNumbers = getBigNumberListFromFile(filename);

        return DataUtils.normaliseNumberLengths(generatedNumbers);
    }

    public static void saveBigNumberToFile(int[] sum, String filename)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, Charset.defaultCharset(), false)))
        {
            writer.write(bigNumberToString(sum));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void saveResultsToCsv(String resultsFilename, Integer minDigits,
                                        Integer maxDigits,
                                        double sequentialTime,
                                        double parallelTime,
                                        Integer threadsCount)
    {
        List<String> data = new ArrayList<>();

        data.add(minDigits + "-" + maxDigits);
        data.add(String.valueOf(sequentialTime));
        data.add(String.valueOf(parallelTime));
        data.add(String.valueOf(threadsCount));

        writeDataToCsvFile(resultsFilename,data);
    }
}
