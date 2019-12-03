package worker.node_sync;

import structure.MyConcurrentLinkedQueue;
import structure.Term;
import structure.TermSortedLinkedList;

import static utils.Utils.anyAlive;

public class InsertOnlyThread extends Thread
{
    private final Thread[] readThreads;
    private final MyConcurrentLinkedQueue<Term> termQueue;
    private final TermSortedLinkedList linkedList;

    public InsertOnlyThread(Thread[] readThreads,
                            TermSortedLinkedList linkedList,
                            MyConcurrentLinkedQueue<Term> termQueue)
    {
        this.readThreads = readThreads;
        this.linkedList = linkedList;
        this.termQueue = termQueue;
    }

    @Override
    public void run()
    {
        while (anyAlive(readThreads) || !termQueue.isEmpty())
        {
            Term term = termQueue.poll();
            if (term != null)
            {
                linkedList.nodeSyncInsert(term);
            }
        }
    }
}
