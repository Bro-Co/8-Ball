public class WallCollision extends Collision
{
    private boolean sideWall;

    public WallCollision(long w, Ball b, boolean sw, int h)
    {
        when = w;
        balls = new Ball[]{b};
        sideWall = sw;
        hits = h;
    }

    @Override
    public boolean isValid()
    {
        return hits == balls[0].getHits();
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
