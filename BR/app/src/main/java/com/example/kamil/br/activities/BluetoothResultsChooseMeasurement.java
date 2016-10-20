package com.example.kamil.br.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kamil.br.adapters.MeasurementAdapter;
import com.example.kamil.br.R;
import com.example.kamil.br.database.controller.MeasurementsController;
import com.example.kamil.br.database.model.Measurements;

import java.util.ArrayList;

public class BluetoothResultsChooseMeasurement extends AppCompatActivity {

    private ListView list;
    private MeasurementAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_results_choose_measurement);
        //odebranie paczki
        final int idRooms = getIntent().getIntExtra("idRooms",-1);

        list = (ListView) findViewById(R.id.listViewBluetoothResultsChooseMeasurement);
        ArrayList<Measurements> all = (ArrayList<Measurements>) new MeasurementsController().selectMeasurementWhereIdRoom(getApplicationContext(), idRooms);
        MeasurementsController.printAllTableToLog(all);

        adapter = new MeasurementAdapter(this, all);
        list.setAdapter(adapter);
        list.setClickable(true);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Measurements measurement = adapter.getItem(position);
                int idToPass = measurement.getIdMeasurements();
                Intent intent = new Intent(BluetoothResultsChooseMeasurement.this, BluetoothResultsView.class);
                intent.putExtra("idMeasurement", idToPass);
                intent.putExtra("idRooms", idRooms);
                startActivity(intent);
            }
        });

    }
}
