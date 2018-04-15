package com.hivini.platformer;

import com.hivini.engine.AbstractGame;
import com.hivini.engine.GameContainer;
import com.hivini.engine.Renderer;
import com.hivini.engine.audio.SoundClip;
import com.hivini.engine.gfx.*;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GameManager extends AbstractGame {

    private ArrayList<GameObject> objects = new ArrayList<>();

    public GameManager() {

    }

    @Override
    public void update(GameContainer gc, float dt) {
        for (int i = 0; i < objects.size(); i ++) {
            objects.get(i).update(gc, dt);
            if (objects.get(i).isDead()) {
                objects.remove(i);
                // Because the array shifts all the data when is removed
                i--;
            }
        }
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        for (GameObject obj : objects) {
            obj.render(gc, r);
        }

    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new GameManager());
        gc.start();
    }
}
