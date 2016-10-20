package com.example.kamil.br.database.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BluetoothResults implements Parcelable
{
    //nazwa tabeli
    public static final String TABLE = "BluetoothResults";

    public static final String ID_BLUETOOTHRESULTS = "idbluetoothresults";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String RSSI = "rssi";
    public static final String TIME = "time";
    public static final String ID_PATHDATA = "idpathdata";


    //nazwy pól
    private int idBluetoothResults;
    private String name;
    private String address;
    private int rssi;
    private long time;

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



    // Parcelable stuff
    public BluetoothResults()
    {}  //empty constructor

    public BluetoothResults(Parcel in)
    {
        super();
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in)
    {
        name = in.readString();
    }

    public static final Parcelable.Creator<BluetoothResults> CREATOR = new Parcelable.Creator<BluetoothResults>()
    {
        public BluetoothResults createFromParcel(Parcel in) {
            return new BluetoothResults(in);
        }

        public BluetoothResults[] newArray(int size) {

            return new BluetoothResults[size];
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeString(name);
    }


}