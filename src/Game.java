import javax.swing.*;
import java.awt.*;
import java.util.PriorityQueue;

public class Game extends JPanel
{
    private long nextFrameTime = 0;
    private final int DIAMETER = 20, FPS = 60;
    private final long START_TIME = System.nanoTime(), TIME_INCREMENT = (long)Math.pow(10, 9)/FPS;
    private Ball[] balls = {
            new Ball(150, 200, DIAMETER, new int[]{255, 0, 0}),
            new Ball(450, 200, DIAMETER, new int[]{255, 0, 0})
    };
    private PriorityQueue<Collision> collisions = new PriorityQueue<>();

    public Game()
    {
        balls[0].applyVel(50, 0);
        for (int i = 0; i < balls.length-1; i++) {
            for (int j = i+1; j < balls.length; j++) {
                
            }
        }
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(600,400);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for (Ball b: balls) {
            b.displayBall(g);
        }
        repaint();

        for (Ball b: balls) {
            b.applyTime(TIME_INCREMENT);
        }

        nextFrameTime += TIME_INCREMENT;
        System.out.println(nextFrameTime-(System.nanoTime()-START_TIME));
        while (System.nanoTime()-START_TIME < nextFrameTime);
    }
}
