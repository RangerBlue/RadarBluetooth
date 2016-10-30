package com.example.kamil.br.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kamil.br.R;
import com.example.kamil.br.database.controller.PathDataController;
import com.example.kamil.br.database.model.PathData;

import java.util.ArrayList;
import java.util.List;

/**
 * Aktywność służaca do wyboru opcji związanych z pokojami czyli
 * utworzenie, wyświetlenie, edycja, usunięcie
 * Created by Kamil
 */
public class PathMenu extends AppCompatActivity {

    private Button buttonCreate;
    private Button buttonView;
    private int idRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_editor);

        idRooms = getIntent().getIntExtra("id",-1);

        buttonCreate = (Button) findViewById(R.id.buttonCreateInPath);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });

        buttonView = (Button) findViewById(R.id.buttonViewInPath);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });

        //drukowanie
       List<PathData> all = new PathDataController().selectAll(getApplicationContext());
       PathDataController.printAllTableToLog((ArrayList<PathData>) all);
    }




    /**
     * Przejście do aktywności dodawania śćieżki
     */
    private void create()
    {

        Intent intent = new Intent(this, PathCreator.class);
        intent.putExtra("id", idRooms);
        startActivity(intent);
    }

    /**
     * Przejście do aktywności edycji ścieżki
     */
    private void edit()
    {
        Intent intent = new Intent(this, PathViewer.class);
        intent.putExtra("id", idRooms);
        intent.putExtra("option", 1);
        startActivity(intent);
    }

}
