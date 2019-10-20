package structure;

public class AdderThread extends Thread
{
    private int[] number1;
    private int[] number2;
    private int[] sum;
    private int left;
    private int right;
    private int[] carryFlags;

    public AdderThread(int[] number1,
                       int[] number2,
                       int[] sum,
                       int left,
                       int right,
                       int[] carryFlags)
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
        for (int i = left; i < right; i++)
        {
            int digitSum = number1[i] + number2[i];
            sum[i] = digitSum % 10;
            if (digitSum / 10 != 0)
                carryFlags[i + 1] = 1;
        }
    }
}
