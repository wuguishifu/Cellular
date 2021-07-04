package com.bramerlabs.cellular;

import java.awt.*;
import java.util.ArrayList;

public class Cell {

    public Vector2f position;
    public Vector2f velocity;
    public Vector2f acceleration;
    public Vector2f friction;
    public Vector2f force;


    public int radius = 10;
    public Color color;

    public float mouseForceMagnitude;
    public float maxDistance;
    public float mass;
    public float mu;
    public int type;

    public Cell(Vector2f position, int type) {
        switch (type) {
            case 1:
                maxDistance = 100;
                mass = 1.0f;
                mu = 0.03f;
                color = new Color(79, 208, 136);
                mouseForceMagnitude = 5f;
                type = 1;
                break;
            case 2:
                maxDistance = 100;
                mass = 1.0f;
                mu = 0.03f;
                color = new Color(187, 64, 107);
                mouseForceMagnitude = -5f;
                type = 2;
                break;
        }
        this.position = position;
        this.velocity = new Vector2f(0, 0);
        this.acceleration = new Vector2f(0, 0);
        this.friction = new Vector2f(0, 0);
        this.force = new Vector2f(0, 0);
    }

    public void update(ArrayList<Cell> cells) {
        for (Cell cell : cells) {
            if (cell != this) {

                Vector2f normal = Vector2f.subtract(this.position, cell.position);
                float distance = Vector2f.length(normal);
                float magnitude = mouseForceMagnitude / (distance * distance);
                if (distance < radius) {
                    float smooth = 2.0f;
                    float forceMagnitude = smooth * radius * 2 * (1.0f/(radius * 2 + smooth) - 1.0f/(distance + smooth));
                    force = Vector2f.scale(normal, forceMagnitude);
                    acceleration = Vector2f.scale(force, 1 / mass);
                    velocity = Vector2f.add(velocity, acceleration);
//                } else if (distance < maxDistance) {
//                    if (this.type == 1 && cell.type == 2) {
//                        force = Vector2f.scale(normal, RGForce);
//                        acceleration = Vector2f.scale(force, 1 / mass);
//                        velocity = Vector2f.add(velocity, acceleration);
//                    } else if (this.type == 2 && cell.type == 1) {
//                        force = Vector2f.scale(normal, RGForce);
//                        acceleration = Vector2f.scale(force, 1 / mass);
//                        velocity = Vector2f.add(velocity, acceleration);
//                    } else if (this.type == 2 && cell.type == 2) {
//                        force = Vector2f.scale(normal, RRForce);
//                        acceleration = Vector2f.scale(force, 1 / mass);
//                        velocity = Vector2f.add(velocity, acceleration);
//                    } else if (this.type == 1 && cell.type == 1) {
//                        force = Vector2f.scale(normal, GGForce);
//                        acceleration = Vector2f.scale(force, 1 / mass);
//                        velocity = Vector2f.add(velocity, acceleration);
//                    }
//                }
                } else if (distance < maxDistance) {
                    force = Vector2f.scale(normal, magnitude);
                    acceleration = Vector2f.scale(force, 1 / mass);
                    velocity = Vector2f.add(velocity, acceleration);
                }
            }
        }

        friction = Vector2f.scale(velocity, -mu);
        acceleration = Vector2f.scale(friction, 1/mass);
        velocity = Vector2f.add(velocity, acceleration);

//        Vector2f tempPosition = Vector2f.add(position, velocity);
//        boolean move = false;
//        for (Cell cell : cells) {
//            float distance = Vector2f.length(Vector2f.subtract(tempPosition, cell.position));
//            if (distance < 2 * radius) {
//                move = false;
//                break;
//            }
//            move = true;
//        }
//        if (move) {
//            position = Vector2f.add(position, velocity);
//        }

        position = Vector2f.add(position, velocity);

        // check position bound
        int pointRadius = radius + 2;
        if (position.x < -pointRadius) {
            position.x = Main.windowSize.width + pointRadius;
        }
        if (position.x > Main.windowSize.width + pointRadius) {
            position.x = -pointRadius;
        }
        if (position.y < -pointRadius) {
            position.y = Main.windowSize.height + pointRadius;
        }
        if (position.y > Main.windowSize.height + pointRadius) {
            position.y = -pointRadius;
        }

    }

    public void paint(Graphics g) {
        g.setColor(color);
        g.fillOval((int) (position.x - radius), (int) (position.y - radius), 2 * radius, 2 * radius);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval((int) (position.x - radius), (int) (position.y - radius), 2 * radius, 2 * radius);
    }

}
