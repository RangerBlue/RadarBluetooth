package com.example.kamil.br.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kamil.br.R;
import com.example.kamil.br.database.controller.PathDataController;
import com.example.kamil.br.database.model.PathData;

public class RoomEditor extends AppCompatActivity {

    private Button buttonCreate;
    private Button buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_editor);

        buttonCreate = (Button) findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });

        buttonView = (Button) findViewById(R.id.buttonView);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view();
            }
        });

    }

    private void create()
    {
        Intent intent = new Intent(this, RoomCreator.class);
        startActivity(intent);
    }

    private void view()
    {
        Intent intent = new Intent(this, RoomViewer.class);
        startActivity(intent);
    }
}
