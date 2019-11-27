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
        //System.out.println("Thread " + Thread.currentThread().getId() + " - inserting " + data);

        if (root == null)
        {
            root = new Node<>(data, null);
            //System.out.println("Thread " + Thread.currentThread().getId() + " - " + data + " inserted as new root. ");
            return;
        }

        synchronized (root)
        {
            int dataCompareResult = root.getData().compareTo(data);

            if (dataCompareResult < 0)
            {
                root = new Node<>(data, root);
                //System.out.println("Thread " + Thread.currentThread().getId() + " - " + data + " inserted behind root. ");
                return;
            }

            if (dataCompareResult == 0)
            {
                Term rootData = root.getData();
                int coefficientSum = rootData.getCoefficient() + data.getCoefficient();

                if (coefficientSum == 0)
                {
                    root = root.getNext();
                    //System.out.println("Thread " + Thread.currentThread().getId() + " - " + data + " deleted root. ");
                    return;
                }

                rootData.setCoefficient(coefficientSum);
                root.setData(rootData);
                //System.out.println("Thread " + Thread.currentThread().getId() + " - " + data + " added to root. ");

                return;
            }
        }


        Node<Term> previous = root;
        Node<Term> current = root.getNext();

        while (true)
        {
            synchronized (previous)
            {
                try
                {
                    synchronized (current)
                    {


                        int dataCompareResult = current.getData().compareTo(data);

                        if (dataCompareResult < 0)
                        {
                            current = new Node<>(data, current);
                            previous.setNext(current);
                            //System.out.println("Thread " + Thread.currentThread().getId() + " - " + data + " inserted after " + previous.getData() + " and behind " + current.getNext().getData());
                            return;
                        }

                        if (dataCompareResult == 0)
                        {
                            Term nextData = current.getData();
                            int coefficientSum = nextData.getCoefficient() + data.getCoefficient();

                            if (coefficientSum == 0)
                            {
                                previous.setNext(current.getNext());
                                //System.out.println("Thread " + Thread.currentThread().getId() + " - " + data + " deleted " + current.getData());

                                return;
                            }

                            //System.out.println("Thread " + Thread.currentThread().getId() + " - " + data + " added to " + current.getData());

                            nextData.setCoefficient(coefficientSum);
                            current.setData(nextData);

                            return;

                        }

                        previous = current;
                        current = current.getNext();
                    }
                }
                catch (NullPointerException currentNull)
                {
                    previous.setNext(new Node<>(data, null));
                    //System.out.println("Thread " + Thread.currentThread().getId() + " - " + data + " inserted after " + previous.getData());
                    return;
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
