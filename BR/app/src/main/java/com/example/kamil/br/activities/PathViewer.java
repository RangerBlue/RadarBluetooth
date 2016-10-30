package com.example.kamil.br.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.kamil.br.views.PathDrawView;
import com.example.kamil.br.R;

import com.example.kamil.br.database.controller.PathDataController;
import com.example.kamil.br.database.model.PathData;

import java.util.ArrayList;



public class PathViewer extends AppCompatActivity  {


    private String TAG="PathViewer";
    private ImageButton buttonNext;
    private ImageButton buttonStopStart;
    private ImageButton buttonSave;
    private int counter=0;
    private int counterLimit;
    private PathDrawView map;
    private long timeStart;
    private long timeStop;
    private boolean ifTheClockIsTicking = false;
    private ArrayList<PathData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //odebranie paczki
        //odebranie paczki
        int idRooms = getIntent().getIntExtra("id",-1);
        Log.d(TAG, Integer.toString(idRooms));
        setContentView(R.layout.activity_path_viewer);
        //wyzerowanie ratia
        PathData.setRatio(0);

        //tu dane są pobierane podczas tworzenia ścierzki
        if(idRooms==-1)
        {
            Log.d(TAG, "Z tworzenia ścieżki");
            ArrayList<PathData> passedData = new ArrayList<>();
            passedData = (ArrayList<PathData>) getIntent().getSerializableExtra("drawData");
            map = (PathDrawView) findViewById(R.id.viewDrawMap);
            map.setData(passedData);


        }
        else//tu z bazy danych
        {

            Log.d(TAG, "Z bazy danych");
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
                    if(ifTheClockIsTicking)
                    {
                        buttonStopStart.setImageResource(R.drawable.start_process_icon);
                        timeStop = System.currentTimeMillis();
                        getTimeDifference(timeStart, timeStop);
                        ifTheClockIsTicking = false;
                        buttonNext.setVisibility(View.VISIBLE);
                        hideButtonIfRatioIsNotSet(buttonNext);
                    }
                    else
                    {
                        buttonStopStart.setImageResource(R.drawable.stop_icon);
                        timeStart = System.currentTimeMillis();
                        ifTheClockIsTicking = true;
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
                    Log.d(TAG, "save");
                    int listLength = list.size();
                    //uaktualnienie wpółczynników funkcji
                    for (int i = 0; i < listLength - 1; i++)
                    {
                        PathData.setNewCoefficients(list.get(i), list.get(i+1));
                    }
                    //ostatni punkt z pierwszym
                    PathData.setNewCoefficients(list.get(listLength-2), list.get(0));

                    //uaktualnienie ich w bazie
                    for( PathData item : list)
                    {
                        PathDataController controller = new PathDataController();
                        controller.update(item, getApplicationContext());
                    }

                    PathDataController.printAllTableToLog(list);

                }
            });


        }

    }


    public void counterIncrement()
    {
        if(counter == counterLimit)
        {
            counter = 0;
        }
        else
            counter++;
    }

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

    public void getTimeDifference(long start, long stop)
    {
        long time = stop-start;
        Log.d(TAG, "Czas w ms: "+Long.toString((time)));

        if(counter == 0)
        {
            float segmentLength = PathData.getSegmentLenght(list.get(0).getP1(), list.get(1).getP1(), list.get(0).getP2(), list.get(1).getP2());
            float ratio = time/ segmentLength;
            PathData.setRatio(ratio);
            Log.d(TAG, "Ratio: "+ratio);
            Log.d(TAG, "Długość pierwszego(0) odcinka: "+Float.toString(segmentLength));
        }
        else
        {
            Log.d(TAG,"sprawdzenie counter i getCounter "+ counter +","+getCounterIncrement());
            Log.d(TAG, "Ratio: "+PathData.getRatio());

            PathData.calculateNewPoint(time, list.get(counter), list.get(getCounterIncrement()));

        }

    }

    public void hideButtonIfRatioIsNotSet(ImageButton button)
    {
        if(PathData.getRatio() == 0)
        {
            button.setVisibility(View.INVISIBLE);
        }
    }

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

    @Override
    protected void onStop() {
        super.onStop();
        //wyzerowanie ratia
        PathData.setRatio(0);
        Log.d(TAG, "wyzerowanie ratia");
    }


}
