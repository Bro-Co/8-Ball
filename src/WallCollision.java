public class WallCollision extends Collision
{
    private int ball;
    private boolean sideWall;

    public WallCollision(long w, int b, boolean sw, int h)
    {
        when = w;
        ball = b;
        sideWall = sw;
        b2b = false;
        hits = h;
    }

    public int getBall()
    {
        return ball;
    }

    public boolean isSideWall()
    {
        return sideWall;
    }

    @Override
    public String toString()
    {
        return "[when=" + when + ", ball=" + ball + ", sw=" + sideWall + ", b2b=" + b2b + "]";
    }
}
