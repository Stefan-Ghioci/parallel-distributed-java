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

    public MyConcurrentLinkedQueue(MyConcurrentLinkedQueue<T> original)
    {
        this.size = original.size;

        if (original.head == null)
        {
            this.head = this.tail = null;
        }
        else
        {
            this.head = new Node<>(original.head.getData(), null);

            Node<T> current = this.head;
            Node<T> originalCurrent = original.head;

            while (originalCurrent.getNext() != null)
            {
                originalCurrent = originalCurrent.getNext();
                current.setNext(new Node<>(originalCurrent.getData(), null));
                current = current.getNext();
            }

            this.tail = current;
        }
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
