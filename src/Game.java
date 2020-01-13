import javax.swing.*;
import java.awt.*;
import java.util.PriorityQueue;

public class Game extends JPanel
{
    private long currentTime = 0, nextCollisionTime;
    private final double RADIUS = 50, FPS = 60, WIDTH = 1000, HEIGHT = 500;
    private final long START_TIME = System.nanoTime(), TIME_INCREMENT = (long)(Math.pow(10, 9)/FPS);
    private Ball[] balls = {
            new Ball(500, 250, RADIUS, new int[]{255, 0, 0})
    };
    private PriorityQueue<Collision> collisions = new PriorityQueue<>();

    public Game()
    {
        balls[0].applyVel(500, 250);

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

            if (!collisions.peek().isB2b()) {
                WallCollision c = (WallCollision) collisions.peek();
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

            collisions.poll();
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
            System.out.printf("right side: %d + ((%f - %f - %f) / %f * %f) = %d\n", currentTime, WIDTH, RADIUS, balls[b].getxPos(), balls[b].getxVel(), Math.pow(10, 9), currentTime + Math.round((WIDTH - RADIUS - balls[b].getxPos()) / balls[b].getxVel() * Math.pow(10, 9)));
        } else if (balls[b].getxVel() < 0) {
            collisions.add(new WallCollision(
                    currentTime + Math.round((RADIUS - balls[b].getxPos()) / balls[b].getxVel() * Math.pow(10, 9)),
                    b,
                    true,
                    balls[b].getHits()
            ));
            System.out.printf("left side: %d + ((%f - %f) / %f * %f) = %d\n", currentTime, RADIUS, balls[b].getxPos(), balls[b].getxVel(), Math.pow(10, 9), currentTime + Math.round((RADIUS - balls[b].getxPos()) / balls[b].getxVel() * Math.pow(10, 9)));
        }

        if (balls[b].getyVel() > 0) {
            collisions.add(new WallCollision(
                    currentTime + Math.round((HEIGHT - RADIUS - balls[b].getyPos()) / balls[b].getyVel() * Math.pow(10, 9)),
                    b,
                    false,
                    balls[b].getHits()
            ));
            System.out.printf("bottom: %d + ((%f - %f - %f) / %f * %f) = %d\n", currentTime, HEIGHT, RADIUS, balls[b].getyPos(), balls[b].getyVel(), Math.pow(10, 9), currentTime + Math.round((HEIGHT - RADIUS - balls[b].getyPos()) / balls[b].getyVel() * Math.pow(10, 9)));
        } else if (balls[b].getyVel() < 0) {
            collisions.add(new WallCollision(
                    currentTime + Math.round((RADIUS - balls[b].getyPos()) / balls[b].getyVel() * Math.pow(10, 9)),
                    b,
                    false,
                    balls[b].getHits()
            ));
            System.out.printf("top: %d + ((%f - %f) / %f * %f) = %d\n", currentTime, RADIUS, balls[b].getyPos(), balls[b].getyVel(), Math.pow(10, 9), currentTime + Math.round((RADIUS - balls[b].getyPos()) / balls[b].getyVel() * Math.pow(10, 9)));
        }

        System.out.println();
    }
}
