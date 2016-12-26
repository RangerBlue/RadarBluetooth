package com.example.kamil.br.activities.mapping.measurement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.kamil.br.R;
import com.example.kamil.br.database.controller.MeasurementsController;
import com.example.kamil.br.database.model.Measurements;

import java.util.ArrayList;
import java.util.List;

/**
 * Aktywność służąca do wyboru między dodaniem nowego mapowania, a wybraniem istniejącego
 * do wyświetlenia lub edytowania
 */
public class MeasurementChooseOption extends AppCompatActivity {

    /**
     * przycisk do utworzenia pomiaru
     */
    private ImageButton buttonCreate;

    /**
     * przycisk do wyświetlenia pomiaru
     */
    private ImageButton buttonView;

    /**
     * identyfikator pokoju, w którym są pomiary
     */
    private int idRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_results_choose_option);

        //odebranie paczki
        idRooms = getIntent().getIntExtra("idRooms",-1);

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



    }

    /**
     * Nie można kliknąć przycsiku jeśli nie istnieje żaden pomiar
     */
    private void hideButtonIfMeasurementNotExist(ImageButton button)
    {
        if((MeasurementsController.selectMeasurementWhereIdRoom(getApplicationContext(), idRooms).isEmpty()))
        {
            button.setClickable(false);
        }
    }

    /**
     * włączenie aktywności utworzenie pomiaru
     */
    private void create()
    {
        Intent intent = new Intent(this, MeasurementCreate.class);
        intent.putExtra("idRooms", idRooms);
        startActivity(intent);
    }

    /**
     * włączenie aktywności edytowania pomiaru
     */
    private void view()
    {
        Intent intent = new Intent(this, MeasurementChooser.class);
        intent.putExtra("idRooms", idRooms);
        startActivity(intent);
    }


}
