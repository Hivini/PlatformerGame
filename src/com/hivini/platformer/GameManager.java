package com.hivini.platformer;

import com.hivini.engine.AbstractGame;
import com.hivini.engine.GameContainer;
import com.hivini.engine.Renderer;
import com.hivini.engine.audio.SoundClip;
import com.hivini.engine.gfx.*;

import java.awt.event.KeyEvent;

public class GameManager extends AbstractGame {

    private ImageTile image;
    private SoundClip clip;

    public GameManager() {
        image = new ImageTile("/test.png", 16, 16);
        clip = new SoundClip("/audio/reload.wav");
    }
    @Override
    public void update(GameContainer gc, float dt) {
        if (gc.getInput().isKeyDown(KeyEvent.VK_A)) {
            clip.play();
        }

        temp += dt;

        if (temp > 3) {
            temp = 0;
        }
    }

    float temp = 0;

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawImageTile(image, gc.getInput().getMouseX() - 8, gc.getInput().getMouseY() - 16, (int)temp, 0);
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new GameManager());
        gc.start();
    }
}
