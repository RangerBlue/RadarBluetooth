package com.example.kamil.br;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.Random;

/**
 * Created by kamil on 07.05.16.
 */
public class MapDraw extends View
{

    public MapDraw(Context context) {
        super(context);
    }

    public MapDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MapDraw(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        //-26 blisko, -100 daleko
        DBHandler db = new DBHandler(getContext());
        List<Record> elements = db.getAll();
        //robione na sucho, poprawić żeby było uniwersalnie
        long leftDown = elements.get(0).getTime();
        long leftUp = elements.get(3).getTime();
        long rigtUp = elements.get(7).getTime();

        long left = leftUp-leftDown;
        float left1 = (elements.get(1).getTime()-leftDown)/(float)left;
        Log.d("left1=", String.valueOf(left1));
        float left2 = (elements.get(2).getTime()-leftDown)/(float)left;

        long up = rigtUp-leftUp;
        float up1 = (elements.get(4).getTime()-leftUp)/(float)up;
        float up2 = (elements.get(5).getTime()-leftUp)/(float)up;
        float up3 = (elements.get(6).getTime()-leftUp)/(float)up;

        int szerEkranu = getWidth();
        int wysEkranu = getWidth();
        int wys = (int) left;
        int szer = (int) up;

        if(wys>szer)
        {
            szerEkranu = (wysEkranu * szer)/wys;
        }
        else
        {
            wysEkranu = (szerEkranu * wys)/szer;
        }

        Log.d("obliczenie", String.valueOf(left1*wysEkranu));

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(5);
        p.setColor(Color.rgb(255, 153, 51));
        Random r = new Random();

        //lewa, góra, prawa, dól liczymy od lewej i góry
        canvas.drawLine(0,wysEkranu*left1,elements.get(1).getRssi()*(-1),wysEkranu*left1, p);
        canvas.drawLine(0,wysEkranu*left2,elements.get(2).getRssi()*(-1),wysEkranu*left2, p);
        //canvas.drawLine(50,100,400,100,p);

        canvas.drawLine(szerEkranu*up1,0,szerEkranu*up1,elements.get(4).getRssi()*(-1), p);
        canvas.drawLine(szerEkranu*up2,0,szerEkranu*up2,elements.get(5).getRssi()*(-1), p);
        canvas.drawLine(szerEkranu*up3,0,szerEkranu*up3,elements.get(6).getRssi()*(-1), p);


        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        p.setColor(Color.BLUE);
        canvas.drawRect(2,2,szerEkranu-3,wysEkranu-3, p);

        super.onDraw(canvas);
    }


}
