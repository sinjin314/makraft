package com.ynov.core.entity;

public class Model {

    private int id;
    private int vertexCount;
    private Material material;

    private float[] vertexArr;

    public Model(int id, int vertexCount) {
        this.id = id;
        this.vertexCount = vertexCount;
        this.material = new Material();
        this.vertexArr = null;
    }

    public Model(int id, int vertexCount, Texture texture, float[] vertexArr) {
        this.id = id;
        this.vertexCount = vertexCount;
        this.material = new Material(texture);
        this.vertexArr = vertexArr;
    }

    public Model(Model model, Texture texture) {
        this.id = model.getId();
        this.vertexCount = model.getVertexCount();
        this.material = model.getMaterial();
        this.material.setTexture(texture);
        this.vertexArr = model.getVertexArr();
    }

    public int getId() {
        return id;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Texture getTexture() {
        return material.getTexture();
    }

    public void setTexture(Texture texture) {
        this.material.setTexture(texture);
    }

    public void setTexture(Texture texture, float reflectance) {
        this.material.setTexture(texture);
        this.material.setReflectance(reflectance);
    }

    public float[] getVertexArr() {
        return vertexArr;
    }
}
