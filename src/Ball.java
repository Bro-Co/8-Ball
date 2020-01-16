import java.awt.*;

public class Ball
{
    private Vector position, velocity;
    private double radius, mass;
    private Color color;
    private int hits = 0;

    public Ball(double rx, double ry, double vx, double vy, double rad, int r, int g, int b)
    {
        position = new Vector(rx, ry);
        velocity = new Vector(vx, vy);
        radius = rad;
        mass = Math.PI * Math.pow(rad, 2);
        color = new Color(r, g, b);
    }

    public Vector getPos()
    {
        return position;
    }

    public Vector getVel()
    {
        return velocity;
    }

    public double getRadius()
    {
        return radius;
    }

    public double getMass()
    {
        return mass;
    }

    public int getHits()
    {
        return hits;
    }

    public void increaseHits()
    {
        hits++;
    }

    public void reflectX()
    {
        velocity.x = - velocity.x;
    }

    public void reflectY()
    {
        velocity.y = - velocity.y;
    }

    public void applyVel(Vector v)
    {
        velocity = velocity.add(v);
        if (Math.abs(velocity.x) < 0.01) {
            velocity.x = 0;
        }
        if (Math.abs(velocity.y) < 0.01) {
            velocity.y = 0;
        }
    }

    public void applyTime(long t)
    {
        position = position.add(velocity.scaleUp(t / Math.pow(10, 9)));
    }

    public void displayBall(Graphics g)
    {
        g.setColor(color);
        g.fillOval(
                (int) Math.round(position.x - radius),
                (int) Math.round(position.y - radius),
                (int) Math.round(radius * 2),
                (int) Math.round(radius * 2)
        );
    }
}
