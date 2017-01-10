package com.example.kamil.br.views;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.View;

import com.example.kamil.br.R;
import com.example.kamil.br.database.controller.PathDataController;
import com.example.kamil.br.database.model.PathData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 07.05.16.
 */
public class PathDrawView extends View
{

    private static ArrayList<PathData> data;
    private Paint p;
    private static int number;
    private static int screenWidth;
    private static int screenHeight;
    private Float xMove = 0f ;
    private Float yMove = 0f;

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(int screenHeight) {
        PathDrawView.screenHeight = screenHeight;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(int screenWidth) {
        PathDrawView.screenWidth = screenWidth;
    }

    private static String TAG = PathDrawView.class.getSimpleName();



    public void setData(ArrayList<PathData> data)
    {
        this.data = data;
    }
    public void setNumber(int number){this.number=number;}

    public PathDrawView(Context context)
    {
        super(context);
        setWillNotDraw(false);
    }

    public PathDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public PathDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        p = new Paint();
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(5);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLACK);



        //dodać obliczanie współczynnika zależnie od urządzenia, gdy jest szersze itp
        //int ratio = 44;

        Pair moves = calculateMoves(data);
        xMove = (Float) moves.first;
        yMove = (Float) moves.second;
        Log.d(TAG, "przemieszcezniea"+xMove+","+yMove);
        updateListElements(data, xMove, yMove);
        Log.d(TAG, "UPDATE");
        PathDataController.printAllTableToLog(data);
        int ratio = calculateAbsoluteRatio(screenWidth,screenHeight, data);
        Log.d(TAG,"ratio nowe: "+ratio);
        int radius = 3;
        //rysowanie punktów
        for(PathData e : data )
        {
            p.setColor(Color.RED);
            canvas.drawCircle(
                    (e.getP1()+xMove)*ratio,
                    (e.getP2Reverse()+yMove)*ratio,
                    radius,p);
        }


        //rysowanie lini, od pierwszej do ostatniej

        p.setColor(Color.BLACK);
        for(int i = 0 ; i < data.size()-1; i++)
        {
            if(data.get(i).getEdgeNumber()==number)
            {
                p.setColor(getResources().getColor(R.color.colorPrimaryDark));
                canvas.drawLine(
                        (data.get(i).getP1()+ xMove)*ratio ,
                        (data.get(i).getP2Reverse()+ yMove)*ratio ,
                        (data.get(i+1).getP1() + xMove)*ratio,
                        (data.get(i+1).getP2Reverse() + yMove)*ratio,
                        p);
            }
            else
            {
                p.setColor(Color.BLACK);
                canvas.drawLine(
                        (data.get(i).getP1()+ xMove)*ratio ,
                        (data.get(i).getP2Reverse() + yMove)*ratio,
                        (data.get(i+1).getP1()+ xMove)*ratio ,
                        (data.get(i+1).getP2Reverse() + yMove)*ratio,
                        p);
            }

        }

        //rysowanie lini ostatniej z pierwszą
        if(data.get(data.size()-1).getEdgeNumber()==number)
        {
            p.setColor(getResources().getColor(R.color.colorPrimaryDark));
            canvas.drawLine(
                    (data.get(data.size()-1).getP1() + xMove)*ratio,
                    (data.get(data.size()-1).getP2Reverse()+ yMove)*ratio,
                    (data.get(0).getP1() + xMove)*ratio,
                    (data.get(0).getP2Reverse()+ yMove)*ratio,
                    p);
        }
        else
        {
            p.setColor(Color.BLACK);
            canvas.drawLine(
                    (data.get(data.size()-1).getP1() + xMove)*ratio,
                    (data.get(data.size()-1).getP2Reverse()+ yMove)*ratio,
                    (data.get(0).getP1() + xMove)*ratio,
                    (data.get(0).getP2Reverse()+ yMove)*ratio,
                    p);
        }

        super.dispatchDraw(canvas);

    }

    /**
     * Oblicza współczynnik powiększający rysunek zależnie od rozmiaru ekranu
     * @param screenWidth szerokość ekranu
     * @param list lista z danymi o krawędziach
     * @return
     */
    private int calculateAbsoluteRatio(int screenWidth, int screenHeight, ArrayList<PathData> list)
    {
        float maxX = 0;
        float maxY = 0;
        for ( PathData item : list )
        {
            if(item.getP1() > maxX)
                maxX = item.getP1();
            if(item.getP2Reverse() > maxY)
                maxY = item.getP2Reverse();
        }
        Log.d(TAG, "screenWidth "+ screenWidth);
        Log.d(TAG, "screenHeight "+ screenHeight);
        Log.d(TAG, "maxX "+ maxX);
        Log.d(TAG, "maxY "+ maxY);

        if( maxX >= maxY)
        {
            return (int) ((screenWidth/maxX)*0.95);
        }
        else
        {
            return (int) ((screenHeight/maxY)*0.95);
        }

    }

    /**
     * Zwraca przesunięcie osi X i Y jeśli rysunek wychodzi poza ekran
     * @param list lista z danymi o krawędziach
     * @return
     */
    private Pair calculateMoves( ArrayList<PathData> list)
    {
        float minX = 0;
        float minY = 0;
        float xMove = 0;
        float yMove = 0;
        for ( PathData item : list )
        {
            if(item.getP1() < minX)
                minX = item.getP1();
            if(item.getP2Reverse() < minY)
                minY = item.getP2Reverse();
        }

        if( minX < 0 )
            xMove = (-1) * minX;
        if( minY <0 )
            yMove = (-1) * minY;

        Pair result = new Pair(xMove, yMove);

        return result;
    }

    /**
     * Uaktualnia elementy listy uwzględniacjąc przesunięcia
     * @param list lista z danymi o krawędziach
     * @param moveX przesunięcie na osi X
     * @param moveY przesunięcie na osi Y
     */
    private void updateListElements(ArrayList<PathData> list, float moveX, float moveY )
    {
        for(PathData item : list)
        {
            item.setP1(item.getP1()+moveX);
            item.setP2(item.getP2()-moveY);
        }
    }
    //gdy chcemy zmienić cos w widoku
    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        screenWidth= MeasureSpec.getSize(widthMeasureSpec);
        screenHeight = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
