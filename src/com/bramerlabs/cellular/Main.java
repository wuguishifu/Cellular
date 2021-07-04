package com.bramerlabs.cellular;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

    public static final Dimension windowSize = new Dimension(1920, 1080);

    private JFrame frame;
    private JPanel panel;

    private Universe universe;

    private boolean run = true;

    public static void main(String[] args0) {
        new Main().init();
    }

    public void paint(Graphics g) {
        universe.paint(g);
    }

    public void update() {
        for (int i = 0; i < 10; i++) {
            universe.step();
        }
    }

    public void init() {
        universe = new Universe(6, 400, windowSize.width, windowSize.height);
        universe.reseed(0.02f, 0.04f, 0.0f,30.0f,
                30.0f, 100.0f, 0.01f, false);
        universe.setRandomTypes();
        universe.setRandomParticles();
        universe.wrap = true;
        universe.flatForce = true;

        KeyListener keyListener = new KeyListener() {
            public void keyTyped(KeyEvent keyEvent) {}
            public void keyPressed(KeyEvent keyEvent) {
                onKeyPressed(keyEvent);
            }
            public void keyReleased(KeyEvent keyEvent) {
                onKeyReleased(keyEvent);
            }
        };

        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {}
            public void mousePressed(MouseEvent mouseEvent) {}
            public void mouseReleased(MouseEvent mouseEvent) {}
            public void mouseEntered(MouseEvent mouseEvent) {}
            public void mouseExited(MouseEvent mouseEvent) {}
        };

        frame = new JFrame();
        frame.setSize(windowSize);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel = new JPanel(){
            @Override
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paint(g);
                Main.this.paint(g);
            }
        };
        panel.addMouseListener(mouseListener);
        panel.setPreferredSize(windowSize);

        frame.addKeyListener(keyListener);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        run();
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                run = false;
                break;
        }
    }

    public void onKeyReleased(KeyEvent keyEvent) {

    }

    @SuppressWarnings("BusyWait")
    public void run() {
        while (run) {
            update();
            panel.repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        frame.dispose();
    }

}
