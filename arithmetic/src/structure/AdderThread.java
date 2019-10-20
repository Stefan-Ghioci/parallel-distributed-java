package structure;

public class AdderThread extends Thread
{
    private int[] number1;
    private int[] number2;
    private int[] sum;
    private int[] carryFlags;
    private int left;
    private int right;

    public AdderThread(int[] number1, int[] number2, int[] sum, int[] carryFlags, int left, int right)
    {
        this.number1 = number1;
        this.number2 = number2;
        this.sum = sum;
        this.carryFlags = carryFlags;
        this.left = left;
        this.right = right;
    }

    @Override
    public void run()
    {
        int i;
        for (i = left; i < right; i++)
        {
            sum[i] = (number1[i] + number2[i]) % 10;
            carryFlags[i + 1] = (number1[i] + number2[i]) / 10;
        }
    }
}
