package com.bramerlabs.cellular;

import java.awt.*;

public class Universe {

    public static final float RADIUS = 5.0f;
    public static final float DIAMETER = 2.0f * RADIUS;
    public static final float R_SMOOTH = 2.0f;

    private int numTypes;
    private int numParticles;
    private float attractMean;
    private float attractSTD;
    private float minRLower;
    private float minRUpper;
    private float maxRLower;
    private float maxRUpper;
    private float friction;
    public boolean flatForce;
    public boolean wrap;

    private int width, height;
    private float centerX, centerY;

    private int seed;

    private CellTypes cellTypes;
    private Cell[] cells;

    public Universe(int numTypes, int numParticles, int width, int height) {
        this.numTypes = numTypes;
        this.numParticles = numParticles;
        this.width = width;
        this.height = height;

        this.attractMean = 0;
        this.attractSTD = 0;
        this.minRLower = 0;
        this.minRUpper = 0;
        this.maxRLower = 0;
        this.maxRUpper = 0;
        this.friction = 0;
        this.flatForce = false;
        this.wrap = false;

        this.seed = 10;

        this.cellTypes = new CellTypes(numTypes);
        this.cells = new Cell[numParticles];
    }

    public void reseed(float attractMean, float attractSTD, float minRLower, float minRUpper,
                       float maxRLower, float maxRUpper, float friction, boolean flatForce) {
        this.attractMean = attractMean;
        this.attractSTD = attractSTD;
        this.minRLower = minRLower;
        this.minRUpper = minRUpper;
        this.maxRLower = maxRLower;
        this.maxRUpper = maxRUpper;
        this.friction = friction;
        this.flatForce = flatForce;
    }

    public void setPopulation(int numTypes, int numParticles) {
        this.cellTypes.resize(numTypes);
        this.cells = new Cell[numParticles];
    }

    public void setRandomTypes() {
        cellTypes.populate(attractMean, attractSTD, minRUpper, minRLower, maxRUpper, maxRLower);
    }

    public void setRandomParticles() {
        for (int i = 0; i < numParticles; i++) {
            int cellType = (int) (Math.random() * numTypes);
            float x = (float) ((Math.random() * 0.5 + 0.25) * width);
            float y = (float) ((Math.random() * 0.5 + 0.25) * height);
            float vx = (float) (Math.random() * 0.2f);
            float vy = (float) (Math.random() * 0.2f);
            cells[i] = new Cell(x, y, vx, vy, cellType);
        }
    }

    public void step() {
        for (int i = 0; i < numParticles; i++) {
            Cell p = cells[i];

            for (int j = 0; j < numParticles; j++) {
                Cell q = cells[j];

                // deltas
                float dx = q.x - p.x;
                float dy = q.y - p.y;

                if (wrap) {
                    if (dx > width * 0.5f) {
                        dx -= width;
                    } else if (dx < -width * 0.5f) {
                        dx += width;
                    }
                    if (dy > height * 0.5f) {
                        dy -= height;
                    } else if (dy < height * 0.5f) {
                        dy += height;
                    }
                }

                // square distance calculation
                float r2 = dx * dx + dy * dy;
                float minR = cellTypes.minR(p.type, q.type);
                float maxR = cellTypes.maxR(p.type, q.type);

                if (r2 > maxR * maxR || r2 < 0.01f) {
                    continue;
                }

                // normalize displacement
                float r = (float) Math.sqrt(r2);
                dx /= r;
                dy /= r;

                // calculate forces
                float f;
                if (r > minR) {
                    if (flatForce) {
                        f = cellTypes.attract(p.type, q.type);
                    } else {
                        float n = 2.0f * Math.abs(r - 0.5f * (maxR + minR));
                        float d = maxR - minR;
                        f = cellTypes.attract(p.type, q.type) * (1.0f - n / d);
                    }
                } else {
                    f = R_SMOOTH * minR * (1.0f / (minR + R_SMOOTH) - 1.0f / (r + R_SMOOTH));
                }

                // apply forces
                p.vx += f * dx;
                p.vy += f * dy;
            }
        }

        // update cell
        for (int i = 0; i < numParticles; i++) {
            Cell p = cells[i];

            // update position and velocity
            p.x += p.vx;
            p.y += p.vy;

            // friction calculation
            p.vx *= (1.0f - friction);
            p.vy *= (1.0f - friction);

            // check walls
            if (wrap) {
                if (p.x < 0) {
                    p.x += width;
                } else if (p.x >= width) {
                    p.x -= width;
                }
                if (p.y < 0) {
                    p.y += height;
                } else if (p.y >= height) {
                    p.y -= height;
                }
            } else {
                if (p.x <= DIAMETER) {
                    p.vx = -p.vx;
                    p.x = DIAMETER;
                } else if (p.x >= width - DIAMETER) {
                    p.vx = -p.vx;
                    p.x = width - DIAMETER;
                }
                if (p.y <= DIAMETER) {
                    p.vy = -p.vy;
                    p.y = DIAMETER;
                } else if (p.y >= height - DIAMETER) {
                    p.vy = -p.vy;
                    p.y = height - DIAMETER;
                }
            }
        }
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setStroke(new BasicStroke(2));
        for (Cell cell : cells) {
            g.setColor(cellTypes.color(cell.type));
            g.fillOval((int) (cell.x - cell.radius), (int) (cell.y - cell.radius), 2 * cell.radius, 2 * cell.radius);
            g.setColor(Color.BLACK);
            g2d.drawOval((int) (cell.x - cell.radius), (int) (cell.y - cell.radius), 2 * cell.radius, 2 * cell.radius);
        }
        g2d.dispose();
    }

}
