package com.example.kamil.br;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.List;

public class DataBaseValues extends AppCompatActivity  {



    private DBHandler db = new DBHandler(this);
    private int mapID= R.layout.activity_data_base_v2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(mapID);
        Button przyciskDelete = (Button) findViewById(R.id.button_delete_records);



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


        //jesli usunac baze programowo
        /*for (Record item : elements)
        {
            db.delete(item);
            Toast.makeText(DataBaseValues.this, "Usunięto rekordy", Toast.LENGTH_SHORT).show();
        }*/


        przyciskDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                List<Record> elements = db.getAll();
                for (Record item : elements)
                {
                    db.delete(item);
                    Toast.makeText(DataBaseValues.this, "Usunięto rekordy", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



}
