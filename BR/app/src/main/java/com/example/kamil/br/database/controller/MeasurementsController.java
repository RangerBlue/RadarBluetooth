package com.example.kamil.br.database.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.kamil.br.activities.MainActivity;
import com.example.kamil.br.database.DBHandler;
import com.example.kamil.br.database.model.Measurements;
import com.example.kamil.br.database.model.PathData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

    public void insert(Measurements measurement, Context context)
    {
        SQLiteDatabase db =new DBHandler(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Measurements.NAME, measurement.getName());
        values.put(Measurements.ID_ROOMS, measurement.getIdRooms());
        db.insert(Measurements.TABLE, null, values);
        db.close(); // Closing database connection
    }

    public List<Measurements> selectAll(Context context)
    {
        List<Measurements> measurement = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                        "Measurements." + Measurements.ID_MEASUREMENTS +
                        ", Measurements." + Measurements.NAME +
                        ", Measurements." + Measurements.ID_ROOMS +
                        " FROM " + Measurements.TABLE;

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Measurements measurement_= new Measurements();
                measurement_.setIdMeasurements(cursor.getInt(cursor.getColumnIndex(Measurements.ID_MEASUREMENTS)));
                measurement_.setName(cursor.getString(cursor.getColumnIndex(Measurements.NAME)));
                measurement_.setIdRooms(cursor.getInt(cursor.getColumnIndex(Measurements.ID_ROOMS)));
                measurement.add(measurement_);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return measurement;
    }

    public List<Measurements> selectMeasurementWhereIdRoom(Context context, int id)
    {
        List<Measurements> measurement = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                        "Measurements." + Measurements.ID_MEASUREMENTS +
                        ", Measurements." + Measurements.NAME +
                        ", Measurements." + Measurements.ID_ROOMS +
                        " FROM " + Measurements.TABLE +
                        " WHERE Measurements." + Measurements.ID_ROOMS+"="+Integer.toString(id);

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Measurements measurement_= new Measurements();
                measurement_.setIdMeasurements(cursor.getInt(cursor.getColumnIndex(Measurements.ID_MEASUREMENTS)));
                measurement_.setName(cursor.getString(cursor.getColumnIndex(Measurements.NAME)));
                measurement_.setIdRooms(cursor.getInt(cursor.getColumnIndex(Measurements.ID_ROOMS)));
                measurement.add(measurement_);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return measurement;
    }

    public Measurements getLastRecord(Context context)
    {
        List<Measurements> measurement = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT *" +
                        " FROM " + Measurements.TABLE+
                        " WHERE Measurements."+ Measurements.ID_MEASUREMENTS+"="+
                        "(SELECT MAX(Measurements."+Measurements.ID_MEASUREMENTS+") FROM "+Measurements.TABLE+")";


        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Measurements measurement_= new Measurements();
                measurement_.setIdMeasurements(cursor.getInt(cursor.getColumnIndex(Measurements.ID_MEASUREMENTS)));
                measurement_.setName(cursor.getString(cursor.getColumnIndex(Measurements.NAME)));
                measurement_.setIdRooms(cursor.getInt(cursor.getColumnIndex(Measurements.ID_ROOMS)));
                measurement.add(measurement_);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return measurement.get(0);
    }

    //debug only
    public static void printAllTableToLog(ArrayList<Measurements> list)
    {
        for ( Measurements element : list )
        {
            Log.d("Tabela PathData: ",
                    "\t|ID| "+String.valueOf(element.getIdMeasurements()) +
                            "\t|Name| "+ element.getName() +
                            "\t|IdRooms| "+ element.getIdRooms()
            ) ;
        }
    }
}
