public class WallCollision extends Collision
{
    private int ball;
    private boolean sideWall;

    public WallCollision(long w, int b, boolean sw)
    {
        when = w;
        ball = b;
        sideWall = sw;
        b2b = false;
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
