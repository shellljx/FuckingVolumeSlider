package com.licrafter.library;

/**
 * Created by lijx on 2017/6/14.
 */

public class Point {
    public float x;
    public float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void trans(float x, float y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
