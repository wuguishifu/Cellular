package com.bramerlabs.cellular;

public class Cell {

    public float x, y;
    public float vx, vy;
    public int type;

    public int radius = 10;

    public Cell(float x, float y, float vx, float vy, int type) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.type = type;
    }

}
