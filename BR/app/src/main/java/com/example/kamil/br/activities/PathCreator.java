package com.example.kamil.br.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;

import com.example.kamil.br.R;
import com.example.kamil.br.database.model.PathData;

import java.util.ArrayList;

/**
 * Aktywność służaca do utowrzenia ścieżki, towrzy się ją
 * zaznaczając kolejne narożniki pomieszczenia
 * Created by Kamil
 */
public class PathCreator extends AppCompatActivity {
    /**
     * Przycisk przejścia do kolejnego okna
     */
    private ImageButton buttonNext;

    /**
     * Ilość narożników pomieszczenia
     */
    private int numberOfVertex = 0;

    /**
     * Tablica pól, na których zaznaczamy kształt, mają numery od 0 do gridCells
     */
    private Button[] cells;

    /**
     * Lista z pozycjami zaznaczonych narożników, np 43
     */
    private ArrayList<Integer> position;

    /**
     * Lista ze obiektami PathData, opisująca krawędzie
     */
    private ArrayList<PathData> pathData;

    /**
     * Ilośc pól na planszy do zaznaczania kształtu, liczba z kórej można zpierwiastkowac
     */
    private int gridsCells = 100; //ilosc pól w planszy do zaznaczania kształtu

    /**
     * Numer pokoju, do ktorego chcemy dodać ścieżkę
     */
    private int idRooms ;

    /**
     * Szerokość ekranu horyzontalna lub wertykalna
     */
    private int screenWidth;

    private String TAG = PathCreator.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_creator);
        //ustawienie ilości pól
        PathData.setGridCells(gridsCells);
        //odebranie paczki
        idRooms = getIntent().getIntExtra("id",-1);
        //ustawienie id pokoju
        PathData.setRoomNumber(idRooms);
        position = new ArrayList<>();
        pathData = new ArrayList<>();
        setScreenWidth();
        drawGrid();

        buttonNext = (ImageButton) findViewById(R.id.buttonPathCreaterNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PathData.calculateFunctions(position, pathData, getApplicationContext() );
                Intent intent = new Intent(PathCreator.this, PathViewer.class);
                intent.putExtra("drawData", pathData);
                startActivity(intent);
            }
        });


    }

    /**
     * Rysowanie pól, na których zaznaczamy kształ
     */
    public void drawGrid()
    {

        GridLayout grid = (GridLayout) findViewById(R.id.grid);
        int columnAndRowCount = (int) Math.sqrt(gridsCells);
        grid.setColumnCount(columnAndRowCount);
        grid.setRowCount(columnAndRowCount);
        cells = new Button[gridsCells];
        for (int i = 0; i < cells.length; i++)
        {
            cells[i] = new Button(this);
            cells[i].setBackground(getResources().getDrawable(R.drawable.curved_button));
            cells[i].setBackground(null);
            //dodawanie pola
            grid.addView(cells[i], screenWidth / grid.getColumnCount(), screenWidth / grid.getColumnCount());
            final int i_ = i;
            //czynności wykonane po kliknięciu pola
            cells[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setEnabled(false);
                    v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    Log.d(TAG, "Numer pola: "+String.valueOf(i_));
                    position.add(i_);
                    numberOfVertex++;
                }
            });
        }
    }

    /**
     * Konwetowanie wartości dip na pixele
     * @param dip wartość w dip
     * @return wartość w pixelach
     */
    public float dipToPixels(int dip)
    {
        Resources res = getResources();
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, res.getDisplayMetrics());
        return pixels;
    }

    /**
     * Ustawienie szerokości pola wyboru narożników zależności od orientacji ekranu
     */
    public void setScreenWidth()
    {
        //pobranie wymiaru wyświetlacza
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Configuration newConfig = getResources().getConfiguration();

        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            //odjęcie od szerokości paddingu layoutu i paddingu theme
            screenWidth = (int) (size.x-dipToPixels(20)-dipToPixels(32));
        }
        else if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {

            float ratio = 0.6f;
            screenWidth = (int) (size.y * ratio);
        }
    }
}
