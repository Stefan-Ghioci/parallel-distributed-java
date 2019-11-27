package structure;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TermSortedLinkedList
{
    private Node<Term> root;

    public TermSortedLinkedList()
    {
    }


    public synchronized void listSyncInsert(Term data)
    {
        if (root == null)
        {
            root = new Node<>(data, null);
            return;
        }

        int dataCompareResult = root.getData().compareTo(data);

        if (dataCompareResult < 0)
        {
            root = new Node<>(data, root);
            return;
        }

        if (dataCompareResult == 0)
        {
            Term rootData = root.getData();
            int coefficientSum = rootData.getCoefficient() + data.getCoefficient();

            if (coefficientSum == 0)
            {
                root = root.getNext();
                return;
            }

            rootData.setCoefficient(coefficientSum);
            root.setData(rootData);

            return;
        }

        Node<Term> previous = root;
        Node<Term> current = root.getNext();

        while (true)
        {
            if (current == null)
            {
                previous.setNext(new Node<>(data, null));
                return;
            }

            dataCompareResult = current.getData().compareTo(data);

            if (dataCompareResult < 0)
            {
                current = new Node<>(data, current);
                previous.setNext(current);
                return;
            }

            if (dataCompareResult == 0)
            {
                Term currentData = current.getData();
                int coefficientSum = currentData.getCoefficient() + data.getCoefficient();

                if (coefficientSum == 0)
                {
                    previous.setNext(current.getNext());
                    return;
                }

                currentData.setCoefficient(coefficientSum);
                current.setData(currentData);

                return;
            }

            previous = current;
            current = current.getNext();
        }
    }

    public void nodeSyncInsert(Term data)
    {
        if (root == null)
            synchronized (this)
            {
                if (root == null)
                {
                    root = new Node<>(data, null);
                    return;
                }
            }

        synchronized (root)
        {
            int dataCompareResult = root.getData().compareTo(data);

            if (dataCompareResult < 0)
            {
                root = new Node<>(data, root);
                return;
            }

            if (dataCompareResult == 0)
            {
                Term rootData = root.getData();
                int coefficientSum = rootData.getCoefficient() + data.getCoefficient();

                if (coefficientSum == 0)
                {
                    root = root.getNext();
                    return;
                }

                rootData.setCoefficient(coefficientSum);
                root.setData(rootData);
                return;
            }
        }


        Node<Term> previous = root;

        while (true)
        {
            synchronized (previous)
            {
                if (previous.getNext() == null)
                {
                    previous.setNext(new Node<>(data, null));
                    return;
                }

                Node<Term> current = previous.getNext();

                synchronized (current)
                {
                    int dataCompareResult = current.getData().compareTo(data);

                    if (dataCompareResult < 0)
                    {
                        current = new Node<>(data, current);
                        previous.setNext(current);
                        return;
                    }

                    if (dataCompareResult == 0)
                    {
                        Term nextData = current.getData();
                        int coefficientSum = nextData.getCoefficient() + data.getCoefficient();

                        if (coefficientSum == 0)
                        {
                            previous.setNext(current.getNext());
                            return;
                        }

                        nextData.setCoefficient(coefficientSum);
                        current.setData(nextData);

                        return;

                    }

                    previous = current;
                }


            }

        }

    }


    public void writePolynomialToFile(String filename)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false)))
        {
            Node<Term> current = root;

            while (current.getNext() != null)
            {
                writer.write(current.getData().toString() + " + ");

                current = current.getNext();
            }

            writer.write(current.getData().toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
