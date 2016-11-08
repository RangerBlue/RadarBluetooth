package com.example.kamil.br.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamil.br.R;

/**
 * Menu główne, tutaj znajduje się całe sterowanie aplikacji
 * Created by Kamil
 */
public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    /**
     * Zmienna całkowitoliczbowa do włączenia bluetooth, powinna być większa niż 0
     */
    private int REQUEST_ENABLE_BT = 99;

    /**
     * Adapter bluetooth
     */
    private BluetoothAdapter mBluetoothAdapter;

    /**
     * Przycisk do przejścia do menu mapowania
     */
    private ImageButton buttonMapping;

    /**
     * Przycisk do włączenia bluetoth
     */
    private ImageButton buttonEnableBT;

    /**
     * Przycisk to aktywności śledzenia urządzeń
     */
    private ImageButton buttonTracking;

    /**
     * Przycisk do włączenia radaru
     */
    private ImageButton buttonRadar;

    /**
     * Przycisk do przejścia do aktwności szukania urządzeń
     */
    private ImageButton buttonSearchDevices;

    /**
     * Przycisk do przejścia do ustawień
     */
    private ImageButton buttonSettings;



    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        buttonMapping = (ImageButton) findViewById(R.id.button_map);
        buttonMapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mappingMenu();
            }
        });

        buttonEnableBT = (ImageButton) findViewById(R.id.button_enableBT);
        buttonEnableBT.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                enableBluetoothOnDevice();
            }
        });
        buttonTracking = (ImageButton) findViewById(R.id.button_tracking_device);
        buttonTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackingActivity();
            }
        });

        buttonRadar = (ImageButton) findViewById(R.id.buttton_radar);
        buttonRadar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radar();
            }
        });

        buttonSearchDevices = (ImageButton) findViewById(R.id.button_search_devices);
        buttonSearchDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDevices();
            }
        });

        buttonSettings = (ImageButton) findViewById(R.id.button_settings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings();
            }
        });

        //checkBluetoothState();
    }

    /**
     * Włącza bluetooth jeśli nie jest włączony, jeśli urządzenie go nie posiada zamyka aplikację
     */
    private void enableBluetoothOnDevice()
    {
        //jeśli nie istnieje
        if (mBluetoothAdapter == null)
        {
            Log.e(TAG, String.valueOf(R.string.no_bluetooth));
            Toast.makeText(this,R.string.no_bluetooth , Toast.LENGTH_LONG).show();
            finish();
        }

        // jeślii nie jest włączony to go włącza
        if( !mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else
        {
            Toast.makeText(this, R.string.enabled_bluetooth, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT)
        {
            if (resultCode == 0)
            {
                Toast.makeText(this,R.string.not_allow_bluetooth, Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this,R.string.enabling_bluetooth , Toast.LENGTH_LONG).show();
                buttonEnableBT.setImageResource(R.drawable.bt_icon_blue);
            }
        }
    }


    /**
     * Przechodzi do aktywności śledzenia urządzenia
     */
    private void trackingActivity()
    {

    }

    /**
     * Przechodzi do aktywności szukania urządzenia
     */
    private void searchDevices()
    {
        Intent intent = new Intent(this, BluetoothFinder.class);
        startActivity(intent);
    }

    /**
     * Przechodzi do aktywności szukania urządzeń na radarze
     */
    private void radar()
    {
        Intent intent = new Intent(this, Radar.class);
        startActivity(intent);
    }

    /**
     * Przechodzi do aktywności mapowania
     */
    private void mappingMenu()
    {
        Intent intent = new Intent(this, MappingMenu.class);
        startActivity(intent);
    }

    /**
     * Przechodzi do aktywności ustawień użytkownika
     */
    private void settings()
    {
        Intent intent = new Intent(this, UserSpeed.class);
        startActivity(intent);
    }

    /**
     * Sprawdza stan modułu bluetooth, jeśli jest włączony ikonka staje się niebieska,
     * jeśli jest wyłączony czerwona
     */
    private void checkBluetoothState()
    {
        if(mBluetoothAdapter.isEnabled())
        {
            buttonEnableBT.setImageResource(R.drawable.bt_icon_blue);
        }
        else
        {
            buttonEnableBT.setImageResource(R.drawable.bt_icon_red);
        }
    }

}
