public class WallCollision extends Collision
{
    private boolean sideWall;

    public WallCollision(long w, Ball[] bs, boolean sw)
    {
        when = w;
        balls = bs;
        sideWall = sw;
        hits = calculateHits();
    }

    @Override
    public void handleCollision()
    {
        if (sideWall) {
            balls[0].reflectX();
        } else {
            balls[0].reflectY();
        }
        balls[0].increaseHits();
    }
}
