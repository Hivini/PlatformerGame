package com.hivini.platformer;

import com.hivini.engine.GameContainer;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        GameContainer gc = new GameContainer(new GameManager());
        gc.start();
    }


}
