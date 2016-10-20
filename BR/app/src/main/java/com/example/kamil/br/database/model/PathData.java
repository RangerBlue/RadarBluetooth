package com.example.kamil.br.database.model;

import android.content.Context;
import android.nfc.Tag;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.kamil.br.QuadraticFunction;
import com.example.kamil.br.database.controller.PathDataController;

import java.io.Serializable;
import java.util.ArrayList;

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

    private static int gridCells;

    private static int roomNumber;

    public PathData(float a, float b, float p1, float p2) {
        this.a = a;
        this.b = b;
        this.x = 0;
        this.p1 = p1;
        this.p2 = p2;
        this.ifLinear=1;
    }

    public PathData(float xp1, float p2) {
        this.a = 0;
        this.b = 0;
        this.x = xp1;
        this.p1 = xp1;
        this.p2 = p2;
        this.ifLinear=0;
    }

    public static int getRoomNumber() {
        return roomNumber;
    }

    public static void setRoomNumber(int roomNumber) {
        PathData.roomNumber = roomNumber;
    }

    public static int getGridCells() {
        return gridCells;
    }

    public static void setGridCells(int gridCells) {
        PathData.gridCells = gridCells;
    }

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



    public  int getIdRooms() {
        return idRooms;
    }

    public  void setIdRooms(int idRooms) {
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


    public static void calculateNewPoint(long time, PathData firstValue, PathData secondValue )
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
        //float quadraticFunctionC = (float) (Math.pow((b),2)+Math.pow(x1,2)-Math.pow(d,2)-2*(b*y1)); Log.d(TAG+"qFC", Float.toString(quadraticFunctionC));
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



    //obliczanie funkcji liniowych
    public static void calculateFunctions(ArrayList<Integer> source, ArrayList<PathData> target, Context applicationContext) {
        int listLenght = source.size();
        //zamiana numeru i kolejnosci dwóch pól(punktów) na funkcje liniową przechodzącą przez 2 punkty
        for (int i = 0; i < listLenght - 1; i++) {
            target.add(positionAndOrderToCoefficients(source.get(i), source.get(i + 1)));
        }
        //ostatni punkt z pierwszym
        target.add(positionAndOrderToCoefficients(source.get(listLenght - 1), source.get(0)));


        PathDataController pathDataController = new PathDataController();

        //wypisanie w logu calej listy funkcji
        int i=0;
        for (PathData item : target)
        {
            //dodanie klucza obcego oraz edgeNumber do obiektu room
            item.setEdgeNumber(i); i++;
            item.setIdRooms(roomNumber);
            //umieszczenie wszystkich danych w bazie

            pathDataController.insert(item, applicationContext);

            Log.d("Dane: : ",
                    "a:" + Float.toString(item.getA()) +
                            "|b:" + Float.toString(item.getB()) +
                            "|x:" + Float.toString(item.getX()) +
                            "|linear:" + Float.toString(item.getIsIfLinear()) +
                            "|p1:" + Float.toString(item.getP1()) +
                            "|p2:" + Float.toString(item.getP2()) +
                            "|edgeNumber:" + Integer.toString(item.getEdgeNumber())+
                            "|idRooms:" + Integer.toString(item.getIdRooms()));
        }
    }

    //zamienia pozycje dwóch pól na współczynniki funkcji liniowej
    public static PathData positionAndOrderToCoefficients(int position1, int position2) {
        Log.d("Positions", Integer.toString(position1) + ", " + Integer.toString(position2));
        int xposition1 = getXAxis(position1);
        int yposition1 = getYAxis(xposition1, position1);
        int xposition2 = getXAxis(position2);
        int yposition2 = getYAxis(xposition2, position2);

        //sprawdzanie czy funkcja jest "pionowa"
        if (xposition1 == xposition2) {
            PathData result = new PathData(xposition1, yposition1);
            return result;
        }
        else
        {
            //wzor na funkcję przechodzącą przez 2 punkty
            float a = (yposition1 - yposition2) / (float) (xposition1 - xposition2);
            float b = (yposition1 - a * xposition1);
            PathData result = new PathData(a, b, xposition1, yposition1);

            return result;
        }

    }

    //oblicza współrzędną na osi x
    public static int getXAxis(int order) {
        int divider = gridCells;
        int step = (int) Math.sqrt(gridCells);

        if (order == 0)
        {
            return 0;
        }
        else
        if (order < 10)
        {
            return order;
        }
        else
        {
            while ((order % divider) > step) {
                divider -= step;
            }
            return order - divider;
        }


    }

    //oblicza współrzedna na osi y
    public static int getYAxis(int xposition, int order) {
        int step = (int) Math.sqrt(gridCells);
        if (order < step - 1) {
            return 0;
        } else {
            //pobranie pierwszej cyfry z integera
            return -Integer.parseInt(Integer.toString(order - xposition).substring(0, 1));
        }
    }

    public static void setNewCoefficients(PathData firstValue, PathData secondValue) {
        Log.d(TAG, "wywołanie setNewCoefficients");
        float x1 = firstValue.getP1();
        float y1 = firstValue.getP2();
        float x2 = secondValue.getP1();
        float y2 = secondValue.getP2();

        //sprawdzanie czy funkcja jest "pionowa"
        if (x1 == x2)
        {
            PathData result = new PathData(x1, y1);
            firstValue.setA(result.getA());
            firstValue.setB(result.getB());
            firstValue.setX(result.getX());
            firstValue.setIfLinear(result.getIsIfLinear());
        }
        else
        {
            //wzor na funkcję przechodzącą przez 2 punkty
            float a = (y1 - y2) / (x1 - x2);
            float b = (y1 - a * x1);
            PathData result = new PathData(a, b, x1, y1);
            firstValue.setA(result.getA());
            firstValue.setB(result.getB());
            firstValue.setX(result.getX());
            firstValue.setIfLinear(result.getIsIfLinear());
        }

    }

}
