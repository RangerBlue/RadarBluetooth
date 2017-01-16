package com.example.kamil.br.activities.main;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;


import com.example.kamil.br.math.BluetoothDistance;
import com.example.kamil.br.R;
import com.example.kamil.br.views.RadarDrawView;
import com.example.kamil.br.database.model.BluetoothResults;

import java.util.ArrayList;

/**
 * Aktywność służąca do przestawienia wyników wyszukiwania urządzeń w postaci graficznej,
 * w jakim zasięgu one sie znajdują
 * Created by Kamil
 */
public class Radar extends AppCompatActivity {

    private final String TAG = Radar.class.getSimpleName();

    /**
     * Adapter bluetooth
     */
    private BluetoothAdapter mBluetoothAdapter;

    /**
     * Lista znalezionych urządzeń
     */
    private ArrayList<BluetoothResults> arrayOfFoundBTDevices;

    /**
     * Przycisk do rozpoczęcia wyszukiwania
     */
    private ImageButton buttonSearch;

    /**
     * Przycisk do wyświetlenia informacji
     */
    private ImageButton buttonInfo;

    /**
     * Pasek postępu, podczas szukania
     */
    private ProgressDialog progressBar;

    /**
     * Układ wspólrzędnych, na którym wyświetlana jest odległość od znalezionych urządzeń
     */
    private RadarDrawView radar;

    /**
     * Lista kolorów
     */
    private ArrayList<Integer> colors;

    /**
     * Liczba urządzeń
     */
    public int numberOfDevices;

    /**
     * Obiekt, który reaguje na powiadomienie, w tym przypadku bluetooth
     */
    final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.d("OnReceive", "W środku ");

            String action = intent.getAction();
            // znaleziono urządzenie
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {

                // pobranie urządzenia bluetooth
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // pobranie rssi
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);

                // stworzenie obiektu opisującego pomiar
                BluetoothResults bluetoothResults = new BluetoothResults();
                bluetoothResults.setName(device.getName());
                bluetoothResults.setAddress(device.getAddress());
                bluetoothResults.setRssi(rssi);
                bluetoothResults.setTime(System.currentTimeMillis());

                //wstawianie do listy
                arrayOfFoundBTDevices.add(bluetoothResults);

            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                progressBar.dismiss();
                unregisterReceiver(this);
                radar.setData(arrayOfFoundBTDevices);
                numberOfDevices = arrayOfFoundBTDevices.size();
                colors = BluetoothDistance.getColorsForDevices(numberOfDevices);
                radar.setColorList(colors);
                radar.invalidate();

            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
            {
               // Log.d("BluetoothAdapter", "Starting discovery ");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);
        arrayOfFoundBTDevices = new ArrayList<>();
        radar = (RadarDrawView) findViewById(R.id.viewRadarDraw);
        radar.setData(arrayOfFoundBTDevices);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        buttonSearch = (ImageButton) findViewById(R.id.buttonRadarSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                progressBar = new ProgressDialog(v.getContext());
                progressBar.setMessage(getResources().getString(R.string.progress_scanning));
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.show();
                progressBar.setCanceledOnTouchOutside(false);
                getFoundDevices();
            }
        });

        buttonInfo = (ImageButton) findViewById(R.id.buttonRadarInfo);
        buttonInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TableLayout layout = new TableLayout(getApplicationContext());


                for( int i=0; i<numberOfDevices; i++)
                {
                    TextView item = new TextView(getApplicationContext());
                    item.setText(arrayOfFoundBTDevices.get(i).getName());
                    item.setTextColor(colors.get(i));
                    layout.addView(item);
                }
                layout.setPadding(10,0,0,0);

                new AlertDialog.Builder(Radar.this)
                        .setTitle(getResources().getString(R.string.found_devices))
                        .setView(layout)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            }
                        })
                        .setIcon(R.drawable.tracking_icon)
                        .show();
            }
        });

    }

    private void getFoundDevices()
    {
        Log.d("DisplayList", "W środku ");

        arrayOfFoundBTDevices.clear();
        mBluetoothAdapter.startDiscovery();

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mBluetoothAdapter.cancelDiscovery();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
