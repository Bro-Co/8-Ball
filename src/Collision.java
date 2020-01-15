public class Collision implements Comparable<Collision>
{
    protected Long when;
    protected boolean b2b;
    protected int hits;

    public Long getWhen()
    {
        return when;
    }

    public boolean isB2b()
    {
        return b2b;
    }

    public boolean isValid()
    {
        return true;
    }

    @Override
    public int compareTo(Collision c) {
        return when.compareTo(c.getWhen());
    }
}

