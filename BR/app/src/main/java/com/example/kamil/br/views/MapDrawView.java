package com.example.kamil.br.views;

/**
 * Created by Kamil on 2016-10-13.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.kamil.br.BluetoothDistance;
import com.example.kamil.br.database.controller.BluetoothResultsController;
import com.example.kamil.br.database.controller.PathDataController;
import com.example.kamil.br.database.model.BluetoothResults;
import com.example.kamil.br.database.model.PathData;

import java.util.ArrayList;
import java.util.Random;

public class MapDrawView extends View {
    private static ArrayList<BluetoothResults> results;
    private static ArrayList<PathData>  path;
    private static ArrayList<String> distinctDevices;
    private int edgeNumbers;
    private Paint p;
    private int ratio;
    private int radius;
    private static String TAG = MapDrawView.class.getSimpleName();

    //ratio ustawione na pałe
    private float walkRatio = 0.1f;



    public MapDrawView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public MapDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public MapDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    public static void setResults(ArrayList<BluetoothResults> results) {
        MapDrawView.results = results;
    }

    public static void setPath(ArrayList<PathData> path) {
        MapDrawView.path = path;
    }

    public static void setDistinctDevices(ArrayList<String> distinctDevices) {
        MapDrawView.distinctDevices = distinctDevices;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        Log.d(TAG, "drukowanie tabel");
        PathDataController.printAllTableToLog(path);
        BluetoothResultsController.printAllTableToLog(results);

        edgeNumbers = results.get(results.size()-1).getEdgeNumber();
        p = new Paint();
        p.setAntiAlias(true);
        //figura wypełniona
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLUE);

        //dodać obliczanie współczynnika zależnie od urządzenia, gdy jest szersze itp
        ratio = 44;
        radius = 1;

        //rysowanie krawędzi
        p.setColor(Color.BLUE);
        for(int i = 0 ; i < path.size()-1; i++)
        {
               p.setColor(Color.BLUE);
            canvas.drawLine(path.get(i).getP1()*ratio,path.get(i).getP2Reverse()*ratio,path.get(i+1).getP1()*ratio,path.get(i+1).getP2Reverse()*ratio,p);
        }

        //rysowanie lini ostatniej z pierwszą
        canvas.drawLine(path.get(path.size()-1).getP1()*ratio,path.get(path.size()-1).getP2Reverse()*ratio,path.get(0).getP1()*ratio,path.get(0).getP2Reverse()*ratio,p);


        //rysowanie punktów
        drawPoints(canvas, path, results);

        super.dispatchDraw(canvas);

    }


    /**
     * Rysuje punkty na krawędziach w miejscach gdzie nastąpił pomiar
     * @param canvas używana kanwa
     * @param path lista z rekordami PathData
     * @param results lista z rekordami BluetoothResults
     */
    private void drawPoints(Canvas canvas, ArrayList<PathData> path, ArrayList<BluetoothResults> results)
    {
        ArrayList<BluetoothResults> sublist ;
        Random random = new Random();

       // p.setColor(Color.rgb(random.nextInt((255 - 1) + 1) + 1, random.nextInt((255 - 1) + 1) + 1, random.nextInt((255 - 1) + 1) + 1));

        p.setColor(Color.RED);

        int durationTime;
        int elapsedTime ;
        float edgeLength ;

        //punkty bez ostaniego i pierwszego
            sublist = (ArrayList<BluetoothResults>) BluetoothResults.getSublistWhereEdgeNumbers(edgeNumbers, results);
            Log.d(TAG, "Drukowanie sublisty");
            BluetoothResultsController.printAllTableToLog(sublist);
            durationTime = getEdgeDurationTime(sublist);
            Log.d(TAG, "durationTime "+durationTime );
            edgeLength = PathData.getSegmentLenght(
                    path.get(edgeNumbers).getP1(),
                    path.get(0).getP1(),
                    path.get(edgeNumbers).getP2(),
                    path.get(0).getP2()
            );
            Log.d(TAG, "edgeLenght"+edgeLength);
            PathData.setRatio(durationTime/edgeLength);
            Log.d(TAG, "sprawdzenie ratio"+PathData.getRatio());
            elapsedTime = 0;
            //pętla po oddzielonej liście
            for(int j=0 ; j<sublist.size(); j++)
            {
                if(sublist.get(j).getAddress().equals("NULL"))
                {
                    elapsedTime+=sublist.get(j).getTime();
                    Log.d(TAG, "elapsedTime: "+elapsedTime);
                }
                else
                {
                    PathData clone = new PathData();
                    clone.setP1(path.get(0).getP1());
                    clone.setP2(path.get(0).getP2());
                    clone.setIfLinear(path.get(0).getIsIfLinear());
                    PathData.calculateNewPoint(elapsedTime,path.get(edgeNumbers), clone );
                    drawMeasure(canvas, clone, sublist.get(j).getRssi());
                }
            }
            //ostatni z pierwszym
            for(int i=0 ; i<edgeNumbers; i++)
            {
                sublist = (ArrayList<BluetoothResults>) BluetoothResults.getSublistWhereEdgeNumbers(i, results);
                Log.d(TAG, "Drukowanie sublisty");
                BluetoothResultsController.printAllTableToLog(sublist);
                durationTime = getEdgeDurationTime(sublist);
                Log.d(TAG, "durationTime "+durationTime );
                edgeLength = PathData.getSegmentLenght(
                        path.get(i).getP1(),
                        path.get(i+1).getP1(),
                        path.get(i).getP2(),
                        path.get(i+1).getP2()
                );
                Log.d(TAG, "edgeLenght"+edgeLength);
                PathData.setRatio(durationTime/edgeLength);
                Log.d(TAG, "sprawdzenie ratio"+PathData.getRatio());
                elapsedTime = 0;
                //pętla po oddzielonej liście
                for(int j=0 ; j<sublist.size(); j++)
                {
                    if(sublist.get(j).getAddress().equals("NULL"))
                    {
                        elapsedTime+=sublist.get(j).getTime();
                        Log.d(TAG, "elapsedTime: "+elapsedTime);
                    }
                    else
                    {
                        PathData clone = new PathData();
                        clone.setP1(path.get(i+1).getP1());
                        clone.setP2(path.get(i+1).getP2());
                        clone.setIfLinear(path.get(i+1).getIsIfLinear());
                        PathData.calculateNewPoint(elapsedTime,path.get(i), clone );
                        drawMeasure(canvas, clone, sublist.get(j).getRssi());
                    }
                }

            }



    }

    private void drawMeasure(Canvas canvas, PathData record, int value)
    {
        canvas.drawCircle(record.getP1()*ratio,record.getP2Reverse()*ratio,radius,p);
        float length = getConvertedValue(BluetoothDistance.getDistance(value));
        canvas.drawCircle(record.getP1()*ratio,record.getP2Reverse()*ratio,length*ratio,p);

    }

    private float getConvertedValue(float result)
    {
        return result/walkRatio;
    }

    /**
     * Oblicza jak długo trwało przejście danej krawędzi pomieszczenia
     * podczas pomiaru
     * @param list lista zawierająca dane jednej krawędzi
     * @return czas trwania
     */
    public int getEdgeDurationTime(ArrayList<BluetoothResults> list)
    {
        int durationTime = 0;
        for(BluetoothResults item : list)
        {
            if(item.getAddress().equals("NULL"))
            {
                durationTime +=item.getTime();
            }
        }
    Log.d(TAG, "czas trwania krawędzi:"+durationTime);
        return durationTime;
    }



    //gdy chcemy zmienić cos w widoku
    @Override
    public void invalidate() {
        super.invalidate();
    }


}