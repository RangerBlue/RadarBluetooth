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

import com.example.kamil.br.adapters.BluetoothFinderAdapter;
import com.example.kamil.br.views.PathDrawView;
import com.example.kamil.br.R;
import com.example.kamil.br.database.DBHandler;
import com.example.kamil.br.database.controller.BluetoothResultsController;
import com.example.kamil.br.database.controller.PathDataController;
import com.example.kamil.br.database.model.BluetoothResults;
import com.example.kamil.br.database.model.PathData;

import java.util.ArrayList;

public class MeasurementCreate extends AppCompatActivity
{
    //https://developer.android.com/guide/topics/connectivity/bluetooth.html
    /*
    From documentation:
    The discovery process usually involves an inquiry scan of about 12 seconds, followed by a page scan of each new device to retrieve its Bluetooth name.
    Device discovery is a heavyweight procedure.

    I suppose that you have to wait until discovery routine is finished and there's no ways to speed up this process.
     */
    private String TAG = MeasurementCreate.class.getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothResults> arrayOfFoundBTDevices;
    private DBHandler db;
    private Button buttonSearch;
    private Button buttonNext;
    private Button buttonStopStart;
    private Button buttonSave;
    private BluetoothFinderAdapter adapter;
    private ProgressDialog progressBar;
    private ArrayList<PathData> list;
    private PathDrawView map;
    private int counter=0;
    private int counterLimit;
    private boolean ifTheClockIsTicking = false;
    private long timeStart=0;
    private long timeStop=0;
    private int idMeasurements;
    private int  idRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_creator);

        //odebranie paczki
        idRooms = getIntent().getIntExtra("idRooms",-1);
        list = (ArrayList<PathData>) new PathDataController().selectPathDataWhereId(getApplicationContext(), idRooms);
        idMeasurements = getIntent().getIntExtra("idMeasurements", -1);


        arrayOfFoundBTDevices = new ArrayList<>();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        buttonSearch = (Button) findViewById(R.id.buttonMeasurementCreatorSeatch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                progressBar = new ProgressDialog(v.getContext());
                progressBar.setMessage(getResources().getString(R.string.progress_scanning));
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.show();
                progressBar.setCanceledOnTouchOutside(false);
                Log.d("Wywołanie funkcji", "Wywoływanie display ..");
                displayListOfFoundDevices();

            }
        });

        list = (ArrayList<PathData>) new PathDataController().selectPathDataWhereId(getApplicationContext(), idRooms);
        map = (PathDrawView) findViewById(R.id.viewDrawMap);
        map.setData(list);
        map.setNumber(counter);
        counterLimit = list.get(list.size()-1).getEdgeNumber();

        PathDataController.printAllTableToLog(list);

        buttonNext = (Button) findViewById(R.id.buttonMeasurementCreatorNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                counterIncrement();
                map.setNumber(counter);
                map.invalidate();

                Log.d(TAG, "Wybrana krawędź "+Integer.toString(counter));
            }
        });

        buttonStopStart = (Button) findViewById(R.id.buttonMeasurementCreatorStartStop);
        buttonStopStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(ifTheClockIsTicking)
                {
                    buttonStopStart.setText(R.string.start);
                    timeStop = System.currentTimeMillis();
                    ifTheClockIsTicking = false;
                    buttonNext.setVisibility(View.VISIBLE);
                    buttonSearch.setVisibility(View.VISIBLE);
                    BluetoothResults emptyResult = new BluetoothResults(getTimeDifference(timeStart, timeStop));
                    emptyResult.setEdgeNumber(counter);
                    emptyResult.setIdMeasurements(idMeasurements);
                    emptyResult.setIdRooms(idRooms);
                    arrayOfFoundBTDevices.add(emptyResult);
                }
                else
                {
                    buttonStopStart.setText(R.string.stop);
                    timeStart = System.currentTimeMillis();
                    ifTheClockIsTicking = true;
                    buttonNext.setVisibility(View.INVISIBLE);
                    buttonSearch.setVisibility(View.INVISIBLE);

                }
            }
        });

        buttonSave = (Button) findViewById(R.id.buttonMeasurementCreatorSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                BluetoothResultsController controller = new BluetoothResultsController();
                //wstawienie do bazy
                for( BluetoothResults item : arrayOfFoundBTDevices)
                {
                    controller.insert(item, getApplicationContext());
                }
                BluetoothResultsController.printAllTableToLog(arrayOfFoundBTDevices);

            }
        });







    }

    public void counterIncrement()
    {
        if(counter == counterLimit)
        {
            counter = 0;
        }
        else
            counter++;
    }

    public int getCounterIncrement()
    {
        if(counter == counterLimit)
        {
            return 0;
        }
        else
        {
            int _counter = counter;
            return ++_counter;
        }

    }

    public long getTimeDifference(long start, long stop)
    {
        long time = stop-start;
        Log.d(TAG, "Czas w ms: "+Long.toString((time)));
        return time;
    }

    private void displayListOfFoundDevices()
    {
        Log.d("DisplayList", "W środku ");


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
                    bluetoothResults.setTime(getTimeDifference(timeStart, timeStop));
                    bluetoothResults.setEdgeNumber(counter);
                    bluetoothResults.setIdMeasurements(idMeasurements);
                    bluetoothResults.setIdRooms(idRooms);


                    //wstawianie do listy
                    arrayOfFoundBTDevices.add(bluetoothResults);

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

}