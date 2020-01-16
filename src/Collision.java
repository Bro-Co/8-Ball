public class Collision implements Comparable<Collision>
{
    protected Long when;
    protected Ball[] balls;
    protected int hits;

    public Long getWhen()
    {
        return when;
    }

    public Ball[] getBalls()
    {
        return balls;
    }

    public boolean isValid()
    {
        return true;
    }

    public void handleCollision() { }

    @Override
    public int compareTo(Collision c) {
        return when.compareTo(c.getWhen());
    }
}

