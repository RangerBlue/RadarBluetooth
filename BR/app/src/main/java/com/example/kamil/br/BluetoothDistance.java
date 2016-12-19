package com.example.kamil.br;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

/**
 * Klasa do obliczania odległości między szukanymi urządzeniami
 * RSSI to skrót od ang. Received Signal Strength Indication. Jest to wskaźnik mocy (nie jakości) odbieranego sygnału radiowego.
 * RSSI jest w dBm
 * Created by Kamil on 2016-10-09.
 * @link http://evilrobotfactory.blogspot.com/2014/05/estimating-distance-from-rssi-values.html
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
     * @param rssi wielkość rssi
     */
    public static float getDistance(int rssi, float type)
    {
        Float n;
        if( type != -1 )
            n = type;
        else
            n = 2.0f;
        
        Log.d(TAG, "Odległość:  "+n);
        int A = -77; // ustawione doświadczalnie                        xD
        float d = (float) (Math.pow(10, (((-1*(rssi-A))) / (10 * n))));
        Log.d(TAG, "Odległość:  "+d);
        Log.d(TAG,"rssi: "+rssi);
        return d;

    }
}
