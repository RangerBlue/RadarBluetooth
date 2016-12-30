package com.example.kamil.br.activities.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.kamil.br.R;
import com.example.kamil.br.activities.mapping.process.OptionRoom;
import com.example.kamil.br.activities.mapping.results.BluetoothResultsChooseRoom;
import com.example.kamil.br.activities.mapping.measurement.MeasurementChooseRoom;
import com.example.kamil.br.activities.mapping.path.PathChooseRoom;
import com.example.kamil.br.activities.mapping.room.RoomMenu;

/**
 * Menu mapowania, glówna część aplikacji, obsługa mapowania
 */
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
        buttonStartProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 launchProcess();
            }
        });

        buttonRoom = (ImageButton) findViewById(R.id.button_room);
        buttonRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRoomMenu();
            }
        });

        buttonMeasurement = (ImageButton) findViewById(R.id.button_measurement);
        buttonMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchBluetoothResultsMenu();
            }
        });

        buttonPath = (ImageButton) findViewById(R.id.button_path);
        buttonPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPathMenu();
            }
        });

        buttonMap = (ImageButton) findViewById(R.id.button_map);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMeasurementMenu();
            }
        });
    }

    /**
     * funkcja włączająca aktywność obsługi pomieszczeń
     */
    private void launchRoomMenu()
    {
        Intent intent = new Intent(this, RoomMenu.class);
        startActivity(intent);
    }

    /**
     * funkcja włączająca aktywnośc obsługi kształtu pomieszczeń
     */
    private void launchPathMenu()
    {
        Intent intent = new Intent(this, PathChooseRoom.class);
        startActivity(intent);
    }

    /**
     * funkcja włączająca aktywnośc obsługi pomiarów(mapowań)
     */
    private void launchMeasurementMenu()
    {
        Intent intent = new Intent(this, MeasurementChooseRoom.class);
        startActivity(intent);
    }

    /**
     * funkcja włączająca aktywnośc obsługi rezultatów w pomiarze
     */
    private void launchBluetoothResultsMenu()
    {
        Intent intent = new Intent(this, BluetoothResultsChooseRoom.class);
        startActivity(intent);
    }

    /**
     * funkcja włączająca aktywnośc procesu
     */
    private void launchProcess()
    {
        Intent intent = new Intent(this, OptionRoom.class);
        startActivity(intent);
    }
}
