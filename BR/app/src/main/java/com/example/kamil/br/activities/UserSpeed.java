package com.example.kamil.br.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kamil.br.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class UserSpeed extends AppCompatActivity {

    TextView textViewSpeed;
    TextView textViewSpeedKm;
    TextView textViewWynik;
    ArrayList<Float> test;
    Button wynik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_speed);

        test = new ArrayList<>();

        textViewSpeed = (TextView) findViewById(R.id.speed);
        textViewSpeedKm = (TextView) findViewById(R.id.speedKm);
        textViewWynik = (TextView) findViewById(R.id.textViewResult);

        wynik = (Button) findViewById(R.id.buttonSpeedStartStop);
        wynik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float result = getAverage(test);
                textViewWynik.setText(result.toString());
            }
        });

        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                DecimalFormat format = new DecimalFormat();
                format.setMinimumFractionDigits(2);
                format.format(location.getSpeed());
                textViewSpeed.setText(format.format(location.getSpeed()));
                textViewSpeedKm.setText(format.format(meterPerSecondsToKilometersPerHour(location.getSpeed())));
                addValue(location.getSpeed());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

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
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


    }

    private void addValue( Float value)
    {
        test.add(value);
    }

    private Float getAverage(ArrayList<Float> list)
    {
        Float sum = 0f;
        for( Float item : list)
        {
            sum += item;
        }

        return sum/list.size();
    }

    private float meterPerSecondsToKilometersPerHour(float velocity)
    {
        return velocity/3.6f;
    }
}
