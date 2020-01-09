public class WallCollision extends Collision
{
    public int ball;
    public boolean sideWall;

    public WallCollision(long w, int b, boolean sw)
    {
        when = w;
        ball = b;
        sideWall = sw;
        b2b = false;
    }
}
