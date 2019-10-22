package structure;

import java.util.Map;

public class AdderThread extends Thread
{
    private byte[] number1;
    private byte[] number2;
    private int left;
    private int right;
    private byte[] sum;
    private Map<Integer, Byte> carryFlags;

    public AdderThread(byte[] number1,
                       byte[] number2,
                       byte[] sum,
                       int left,
                       int right,
                       Map<Integer, Byte> carryFlags)
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
        byte carry = 0;
        for (int i = left; i < right; i++)
        {
            int digitSum = number1[i] + number2[i] + carry;
            sum[i] = (byte) (digitSum % 10);
            carry = (byte) (digitSum / 10);
        }
        carryFlags.put(right, carry);
    }
}
