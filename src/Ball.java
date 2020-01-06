import java.awt.*;

class Ball
{
    private double xPos, yPos, diam, vel, dir;
    private Color col;

    public Ball(int x, int y, int d, int[] rgb)
    {
        xPos = x;
        yPos = y;
        diam = d;
        vel = 1;
        dir = 0;
        col = new Color(rgb[0], rgb[1], rgb[2]);
    }

    public void move()
    {
        xPos += vel*Math.cos(dir);
        yPos += vel*Math.sin(dir);
    }

    public void display(Graphics g)
    {
        g.setColor(col);
        g.fillOval(
                (int)Math.round(xPos-diam/2),
                (int)Math.round(yPos-diam/2),
                (int)Math.round(diam),
                (int)Math.round(diam)
        );
    }
}
