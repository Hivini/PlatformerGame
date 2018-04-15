package com.hivini.platformer;

import com.hivini.engine.GameContainer;
import com.hivini.engine.Renderer;

public abstract class GameObject {

    // Identifing object in an array
    protected String tag;
    protected int positionX, positionY, width, height;
    protected boolean dead = false;

    public abstract void update(GameContainer gc, float dt);
    public abstract void render(GameContainer gc, Renderer r);

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
