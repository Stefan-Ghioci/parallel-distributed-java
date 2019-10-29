package structure;

import algorithm.SequentialAdder;

import java.util.List;
import java.util.stream.IntStream;

public class ReducerThread extends Thread
{
    private final List<byte[]> numbers;
    private final int left;
    private final int right;
    private final List<byte[]> reducedNumbers;

    public ReducerThread(List<byte[]> numbers, int left, int right, List<byte[]> reducedNumbers)
    {
        this.numbers = numbers;
        this.left = left;
        this.right = right;
        this.reducedNumbers = reducedNumbers;
    }


    @Override
    public void run()
    {
        byte[] sum = new byte[numbers.get(left).length];

        System.arraycopy(numbers.get(left), 0, sum, 0, sum.length);

        IntStream.range(left + 1, right).forEach(i -> SequentialAdder.compute(sum, numbers.get(i), sum));

        synchronized (reducedNumbers)
        {
            reducedNumbers.add(sum);
        }
    }
}
