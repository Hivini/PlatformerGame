package com.hivini.platformer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This is gonna be were the game gets executed, all the methods and logic is managed here
 * @author jorgequintero
 */
public class GamePanel extends JPanel implements Runnable {

    public static int width;
    public static int height;

    private Thread thread;
    private boolean running = false;
    private BufferedImage image;
    private Graphics2D g;

    public GamePanel(int width, int height) {
        GamePanel.width = width;
        GamePanel.height = height;
        setPreferredSize(new Dimension(width, height));
        // Allows the JPanel to allow input so we can use the input
        setFocusable(true);
        requestFocus();

    }

    public void addNotify() {
        super.addNotify();

        if (thread == null) {
            thread = new Thread(this, "GameThread");
            thread.start();
        }

    }

    public void init() {
        running = true;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D) image.getGraphics();
    }

    @Override
    public void run() {
        init();

        // Time settings so that the game doesn't go crazy rendering
        final double GAME_TICKS = 60.0;
        final double TIME_BEFORE_UPDATE = 1000000000 / GAME_TICKS;
        final int MUST_UPDATES_RENDER = 5;
        double lastUpdate = System.nanoTime();
        double lastRenderTime;

        final double TARGET_FPS = 60;
        final double TOTAL_TIME_RENDER = 1000000000 / TARGET_FPS;

        int frameCount = 0;
        int lastSecondTime = (int) (lastUpdate / 1000000000);
        int oldFrameCount = 0;


        while(running) {
            double now = System.nanoTime();
            int updateCount = 0;
            while (( (now - lastUpdate) > TIME_BEFORE_UPDATE) && (updateCount < MUST_UPDATES_RENDER)) {
                update();
                // Check input
                input();
                lastUpdate += TIME_BEFORE_UPDATE;
                updateCount ++;
            }

            if (now - lastUpdate > TIME_BEFORE_UPDATE)
                lastRenderTime = now - TIME_BEFORE_UPDATE;

            // Check input
            input();

            render();
            draw();
            lastRenderTime = now;
            frameCount ++;

            int thisSecond = (int) (lastUpdate / 1000000000);
            if (thisSecond > lastSecondTime) {
                if (frameCount != oldFrameCount) {
                    System.out.println("New Second " + thisSecond + " " + frameCount);
                    oldFrameCount = frameCount;
                }
                frameCount = 0;
                lastSecondTime = thisSecond;
            }

            while (now - lastRenderTime < TOTAL_TIME_RENDER && now - lastUpdate < TIME_BEFORE_UPDATE) {
                Thread.yield();

                try {
                    Thread.sleep(1);
                } catch(Exception e) {
                    System.out.println("ERROR");
                }

                now = System.nanoTime();
            }
        }
    }

    /**
     * Limits the loop of the game
     */
    public void update() {

    }

    public void render() {
        if (g != null) {
            g.setColor(new Color(66, 134, 244));
            g.fillRect(0, 0, width, height);
        }
    }

    public void draw() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

    public void input() {

    }

}
