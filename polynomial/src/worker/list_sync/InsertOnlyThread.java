package worker.list_sync;

import structure.MyConcurrentLinkedQueue;
import structure.Term;
import structure.TermSortedLinkedList;

public class InsertOnlyThread extends Thread
{
    private final Thread readThread;
    private final MyConcurrentLinkedQueue<Term> termQueue;
    private final TermSortedLinkedList linkedList;

    public InsertOnlyThread(Thread readThread,
                            TermSortedLinkedList linkedList,
                            MyConcurrentLinkedQueue<Term> termQueue)
    {
        this.readThread = readThread;
        this.linkedList = linkedList;
        this.termQueue = termQueue;
    }

    @Override
    public void run()
    {
        while (readThread.isAlive() || !termQueue.isEmpty())
        {
            Term term = termQueue.poll();
            if (term != null)
            {
                linkedList.listSyncInsert(term);
//                System.out.println("Thread(" + currentThread().getId() + ") polled " + term);
            }
        }
//        System.out.println("Thread(" + currentThread().getId() + ") finished.");
    }
}
