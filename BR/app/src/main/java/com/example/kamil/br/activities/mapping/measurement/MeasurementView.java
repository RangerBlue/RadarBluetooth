package com.example.kamil.br.activities.mapping.measurement;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.kamil.br.math.BluetoothDistance;
import com.example.kamil.br.R;
import com.example.kamil.br.activities.main.MappingMenu;
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

    /**
     * lista kolorów
     */
    private ArrayList<Integer> colors;

    /**
     * Przycisk wyświetlający informacje
     */
    private ImageButton buttonInfo;

    /**
     * Przycisk zamykajacy aktywność
     */
    private ImageButton buttonClose;

    /**
     * Lista z nazwami unikalnych urządzeń
     */
    private ArrayList<String> distinctDevices;
    private String TAG = MeasurementView.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_results_view);
        map = (MapDrawView) findViewById(R.id.viewMapDraw);


        //odebranie paczki
        int idMeasurement = getIntent().getIntExtra("idMeasurement",-1);
        final int idRooms = getIntent().getIntExtra("idRooms",-1);
        final int process = getIntent().getIntExtra("process",-1);


        BluetoothResultsController btController = new BluetoothResultsController();
        ArrayList<BluetoothResults> bluetoothResultsList;
        bluetoothResultsList = (ArrayList<BluetoothResults>) btController.selectBluetoothResultsWhereIdRoomsAndIdMeasurements(getApplicationContext(), idRooms, idMeasurement );
        btController.printAllTableToLog(bluetoothResultsList);

        PathDataController pdController = new PathDataController();
        ArrayList<PathData> pathDataList ;
        pathDataList = (ArrayList<PathData>) pdController.selectPathDataWhereIdRoom(getApplicationContext(), idRooms);

        distinctDevices = BluetoothResultsController.selectNameDistinct(getApplicationContext(), idRooms, idMeasurement);
        colors = BluetoothDistance.getColorsForDevices(distinctDevices.size());
        //ustawienie danych w widoku
        //dane o krawędziach pokoju
        map.setPath(pathDataList);
        //dane o resultatach pomiaru
        map.setResults(bluetoothResultsList);
        //dane o unikalnych urządzeniach
        map.setDistinctDevices(distinctDevices);
        //ustawienie kolorów
        map.setColors(colors);



        buttonInfo = (ImageButton) findViewById(R.id.buttonMeasurementViewerInfo);
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableLayout layout = new TableLayout(getApplicationContext());
                for( int i=0; i<distinctDevices.size(); i++)
                {
                    TextView item = new TextView(getApplicationContext());
                    item.setText(distinctDevices.get(i));
                    item.setTextColor(colors.get(i));
                    layout.addView(item);
                }
                layout.setPadding(10,0,0,0);

                new AlertDialog.Builder(MeasurementView.this)
                        .setTitle(getResources().getString(R.string.map_devices))
                        .setView(layout)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setIcon(R.drawable.map_icon)
                        .show();
            }
        });

        buttonClose = (ImageButton) findViewById(R.id.buttonMeasurementViewerClose);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MeasurementView.this, MappingMenu.class);
                Intent intent2 = new Intent(MeasurementView.this, MeasurementChooser.class);

                if (process == 1)
                    startActivity(intent1);
                else
                   finish();
            }
        });



    }
}
