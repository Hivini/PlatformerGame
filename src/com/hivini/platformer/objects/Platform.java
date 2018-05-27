package com.hivini.platformer.objects;

import com.hivini.engine.GameContainer;
import com.hivini.engine.Renderer;
import com.hivini.platformer.GameManager;
import com.hivini.platformer.components.AABBComponent;

public class Platform extends GameObject {

    private int color = (int)(Math.random() * Integer.MAX_VALUE);
    private boolean playerOnMe = false;
    private boolean changedOnce = false;

    public Platform(int x, int y, int width, int height) {
        this.tag = "Platform";
        this.width = width;
        this.height = height;
        this.padding = 0;
        this.paddingTop = 0;
        this.positionX = x;
        this.positionY = y;

        this.addComponent(new AABBComponent(this));
    }

    // float temp = 0;

    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {
        // temp += dt;
        // positionX += Math.cos(temp) * 2;
        if (!Player.isOnPlatform() && playerOnMe) {
            playerOnMe = false;
            changedOnce = false;
        }


        this.updateComponents(gc, gm, dt);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawFillRect((int)positionX, (int)positionY, width, height, color);
        this.renderComponents(gc, r);
    }

    @Override
    public void collision(GameObject other) {
        playerOnMe = true;
        if (Player.isOnPlatform() && !changedOnce) {
            color = (int)(Math.random() * Integer.MAX_VALUE);
            changedOnce = true;
        }
    }


}
