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
    private final MyConcurrentLinkedQueue<String> filenames;
    private final MyConcurrentLinkedQueue<Term> termQueue;

    public ReadOnlyThread(MyConcurrentLinkedQueue<String> filenames, MyConcurrentLinkedQueue<Term> termQueue)
    {
        this.filenames = filenames;
        this.termQueue = termQueue;
    }

    @Override
    public void run()
    {
        String filename = filenames.poll();
        while (filename != null)
        {
            try (Stream<String> stream = Files.lines(Paths.get(filename)))
            {
                stream.forEach(line -> {

                    Integer degree = Integer.valueOf(line.split(" ")[0]);
                    Integer coefficient = Integer.valueOf(line.split(" ")[1]);

                    Term term = new Term(degree, coefficient);
                    termQueue.add(term);
                });
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            filename = filenames.poll();
        }
    }
}
