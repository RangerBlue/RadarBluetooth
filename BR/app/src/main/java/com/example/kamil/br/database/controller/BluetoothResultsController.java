package com.example.kamil.br.database.controller;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.kamil.br.activities.MainActivity;
import com.example.kamil.br.database.DBHandler;
import com.example.kamil.br.database.model.BluetoothResults;

/**
 * Created by Kamil on 2016-09-21.
 */
public class BluetoothResultsController {



    public static String createTable() {
        return "CREATE TABLE " + BluetoothResults.TABLE + "(" +
                BluetoothResults.ID_BLUETOOTHRESULTS + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BluetoothResults.NAME + " TEXT," +
                BluetoothResults.ADDRESS + " TEXT," +
                BluetoothResults.RSSI + " INTEGER," +
                BluetoothResults.TIME + "TEXT," +
                BluetoothResults.ID_PATHDATA + "INTEGER )";
    }

    public void insert(BluetoothResults bluetoothResult)
    {
        SQLiteDatabase db = new DBHandler(MainActivity.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BluetoothResults.NAME, bluetoothResult.getName());
        values.put(BluetoothResults.ADDRESS, bluetoothResult.getName());
        values.put(BluetoothResults.RSSI, bluetoothResult.getRssi());
        values.put(BluetoothResults.TIME, bluetoothResult.getTime());
        values.put(BluetoothResults.ID_PATHDATA, 1);//tu bede musiał ustawić jakos magicznie
        db.insert(BluetoothResults.TABLE, null, values);
        db.close(); // Closing database connection
    }
}
