package com.example.kamil.br.activities.mapping.results;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kamil.br.R;
import com.example.kamil.br.adapters.MeasurementAdapter;
import com.example.kamil.br.database.controller.MeasurementsController;
import com.example.kamil.br.database.model.Measurements;

import java.util.ArrayList;

/**
 * aktwyność służąca do wybrania pomiaru, z ktorego można wyświetlić rezultaty
 */
public class BluetoothResultsMeasurementChooser extends AppCompatActivity {

    /**
     * widok listowy
     */
    private ListView list;

    /**
     * adapter listy
     */
    private MeasurementAdapter adapter;
    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_chooser);
        final int idRooms = getIntent().getIntExtra("idRooms",-1);

        list = (ListView) findViewById(R.id.listViewMeasurementChooser);
        registerForContextMenu(list);
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
                Intent intent = new Intent(BluetoothResultsMeasurementChooser.this, BluetoothResultsEditor.class);
                intent.putExtra("idMeasurement", idToPass);
                intent.putExtra("idRooms", idRooms);
                startActivity(intent);
            }
        });

    }



}
