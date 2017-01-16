package com.example.kamil.br.activities.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kamil.br.R;
import com.example.kamil.br.activities.settings.UserSpeed;
import com.example.kamil.br.database.controller.BluetoothResultsController;
import com.example.kamil.br.database.controller.MeasurementsController;
import com.example.kamil.br.database.controller.PathDataController;
import com.example.kamil.br.database.controller.RoomsController;

/**
 * Aktywność służąca do ustawienia preferencji użytkownika
 * Created by Kamil
 */
public class Settings extends AppCompatActivity {

    /**
     * Pole tekstowe do wyświetlenia prędkości
     */
    private TextView speedValueTextView;

    /**
     * Pole tekstowe do wyświetlenia liczby pomieszczeń
     */
    private TextView numberOfRoomsTextView;

    /**
     * Pole tekstowe do wyświetlenia liczby ścian
     */
    private TextView numberOfEdgesTextView;

    /**
     * Pole tekstowe do wyświetlenia liczby pomiarów
     */
    private TextView numberOfMeasurementsTextView;

    /**
     * Pole tekstowe do wyświetlenia liczby rezultatów
     */
    private TextView numberOfResultsTextView;

    /**
     * Przycisk przejścia do aktywności ustawiania prędkości
     */
    private Button setSpeedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("options", 0);
        speedValueTextView = (TextView) findViewById(R.id.textViewSettingsGPSValue);
        speedValueTextView.setText(String.valueOf(pref.getFloat("velocity", 0f)));

        numberOfRoomsTextView = (TextView) findViewById(R.id.textViewSettingsRoomAmount);
        numberOfRoomsTextView.setText( Integer.toString(RoomsController.selectAll(getApplicationContext()).size()));

        numberOfEdgesTextView = (TextView) findViewById(R.id.textViewSettingsEdgesAmount);
        numberOfEdgesTextView.setText(Integer.toString(PathDataController.selectAll(getApplicationContext()).size()));

        numberOfMeasurementsTextView = (TextView) findViewById(R.id.textViewSettingsMeasurementsAmount);
        numberOfMeasurementsTextView.setText(Integer.toString(MeasurementsController.selectAll(getApplicationContext()).size()));

        numberOfResultsTextView = (TextView) findViewById(R.id.textViewSettingsResultsAmount);
        numberOfResultsTextView.setText(Integer.toString(BluetoothResultsController.selectAll(getApplicationContext()).size()));

        setSpeedButton = (Button) findViewById(R.id.buttonSettingsGps);
        setSpeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                launchGPS();
            }
        });

    }

    /**
     * Przechodzi do aktywności ustawiania prędkości
     */
    private void launchGPS( )
    {
        Intent intent = new Intent(this, UserSpeed.class);
        startActivity(intent);
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