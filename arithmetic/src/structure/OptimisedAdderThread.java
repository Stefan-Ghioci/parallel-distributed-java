package structure;

import static utils.DataUtils.isEmpty;

public class OptimisedAdderThread extends Thread
{
    private int threadId;
    private int[] number1;
    private int[] number2;
    private int right;
    private int left;
    private int[] sum;
    private int[] carryFlags;

    public OptimisedAdderThread(int threadId,
                                int[] number1,
                                int[] number2,
                                int[] sum,
                                int left,
                                int right,
                                int[] carryFlags)
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
        int carry = 0;
        for (int i = left; i < right; i++)
        {
            int digitSum = number1[i] + number2[i] + carry;
            sum[i] = digitSum % 10;
            carry = digitSum / 10;
        }

        passOverCarry(carry);

        while (!isEmpty(carryFlags))
        {
            if (carryFlags[threadId] == 1)
            {
                carry = carryFlags[threadId];
                for (int i = left; i < right; i++)
                {
                    int digitSum = sum[i] + carry;
                    sum[i] = digitSum % 10;
                    carry = digitSum / 10;
                    if (carry == 0)
                        break;
                }
                carryFlags[threadId] = 0;
                passOverCarry(carry);
            }
        }
    }

    private void passOverCarry(int carry)
    {
        try
        {
            carryFlags[threadId + 1] = carry;
        }
        catch (IndexOutOfBoundsException ignored)
        {
            sum[right] = carry;
        }
    }

}
