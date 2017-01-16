package com.example.kamil.br.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.kamil.br.database.controller.BluetoothResultsController;
import com.example.kamil.br.database.controller.MeasurementsController;
import com.example.kamil.br.database.controller.PathDataController;
import com.example.kamil.br.database.controller.RoomsController;
import com.example.kamil.br.database.controller.WalkRatioController;
import com.example.kamil.br.database.model.BluetoothResults;
import com.example.kamil.br.database.model.Measurements;
import com.example.kamil.br.database.model.PathData;
import com.example.kamil.br.database.model.Rooms;
import com.example.kamil.br.database.model.WalkRatio;

/**
 * Obiekt do oblsugi bazy danych
 * Created by kamil on 05.05.16.
 */
public class DBHandler extends SQLiteOpenHelper {


    /**
     * wersja bazy
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * nazwa bazy
     */
    private static final String DATABASE_NAME = "BazaBluetooth2";



    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //tworzenie tabel
        db.execSQL(RoomsController.createTable()); //Log.d("Created database: ", Rooms.TABLE);
        db.execSQL(MeasurementsController.createTable()); //Log.d("Created database: ", Measurements.TABLE);
        db.execSQL(PathDataController.createTable()); //Log.d("Created database: ", PathData.TABLE);
        db.execSQL(BluetoothResultsController.createTable()); //Log.d("Created database: ", BluetoothResults.TABLE);
        db.execSQL(WalkRatioController.createTable()); //Log.d("Created database: ", WalkRatio.TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // zniszcz jesli istnieje
        db.execSQL("DROP TABLE IF EXISTS " + Rooms.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Measurements.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PathData.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + BluetoothResults.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + WalkRatio.TABLE);
        // utwórz ponownie
        onCreate(db);
    }


}