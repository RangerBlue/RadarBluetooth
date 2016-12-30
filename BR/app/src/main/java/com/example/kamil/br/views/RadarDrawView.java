package com.example.kamil.br.views;

import android.content.Context;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.kamil.br.BluetoothDistance;
import com.example.kamil.br.R;
import com.example.kamil.br.database.model.BluetoothResults;
import com.example.kamil.br.database.model.PathData;

import java.util.ArrayList;
import java.util.List;

/**
 * Widok w którym pokazywane jest odległość znalezionych urządzeń przez
 * nasze urzadzenie w formie graficznej
 */
public class RadarDrawView extends View {
    private static ArrayList<BluetoothResults> data;
    private Paint p;
    private static String TAG = RadarDrawView.class.getSimpleName();
    private static ArrayList<Integer> colorList;
    int canvasWidth;
    int canvasHeight;

    int ratio = 1000;
    int radius = 3;



    public void setData(ArrayList<BluetoothResults> data)
    {
        this.data = data;
    }
    public void setColorList(ArrayList<Integer> list) {this.colorList = list;}

    public RadarDrawView(Context context)
    {
        super(context);
        setWillNotDraw(false);
    }

    public RadarDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public RadarDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }




    @Override
    protected void dispatchDraw(Canvas canvas) {

        p = new Paint();
        p.setAntiAlias(true);
        //figura wypełniona
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLUE);

        //dodać obliczanie współczynnika zależnie od urządzenia, gdy jest szersze itp


        canvasWidth = this.getMeasuredWidth();
        canvasHeight = this.getMeasuredHeight();


        drawAxis(canvas, p, canvasWidth, canvasHeight);

        int i = 0;
        //rysowanie punktów
        for(BluetoothResults e : data )
        {
            p.setStrokeWidth(2);
            p.setColor(colorList.get(i));
            //figura pusta w środku
            p.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(canvasWidth/2,canvasWidth/2,MetersToPixels(BluetoothDistance.getDistance(e.getRssi(), -1)),p);
            Log.d(TAG,e.getName()+","+ Integer.toString(e.getRssi()));
            i++;
         }

        super.dispatchDraw(canvas);

    }



    //gdy chcemy zmienić cos w widoku
    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentWidth, parentHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Rysowanie osi układu
     * @param canvas kanwa, na której rysujemy
     */
    private void drawAxis(Canvas canvas, Paint p, int canvasWidth, int canvasHeight )
    {
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(3);
        p.setStyle(Paint.Style.STROKE);
        /**
         * Zapewniamy, że widok jest kwadratowy
         */
        canvasWidth-=3;
        canvasHeight = canvasWidth;
        int start = 2;

        /**
         * Oś x dodatnia
         */
        canvas.drawLine(canvasWidth/2,canvasHeight/2,canvasWidth,canvasHeight/2, p );
        canvas.drawLine(canvasWidth*3/4,(canvasWidth/2)+canvasWidth/20,canvasWidth*3/4,(canvasWidth/2)-canvasWidth/20,p );
        canvas.drawLine(canvasWidth,(canvasWidth/2)+canvasWidth/20,canvasWidth,(canvasWidth/2)-canvasWidth/20,p );

        /**
         * Oś x ujemna
         */
        canvas.drawLine(start,canvasHeight/2,canvasWidth/2,canvasHeight/2,p );
        canvas.drawLine(start,(canvasWidth/2)+canvasWidth/20,start,(canvasWidth/2)-canvasWidth/20,p );
        canvas.drawLine(canvasWidth/4,(canvasWidth/2)+canvasWidth/20,canvasWidth/4,(canvasWidth/2)-canvasWidth/20,p );

        /**
         * Oś y dodatnia
         */
        canvas.drawLine(canvasWidth/2,start,canvasWidth/2,canvasHeight/2,p );
        canvas.drawLine((canvasWidth/2)-canvasWidth/20,canvasHeight/4,(canvasWidth/2)+canvasWidth/20,canvasHeight/4,p );
        canvas.drawLine((canvasWidth/2)-canvasWidth/20,start,(canvasWidth/2)+canvasWidth/20,start,p );

        /**
         * Oś y ujemna
         */
        canvas.drawLine(canvasWidth/2,start,canvasWidth/2,canvasHeight,p );
        canvas.drawLine((canvasWidth/2)-canvasWidth/20,canvasHeight*3/4,(canvasWidth/2)+canvasWidth/20,canvasHeight*3/4,p );
        canvas.drawLine((canvasWidth/2)-canvasWidth/20,canvasHeight,(canvasWidth/2)+canvasWidth/20,canvasHeight,p );

    }

    private float MetersToPixels(float meters)
    {
        return ( canvasWidth/2 * meters )/10;
    }


}
