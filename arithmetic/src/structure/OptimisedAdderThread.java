package structure;

import static algorithm.OptimisedParallelAdder.isEmpty;
import static utils.DataUtils.bigNumberToString;

public class OptimisedAdderThread extends Thread
{
    private int threadId;
    private byte[] number1;
    private byte[] number2;
    private int right;
    private int left;
    private byte[] sum;
    private byte[] carryFlags;

    public OptimisedAdderThread(int threadId,
                                byte[] number1,
                                byte[] number2,
                                byte[] sum,
                                int left,
                                int right,
                                byte[] carryFlags)
    {
        this.threadId = threadId;
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
        int digitSum;
        byte carry = 0;
        for (int i = left; i < right; i++)
        {
            digitSum = number1[i] + number2[i] + carry;
            sum[i] = (byte) (digitSum % 10);
            carry = (byte) (digitSum / 10);
        }

        if (threadId != carryFlags.length - 1)
            carryFlags[threadId] = carry;
        else
        {
            carryFlags[threadId] = 0;
            sum[right] = carry;
        }

        if (threadId == 0)
            return;

        while (!isEmpty(carryFlags))
        {
            if (carryFlags[threadId - 1] == 1)
            {
                carry = 1;
                carryFlags[threadId - 1] = 0;

                for (int i = left; i < right; i++)
                {
                    digitSum = sum[i] + carry;
                    sum[i] = (byte) (digitSum % 10);
                    carry = (byte) (digitSum / 10);

                    if (carry == 0)
                        break;
                }

                if (threadId != carryFlags.length - 1)
                    carryFlags[threadId] += carry;
                else
                    sum[right] += carry;
            }
        }
    }


}
