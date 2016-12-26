package com.example.kamil.br.activities.main;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


import com.example.kamil.br.adapters.BluetoothFinderAdapter;
import com.example.kamil.br.R;
import com.example.kamil.br.views.RadarDrawView;
import com.example.kamil.br.database.model.BluetoothResults;

import java.util.ArrayList;

/**
 * aktywność służąca do przestawienia wyników wyszukiwania urządzeń w postaci graficznej,
 * w jakiej odległości one sie znajdują
 */
public class Radar extends AppCompatActivity {

    /**
     * adapter bluetooth
     */
    private BluetoothAdapter mBluetoothAdapter;

    /**
     * lista znalezionych urządzeń
     */
    private ArrayList<BluetoothResults> arrayOfFoundBTDevices;

    /**
     * przycisk do rozpoczęcia wyszukiwania
     */
    private ImageButton buttonSearch;

    /**
     * pasek postępu, podczas szukania
     */
    private ProgressDialog progressBar;

    /**
     * układ wspólrzędnych, na którym wyświetlana jest odległość od znalezionych urządzeń
     */
    private RadarDrawView radar;

    private final String TAG = Radar.class.getSimpleName();

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
                Log.d(TAG, "action_finished" );
                progressBar.dismiss();
                unregisterReceiver(this);
                radar.setData(arrayOfFoundBTDevices);
                radar.invalidate();
            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
            {
                Log.d("BluetoothAdapter", "Starting discovery ");
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
    protected void onPause()
    {
        super.onPause();
        mBluetoothAdapter.cancelDiscovery();
    }
}
