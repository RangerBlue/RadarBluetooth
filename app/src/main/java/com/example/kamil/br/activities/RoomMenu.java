package com.example.kamil.br.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kamil.br.R;

public class RoomMenu extends AppCompatActivity {

    private Button buttonCreate;
    private Button buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_menu);

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
        Intent intent = new Intent(this, RoomChoose.class);
        startActivity(intent);
    }
}
