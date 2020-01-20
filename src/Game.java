import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Game extends JPanel
{
    private long currentTime = 0, nextCollisionTime;
    private final int FPS = 50, WIDTH = 600, HEIGHT = 400;
    private final long START_TIME, TIME_INCREMENT = (long) Math.pow(10, 9) / FPS;
    private Ball[] balls = {
            new Ball(200, 200, 100, 0, 25, 255, 0, 0),
            new Ball(400, 175, 0, 0, 25, 0, 0, 0),
            new Ball(400, 225, 0, 0, 25, 0, 0, 0)
    };
    private PriorityQueue<Collision> collisions = new PriorityQueue<>();

    public Game()
    {
        START_TIME = System.nanoTime();
        for (Ball b : balls) {
            scheduleCollisions(b);
        }
        nextCollisionTime = collisions.peek().getWhen();
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(WIDTH, HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for (Ball b : balls) {
            b.displayBall(g);
        }

        repaint();

        long remainingTime = TIME_INCREMENT;
        while (nextCollisionTime <= currentTime + remainingTime) {
            for (Ball b : balls) {
                b.applyTime(nextCollisionTime - currentTime);
            }
            remainingTime -= (nextCollisionTime - currentTime);
            currentTime = nextCollisionTime;

            collisions.peek().handleCollision();
            for (Ball b : collisions.peek().getBalls()) {
                scheduleCollisions(b);
            }

            while (!collisions.peek().isValid()) {
                collisions.poll();
            }
            nextCollisionTime = collisions.peek().getWhen();
        }

        for (Ball b : balls) {
            b.applyTime(remainingTime);
        }
        currentTime += remainingTime;
        while (System.nanoTime() - START_TIME < currentTime);
    }

    public void scheduleCollisions(Ball b)
    {
        if (b.getVel().x > 0) {
            collisions.add(new WallCollision(
                    currentTime + Math.round((WIDTH - b.getRadius() - b.getPos().x) / b.getVel().x * Math.pow(10, 9)),
                    new Ball[] {b},
                    true
            ));
        } else if (b.getVel().x < 0) {
            collisions.add(new WallCollision(
                    currentTime + Math.round((b.getRadius() - b.getPos().x) / b.getVel().x * Math.pow(10, 9)),
                    new Ball[] {b},
                    true
            ));
        }

        if (b.getVel().y > 0) {
            collisions.add(new WallCollision(
                    currentTime + Math.round((HEIGHT - b.getRadius() - b.getPos().y) / b.getVel().y * Math.pow(10, 9)),
                    new Ball[] {b},
                    false
            ));
        } else if (b.getVel().y < 0) {
            collisions.add(new WallCollision(
                    currentTime + Math.round((b.getRadius() - b.getPos().y) / b.getVel().y * Math.pow(10, 9)),
                    new Ball[] {b},
                    false
            ));
        }

        LinkedList<Long> collisionTimes = new LinkedList<>();
        LinkedList<ArrayList<Ball>> ballsInvolved = new LinkedList<>();
        for (Ball b2 : balls) {
            Vector
                    position = b2.getPos().sub(b.getPos()),
                    velocity = b2.getVel().sub(b.getVel());
            double
                    dist = b.getRadius() + b2.getRadius(),
                    discriminant = Math.pow(position.dot(velocity), 2) - velocity.dot(velocity) * (position.dot(position) - Math.pow(dist, 2));

            if (position.dot(velocity) < 0 && discriminant >= 0) {
                long t = currentTime + Math.round(- (position.dot(velocity) + Math.sqrt(discriminant)) / velocity.dot(velocity) * Math.pow(10, 9));
                int p = Collections.binarySearch(collisionTimes, t);
                if (p < 0) {
                    collisionTimes.add(- p - 1, t);
                    ballsInvolved.add(- p - 1, new ArrayList<>());
                    ballsInvolved.get(- p - 1).add(b);
                    ballsInvolved.get(- p - 1).add(b2);
                } else {
                    ballsInvolved.get(p).add(b2);
                }
            }
        }

        for (int i = 0; i < collisionTimes.size(); i++) {
            collisions.add(new BallCollision(
                    collisionTimes.get(i),
                    ballsInvolved.get(i).toArray(new Ball[ballsInvolved.get(i).size()])
            ));
        }
    }
}
