package algorithm;

import utils.DataUtils;
import utils.Paths;

import static utils.DataUtils.getElapsedTimeMilli;
import static utils.FileUtils.saveBigNumberToFile;

public class SequentialAdder
{
    static void compute(int[] number1, int[] number2, int[] sum)
    {
        int length = number1.length;

        int carry = 0;
        for (int i = 0; i < length; i++)
        {
            int digitSum = number1[i] + number2[i] + carry;
            sum[i] = digitSum % 10;
            carry = digitSum / 10;
        }
        if (carry != 0) sum[length] = carry;

    }

    public static double run(int[] number1, int[] number2)
    {
        int maxLength = number1.length + 1;
        int[] sum = new int[maxLength];

        long startTime = System.nanoTime();

        compute(number1, number2, sum);

        long endTime = System.nanoTime();
        double elapsedTime = getElapsedTimeMilli(startTime, endTime);

        saveBigNumberToFile(sum, Paths.SEQUENTIAL_SUM);

        return elapsedTime;
    }
}
