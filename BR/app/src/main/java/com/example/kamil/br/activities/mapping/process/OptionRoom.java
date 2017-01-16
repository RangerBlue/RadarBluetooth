package com.example.kamil.br.activities.mapping.process;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.kamil.br.R;
import com.example.kamil.br.activities.mapping.room.RoomChoose;
import com.example.kamil.br.activities.mapping.room.RoomCreator;

/**
 * Aktwyność służąca do wyboru działania w procesie
 * Created by Kamil
 */
public class OptionRoom extends AppCompatActivity {

    /**
     * Przycisk do utworzenia pokoju
     */
    private ImageButton buttonCreate;
    /**
     * Przycisk do wyboru istniejącego
     */
    private ImageButton buttonExisting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_room);

        buttonCreate = (ImageButton) findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });

        buttonExisting = (ImageButton) findViewById(R.id.buttonEdit);
        buttonExisting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                existing();
            }
        });
    }

    /**
     * Przejście do aktwyności dodawania pokoju
     */
    private void create()
    {
        Intent intent = new Intent(this, RoomCreator.class);
        intent.putExtra("process", 1);
        startActivity(intent);
    }

    /**
     * Przycisk do aktywności edycji pokoju
     */
    private void existing()
    {
        Intent intent = new Intent(this, RoomChoose.class);
        intent.putExtra("process", 1);
        startActivity(intent);
    }
}
