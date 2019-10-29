package algorithm;

import utils.Paths;

import static utils.DataUtils.getElapsedTimeMilli;
import static utils.FileUtils.saveBigNumberToFile;

public class SequentialAdder
{
    public static void compute(byte[] number1, byte[] number2, byte[] sum)
    {
        int length = number1.length;

        byte carry = 0;
        int digitSum;
        for (int i = 0; i < length; i++)
        {
            digitSum = number1[i] + number2[i] + carry;
            sum[i] = (byte) (digitSum % 10);
            carry = (byte) (digitSum / 10);
        }
        if (carry != 0) sum[length] = carry;

    }

    public static double run(byte[] number1, byte[] number2)
    {
        int maxLength = number1.length + 1;
        byte[] sum = new byte[maxLength];

        long startTime = System.nanoTime();

        compute(number1, number2, sum);

        long endTime = System.nanoTime();
        double elapsedTime = getElapsedTimeMilli(startTime, endTime);

        saveBigNumberToFile(sum, Paths.SEQUENTIAL_SUM);

        return elapsedTime;
    }
}
