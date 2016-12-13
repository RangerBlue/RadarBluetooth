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

import com.example.kamil.br.R;
import com.example.kamil.br.adapters.BluetoothResultsAdapter;
import com.example.kamil.br.adapters.MeasurementAdapter;
import com.example.kamil.br.database.controller.BluetoothResultsController;
import com.example.kamil.br.database.controller.MeasurementsController;
import com.example.kamil.br.database.controller.RoomsController;
import com.example.kamil.br.database.model.BluetoothResults;
import com.example.kamil.br.database.model.Measurements;
import com.example.kamil.br.database.model.Rooms;

import java.util.ArrayList;

public class MeasurementEditor extends AppCompatActivity {

    private ListView list;
    private BluetoothResultsAdapter adapter;
    private String TAG = getClass().getSimpleName();
    private int idMeasurements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_editor);

        //odebranie paczki
        final int idRooms = getIntent().getIntExtra("idRooms",-1);
        idMeasurements = getIntent().getIntExtra("idMeasurement",-1);

        list = (ListView) findViewById(R.id.listViewMeasurementEditor);
        registerForContextMenu(list);
        ArrayList<BluetoothResults> all = (ArrayList<BluetoothResults>) new BluetoothResultsController().selectBluetoothResultsWhereIdRoomsAndIdMeasurements(getApplicationContext(), idRooms, idMeasurements);
        BluetoothResultsController.printAllTableToLog(all);

        adapter = new BluetoothResultsAdapter(this, all);
        list.setAdapter(adapter);

       /* list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        }); */
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //ustawienie menu
        if(v.getId() == R.id.listViewMeasurementEditor)
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.room_choose_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //obłsuga menu
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        BluetoothResults btResult = adapter.getItem(info.position);
        int idToPass = btResult.getIdMeasurements();
        int idbtResult = btResult.getIdBluetoothResults();
        switch (item.getItemId())
        {
            case R.id.menu_edit:
                Log.d(TAG, "edit");
                return true;
            case R.id.menu_delete:
                Log.d(TAG, "delete");
                BluetoothResultsController controller = new BluetoothResultsController();
                BluetoothResultsController.deleteWhereId(idbtResult, getApplicationContext());
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
