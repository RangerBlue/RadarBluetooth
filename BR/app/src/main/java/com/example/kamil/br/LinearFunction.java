package com.example.kamil.br;

/**
 * Created by kamil on 30.07.16.
 */
public class LinearFunction
{
    private float a;
    private float b;
    private float x;
    private boolean ifLinear;

    public boolean getIsIfLinear() {
        return ifLinear;
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public LinearFunction(float a, float b) {
        this.a = a;
        this.b = b;
        this.ifLinear=true;
    }

    public LinearFunction(float x) {
        this.x = x;
        this.ifLinear=false;
    }

    public float getB() {
        return b;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

    public void setB(float b) {
        this.b = b;
    }
}
