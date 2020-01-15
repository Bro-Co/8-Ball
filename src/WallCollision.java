public class WallCollision extends Collision
{
    private Ball ball;
    private boolean sideWall;

    public WallCollision(long w, Ball b, boolean sw, int h)
    {
        when = w;
        ball = b;
        sideWall = sw;
        hits = h;
        b2b = false;
    }

    public Ball getBall()
    {
        return ball;
    }

    public boolean isSideWall()
    {
        return sideWall;
    }

    @Override
    public boolean isValid()
    {
        return hits == ball.getHits();
    }
}
