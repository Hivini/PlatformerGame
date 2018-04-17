package com.hivini.platformer;

import com.hivini.engine.AbstractGame;
import com.hivini.engine.GameContainer;
import com.hivini.engine.Renderer;
import com.hivini.engine.audio.SoundClip;
import com.hivini.engine.gfx.*;
import com.hivini.platformer.objects.GameObject;
import com.hivini.platformer.objects.Platform;
import com.hivini.platformer.objects.Player;

import java.util.ArrayList;

public class GameManager extends AbstractGame {

    public static final int TS = 16;

    private Image levelImage = new Image("/world11scenary.png");
    //private Image skyBackground = new Image("/skybackground.png");
    //private Image levelImage = new Image("/levelscenary.png");

    private SoundClip backgroundMusic = new SoundClip("/audio/backgroundmusic.wav");

    private ArrayList<GameObject> objects = new ArrayList<>();
    private Camera camera;

    private boolean[] colission;
    private int levelWidth, levelHeight;

    public GameManager() {
        objects.add(new Player(4, 4));
        objects.add(new Platform());
        loadLevel("/world11.png");
        camera = new Camera("Player");
        backgroundMusic.loop();
    }

    @Override
    public void init(GameContainer gc) {
        gc.getRenderer().setAmbientColor(-1);
    }

    @Override
    public void update(GameContainer gc, float dt) {
        for (int i = 0; i < objects.size(); i ++) {
            objects.get(i).update(gc,this, dt);
            if (objects.get(i).isDead()) {
                objects.remove(i);
                // Because the array shifts all the data when is removed
                i--;
            }
        }

        Physics.update();
        camera.update(gc, this, dt);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        camera.render(r);

        r.drawImage(levelImage, 0, 0);

        /*
        for (int y = 0; y < levelHeight; y++) {
            for (int x = 0; x < levelWidth; x++) {
                if (colission[x + y * levelWidth])
                    r.drawFillRect(x * TS, y * TS, TS, TS, 0xff0f0f0f);
                else
                    r.drawFillRect(x * TS, y * TS, TS, TS, 0xfff9f9f9);
            }
        }*/

        for (GameObject obj : objects) {
            obj.render(gc, r);
        }

    }

    public void loadLevel(String path) {
        Image levelImage = new Image(path);

        levelWidth = levelImage.getWidth();
        levelHeight = levelImage.getHeight();
        colission = new boolean[levelWidth * levelHeight];

        for (int y = 0; y < levelImage.getHeight(); y++) {
            for (int x = 0; x < levelImage.getWidth(); x++) {
                if (levelImage.getP()[x + y * levelImage.getWidth()] == 0xff000000)
                    colission[x + y * levelImage.getWidth()] = true;
                else
                    colission[x + y * levelImage.getWidth()] = false;
            }
        }
    }

    public boolean getColission(int x, int y) {
        if (x < 0 || x >= levelWidth || y < 0 || y >= levelHeight)
            return true;
        return colission[x + y * levelWidth];
    }

    public void addObject(GameObject object) {
        objects.add(object);
    }

    public GameObject getObject(String tag) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getTag().equals(tag)) return objects.get(i);
        }
        return null;
    }

    public int getLevelWidth() {
        return levelWidth;
    }

    public int getLevelHeight() {
        return levelHeight;
    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new GameManager());
        gc.start();
    }
}
