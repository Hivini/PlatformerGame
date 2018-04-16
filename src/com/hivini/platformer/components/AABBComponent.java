package com.hivini.platformer.components;

import com.hivini.engine.GameContainer;
import com.hivini.engine.Renderer;
import com.hivini.platformer.GameManager;
import com.hivini.platformer.Physics;
import com.hivini.platformer.objects.GameObject;

public class AABBComponent extends Component {

    private GameObject parent;
    private int centerX, centerY, halftWidth, halfHeight;

    public AABBComponent(GameObject parent) {
        this.parent = parent;
        this.tag = "aabb";
    }

    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {
        centerX = (int) (parent.getPositionX() + (parent.getWidth() / 2));
        centerY = (int) (parent.getPositionY() + (parent.getHeight() / 2) + (parent.getPaddingTop()/2));
        halftWidth = (parent.getWidth() / 2) - parent.getPadding();
        halfHeight = (parent.getHeight() / 2) - (parent.getPaddingTop() / 2);
        Physics.addAABBComponent(this);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawRect(centerX - halftWidth, centerY - halfHeight, halftWidth * 2, halfHeight * 2, 0xff000000);
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public int getHalftWidth() {
        return halftWidth;
    }

    public void setHalftWidth(int halftWidth) {
        this.halftWidth = halftWidth;
    }

    public int getHalfHeight() {
        return halfHeight;
    }

    public void setHalfHeight(int halfHeight) {
        this.halfHeight = halfHeight;
    }

    public GameObject getParent() {
        return parent;
    }

    public void setParent(GameObject parent) {
        this.parent = parent;
    }
}
