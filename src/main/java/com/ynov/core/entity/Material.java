package com.ynov.core.entity;

import com.ynov.core.utils.Constants;
import org.joml.Vector4f;

public class Material {

    private Vector4f ambientColour, diffuseColor, specularColour;

    private float reflectance;
    private Texture texture;

    public Material() {
        this.ambientColour = Constants.DEFAULT_COLOUR;
        this.diffuseColor = Constants.DEFAULT_COLOUR;
        this.specularColour = Constants.DEFAULT_COLOUR;
        this.texture = null;
        this.reflectance = 0;
    }

    public Material(Vector4f colour, float reflectance) {
        this(colour, colour, colour, reflectance, null);
    }

    public Material(Vector4f colour, float reflectance, Texture texture) {
        this(colour, colour, colour, reflectance, texture);
    }

    public Material(Texture texture) {
        this(Constants.DEFAULT_COLOUR, Constants.DEFAULT_COLOUR, Constants.DEFAULT_COLOUR, 0, texture);
    }

    public Material(Vector4f ambientColour, Vector4f diffuseColor, Vector4f specularColour, float reflectance, Texture texture) {
        this.ambientColour = ambientColour;
        this.diffuseColor = diffuseColor;
        this.specularColour = specularColour;
        this.reflectance = reflectance;
        this.texture = texture;
    }

    public Vector4f getAmbientColour() {
        return ambientColour;
    }

    public void setAmbientColour(Vector4f ambientColour) {
        this.ambientColour = ambientColour;
    }

    public Vector4f getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(Vector4f diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public Vector4f getSpecularColour() {
        return specularColour;
    }

    public void setSpecularColour(Vector4f specularColour) {
        this.specularColour = specularColour;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean hasTexture() {
        return texture != null;
    }


}
