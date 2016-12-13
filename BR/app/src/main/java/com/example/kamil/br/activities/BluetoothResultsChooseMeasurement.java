package com.example.kamil.br.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kamil.br.adapters.MeasurementAdapter;
import com.example.kamil.br.R;
import com.example.kamil.br.database.controller.MeasurementsController;
import com.example.kamil.br.database.model.Measurements;

import java.util.ArrayList;

public class BluetoothResultsChooseMeasurement extends AppCompatActivity {

    private ListView list;
    private MeasurementAdapter adapter;
    private String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_results_choose_measurement);
        //odebranie paczki
        final int idRooms = getIntent().getIntExtra("idRooms",-1);
        Log.d(TAG, "UWAGAGAAG"+idRooms);
        list = (ListView) findViewById(R.id.listViewBluetoothResultsChooseMeasurement);
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
                Intent intent = new Intent(BluetoothResultsChooseMeasurement.this, BluetoothResultsView.class);
                intent.putExtra("idMeasurement", idToPass);
                intent.putExtra("idRooms", idRooms);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //ustawienie menu
        if(v.getId() == R.id.listViewBluetoothResultsChooseMeasurement)
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.room_choose_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //obłsuga menu
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Measurements measurement = adapter.getItem(info.position);
        int idToPass = measurement.getIdMeasurements();
        switch (item.getItemId())
        {
            case R.id.menu_edit:
                Log.d(TAG, "edit");
                return true;
            case R.id.menu_delete:
                Log.d(TAG, "delete");
                MeasurementsController controller = new MeasurementsController();
                controller.deleteMeasurementAndAllDependencies(idToPass, getApplicationContext());
                Toast.makeText(getApplicationContext(), R.string.deleted_room, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "delete");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
}
