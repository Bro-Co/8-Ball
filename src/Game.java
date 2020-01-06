import javax.swing.*;
import java.awt.*;

class Game extends JPanel
{
    int nextTick = 0;
    final int DIAM = 20, FPS = 60;
    final long START = System.nanoTime();
    Ball[] balls = {new Ball(150, 200, DIAM, new int[]{255, 0, 0}), new Ball(450, 200, DIAM, new int[]{255, 0, 0})};

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
            b.move();
            b.display(g);
        }
        repaint();

        nextTick += 1000/FPS;
        long sleepTime = nextTick-(System.nanoTime()-START)/1000000;
        System.out.println(sleepTime);
        if (sleepTime >= 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
