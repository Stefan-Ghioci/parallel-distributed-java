package structure;

import java.util.List;

public class MultiplierThread extends Thread
{
    private final List<byte[]> partialProducts;
    private byte[] multiplicand;
    private byte[] multiplier;
    private int left;
    private int right;

    public MultiplierThread(byte[] multiplicand, byte[] multiplier, int left, int right, List<byte[]> partialProducts)
    {
        this.multiplicand = multiplicand;
        this.multiplier = multiplier;
        this.left = left;
        this.right = right;
        this.partialProducts = partialProducts;
    }

    @Override
    public void run()
    {
        for (int i = left; i < right; i++)
        {
            byte[] partialProduct = new byte[multiplicand.length + multiplier.length];

            byte carry = 0;

            for (int j = 0; j < multiplicand.length; j++)
            {
                int digitProduct = multiplicand[j] * multiplier[i] + carry;
                partialProduct[i + j] = (byte) (digitProduct % 10);
                carry = (byte) (digitProduct / 10);
            }
            if (carry != 0) partialProduct[i + multiplicand.length] = carry;

            synchronized (partialProducts)
            {
                partialProducts.add(partialProduct);
            }
        }
    }
}
