import javax.swing.*;
import java.awt.*;

class Game extends JPanel
{
    long nextFrameTime = 0;
    final int DIAMETER = 20, FPS = 60;
    final long START_TIME = System.nanoTime(), TIME_INCREMENT = (long)Math.pow(10, 9)/FPS;
    Ball[] balls = {
            new Ball(150, 200, DIAMETER, new int[]{255, 0, 0}),
            new Ball(450, 200, DIAMETER, new int[]{255, 0, 0})
    };

    @Override
    public Dimension getPreferredSize()
    {
        balls[0].applyVel(50, 0);
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
