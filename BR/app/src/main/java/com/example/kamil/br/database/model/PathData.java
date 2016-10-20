package com.example.kamil.br.database.model;

import android.nfc.Tag;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.kamil.br.QuadraticFunction;

import java.io.Serializable;

/**
 * Created by kamil on 30.07.16.
 */
public class PathData implements Serializable
{
    public static final String TAG = "PathData";
    public static final String TABLE = "PathData";

    public static final String ID_PATHDATA = "idpathadata";
    public static final String A = "a";
    public static final String B = "b";
    public static final String X = "x";
    public static final String LINEAR = "linear";
    public static final String P1 = "p1";
    public static final String P2 = "p2";
    public static final String EDGENUMBER = "edgenumber";
    public static final String ID_ROOMS = "idrooms";

    //nazwy pól
    private int idPathData;
    private float a;
    private float b;
    private float x;
    private int ifLinear;
    private float p1;
    private float p2;
    private int edgeNumber;
    private int idRooms;

    //stosunek dlugosc 0 odcinka/czas trwania pierwszego odcinka
    private static float ratio;

    public static float getRatio() {
        return ratio;
    }

    public static void setRatio(float ratio) {
        PathData.ratio = ratio;
    }

    public int getIdPathData() {
        return idPathData;
    }

    public void setIdPathData(int idPathData) {
        this.idPathData = idPathData;
    }



    public int getIdRooms() {
        return idRooms;
    }

    public void setIdRooms(int idRooms) {
        this.idRooms = idRooms;
    }

    public PathData() {
    }

    public int getEdgeNumber() {
        return edgeNumber;


    }

    public void setEdgeNumber(int edgeNumber) {
        this.edgeNumber = edgeNumber;
    }

    public float getP1() {
        return p1;
    }

    public void setP1(float p1) {
        this.p1 = p1;
    }

    public float getP2() {
        return p2;
    }

    public float getP2Reverse(){return -p2;}

    public void setP2(float p2) {
        this.p2 = p2;
    }



    public int getIsIfLinear() {
        return ifLinear;
    }

    public float getX() {
        return x;
    }

    public void setIfLinear(int ifLinear) {
        this.ifLinear = ifLinear;
    }

    public void setX(float x) {
        this.x = x;
    }

    public PathData(float a, float b, int p1, int p2) {
        this.a = a;
        this.b = b;
        this.p1 = p1;
        this.p2 = p2;
        this.ifLinear=1;
    }

    public PathData(float x, int p1, int p2) {
        this.x = x;
        this.p1 = p1;
        this.p2 = p2;
        this.ifLinear=0;
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

    public static void setNewPoint(long time, PathData firstValue, PathData secondValue )
    {
        float oldlenght = getSegmentLenght(firstValue.getP1(), secondValue.getP1(), firstValue.getP2(), secondValue.getP2()); Log.d(TAG+" oldLength",Float.toString(oldlenght) );
        float newLenght = time/ratio;  Log.d(TAG+" newLength",Float.toString(newLenght) );
        Log.d(TAG+" starting point","("+(Float.toString(firstValue.getP1()))+","+Float.toString(firstValue.getP2())+")");
        Log.d(TAG+" stary punkt","("+(Float.toString(secondValue.getP1()))+","+Float.toString(secondValue.getP2())+")");

        if(firstValue.getIsIfLinear()==1)
            getNewPoint(newLenght, firstValue, secondValue);
        else
            getNewPointNotLinear(newLenght, firstValue, secondValue);

        Log.d(TAG,
                    "ID "+String.valueOf(secondValue.getIdPathData()) +
                            " A "+ secondValue.getA() +
                            " B "+ secondValue.getB() +
                            " X "+ secondValue.getX() +
                            " Linear "+ secondValue.getIsIfLinear() +
                            " P1 "+ secondValue.getP1() +
                            " P2 "+ secondValue.getP2() +
                            " EdgeNumbers "+ secondValue.getEdgeNumber() +
                            " IdRooms "+ secondValue.getIdRooms()
            ) ;


    }

    public static float getSegmentLenght(float x1, float x2, float y1, float y2)
    {
        return (float) Math.sqrt(Math.abs(Math.pow((x2-x1),2)+Math.pow((y2-y1),2))) ;
    }

    public static void getNewPoint(float newLength, PathData firstValue,PathData secondValue)
    {
        Log.d(TAG+"getNewPoint","wywołanie");
        float a = firstValue.getA(); Log.d(TAG+ " a", Float.toString(a));
        float b = firstValue.getB(); Log.d(TAG+" b", Float.toString(b));
        float x1 = firstValue.getP1(); Log.d(TAG+ " x1", Float.toString(x1));
        float y1 = firstValue.getP2(); Log.d(TAG+" y1", Float.toString(y1));
        float d = newLength; Log.d(TAG+"d", Float.toString(d));
        float quadraticFunctionA = (1+a*a); Log.d(TAG+" qFA", Float.toString(quadraticFunctionA));
        float quadraticFunctionB = 2*(a*(b-y1)-x1);  Log.d(TAG+" qFB", Float.toString(quadraticFunctionB));
        float quadraticFunctionC = (float) (Math.pow((y1-b),2)+Math.pow(x1,2)-Math.pow(d,2)); Log.d(TAG+"qFC", Float.toString(quadraticFunctionC));
        QuadraticFunction function = new QuadraticFunction(quadraticFunctionA, quadraticFunctionB, quadraticFunctionC);


        Point solution1 = new Point(function.getX1(), firstValue.getA(), firstValue.getB()); Log.d(TAG+" solution1", "("+Float.toString(solution1.x)+","+Float.toString(solution1.y)+")");
        Point solution2 = new Point(function.getX2(), firstValue.getA(), firstValue.getB()); Log.d(TAG+" solution2", "("+Float.toString(solution2.x)+","+Float.toString(solution2.y)+")");

        //ustawienie odległosci od nowego punktu to strego punktu
        solution1.setLength(getSegmentLenght(secondValue.getP1(), solution1.x, secondValue.getP2(), solution1.y)); Log.d(TAG+" s1 length", Float.toString(solution1.getLength()));
        solution2.setLength(getSegmentLenght(secondValue.getP1(), solution2.x, secondValue.getP2(), solution2.y)); Log.d(TAG+" s2 length", Float.toString(solution2.getLength()));

        Point closerPoint = Point.getCloserSolution(solution1, solution2);

        secondValue.setP1(closerPoint.x); Log.d(TAG+"wynik x", Float.toString(closerPoint.x));
        secondValue.setP2(closerPoint.y); Log.d(TAG+"wynik y", Float.toString(closerPoint.y));

    }

    public static void getNewPointNotLinear(float newLength, PathData firstValue,PathData secondValue)
    {
        Log.d(TAG,"getNewPointNotLinear wywołanie");

        Point solution1 = new Point(secondValue.getP1(), firstValue.getP2()+newLength); Log.d(TAG+" solution1", "("+Float.toString(solution1.x)+","+Float.toString(solution1.y)+")");
        Point solution2 = new Point(secondValue.getP1(), firstValue.getP2()-newLength); Log.d(TAG+" solution2", "("+Float.toString(solution2.x)+","+Float.toString(solution2.y)+")");

        solution1.setLength(getSegmentLenght(secondValue.getP1(), solution1.x, secondValue.getP2(), solution1.y)); Log.d(TAG+" s1 length", Float.toString(solution1.getLength()));
        solution2.setLength(getSegmentLenght(secondValue.getP1(), solution2.x, secondValue.getP2(), solution2.y)); Log.d(TAG+" s2 length", Float.toString(solution2.getLength()));

        Point closerPoint = Point.getCloserSolution(solution1, solution2);

        secondValue.setP1(closerPoint.x); Log.d(TAG+"wynik x", Float.toString(closerPoint.x));
        secondValue.setP2(closerPoint.y); Log.d(TAG+"wynik y", Float.toString(closerPoint.y));

    }





    //klasa wewnętrzna
    public static class Point
    {
        private float x;
        private float y;
        private float length;

        public float getLength() {
            return length;
        }


        public void setLength(float length) {
            this.length = length;
        }

        public Point(float x, float a, float b)
        {
            this.x = x;
            this.y = a*x+b;
        }

        public Point( float x, float y)
        {
            this.x = x;
            this.y = y;
        }

        public static Point getCloserSolution(Point point1, Point point2)
        {
            return (point1.getLength()<point2.getLength()) ? point1:point2;
        }
    }

}
