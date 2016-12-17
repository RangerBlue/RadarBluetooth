package com.example.kamil.br.database.model;

import android.content.Context;
import android.util.Log;

import com.example.kamil.br.QuadraticFunction;
import com.example.kamil.br.database.controller.PathDataController;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Klasa służąca do opisu krawędzi danego pokoju, każda krawędź jest opisana w postaci półprostej,
 * punkt początkowy, oraz funkcja przechodząca przez ten punkt, punktem końcowym danej krawędzi jest
 * punkt początkowy następnej
 * Created by kamil on 30.07.16.
 */
public class PathData implements Serializable
{
    public static final String TAG = PathData.class.getSimpleName();
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

    /**
     * Identyfikator w bazie
     */
    private int idPathData;

    /**
     * Wspólczynnik przy x w funkcji liniowej
     */
    private float a;

    /**
     * Wolny wyraz w funkcji liniowej
     */
    private float b;

    /**
     * Współczynnik funkcji jeśli nie jest liniowa
     */
    private float x;

    /**
     * Wspólczynnik mówiący o tym czy funkcja jest liniowa
     */
    private int ifLinear;

    /**
     * Wpółrzędna x punktu początkowego
     */
    private float p1;

    /**
     * Wpółrzędna y punktu początkowego
     */
    private float p2;

    /**
     * Numer krawędzi, liczony od 0
     */
    private int edgeNumber;

    /**
     * Identyfikator pokoju, do którego krawędzie się odnoszą
     */
    private int idRooms;

    /**
     * Stosunek długości krawędzi przez czas trwania przejścia tej krawędzi przez
     * użytkownika, używany przy rysowaniu punktów
     */
    private static float ratio;



    /**
     * Stosunek długości krawędzi nr 0  przez czas trwania przejścia tej krawędzi przez
     * użytkownika, używany przy konwertowaniu odległości od urządzenia w metrach do jednostek
     * ukłądy współrzędnych
     */
    private static float walkRatio;

    /**
     * Ilość komórek w polu wyboru narożników
     */
    private static int gridCells;

    /**
     * nie wiem do czego to
     */
    //Todo: ogarnąc do czego jest zdublowany
    private static int roomNumber;

    /**
     * Konstruktor obiektu funkcji liniowej
     * @param a Współczynnik przy x
     * @param b Wolny wyraz funkcji liniowej
     * @param p1 Wpólrzędna x pounktu startowego
     * @param p2 Wpólrzędna y pounktu startowego
     */
    public PathData(float a, float b, float p1, float p2) {
        this.a = a;
        this.b = b;
        this.x = 0;
        this.p1 = p1;
        this.p2 = p2;
        this.ifLinear=1;
    }

    /**
     * Konstrukor obiektu funkcji nieliniowej
     * @param xp1 Wspólczynnik x punktu początkowego(wsółczynnik funkcji nielniowej)
     * @param p2  Wspólczynnik y punktu początkowego
     */
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

    public static float getWalkRatio() {
        return walkRatio;
    }

    public static void setWalkRatio(float walkRatio) {
        PathData.walkRatio = walkRatio;
    }

    /**
     * Zmienia długość krawędzi, w zależności od czasu jej przejścia
     * @param time Czas potrzebny na przebycie danej krawędzi
     * @param firstValue Rekord z bazy opisujący daną krawedź(bez punktu końcowego)
     * @param secondValue Rekord z bazy opisujący daną krawedź(tylko punkt końcowy)
     */
    public static void setNewLength(long time, PathData firstValue, PathData secondValue )
    {
        float oldLength = getSegmentLength(firstValue.getP1(), secondValue.getP1(), firstValue.getP2(), secondValue.getP2()); Log.d(TAG," oldLength: "+Float.toString(oldLength) );
        float newLength = time/ratio;  Log.d(TAG+" newLength",Float.toString(newLength) );
        Log.d(TAG,"Punkt startowy: "+"("+(Float.toString(firstValue.getP1()))+","+Float.toString(firstValue.getP2())+")");
        Log.d(TAG+"Punkt przed zmianą","("+(Float.toString(secondValue.getP1()))+","+Float.toString(secondValue.getP2())+")");

        //sprawdzenie czy liniowa czy nie
        if(firstValue.getIsIfLinear()==1)
            getNewPoint(newLength, firstValue, secondValue);
        else
            getNewPointNotLinear(newLength, firstValue, secondValue);
        //wypisanie zmienonego wiersza
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

    /**
     * Oblicza odległość między dwoma punktami
     * @param x1 wspólrzędna x pierwszego punktu
     * @param x2 współrzędna y pierwszego punktu
     * @param y1 wspólrzędna x drugiego punktu
     * @param y2 wspołrzędna y drugiego punktu
     * @return odległośc
     */
    public static float getSegmentLength(float x1, float x2, float y1, float y2)
    {
        return (float) Math.sqrt(Math.abs(Math.pow((x2-x1),2)+Math.pow((y2-y1),2))) ;
    }

    /**
     * Obliczenie punktu, który znajduje się w podanej odległości od punktu startowego, korzystając z funkcji liniowej
     * @param newLength Podana odległość
     * @param firstValue Rekord z bazy opisujący daną krawedź(bez punktu końcowego)
     * @param secondValue Rekord z bazy opisujący daną krawedź(tylko punkt końcowy)
     */
    private static void getNewPoint(float newLength, PathData firstValue,PathData secondValue)
    {
        Log.d(TAG,"getNewPoint wywołanie");
        float a = firstValue.getA(); Log.d(TAG+ " a", Float.toString(a));
        float b = firstValue.getB(); Log.d(TAG+" b", Float.toString(b));
        float x1 = firstValue.getP1(); Log.d(TAG+ " x1", Float.toString(x1));
        float y1 = firstValue.getP2(); Log.d(TAG+" y1", Float.toString(y1));
        float d = newLength; Log.d(TAG+"d", Float.toString(d));
        float quadraticFunctionA = (1+a*a); Log.d(TAG+" qFA", Float.toString(quadraticFunctionA));
        float quadraticFunctionB = 2*(a*(b-y1)-x1);  Log.d(TAG+" qFB", Float.toString(quadraticFunctionB));
        float quadraticFunctionC = (float) (Math.pow((y1-b),2)+Math.pow(x1,2)-Math.pow(d,2)); Log.d(TAG+"qFC", Float.toString(quadraticFunctionC));
        QuadraticFunction function = new QuadraticFunction(quadraticFunctionA, quadraticFunctionB, quadraticFunctionC);

        //obliczenie rozwiązań równania okręgu
        Point solution1 = new Point(function.getX1(), firstValue.getA(), firstValue.getB()); Log.d(TAG+" solution1", "("+Float.toString(solution1.x)+","+Float.toString(solution1.y)+")");
        Point solution2 = new Point(function.getX2(), firstValue.getA(), firstValue.getB()); Log.d(TAG+" solution2", "("+Float.toString(solution2.x)+","+Float.toString(solution2.y)+")");

        //ustawienie odległosci od nowego punktu to starego punktu
        solution1.setLength(getSegmentLength(secondValue.getP1(), solution1.x, secondValue.getP2(), solution1.y)); Log.d(TAG+" s1 length", Float.toString(solution1.getLength()));
        solution2.setLength(getSegmentLength(secondValue.getP1(), solution2.x, secondValue.getP2(), solution2.y)); Log.d(TAG+" s2 length", Float.toString(solution2.getLength()));

        //wybranie punktu znajdującego się bliżej staregp
        Point closerPoint = Point.getCloserSolution(solution1, solution2);

        //ustawienie nowego punktu
        secondValue.setP1(closerPoint.x); Log.d(TAG+"wynik x", Float.toString(closerPoint.x));
        secondValue.setP2(closerPoint.y); Log.d(TAG+"wynik y", Float.toString(closerPoint.y));

    }

    /**
     * Obliczenie punktu, który znajduje się w podanej odległości od punktu startowego, korzystając z nie funkcji
     * @param newLength Podana odległość
     * @param firstValue Rekord z bazy opisujący daną krawedź(bez punktu końcowego)
     * @param secondValue Rekord z bazy opisujący daną krawedź(tylko punkt końcowy)
     */
    private static void getNewPointNotLinear(float newLength, PathData firstValue,PathData secondValue)
    {
        Log.d(TAG,"getNewPointNotLinear wywołanie");

        Point solution1 = new Point(secondValue.getP1(), firstValue.getP2()+newLength); Log.d(TAG+" solution1", "("+Float.toString(solution1.x)+","+Float.toString(solution1.y)+")");
        Point solution2 = new Point(secondValue.getP1(), firstValue.getP2()-newLength); Log.d(TAG+" solution2", "("+Float.toString(solution2.x)+","+Float.toString(solution2.y)+")");

        solution1.setLength(getSegmentLength(secondValue.getP1(), solution1.x, secondValue.getP2(), solution1.y)); Log.d(TAG+" s1 length", Float.toString(solution1.getLength()));
        solution2.setLength(getSegmentLength(secondValue.getP1(), solution2.x, secondValue.getP2(), solution2.y)); Log.d(TAG+" s2 length", Float.toString(solution2.getLength()));

        Point closerPoint = Point.getCloserSolution(solution1, solution2);

        secondValue.setP1(closerPoint.x); Log.d(TAG+"wynik x", Float.toString(closerPoint.x));
        secondValue.setP2(closerPoint.y); Log.d(TAG+"wynik y", Float.toString(closerPoint.y));

    }


    /**
     * Klasa opisująca punkt i przechowywująca długośc odcinka
     */
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


    /**
     * Zamiana numerów pól na punkty w IV ćwiartce(obiekt PathData) i zapisanie ich do bazy,
     * używana po wybraniu narożnków pomieszczenia w tworzeniu ścieżki
     * @param source lista z numerami pól
     * @param target lista do, której zapisywane są obiekty pathData
     * @param idRooms id pokoju
     * @param applicationContext
     */
    public static void calculateFunctions(ArrayList<Integer> source, ArrayList<PathData> target, int idRooms , Context applicationContext)
    {
        int listLength = source.size();
        PathDataController pathDataController = new PathDataController();
        PathData record;
        //zamiana numeru i kolejnosci dwóch pól(punktów) na funkcje liniową przechodzącą przez 2 punkty

        for (int i = 0; i < listLength - 1; i++)
        {
            target.add(positionToCoefficients(source.get(i), source.get(i + 1)));
            record = target.get(i);
            record.setEdgeNumber(i);
            record.setIdRooms(idRooms);
            pathDataController.insert(record, applicationContext);
        }
        //ostatni punkt z pierwszym
        target.add(positionToCoefficients(source.get(listLength - 1), source.get(0)));
        record = target.get(listLength-1);
        record.setEdgeNumber(listLength-1);
        record.setIdRooms(idRooms);
        pathDataController.insert(record, applicationContext);



        //wypisanie w logu calej listy funkcji
        for (PathData item : target)
        {
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

    /**
     * Zamienia pozycje narożników, na ich opis w ukłądzie współzędnych-półprostą(obiekt PathData)
     * @param position1 Pozycja pierwszego narożnika
     * @param position2 Pozycja drugiego narożnika
     * @return obekt pathData opsujacy daną zależność
     */
    public static PathData positionToCoefficients(int position1, int position2)
    {
        Log.d("Positions", Integer.toString(position1) + ", " + Integer.toString(position2));
        int xPosition1 = getXAxis(position1);
        int yPosition1 = getYAxis(xPosition1, position1);
        int xPosition2 = getXAxis(position2);
        int yPosition2 = getYAxis(xPosition2, position2);

        //sprawdzanie czy funkcja jest "pionowa"
        if (xPosition1 == xPosition2) {
            PathData result = new PathData(xPosition1, yPosition1);
            return result;
        }
        else
        {
            //wzor na funkcję przechodzącą przez 2 punkty
            float a = (yPosition1 - yPosition2) / (float) (xPosition1 - xPosition2);
            float b = (yPosition1 - a * xPosition1);
            PathData result = new PathData(a, b, xPosition1, yPosition1);

            return result;
        }

    }

    /**
     * Zamienia pozycje narożnika na jego miejsce na osi x
     * @param position pozycja narożnika
     * @return pozycja na osi x
     */
    public static int getXAxis(int position) {
        int divider = gridCells;
        int step = (int) Math.sqrt(gridCells);

        if (position == 0)
        {
            return 0;
        }
        else
        if (position < 10)
        {
            return position;
        }
        else
        {
            while ((position % divider) > step) {
                divider -= step;
            }
            return position - divider;
        }


    }

    /**
     * Zamienia pozycje narożnika na jego miejsce na osi y
     * @param xPosition wspólrzędna x narożnika na osi x
     * @param order pozcyja narożnika
     * @return współrzędna narożnika na osi y
     */
    public static int getYAxis(int xPosition, int order) {
        int step = (int) Math.sqrt(gridCells);
        if (order < step - 1) {
            return 0;
        } else {
            //pobranie pierwszej cyfry z integera
            return -Integer.parseInt(Integer.toString(order - xPosition).substring(0, 1));
        }
    }

    /**
     * Uaktualnienie wsółczynników funkcji w obiekcie pathData po zmienianiu długości krawędzi
     * @param firstValue Rekord z bazy opisujący daną krawedź(bez punktu końcowego)
     * @param secondValue Rekord z bazy opisujący daną krawedź(tylko punkt końcowy)
     */
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
