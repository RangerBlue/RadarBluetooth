package com.example.kamil.br.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.kamil.br.R;
import com.example.kamil.br.database.controller.MeasurementsController;
import com.example.kamil.br.database.model.Measurements;

import java.util.ArrayList;
import java.util.List;

public class MeasurementChooseOption extends AppCompatActivity {

    private ImageButton buttonCreate;
    private ImageButton buttonView;
    private int idRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_results_choose_option);

        //odebranie paczki
        idRooms = getIntent().getIntExtra("idRooms",-1);
        Log.d("odbieranieidz listy v2", String.valueOf(idRooms));

        buttonCreate = (ImageButton) findViewById(R.id.buttonCreateBluetoothResultsEditor);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });

        buttonView = (ImageButton) findViewById(R.id.buttonViewBluetoothResultsEditor);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view();
            }
        });


        List<Measurements> all = new MeasurementsController().selectAll(getApplicationContext());
        MeasurementsController.printAllTableToLog((ArrayList<Measurements>) all);

       // Log.d("dupa", MeasurementsController.selectMeasurementWhereIdRoom(getApplicationContext(), idRooms).get(0).getName());

    }

    /**
     * Nie można kliknąć przycsiku jeśli nie istnieje żaden pomiar
     */
    private void hideButtonIfMeasurementNotExist()
    {
        if((MeasurementsController.selectMeasurementWhereIdRoom(getApplicationContext(), idRooms).isEmpty()))
        {
            buttonCreate.setClickable(false);
        }
    }

    //TODO:zmienić podpis klasy
    private void create()
    {
        Intent intent = new Intent(this, MeasurementCreate.class);
        intent.putExtra("idRooms", idRooms);
        startActivity(intent);
    }

    private void view()
    {
        Intent intent = new Intent(this, MeasurementChooser.class);
        intent.putExtra("idRooms", idRooms);
        startActivity(intent);
    }


}
