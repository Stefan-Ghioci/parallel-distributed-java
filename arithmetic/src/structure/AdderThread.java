package structure;

public class AdderThread extends Thread
{
    private int[] number1;
    private int[] number2;
    private int[] sum;
    private int left;
    private int right;
    private Thread previousThread;

    public AdderThread(int[] number1, int[] number2, int[] sum, int left, int right, Thread previousThread)
    {
        this.number1 = number1;
        this.number2 = number2;
        this.sum = sum;
        this.left = left;
        this.right = right;
        this.previousThread = previousThread;
    }

    @Override
    public void run()
    {

        try
        {
            this.previousThread.join();
        }
        catch (NullPointerException ignored)
        {
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        int carry = sum[left];

        for (int i = left; i < right; i++)
        {
            sum[i] = (number1[i] + number2[i] + carry) % 10;
            carry = (number1[i] + number2[i] + carry) / 10;
        }

        if (carry != 0) sum[right] = carry;
    }
}
