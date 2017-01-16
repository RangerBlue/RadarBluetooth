package com.example.kamil.br.database.model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Obiekt rezultatu zwróconego przez bluetooth
 * Created by Kamil
 */
public class BluetoothResults
{
    //nazwa tabeli
    public static final String TABLE = "BluetoothResults";

    public static final String ID_BLUETOOTHRESULTS = "idbluetoothresults";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String RSSI = "rssi";
    public static final String TIME = "time";
    public static final String EGDENUMBER = "edgenumber";
    public static final String ID_MEASUREMENTS = "idmeasurements";
    public static final String ID_ROOMS = "idrooms";


    /**
     * identyfikator
     */
    private int idBluetoothResults;

    /**
     * nazwa urządzenia
     */
    private String name;

    /**
     * adres urządzenia
     */
    private String address;

    /**
     * rssi urządzenia
     */
    private int rssi;

    /**
     * godzina znalezienia urządzenia
     */
    private long time;

    /**
     * numer krawędzi
     */
    private int edgeNumber;

    /**
     * numer pomiaru
     */
    private int idMeasurements;

    /**
     * numer pokoju
     */
    private int idRooms;

    public BluetoothResults(long time)
    {
        this.name = "NULL";
        this.address = "NULL";
        this.rssi = 0;
        this.time = time;
    }

    public BluetoothResults()
    {}

    public int getEdgeNumber() {
        return edgeNumber;
    }

    public void setEdgeNumber(int edgeNumber) {
        this.edgeNumber = edgeNumber;
    }

    public int getIdMeasurements() {
        return idMeasurements;
    }

    public void setIdMeasurements(int idMeasurements) {
        this.idMeasurements = idMeasurements;
    }

    public int getIdBluetoothResults() {
        return idBluetoothResults;
    }

    public void setIdBluetoothResults(int idBluetoothResults) {
        this.idBluetoothResults = idBluetoothResults;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIdRooms() {
        return idRooms;
    }

    public void setIdRooms(int idRooms) {
        this.idRooms = idRooms;
    }


    /**
     * Zwraca listę zawierającą tylko dane z pomiaru bluetooth tylko jednej krawędzi
     * gdy znajdzie już określone elementy nie przeszukuje dalej
     * @param edgeNumber wybrana krawędź
     * @param listToSlice lista zawierająca rekordy z kilku krawędzi
     * @return lista z danymi wybranej krawędzi
     */
    public static ArrayList<BluetoothResults> getSublistWhereEdgeNumbers(int edgeNumber, ArrayList<BluetoothResults> listToSlice)
    {
        ArrayList<BluetoothResults> slicedList ;
        int indexOfFirstElement=-1;
        int indexOfLastElement=-1;


        for(int i=0 ; i<listToSlice.size(); i++)
        {
            if((listToSlice.get(i).getEdgeNumber() == edgeNumber) )
            {
                if(indexOfFirstElement == -1)
                {
                    indexOfFirstElement = i;
                }
                indexOfLastElement = i;
            }

        }

        if(indexOfFirstElement != indexOfLastElement)
        {
            slicedList =  new ArrayList<>(listToSlice.subList(indexOfFirstElement, indexOfLastElement+1));
            return slicedList;
        }
        else
        {
            slicedList = new ArrayList<>();
            slicedList.add(listToSlice.get(indexOfFirstElement));
            return slicedList;
        }

    }


}