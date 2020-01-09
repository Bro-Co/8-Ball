import java.awt.*;

public class Ball
{
    private double xPos, yPos, rad, xVel,  yVel;
    private Color col;

    public Ball(int x, int y, int r, int[] rgb)
    {
        xPos = x;
        yPos = y;
        rad = r;
        xVel = 0;
        yVel = 0;
        col = new Color(rgb[0], rgb[1], rgb[2]);
    }

    public double getxPos()
    {
        return xPos;
    }

    public double getyPos()
    {
        return yPos;
    }

    public double getxVel()
    {
        return xVel;
    }

    public double getyVel()
    {
        return yVel;
    }

    public void applyVel(double x, double y)
    {
        xVel = x;
        yVel = y;
    }

    public void applyTime(long t)
    {
        xPos += xVel*t/Math.pow(10, 9);
        yPos += yVel*t/Math.pow(10, 9);
    }

    public void displayBall(Graphics g)
    {
        g.setColor(col);
        g.fillOval(
                (int)Math.round(xPos-rad),
                (int)Math.round(yPos-rad),
                (int)Math.round(rad*2),
                (int)Math.round(rad*2)
        );
    }
}
