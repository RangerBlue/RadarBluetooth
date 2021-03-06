package com.example.kamil.br.math;

import android.util.Log;


/**
 * Zapewnia rozwiązanie równania kwadratowego,
 * Created by Kamil on 2016-10-01.
 * @author Kamil
 */
public class QuadraticFunction
{
    private String TAG = getClass().getSimpleName();

    /**
     * współczynnik przy x^2
     */
    private float a;

    /**
     * współczynnik przy x
     */
    private float b;

    /**
     * wyraz wolny
     */
    private float c;

    /**
     * delta
     */
    private float delta;

    /**
     * rozwiązanie nr 1
     */
    private float x1;

    /**
     * rozwiązanie nr 2
     */
    private float x2;

    public QuadraticFunction() {

    }

    /**
     * Konstruktor tworzący obiekt z równania a*x^2+b*x+c
     * @param a wartość przy x^2
     * @param b wartość przy x
     * @param c wartośc wyrazów wolnych
     */
    public QuadraticFunction(float a, float b, float c) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.delta = calculateDelta(a,b,c);
        Point point = calculateSolutions(a,b,delta);
        this.x1 = point.getA();
        this.x2 = point.getB();
    }

    /**
     * Oblicza deltę i ją zwraca
     * @param a wartość przy x^2
     * @param b wartość przy x
     * @param c wartośc wyrazów wolnych
     * @return obliczona delta
     */
    private float calculateDelta(float a, float b, float c) throws ArithmeticException
    {
        float delta = b*b - 4*a*c; //Log.d(TAG +" delta", Float.toString(delta));
        if( delta<0 ) throw new ArithmeticException();
        return delta;
    }

    /**
     * Oblicza i zwraca rozwiązania równania kwadratowego
     * @param a wartość przy x^2
     * @param b wartość przy x
     * @param delta delta równania, @see QadraticFunction#calculateDelta()
     *
     * @return obliczona delta
     */
    private Point calculateSolutions(float a, float b, float delta)
    {
        Point result = new Point();
        result.setA((float) ((-b - Math.sqrt(delta))/(2*a)));
        result.setB((float) ((-b + Math.sqrt(delta))/(2*a)));
       // Log.d(TAG +" x1", Float.toString(result.getA()));
        //Log.d(TAG +" x2", Float.toString(result.getB()));

        return result;
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

    /**
     * Punkt o dwóch współrzędnych w układzie o osiach x i y
     * @author Kamil
     */
    public class Point
    {
        float a;
        float b;


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
    }
}
