package com.hivini.platformer;

import javax.swing.JFrame;

public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("Platformer Game");
        setContentPane(new GamePanel(1280, 720));

        pack();
        setLocationRelativeTo(null);
    }
}
