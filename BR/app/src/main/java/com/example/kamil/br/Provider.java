package com.example.kamil.br;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by kamil on 18.04.16.
 */
public class Provider extends ContentProvider {
    private static DBHandler handler;
    private final static String IDENTYFIKATOR = "com.BR.Provider";
    public static final Uri URI_ZAWARTOSC = Uri.parse("content://"+IDENTYFIKATOR+"/"+handler.TABLE_DB);
    private final static int CALA_TABELA=1;
    private final static int WYBRANY_WIERSZ=2;
    private static final UriMatcher sDopasowanieUri = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        sDopasowanieUri.addURI(IDENTYFIKATOR, handler.TABLE_DB, CALA_TABELA);
        sDopasowanieUri.addURI(IDENTYFIKATOR, handler.TABLE_DB+"/#", WYBRANY_WIERSZ);
    }




    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        int typUri = sDopasowanieUri.match(uri);
        SQLiteDatabase baza = handler.getWritableDatabase();

        long idDodanego = 0;
        switch(typUri){
            case CALA_TABELA :
                idDodanego = baza.insert(handler.TABLE_DB, null, values);
                break;
            default : throw new IllegalArgumentException("Nieznane Uri : "+uri);
        }

        getContext().getContentResolver().notifyChange(	uri, null);
        return Uri.parse(handler.TABLE_DB+"/"+idDodanego);

    }

    @Override
    public int delete(Uri uri, String selection,
                      String[] selectionArgs) {
        int typUri = sDopasowanieUri.match(uri);
        SQLiteDatabase baza = handler.getWritableDatabase();
        int liczbaUsunietych = 0;
        switch (typUri) {
            case CALA_TABELA:
                liczbaUsunietych = baza.delete(PomocnikBD.NAZWA_TABELI,
                        selection, //WHERE
                        selectionArgs); //argumenty

                break;
            case WYBRANY_WIERSZ: //modyfikowane jest WHERE
                liczbaUsunietych = baza.delete(PomocnikBD.NAZWA_TABELI,
                        dodajIdDoSelekcji(selection, uri), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI: " + uri);
        }
//powiadomienie o zmianie danych
        getContext().getContentResolver().notifyChange(uri, null);
        return liczbaUsunietych;
    }

    @Override
    public boolean onCreate() {
        handler = new DBHandler((getContext()));

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, //dostawca tresci
                        String[] selectionArgs, String sortOrder) {

        int typUri = sDopasowanieUri.match(uri);
        SQLiteDatabase baza = handler.getWritableDatabase();
        Cursor kursorTel = null;
        switch(typUri){
            case CALA_TABELA:
                kursorTel = baza.query(false, handler.TABLE_DB, projection, selection, selectionArgs, null, null, sortOrder, null, null);
                break;
            case WYBRANY_WIERSZ:
                kursorTel = baza.query(false, handler.TABLE_DB, projection, dodajIdDoSelekcji(selection,uri), null, null, sortOrder, null, null);
                break;
            default:
                throw new IllegalArgumentException("Nieznane uri "+uri);
        }
        kursorTel.setNotificationUri(getContext().getContentResolver(), uri);

        return kursorTel;
    }

    private String dodajIdDoSelekcji(String selection, Uri uri) {
        if(selection !=null && !selection.equals(""))
            selection = selection + " and "+PomocnikBD.ID+"="+uri.getLastPathSegment();
        else
            selection = handler.ID+"="+uri.getLastPathSegment();
        return selection;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int typUri = sDopasowanieUri.match(uri);
        SQLiteDatabase baza = handler.getWritableDatabase();
        int liczbaZaktualizowanych = 0;

        switch(typUri){
            case CALA_TABELA:
                liczbaZaktualizowanych = baza.update(handler.TABLE_DB, values, selection, selectionArgs);
                break;

            case WYBRANY_WIERSZ:
                liczbaZaktualizowanych = baza.update(handler.TABLE_DB, values, dodajIdDoSelekcji(selection, uri), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI: "+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return liczbaZaktualizowanych;
    }




}