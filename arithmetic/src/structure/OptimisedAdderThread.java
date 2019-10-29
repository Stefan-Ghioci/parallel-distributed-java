package structure;

public class OptimisedAdderThread extends Thread
{
    private int threadId;
    private byte[] number1;
    private byte[] number2;
    private byte[] sum;
    private int left;
    private int right;
    private byte[] carryFlags;
    private Thread previousThread;

    public OptimisedAdderThread(int threadId,
                                byte[] number1,
                                byte[] number2,
                                byte[] sum,
                                int left,
                                int right,
                                byte[] carryFlags,
                                Thread previousThread)
    {
        this.threadId = threadId;
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
            int digitSum;
            byte carry = 0;
            for (int i = left; i < right; i++)
            {
                digitSum = number1[i] + number2[i] + carry;
                sum[i] = (byte) (digitSum % 10);
                carry = (byte) (digitSum / 10);
            }
            carryFlags[threadId] = carry;

            if (threadId != 0)
            {

                if (carryFlags[threadId - 1] == 0)
                    previousThread.join();

                carry = carryFlags[threadId - 1];

                if (carry != 0)
                {
                    for (int i = left; i < right; i++)
                    {
                        digitSum = sum[i] + carry;
                        sum[i] = (byte) (digitSum % 10);
                        carry = (byte) (digitSum / 10);

                        if (carry == 0)
                            break;
                    }
                    carryFlags[threadId] += carry;
                }
            }

            byte carryFlag = carryFlags[threadId];
            if (threadId + 1 == carryFlags.length && carryFlag != 0)
            {
                sum[right] = carryFlag;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
