package utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataUtils
{
    static int[] stringToBigNumber(String string)
    {
        return IntStream.range(0, string.length())
                .map(i -> Character.getNumericValue(string.charAt(string.length() - i - 1)))
                .toArray();
    }

    static String bigNumberToString(int[] number)
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

    public static List<int[]> extendNumbersToMaxLength(List<int[]> numbers)
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
