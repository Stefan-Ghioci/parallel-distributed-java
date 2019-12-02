package structure;

public class MyConcurrentLinkedQueue<T extends Comparable<T>>
{
    int size;
    Node<T> head;
    Node<T> tail;

    public MyConcurrentLinkedQueue()
    {
        size = 0;
        head = tail = null;
    }

    public synchronized void add(T data)
    {
        Node<T> node = new Node<>(data, null);

        if (head == null)
        {
            head = tail = node;
        }
        else
        {
            tail.setNext(node);
            tail = node;
        }

        size++;
    }

    public synchronized T poll()
    {
        if (head == null)
            return null;

        T data = head.getData();
        head = head.getNext();

        if (head == null)
            tail = null;

        size--;
        return data;
    }

    public synchronized int size()
    {
        return size;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isEmpty()
    {
        return size() == 0;
    }
}
