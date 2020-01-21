public class Collision implements Comparable<Collision>
{
    protected Long when;
    protected Ball[] balls;
    protected int hits;

    public Collision()
    {
        when = Long.MAX_VALUE;
    }

    public Long getWhen()
    {
        return when;
    }

    public Ball[] getBalls()
    {
        return balls;
    }

    public int calculateHits()
    {
        int sum = 0;
        for (Ball b : balls) {
            sum += b.getHits();
        }
        return sum;
    }

    public boolean isValid()
    {
        return calculateHits() == hits;
    }

    public void handleCollision() { }

    @Override
    public int compareTo(Collision c) {
        return when.compareTo(c.getWhen());
    }
}

