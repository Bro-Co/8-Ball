public class BallCollision extends Collision
{
    public int ball1, ball2;

    public BallCollision(long w, int b1, int b2)
    {
        when = w;
        ball1 = b1;
        ball2 = b2;
        b2b = true;
    }
}
