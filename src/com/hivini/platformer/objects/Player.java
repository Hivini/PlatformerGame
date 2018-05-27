package com.hivini.platformer.objects;

import com.hivini.engine.GameContainer;
import com.hivini.engine.Renderer;
import com.hivini.engine.audio.SoundClip;
import com.hivini.engine.gfx.ImageTile;
import com.hivini.platformer.GameManager;
import com.hivini.platformer.components.AABBComponent;
import com.hivini.platformer.dialogs.DieDialog;
import com.hivini.platformer.dialogs.VictoryDialog;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class Player extends GameObject {

    private ImageTile playerSprite = new ImageTile("/testcharacter.png", 16, 16);
    private SoundClip playerJumpSound = new SoundClip("/audio/smalljump.wav");
    private SoundClip playerKickSound = new SoundClip("/audio/kick.wav");
    private SoundClip flagSound = new SoundClip("/audio/checkpointSound.wav");
    private SoundClip diedSound = new SoundClip("/audio/diedmusic.wav");
    private SoundClip victorySound = new SoundClip("/audio/stageclear.wav");

    private int direction = 0;
    private float animation = 0;

    private int tileX, tileY;
    private float offX, offY;

    private float speed = 90;
    private float fallSpeed = 8;
    private float fallDistance = 0;
    private float jump = -4.2f; // Change this to 4
    private int lives;
    private boolean isOnGround = false;
    private boolean isOnGroundLast = false;
    private static boolean isOnPlatform = false;
    private boolean victory = false;
    private boolean respawned = false;
    private boolean renderPlayer = true;
    private boolean invulnable = false;
    private boolean checkPoint = false;
    private boolean alreadyCheckPoint = false;
    private boolean alreadySoundDied = false;
    private boolean alreadyVictory = false;

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
        this.lives = 3;
        padding = 5;
        paddingTop = 2; // TODO Depends the sprite I'm using

        this.addComponent(new AABBComponent(this));
    }

    private float temp = 0;
    private float checkPointTemp = 0;
    private float diedTemp = 0;
    private float victoryTemp = 0;
    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {

        if (lives > 0 && !victory) {
            if (respawned) gc.drawDiedText(true);


            if (checkPoint) {
                checkPointTemp += dt;
                gc.drawCheckPoint(true);
            }
            if (checkPointTemp > 4) {
                checkPoint = false;
                gc.drawCheckPoint(false);
            }


            if (respawned)
                temp += dt;
            if (respawned && temp % 1.01 > 0.55)
                renderPlayer = false;
            else
                renderPlayer = true;

            if (respawned && temp > 4) {
                temp = 0;
                respawned = false;
                renderPlayer = true;
                invulnable = false;
                gc.drawDiedText(false);
            }

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
                playerJumpSound.play();
                isOnGround = false;
                isOnPlatform = false;
            }

            if (gc.getInput().isKey(KeyEvent.VK_N)) {
                speed = 140;
                jump = -5;
                animation *= 1.05;
            } else {
                speed = 90;
                jump = -4.2f;
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

        } else if (victory) {

            victoryTemp += dt;
            gm.diedSound();
            if (!alreadyVictory) {
                victorySound.play();
                alreadyVictory = true;
            }
            if (victorySound.isRunning()) {
                double move = Math.cos(victoryTemp * 3); // 3

                animation += dt * 4;
                if (animation >= 4)
                    animation = 0;

                if (move < 0) {
                    direction = 1;
                } else if (move > 0) {
                    direction = 0;
                } else {
                    victoryTemp = 0;
                }

                positionX += move * 3;
            } else {
                gc.stop();
                VictoryDialog victoryDialog = new VictoryDialog();
                System.exit(0);
            }
        } else {
            animation = 1;
            diedTemp += dt;
            if (diedTemp < 0.5)
                positionY -= dt * 100;
            else
                positionY += dt * 100;

            //if (positionY > 17)
              //  renderPlayer = false;

            gm.diedSound();
            if (!alreadySoundDied) {
                diedSound.play();
                alreadySoundDied = true;
            }
            if (!diedSound.isRunning()) {
                gc.stop();
                DieDialog dieDialog = new DieDialog();
                System.exit(0);
            }
        }


        this.updateComponents(gc, gm, dt);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        if (renderPlayer)
            r.drawImageTile(playerSprite, (int)positionX, (int)positionY, (int)animation, direction);
        //r.drawFillRect((int)positionX, (int)positionY, width, height, 0xff00ff00);
        //this.renderComponents(gc, r);
    }

    @Override
    public void collision(GameObject other) {
        if (other.getTag().equalsIgnoreCase("platform")) {

            AABBComponent playerComponent = (AABBComponent) this.findComponent("aabb");
            AABBComponent otherComponent = (AABBComponent) other.findComponent("aabb");

            // With these code we know that we are dealing with a top and bottom colission
            if (Math.abs(playerComponent.getLastCenterX() - otherComponent.getLastCenterX())
                    < playerComponent.getHalftWidth() + otherComponent.getHalftWidth()) {
                if (playerComponent.getCenterY() < otherComponent.getCenterY()) {

                    int distance = (playerComponent.getHalfHeight() + otherComponent.getHalfHeight()) - (otherComponent.getCenterY() - playerComponent.getCenterY());
                    offY -= distance;
                    positionY -= distance;
                    playerComponent.setCenterY(playerComponent.getCenterY() - distance);
                    fallDistance = 0;
                    isOnGround = true;
                }
                // Collide bottom
                if (playerComponent.getCenterY() > otherComponent.getCenterY()) {

                    int distance = (playerComponent.getHalfHeight() + otherComponent.getHalfHeight()) - (playerComponent.getCenterY() - otherComponent.getCenterY());
                    offY += distance;
                    positionY += distance;
                    playerComponent.setCenterY(playerComponent.getCenterY() + distance);
                    fallDistance = 0;
                }
            } else {
                // This is a side colission
                if (playerComponent.getCenterX() < otherComponent.getCenterX()) {

                    int distance = (playerComponent.getHalftWidth() + otherComponent.getHalftWidth()) - (otherComponent.getCenterX() - playerComponent.getCenterX());
                    offX -= distance;
                    positionX -= distance;
                    playerComponent.setCenterX(playerComponent.getCenterX() - distance);
                }
                // Collide bottom
                if (playerComponent.getCenterX() > otherComponent.getCenterX()) {

                    int distance = (playerComponent.getHalftWidth() + otherComponent.getHalftWidth()) - (playerComponent.getCenterX() - otherComponent.getCenterX());
                    offX += distance;
                    positionX += distance;
                    playerComponent.setCenterX(playerComponent.getCenterX() + distance);
                }
            }

            isOnPlatform = true;


        } else if (other.getTag().equalsIgnoreCase("diePlatform")) {
            AABBComponent playerComponent = (AABBComponent) this.findComponent("aabb");
            AABBComponent otherComponent = (AABBComponent) other.findComponent("aabb");

            // With these code we know that we are dealing with a top and bottom colission
            if (Math.abs(playerComponent.getLastCenterX() - otherComponent.getLastCenterX())
                    < playerComponent.getHalftWidth() + otherComponent.getHalftWidth()) {
                // positionX = GameManager.PLAYERSTARTX * GameManager.TS;
                // positionY = GameManager.PLAYERSTARTY * GameManager.TS;
                lives--;
                if (lives > 0)
                    resetPlayer();
                respawned = true;
                invulnable = true;

            } else {
                // positionX = GameManager.PLAYERSTARTX * GameManager.TS;
                // positionY = GameManager.PLAYERSTARTY * GameManager.TS;
                lives--;
                if (lives > 0)
                    resetPlayer();
                respawned = true;
                invulnable = true;
            }

        } else if (other.getTag().equalsIgnoreCase("victoryPlatform")) {

            AABBComponent playerComponent = (AABBComponent) this.findComponent("aabb");
            AABBComponent otherComponent = (AABBComponent) other.findComponent("aabb");

            // With these code we know that we are dealing with a top and bottom colission
            if (Math.abs(playerComponent.getLastCenterX() - otherComponent.getLastCenterX())
                    < playerComponent.getHalftWidth() + otherComponent.getHalftWidth()) {
                victory = true;
            } else {
                victory = true;
            }
        } else if (other.getTag().equalsIgnoreCase("Enemy")) {

            AABBComponent playerComponent = (AABBComponent) this.findComponent("aabb");
            AABBComponent otherComponent = (AABBComponent) other.findComponent("aabb");

            // With these code we know that we are dealing with a top and bottom colission
            if (Math.abs(playerComponent.getLastCenterX() - otherComponent.getLastCenterX())
                    < playerComponent.getHalftWidth() + otherComponent.getHalftWidth()) {
                if (playerComponent.getCenterY() < otherComponent.getCenterY()) {
                    other.setDead(true);
                    GameManager.score += 100;
                    // Jump when you kill him
                    fallDistance = jump;
                    playerKickSound.play();
                    isOnGround = false;
                }
            } else {
                // This is a side colission
                if (playerComponent.getCenterX() < otherComponent.getCenterX() && !invulnable) {
                    lives--;
                    if (lives > 0)
                        resetPlayer();
                    respawned = true;
                    invulnable = true;
                }
                // Collide bottom
                if (playerComponent.getCenterX() > otherComponent.getCenterX() && !invulnable) {
                    lives--;
                    if (lives > 0)
                        resetPlayer();
                    respawned = true;
                    invulnable = true;
                }
            }
        } else if (other.getTag().equalsIgnoreCase("checkpoint")) {
            if (!alreadyCheckPoint) {
                AABBComponent playerComponent = (AABBComponent) this.findComponent("aabb");
                AABBComponent otherComponent = (AABBComponent) other.findComponent("aabb");

                // With these code we know that we are dealing with a top and bottom colission
                if (Math.abs(playerComponent.getLastCenterX() - otherComponent.getLastCenterX())
                        < playerComponent.getHalftWidth() + otherComponent.getHalftWidth()) {
                    checkPoint = true;
                    alreadyCheckPoint = true;
                    flagSound.play();
                    GameManager.PLAYERSTARTX = 124;
                } else {
                    checkPoint = true;
                    alreadyCheckPoint = true;
                    flagSound.play();
                    GameManager.PLAYERSTARTX = 124;
                }
            }
        }
    }

    private void resetPlayer() {
        this.tileX = GameManager.PLAYERSTARTX;
        this.tileY = GameManager.PLAYERSTARTY;
        this.offX = 0;
        this.offY = 0;
        this.positionX = GameManager.PLAYERSTARTX * GameManager.TS;
        this.positionY = GameManager.PLAYERSTARTY * GameManager.TS;
    }

    public int getLives() {
        return lives;
    }

    public static boolean isOnPlatform() {
        return isOnPlatform;
    }
}
