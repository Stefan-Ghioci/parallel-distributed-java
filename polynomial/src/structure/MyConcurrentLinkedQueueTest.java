package structure;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyConcurrentLinkedQueueTest
{
    static class AddTestThread extends Thread
    {
        private final MyConcurrentLinkedQueue<Integer> queue;

        public AddTestThread(MyConcurrentLinkedQueue<Integer> queue)
        {
            this.queue = queue;
        }

        @Override
        public void run()
        {
            queue.add((int) currentThread().getId());
        }

    }

    static class PollTestThread extends Thread
    {
        private final MyConcurrentLinkedQueue<Integer> queue;
        private final List<Integer> collected;

        public PollTestThread(List<Integer> collected, MyConcurrentLinkedQueue<Integer> queue)
        {
            this.collected = collected;
            this.queue = queue;
        }

        @Override
        public void run()
        {
            Integer element = queue.poll();
            while (element != null)
            {
                collected.add(element);
                element = queue.poll();
            }
        }

    }

    private final MyConcurrentLinkedQueue<Integer> queue = new MyConcurrentLinkedQueue<>();


    @Test
    void add() throws InterruptedException
    {
        Thread[] threads = new Thread[10];
        Arrays.setAll(threads, i -> new AddTestThread(queue));

        for (Thread thread : threads) thread.start();
        for (Thread thread : threads) thread.join();

        assertEquals(threads.length, queue.size());
    }

    @Test
    void poll() throws InterruptedException
    {
        int elementsCount = 10000;
        int threadsCount = 8;

        IntStream.range(0, elementsCount).forEach(queue::add);

        List<List<Integer>> collected = IntStream.range(0, threadsCount)
                .<List<Integer>>mapToObj(i -> new ArrayList<>())
                .collect(Collectors.toList());

        Thread[] threads = new Thread[threadsCount];

        Arrays.setAll(threads, i -> new PollTestThread(collected.get(i), queue));

        for (Thread thread : threads) thread.start();
        for (Thread thread : threads) thread.join();

        int total = collected.stream()
                .mapToInt(List::size)
                .sum();

        assertEquals(elementsCount, total);
    }
}