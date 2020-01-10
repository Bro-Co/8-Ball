import javax.swing.*;
import java.awt.*;
import java.util.PriorityQueue;

public class Game extends JPanel
{
    private long nextFrameTime = 0;
    private final int RADIUS = 20, FPS = 60, WIDTH = 600, HEIGHT = 400;
    private final long START_TIME = System.nanoTime(), TIME_INCREMENT = (long)Math.pow(10, 9)/FPS;
    private Ball[] balls = {
            new Ball(200, 200, RADIUS, new int[]{255, 0, 0}),
            new Ball(400, 200, RADIUS, new int[]{255, 0, 0})
    };
    private PriorityQueue<Collision> collisions = new PriorityQueue<>();

    public Game()
    {
        balls[0].applyVel(50, 50);
        balls[1].applyVel(-50, -50);

        for (int i = 0; i < balls.length; i++) {
            if (balls[i].getxVel() > 0) {
                collisions.add(new WallCollision(
                        (long) ((WIDTH - RADIUS - balls[i].getxPos()) / balls[i].getxVel() * Math.pow(10, 9)),
                        i,
                        true,
                        balls[i].getHits()
                ));
            } else if (balls[i].getxVel() < 0) {
                collisions.add(new WallCollision(
                        (long) ((RADIUS - balls[i].getxPos()) / balls[i].getxVel() * Math.pow(10, 9)),
                        i,
                        true,
                        balls[i].getHits()
                ));
            }
            if (balls[i].getyVel() > 0) {
                collisions.add(new WallCollision(
                        (long) ((HEIGHT - RADIUS - balls[i].getyPos()) / balls[i].getyVel() * Math.pow(10, 9)),
                        i,
                        false,
                        balls[i].getHits()
                ));
            } else if (balls[i].getyVel() < 0) {
                collisions.add(new WallCollision(
                        (long) ((RADIUS - balls[i].getyPos()) / balls[i].getyVel() * Math.pow(10, 9)),
                        i,
                        false,
                        balls[i].getHits()
                ));
            }
        }

        for (int i = 0; i < balls.length-1; i++) {
            for (int j = 0; j < balls.length; j++) {

            }
        }

        for (Collision c: collisions)
        {
            System.out.println(c);
        }
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(WIDTH,HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for (Ball b: balls) {
            b.displayBall(g);
        }
        repaint();

        if (nextFrameTime >= collisions.peek().getWhen()) {
            
            if (!collisions.peek().isB2b()) {
                WallCollision c = (WallCollision) collisions.poll();
                if (c.getHits() == balls[c.getBall()].getHits()) {
                    if (c.isSideWall()) {
                        balls[c.getBall()].applyVel(-2*balls[c.getBall()].getxVel(), 0);
                    } else {
                        balls[c.getBall()].applyVel(0, -2*balls[c.getBall()].getyVel());
                    }
                    balls[c.getBall()].increaseHits();
                }
            }
        } else {
            for (Ball b : balls) {
                b.applyTime(TIME_INCREMENT);
            }
        }

        nextFrameTime += TIME_INCREMENT;
        //System.out.println(nextFrameTime-(System.nanoTime()-START_TIME));
        while (System.nanoTime()-START_TIME < nextFrameTime);
    }
}
