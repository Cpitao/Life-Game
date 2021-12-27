package agh.ics.oop.mapparts;

import java.util.Objects;

public class Vector2d {
    public int x;
    public int y;

    public Vector2d(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public String toString()
    {
        return "(" + String.valueOf(this.x) + "," + String.valueOf(this.y) + ")";
    }

    public boolean precedes(Vector2d other)
    {
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(Vector2d other)
    {
        return this.x >= other.x && this.y >= other.y;
    }

    public Vector2d upperRight(Vector2d other)
    {
        int x, y;
        x = Math.max(this.x, other.x);
        y = Math.max(this.y, other.y);

        return new Vector2d(x, y);
    }

    public Vector2d lowerLeft(Vector2d other)
    {
        int x, y;
        x = Math.min(this.x, other.x);
        y = Math.min(this.y, other.y);

        return new Vector2d(x, y);
    }

    public Vector2d add(Vector2d other)
    {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other)
    {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Vector2d)) return false;
        Vector2d that = (Vector2d) o;
        return (Integer.compare(this.x, that.x) == 0
                && Integer.compare(this.y, that.y) == 0);
    }

    public Vector2d opposite()
    {
        return new Vector2d(-this.x, -this.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
