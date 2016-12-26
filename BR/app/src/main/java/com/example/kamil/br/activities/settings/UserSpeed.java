package com.example.kamil.br.activities.settings;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamil.br.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Aktywność służąca do pomiaru średniej prędkości użytkownika z jaką będzie się
 * przemieszczał mierząc pomieszczenia
 */
public class UserSpeed extends AppCompatActivity {

    private static String TAG = UserSpeed.class.getSimpleName();
    /**
     * pole tekstowe, w którym wyświetlana jest prędkość w metrach na sekundę
     */
    private TextView textViewSpeedM;

    /**
     * pole tekstowe, w którym wyświetlana jest prędkość w kilometrach na sekundę
     */
    private TextView textViewSpeedKm;

    /**
     * pole tekstowe, w którym wyświetlany jest wynik pomiaru w metrach na sekundę
     */
    private TextView textViewResult;

    /**
     * lista z prędkościami, dodawanymi przy zmianie lokacji
     */
    private ArrayList<Float> values;

    /**
     * przycisk rozpoczynający pomiar
     */
    private ImageButton startButton;

    /**
     * przycisk zapisujący wynik pomiaru
     */
    private ImageButton saveButton;

    /**
     * pasek postępu, odmierzający upływ czasu (30s)
     */
    private ProgressBar progressBar;

    /**
     * zmienna logiczna, wskazująca czy czas jest aktualnie odmierzany
     */
    private boolean isClockIsTicking = false;

    /**
     * średnia wartość prędkości
     */
    private Float averageValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_speed);

        values = new ArrayList<>();

        textViewSpeedM = (TextView) findViewById(R.id.speed);
        textViewSpeedKm = (TextView) findViewById(R.id.speedKm);
        textViewResult = (TextView) findViewById(R.id.textViewResult);
        progressBar = (ProgressBar) findViewById(R.id.progressBarSpeed);
        saveButton = (ImageButton) findViewById(R.id.buttonSpeedSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(), R.string.your_speed +Float.toString(averageValue) + " m/s", Toast.LENGTH_SHORT).show();
                saveSpeed(averageValue);
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
                isClockIsTicking = true;
            }
        });

        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location)
            {

                DecimalFormat format = new DecimalFormat();
                format.setMinimumFractionDigits(2);
                format.format(location.getSpeed());
                textViewSpeedM.setText(format.format(location.getSpeed()));
                textViewSpeedKm.setText(format.format(meterPerSecondsToKilometersPerHour(location.getSpeed())));

                if (isClockIsTicking) {
                    addValue(location.getSpeed(), values);
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


    /**
     * Klasa obsługująca progressBar, odlicza okres czasu 30 s, w którym
     * użytkownik dokonuje pomiaru prędkości
     */
    private class TimerTask extends AsyncTask<Long, Integer, Void>
    {

        @Override
        protected Void doInBackground(Long... longs) {
            //czas początkowy
            long time = longs[0];
            //początkowa róznica
            long difference = 0;
            //zakres 30s
            float range = 30000;
            float result;
            while ( difference < range )
            {
                difference = System.currentTimeMillis() - time;
                result = (int) ((difference/range)*100);
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
        protected void onPostExecute(Void aVoid)
        {
            averageValue = getAverage(values);
            textViewResult.setText(averageValue.toString());
            saveButton.setVisibility(View.VISIBLE);
            super.onPostExecute(aVoid);
        }
    }

    /**
     * Dodaje wartość do listy, jeśli nie jest równa zera
     * @param value wartość, którą chcemy dodać
     * @param test lista, do której chcemy dodać wartość
     */
    private void addValue( Float value, ArrayList<Float> test)
    {
        if( value != 0)
        test.add(value);
    }

    /**
     * Zwraca średnią wartość obiektów listy
     * @param list lista z obiektami FLoat
     * @return śrenia wartość
     */
    private Float getAverage(ArrayList<Float> list)
    {
        int i = 0;
        Float sum = 0f;
        for( Float item : list)
        {
            sum += item;
            i++;
        }

        return sum/i;
    }

    /**
     * Zamienia prędkość z m/s na km/h
     * @param velocity prędkość w m/s
     * @return prędkość w km/h
     */
    private float meterPerSecondsToKilometersPerHour(float velocity)
    {
        return velocity/3.6f;
    }

    /**
     * Zapisuje wartość prędkość w sharedPreferences pod nazwą velocity
     * @param speed wartość, która chcemy zapisać
     */
    private void saveSpeed(float speed)
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("options", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat("velocity", speed);
        editor.commit();
    }
}
