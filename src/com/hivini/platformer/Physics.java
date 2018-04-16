package com.hivini.platformer;

import com.hivini.platformer.components.AABBComponent;

import java.util.ArrayList;

public class Physics {

    private static ArrayList<AABBComponent> aabbComponents = new ArrayList<>();

    public static void addAABBComponent(AABBComponent aabb) {
        aabbComponents.add(aabb);
    }

    public static void update() {
        for (int i = 0; i < aabbComponents.size(); i++) {
            for (int j = i + 1; j < aabbComponents.size(); j++) {
                // Remember, here is the components loop for the collision checking
                AABBComponent c0 = aabbComponents.get(i);
                AABBComponent c1 = aabbComponents.get(j);
                if (Math.abs(c0.getCenterX() - c1.getCenterX()) < c0.getHalftWidth() + c1.getHalftWidth()) {

                    if (Math.abs(c0.getCenterY() - c1.getCenterY()) < c0.getHalfHeight() + c1.getHalfHeight()) {
                        // Telling the objects that they collided each other
                        c0.getParent().collision(c1.getParent());
                        c1.getParent().collision(c0.getParent());
                    }
                }

            }
        }

        aabbComponents.clear();
    }
}
