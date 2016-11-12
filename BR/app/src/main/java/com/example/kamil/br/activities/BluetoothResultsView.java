package com.example.kamil.br.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.kamil.br.R;
import com.example.kamil.br.database.controller.BluetoothResultsController;
import com.example.kamil.br.database.controller.PathDataController;
import com.example.kamil.br.database.model.BluetoothResults;
import com.example.kamil.br.database.model.PathData;
import com.example.kamil.br.views.MapDrawView;

import java.util.ArrayList;

public class BluetoothResultsView extends AppCompatActivity {

    private MapDrawView map;
    private String TAG = BluetoothResultsView.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_results_view);
        map = (MapDrawView) findViewById(R.id.viewMapDraw);


        //odebranie paczki
        int idMeasurement = getIntent().getIntExtra("idMeasurement",-1);
        int idRooms = getIntent().getIntExtra("idRooms",-1);



        BluetoothResultsController btController = new BluetoothResultsController();
        ArrayList<BluetoothResults> bluetoothResultsList;
        bluetoothResultsList = (ArrayList<BluetoothResults>) btController.selectBluetoothResultsWhereIdRoomsAndIdMeasurements(getApplicationContext(), idRooms, idMeasurement );
        //btController.printAllTableToLog(bluetoothResultsList);

        PathDataController pdController = new PathDataController();
        ArrayList<PathData> pathDataList ;
        pathDataList = (ArrayList<PathData>) pdController.selectPathDataWhereId(getApplicationContext(), idRooms);

        ArrayList<String> distinctDevices = btController.selectNameDistinct(getApplicationContext(), idRooms, idMeasurement);

        Log.d(TAG, "tu jestem");

        map.setPath(pathDataList);
        map.setResults(bluetoothResultsList);

    }
}
