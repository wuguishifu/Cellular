package com.bramerlabs.cellular;

import java.awt.*;

public class CellTypes {

    private Color[] colors;
    private float[] attract;
    private float[] minR;
    private float[] maxR;

    private int size;

    public CellTypes(int size) {
        this.size = size;
        this.resize(size);
    }

    public void resize(int size) {
        this.size = size;
        this.colors = new Color[size];
        this.attract = new float[size * size];
        this.minR = new float[size * size];
        this.maxR = new float[size * size];
    }

    public void populate(float attractMean, float attractSTD, float minRLower, float minRUpper,
                         float maxRLower, float maxRUpper) {
        for (int i = 0; i < size; i++) {
            colors[i] = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    attract[i * size + j] = (float) -Math.abs(Math.random() * attractSTD + attractMean);
                    minR[i * size + j] = Universe.DIAMETER;
                } else {
                    attract[i * size + j] = (float) Math.random() * attractSTD + attractMean;
                    minR[i * size + j] = (float) Math.max(Math.random() * minRUpper + minRLower, Universe.DIAMETER);
                }
                maxR[i * size + j] = (float) Math.max(Math.random() * maxRUpper + maxRLower, minR[i * size + j]);

                // want radius to be symmetrical
                maxR[i * size + j] = maxR[j * size + i];
                minR[i * size + j] = minR[j * size + i];
            }
        }
    }

    public int size() {
        return this.size;
    }

    public Color color(int i) {
        return colors[i];
    }

    public float attract(int i, int j) {
        return attract[i * size + j];
    }

    public float minR(int i, int j) {
        return minR[i * size + j];
    }

    public float maxR(int i, int j) {
        return maxR[i * size + j];
    }

}
