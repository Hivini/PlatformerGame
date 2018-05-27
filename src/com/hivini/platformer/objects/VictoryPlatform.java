package com.hivini.platformer.objects;

import com.hivini.engine.GameContainer;
import com.hivini.engine.Renderer;
import com.hivini.engine.gfx.Image;
import com.hivini.platformer.GameManager;
import com.hivini.platformer.components.AABBComponent;

public class VictoryPlatform extends GameObject {

    public VictoryPlatform(int x, int y, int width, int height) {
        this.tag = "victoryPlatform";
        this.width = width;
        this.height = height;
        this.padding = 0;
        this.paddingTop = 0;
        this.positionX = x;
        this.positionY = y;

        this.addComponent(new AABBComponent(this));
    }


    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {
        this.updateComponents(gc, gm, dt);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        // this.renderComponents(gc, r);
    }

    @Override
    public void collision(GameObject other) {

    }
}
