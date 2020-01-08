public class Collision implements Comparable<Collision>
{
    protected Long when;
    protected boolean b2b;

    public Long getWhen()
    {
        return when;
    }

    public boolean isB2b()
    {
        return b2b;
    }

    @Override
    public int compareTo(Collision c) {
        return when.compareTo(c.getWhen());
    }
}

