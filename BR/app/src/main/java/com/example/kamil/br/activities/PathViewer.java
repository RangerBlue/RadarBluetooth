package com.example.kamil.br.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.kamil.br.database.controller.WalkRatioController;
import com.example.kamil.br.database.model.WalkRatio;
import com.example.kamil.br.views.PathDrawView;
import com.example.kamil.br.R;

import com.example.kamil.br.database.controller.PathDataController;
import com.example.kamil.br.database.model.PathData;

import java.util.ArrayList;

/**
 * Aktywność do pokazania oraz edytowania ścieżki, po edytowaniu ścieżki można ją nadpisać
 * Created by Kamil
 */

public class PathViewer extends AppCompatActivity  {


    private String TAG=PathViewer.class.getSimpleName();

    /**
     * Przycisk do wybrania kolejnej krawędzi
     */
    private ImageButton buttonNext;

    /**
     * Przycisk do wystartowania lub zastopowania licznika
     */
    private ImageButton buttonStopStart;

    /**
     * Przycisk do zapisania wyników edycji
     */
    private ImageButton buttonSave;

    /**
     * Licznik wskazujący na wybraną krawędź
     */
    private int counter=0;

    /**
     * Maksymalny zakres licznika
     */
    private int counterLimit;

    /**
     * Mapa do rysowania ścieżki
     */
    private PathDrawView map;

    /**
     * Czas wystartowania licznika
     */
    private long timeStart;

    /**
     * Czas zastopowania licznika
     */
    private long timeStop;

    /**
     * Zmienna logiczna wskazująca czy czas jest mierzony
     */
    private boolean isTheClockTicking = false;

    /**
     * Lista danych z krawędziami do narysowania
     */
    private ArrayList<PathData> list;

    private int idRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        idRooms = getIntent().getIntExtra("id",-1);
        Log.d(TAG, Integer.toString(idRooms));
        setContentView(R.layout.activity_path_viewer);
        PathData.setWalkRatio(0);


        Log.d(TAG, "Z bazy danych");
        //lista z danymi o krawędziach
        list = (ArrayList<PathData>) new PathDataController().selectPathDataWhereId(getApplicationContext(), idRooms);
        map = (PathDrawView) findViewById(R.id.viewDrawMap);
        map.setData(list);
        map.setNumber(counter);
        counterLimit = list.get(list.size()-1).getEdgeNumber();

        PathDataController.printAllTableToLog(list);

        buttonNext = (ImageButton) findViewById(R.id.buttonPathViewerNext);
        hideButtonIfRatioIsNotSet(buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "next");
                counterIncrement();
                map.setNumber(counter);
                map.invalidate();

                Log.d(TAG, "Wybrana krawędź "+Integer.toString(counter));
                hideButtonOnMainEdgeIfRatioIsSet(buttonStopStart);
            }
        });

        buttonStopStart = (ImageButton) findViewById(R.id.buttonPathViewerStartStop);
        buttonStopStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "stopstart");
                if(isTheClockTicking)
                {
                    buttonStopStart.setImageResource(R.drawable.start_process_icon);
                    timeStop = System.currentTimeMillis();
                    getTimeDifference(timeStart, timeStop);
                    isTheClockTicking = false;
                    buttonNext.setVisibility(View.VISIBLE);
                    hideButtonIfRatioIsNotSet(buttonNext);
                }
                else
                {
                    buttonStopStart.setImageResource(R.drawable.stop_icon);
                    timeStart = System.currentTimeMillis();
                    isTheClockTicking = true;
                    buttonNext.setVisibility(View.INVISIBLE);
                    hideButtonIfRatioIsNotSet(buttonNext);
                }
            }
        });

        buttonSave = (ImageButton) findViewById(R.id.buttonPathViewerSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                updateData(true);
            }
        });




    }

    /**
     * Inkrementuje licznik, jeśli przekroczy swój zakres, zaczyna od nowa
     */
    public void counterIncrement()
    {
        if(counter == counterLimit)
        {
            counter = 0;
        }
        else
            counter++;
    }

    /**
     * Zwraca licznik powiękoszony o jeden
     * @return zinkrementowany licznik
     */
    public int getCounterIncrement()
    {
        if(counter == counterLimit)
        {
            return 0;
        }
        else
        {
            int _counter = counter;
            return ++_counter;
        }

    }

    /**
     * Zwraca licznik powiękoszony o jeden
     * @param number podana liczba
     * @return zinkrementowany licznik
     */
    public int getCounterIncrement(int number)
    {
        if(number == counterLimit)
        {
            return 0;
        }
        else
        {
            int _counter = number;
            return ++_counter;
        }

    }

    /**
     * Oblicza czas między stop a start, jeśli jest wywołana na pierwszej krawędzi ustawia ratio,
     * jeśli na innych to ustawia ich długość
     * @param start czas rozpoczęcia odliczania
     * @param stop czas zakończenia odliczania
     */
    public void getTimeDifference(long start, long stop)
    {
        long time = stop-start;
        Log.d(TAG, "Czas w ms: "+Long.toString((time)));
        ArrayList<Integer> parallel = new ArrayList<>();

        for(int i = 0 ; i<=counterLimit ; i++)
        {
            float jeden = PathData.getSegmentLength(list.get(i).getP1(), list.get(getCounterIncrement(i)).getP1(), list.get(i).getP2(), list.get(getCounterIncrement(i)).getP2());
            float dwa =  PathData.getSegmentLength(list.get(counter).getP1(), list.get(getCounterIncrement()).getP1(), list.get(counter).getP2(), list.get(getCounterIncrement()).getP2());
            float adin = list.get(i).getA();
            float adin2 = list.get(counter).getA();
            Log.d(TAG, "lalala"+jeden+","+dwa);
            Log.d(TAG, "oololo"+adin+","+adin2);

            if( (list.get(i).getA() == list.get(counter).getA()) &&
                    (PathData.getSegmentLength(list.get(i).getP1(), list.get(getCounterIncrement(i)).getP1(), list.get(i).getP2(), list.get(getCounterIncrement(i)).getP2()) ==
                            PathData.getSegmentLength(list.get(counter).getP1(), list.get(getCounterIncrement()).getP1(), list.get(counter).getP2(), list.get(getCounterIncrement()).getP2())) &&
                    list.get(i).getIsIfLinear() == list.get(counter).getIsIfLinear() &&
                    i!= counter)
            {
                parallel.add(i);
                Log.d(TAG, "MAM!");
            }
        }


        if(counter == 0)
        {
            float segmentLength = PathData.getSegmentLength(list.get(0).getP1(), list.get(1).getP1(), list.get(0).getP2(), list.get(1).getP2());
            float ratio = time/ segmentLength;
            PathData.setWalkRatio(ratio);
            PathData.setRatio(ratio);
            WalkRatio item = new WalkRatio();
            item.setValue(ratio);
            item.setIdRooms(idRooms);
            WalkRatioController controller = new WalkRatioController();
            controller.insert(item, getApplicationContext());

            Log.d(TAG, "Ratio: "+ratio);
            Log.d(TAG, "Długość pierwszego(0) odcinka: "+Float.toString(segmentLength));
        }
        else
        {
            Log.d(TAG,"sprawdzenie counter i getCounter "+ counter +","+getCounterIncrement());
            Log.d(TAG, "Ratio: "+PathData.getRatio());

            PathData.setNewLength(time, list.get(counter), list.get(getCounterIncrement()), false);

            for( Integer item : parallel)
            {
                Log.d(TAG, "paralel" + item);
            }

    /*
            if(!parallel.isEmpty())
            {
                for( Integer item : parallel)
                {
                    if(PathData.isPointsCrossed(list.get(counter), list.get(getCounterIncrement()), list.get(item), list.get(getCounterIncrement(item))))
                        PathData.setNewLength(time, list.get(getCounterIncrement(item)), list.get(item) );
                    else
                        PathData.setNewLength(time, list.get(item), list.get(getCounterIncrement(item)));
                }
            }
*/

            if(!parallel.isEmpty())
            {
                for( Integer item : parallel)
                {
                    if(PathData.isPointsCrossed(list.get(counter), list.get(getCounterIncrement()), list.get(item), list.get(getCounterIncrement(item))))
                        PathData.setNewLength(time, list.get(item), list.get(getCounterIncrement(item)), true);
                    else
                        PathData.setNewLength(time, list.get(item), list.get(getCounterIncrement(item)), false);
                }
                updateData(false);
            }
        }

    }

    /**
     * Jeśli ratio nie jest ustawione, ukrywa przekazany przycisk
     * @param button przycisk, który chcemy ukryć
     */
    public void hideButtonIfRatioIsNotSet(ImageButton button)
    {
        if(PathData.getRatio() == 0)
        {
            button.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Ukrywa przycisk podczas wybrania pierwszej krawędzi, jeśli ratio jest już ustawione
     * @param button przycisk, który chcey ukryć
     */
    public void hideButtonOnMainEdgeIfRatioIsSet(ImageButton button)
    {
        if(PathData.getRatio()!=0 && counter == 0)
        {
            button.setVisibility(View.INVISIBLE);
        }
        else
        {
            button.setVisibility(View.VISIBLE);
        }
    }

    private void updateData(boolean ifInDatabase)
    {
        Log.d(TAG, "save");
        int listLength = list.size();
        //uaktualnienie wpółczynników funkcji
        for (int i = 0; i < listLength-1; i++)
        {
            PathData.setNewCoefficients(list.get(i), list.get(i+1));
            Log.d(TAG, "iterator"+i+", "+i+1);
        }
        //ostatni punkt z pierwszym
        PathData.setNewCoefficients(list.get(listLength-1), list.get(0));

        if( ifInDatabase )
        {
            //uaktualnienie ich w bazie
            for( PathData item : list)
            {
                PathDataController controller = new PathDataController();
                controller.update(item, getApplicationContext());
            }

            PathDataController.printAllTableToLog(list);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        //wyzerowanie ratia
        //PathData.setRatio(0);
       // Log.d(TAG, "wyzerowanie ratia");
    }


}
