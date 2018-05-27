package com.hivini.platformer;

import com.hivini.engine.AbstractGame;
import com.hivini.engine.GameContainer;
import com.hivini.engine.Renderer;
import com.hivini.engine.audio.SoundClip;
import com.hivini.engine.gfx.*;
import com.hivini.platformer.objects.*;

import javax.swing.*;
import java.util.ArrayList;

public class GameManager extends AbstractGame {

    public static final int TS = 16;
    public static int PLAYERSTARTX = 4; // 4
    public static int PLAYERSTARTY = 15;
    public static int score = 0;

    private Image levelImage = new Image("/world11scenary.png");
    //private Image skyBackground = new Image("/skybackground.png");
    //private Image levelImage = new Image("/levelscenary.png");

    private SoundClip backgroundMusic = new SoundClip("/audio/backgroundmusic.wav");

    private ArrayList<GameObject> objects = new ArrayList<>();
    private Camera camera;
    private static Player player = new Player(PLAYERSTARTX, PLAYERSTARTY);

    private boolean[] colission;
    private int levelWidth, levelHeight;

    public GameManager() {
        objects.add(player);
        // Adding the static platforms
        objects.add(new Platform(9*TS, 12*TS, TS, TS));
        objects.add(new Platform(13*TS, 12*TS, 5*TS, TS));
        objects.add(new Platform(15*TS, 9*TS, TS, TS));
        objects.add(new Platform(41*TS, 12*TS, 3*TS, TS));
        objects.add(new Platform(52*TS, 12*TS, 2*TS, TS));
        objects.add(new Platform(73*TS, 12*TS, TS, TS));
        objects.add(new Platform(77*TS, 11*TS, 4*TS, TS));
        objects.add(new Platform(82*TS, 8*TS, 7*TS, TS));
        objects.add(new Platform(92*TS, 8*TS, 4*TS, TS));
        objects.add(new Platform(95*TS, 11*TS, TS, TS));
        objects.add(new Platform(100*TS, 11*TS, 2*TS, TS));
        objects.add(new Platform(107*TS, 11*TS, TS, TS));
        objects.add(new Platform(110*TS, 11*TS, TS, TS));
        objects.add(new Platform(113*TS, 11*TS, TS, TS));
        objects.add(new Platform(110*TS, 7*TS, TS, TS));
        objects.add(new Platform(119*TS, 11*TS, TS, TS));
        objects.add(new Platform(122*TS, 7*TS, 3*TS, TS));
        objects.add(new Platform(130*TS, 7*TS, 4*TS, TS));
        objects.add(new Platform(132*TS, 11*TS, 2*TS, TS));
        objects.add(new Platform(206*TS, 9*TS, 15*TS, TS));
        // objects.add(new Platform(9*TS, 12*TS, TS, TS));


        // Adding the platform that make the player die
        objects.add(new DiePlatform(63*TS, 18*TS + 15, 6*TS, 1));
        objects.add(new DiePlatform(86*TS, 18*TS + 15, 4*TS, 1));
        objects.add(new DiePlatform(175*TS, 18*TS + 15, 5*TS, 1));
        // Adding the victory platform that makes the player win
        objects.add(new VictoryPlatform(294*TS, 13*TS, 2*TS, 3*TS));
        // Adding the checkpoint
        objects.add(new CheckpointPlatform(123*TS+12, 13*TS, 2*TS, 3*TS));

        // Add enemies
        objects.add(new Enemy(15, 15, 1f));
        objects.add(new Enemy(29, 15, 0.9f));
        objects.add(new Enemy(42, 15, 0.9f));
        objects.add(new Enemy(74, 15, 0.6f));
        objects.add(new Enemy(80, 15, 0.6f));
        objects.add(new Enemy(110, 15, 0.9f));
        objects.add(new Enemy(125, 15, 0.9f));
        objects.add(new Enemy(147, 15, 0.3f));
        objects.add(new Enemy(162, 15, 0.6f));
        objects.add(new Enemy(192, 15, 0.5f));
        objects.add(new Enemy(206, 15, 0.8f));
        objects.add(new Enemy(216, 15, 0.8f));
        objects.add(new Enemy(206, 15, 0.8f));
        objects.add(new Enemy(241, 8, 0.5f));
        objects.add(new Enemy(244, 8, 0.5f));
        objects.add(new Enemy(248, 8, 0.5f));
        objects.add(new Enemy(249, 8, 0.5f));
        objects.add(new Enemy(252, 8, 0.5f));
        objects.add(new Enemy(256, 8, 0.5f));
        objects.add(new Enemy(258, 8, 0.5f));


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

    public void diedSound() {
        backgroundMusic.stop();
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

    public static Player getPlayer(){
        return player;
    }
}
