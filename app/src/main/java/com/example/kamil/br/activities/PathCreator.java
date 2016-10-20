package com.example.kamil.br.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.example.kamil.br.R;
import com.example.kamil.br.database.controller.PathDataController;
import com.example.kamil.br.database.model.PathData;

import java.util.ArrayList;

public class PathCreator extends AppCompatActivity {

    private int numberOfVertex = 0;
    private Button[] cells;
    private ArrayList<Integer> position;//lista z pozycja( np. 43)
    private ArrayList<PathData> pathData;//lista z współczynnikami a,b, opcjonalnym x i logicznym czy funkcja jest liniowa
    private int gridsCells = 100; //ilosc pól w planszy do zaznaczania kształtu
    private int idRooms ;
    //korzystamy z 4 ćwiartki osi
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_creator);
        PathData.setGridCells(gridsCells);
        //odebranie paczki
        idRooms = getIntent().getIntExtra("id",-1);
        PathData.setRoomNumber(idRooms);

        position = new ArrayList<>();
        pathData = new ArrayList<>();
        drawGrid();



        Button przycisk = (Button) findViewById(R.id.button);
        przycisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PathData.calculateFunctions(position, pathData, getApplicationContext() );
                Intent intent = new Intent(PathCreator.this, PathViewer.class);
                intent.putExtra("drawData", pathData);
                startActivity(intent);
            }
        });


    }

    //rysowanie pól na których zaznaczamy kształt
    public void drawGrid() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        float fieldWidthRatio = 0.9f;
        float fieldHeightRatio = 0.5f;
        GridLayout grid = (GridLayout) findViewById(R.id.grid);
        grid.setColumnCount((int) Math.sqrt(gridsCells));
        grid.setRowCount((int) Math.sqrt(gridsCells));
        Button[] cells = new Button[gridsCells];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = new Button(this);

            grid.addView(cells[i], (int) ((fieldWidthRatio * width) / grid.getColumnCount()), (int) ((fieldHeightRatio * height)) / grid.getColumnCount());
            final int i_ = i;
            cells[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setEnabled(false);
                    v.setBackgroundColor(Color.GREEN);
                    Log.d("Numer", String.valueOf(i_));
                    position.add(i_);
                    numberOfVertex++;
                }
            });
        }
    }




}
