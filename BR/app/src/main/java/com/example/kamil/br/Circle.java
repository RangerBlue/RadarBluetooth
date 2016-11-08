package com.example.kamil.br;

/**
 * Opisuje okrąg w układzie wsółrzędnych
 * Created by Kamil on 2016-11-03.
 */

public class Circle
{
    /**
     * Współrzędna na osi x
     */
    private float a;

    /**
     * Współrzędna na osi y
     */
    private float b;

    /**
     * Promień okręgu
     */
    private float r;


    public Circle(float a, float b, float r) {
        this.a = a;
        this.b = b;
        this.r = r;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }
}
