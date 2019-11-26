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


    synchronized void listSyncInsert(Term data)
    {
        if (root == null)
        {
            root = new Node<>(data, null);
        }
        else
        {
            int dataCompareResult = root.getData().compareTo(data);

            if (dataCompareResult < 0)
            {
                root = new Node<>(data, root);
            }
            else if (dataCompareResult == 0)
            {
                Term rootData = root.getData();
                int coefficientSum = rootData.getCoefficient() + data.getCoefficient();

                if (coefficientSum != 0)
                {
                    rootData.setCoefficient(coefficientSum);
                    root.setData(rootData);
                }
                else
                {
                    root = root.getNext();
                }
            }
            else
            {
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

                        if (coefficientSum != 0)
                        {
                            currentData.setCoefficient(coefficientSum);
                            current.setData(currentData);

                        }
                        else
                        {
                            previous.setNext(current.getNext());
                        }

                        return;
                    }

                    previous = current;
                    current = current.getNext();
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
