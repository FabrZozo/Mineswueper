package de.uniwue.jpp.mineswueper;

import java.util.Objects;

public class Coordinate {
    private int x;private int y;
    public Coordinate(int x, int y) {

        this.x = x;
        this.y = y;
    }

    public Coordinate(Coordinate old, int x, int y) {
        this.x=x+old.x;
        this.y=y+old.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "/" + y + ")";
    }
}
