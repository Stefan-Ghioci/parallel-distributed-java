package structure;

import java.util.Map;

public class AdderThread extends Thread
{
    private int[] number1;
    private int[] number2;
    private int[] sum;
    private int left;
    private int right;
    private Map<Integer, Integer> carryFlags;

    public AdderThread(int[] number1,
                       int[] number2,
                       int[] sum,
                       int left,
                       int right,
                       Map<Integer, Integer> carryFlags)
    {
        this.number1 = number1;
        this.number2 = number2;
        this.sum = sum;
        this.left = left;
        this.right = right;
        this.carryFlags = carryFlags;
    }

    @Override
    public void run()
    {

        int carry = 0;

        for (int i = left; i < right; i++)
        {
            sum[i] = (number1[i] + number2[i] + carry) % 10;
            carry = (number1[i] + number2[i] + carry) / 10;
        }

        if (carry != 0) carryFlags.put(right, carry);
    }
}
