package com.example.kamil.br.database.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.example.kamil.br.activities.MainActivity;
import com.example.kamil.br.database.DBHandler;
import com.example.kamil.br.database.model.BluetoothResults;
import com.example.kamil.br.database.model.Rooms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamil on 2016-09-21.
 */
public class RoomsController {

    private final String TAG = RoomsController.class.getSimpleName();

    public static String createTable() {
        return "CREATE TABLE " + Rooms.TABLE + "(" +
                Rooms.ID_ROOMS + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Rooms.NAME + " TEXT )";
    }

    public void insert(Rooms room, Context context)
    {
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Rooms.NAME, room.getName());
        db.insert(Rooms.TABLE, null, values);
        db.close(); // Closing database connection
        Log.d("Insert record: ", room.TABLE+" "+ room.getName());
    }

    /**
     * Usuwa pokój
     * @param @param idRoom id pokoju do usunięcia
     * @param @context kontekst aplikacji
     */
    public void deleteWhereIdRooms(int idRoom, Context context)
    {
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String delete =
                " DELETE FROM " + Rooms.TABLE +
                " WHERE Rooms." + Rooms.ID_ROOMS+"="+Integer.toString(idRoom);
        db.execSQL(delete);
        Log.d(TAG, "deleted record from rooms");
        db.close(); // Closing database connection
    }

    /**
     * Usuwa pokój wraz ze wszystkimi kluczami obcymi w innych tabelach
     * @param @param idRoom id pokoju do usunięcia
     * @param @context kontekst aplikacji
     */
    public void deleteRoomAndAllDependences(int idRoom, Context context)
    {
        deleteWhereIdRooms(idRoom, context);
        PathDataController.deleteWhereIdRooms(idRoom, context);
        BluetoothResultsController.deleteWhereIdRooms(idRoom, context);
        MeasurementsController.deleteWhereIdRooms(idRoom, context);
    }

    public List<Rooms> selectAll(Context context)
    {
        List<Rooms> rooms = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                "Rooms." + Rooms.ID_ROOMS +
                ", Rooms." + Rooms.NAME +
                " FROM " + Rooms.TABLE;

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Rooms room= new Rooms();
                room.setIdRooms(cursor.getInt(cursor.getColumnIndex(Rooms.ID_ROOMS)));
                room.setName(cursor.getString(cursor.getColumnIndex(Rooms.NAME)));
                rooms.add(room);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return rooms;
    }

    //debug only
    public static void printAllTableToLog(ArrayList<Rooms> list)
    {
        //wypisanie rekordów z PathData nienaruszonych
        for ( Rooms element : list )
        {
            Log.d("Tabela Rooms: ",
                    "\t|ID| "+String.valueOf(element.getIdRooms()) +
                            "\t|Name| "+ String.valueOf(element.getName())
            ) ;
        }
    }
}
