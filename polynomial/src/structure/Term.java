package structure;

public class Term implements Comparable<Term>
{
    private Integer degree;
    private Integer coefficient;

    public Term(Integer degree, Integer coefficient)
    {
        this.degree = degree;
        this.coefficient = coefficient;
    }

    public Integer getCoefficient()
    {
        return coefficient;
    }

    public void setCoefficient(Integer coefficient)
    {
        this.coefficient = coefficient;
    }

    @Override
    public String toString()
    {
        return coefficient + "*x^" + degree;
    }


    @Override
    public int compareTo(Term o)
    {
        return this.degree.compareTo(o.degree);
    }
}
