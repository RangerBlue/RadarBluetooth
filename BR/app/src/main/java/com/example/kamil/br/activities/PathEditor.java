package com.example.kamil.br.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.kamil.br.R;
import com.example.kamil.br.database.controller.PathDataController;
import com.example.kamil.br.database.model.PathData;

import java.util.List;

public class PathEditor extends AppCompatActivity {

    private Button buttonCreate;
    private Button buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_editor);

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
                view();
            }
        });

        //wypisanie całej bazy
        List<PathData> all = new PathDataController().selectAll(getApplicationContext());
        for ( PathData element : all )
        {
            Log.d("Tabela PathData: ",
                            "ID "+String.valueOf(element.getIdPathData()) +
                            " A "+ element.getA() +
                            " B "+ element.getB() +
                            " X "+ element.getX() +
                            " Linear "+ element.getIsIfLinear() +
                            " P1 "+ element.getP1() +
                            " P2 "+ element.getP2() +
                            " EdgeNumbers "+ element.getEdgeNumber() +
                            " IdRooms "+ element.getIdRooms()
                ) ;
        }


    }





    private void create()
    {
        Intent intent = new Intent(this, PathChooseRoom.class);
        startActivity(intent);
    }

    private void view()
    {
        Intent intent = new Intent(this, RoomViewer.class);
        startActivity(intent);
    }

}
