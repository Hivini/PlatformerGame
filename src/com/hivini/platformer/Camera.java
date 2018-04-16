package com.hivini.platformer;

import com.hivini.engine.GameContainer;
import com.hivini.engine.Renderer;
import com.hivini.platformer.objects.GameObject;

public class Camera {

    private float offX, offY;

    private String targetTag;
    private GameObject target = null;

    public Camera(String tag) {

        this.targetTag = tag;
    }

    public void update(GameContainer gameContainer, GameManager gameManager, float dt) {
        if (target == null) target = gameManager.getObject(targetTag);
        if (target == null) return;

        // Setting the position of the camera
        float targetX = (target.getPositionX() + target.getWidth() / 2) - gameContainer.getWidth() / 2;
        float targetY = (target.getPositionY() + target.getHeight() / 2) - gameContainer.getHeight() / 2;

        offX = targetX;
        offY = targetY;

        // Tried to do dynamic camera but causes a jitter
        //offX -= dt * (int)(offX - targetX) * 20;
        //offY -= dt * (int)(offY - targetY) * 20;

        // Prevent the camera from going out of the screen
        if (offX < 0) offX = 0;
        if (offY < 0) offY = 0;
        if (offX + gameContainer.getWidth() > gameManager.getLevelWidth() * GameManager.TS)
            offX = gameManager.getLevelWidth() * GameManager.TS - gameContainer.getWidth();
        if (offY + gameContainer.getHeight() > gameManager.getLevelHeight() * GameManager.TS)
            offY = gameManager.getLevelHeight() * GameManager.TS - gameContainer.getHeight();
    }

    public void render(Renderer r) {
        r.setCameraX((int)offX);
        r.setCameraY((int)offY);
    }

    public float getOffX() {
        return offX;
    }

    public void setOffX(float offX) {
        this.offX = offX;
    }

    public float getOffY() {
        return offY;
    }

    public void setOffY(float offY) {
        this.offY = offY;
    }

    public String getTargetTag() {
        return targetTag;
    }

    public void setTargetTag(String targetTag) {
        this.targetTag = targetTag;
    }

    public GameObject getTarget() {
        return target;
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }
}
