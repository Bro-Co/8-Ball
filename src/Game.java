import javax.swing.*;
import java.awt.*;
import java.util.PriorityQueue;

public class Game extends JPanel
{
    private long currentTime = 0, nextCollisionTime;
    private final int FPS = 50, WIDTH = 500, HEIGHT = 500, GRID = 20;
    private final long START_TIME, TIME_INCREMENT = (long) Math.pow(10, 9) / FPS;
    private Ball[] balls = new Ball[(int) Math.pow(GRID, 2)];
    private PriorityQueue<Collision> collisions = new PriorityQueue<>();

    public Game()
    {
        System.out.println(balls.length);
        final double X_INCREMENT = (double) WIDTH / (GRID + 1), Y_INCREMENT = (double) HEIGHT / (GRID + 1);
        System.out.println(X_INCREMENT);
        int i = 0;
        for (double x = X_INCREMENT; x <= WIDTH - X_INCREMENT; x += X_INCREMENT) {
            for (double y = Y_INCREMENT; y <= HEIGHT - Y_INCREMENT; y += Y_INCREMENT) {
                balls[i] = new Ball(
                        x,
                        y,
                        (Math.random() - 0.5) * 200,
                        (Math.random() - 0.5) * 200,
                        (Math.random()) * 10 + 2,
                        (int) (Math.random() * 255),
                        (int) (Math.random() * 255),
                        (int) (Math.random() * 255)
                );
                i++;
            }
        }

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

            if (collisions.peek().isB2b()) {
                BallCollision c = (BallCollision) collisions.poll();
                Vector
                        position = c.getBall2().getPos().sub(c.getBall1().getPos()),
                        velocity = c.getBall2().getVel().sub(c.getBall1().getVel());
                double
                        dist = c.getBall1().getRadius() + c.getBall2().getRadius(),
                        power = 2 * c.getBall1().getMass() * c.getBall2().getMass() * position.dot(velocity) / (dist * (c.getBall1().getMass() + c.getBall2().getMass()));
                Vector
                        impulse = position.scale(power / dist);

                c.getBall1().applyVel(impulse.scale(1 / c.getBall1().getMass()));
                c.getBall2().applyVel(impulse.scale(-1 / c.getBall2().getMass()));
                c.getBall1().increaseHits();
                c.getBall2().increaseHits();
                scheduleCollisions(c.getBall1());
                scheduleCollisions(c.getBall2());
            } else {
                WallCollision c = (WallCollision) collisions.poll();
                if (c.isSideWall()) {
                    c.getBall().reflectX();
                } else {
                    c.getBall().reflectY();
                }
                c.getBall().increaseHits();
                scheduleCollisions(c.getBall());
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
        System.out.printf("%.0f%%\n", ((currentTime - (System.nanoTime() - START_TIME))/Math.pow(10, 9)) / (1.0 / FPS) * 100);
        while (System.nanoTime() - START_TIME < currentTime);
    }

    public void scheduleCollisions(Ball b)
    {
        if (b.getVel().x > 0) {
            collisions.add(new WallCollision(
                    currentTime + Math.round((WIDTH - b.getRadius() - b.getPos().x) / b.getVel().x * Math.pow(10, 9)),
                    b,
                    true,
                    b.getHits()
            ));
        } else if (b.getVel().x < 0) {
            collisions.add(new WallCollision(
                    currentTime + Math.round((b.getRadius() - b.getPos().x) / b.getVel().x * Math.pow(10, 9)),
                    b,
                    true,
                    b.getHits()
            ));
        }

        if (b.getVel().y > 0) {
            collisions.add(new WallCollision(
                    currentTime + Math.round((HEIGHT - b.getRadius() - b.getPos().y) / b.getVel().y * Math.pow(10, 9)),
                    b,
                    false,
                    b.getHits()
            ));
        } else if (b.getVel().y < 0) {
            collisions.add(new WallCollision(
                    currentTime + Math.round((b.getRadius() - b.getPos().y) / b.getVel().y * Math.pow(10, 9)),
                    b,
                    false,
                    b.getHits()
            ));
        }

        for (Ball b2 : balls) {
            Vector
                    position = b2.getPos().sub(b.getPos()),
                    velocity = b2.getVel().sub(b.getVel());
            double
                    dist = b.getRadius() + b2.getRadius(),
                    discriminant = Math.pow(position.dot(velocity), 2) - velocity.dot(velocity) * (position.dot(position) - Math.pow(dist, 2));

            if (position.dot(velocity) < 0 && discriminant >= 0) {
                collisions.add(new BallCollision(
                        currentTime + Math.round(- (position.dot(velocity) + Math.sqrt(discriminant)) / velocity.dot(velocity) * Math.pow(10, 9)),
                        b,
                        b2,
                        b.getHits() + b2.getHits()
                ));
            }
        }
    }
}
