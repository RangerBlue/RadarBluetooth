package com.example.kamil.br.activities.mapping.room;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.kamil.br.R;
import com.example.kamil.br.activities.mapping.path.PathCreator;
import com.example.kamil.br.database.controller.RoomsController;
import com.example.kamil.br.database.model.BluetoothResults;
import com.example.kamil.br.database.model.Rooms;
import com.example.kamil.br.math.BluetoothDistance;

/**
 * Aktywność służaca do dodania nowego pokoju
 * Created by Kamil
 */
public class RoomCreator extends AppCompatActivity {
    /**
     * Przycisk do potwierdzenia dodania pokoju
     */
    private ImageButton confirmButton;
    /**
     * Pole tekstowe na nazwę pokoju
     */
    private EditText roomNameEditText;
    /**
     * Nazwa urządzenia
     */
    String name ;

    /**
     * Adres urządzenia
     */
    String address;

    /**
     * Liczniki średnichwartości RSSI dla każdego metra odległości
     */
    int[] values = { 0,0,0,0,0,0,0,0,0 };

    /**
     * Kołko postępu
     */
    public ProgressDialog progressCircle;

    /**
     * Obiekt, który reaguje na powiadomienie, w tym przypadku bluetooth
     */

    /**
     * Numer pozycji wciśniętego przycsiku
     */
    int currentPosition=-1;

    /**
     * Adapter bluetooth
     */
    private BluetoothAdapter mBluetoothAdapter;

    private boolean ifOver = false;

    private float valueN;

    AlertDialog alertDialog;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // znaleziono urządzenie
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                // pobranie urządzenia bluetooth
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // pobranie rssi
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                //Log.d(TAG, "znaleziono urządzenie: " + device.getName() + " o rssi: " + rssi + " dB");

                // stworzenie obiektu opisującego pomiar
                BluetoothResults bluetoothResults = new BluetoothResults();

                //ustawienie koloru w zależności od odległości
                if ((device.getName().equals(name) || device.getAddress().equals(address)))
                {
                    values[currentPosition] = (values[currentPosition]==0) ? values[currentPosition]=rssi : (values[currentPosition]+rssi)/2;
                }
            }
            //zakończono szukanie
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //Log.i(TAG, "zakończono szukanie");
                progressCircle.dismiss();
                unregisterReceiver(this);

            }
            //rozpoczęto szukanie
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //Log.i(TAG, "rozpoczęto szukanie");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_creator);

        //odebranie paczki z aktywności procesu
        final int process = getIntent().getIntExtra("process",-1);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        LayoutInflater inflater = LayoutInflater.from(this);
        View prompt = inflater.inflate(R.layout.calibrate_input, null);

        View prompt1 = inflater.inflate(R.layout.tracking_input, null);

        AlertDialog.Builder promptDialogInput = new AlertDialog.Builder(this);
        promptDialogInput.setView(prompt1);
        final AlertDialog.Builder promptDialog = new AlertDialog.Builder(this);
        promptDialog.setView(prompt);

        Button button1 = (Button) prompt.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = 0;
                Log.d("sprwadzamy ", String.valueOf(currentPosition));
                progressCircle = new ProgressDialog(view.getContext());
                progressCircle.setMessage(getResources().getString(R.string.progress_scanning));
                progressCircle.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressCircle.show();
                progressCircle.setCanceledOnTouchOutside(false);
                getFoundDevices();
            }
        });

        Button button2 = (Button) prompt.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = 1;
                Log.d("sprwadzamy ", String.valueOf(currentPosition));
                progressCircle = new ProgressDialog(view.getContext());
                progressCircle.setMessage(getResources().getString(R.string.progress_scanning));
                progressCircle.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressCircle.show();
                progressCircle.setCanceledOnTouchOutside(false);
                getFoundDevices();
            }
        });

        Button button3 = (Button) prompt.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = 2;
                progressCircle = new ProgressDialog(view.getContext());
                progressCircle.setMessage(getResources().getString(R.string.progress_scanning));
                progressCircle.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressCircle.show();
                progressCircle.setCanceledOnTouchOutside(false);
                getFoundDevices();
            }
        });

        Button button4 = (Button) prompt.findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = 3;
                progressCircle = new ProgressDialog(view.getContext());
                progressCircle.setMessage(getResources().getString(R.string.progress_scanning));
                progressCircle.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressCircle.show();
                progressCircle.setCanceledOnTouchOutside(false);
                getFoundDevices();
            }
        });

        Button button5 = (Button) prompt.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = 4;
                progressCircle = new ProgressDialog(view.getContext());
                progressCircle.setMessage(getResources().getString(R.string.progress_scanning));
                progressCircle.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressCircle.show();
                progressCircle.setCanceledOnTouchOutside(false);
                getFoundDevices();
            }
        });

        Button button6 = (Button) prompt.findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = 5;
                progressCircle = new ProgressDialog(view.getContext());
                progressCircle.setMessage(getResources().getString(R.string.progress_scanning));
                progressCircle.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressCircle.show();
                progressCircle.setCanceledOnTouchOutside(false);
                getFoundDevices();
            }
        });

        Button button7 = (Button) prompt.findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = 6;
                progressCircle = new ProgressDialog(view.getContext());
                progressCircle.setMessage(getResources().getString(R.string.progress_scanning));
                progressCircle.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressCircle.show();
                progressCircle.setCanceledOnTouchOutside(false);
                getFoundDevices();
            }
        });

        Button button8 = (Button) prompt.findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = 7;
                progressCircle = new ProgressDialog(view.getContext());
                progressCircle.setMessage(getResources().getString(R.string.progress_scanning));
                progressCircle.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressCircle.show();
                progressCircle.setCanceledOnTouchOutside(false);
                getFoundDevices();
            }
        });

        Button button9 = (Button) prompt.findViewById(R.id.button9);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = 8;
                progressCircle = new ProgressDialog(view.getContext());
                progressCircle.setMessage(getResources().getString(R.string.progress_scanning));
                progressCircle.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressCircle.show();
                progressCircle.setCanceledOnTouchOutside(false);
                getFoundDevices();
            }
        });

        Button buttonOK = (Button) prompt.findViewById(R.id.calibrateEnd);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0 ; i <9 ; i++)
                {
                    Log.d("sprwadzamy "+i, String.valueOf(values[i]));
                }
                valueN = BluetoothDistance.getValueN(values);
                Log.d("sprwadzamy wynik", String.valueOf(valueN));
                alertDialog.dismiss();
            }
        });

        alertDialog = promptDialog.create();
        final EditText nameEditText = (EditText) prompt1.findViewById(R.id.trackingInputNameEditText);
        final EditText addressEditText = (EditText) prompt1.findViewById(R.id.trackingInputAddressEditText);
        //okno do wpisywania danych
        promptDialogInput
                .setCancelable(false)
                .setMessage(getResources().getString(R.string.tracking_input))
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id)
                            {
                                final String nameValue = nameEditText.getText().toString();
                                final String addressValue = addressEditText.getText().toString();
                                if(nameValue.isEmpty() && addressValue.isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), R.string.wrong_input, Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    name = nameValue;
                                    address = addressValue;
                                }

                                alertDialog.show();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialogInput = promptDialogInput.create();
        alertDialogInput.show();


        promptDialog
                .setCancelable(false)
                .setMessage(getResources().getString(R.string.gps_input))
                .setPositiveButton(getResources().getString(R.string.set),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id)
                            {

                            }
                        })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });





        roomNameEditText = (EditText) findViewById(R.id.editTextRoomName);



        confirmButton = (ImageButton) findViewById(R.id.buttonConfirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if( !roomNameEditText.getText().toString().isEmpty() && !RoomsController.ifNameExists(getApplicationContext(), roomNameEditText.getText().toString()))
                {
                    Rooms room = new Rooms();
                    room.setName(roomNameEditText.getText().toString());
                    room.setWalkRatio(0);
                    if(values[0] == 0) {
                        room.setA(-74);
                        room.setN(1.7f);
                    }
                    else
                    {
                        room.setA(values[0]);
                        room.setN(valueN);
                    }
                    RoomsController roomController = new RoomsController();
                    roomController.insert(room, getApplicationContext());
                    Toast.makeText(getApplicationContext(), R.string.added_room, Toast.LENGTH_SHORT).show();
                    roomNameEditText.setFocusable(false);
                    roomNameEditText.setClickable(false);
                    v.setClickable(false);

                    if(process == 1)
                    launchPathEditor(process);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), R.string.wrong_input, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    /**
     * Przechodzi do aktywnosci rysowania kształtu pomieszczenia
     * @param process parametr procesu
     */
    private void launchPathEditor(int process)
    {
        int idRooms = RoomsController.getLastRecord(getApplicationContext()).getIdRooms();
        Intent intent = new Intent(this, PathCreator.class);
        intent.putExtra("id", idRooms);
        intent.putExtra("process", process);
        startActivity(intent);
    }

    /**
     * Funkcja rozpoczynajca proces szukania
     */
    public void getFoundDevices()
    {

        mBluetoothAdapter.startDiscovery();

        // zarejestrowanie broadcastreceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
    }
}
