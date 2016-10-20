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



    public void setData(ArrayList<BluetoothResults> data)
    {
        this.data = data;
    }

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
        int ratio = 1000;
        int radius = 3;

        int canvasWidth = this.getMeasuredWidth();
        int canvasHeight = this.getMeasuredHeight();

        //rysowanie punktu na środku, pozycja naszego urządzenia
        canvas.drawCircle(canvasWidth/2,canvasHeight/2,radius,p);



        //rysowanie punktów
        for(BluetoothResults e : data )
        {
            p.setColor(Color.RED);
            //figura pusta w środku
            p.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(canvasWidth/2,canvasHeight/2,BluetoothDistance.getDistance(e.getRssi())*ratio,p);
            Log.d(TAG,e.getName()+","+ Integer.toString(e.getRssi()));
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


}
