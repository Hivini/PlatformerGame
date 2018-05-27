package com.hivini.platformer.objects;

import com.hivini.engine.GameContainer;
import com.hivini.engine.Renderer;
import com.hivini.engine.gfx.ImageTile;
import com.hivini.platformer.GameManager;
import com.hivini.platformer.components.AABBComponent;

public class Enemy extends GameObject {

    private ImageTile enemySprite = new ImageTile("/enemy.png", 16, 16);

    private int direction = 0;
    private float animation = 0;
    private float distance;

    private int tileX, tileY;
    private float offX, offY;

    private float speed = 90;

    public Enemy(int posX, int posY, float distance) {
        this.tag = "Enemy";
        this.tileX = posX;
        this.tileY = posY;
        this.offX = 0;
        this.offY = 0;
        this.positionX = posX * GameManager.TS;
        this.positionY = posY * GameManager.TS;
        this.height = GameManager.TS;
        this.width = GameManager.TS;
        this.distance = distance;

        this.addComponent(new AABBComponent(this));
    }

    private float temp = 0;
    private double variable = 0;
    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {

        temp += dt;
        variable = Math.cos(temp);

        animation += dt * 4;
        if (animation >= 4)
            animation = 0;

        if (variable < 0) {
            direction = 1;
        } else if (variable > 0) {
            direction = 0;
        } else {
            temp = 0;
        }

        positionX += variable * distance;

        this.updateComponents(gc, gm, dt);

    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawImageTile(enemySprite, (int)positionX, (int)positionY, (int)animation, direction);
        // this.renderComponents(gc, r);
    }

    @Override
    public void collision(GameObject other) {

    }
}
