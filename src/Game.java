import javax.swing.*;
import java.awt.*;
import java.util.PriorityQueue;

public class Game extends JPanel
{
    private long currentTime = 0, nextCollisionTime;
    private final double RADIUS = 50, FPS = 50, WIDTH = 1000, HEIGHT = 500;
    private final long START_TIME = System.nanoTime(), TIME_INCREMENT = (long)(Math.pow(10, 9)/FPS);
    private Ball[] balls = {
            new Ball(250, 250, RADIUS, new int[]{255, 0, 0}),
            new Ball(750, 250, RADIUS, new int[]{0, 0, 0})
    };
    private PriorityQueue<Collision> collisions = new PriorityQueue<>();

    public Game()
    {
        balls[0].applyVel(250, 25);

        for (int i = 0; i < balls.length; i++) {
            scheduleCollisions(i);
        }
        nextCollisionTime = collisions.peek().getWhen();
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension((int)Math.round(WIDTH), (int)Math.round(HEIGHT));
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for (Ball b: balls) {
            b.displayBall(g);
        }
        repaint();

        long remainingTime = TIME_INCREMENT;
        while (nextCollisionTime <= currentTime+remainingTime) {
            for (Ball b : balls) {
                b.applyTime(nextCollisionTime-currentTime);
            }
            remainingTime -= (nextCollisionTime-currentTime);
            currentTime = nextCollisionTime;

            if (collisions.peek().isB2b()) {
                BallCollision c = (BallCollision) collisions.poll();
                if (c.getHits() == balls[c.getBall1()].getHits() + balls[c.getBall2()].getHits()) {
                    double
                            rx = balls[c.getBall2()].getxPos()-balls[c.getBall1()].getxPos(),
                            ry = balls[c.getBall2()].getyPos()-balls[c.getBall1()].getyPos(),
                            vx = balls[c.getBall2()].getxVel()-balls[c.getBall1()].getxVel(),
                            vy = balls[c.getBall2()].getyVel()-balls[c.getBall1()].getyVel(),
                            vr = dot(vx, vy, rx, ry),
                            j = vr / (2 * RADIUS),
                            jx = j * rx / (2 * RADIUS),
                            jy = j * ry / (2 * RADIUS);

                    balls[c.getBall1()].applyVel(jx, jy);
                    balls[c.getBall2()].applyVel(-jx, -jy);
                    balls[c.getBall1()].increaseHits();
                    balls[c.getBall2()].increaseHits();
                    scheduleCollisions(c.getBall1());
                    scheduleCollisions(c.getBall2());
                }
            } else {
                WallCollision c = (WallCollision) collisions.poll();
                if (c.getHits() == balls[c.getBall()].getHits()) {
                    if (c.isSideWall()) {
                        balls[c.getBall()].applyVel(-2*balls[c.getBall()].getxVel(), 0);
                    } else {
                        balls[c.getBall()].applyVel(0, -2*balls[c.getBall()].getyVel());
                    }
                    balls[c.getBall()].increaseHits();
                    scheduleCollisions(c.getBall());
                }
            }
            nextCollisionTime = collisions.peek().getWhen();
        }

        for (Ball b : balls) {
            b.applyTime(remainingTime);
        }

        currentTime += remainingTime;
        while (System.nanoTime()-START_TIME < currentTime);
    }

    public void scheduleCollisions(int b)
    {
        if (balls[b].getxVel() > 0) {
            collisions.add(new WallCollision(
                    currentTime + Math.round((WIDTH - RADIUS - balls[b].getxPos()) / balls[b].getxVel() * Math.pow(10, 9)),
                    b,
                    true,
                    balls[b].getHits()
            ));
        } else if (balls[b].getxVel() < 0) {
            collisions.add(new WallCollision(
                    currentTime + Math.round((RADIUS - balls[b].getxPos()) / balls[b].getxVel() * Math.pow(10, 9)),
                    b,
                    true,
                    balls[b].getHits()
            ));
        }

        if (balls[b].getyVel() > 0) {
            collisions.add(new WallCollision(
                    currentTime + Math.round((HEIGHT - RADIUS - balls[b].getyPos()) / balls[b].getyVel() * Math.pow(10, 9)),
                    b,
                    false,
                    balls[b].getHits()
            ));
        } else if (balls[b].getyVel() < 0) {
            collisions.add(new WallCollision(
                    currentTime + Math.round((RADIUS - balls[b].getyPos()) / balls[b].getyVel() * Math.pow(10, 9)),
                    b,
                    false,
                    balls[b].getHits()
            ));
        }

        for (int b2 = 0; b2 < balls.length; b2++) {
            double
                    rx = balls[b2].getxPos()-balls[b].getxPos(),
                    ry = balls[b2].getyPos()-balls[b].getyPos(),
                    vx = balls[b2].getxVel()-balls[b].getxVel(),
                    vy = balls[b2].getyVel()-balls[b].getyVel(),
                    rr = dot(rx, ry, rx, ry),
                    vv = dot(vx, vy, vx, vy),
                    vr = dot(vx, vy, rx, ry),
                    d = Math.pow(vr, 2) - vv * (rr - Math.pow(RADIUS * 2, 2));

            if (vr < 0 && d >= 0) {
                collisions.add(new BallCollision(
                        currentTime + Math.round(- (vr + Math.sqrt(d)) / vv * Math.pow(10, 9)),
                        b,
                        b2,
                        balls[b].getHits()+balls[b2].getHits()
                ));
            }
        }
    }

    public double dot(double ix, double iy, double jx, double jy)
    {
        return ix*jx + iy*jy;
    }
}
