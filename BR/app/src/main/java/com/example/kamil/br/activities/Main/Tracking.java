package com.example.kamil.br.activities.main;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamil.br.R;
import com.example.kamil.br.database.model.BluetoothResults;
import com.example.kamil.br.math.BluetoothDistance;


/**
 * aktywność służaca do pobrania nazwy urządzenia od użytkownika,
 * które ma być "śledzone"
 * Created by Kamil
 */
public class Tracking extends AppCompatActivity {

    private static String TAG = Tracking.class.getSimpleName();
    /**
     * Pole tekstowe wyświetlajace nazwę urządzenia
     */
    private TextView nameTextView;

    /**
     * Pole tesktowe wyświetlające adres urządzenia
     */
    private TextView addressTextView;

    /**
     * Pole tesktowe wyświetlajace zasięg w jakim znajduje się urządzenie
     */
    private TextView distanceTextView;

    /**
     * Przycisk uruchamiający wyszukiwanie urządzeń
     */
    private ImageButton searchButton;

    /**
     * Adapter bluetooth
     */
    private BluetoothAdapter mBluetoothAdapter;

    /**
     * Kołko postępu
     */
    private ProgressDialog progressCircle;

    /**
     * Obiekt, który reaguje na powiadomienie, w tym przypadku bluetooth
     */
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
                if ((device.getName().equals(nameTextView.getText().toString())) || device.getAddress().equals(addressTextView.getText().toString())) {

                    Float distance = BluetoothDistance.getDistance(rssi, -1);
                    String distance_ = Float.toString(distance);
                    if (distance_.length() > 4)
                        distance_ = distance_.substring(0, 4);
                    distanceTextView.setText(distance_+" m");

                    if (distance > 0 && distance <= 2.5) {
                        distanceTextView.setTextColor(Color.rgb(0, 255, 0));
                    } else if (distance > 2.5 && distance <= 5) {
                        distanceTextView.setTextColor(Color.rgb(255, 255, 51));
                    } else if (distance > 5 && distance <= 7.5) {
                        distanceTextView.setTextColor(Color.rgb(255, 140, 0));
                    } else {
                        distanceTextView.setTextColor(Color.rgb(255, 0, 0));
                    }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        nameTextView = (TextView) findViewById(R.id.trackingNameTextView);
        addressTextView = (TextView) findViewById(R.id.trackingAddressTextView);
        distanceTextView = (TextView) findViewById(R.id.trackingDistanceTextView);

        LayoutInflater inflater = LayoutInflater.from(this);
        View prompt = inflater.inflate(R.layout.tracking_input, null);

        AlertDialog.Builder promptDialog = new AlertDialog.Builder(this);
        promptDialog.setView(prompt);
        final EditText nameEditText = (EditText) prompt.findViewById(R.id.trackingInputNameEditText);
        final EditText addressEditText = (EditText) prompt.findViewById(R.id.trackingInputAddressEditText);

        //okno do wpisywania danych
        promptDialog
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
                                    nameTextView.setText(nameValue);
                                    addressTextView.setText(addressValue);
                                }
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = promptDialog.create();
        alertDialog.show();

        searchButton = (ImageButton) findViewById(R.id.buttonTrackingSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    progressCircle = new ProgressDialog(view.getContext());
                    progressCircle.setMessage(getResources().getString(R.string.progress_scanning));
                    progressCircle.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressCircle.show();
                    progressCircle.setCanceledOnTouchOutside(false);
                    getFoundDevices();
            }
        });
    }

    /**
     * Fukcja rozpoczynająca proces wyszukiwania
     */
    private void getFoundDevices()
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
        if(mBluetoothAdapter != null)
        {
            mBluetoothAdapter.cancelDiscovery();
        }
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
