package utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataUtils
{
    static byte[] stringToBigNumber(String string)
    {
        int length = string.length();
        byte[] arr = new byte[10];
        int count = 0;
        for (int i = 0; i < length; i++)
        {
            byte numericValue = (byte) Character.getNumericValue(string.charAt(length - i - 1));
            if (arr.length == count) arr = Arrays.copyOf(arr, count * 2);
            arr[count++] = numericValue;
        }
        arr = Arrays.copyOfRange(arr, 0, count);
        return arr;
    }

    static String bigNumberToString(byte[] number)
    {
        String string = IntStream.iterate(number.length - 1, i -> i >= 0, i -> i - 1)
                .mapToObj(i -> String.valueOf(number[i]))
                .collect(Collectors.joining());

        return cutStartingZeroes(string);
    }

    private static String cutStartingZeroes(String string)
    {
        int i = 0;
        while (string.charAt(i) == '0')
            i++;
        return string.substring(i);
    }

    static String stringListToCsvString(List<String> numbers)
    {
        StringBuilder csvBuilder = new StringBuilder();

        for (String number : numbers)
        {
            csvBuilder.append(number);
            csvBuilder.append(",");
        }

        String line = csvBuilder.toString();
        line = line.substring(0, line.length() - 1);

        return line;
    }

    public static List<byte[]> extendArrayMemory(List<byte[]> numbers)
    {
        int maxLength = Collections.max(numbers, (Comparator.comparing((number) -> number.length))).length;

        return numbers.stream()
                .map(number -> number.length == maxLength ? number : Arrays.copyOf(number, maxLength))
                .collect(Collectors.toList());
    }

    public static double getElapsedTimeMilli(long startTime, long endTime)
    {
        return (double) (endTime - startTime) / 1000000;
    }

}
