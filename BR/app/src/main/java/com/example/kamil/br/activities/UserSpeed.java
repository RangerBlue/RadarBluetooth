package com.example.kamil.br.activities;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kamil.br.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;

public class UserSpeed extends AppCompatActivity {

    private TextView textViewSpeed;
    private TextView textViewSpeedKm;
    private TextView textViewResult;
    private ArrayList<Float> test;
    private ImageButton startButton;
    private ImageButton saveButton;
    private ProgressBar progressBar;
    private boolean isClockIsTicking = false;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_speed);

        test = new ArrayList<>();

        textViewSpeed = (TextView) findViewById(R.id.speed);
        textViewSpeedKm = (TextView) findViewById(R.id.speedKm);
        textViewResult = (TextView) findViewById(R.id.textViewResult);
        progressBar = (ProgressBar) findViewById(R.id.progressBarSpeed);
        saveButton = (ImageButton) findViewById(R.id.buttonSpeedSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSpeed(3.4f);
            }
        });
        saveButton.setVisibility(View.INVISIBLE);
       // progressBar.setVisibility(View.INVISIBLE);
        startButton = (ImageButton) findViewById(R.id.buttonSpeedStartStop);
        //start.setClickable(false);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimerTask().execute(System.currentTimeMillis());
            }
        });

        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (isClockIsTicking) {
                    DecimalFormat format = new DecimalFormat();
                    format.setMinimumFractionDigits(2);
                    format.format(location.getSpeed());
                    textViewSpeed.setText(format.format(location.getSpeed()));
                    textViewSpeedKm.setText(format.format(meterPerSecondsToKilometersPerHour(location.getSpeed())));
                    addValue(location.getSpeed());
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                startButton.setClickable(true);
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            startButton.setImageResource(R.drawable.switch_on_off_icon);
            progressBar.setVisibility(View.VISIBLE);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);



    }

    private class TimerTask extends AsyncTask<Long, Integer, Void>
    {

        @Override
        protected Void doInBackground(Long... longs) {
            long time = longs[0];
            long difference = 0;
            float range = 30000;
            float result;
            while ( difference < range )
            {
                difference = System.currentTimeMillis() - time;
                Log.d("diff", String.valueOf(difference));
                result = (int) ((difference/range)*100);
                Log.d("alala", String.valueOf(result));
                publishProgress((int)result);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            textViewResult.setText(getAverage(test).toString());
            saveButton.setVisibility(View.VISIBLE);
            super.onPostExecute(aVoid);
        }
    }

    private void addValue( Float value)
    {
        test.add(value);
    }

    private Float getAverage(ArrayList<Float> list)
    {
        int i = 0;
        Float sum = 0f;
        for( Float item : list)
        {
            if( item != 0)
            {
                sum += item;
                i++;
            }

        }

        return sum/i;
    }

    private float meterPerSecondsToKilometersPerHour(float velocity)
    {
        return velocity/3.6f;
    }

    private void saveSpeed(float speed)
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("options", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat("velocity", speed);
        editor.commit();
    }
}
