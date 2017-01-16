package com.example.kamil.br.activities.mapping.path;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.kamil.br.activities.mapping.measurement.MeasurementCreate;
import com.example.kamil.br.database.controller.RoomsController;
import com.example.kamil.br.database.model.Rooms;
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

    /**
     * Parametr procesu
     */
    private int process ;

    /**
     * Szerokość ekranu
     */
    private int screenWidth ;

    private float ratio = 0;

    private int idRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        idRooms = getIntent().getIntExtra("id",-1);
        process = getIntent().getIntExtra("process",-1);
        Log.d(TAG, Integer.toString(idRooms));
        setContentView(R.layout.activity_path_viewer);
        PathData.setWalkRatio(0);

        screenWidth = setScreenWidth();

        Log.d(TAG, "Z bazy danych");
        //lista z danymi o krawędziach
        list = (ArrayList<PathData>) new PathDataController().selectPathDataWhereIdRoom(getApplicationContext(), idRooms);
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
                counterIncrement();
                map.setNumber(counter);
                map.invalidate();
                //Log.d(TAG, "Wybrana krawędź "+Integer.toString(counter));
                hideButtonOnMainEdgeIfRatioIsSet(buttonStopStart);
            }
        });

        buttonStopStart = (ImageButton) findViewById(R.id.buttonPathViewerStartStop);
        buttonStopStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try
                {
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
                        buttonSave.setVisibility(View.VISIBLE);
                        hideButtonIfRatioIsNotSet(buttonNext);
                    }
                }
                catch (ArithmeticException e)
                {
                    new AlertDialog.Builder(PathViewer.this)
                            .setTitle(getResources().getString(R.string.error))
                            .setMessage(getResources().getString(R.string.error_path))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });

        buttonSave = (ImageButton) findViewById(R.id.buttonPathViewerSave);
        hideButtonIfRatioIsNotSet(buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                updateData(true);
                Toast.makeText(getApplicationContext(), R.string.updated_edges, Toast.LENGTH_SHORT).show();
                if( process == 1 )
                launchMeasurementCreator();
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
        //Log.d(TAG, "Czas w ms: "+Long.toString((time)));
        ArrayList<Integer> parallel = new ArrayList<>();

        for(int i = 0 ; i<=counterLimit ; i++)
        {
            if( (list.get(i).getA() == list.get(counter).getA()) &&
                    (PathData.getSegmentLength(list.get(i).getP1(), list.get(getCounterIncrement(i)).getP1(), list.get(i).getP2(), list.get(getCounterIncrement(i)).getP2()) ==
                            PathData.getSegmentLength(list.get(counter).getP1(), list.get(getCounterIncrement()).getP1(), list.get(counter).getP2(), list.get(getCounterIncrement()).getP2())) &&
                    list.get(i).getIsIfLinear() == list.get(counter).getIsIfLinear() &&
                    i!= counter)
            {
                parallel.add(i);
            }
        }

        if(counter == 0)
        {
            float segmentLength = PathData.getSegmentLength(list.get(0).getP1(), list.get(1).getP1(), list.get(0).getP2(), list.get(1).getP2());
            float ratio = time/ segmentLength;
            PathData.setWalkRatio(ratio);
            PathData.setRatio(ratio);
            this.ratio = ratio;
            Rooms room = RoomsController.selectWhereId(getApplicationContext(), idRooms);
            room.setWalkRatio(ratio);
            RoomsController controller = new RoomsController();
            controller.update(room, getApplicationContext());
        }
        else
        {
            PathData.setNewLength(time, list.get(counter), list.get(getCounterIncrement()), false);

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
        if(ratio == 0)
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
        int listLength = list.size();
        //uaktualnienie wpółczynników funkcji
        for (int i = 0; i < listLength-1; i++)
        {
            PathData.setNewCoefficients(list.get(i), list.get(i+1));
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

    public int setScreenWidth()
    {
        //pobranie wymiaru wyświetlacza
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }


    /**
     * Przechodzi do aktywnosci tworzenia pomiaru
     */
    private void launchMeasurementCreator()
    {
        Intent intent = new Intent(this, MeasurementCreate.class);
        intent.putExtra("idRooms", idRooms);
        intent.putExtra("process", process);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
