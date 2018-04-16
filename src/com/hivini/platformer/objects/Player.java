package com.hivini.platformer.objects;

import com.hivini.engine.GameContainer;
import com.hivini.engine.Renderer;
import com.hivini.engine.gfx.ImageTile;
import com.hivini.platformer.GameManager;
import com.hivini.platformer.components.AABBComponent;

import java.awt.event.KeyEvent;

public class Player extends GameObject {

    private ImageTile playerSprite = new ImageTile("/testcharacter.png", 16, 16);

    private int direction = 0;
    private float animation = 0;

    private int tileX, tileY;
    private float offX, offY;

    private float speed = 100;
    private float fallSpeed = 10;
    private float fallDistance = 0;
    private float jump = -4;
    private boolean isOnGround = false;
    private boolean isOnGroundLast = false;

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
        padding = 5;
        paddingTop = 2; // TODO Depends the sprite I'm using

        this.addComponent(new AABBComponent(this));
    }

    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {

        // Moving left and right with colission
        if (gc.getInput().isKey(KeyEvent.VK_A)) {
            if (gm.getColission(tileX - 1, tileY) || gm.getColission(tileX - 1, tileY + (int)Math.signum((int)offY))) {
                offX -= dt * speed;
                // Avoiding to stick in the wall for one frame
                if (offX < -padding)
                    offX = -padding;
            } else {
                offX -= dt * speed;
            }
        }

        if (gc.getInput().isKey(KeyEvent.VK_D)) {
            if (gm.getColission(tileX + 1, tileY) || gm.getColission(tileX + 1, tileY + (int)Math.signum((int) offY))) {
                offX += dt * speed;

                if (offX > padding)
                    offX = padding;

            } else {
                offX += dt * speed;
            }
        }

        // Gravity & Jump
        fallDistance += dt * fallSpeed;

        // The math signum solves a corner bug where the player goes through the wall
        if (fallDistance < 0) {
            if ((gm.getColission(tileX, tileY - 1) ||
                    gm.getColission(tileX + (int)Math.signum((int) Math.abs(offX) > padding ? offX : 0), tileY - 1)) && offY < -paddingTop) {
                fallDistance = 0;
                offY = -paddingTop;
            }
        }
        if (fallDistance > 0) {
            if ((gm.getColission(tileX, tileY + 1) ||
                    gm.getColission(tileX + (int)Math.signum((int) Math.abs(offX) > padding ? offX : 0), tileY + 1)) && offY > 0) {
                fallDistance = 0;
                offY = 0;
                isOnGround = true;
            }
        }

        if (gc.getInput().isKeyDown(KeyEvent.VK_W) && isOnGround) {
            fallDistance = jump;
            isOnGround = false;
        }

        offY += fallDistance;

        positionX = tileX * GameManager.TS + offX;
        positionY = tileY * GameManager.TS + offY;

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

        // Run animation
        if (gc.getInput().isKey(KeyEvent.VK_D)) {
            direction = 0;
            animation += dt * 8;
            if (animation >= 4)
                animation = 0;
        } else if (gc.getInput().isKey(KeyEvent.VK_A)) {
            direction = 1;
            animation += dt * 8;
            if (animation >= 4)
                animation = 0;
        } else
            animation = 0;

        // Jump animation
        if ((int)fallDistance != 0) {
            animation = 1;
            isOnGround = false;
        }

        // Ground animation
        if (isOnGround && !isOnGroundLast)
            animation = 2;

        isOnGroundLast = isOnGround;

        this.updateComponents(gc, gm, dt);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawImageTile(playerSprite, (int)positionX, (int)positionY, (int)animation, direction);
        //r.drawFillRect((int)positionX, (int)positionY, width, height, 0xff00ff00);
        this.renderComponents(gc, r);
    }

    @Override
    public void collision(GameObject other) {
        if (other.getTag().equalsIgnoreCase("platform")) {

            AABBComponent playerComponent  = (AABBComponent) this.findComponent("aabb");
            AABBComponent otherComponent  = (AABBComponent) other.findComponent("aabb");

            if (playerComponent.getCenterY() < otherComponent.getCenterY()) {

                int distance = (playerComponent.getHalfHeight() + otherComponent.getHalfHeight()) - (otherComponent.getCenterY() - playerComponent.getCenterY());
                offY -= distance;
                positionY -= distance;
                fallDistance = 0;
                isOnGround = true;
            }
            // Collide bottom
            if (playerComponent.getCenterY() > otherComponent.getCenterY()) {

                int distance = (playerComponent.getHalfHeight() + otherComponent.getHalfHeight()) - (playerComponent.getCenterY() - otherComponent.getCenterY());
                offY += distance;
                positionY += distance;
                fallDistance = 0;
            }
        }
    }
}
