package com.example.kamil.br.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kamil.br.R;


public class MainActivity extends AppCompatActivity {

    private static Context context;
    private String LOG_TAG;
    private int REQUEST_ENABLE_BT = 99; // Any positive integer should work.
    private BluetoothAdapter mBluetoothAdapter;

    private Button buttonEnableBT;
    private Button buttonScanBT;
    private Button buttonData;
    private Button buttonPath;
    private Button buttonSearchDevices;
    private Button buttonRadar;
    private Button buttonMap;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        LOG_TAG = getResources().getString(R.string.app_name);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        buttonEnableBT = (Button) findViewById(R.id.button_enableBT);
        buttonScanBT = (Button) findViewById(R.id.button_scanBT);
        buttonData = (Button) findViewById(R.id.button_edit_room) ;
        buttonPath = (Button) findViewById(R.id.button_path);
        buttonSearchDevices = (Button) findViewById(R.id.button_search_devices);
        buttonRadar = (Button) findViewById(R.id.buttton_radar);
        buttonMap = (Button) findViewById(R.id.button_map);

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

        /*
        buttonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               int i=0;
                List<Record> elements = db.getAll();
                for (Record r : elements)
                {
                    if(r.getDirection()==0)
                        i++;
                }
                if(i==5)
                {
                    Log.d("open baza activivty","udało sie");
                    showDatabase();
                }
                else
                {
                    Log.d("open baza activivty","nie udało sie");
                    for (Record item : elements)
                    {
                        db.delete(item);
                    }
                    Toast.makeText(MainActivity.this, "Błąd w bazie, wyczyszczono", Toast.LENGTH_LONG).show();
                }
            }
        });

        */
    }//end onCreate

    private void enableBluetoothOnDevice()
    {
        if (mBluetoothAdapter == null)
        {
            Log.e(LOG_TAG, "This device does not have a bluetooth adapter");
            finish();
            // If the android device does not have bluetooth, just return and get out.
            // There's nothing the app can do in this case. Closing app.
        }

        // Check to see if bluetooth is enabled. Prompt to enable it
        if( !mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
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
                // If the resultCode is 0, the user selected "No" when prompt to
                // allow the app to enable bluetooth.
                // You may want to display a dialog explaining what would happen if
                // the user doesn't enable bluetooth.
                Toast.makeText(this, "The user decided to deny bluetooth access", Toast.LENGTH_LONG).show();
            }
            else
                Log.i(LOG_TAG, "User allowed bluetooth access!");
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
        Intent intent = new Intent(this, RoomEditor.class);
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

    public static Context getContext() {
        return context;
    }
}
