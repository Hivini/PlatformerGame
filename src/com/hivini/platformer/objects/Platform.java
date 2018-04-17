package com.hivini.platformer.objects;

import com.hivini.engine.GameContainer;
import com.hivini.engine.Renderer;
import com.hivini.platformer.GameManager;
import com.hivini.platformer.components.AABBComponent;

public class Platform extends GameObject {

    private int color = (int)(Math.random() * Integer.MAX_VALUE);

    public Platform() {
        this.tag = "Platform";
        this.width = 32;
        this.height = 16;
        this.padding = 0;
        this.paddingTop = 0;
        this.positionX = 247;
        this.positionY = 190;

        this.addComponent(new AABBComponent(this));
    }

    float temp = 0;

    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {
        temp += dt;
        positionX += Math.cos(temp) * 2;


        this.updateComponents(gc, gm, dt);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawFillRect((int)positionX, (int)positionY, width, height, color);
        this.renderComponents(gc, r);
    }

    @Override
    public void collision(GameObject other) {
        color = (int)(Math.random() * Integer.MAX_VALUE);
    }
}
