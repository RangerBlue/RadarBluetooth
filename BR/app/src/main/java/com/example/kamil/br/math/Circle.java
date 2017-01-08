package com.example.kamil.br.math;

import android.util.Log;

import com.example.kamil.br.database.model.PathData;

import java.util.ArrayList;
import java.util.Collections;

import com.example.kamil.br.math.QuadraticFunction.Point;

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

    /**
     * Zwraca punkty przecięcia dwóch okręgów
     * @param circle1 Pierwszy okrąg
     * @param circle2 Drugi okrąg
     * @return lista punktów
     */
    public static ArrayList<Point> getIntersectionPointsOfTwoCircles(Circle circle1 , Circle circle2)
    {
        ArrayList returnList = new ArrayList();
        returnList.ensureCapacity(2);

        float a = circle1.getA();
        float b = circle1.getB();
        float r1 = circle1.getR();

        float c = circle2.getA();
        float d = circle2.getB();
        float r2 = circle2.getR();

        if(isCirclesCrossed(circle1, circle2))
        {
            QuadraticFunction container = new QuadraticFunction();
            //jesli rozwiązaniem będzie funkcjia "nielioniwa"
            if(b == d)
            {
                float x = (a + c)/2;

                //postać równania kwadratowego do otrzymania rozwiązań
                float resultEquation_b = (-2) * b ;
                float resultEquation_c = (float) (Math.pow((x-a), 2) + Math.pow((b), 2) - Math.pow(r1, 2));
                QuadraticFunction result = new QuadraticFunction(1, resultEquation_b, resultEquation_c);

                //rozwiązania
                Point point1= container.new Point();
                Point point2= container.new Point();

                double p1x = x;
                double p2x = x;

                double p1y = result.getX1();

                double p2y = result.getX2();




                point1.setA((float) p1x);
                point2.setA((float) p2x);

                point1.setB((float) p1y);
                point2.setB((float) p2y);


                returnList.add(point1);
                returnList.add(point2);


            }
            else
            {
                //równanie y= a_x + b_
                float a_ = (a-c)/(d-b);

                float b_ = (float) ((Math.pow(c, 2) - Math.pow(a, 2) - Math.pow(b, 2) + Math.pow(d, 2)+ Math.pow(r1, 2)-Math.pow(r2, 2)))/(2*(d-b));


                //postać równania kwadratowego do otrzymania rozwiązań
                float resultEquation_a = (float) (1 + Math.pow(a_, 2));

                float resultEquation_b = (float) 2*(a_*b_ - a - b*a_);

                float resultEquation_c = (float) (Math.pow(a, 2)+ Math.pow((b_ - b), 2) - Math.pow(r1, 2));

                QuadraticFunction result = new QuadraticFunction(resultEquation_a, resultEquation_b, resultEquation_c);


                //rozwiązania
                Point point1= container.new Point();
                Point point2= container.new Point();

                float p1x = result.getX1();

                float p2x = result.getX2();



                float p1y = a_*p1x + b_;

                float p2y = a_*p2x + b_;


                point1.setA((float) p1x);
                point2.setA((float) p2x);

                point1.setB((float) p1y);
                point2.setB((float) p2y);

                returnList.add(point1);
                returnList.add(point2);

            }
        }
        else
        {

            QuadraticFunction container = new QuadraticFunction();
            Point point1= container.new Point();
            Point point2= container.new Point();

            point1.setA((float) circle1.getA());
            point2.setA((float) circle2.getA());

            point1.setB((float) circle1.getB());
            point2.setB((float) circle2.getB());
            float addLength = (PathData.getSegmentLength(point1.getA(), point2.getA(), point1.getB(), point2.getB()) - circle1.getR() - circle2.getR())/2;


            Circle circlePlus1 = new Circle(a, b, r1 + addLength);
            Circle circlePlus2 = new Circle(c, d, r2 + addLength);

            ArrayList<QuadraticFunction.Point> result;
            result = getIntersectionPointsOfTwoCircles(circlePlus1,circlePlus2);



            returnList.add(result.get(0));
            returnList.add(result.get(1));
        }
        return returnList;

    }

    /**
     * Zwraca liste punktów przecięcia trzech okręgów
     * @param first Pierwszy okrąg
     * @param second Drugi okrąg
     * @param third Trzeci okrąg
     * @return lista punktów
     */
    public static ArrayList<QuadraticFunction.Point> getIntersectionPointsOfThreeCircles(Circle first, Circle second, Circle third)
    {
        ArrayList<QuadraticFunction.Point> result = new ArrayList<>();
        result.ensureCapacity(6);

        result.addAll(getIntersectionPointsOfTwoCircles(first, second));
        result.addAll(getIntersectionPointsOfTwoCircles(second, third));
        result.addAll(getIntersectionPointsOfTwoCircles(third, first));

        return result;
    }

    /**
     * Sprawdza czy okręgi pzecinają się
     * @param circle1 pierwszy okrąg
     * @param circle2 drugi okrąg
     * @return true or false
     */
    private static boolean isCirclesCrossed(Circle circle1, Circle circle2)
    {
        float distanceBetweenCenterOfCircles = (float) Math.sqrt(Math.abs(Math.pow((circle2.getA()-circle1.getA()),2)+Math.pow((circle2.getB()-circle1.getB()),2)));
        float radiusSum = circle1.getR() + circle2.getR();

        if( distanceBetweenCenterOfCircles > radiusSum)
            return false;
        else
            return true;

    }

    public static QuadraticFunction.Point centerOfTriangle(QuadraticFunction.Point p1, QuadraticFunction.Point p2, QuadraticFunction.Point p3){

        QuadraticFunction container = new QuadraticFunction();
        float centerX = (p1.getA()+p2.getA()+p3.getA())/3;
        float centerY = (p1.getB()+p2.getB()+p3.getB())/3;
        QuadraticFunction.Point center = container.new Point();
        center.setA(centerX);
        center.setB(centerY);
        return center;
    }

    public static QuadraticFunction.Point centerOfPolygon(QuadraticFunction.Point p1, QuadraticFunction.Point p2, QuadraticFunction.Point p3, QuadraticFunction.Point p4){

        QuadraticFunction.Point c1=centerOfTriangle(p1, p2, p4);
        QuadraticFunction.Point c2=centerOfTriangle(p2, p3, p4);
        float centerX = (c1.getA()+c2.getA())/2;
        float centerY = (c1.getB()+c2.getB())/2;
        QuadraticFunction container = new QuadraticFunction();
        QuadraticFunction.Point center = container.new Point();
        center.setA(centerX);
        center.setB(centerY);
        return center;
    }

    /**
     * Metoda do wyznaczania urządzenia w przestrzeni
     * przyjmuje liste okręgów
     * zwraca punkt w którym powinno znajdować się urządzenie
     * @param tab
     * @return
     */
    public static Point multiCircle(ArrayList<Circle> tab){
        QuadraticFunction container = new QuadraticFunction();
        Point center = container.new Point();
        if(tab.size()==3){
            ArrayList<Point> test1;
            ArrayList<PointsDistance> wyniki ;
            ArrayList<Point> end = new ArrayList<>();
            Point finalPoint = container.new Point();

            test1 = getIntersectionPointsOfThreeCircles(tab.get(0), tab.get(1), tab.get(2));
            wyniki = Circle.getClosestPoint(test1);
            wyniki = Circle.getTriangleOfResult(wyniki);
            end = Circle.pairsOfPointsToPoints(wyniki);
            finalPoint = Circle.centerOfTriangle(end.get(0), end.get(1), end.get(2));
            Log.d("multiccore", "("+finalPoint.getA()+","+finalPoint.getB()+")");
            return finalPoint;


        }else if(tab.size()==99999){
            //tutaj dokleje później
        }else{
            ArrayList<Point> cross = new ArrayList<>();
            ArrayList<PointsDistance> wyniki ;

            for(int i=0; i<tab.size()-2; i++){
                for(int j=i+1; j<tab.size()-1; j++){
                    for(int k=j+1; k<tab.size(); k++){
                        cross = getIntersectionPointsOfThreeCircles(tab.get(i), tab.get(j), tab.get(k));
                    }
                }
            }
            //wyniki = Circle.getClosestPoint(cross);
            Point wynik = arithmeticMean(cross);
            Log.d("multiccore", "("+wynik.getA()+","+wynik.getB()+")");
            return wynik;

        }
        Log.d("multiccore", "("+center.getA()+","+center.getB()+")");
        return center;

    }

    /**
     * wyliczanie średniej artmetycznej
     * @param point
     * @return
     */

    public static Point arithmeticMean(ArrayList<Point> point)
    {
        float sumx = 0;
        float sumy = 0;
        float ile = point.size();
        QuadraticFunction container = new QuadraticFunction();
        Point wynik = container.new Point();

        for( Point item : point )
        {
            sumx+=item.getA();
            sumy+=item.getB();
        }
        wynik.setA(sumx/ile);
        wynik.setB(sumy/ile);
        return wynik;
    }

    public static ArrayList<PointsDistance> getClosestPoint(ArrayList<Point> listOfPoints)
    {
        ArrayList<PointsDistance> distanceBetweenPoints = new ArrayList<>();
        for(int i=0; i<listOfPoints.size()-1;i++){
            for(int j=1+i; j<listOfPoints.size();j++){
                distanceBetweenPoints.add(new PointsDistance(listOfPoints.get(i), listOfPoints.get(j)));
            }
        }
        return distanceBetweenPoints;
    }

    public static ArrayList<PointsDistance>getTriangleOfResult(ArrayList<PointsDistance> closestPoints)
    {
        ArrayList<PointsDistance> result = new ArrayList<>();
        Collections.sort(closestPoints);

        for(int i=0; i<3 ; i++)
        {
            result.add(closestPoints.get(i));
        }

        return result;
    }

    public static ArrayList<Point> pairsOfPointsToPoints(ArrayList<PointsDistance> list)
    {
        ArrayList<Point> returnList = new ArrayList<>();
        returnList.ensureCapacity(3);
        QuadraticFunction container= new QuadraticFunction();

        for( PointsDistance item : list )
        {
            Point p = container.new Point();
            p.setA((item.getP1().getA()+item.getP2().getA())/2);
            p.setB((item.getP1().getB()+item.getP2().getB())/2);
            returnList.add(p);

        }
        return  returnList;
    }

    public static class PointsDistance implements Comparable<PointsDistance>
    {
        private float distance;
        private Point p1;
        private Point p2;

        public PointsDistance() {
        }

        public PointsDistance(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance =PathData.getSegmentLength(p1.getA(), p2.getA(), p1.getB(), p2.getB());
        }



        public PointsDistance(float distance, Point p1, Point p2) {
            this.distance = distance;
            this.p1 = p1;
            this.p2 = p2;
        }

        public float getDistance() {
            return distance;
        }

        public void setDistance(float distance) {
            this.distance = distance;
        }

        public Point getP1() {
            return p1;
        }

        public void setP1(Point p1) {
            this.p1 = p1;
        }

        public Point getP2() {
            return p2;
        }

        public void setP2(Point p2) {
            this.p2 = p2;
        }

        @Override
        public int compareTo(PointsDistance t) {
            if(distance == t.getDistance())
                return 0;
            else if(distance < t.getDistance())
                return -1;
            else
                return 1;
        }



    }

    public static void printAllTableToLog(ArrayList<Circle> list)
    {
        Log.d("Tablica", "z okręgami");
        for(Circle item : list)
        {
            Log.d("Lista: ",
                    "\t|X| "+String.valueOf(item.getA()) +
                            "\t|Y| "+ String.valueOf(item.getB()) +
                            "\t|R| "+ String.valueOf(item.getR())
            ) ;
        }
    }
}
