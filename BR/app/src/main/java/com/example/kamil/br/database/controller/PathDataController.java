package com.example.kamil.br.database.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Path;
import android.util.Log;

import com.example.kamil.br.activities.MainActivity;
import com.example.kamil.br.database.DBHandler;
import com.example.kamil.br.database.model.PathData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamil on 2016-09-21.
 */
public class PathDataController {

    public static String TAG = PathDataController.class.getSimpleName();

    public static String createTable() {
        return "CREATE TABLE " + PathData.TABLE + "(" +
                PathData.ID_PATHDATA + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PathData.A + " REAL," +
                PathData.B + " REAL," +
                PathData.X + " REAL," +
                PathData.LINEAR + " INTEGER," +
                PathData.P1 + " REAL," +
                PathData.P2 + " REAL," +
                PathData.EDGENUMBER + " INTEGER," +
                PathData.ID_ROOMS + " INTEGER )";
    }

    public void insert(PathData pathadata, Context context)
    {
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PathData.A, pathadata.getA());
        values.put(PathData.B, pathadata.getB());
        values.put(PathData.X, pathadata.getX());
        values.put(PathData.LINEAR, (pathadata.getIsIfLinear()));
        values.put(PathData.P1, pathadata.getP1());
        values.put(PathData.P2, pathadata.getP2());
        values.put(PathData.EDGENUMBER, pathadata.getEdgeNumber());
        values.put(PathData.ID_ROOMS, pathadata.getIdRooms());
        db.insert(PathData.TABLE, null, values);
        db.close(); // Closing database connection
    }

    public void update(PathData pathdata, Context context)
    {
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PathData.A, pathdata.getA());
        values.put(PathData.B, pathdata.getB());
        values.put(PathData.X, pathdata.getX());
        values.put(PathData.LINEAR, (pathdata.getIsIfLinear()));
        values.put(PathData.P1, pathdata.getP1());
        values.put(PathData.P2, pathdata.getP2());

        String where = " PathData." + PathData.ID_PATHDATA+"="+Integer.toString(pathdata.getIdPathData());

        db.update(PathData.TABLE, values, where,  null);
        Log.d(TAG, " Updated row number: "+pathdata.getIdPathData());
    }

    public List<PathData> selectAll(Context context)
    {
        List<PathData> pathData = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                        "PathData." + PathData.ID_PATHDATA +
                        ", PathData." + PathData.A +
                        ", PathData." + PathData.B +
                        ", PathData." + PathData.X +
                        ", PathData." + PathData.LINEAR +
                        ", PathData." + PathData.P1 +
                        ", PathData." + PathData.P2 +
                        ", PathData." + PathData.EDGENUMBER +
                        ", PathData." + PathData.ID_ROOMS +
                        " FROM " + PathData.TABLE;

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PathData pathData_= new PathData();
                pathData_.setIdPathData(cursor.getInt(cursor.getColumnIndex(PathData.ID_PATHDATA)));
                pathData_.setA(cursor.getFloat(cursor.getColumnIndex(PathData.A)));
                pathData_.setB(cursor.getFloat(cursor.getColumnIndex(PathData.B)));
                pathData_.setX(cursor.getFloat(cursor.getColumnIndex(PathData.X)));
                pathData_.setIfLinear(cursor.getInt(cursor.getColumnIndex(PathData.LINEAR)));
                pathData_.setP1(cursor.getFloat(cursor.getColumnIndex(PathData.P1)));
                pathData_.setP2(cursor.getFloat(cursor.getColumnIndex(PathData.P2)));
                pathData_.setEdgeNumber(cursor.getInt(cursor.getColumnIndex(PathData.EDGENUMBER)));
                pathData_.setIdRooms(cursor.getInt(cursor.getColumnIndex(PathData.ID_ROOMS)));
                pathData.add(pathData_);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return pathData;
    }

    public static List<PathData> selectPathDataWhereId(Context context, int id)
    {
        List<PathData> pathData = new ArrayList<>();
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String selectQuery =
                " SELECT " +
                        "PathData." + PathData.ID_PATHDATA +
                        ", PathData." + PathData.A +
                        ", PathData." + PathData.B +
                        ", PathData." + PathData.X +
                        ", PathData." + PathData.LINEAR +
                        ", PathData." + PathData.P1 +
                        ", PathData." + PathData.P2 +
                        ", PathData." + PathData.EDGENUMBER +
                        ", PathData." + PathData.ID_ROOMS +
                        " FROM " + PathData.TABLE +
                        " WHERE PathData." + PathData.ID_ROOMS+"="+Integer.toString(id);

        // pętla po wszystkich wierszach i zapis do listy
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PathData pathData_= new PathData();
                pathData_.setIdPathData(cursor.getInt(cursor.getColumnIndex(PathData.ID_PATHDATA)));
                pathData_.setA(cursor.getFloat(cursor.getColumnIndex(PathData.A)));
                pathData_.setB(cursor.getFloat(cursor.getColumnIndex(PathData.B)));
                pathData_.setX(cursor.getFloat(cursor.getColumnIndex(PathData.X)));
                pathData_.setIfLinear(cursor.getInt(cursor.getColumnIndex(PathData.LINEAR)));
                pathData_.setP1(cursor.getFloat(cursor.getColumnIndex(PathData.P1)));
                pathData_.setP2(cursor.getFloat(cursor.getColumnIndex(PathData.P2)));
                pathData_.setEdgeNumber(cursor.getInt(cursor.getColumnIndex(PathData.EDGENUMBER)));
                pathData_.setIdRooms(cursor.getInt(cursor.getColumnIndex(PathData.ID_ROOMS)));
                pathData.add(pathData_);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return pathData;
    }

    /**
     * Usuwa ścieżkę danego pokoju
     * @param idRoom id pokoju do usunięcia
     * @param context kontekst aplikacji
     */
    public static void deleteWhereIdRooms(int idRoom, Context context)
    {
        SQLiteDatabase db = new DBHandler(context).getWritableDatabase();
        String delete =
                " DELETE FROM " + PathData.TABLE +
                        " WHERE PathData." + PathData.ID_ROOMS+"="+Integer.toString(idRoom);
        db.execSQL(delete);
        Log.d(TAG, "deleted record in pathdata");
        db.close(); // Closing database connection
    }

    //debug only
    public static void printAllTableToLog(ArrayList<PathData> list)
    {
        //wypisanie rekordów z PathData nienaruszonych
        DecimalFormat format = new DecimalFormat();
        format.setMinimumFractionDigits(2);
        for ( PathData element : list )
        {
            Log.d("Tabela PathData: ",
                    "\t|ID| "+String.valueOf(element.getIdPathData()) +
                            "\t|A| "+ format.format(element.getA()) +
                            "\t|B| "+ format.format(element.getB()) +
                            "\t|X| "+ format.format(element.getX()) +
                            "\t|Linear| "+ element.getIsIfLinear() +
                            "\t|P1| "+ format.format(element.getP1()) +
                            "\t|P2| "+ format.format(element.getP2()) +
                            "\t|EdgeNumbers| "+ element.getEdgeNumber() +
                            "\t|IdRooms| "+ element.getIdRooms()
            ) ;
        }
    }
}

