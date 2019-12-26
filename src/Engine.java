
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Engine extends JPanel
{
    int x = 200, y = 0;
    int velX = 0, velY = 1;

    private void move()
    {
        x += velX;
        y += velY;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        g.fillOval(x, y, 200, 200);
    }

    public static void main(String[] args) throws InterruptedException
    {
        JFrame jf = new JFrame("Physics Engine");
        Engine eng = new Engine();
        jf.add(eng);
        jf.setSize(400, 400);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for (int i = 0; i < 1000; i++) {
            eng.move();
            eng.repaint();
            Thread.sleep(100);
        }
    }
}