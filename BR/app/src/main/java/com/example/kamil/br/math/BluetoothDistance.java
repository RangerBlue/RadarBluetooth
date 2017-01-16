package com.example.kamil.br.math;

import android.bluetooth.BluetoothDevice;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Klasa do obliczania odległości między szukanymi urządzeniami
 * RSSI to skrót od ang. Received Signal Strength Indication. Jest to wskaźnik mocy (nie jakości) odbieranego sygnału radiowego.
 * RSSI jest w dBm
 * Created by Kamil on 2016-10-09.
 */
public class BluetoothDistance
{

    public static String TAG = BluetoothDevice.class.getSimpleName();


    /**
     * Obliczanie dystansu na podstawie otrzymanej wielkosci RSSI
     * RSSI = -(10*n*log10(d)-A)
     * n- wykładnik utraty sygnału
     * d - dystans
     * A-wartość RSSI w referencyjnym dystansie(tu będzie 1m prawdopodobnie)
     * @param P wielkość rssi
     */
    public static float getDistance(int P, float type)
    {
        int A = -74;
        float n = 1.7f;
        float d = (float) (Math.pow(10, ((A-P)) / (10 * n)));
        Log.d(TAG, "Odległość:  "+d);
        Log.d(TAG,"rssi: "+P);
        return d;

    }

    /**
     * zwraca unikalne losowe kolory dla ilości podanych elementów
     * @param numberOfDevices liczba elementów
     * @return lista kolorów
     */
    public static ArrayList<Integer> getColorsForDevices(int numberOfDevices)
    {
        Random random = new Random();
        ArrayList<Integer> returnList = new ArrayList<>();
        int r, g, b;

        for(int i = 0 ; i < numberOfDevices ; i++)
        {
            r = random.nextInt(255);
            g = random.nextInt(255);
            b = random.nextInt(255);
            int color = Color.rgb(r,g,b);
            returnList.add(color);
        }

        return returnList;
    }
}
