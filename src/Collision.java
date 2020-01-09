public class Collision implements Comparable<Collision>
{
    public Long when;
    public boolean b2b;

    @Override
    public int compareTo(Collision c) {
        return when.compareTo(c.getWhen());
    }
}

