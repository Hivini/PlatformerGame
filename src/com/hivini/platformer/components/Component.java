package com.hivini.platformer.components;

import com.hivini.engine.GameContainer;
import com.hivini.engine.Renderer;
import com.hivini.platformer.GameManager;
import com.hivini.platformer.objects.GameObject;

public abstract class Component {

    protected String tag;

    public abstract void update(GameContainer gc, GameManager gm, float dt);
    public abstract void render(GameContainer gc, Renderer r);

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
