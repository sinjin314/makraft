package com.ynov.core.entity;

import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.Vector;

public class Entity {
    private Model model;
    private Vector3f pos, rotation, hitbox;
    private Vector3i coordinate;
    private float scale;

    public Entity(Model model, Vector3f pos, Vector3i coordinate, Vector3f rotation, float scale, Vector3f hitbox) {
        this.model = model;
        this.pos = pos;
        this.coordinate = coordinate;
        this.rotation = rotation;
        this.scale = scale;
        this.hitbox = hitbox;
    }

    public void incPos(float x, float y, float z) {
        this.pos.x += x;
        this.pos.y += y;
        this.pos.z += z;
    }

    public void setPos(float x, float y, float z) {
        this.pos.x = x;
        this.pos.y = y;
        this.pos.z = z;
    }

    public void incRotation(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Model getModel() {
        return model;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public Vector3f getHitbox() {
        return hitbox;
    }

    public Vector3i getCoordinate() {
        return coordinate;
    }
}
