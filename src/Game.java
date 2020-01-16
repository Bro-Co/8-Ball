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
        final double X_INCREMENT = (double) WIDTH / GRID, Y_INCREMENT = (double) HEIGHT / GRID;
        int i = 0;
        for (double x = X_INCREMENT / 2; x <= WIDTH - X_INCREMENT/2; x += X_INCREMENT) {
            for (double y = Y_INCREMENT / 2; y <= HEIGHT - Y_INCREMENT/2; y += Y_INCREMENT) {
                balls[i] = new Ball(
                        x,
                        y,
                        (Math.random() - 0.5) * 200,
                        (Math.random() - 0.5) * 200,
                        (Math.random()) * 9 + 2,
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
        long one = System.nanoTime();

        super.paintComponent(g);
        for (Ball b : balls) {
            b.displayBall(g);
        }
        repaint();

        long two = System.nanoTime();

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

        long three = System.nanoTime();

        for (Ball b : balls) {
            b.applyTime(remainingTime);
        }
        currentTime += remainingTime;

        int percent = (int) ((currentTime - (System.nanoTime() - START_TIME)) / Math.pow(10, 7) * FPS);
        for (int i = 0; i < 100; i++) {
            System.out.print(i <= percent ? "#" : ".");
        }

        long four = System.nanoTime();

        System.out.printf(" %d %d %d\n", two - one, three - two, four - three);
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
                        new Ball[] {b, b2}
                ));
            }
        }
    }
}
