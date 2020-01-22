import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Game extends JPanel
{
    private long currentTime = 0, nextCollisionTime;
    private final int FPS = 50, WIDTH = 500, HEIGHT = 500;
    private final long START_TIME, TIME_INCREMENT = (long) Math.pow(10, 9) / FPS;
    private ArrayList<Ball> balls = new ArrayList<>();
    private PriorityQueue<Collision> collisions = new PriorityQueue<>();

    public Game()
    {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addBall(e.getX(), e.getY());
            }
        });

        collisions.add(new Collision());
        nextCollisionTime = collisions.peek().getWhen();
        START_TIME = System.nanoTime();
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

        int percent = (int) ((currentTime - (System.nanoTime() - START_TIME)) / Math.pow(10, 7) * FPS);
        for (int i = 0; i < 100; i++) {
            System.out.print(i <= percent ? "#" : ".");
        }
        System.out.println();
        while (System.nanoTime() - START_TIME < currentTime);
    }

    public void addBall(double x, double y)
    {
        balls.add(new Ball(
                x,
                y,
                (Math.random() - 0.5) * 200,
                (Math.random() - 0.5) * 200,
                (Math.random()) * 20 + 5,
                (int) (Math.random() * 255),
                (int) (Math.random() * 255),
                (int) (Math.random() * 255)
        ));
        scheduleCollisions(balls.get(balls.size() - 1));
        nextCollisionTime = collisions.peek().getWhen();
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
                    distSquared = Math.pow(b.getRadius() + b2.getRadius(), 2),
                    discriminant = Math.pow(position.dot(velocity), 2) - velocity.dot(velocity) * (position.dot(position) - distSquared);

            if (position.dot(velocity) < 0 && discriminant >= 0) {
                collisions.add(new BallCollision(
                        currentTime + Math.round(- (position.dot(velocity) + Math.sqrt(discriminant)) / velocity.dot(velocity) * Math.pow(10, 9)),
                        new Ball[] {b, b2}
                ));
            }
        }
    }
}
 