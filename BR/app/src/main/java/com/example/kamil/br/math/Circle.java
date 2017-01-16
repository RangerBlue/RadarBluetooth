package com.example.kamil.br.math;

import android.util.Log;

import com.example.kamil.br.database.model.PathData;
import com.example.kamil.br.math.QuadraticFunction.Point;

import java.util.ArrayList;

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
            //jesli rozwiązaniem nie będzie funkcja
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

                point1.setA(p1x);
                point2.setA(p2x);

                point1.setB(p1y);
                point2.setB(p2y);

                returnList.add(point1);
                returnList.add(point2);

            }
        }
        else
        {
            //okręgi nie przecinają się
            QuadraticFunction container = new QuadraticFunction();
            Point point1= container.new Point();
            Point point2= container.new Point();

            point1.setA( circle1.getA());
            point2.setA( circle2.getA());

            point1.setB(circle1.getB());
            point2.setB(circle2.getB());
            float distanceBetweenCircles = (PathData.getSegmentLength(point1.getA(), point2.getA(), point1.getB(), point2.getB()));
            float addLength  = distanceBetweenCircles/(circle1.getR()+circle2.getR());
            addLength+=addLength*0.1f;
            Circle circlePlus1 = new Circle(a, b, r1 * addLength);
            Circle circlePlus2 = new Circle(c, d, r2 * addLength);

            ArrayList<Point> result;
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
    public static ArrayList<Point> getIntersectionPointsOfThreeCircles(Circle first, Circle second, Circle third)
    {
        ArrayList<Point> result = new ArrayList<>();
        ArrayList<Point> temp;
        result.ensureCapacity(3);

        temp = getIntersectionPointsOfTwoCircles(first, second);
        result.add(getAveragePoint(temp.get(0), temp.get(1)));

        temp = getIntersectionPointsOfTwoCircles(second, third);
        result.add(getAveragePoint(temp.get(0), temp.get(1)));

        temp = getIntersectionPointsOfTwoCircles(third, first);
        result.add(getAveragePoint(temp.get(0), temp.get(1)));

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

    /**
     * Zwraca środek cięzkości trójkąta
     * @param p1 punkt 1
     * @param p2 punkt 2
     * @param p3 punkt 3
     * @return środek ciężkości
     */
    public static QuadraticFunction.Point centerOfTriangle(QuadraticFunction.Point p1, QuadraticFunction.Point p2, QuadraticFunction.Point p3){

        QuadraticFunction container = new QuadraticFunction();
        float centerX = (p1.getA()+p2.getA()+p3.getA())/3;
        float centerY = (p1.getB()+p2.getB()+p3.getB())/3;
        QuadraticFunction.Point center = container.new Point();
        center.setA(centerX);
        center.setB(centerY);
        return center;
    }

    /**
     * Zwraca środek ciężkości trójkąta
     * @param list lista trzech punktów
     * @return środek ciężkości
     */
    public static Point centerOfTriangle(ArrayList<Point> list)
    {
        Point p1 = list.get(0);
        Point p2 = list.get(1);
        Point p3 = list.get(2);
        QuadraticFunction container = new QuadraticFunction();
        float centerX = (p1.getA()+p2.getA()+p3.getA())/3;
        float centerY = (p1.getB()+p2.getB()+p3.getB())/3;
        Point center = container.new Point();
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
    public static Point multiCircle(ArrayList<Circle> tab)
    {
        if(tab.size()==3)
        {
            ArrayList<Point> points = getIntersectionPointsOfThreeCircles(tab.get(0), tab.get(1), tab.get(2));
            Point finalPoint =Circle.centerOfTriangle(points.get(0), points.get(1), points.get(2));
            return finalPoint;
        }
        else
        {
            ArrayList<Point> cross = new ArrayList<>();
            for( int i = 0, j=i+1, k=j+1; i<tab.size() ; i++,j++,k++)
            {
                j = (j == tab.size()) ? 0 : j ;
                k = (k == tab.size()) ? 0 : k ;
                cross.add(centerOfTriangle(getIntersectionPointsOfThreeCircles(tab.get(i), tab.get(j), tab.get(k)))) ;
            }

            Point finalPoint = Circle.arithmeticMean(cross);
            return finalPoint;

        }
    }


    /**
     * wyliczanie średniej z list punkyów
     * @param point lista punktó
     * @return średni punkt
     */

    public static Point arithmeticMean(ArrayList<Point> point)
    {
        float sumX = 0;
        float sumY = 0;
        float i = point.size();
        QuadraticFunction container = new QuadraticFunction();
        Point result = container.new Point();

        for( Point item : point )
        {
            sumX+=item.getA();
            sumY+=item.getB();
        }
        result.setA(sumX/i);
        result.setB(sumY/i);
        return result;
    }


    /**
     * Wyliczanie średniej z dwóch punktów
     * @param first punkt 1
     * @param second punkt 2
     * @return wynik
     */
    public static Point getAveragePoint(Point first, Point second)
    {
        QuadraticFunction container = new QuadraticFunction();
        Point point = container.new Point();
        point.setA((first.getA()+second.getA())/2);
        point.setB((first.getB()+second.getB())/2);

        return point;
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
