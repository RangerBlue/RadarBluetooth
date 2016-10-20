package com.example.kamil.br;

import android.util.Log;

/**
 * Created by Kamil on 2016-10-01.
 */
public class QuadraticFunction
{
    private String TAG = "QuadraticFunction";
    private float a;
    private float b;
    private float c;
    private float delta;
    private float x1;
    private float x2;

    public QuadraticFunction(float a, float b, float c) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.delta = calculateDelta();
        calculateSolutions();
    }

    private float calculateDelta()
    {
        float delta = this.b*this.b - 4*this.a*this.c; Log.d(TAG +" delta", Float.toString(delta));
        return delta;
    }

    private void calculateSolutions()
    {
        Log.d(TAG +" deltasqrt1", Double.toString(Math.sqrt(delta)));
        Log.d(TAG +" b", Float.toString(b));
        Log.d(TAG +" 2a", Float.toString(2*a));
        float x1 = (float) ((-b - Math.sqrt(delta))/(2*a)); Log.d(TAG +" x1", Float.toString(x1));
        float x2 = (float) ((-b + Math.sqrt(delta))/(2*a)); Log.d(TAG +" x2", Float.toString(x2));
        this.x1 = x1;
        this.x2 = x2;
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

    public float getC() {
        return c;
    }

    public void setC(float c) {
        this.c = c;
    }

    public float getDelta() {
        return delta;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }
}
