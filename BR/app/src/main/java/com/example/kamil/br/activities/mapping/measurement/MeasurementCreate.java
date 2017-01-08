package com.example.kamil.br.activities.mapping.measurement;

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

import com.example.kamil.br.database.controller.MeasurementsController;
import com.example.kamil.br.database.model.Measurements;
import com.example.kamil.br.views.PathDrawView;
import com.example.kamil.br.R;
import com.example.kamil.br.database.controller.BluetoothResultsController;
import com.example.kamil.br.database.controller.PathDataController;
import com.example.kamil.br.database.model.BluetoothResults;
import com.example.kamil.br.database.model.PathData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MeasurementCreate extends AppCompatActivity
{
    /**
     * aktywność służaca do mapowania pomieszczenia, nawigujemy po kolei po krawędziach, każde przejscie po krawędzi trzeba
     * oznaczyć w czasie, naciskając przycisk startu a następnie stopu, gdy znajdziemy się w wybranym miejscu szukamy urządzenia,
     * gdy zakończymy proces możemy go zapisać
     *
     * proces szukania urządzeń jest zasobożerną procedurą, trwa około 12 sekund
     *
    From documentation:
    The discovery process usually involves an inquiry scan of about 12 seconds, followed by a page scan of each new device to retrieve its Bluetooth name.
    Device discovery is a heavyweight procedure.

    I suppose that you have to wait until discovery routine is finished and there's no ways to speed up this process.
     */
    private String TAG = MeasurementCreate.class.getSimpleName();
    /**
     * adapter bluetooth
     */
    private BluetoothAdapter mBluetoothAdapter;

    /**
     * lista na urządzenia bluetooth
     */
    private ArrayList<BluetoothResults> arrayOfFoundBTDevices;

    /**
     * przycisk rozpoczynający szukanie
     */
    private ImageButton buttonSearch;

    /**
     * przycisk nawigujący po krawędziach
     */
    private ImageButton buttonNext;

    /**
     * przycisk rozpoczynający lub zakończający odliczanie
     */
    private ImageButton buttonStopStart;

    /**
     * przycisk zapisywania
     */
    private ImageButton buttonSave;

    /**
     * kólko postępu, gdy trwa szukanie urządzeń
     */
    private ProgressDialog progressBar;

    /**
     * lista z danymi o krawędziach pokoju
     */
    private ArrayList<PathData> list;

    /**
     * widok, na którym rysowany jest kształ pomieszczenia
     */
    private PathDrawView map;

    /**
     * numer obecnej krawędzi
     */
    private int counter=0;

    /**
     * ilość krawędzi
     */
    private int counterLimit;

    /**
     * zmienna logiczna zapisujaca stan czy trwa odliczanie
     */
    private boolean ifTheClockIsTicking = false;

    /**
     * czas rozpoczęcia pomiaru
     */
    private long timeStart=0;

    /**
     * czas zakończenia pomiaru
     */
    private long timeStop=0;

    /**
     * identyfikator pomiaru
     */
    private int idMeasurements;

    /**
     * identyfikator pokoju
     */
    private int idRooms;

    /**
     * Parametr procesu
     */
    private int process ;

    /**
     * objekt mReceiver
     */
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_creator);

        //odebranie paczki
        idRooms = getIntent().getIntExtra("idRooms",-1);
        process = getIntent().getIntExtra("process",-1);
        list = (ArrayList<PathData>) new PathDataController().selectPathDataWhereIdRoom(getApplicationContext(), idRooms);


        //pobranie id pomiaru, który ma zostać utworzony
        idMeasurements = MeasurementsController.getLastRecord(getApplicationContext()).getIdMeasurements()+1;

        arrayOfFoundBTDevices = new ArrayList<>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        list = (ArrayList<PathData>) new PathDataController().selectPathDataWhereIdRoom(getApplicationContext(), idRooms);
        map = (PathDrawView) findViewById(R.id.viewDrawMap);
        map.setData(list);
        map.setNumber(counter);
        counterLimit = list.get(list.size()-1).getEdgeNumber();

        PathDataController.printAllTableToLog(list);

        buttonSearch = (ImageButton) findViewById(R.id.buttonMeasurementCreatorSeatch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //rozpoczęcie szukania
                progressBar = new ProgressDialog(v.getContext());
                progressBar.setMessage(getResources().getString(R.string.progress_scanning));
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.show();
                progressBar.setCanceledOnTouchOutside(false);
                Log.d("Wywołanie funkcji", "Wywoływanie display ..");
                displayListOfFoundDevices();
            }
        });

        buttonNext = (ImageButton) findViewById(R.id.buttonMeasurementCreatorNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //przejście po krawędziach
                counterIncrement();
                map.setNumber(counter);
                map.invalidate();
            }
        });

        buttonStopStart = (ImageButton) findViewById(R.id.buttonMeasurementCreatorStartStop);
        buttonStopStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //odmierzanie czasu, zawsze wstawiany jest rekord z czasem przejścia danej odległości
                //bez danych o jakimkolwiek urządzeniu, tzw. pusty rekord
                if(ifTheClockIsTicking)
                {
                    buttonStopStart.setImageResource(R.drawable.start_process_icon);
                    timeStop = System.currentTimeMillis();
                    ifTheClockIsTicking = false;
                    buttonNext.setVisibility(View.VISIBLE);
                    buttonSearch.setVisibility(View.VISIBLE);
                    buttonSave.setVisibility(View.VISIBLE);
                    BluetoothResults emptyResult = new BluetoothResults(getTimeDifference(timeStart, timeStop));
                    emptyResult.setEdgeNumber(counter);
                    emptyResult.setIdMeasurements(idMeasurements);
                    emptyResult.setIdRooms(idRooms);
                    arrayOfFoundBTDevices.add(emptyResult);
                }
                else
                {
                    //jeśli odliczanie trwa, blokowane są inne elementy gui
                    buttonStopStart.setImageResource(R.drawable.stop_icon);
                    timeStart = System.currentTimeMillis();
                    ifTheClockIsTicking = true;
                    buttonNext.setVisibility(View.INVISIBLE);
                    buttonSearch.setVisibility(View.INVISIBLE);
                    buttonSave.setVisibility(View.INVISIBLE);
                }
            }
        });

        buttonSave = (ImageButton) findViewById(R.id.buttonMeasurementCreatorSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //zapisanie elementów do bazy
                createMeasurement(idRooms);
                BluetoothResultsController controller = new BluetoothResultsController();
                //wstawienie do bazy
                controller.insertList(arrayOfFoundBTDevices, getApplicationContext());
                BluetoothResultsController.printAllTableToLog(arrayOfFoundBTDevices);

                if( process == 1 )
                    launchMeasurementViewer();
            }
        });

    }

    /**
     * iteracja po ilości krawędzi, po osiągnieciu limitu przekręca sie od nowa
     */
    public void counterIncrement()
    {
        if(counter == counterLimit)
        {
            counter = 0;
        }
        else
            counter++;
    }

    /**
     * zwraca daną numer krawędzi powiększony o jeden uwzględniając przekręcanie się
     * @return kolejna wartość licznika
     */
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

    /**
     * zwraca czas trwania wydarzenia
     * @param start godzina początkowa
     * @param stop godzina końcowa
     * @return czas trwania
     */
    public long getTimeDifference(long start, long stop)
    {
        long time = stop-start;
        Log.d(TAG, "Czas w ms: "+Long.toString((time)));
        return time;
    }

    /**
     * szuka urządzeń bluetooth i wstawia je do listy
     */
    private void displayListOfFoundDevices()
    {
        Log.d("DisplayList", "W środku ");


        mBluetoothAdapter.startDiscovery();

        // Discover new devices
        // Create a BroadcastReceiver for ACTION_FOUND

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //nowe rzeczy
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
    }

    /**
     * tworzy nowy pomiar w nazwie używając obecną date
     * @param idToPass id nowego pomiary
     */
    public void createMeasurement(int idToPass)
    {
        Measurements measurement = new Measurements();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date date = new Date();
        measurement.setName(dateFormat.format(date));
        measurement.setIdRooms(idToPass);
        MeasurementsController controller = new MeasurementsController();
        controller.insert(measurement, getApplicationContext());
    }

    /**
     * Przechodzi do aktywnosci pokazania mapy
     */
    private void launchMeasurementViewer()
    {
        int idMeasurement = MeasurementsController.getLastRecord(getApplicationContext()).getIdMeasurements();
        Intent intent = new Intent(this, MeasurementView.class);
        Log.d(TAG, "idMeasure: "+idMeasurement);
        intent.putExtra("idRooms", idRooms);
        intent.putExtra("process", process);
        intent.putExtra("idMeasurement", idMeasurement);
        startActivity(intent);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mBluetoothAdapter.cancelDiscovery();
    }

}