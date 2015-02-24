package com.lunagameserve.decarbonator.physics;

import org.jetbrains.annotations.NotNull;

/**
 * Created by sixstring982 on 2/21/15.
 */
public class Vector2 {
    private final float x;
    private final float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @NotNull
    public Vector2 add(@NotNull Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    @NotNull
    public Vector2 mult(float scalar) {
        return new Vector2(x * scalar, y * scalar);
    }

    @NotNull
    public Vector2 opposite() {
        return new Vector2(-x, -y);
    }

    @NotNull
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
