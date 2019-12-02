package worker;

import structure.MyConcurrentLinkedQueue;
import structure.Term;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class ReadOnlyThread extends Thread
{
    private final List<String> filenames;
    private final MyConcurrentLinkedQueue<Term> termQueue;

    public ReadOnlyThread(List<String> filenames, MyConcurrentLinkedQueue<Term> termQueue)
    {
        this.filenames = filenames;
        this.termQueue = termQueue;
    }

    @Override
    public void run()
    {
        for (String filename : filenames)
        {
            try (Stream<String> stream = Files.lines(Paths.get(filename)))
            {
                stream.forEach(line -> {

                    Integer degree = Integer.valueOf(line.split(" ")[0]);
                    Integer coefficient = Integer.valueOf(line.split(" ")[1]);

                    Term term = new Term(degree, coefficient);
//                    System.out.println("Thread(" + currentThread().getId() + ") queueing " + term);
                    termQueue.add(term);
                });
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
//        System.out.println("Thread(" + currentThread().getId() + ") finished.");
    }
}
