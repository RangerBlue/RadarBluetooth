package com.example.kamil.br.views;

/**
 * Created by Kamil on 2016-10-13.
 * Rysuje kształt pomieszczenia oraz zaznacza na nim miejsca, w których nastąpił pomiar a nastepnie rysuje w jakim
 * zasięgu od tego miejsca znajduje się urządzenie
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.kamil.br.math.BluetoothDistance;
import com.example.kamil.br.math.Circle;
import com.example.kamil.br.math.QuadraticFunction;
import com.example.kamil.br.database.controller.BluetoothResultsController;
import com.example.kamil.br.database.controller.PathDataController;
import com.example.kamil.br.database.controller.RoomsController;
import com.example.kamil.br.database.controller.WalkRatioController;
import com.example.kamil.br.database.model.BluetoothResults;
import com.example.kamil.br.database.model.PathData;

import java.util.ArrayList;
import java.util.Random;

public class MapDrawView extends View {
    /**
     * Lista z danymi urządzeń
     */
    private static ArrayList<BluetoothResults> results;

    /**
     * Lista z krawędziami pokoju
     */
    private static ArrayList<PathData>  path;

    /**
     * Lista z nazwą i adresem urządzeń, które zostały znalezione
     */
    private static ArrayList<String> distinctDevices;

    /**
     * Lista z danymi urządzeń odnoszącymi się do jednej krawędzi
     */
    private static ArrayList<Integer> colors ;

    /**
     * Lista kolorów
     */
    private static ArrayList<BluetoothResults> sublist ;

    /**
     * Numer krawędzi
     */
    private int edgeNumbers;

    /**
     * Styl rysowania
     */
    private Paint p;

    /**
     * Mnożnik pomniejszający lub powiększający mapę w zależności od ekranu urządzenia
     */
    private int ratio;

    /**
     * Promień rysowania punktu w miejscu, w którym nastąpił pomiar
     */
    private int radius;

    /**
     * Tag klasy
     */
    private static String TAG = MapDrawView.class.getSimpleName();

    /**
     * Prędkość chodu użytkownika w m/s
     */
    //TODO połączyć z aktywnością z radaren
    private float walkVelocity = 1f;

    /**
     * Czas, który upłynął do znalezienia urządzenia
     */
    private int elapsedTime = 0;

    /**
     * Lista z danymi o okręgach
     */
    private ArrayList<Circle> circles;

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

    public static void setColors(ArrayList<Integer> colors) {
        MapDrawView.colors = colors;
    }

    /**
     * Wywołanie tej metody zamiast onDraw, używana jest przy własnych widokach
     * @param canvas użyta kanwa
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {

        Log.d(TAG, "drukowanie tabel");
        PathDataController.printAllTableToLog(path);
        BluetoothResultsController.printAllTableToLog(results);

        circles = new ArrayList<>();

        for(String item : distinctDevices)
            Log.d(TAG+"tablca ",item );

        edgeNumbers = results.get(results.size()-1).getEdgeNumber();
        p = new Paint();
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(5);
        p.setStyle(Paint.Style.STROKE);


        //TODO dodać obliczanie współczynnika zależnie od urządzenia, gdy jest szersze itp
        ratio = 44;
        radius = 1;

        //rysowanie krawędzi
        p.setColor(Color.BLUE);
        for(int i = 0 ; i < path.size()-1; i++)
        {
            p.setColor(Color.BLACK);
            canvas.drawLine(path.get(i).getP1()*ratio,path.get(i).getP2Reverse()*ratio,path.get(i+1).getP1()*ratio,path.get(i+1).getP2Reverse()*ratio,p);
        }

        //rysowanie lini ostatniej z pierwszą
        canvas.drawLine(path.get(path.size()-1).getP1()*ratio,path.get(path.size()-1).getP2Reverse()*ratio,path.get(0).getP1()*ratio,path.get(0).getP2Reverse()*ratio,p);


        //rysowanie punktów
        drawPoints(canvas);



        super.dispatchDraw(canvas);

    }


    /**
     * Rysuje punkty na krawędziach w miejscach gdzie nastąpił pomiar oraz okręgi
     * @param canvas używana kanwa
     *
     */
    private void drawPoints(Canvas canvas)
    {
        //p.setColor(Color.RED);
        int durationTime;
        float edgeLength ;

        /**
         * Obsłużenie ostaniej krawędzi
         */
        Random random = new Random();

        int l=0;
        for( String item : distinctDevices)
        {

            p.setColor(colors.get(l));
            l++;
            //ostatni z pierwszym
            sublist =  BluetoothResults.getSublistWhereEdgeNumbers(edgeNumbers, results);
            sublist = getDataOfOneDevice(sublist, item);
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
                sublist = getDataOfOneDevice(sublist, item);
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

            Circle.printAllTableToLog(circles);
            try
            {
                drawResult(canvas, circles);
            }
            catch (StackOverflowError e)
            {
                Log.d(TAG, "złapałem błąd");
                new AlertDialog.Builder(getContext())
                        .setTitle("Błąd")
                        .setMessage("To sie dzieje kiedy moracz pisze kod")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Activity activity = (Activity) getContext();
                                activity.finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            circles.clear();
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
        p.setStrokeWidth(1);
        canvas.drawCircle(record.getP1()*ratio,record.getP2Reverse()*ratio,radius,p);
        float length = getConvertedValue(BluetoothDistance.getDistance(value, RoomsController.selectTypeWhereId(getContext(), results.get(0).getIdRooms())));
        canvas.drawCircle(record.getP1()*ratio,record.getP2Reverse()*ratio,length*ratio,p);
        Circle circle = new Circle(record.getP1(), record.getP2(), length);
        circles.add(circle);
    }

    /**
     * Zamienia odległość od szukanego urządzenia wyrażoną w metrach na
     * odległość w pixelach, uwzględniając ratio
     * @param result
     * @return
     */
    private float getConvertedValue(float result)
    {
        float walkRatio = WalkRatioController.selectWalkRatioWhereRoomId(getContext(), path.get(0).getIdRooms()).getValue();
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

    /**
     * Rysuje punkt w miejscu, w którym znajduję się urządzenie
     * @param canvas kanwa
     * @param list lista z okręgami
     */
    public void drawResult(Canvas canvas, ArrayList<Circle> list) throws StackOverflowError
    {
        QuadraticFunction.Point point = Circle.multiCircle(list);
        Log.d(TAG, "wynik ("+point.getA()+","+point.getB()+")");
        canvas.drawCircle(point.getA()*ratio,(-1)*point.getB()*ratio,radius,p);
    }

    private ArrayList<BluetoothResults> getDataOfOneDevice(ArrayList<BluetoothResults> list, String name)
    {
        ArrayList<BluetoothResults> returnList = new ArrayList<>();

        for (BluetoothResults item :  list)
        {
            if( item.getName().equals(name) || (item.getName().equals("NULL") && item.getRssi() == 0 ) )
                returnList.add(item);
        }

        return returnList;
    }

    /**
     * Aktualizuje dane widoku
     */
    @Override
    public void invalidate() {
        super.invalidate();
    }


}