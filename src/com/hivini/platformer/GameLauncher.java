package com.hivini.platformer;

import javax.swing.*;

public class GameLauncher {

    GameLauncher() {
        GameWindow window = new GameWindow();
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new GameLauncher();
    }
}
