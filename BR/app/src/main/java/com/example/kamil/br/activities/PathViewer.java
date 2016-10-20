package com.example.kamil.br.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.kamil.br.MapDraw;
import com.example.kamil.br.R;
import com.example.kamil.br.database.DBHandler;
import com.example.kamil.br.database.controller.PathDataController;
import com.example.kamil.br.database.model.PathData;

import java.util.ArrayList;


public class PathViewer extends AppCompatActivity  {


    private String TAG="PathViewer";
    private DBHandler db = new DBHandler(this);
    private Button buttonNext;
    private Button buttonStopStart;
    private int counter=0;
    private int counterLimit;
    private MapDraw map;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //odebranie paczki
        //odebranie paczki
        int idRooms = getIntent().getIntExtra("id",-1);
        Log.d(TAG, Integer.toString(idRooms));
        setContentView(R.layout.activity_path_viewer);


        //tu dane są pobierane podczas tworzenia ścierzki
        if(idRooms==-1)
        {
            Log.d(TAG, "Z tworzenia ścieżki");
            ArrayList<PathData> passedData = (ArrayList<PathData>) getIntent().getSerializableExtra("drawData");

            map.setData(passedData);


        }
        else//tu z bazy danych
        {
            Log.d(TAG, "Z bazy danych");
            ArrayList<PathData> list = (ArrayList<PathData>) new PathDataController().selectPathDataWhereId(getApplicationContext(), idRooms);
            map = (MapDraw) findViewById(R.id.viewDrawMap);
            map.setData(list);
            map.setNumber(counter);
            counterLimit = list.get(list.size()-1).getEdgeNumber();

            buttonNext = (Button) findViewById(R.id.buttonPathViewerNext);
            buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    counterIncrement();
                    map.setNumber(counter);
                    map.invalidate();

                    Log.d(TAG, Integer.toString(counter));
                }
            });
        }






        /*
        List<Record> elements = db.getAll();

        for (Record item : elements) {
            String log = "Id: " + item.getId()
                    + " ,Name: " + item.getName()
                    + " ,RSSI: " + item.getRssi()
                    + " ,Time: " + item.getTime()
                    + " ,Direction: " + item.getDirection();
            // Writing shops  to log
            Log.d("Item: : ", log);
        }
        */

        //jesli usunac baze programowo
        /*for (Record item : elements)
        {
            db.delete(item);
            Toast.makeText(PathViewer.this, "Usunięto rekordy", Toast.LENGTH_SHORT).show();
        }*/





/*
        przyciskDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                List<Record> elements = db.getAll();
                for (Record item : elements)
                {
                    db.delete(item);
                    Toast.makeText(PathViewer.this, "Usunięto rekordy", Toast.LENGTH_SHORT).show();
                }
            }
        });

*/
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



}
