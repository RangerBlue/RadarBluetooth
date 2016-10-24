package com.example.kamil.br.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.kamil.br.R;

public class MappingMenu extends AppCompatActivity {

    private String TAG = MappingMenu.class.getSimpleName();

    /**
     * Przycisk do rozpoczęcia sekwencji czynności prowadzących do otrzymania mapy
     */
    private ImageButton buttonStartProcess;

    /**
     * Przycisk do edycji pokoji
     */
    private ImageButton buttonRoom;

    /**
     * Przycisk edycji pomiarów
     */
    private ImageButton buttonMeasurement;

    /**
     * Przycisk do edycji ścieżki
     */
    private ImageButton buttonPath;

    /**
     * Przycisk do edycji map
     */
    private ImageButton buttonMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping_menu);

        buttonStartProcess = (ImageButton) findViewById(R.id.button_start_process);

        buttonRoom = (ImageButton) findViewById(R.id.button_room);
        buttonRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRoomMenu();
            }
        });

        buttonMeasurement = (ImageButton) findViewById(R.id.button_measurement);

        buttonPath = (ImageButton) findViewById(R.id.button_path);

        buttonMap = (ImageButton) findViewById(R.id.button_map);
    }

    private void launchRoomMenu()
    {
        Intent intent = new Intent(this, RoomMenu.class);
        startActivity(intent);
    }
}
