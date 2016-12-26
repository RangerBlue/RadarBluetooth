package com.example.kamil.br.activities.mapping.results;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kamil.br.R;
import com.example.kamil.br.adapters.BluetoothResultsAdapter;
import com.example.kamil.br.database.controller.BluetoothResultsController;
import com.example.kamil.br.database.model.BluetoothResults;

import java.util.ArrayList;

/**
 * Aktywność służąca do wyświetlania wszystkich rezultatów w pomiarze, przytrzymując
 * item można go usunąć z menu podręcznego
 */
public class BluetoothResultsEditor extends AppCompatActivity {

    /**
     * widok listowy
     */
    private ListView list;

    /**
     * adapter listy
     */
    private BluetoothResultsAdapter adapter;
    private String TAG = getClass().getSimpleName();

    /**
     * id pomiaru
     */
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

            case R.id.menu_delete:
                BluetoothResultsController.deleteWhereId(idbtResult, getApplicationContext());
                Toast.makeText(getApplicationContext(), R.string.deleted_result, Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
}
