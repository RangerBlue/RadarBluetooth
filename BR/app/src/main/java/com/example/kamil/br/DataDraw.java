package com.example.kamil.br;

/**
 * Created by kamil on 16.05.16.
 */
public class DataDraw
{
    private float edges[];
    private float left[];
    private float up[];
    private float right[];
    private float down[];

    public DataDraw(float[] edges, float[] down, float[] left, float[] up, float[] right) {
        this.edges = edges;
        this.down = down;
        this.left = left;
        this.up = up;
        this.right = right;
    }

    public float[] getDown() {
        return down;
    }

    public float[] getEdges() {
        return edges;
    }

    public float[] getLeft() {
        return left;
    }

    public float[] getRight() {
        return right;
    }

    public float[] getUp() {
        return up;
    }

    public void setDown(float[] down) {
        this.down = down;
    }

    public void setEdges(float[] edges) {
        this.edges = edges;
    }

    public void setLeft(float[] left) {
        this.left = left;
    }

    public void setRight(float[] right) {
        this.right = right;
    }

    public void setUp(float[] up) {
        this.up = up;
    }
}
