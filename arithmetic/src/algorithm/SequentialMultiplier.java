package algorithm;

import utils.Paths;

import java.util.ArrayList;
import java.util.List;

import static utils.DataUtils.getElapsedTimeMilli;
import static utils.FileUtils.saveBigNumberToFile;

public class SequentialMultiplier
{
    private static void compute(byte[] number1, byte[] number2, byte[] product)
    {
        byte[] multiplicand, multiplier;

        if (number2.length < number1.length)
        {
            multiplicand = number1;
            multiplier = number2;
        }
        else
        {
            multiplicand = number2;
            multiplier = number1;
        }

        List<byte[]> partialProducts = new ArrayList<>();

        for (int i = 0; i < multiplier.length; i++)
        {
            byte[] partialProduct = new byte[product.length];

            byte carry = 0;

            for (int j = 0; j < multiplicand.length; j++)
            {
                int digitProduct = multiplicand[j] * multiplier[i] + carry;
                partialProduct[i + j] = (byte) (digitProduct % 10);
                carry = (byte) (digitProduct / 10);
            }
            if (carry != 0) partialProduct[i + multiplicand.length] = carry;

            partialProducts.add(partialProduct);
        }

        addAllNumbers(product, partialProducts);
    }

    static void addAllNumbers(byte[] sum, List<byte[]> numbers)
    {
        System.arraycopy(numbers.get(0), 0, sum, 0, sum.length);

        for (int i = 1, numbersSize = numbers.size(); i < numbersSize; i++)
        {
            SequentialAdder.compute(sum, numbers.get(i), sum);
        }
    }

    public static double run(byte[] number1, byte[] number2)
    {
        int maxLength = number1.length + number2.length;
        byte[] product = new byte[maxLength];

        long startTime = System.nanoTime();

        compute(number1, number2, product);

        long endTime = System.nanoTime();
        double elapsedTime = getElapsedTimeMilli(startTime, endTime);

        saveBigNumberToFile(product, Paths.SEQUENTIAL_PRODUCT);

        return elapsedTime;
    }
}
