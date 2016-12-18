package com.example.kamil.br.views;

/**
 * Created by Kamil on 2016-10-13.
 * Rysuje kształt pomieszczenia oraz zaznacza na nim miejsca, w których nastąpił pomiar a nastepnie rysuje w jakim
 * zasięgu od tego miejsca znajduje się urządzenie
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
import com.example.kamil.br.database.controller.WalkRatioController;
import com.example.kamil.br.database.model.BluetoothResults;
import com.example.kamil.br.database.model.PathData;

import java.util.ArrayList;

public class MapDrawView extends View {
    private static ArrayList<BluetoothResults> results;
    private static ArrayList<PathData>  path;
    private static ArrayList<String> distinctDevices;
    private static ArrayList<BluetoothResults> sublist ;
    private int edgeNumbers;
    private Paint p;
    private int ratio;
    private int radius;
    private static String TAG = MapDrawView.class.getSimpleName();

    //ratio ustawione na pałe
    private float walkRatio = 0.1f;
    /**
     * prędkość chodu użytkownika w m/s
     */
    private float walkVelocity = 1f;
    private int elapsedTime = 0;



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
        drawPoints(canvas);

        super.dispatchDraw(canvas);

    }


    /**
     * Rysuje punkty na krawędziach w miejscach gdzie nastąpił pomiar
     * @param canvas używana kanwa
     *
     */
    private void drawPoints(Canvas canvas)
    {
        /**
         * Utowrzenie sublisty
         */


        p.setColor(Color.RED);

        int durationTime;
        //int elapsedTime = 0 ;
        float edgeLength ;

        /**
         * Obsłużenie ostaniej krawędzi
         */

        //ostatni z pierwszym
        sublist =  BluetoothResults.getSublistWhereEdgeNumbers(edgeNumbers, results);
        Log.d(TAG, "Drukowanie sublisty");
        BluetoothResultsController.printAllTableToLog(sublist);
        durationTime = getEdgeDurationTime(sublist);
        Log.d(TAG, "durationTime "+durationTime );
        edgeLength = PathData.getSegmentLength(
                    path.get(edgeNumbers).getP1(),
                    path.get(0).getP1(),
                    path.get(edgeNumbers).getP2(),
                    path.get(0).getP2()
                    );
        Log.d(TAG, "edge"+edgeLength);
        PathData.setRatio(durationTime/edgeLength);
        elapsedTime = 0;

        for(int j=0 ; j<sublist.size(); j++)
        {
            Log.d(TAG, "cyk");
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
                Log.d(TAG, "elapsed"+elapsedTime);
                PathData.setNewLength(elapsedTime,path.get(edgeNumbers), clone , false);
                drawMeasure(canvas, clone, sublist.get(j).getRssi());
            }
        }


        //tu przerwa

        /**
         * Obsłużenie reszty krawędzi
         */
        //ostatni z pierwszym
        for(int i=0 ; i<edgeNumbers; i++)
        {
            sublist = BluetoothResults.getSublistWhereEdgeNumbers(i, results);
            Log.d(TAG, "sublista w pętli" );
            BluetoothResultsController.printAllTableToLog(sublist);
            durationTime = getEdgeDurationTime(sublist);
            Log.d(TAG, "durationTime "+durationTime );
            edgeLength = PathData.getSegmentLength(
                    path.get(i).getP1(),
                    path.get(i+1).getP1(),
                    path.get(i).getP2(),
                    path.get(i+1).getP2()
            );
            //Log.d(TAG, "edgeLenght"+edgeLength);
            PathData.setRatio(durationTime/edgeLength);
            //Log.d(TAG, "sprawdzenie ratio"+PathData.getRatio());
            elapsedTime = 0;
            //pętla po oddzielonej liście
            for(int j=0 ; j<sublist.size(); j++)
            {
                if(sublist.get(j).getAddress().equals("NULL"))
                {
                    elapsedTime+=sublist.get(j).getTime();
                    // Log.d(TAG, "elapsedTime: "+elapsedTime);
                }
                else
                {
                    PathData clone = new PathData();
                    clone.setP1(path.get(i+1).getP1());
                    clone.setP2(path.get(i+1).getP2());
                    clone.setIfLinear(path.get(i+1).getIsIfLinear());
                    PathData.setNewLength(elapsedTime,path.get(i), clone, false);
                    drawMeasure(canvas, clone, sublist.get(j).getRssi());
                }
            }
            elapsedTime = 0;
        }

    }


    /**
     * Rysuje okręgi o środkach w miejscach gdzie nastąpił pomiar,
     * ich promień pokazuje w jakim zasięgu jest urządzenie
     * @param canvas kanwa, na której rysujemy
     * @param record objekt, w którym jest zawarty środek okręgu
     * @param value odległość od urządzenia wyrażona w pixelach
     */
    private void drawMeasure(Canvas canvas, PathData record, int value)
    {
        canvas.drawCircle(record.getP1()*ratio,record.getP2Reverse()*ratio,radius,p);
        float length = getConvertedValue(BluetoothDistance.getDistance(value));
        canvas.drawCircle(record.getP1()*ratio,record.getP2Reverse()*ratio,length*ratio,p);

    }

    /**
     * Zamienia odległość od szukanego urządzenia wyrażoną w metrach na
     * odległość w pixelach, uwzględniając ratio
     * @param result
     * @return
     */
    private float getConvertedValue(float result)
    {
        float walkRatio = WalkRatioController.selectWalkRatioWherePathDataId(getContext(), path.get(0).getIdRooms()).getValue();
        float returnValue = ((result/walkVelocity)*1000)/walkRatio;
        Log.d(TAG, "result "+String.valueOf(result));
        Log.d(TAG, "walkWelocity "+String.valueOf(walkVelocity));
        return returnValue;
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