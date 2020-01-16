public class BallCollision extends Collision
{
    public BallCollision(long w, Ball b1, Ball b2, int h)
    {
        when = w;
        balls = new Ball[]{b1, b2};
        hits = h;
    }

    @Override
    public boolean isValid()
    {
        return hits == (balls[0].getHits() + balls[1].getHits());
    }

    @Override
    public void handleCollision()
    {
        Vector
                position = balls[1].getPos().sub(balls[0].getPos()),
                velocity = balls[1].getVel().sub(balls[0].getVel());
        double
                dist = balls[0].getRadius() + balls[1].getRadius(),
                power = 2 * balls[0].getMass() * balls[1].getMass() * position.dot(velocity) / (dist * (balls[0].getMass() + balls[1].getMass()));
        Vector
                impulse = position.scaleUp(power / dist);

        balls[0].applyVel(impulse.scaleDown(balls[0].getMass()));
        balls[1].applyVel(impulse.scaleDown(- balls[1].getMass()));
        balls[0].increaseHits();
        balls[1].increaseHits();
    }
}
