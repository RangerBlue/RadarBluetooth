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

    private TextView appName ;
    private BluetoothAdapter mBluetoothAdapter;
    /**
     * Przycisk do włączenia bluetoth
     */
    private ImageButton buttonEnableBT;
    /**
     * Przycisk do przejścia do aktwności szukania urządzeń
     */
    private ImageButton buttonScanBT;
    private Button buttonData;
    private Button buttonPath;
    private ImageButton buttonSearchDevices;
    private ImageButton buttonRadar;
    private ImageButton buttonMap;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ustawienie tekstu w dobrej wielkosci w zaleznosci od wielkosci urządzenia, trzeba sprawdzic na prawdziwym, na podglądzie nie widać
       // appName = (TextView) findViewById(R.id.textview_title);
        //appName.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.fab_margin));

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        buttonEnableBT = (ImageButton) findViewById(R.id.button_enableBT);
        //buttonScanBT = (ImageButton) findViewById(R.id.button_scanBT);
        //buttonData = (Button) findViewById(R.id.button_edit_room) ;
        //buttonPath = (Button) findViewById(R.id.button_path);
        buttonSearchDevices = (ImageButton) findViewById(R.id.button_search_devices);
        buttonRadar = (ImageButton) findViewById(R.id.buttton_radar);
        buttonMap = (ImageButton) findViewById(R.id.button_map);

        buttonRadar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radar();
            }
        });

        buttonEnableBT.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                enableBluetoothOnDevice();
            }
        });
        if(mBluetoothAdapter.isEnabled())
        {
            buttonEnableBT.setImageResource(R.drawable.bt_icon_blue);
        }
        else
        {
            buttonEnableBT.setImageResource(R.drawable.bt_icon_red);
        }
        /*
        buttonScanBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                measurement();
            }
        });

        buttonPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawPath();
            }
        });

        buttonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editRoom();
            }
        });

        buttonSearchDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDevices();
            }
        });

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map();
            }
        });
 */
    }//end onCreate

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


    private void measurement()
    {

        Intent intent = new Intent(this, MeasurementEditor.class);
        startActivity(intent);
    }


    private void drawPath()
    {
        Intent intent = new Intent(this, PathEditor.class);
        startActivity(intent);
    }

    private void editRoom()
    {
        Intent intent = new Intent(this, RoomMenu.class);
        startActivity(intent);
    }

    private void searchDevices()
    {
        Intent intent = new Intent(this, BluetoothFinder.class);
        startActivity(intent);
    }
    private void radar()
    {
        Intent intent = new Intent(this, Radar.class);
        startActivity(intent);
    }

    private void map()
    {
        Intent intent = new Intent(this, BluetoothResultsChooseRoom.class);
        startActivity(intent);
    }

}
