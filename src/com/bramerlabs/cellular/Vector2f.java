package com.bramerlabs.cellular;

public class Vector2f {

    public float x, y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2f add(Vector2f u, Vector2f v) {
        return new Vector2f(u.x + v.x, u.y + v.y);
    }

    public static Vector2f subtract(Vector2f u, Vector2f v) {
        return new Vector2f(u.x - v.x, u.y - v.y);
    }

    public static Vector2f invert(Vector2f v) {
        return new Vector2f(-v.x, -v.y);
    }

    public static Vector2f scale(Vector2f v, float s) {
        return new Vector2f(v.x * s, v.y * s);
    }

    public static Vector2f normalize(Vector2f v) {
        float length = length(v);
        return new Vector2f(v.x/length, v.y/length);
    }

    public static Vector2f normalize(Vector2f v, float distance) {
        return Vector2f.scale(Vector2f.normalize(v), distance);
    }

    public static float length(Vector2f v) {
        return (float) Math.sqrt(v.x * v.x + v.y * v.y);
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

}
