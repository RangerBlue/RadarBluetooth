package com.example.kamil.br.activities.mapping.measurement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kamil.br.R;
import com.example.kamil.br.database.controller.BluetoothResultsController;
import com.example.kamil.br.database.controller.PathDataController;
import com.example.kamil.br.database.model.BluetoothResults;
import com.example.kamil.br.database.model.PathData;
import com.example.kamil.br.views.MapDrawView;

import java.util.ArrayList;

/**
 * aktywność służąca do wyświetlenia mapy ze znalezionymi urządzeniami, w postaci punktów
 * w pomieszczeniu
 */
public class MeasurementView extends AppCompatActivity {

    /**
     * widok na którym będzie wyświetlany wynik
     */
    private MapDrawView map;
    private String TAG = MeasurementView.class.getSimpleName();

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
        btController.printAllTableToLog(bluetoothResultsList);

        PathDataController pdController = new PathDataController();
        ArrayList<PathData> pathDataList ;
        pathDataList = (ArrayList<PathData>) pdController.selectPathDataWhereIdRoom(getApplicationContext(), idRooms);

        ArrayList<String> distinctDevices = BluetoothResultsController.selectNameDistinct(getApplicationContext(), idRooms, idMeasurement);

        //ustawienie danych w widoku
        //dane o krawędziach pokoju
        map.setPath(pathDataList);
        //dane o resultatach pomiaru
        map.setResults(bluetoothResultsList);
        //dane o unikalnych urządzeniach
        map.setDistinctDevices(distinctDevices);
    }
}
