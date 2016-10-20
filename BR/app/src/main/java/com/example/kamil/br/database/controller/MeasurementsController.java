package com.example.kamil.br.database.controller;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.kamil.br.activities.MainActivity;
import com.example.kamil.br.database.DBHandler;
import com.example.kamil.br.database.model.Measurements;

/**
 * Created by Kamil on 2016-09-21.
 */
public class MeasurementsController {


    public static String createTable() {
        return "CREATE TABLE " + Measurements.TABLE + "(" +
                Measurements.ID_MEASUREMENTS + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Measurements.NAME + " TEXT," +
                Measurements.ID_ROOMS + " INTEGER )";
    }

    public void insert(Measurements measurement)
    {
        SQLiteDatabase db = new DBHandler(MainActivity.getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Measurements.NAME, measurement.getName());
        values.put(Measurements.ID_ROOMS, 1);//magicznie wkladamy
        db.insert(Measurements.TABLE, null, values);
        db.close(); // Closing database connection
    }
}
