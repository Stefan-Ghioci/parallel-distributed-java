package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class FileUtils
{


    public static void writeDataToCsvFile(String filename, List<String> data)
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
}
