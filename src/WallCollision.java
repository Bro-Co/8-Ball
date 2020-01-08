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

    public boolean getSideWall()
    {
        return sideWall;
    }
}
