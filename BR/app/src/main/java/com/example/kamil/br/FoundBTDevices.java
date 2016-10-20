package com.example.kamil.br;

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

import java.util.ArrayList;
import java.util.List;

public class FoundBTDevices extends AppCompatActivity
{
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothObject> arrayOfFoundBTDevices;
    private String direction;
    private DBHandler db;
    private Button buttonSearch;
    private Button buttonCorner;
    private FoundBTDevicesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_devices);
        db = new DBHandler(this);
        List<Record> elements = db.getAll();
        long time = System.currentTimeMillis();
        if(elements.isEmpty()==true)
        {
            db.insert(new Record(null,0,time,0));
           // Log.d("Sprawdzanie wstawiania true",);
        }





        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.d("Wywołanie funkcji", "Wywoływanie display ..");
        displayListOfFoundDevices();

        buttonSearch = (Button) findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                Log.d("przycisk0", "Wywoływanie ");
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

        arrayOfFoundBTDevices = new ArrayList<BluetoothObject>();

        // start looking for bluetooth devices

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
                    BluetoothObject bluetoothObject = new BluetoothObject();
                    bluetoothObject.setBluetoothName(device.getName());
                    bluetoothObject.setBluetoothAddress(device.getAddress());
                    bluetoothObject.setBluetoothRssi(rssi);

                    arrayOfFoundBTDevices.add(bluetoothObject);
                    //String rssi_string = Integer.toString(rssi);
                    addValue(device.getName(), rssi);

                    // 1. Pass context and data to the custom adapter
                    adapter = new FoundBTDevicesAdapter(getApplicationContext(), arrayOfFoundBTDevices);
                    ListView lista = (ListView) findViewById(R.id.list);
                    lista.setAdapter(adapter);
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        //Unregister
        //unregisterReceiver(mReceiver);


    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mBluetoothAdapter.cancelDiscovery();
    }

    private void addValue(String name, int rssi)
    {
        Log.d("Insert: ", "Inserting record");
        long time = System.currentTimeMillis();
        db.insert(new Record(name,rssi,time,1));
    }

    private void addValueStop()
    {
        Log.d("przyciskCorner", "wstawianie przerwy ");
        long time = System.currentTimeMillis();
        db.insert(new Record(null,0,time,0));
    }
}