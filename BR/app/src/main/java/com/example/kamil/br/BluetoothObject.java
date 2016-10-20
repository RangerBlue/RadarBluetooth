package com.example.kamil.br;

import android.os.Parcel;
import android.os.Parcelable;

public class BluetoothObject implements Parcelable
{
    private String bluetoothName;
    private String bluetoothAddress;
    private int bluetoothRSSI;

    public String getBluetoothName() {
        return bluetoothName;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }

    public int getBluetoothRssi() {
        return bluetoothRSSI;
    }

    public void setBluetoothRssi(int bluetoothRssi) {
        this.bluetoothRSSI = bluetoothRssi;
    }

    // Parcelable stuff
    public BluetoothObject()
    {}  //empty constructor

    public BluetoothObject(Parcel in)
    {
        super();
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in)
    {
        bluetoothName = in.readString();
    }

    public static final Parcelable.Creator<BluetoothObject> CREATOR = new Parcelable.Creator<BluetoothObject>()
    {
        public BluetoothObject createFromParcel(Parcel in) {
            return new BluetoothObject(in);
        }

        public BluetoothObject[] newArray(int size) {

            return new BluetoothObject[size];
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeString(bluetoothName);
    }


}