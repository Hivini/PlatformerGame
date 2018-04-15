package com.hivini.platformer;

import com.hivini.engine.GameContainer;
import com.hivini.engine.Renderer;

import java.awt.event.KeyEvent;

public class Player extends GameObject {

    private int tileX, tileY;
    private float offX, offY;

    private float speed = 100;
    private float fallSpeed = 10;
    private float fallDistance = 0;
    private float jump = -4;
    private boolean isOnGround = false;

    public Player(int posX, int posY) {
        this.tag = "Player";
        this.tileX = posX;
        this.tileY = posY;
        this.offX = 0;
        this.offY = 0;
        this.positionX = posX * GameManager.TS;
        this.positionY = posY * GameManager.TS;
        this.height = GameManager.TS;
        this.width = GameManager.TS;
    }

    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {

        // Moving left and right with colission
        if (gc.getInput().isKey(KeyEvent.VK_A)) {
            if (gm.getColission(tileX - 1, tileY) || gm.getColission(tileX - 1, tileY + (int)Math.signum((int)offY))) {
                if (offX > 0) {
                    offX -= dt * speed;
                    // Avoiding to stick in the wall for one frame
                    if (offX < 0)
                        offX = 0;
                }
                else
                    offX = 0;
            } else {
                offX -= dt * speed;
            }
        }

        if (gc.getInput().isKey(KeyEvent.VK_D)) {
            if (gm.getColission(tileX + 1, tileY)) {
                if (offX < 0) {
                    offX += dt * speed;
                    if (offX > 0)
                        offX = 0;
                }
                else
                    offX = 0;
            } else {
                offX += dt * speed;
            }
        }

        // Gravity & Jump
        fallDistance += dt * fallSpeed;

        if (gc.getInput().isKeyDown(KeyEvent.VK_W) && isOnGround) {
            fallDistance = jump;
            isOnGround = false;
        }
        offY += fallDistance;

        // The math signum solves a corner bug where the player goes through the wall
        if (fallDistance < 0) {
            if ((gm.getColission(tileX, tileY - 1) ||
                    gm.getColission(tileX + (int)Math.signum((int)offX), tileY - 1)) && offY < 0) {
                fallDistance = 0;
                offY = 0;
            }
        }
        if (fallDistance > 0) {
            if ((gm.getColission(tileX, tileY + 1) ||
                    gm.getColission(tileX + (int)Math.signum((int)offX), tileY + 1)) && offY > 0) {
                fallDistance = 0;
                offY = 0;
                isOnGround = true;
            }
        }

        // This code is for not stay floating

        if (offY > GameManager.TS / 2) {
            tileY++;
            offY -= GameManager.TS;
        }

        if (offY < -GameManager.TS / 2) {
            tileY--;
            offY += GameManager.TS;
        }

        if (offX > GameManager.TS / 2) {
            tileX++;
            offX -= GameManager.TS;
        }

        if (offX < -GameManager.TS / 2) {
            tileX--;
            offX += GameManager.TS;
        }

        positionX = tileX * GameManager.TS + offX;
        positionY = tileY * GameManager.TS + offY;

    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawFillRect((int)positionX, (int)positionY, width, height, 0xff00ff00);
    }
}
