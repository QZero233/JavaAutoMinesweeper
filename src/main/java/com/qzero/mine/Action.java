package com.qzero.mine;

import java.util.Objects;

public class Action {

    public enum Type{
        OPEN,
        MARK
    }

    private int x;
    private int y;
    private Type type;

    public Action(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Action{" +
                "x=" + x +
                ", y=" + y +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return x == action.x && y == action.y && type == action.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, type);
    }
}
