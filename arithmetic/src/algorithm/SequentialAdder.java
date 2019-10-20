package algorithm;

import utils.Paths;

import java.util.List;

import static utils.FileUtils.saveBigNumberToFile;

public class SequentialAdder
{
    static int[] compute(int[] number1, int[] number2)
    {
        int length = number1.length;

        int[] sum = new int[length + 1];

        int carry = 0;
        for (int i = 0; i < length; i++)
        {
            sum[i] = (number1[i] + number2[i] + carry) % 10;
            carry = (number1[i] + number2[i] + carry) / 10;
        }
        if (carry != 0) sum[length] = carry;

        return sum;
    }

    public static double runSequential(List<int[]> numbers)
    {
        int[] number1 = numbers.get(0);
        int[] number2 = numbers.get(1);

        Long startTime = System.nanoTime();

        int[] sum = compute(number1, number2);

        Long endTime = System.nanoTime();
        double elapsedTime = (double) (endTime - startTime) / 1000000;

        saveBigNumberToFile(sum, Paths.SEQUENTIAL_SUM);

        return elapsedTime;
    }
}
