public class Vector
{
    public double x, y;

    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector add(Vector v)
    {
        return new Vector(x + v.x, y + v.y);
    }

    public Vector sub(Vector v)
    {
        return new Vector(x - v.x, y - v.y);
    }

    public Vector scaleUp(double s)
    {
        return new Vector(x * s, y * s);
    }

    public Vector scaleDown(double s)
    {
        return new Vector(x / s, y / s);
    }

    public double dot(Vector v)
    {
        return (x * v.x) + (y * v.y);
    }
}
