package algorithm;

public class SequentialAdder
{
    public static int[] compute(int[] number1, int[] number2)
    {
        int length = number1.length;

        int[] sum = new int[length + 1];

        int i, carry = 0;
        for (i = 0; i < length; i++)
        {
            sum[i] = (number1[i] + number2[i] + carry) % 10;
            carry = (number1[i] + number2[i] + carry) / 10;
        }
        if (carry != 0) sum[i] = carry;

        return sum;
    }
}
