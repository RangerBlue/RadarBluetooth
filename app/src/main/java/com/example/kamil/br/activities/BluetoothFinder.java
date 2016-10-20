package com.example.kamil.br.activities;

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
import android.widget.Button;
import android.widget.ListView;

import com.example.kamil.br.BluetoothDistance;
import com.example.kamil.br.adapters.BluetoothFinderAdapter;
import com.example.kamil.br.R;
import com.example.kamil.br.database.model.BluetoothResults;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Aktywność, w której ma miejsce szukanie dostępnych urządzeń bluetooth,
 * podobnie jak w aplikacji systemowej, ale zwracana jest dodatkowo informacja o
 * wielkości RSSI oraz odległości od naszego urządzenia. Zwrócone informacje
 * nie są nigdzie zapisywane.
 * Created by Kamil
 */
public class BluetoothFinder extends AppCompatActivity {

    private String TAG = BluetoothFinder.class.getSimpleName();

    /**
     * Adapter bluetooth
     */
    private BluetoothAdapter mBluetoothAdapter;
    /**
     * Lista znalezionych urządzeń
     */
    private ArrayList<BluetoothResults> arrayOfFoundBTDevices;
    /**
     * Przycisk rozpoczynający szukanie urządzeń
     */
    private Button buttonSearch;
    /**
     * Adapter do listy
     */
    private BluetoothFinderAdapter adapter;
    /**
     * Kólko postępu
     */
    private ProgressDialog progressCircle;
    /**
     * Obiekt, który reaguje na powiadomienie, w tym przypadku bluetooth
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            // znaleziono urządzenie
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {

                // pobranie urządzenia bluetooth
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // pobranie rssi
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                Log.d(TAG, "znaleziono urządzenie: "+device.getName()+" o rssi: "+rssi+" dB");

                // stworzenie obiektu opisującego pomiar
                BluetoothResults bluetoothResults = new BluetoothResults();

                DecimalFormat format = new DecimalFormat();
                format.setMinimumFractionDigits(2);

                bluetoothResults.setName(device.getName()+" is "+Float.toString(BluetoothDistance.getDistance(rssi)).substring(0,4)+" from here");
                bluetoothResults.setAddress(device.getAddress());
                bluetoothResults.setRssi(rssi);
                bluetoothResults.setTime(System.currentTimeMillis());

                BluetoothDistance.getDistance(rssi);

                //wstawianie do listy
                arrayOfFoundBTDevices.add(bluetoothResults);

                // Zainicjonowanie adaptera listą urządzeń, i ustawienie na listView adaptera
                adapter = new BluetoothFinderAdapter(getApplicationContext(), arrayOfFoundBTDevices);
                ListView lista = (ListView) findViewById(R.id.listBluetoothFinder);
                lista.setAdapter(adapter);
            }
            //zakończono szukanie
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                Log.i(TAG, "zakończono szukanie");
                progressCircle.dismiss();
                unregisterReceiver(this);

            }
            //rozpoczęto szukanie
            else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
            {
                Log.i(TAG, "rozpoczęto szukanie");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i(TAG, "wywołanie onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_finder);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        arrayOfFoundBTDevices = new ArrayList<>();

        //obsługa przycisku search
        buttonSearch = (Button) findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.i(TAG, "Wciśnięto przycisk search");
                progressCircle = new ProgressDialog(v.getContext());
                progressCircle.setMessage(getResources().getString(R.string.progress_scanning));
                progressCircle.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressCircle.show();
                progressCircle.setCanceledOnTouchOutside(false);
                getFoundDevices();
            }
        });
    }
    /**
     * Szuka dostępnych urządzeń i wyświetla je na liście
     */
    private void getFoundDevices()
    {
        Log.i(TAG,"wywołanie getFoundDevices");
        //wyczyszczenie listy
        arrayOfFoundBTDevices.clear();
        // Zainicjonowanie adaptera listą urządzeń, i ustawienie na listView adaptera
        adapter = new BluetoothFinderAdapter(getApplicationContext(), arrayOfFoundBTDevices);
        ListView lista = (ListView) findViewById(R.id.listBluetoothFinder);
        lista.setAdapter(adapter);

        // rozpoczęcie szukania
        mBluetoothAdapter.startDiscovery();

        // zarejestrowanie broadcastreceiver
        IntentFilter filter = new IntentFilter();
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
        if(mBluetoothAdapter != null)
        {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
       // unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
