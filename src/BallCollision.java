public class BallCollision extends Collision
{
    public int ball1, ball2;

    public BallCollision(long w, int b1, int b2, int h)
    {
        when = w;
        ball1 = b1;
        ball2 = b2;
        b2b = true;
        hits = h;
    }

    public int getBall1()
    {
        return ball1;
    }

    public int getBall2()
    {
        return ball2;
    }
}
