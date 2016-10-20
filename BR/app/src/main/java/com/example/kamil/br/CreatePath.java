package com.example.kamil.br;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

public class CreatePath extends AppCompatActivity {

    private int numberOfVertex=0;
    private Button[] cells;
    private ArrayList<Integer> position;//lista z pozycja( np. 43)
    private ArrayList<LinearFunction> linearFunction ;//lista z współczynnikami a,b, opcjonalnym x i logicznym czy funkcja jest liniowa
    private int gridsCells = 100; //ilosc pól w planszy do zaznaczania kształtu
    //korzystamy z 4 ćwiartki osi
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_path);
        position = new ArrayList<Integer>();
        linearFunction = new ArrayList<LinearFunction>();
        drawGrid();

        Button przycisk = (Button) findViewById(R.id.button);
        przycisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateFunctions(position, linearFunction);
            }
        });


    }

    //rysowanie pól na których zaznaczamy kształt
    public void drawGrid()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        float fieldWidthRatio = 0.9f;
        float fieldHeightRatio = 0.5f;
        GridLayout grid = (GridLayout) findViewById(R.id.grid);
        grid.setColumnCount((int)Math.sqrt(gridsCells));
        grid.setRowCount((int)Math.sqrt(gridsCells));
        Button[] cells = new Button[gridsCells];
        for (int i = 0; i < cells.length; i++)
        {
            cells[i] = new Button(this);

            grid.addView(cells[i], (int) ((fieldWidthRatio*width)/grid.getColumnCount()), (int) ((fieldHeightRatio*height))/grid.getColumnCount());
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

    //obliczanie funkcji liniowych
    public void calculateFunctions(ArrayList<Integer> source, ArrayList<LinearFunction> target)
    {
        int listLenght = source.size();
        //zamiana numeru i kolejnosci dwóch pól(punktów) na funkcje liniową przechodzącą przez 2 punkty
        for (int i = 0 ; i<listLenght-1 ; i++)
        {
            target.add(positionAndOrderToCoefficients(source.get(i), source.get(i+1)));
        }
        //ostatni punkt z pierwszym
        target.add(positionAndOrderToCoefficients(source.get(listLenght-1), source.get(0)));

        //wypisanie w logu calej listy funkcji
        for(LinearFunction item : linearFunction)
        {
            Log.d("Item: : ","a:"+Float.toString(item.getA())+"|b:"+Float.toString(item.getB())+"|x:"+Float.toString(item.getX())+"|linear:"+Boolean.toString(item.getIsIfLinear()));
        }
    }

    //zamienia pozycje dwóch pól na współczynniki funkcji liniowej
    public LinearFunction positionAndOrderToCoefficients(int position1, int position2)
    {
        Log.d("Positions", Integer.toString(position1)+", "+Integer.toString(position2));
        int xposition1 = getX(position1);
        int yposition1 = getY(xposition1, position1);
        int xposition2 = getX(position2);
        int yposition2 = getY(xposition2, position2);

        //sprawdzanie czy funkcja jest "pionowa"
        if(xposition1==xposition2)
        {
            LinearFunction result = new LinearFunction(xposition1);
            return result;
        }
        else
        {
            //wzþr na funkcję przechodzącą przez 2 punkty
            float a = (yposition1-yposition2)/(float)(xposition1-xposition2);
            float b = (yposition1-a*xposition1);
            LinearFunction result = new LinearFunction(a,b);

            return result;
        }

    }

    //oblicza współrzędną na osi x
    public int getX(int order)
    {
        int divider = gridsCells;
        int step = (int)Math.sqrt(gridsCells);

        if(order == 0)
        {
            return 0;
        }
        else
        {
            while( (order % divider) > step)
            {
                divider -= step;
            }

            return order-divider;
        }


    }

    //oblicza współrzedna na osi y
    public int getY(int xposition, int order)
    {
        int step = (int)Math.sqrt(gridsCells);
        if(order<step)
        {
            return 0;
        }
        else
        {
            //pobranie pierwszej cyfry z integera
            return -Integer.parseInt(Integer.toString(order-xposition).substring(0, 1));
        }
    }

}
