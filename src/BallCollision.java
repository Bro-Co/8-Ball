public class BallCollision extends Collision
{
    public Ball ball1, ball2;

    public BallCollision(long w, Ball b1, Ball b2, int h)
    {
        when = w;
        ball1 = b1;
        ball2 = b2;
        hits = h;
        b2b = true;
    }

    public Ball getBall1()
    {
        return ball1;
    }

    public Ball getBall2()
    {
        return ball2;
    }

    @Override
    public boolean isValid()
    {
        return hits == ball1.getHits() + ball2.getHits();
    }
}
