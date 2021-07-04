package com.bramerlabs.cellular;

import java.awt.*;
import java.util.ArrayList;

public class Cell {

    public Vector2f position;
    public Vector2f velocity;
    public Vector2f acceleration;
    public Vector2f friction;
    public Vector2f force;

    public float mouseForceMagnitude = 10;

    public int radius = 10;
    public Color color = new Color(79, 208, 136);

    public float maxDistance = 100;
    public float mass = 1.0f;
    public float mu = 0.03f;

    public Cell(Vector2f position) {
        this.position = position;
        this.velocity = new Vector2f(0, 0);
        this.acceleration = new Vector2f(0, 0);
        this.friction = new Vector2f(0, 0);
        this.force = new Vector2f(0, 0);
    }

    public void update(ArrayList<Cell> cells) {
        for (Cell cell : cells) {
            Vector2f normal = Vector2f.subtract(this.position, cell.position);
            float distance = Vector2f.length(normal);
            float magnitude = mouseForceMagnitude / (distance * distance);
            if (distance < maxDistance) {
                force = Vector2f.add(force, Vector2f.scale(normal, magnitude));
            }
        }

        acceleration = Vector2f.scale(force, 1/mass);
        velocity = Vector2f.add(velocity, acceleration);

        friction = Vector2f.scale(velocity, -mu);
        acceleration = Vector2f.scale(friction, 1/mass);
        velocity = Vector2f.add(velocity, acceleration);

        position = Vector2f.add(position, velocity);

        // check position bound
        if (position.x < 0) {
            position.x = Main.windowSize.width;
        }
        if (position.x > Main.windowSize.width) {
            position.x = 0;
        }
        if (position.y < 0) {
            position.y = Main.windowSize.height;
        }
        if (position.y > Main.windowSize.height) {
            position.y = 0;
        }

    }

    public void paint(Graphics g) {
        g.setColor(color);
        g.fillOval((int) (position.x - radius), (int) (position.y - radius), 2 * radius, 2 * radius);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval((int) (position.x - radius), (int) (position.y - radius), 2 * radius, 2 * radius);

        float forceDrawLine = 100;
        g2d.drawLine((int) position.x, (int) position.y, (int) (position.x + forceDrawLine * force.x), (int) (position.y + forceDrawLine * force.y));
        System.out.println(force);
    }

}
