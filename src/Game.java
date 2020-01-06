import javax.swing.*;
import java.awt.*;

class Game extends JPanel
{
    long ticks = 0;
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
            b.display(g);
        }
        repaint();

        for (Ball b: balls) {
            b.applyTime((long)Math.pow(10, 9)/FPS);
        }

        ticks += Math.pow(10, 9)/FPS;
        System.out.println(ticks-(System.nanoTime()-START));
        long end = 0;
        do {
            end = System.nanoTime();
        } while (end-START < ticks);
    }
}
