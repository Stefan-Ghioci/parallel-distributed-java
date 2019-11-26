package structure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.stream.Stream;

public class AdderThread extends Thread
{
    private final TermSortedLinkedList linkedList;
    private final Queue<String> polynomialFilenames;

    public AdderThread(Queue<String> polynomialFilenames, TermSortedLinkedList linkedList)
    {
        this.polynomialFilenames = polynomialFilenames;
        this.linkedList = linkedList;
    }

    @Override
    public void run()
    {
        String filename = polynomialFilenames.poll();
        while (filename != null)
        {
            try (Stream<String> stream = Files.lines(Paths.get(filename)))
            {
                stream.forEach(line -> {

                    String[] split = line.split(" ");

                    Integer degree = Integer.valueOf(split[0]);
                    Integer coefficient = Integer.valueOf(split[1]);

                    linkedList.listSyncInsert(new Term(degree, coefficient));
                });
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            filename = polynomialFilenames.poll();
        }
    }
}
