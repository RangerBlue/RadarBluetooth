package com.example.kamil.br.views;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;

import com.example.kamil.br.R;
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
        int ratio = calculateAbsoluteRatio(screenWidth, data);
        Log.d(TAG,"ratio nowe: "+ratio);
        //moraczowe
        int radius = 3;
        //rysowanie punktów
        for(PathData e : data )
        {
            p.setColor(Color.RED);
            canvas.drawCircle(e.getP1()*ratio,e.getP2Reverse()*ratio,radius,p);
        }


        //rysowanie lini, od pierwszej do ostatniej

        p.setColor(Color.BLACK);
        for(int i = 0 ; i < data.size()-1; i++)
        {
            if(data.get(i).getEdgeNumber()==number)
            {
                p.setColor(getResources().getColor(R.color.colorPrimaryDark));
                canvas.drawLine(data.get(i).getP1()*ratio,data.get(i).getP2Reverse()*ratio,data.get(i+1).getP1()*ratio,data.get(i+1).getP2Reverse()*ratio,p);
            }
            else
            {
                p.setColor(Color.BLACK);
                canvas.drawLine(data.get(i).getP1()*ratio,data.get(i).getP2Reverse()*ratio,data.get(i+1).getP1()*ratio,data.get(i+1).getP2Reverse()*ratio,p);
            }

        }

        //rysowanie lini ostatniej z pierwszą
        if(data.get(data.size()-1).getEdgeNumber()==number)
        {
            p.setColor(getResources().getColor(R.color.colorPrimaryDark));
            canvas.drawLine(data.get(data.size()-1).getP1()*ratio,data.get(data.size()-1).getP2Reverse()*ratio,data.get(0).getP1()*ratio,data.get(0).getP2Reverse()*ratio,p);
        }
        else
        {
            p.setColor(Color.BLACK);
            canvas.drawLine(data.get(data.size()-1).getP1()*ratio,data.get(data.size()-1).getP2Reverse()*ratio,data.get(0).getP1()*ratio,data.get(0).getP2Reverse()*ratio,p);
        }

        super.dispatchDraw(canvas);

    }

    /**
     *
     * @param screenWidth
     * @param list
     * @return
     */
    private int calculateAbsoluteRatio(int screenWidth, ArrayList<PathData> list)
    {
        float max=0;
        for ( PathData item : list )
        {
            if(item.getP1() > max)
                max = item.getP1();
        }

        return (int) ((screenWidth/max)*0.95);
    }

    //gdy chcemy zmienić cos w widoku
    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
