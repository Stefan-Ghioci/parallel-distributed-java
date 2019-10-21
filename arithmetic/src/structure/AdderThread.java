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
    private Thread previousThread;

    public AdderThread(int[] number1,
                       int[] number2,
                       int[] sum,
                       int left,
                       int right,
                       Map<Integer, Integer> carryFlags, Thread previousThread)
    {
        this.number1 = number1;
        this.number2 = number2;
        this.sum = sum;
        this.left = left;
        this.right = right;
        this.carryFlags = carryFlags;
        this.previousThread = previousThread;
    }

    @Override
    public void run()
    {
        try
        {
            int carry = 0;
            for (int i = left; i < right; i++)
            {
                int digitSum = number1[i] + number2[i] + carry;
                sum[i] = digitSum % 10;
                carry = digitSum / 10;
            }

            carryFlags.put(right, carry);

            previousThread.join();

            carry = carryFlags.get(left);

            int i = left;
            while (carry != 0)
            {
                int digitSum = sum[i] + carry;
                sum[i] = digitSum % 10;
                carry = digitSum / 10;
                i++;
            }
        }
        catch (NullPointerException ignored)
        {
            // first thread will wait for null thread
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
