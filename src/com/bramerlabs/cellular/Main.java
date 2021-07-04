package com.bramerlabs.cellular;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main {

    public static final Dimension windowSize = new Dimension(1920, 1080);

    private JFrame frame;
    private JPanel panel;

    private boolean run = true;

    private ArrayList<Cell> cells;

    public static void main(String[] args0) {
        new Main().init();
    }

    public void paint(Graphics g) {
        for (Cell cell : cells) {
            cell.paint(g);
        }
    }

    public void update() {
        for (Cell cell : cells) {
            cell.update(cells);
        }
    }

    public void init() {

        cells = new ArrayList<>();
        cells.add(new Cell(new Vector2f(100, 100)));
        cells.add(new Cell(new Vector2f(150, 150)));

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
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                    cells.add(new Cell(new Vector2f(mouseEvent.getX(), mouseEvent.getY())));
                }
            }
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

        frame.addKeyListener(keyListener);
        frame.add(panel);
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
