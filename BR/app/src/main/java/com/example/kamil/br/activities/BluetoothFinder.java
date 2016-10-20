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

import com.example.kamil.br.BluetoothFinderAdapter;
import com.example.kamil.br.R;
import com.example.kamil.br.Record;
import com.example.kamil.br.database.DBHandler;
import com.example.kamil.br.database.model.BluetoothResults;

import java.util.ArrayList;

public class BluetoothFinder extends AppCompatActivity
{
    //https://developer.android.com/guide/topics/connectivity/bluetooth.html
    /*
    From documentation:
    The discovery process usually involves an inquiry scan of about 12 seconds, followed by a page scan of each new device to retrieve its Bluetooth name.
    Device discovery is a heavyweight procedure.

    I suppose that you have to wait until discovery routine is finished and there's no ways to speed up this process.
     */
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothResults> arrayOfFoundBTDevices;
    private String direction;
    private DBHandler db;
    private Button buttonSearch;
    private Button buttonCorner;
    private BluetoothFinderAdapter adapter;
    private ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_devices);
        db = new DBHandler(this);

        arrayOfFoundBTDevices = new ArrayList<>();
      /*  List<Record> elements = db.getAll();
        long time = System.currentTimeMillis();
        if(elements.isEmpty()==true)
        {
            db.insert(new Record(null,0,time,0));
           // Log.d("Sprawdzanie wstawiania true",);
        }
        */


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //Log.d("Wywołanie funkcji", "Wywoływanie display ..");
        //displayListOfFoundDevices();

        buttonSearch = (Button) findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                progressBar = new ProgressDialog(v.getContext());
                progressBar.setMessage(getResources().getString(R.string.progres_scanning));
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.show();
                progressBar.setCanceledOnTouchOutside(false);



                Log.d("Wywołanie funkcji", "Wywoływanie display ..");
                displayListOfFoundDevices();

            }
        });

        buttonCorner = (Button) findViewById(R.id.button_corner);
        buttonCorner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d("przyciskCorner", "Wywoływanie ");
                addValueStop();
            }
        });





    }

    private void displayListOfFoundDevices()
    {
        Log.d("DisplayList", "W środku ");

        arrayOfFoundBTDevices.clear();
        // Zainicjonowanie adaptera listą urządzeń, i ustawienie na listView adaptera
        adapter = new BluetoothFinderAdapter(getApplicationContext(), arrayOfFoundBTDevices);
        ListView lista = (ListView) findViewById(R.id.list);
        lista.setAdapter(adapter);

        // rozpocznij szukanie

        mBluetoothAdapter.startDiscovery();

        // Discover new devices
        // Create a BroadcastReceiver for ACTION_FOUND
        final BroadcastReceiver mReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Log.d("OnReceive", "W środku ");

                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {

                    // Get the bluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    // Get the "RSSI" to get the signal strength as integer,
                    // but should be displayed in "dBm" units
                    int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);

                    // Create the device object and add it to the arrayList of devices
                    BluetoothResults bluetoothResults = new BluetoothResults();
                    bluetoothResults.setName(device.getName());
                    bluetoothResults.setAddress(device.getAddress());
                    bluetoothResults.setRssi(rssi);
                    bluetoothResults.setTime(System.currentTimeMillis());
                    //narazie jeden, trzeba zmienic


                    //wstawianie do listy
                    arrayOfFoundBTDevices.add(bluetoothResults);
                    //wstawianie do bazy
                    addValue(bluetoothResults);

                    // Zainicjonowanie adaptera listą urządzeń, i ustawienie na listView adaptera
                    adapter = new BluetoothFinderAdapter(getApplicationContext(), arrayOfFoundBTDevices);
                    ListView lista = (ListView) findViewById(R.id.list);
                    lista.setAdapter(adapter);
                }
                else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
                {
                    progressBar.dismiss();
                    unregisterReceiver(this);

                }
                else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
                {
                    Log.d("BluetoothAdapter", "Starting discovery ");
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //nowe rzeczy
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

    private void addValue(BluetoothResults record)
    {
        Log.d("Insert: ", "Inserting to BluetoothResults");
        //db.insert(new Record(name,rssi,time,1));
    }

    private void addValueStop()
    {
        Log.d("przyciskCorner", "wstawianie przerwy ");
        long time = System.currentTimeMillis();
        db.insert(new Record(null,0,time,0));
    }
}